// movies.js - Fixed with Enhanced Movie Modal UI
let movies = [];
let filteredMovies = [];
let currentMovieIndex = 0;

// Initialize movies functionality
document.addEventListener('DOMContentLoaded', function() {
    initializeMovies();
});

async function initializeMovies() {
    await loadMovies();
    setupMovieEventListeners();
    initializeMovieSearch();
}

function setupMovieEventListeners() {
    // Movie card interactions
    document.addEventListener('click', function(e) {
        if (e.target.closest('.btn-watchlist')) {
            e.preventDefault();
            e.stopPropagation();
            const movieId = e.target.closest('.movie-card').dataset.movieId;
            const button = e.target.closest('.btn-watchlist');
            toggleWatchlist(movieId, button);
        }
        
        if (e.target.closest('.btn-info')) {
            e.preventDefault();
            e.stopPropagation();
            const movieId = e.target.closest('.movie-card').dataset.movieId;
            openMovieModal(movieId);
        }
    });

    // Quick view on hover (desktop only)
    if (window.innerWidth > 768) {
        setupQuickView();
    }

    // Modal close button event listener
    const modalClose = document.getElementById('modalClose');
    if (modalClose) {
        modalClose.addEventListener('click', closeModal);
    }
}

function setupQuickView() {
    let quickViewTimer;
    
    document.addEventListener('mouseover', function(e) {
        const movieCard = e.target.closest('.movie-card');
        if (movieCard && !movieCard.classList.contains('show-quick-view')) {
            clearTimeout(quickViewTimer);
            quickViewTimer = setTimeout(() => {
                showQuickView(movieCard);
            }, 800);
        }
    });

    document.addEventListener('mouseout', function(e) {
        const movieCard = e.target.closest('.movie-card');
        if (movieCard) {
            clearTimeout(quickViewTimer);
            hideQuickView(movieCard);
        }
    });
}

function showQuickView(movieCard) {
    movieCard.classList.add('show-quick-view');
}

function hideQuickView(movieCard) {
    movieCard.classList.remove('show-quick-view');
}

async function loadMovies() {
    const container = document.getElementById('moviesGrid');
    if (!container) return;

    showLoading(container);
    
    try {
        const data = await apiCall('/movies?page=0&size=100');
        if (data && data.content) {
            movies = data.content;
            filteredMovies = [...movies];
            displayMovies();
            populateMovieFilters();
            setupRecommendationMovies();
            updateMovieStats();
        } else {
            // Fallback if data structure is different
            movies = Array.isArray(data) ? data : [];
            filteredMovies = [...movies];
            displayMovies();
            populateMovieFilters();
            updateMovieStats();
        }
    } catch (error) {
        console.error('Failed to load movies:', error);
        showError('Failed to load movies. Please try again.');
        // Show empty state
        displayMovies();
    }
}

function displayMovies() {
    const container = document.getElementById('moviesGrid');
    if (!container) return;

    if (filteredMovies.length === 0) {
        container.innerHTML = `
            <div class="no-results">
                <i class="fas fa-film fa-3x"></i>
                <h3>No Movies Found</h3>
                <p>Try adjusting your search or filters</p>
                <button class="btn-primary" onclick="clearMovieFilters()">Clear Filters</button>
            </div>
        `;
        return;
    }

    // Calculate pagination
    const startIndex = (currentState.currentPage - 1) * currentState.itemsPerPage;
    const endIndex = startIndex + currentState.itemsPerPage;
    const moviesToShow = filteredMovies.slice(startIndex, endIndex);

    container.innerHTML = moviesToShow.map((movie, index) => `
        <div class="movie-card" data-movie-id="${movie.id}" data-index="${startIndex + index}">
            <div class="movie-poster">
                <div class="poster-image">
                    <img src="${getMoviePoster(movie)}" alt="${movie.title}" onerror="this.src='data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjgwIiBoZWlnaHQ9IjQwMCIgdmlld0JveD0iMCAwIDI4MCA0MDAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIyODAiIGhlaWdodD0iNDAwIiBmaWxsPSIjMUMyQzJDIi8+CjxwYXRoIGQ9Ik0xNDAgMjAwTDE2MCAyMjBMMTQwIDI0MEwxMjAgMjIwTDE0MCAyMDBaIiBmaWxsPSIjOTk5Ii8+Cjx0ZXh0IHg9IjE0MCIgeT0iMjgwIiB0ZXh0LWFuY2hvcj0ibWlkZGxlIiBmaWxsPSIjOTk5IiBmb250LXNpemU9IjE0IiBmb250LWZhbWlseT0iQXJpYWwsIHNhbnMtc2VyaWYiPk5vIEltYWdlPC90ZXh0Pgo8L3N2Zz4K'">
                    <div class="poster-overlay">
                        <div class="movie-badge">${movie.releaseYear}</div>
                        <div class="overlay-actions">
                            <button class="btn-play" onclick="event.stopPropagation(); playMovieTrailer(${movie.id})">
                                <i class="fas fa-play"></i>
                            </button>
                            <button class="btn-watchlist ${isInWatchlist(movie.id) ? 'in-watchlist' : ''}" 
                                    onclick="event.stopPropagation(); toggleWatchlist(${movie.id}, this)">
                                <i class="${isInWatchlist(movie.id) ? 'fas' : 'far'} fa-bookmark"></i>
                            </button>
                        </div>
                    </div>
                </div>
                <div class="movie-overlay">
                    <div class="movie-rating">
                        <i class="fas fa-star"></i>
                        <span>${getMovieRating(movie)}</span>
                    </div>
                    <div class="quick-view">
                        <button class="btn-quick-view" onclick="event.stopPropagation(); openMovieModal(${movie.id})">
                            <i class="fas fa-eye"></i> Quick View
                        </button>
                    </div>
                </div>
            </div>
            <div class="movie-info">
                <h3 class="movie-title">${movie.title}</h3>
                <div class="movie-meta">
                    <span class="meta-year">${movie.releaseYear}</span>
                    <span class="meta-duration">${formatDuration(movie.duration)}</span>
                    ${movie.rating ? `<span class="meta-rating">${movie.rating}/10</span>` : ''}
                </div>
                ${movie.genres && movie.genres.length > 0 ? `
                    <div class="genre-tags">
                        ${movie.genres.slice(0, 2).map(genre => `
                            <span class="genre-tag">${genre.name}</span>
                        `).join('')}
                        ${movie.genres.length > 2 ? `<span class="genre-tag-more">+${movie.genres.length - 2}</span>` : ''}
                    </div>
                ` : ''}
                <div class="movie-actions">
                    <button class="btn-watchlist ${isInWatchlist(movie.id) ? 'in-watchlist' : ''}" 
                            onclick="toggleWatchlist(${movie.id}, this)">
                        <i class="${isInWatchlist(movie.id) ? 'fas' : 'far'} fa-bookmark"></i>
                        ${isInWatchlist(movie.id) ? 'In Watchlist' : 'Watchlist'}
                    </button>
                    <button class="btn-info" onclick="openMovieModal(${movie.id})">
                        <i class="fas fa-info-circle"></i> Details
                    </button>
                </div>
            </div>
        </div>
    `).join('');

    // Add intersection observer for lazy loading
    setupLazyLoading();
    updatePaginationUI();
}

function getMoviePoster(movie) {
    // In a real app, this would come from the API
    return `https://via.placeholder.com/280x400/1C2C2C/999999?text=${encodeURIComponent(movie.title)}`;
}

function getMovieRating(movie) {
    return movie.rating ? movie.rating.toFixed(1) : 'N/A';
}

function setupLazyLoading() {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.dataset.src;
                observer.unobserve(img);
            }
        });
    });

    document.querySelectorAll('.movie-poster img[data-src]').forEach(img => {
        observer.observe(img);
    });
}

function populateMovieFilters() {
    populateYearFilter();
    populateGenreFilter();
    populateSortOptions();
}

function populateYearFilter() {
    const yearFilter = document.getElementById('yearFilter');
    if (!yearFilter) return;

    const years = [...new Set(movies.map(movie => movie.releaseYear))]
        .filter(year => year)
        .sort((a, b) => b - a);
    
    yearFilter.innerHTML = '<option value="">All Years</option>' +
        years.map(year => `<option value="${year}">${year}</option>`).join('');
}

function populateGenreFilter() {
    const genreFilter = document.getElementById('genreFilter');
    if (!genreFilter) return;

    const allGenres = movies.flatMap(movie => movie.genres || []);
    const uniqueGenres = [...new Map(allGenres.map(genre => [genre.id, genre])).values()]
        .sort((a, b) => a.name.localeCompare(b.name));
    
    genreFilter.innerHTML = '<option value="">All Genres</option>' +
        uniqueGenres.map(genre => `<option value="${genre.id}">${genre.name}</option>`).join('');
}

function populateSortOptions() {
    const sortSelect = document.getElementById('sortBy');
    if (!sortSelect) return;

    sortSelect.innerHTML = `
        <option value="title">Title A-Z</option>
        <option value="title_desc">Title Z-A</option>
        <option value="year">Year (Oldest First)</option>
        <option value="year_desc">Year (Newest First)</option>
        <option value="rating">Rating (Low to High)</option>
        <option value="rating_desc">Rating (High to Low)</option>
        <option value="duration">Duration (Short to Long)</option>
        <option value="duration_desc">Duration (Long to Short)</option>
    `;
}

function initializeMovieSearch() {
    const searchInput = document.getElementById('movieSearch');
    if (searchInput) {
        searchInput.addEventListener('input', debounce(searchMovies, 300));
    }
}

const searchMovies = debounce(function() {
    const searchTerm = document.getElementById('movieSearch')?.value.toLowerCase() || '';
    const yearFilter = document.getElementById('yearFilter')?.value || '';
    const genreFilter = document.getElementById('genreFilter')?.value || '';
    const sortBy = document.getElementById('sortBy')?.value || 'title';

    filteredMovies = movies.filter(movie => {
        const matchesSearch = !searchTerm || 
            movie.title.toLowerCase().includes(searchTerm) ||
            (movie.description && movie.description.toLowerCase().includes(searchTerm));
        
        const matchesYear = !yearFilter || movie.releaseYear == yearFilter;
        const matchesGenre = !genreFilter || 
            (movie.genres && movie.genres.some(genre => genre.id == genreFilter));
        
        return matchesSearch && matchesYear && matchesGenre;
    });

    // Apply sorting
    sortMovies(sortBy);

    currentState.currentPage = 1;
    displayMovies();
    updateMovieStats();
}, 300);

function sortMovies(sortBy) {
    filteredMovies.sort((a, b) => {
        switch (sortBy) {
            case 'title_desc':
                return b.title.localeCompare(a.title);
            case 'year':
                return (a.releaseYear || 0) - (b.releaseYear || 0);
            case 'year_desc':
                return (b.releaseYear || 0) - (a.releaseYear || 0);
            case 'rating':
                return (a.rating || 0) - (b.rating || 0);
            case 'rating_desc':
                return (b.rating || 0) - (a.rating || 0);
            case 'duration':
                return (a.duration || 0) - (b.duration || 0);
            case 'duration_desc':
                return (b.duration || 0) - (a.duration || 0);
            default: // 'title'
                return a.title.localeCompare(b.title);
        }
    });
}

function filterMovies() {
    searchMovies();
}

function clearMovieFilters() {
    const searchInput = document.getElementById('movieSearch');
    const yearFilter = document.getElementById('yearFilter');
    const genreFilter = document.getElementById('genreFilter');
    const sortSelect = document.getElementById('sortBy');

    if (searchInput) searchInput.value = '';
    if (yearFilter) yearFilter.value = '';
    if (genreFilter) genreFilter.value = '';
    if (sortSelect) sortSelect.value = 'title';

    currentState.filters = {
        genre: '',
        year: '',
        mood: '',
        sortBy: 'title'
    };

    searchMovies();
}

async function showMovieDetails(movieId) {
    await openMovieModal(movieId);
}

async function openMovieModal(movieId) {
    const modal = document.getElementById('movieModal');
    const modalContent = document.getElementById('modalContent');
    
    if (!modal || !modalContent) return;

    showLoading(modalContent);
    modal.style.display = 'block';
    document.body.classList.add('modal-open');

    try {
        const movie = await apiCall(`/movies/${movieId}`);
        if (movie) {
            currentMovieIndex = filteredMovies.findIndex(m => m.id === movieId);
            displayMovieModal(movie, modalContent);
            setupModalNavigation();
        } else {
            throw new Error('Movie not found');
        }
    } catch (error) {
        console.error('Failed to load movie details:', error);
        modalContent.innerHTML = `
            <div class="modal-error">
                <i class="fas fa-exclamation-triangle"></i>
                <h3>Failed to Load Movie Details</h3>
                <p>Please try again later.</p>
                <button class="btn-primary" onclick="closeModal()">Close</button>
            </div>
        `;
    }
}

function displayMovieModal(movie, container) {
    const userRating = getUserRating(movie.id);
    
    container.innerHTML = `
        <div class="movie-modal">
            <div class="modal-header">
                <div class="modal-poster-large">
                    <img src="${getMoviePoster(movie)}" alt="${movie.title}" class="modal-movie-poster">
                    <div class="poster-actions-modal">
                        <button class="btn-action btn-trailer" onclick="playMovieTrailer(${movie.id})">
                            <i class="fas fa-play"></i>
                            <span>Watch Trailer</span>
                        </button>
                        <button class="btn-action btn-watchlist ${isInWatchlist(movie.id) ? 'in-watchlist' : ''}" 
                                onclick="toggleWatchlist(${movie.id}, this)">
                            <i class="${isInWatchlist(movie.id) ? 'fas' : 'far'} fa-bookmark"></i>
                            <span>${isInWatchlist(movie.id) ? 'In Watchlist' : 'Add to Watchlist'}</span>
                        </button>
                    </div>
                </div>
                <div class="modal-info">
                    <div class="modal-header-top">
                        <h1 class="modal-title">${movie.title}</h1>
                        <button class="modal-close" onclick="closeModal()">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                    
                    <div class="modal-meta-grid">
                        <div class="meta-item">
                            <i class="fas fa-calendar"></i>
                            <span>${movie.releaseYear}</span>
                        </div>
                        <div class="meta-item">
                            <i class="fas fa-clock"></i>
                            <span>${formatDuration(movie.duration)}</span>
                        </div>
                        ${movie.rating ? `
                        <div class="meta-item rating">
                            <i class="fas fa-star"></i>
                            <span>${movie.rating}/10</span>
                        </div>
                        ` : ''}
                        ${movie.certification ? `
                        <div class="meta-item certification">
                            <i class="fas fa-tag"></i>
                            <span>${movie.certification}</span>
                        </div>
                        ` : ''}
                    </div>
                    
                    ${movie.genres && movie.genres.length > 0 ? `
                        <div class="modal-genres">
                            ${movie.genres.map(genre => `
                                <span class="genre-tag-large">${genre.name}</span>
                            `).join('')}
                        </div>
                    ` : ''}
                    
                    <div class="user-rating-section">
                        <h4>Your Rating</h4>
                        <div class="star-rating-modal">
                            ${[1,2,3,4,5,6,7,8,9,10].map(star => `
                                <button class="star ${star <= (userRating || 0) ? 'active' : ''}" 
                                        onclick="rateMovie(${movie.id}, ${star})" 
                                        onmouseover="highlightStars(${star})"
                                        onmouseout="resetStars()">
                                    <i class="fas fa-star"></i>
                                    <span>${star}</span>
                                </button>
                            `).join('')}
                        </div>
                        ${userRating ? `<p class="current-rating">You rated this ${userRating}/10</p>` : ''}
                    </div>
                </div>
            </div>
            
            <div class="modal-body">
                ${movie.description ? `
                    <section class="modal-section">
                        <h3><i class="fas fa-scroll"></i> Overview</h3>
                        <p class="movie-description">${movie.description}</p>
                    </section>
                ` : ''}
                
                ${movie.actors && movie.actors.length > 0 ? `
                    <section class="modal-section">
                        <h3><i class="fas fa-users"></i> Cast</h3>
                        <div class="cast-grid">
                            ${movie.actors.slice(0, 12).map(actor => `
                                <div class="cast-member" onclick="openActorModal(${actor.id})">
                                    <div class="cast-photo">
                                        <i class="fas fa-user"></i>
                                    </div>
                                    <div class="cast-info">
                                        <strong class="cast-name">${actor.name}</strong>
                                        ${actor.character ? `<span class="cast-character">as ${actor.character}</span>` : ''}
                                    </div>
                                </div>
                            `).join('')}
                        </div>
                    </section>
                ` : ''}
                
                <section class="modal-section">
                    <h3><i class="fas fa-info-circle"></i> Details</h3>
                    <div class="details-grid">
                        ${movie.director ? `
                            <div class="detail-item">
                                <i class="fas fa-video"></i>
                                <div>
                                    <strong>Director</strong>
                                    <span>${movie.director}</span>
                                </div>
                            </div>
                        ` : ''}
                        ${movie.budget ? `
                            <div class="detail-item">
                                <i class="fas fa-money-bill-wave"></i>
                                <div>
                                    <strong>Budget</strong>
                                    <span>$${movie.budget.toLocaleString()}</span>
                                </div>
                            </div>
                        ` : ''}
                        ${movie.revenue ? `
                            <div class="detail-item">
                                <i class="fas fa-chart-line"></i>
                                <div>
                                    <strong>Revenue</strong>
                                    <span>$${movie.revenue.toLocaleString()}</span>
                                </div>
                            </div>
                        ` : ''}
                        ${movie.language ? `
                            <div class="detail-item">
                                <i class="fas fa-language"></i>
                                <div>
                                    <strong>Language</strong>
                                    <span>${movie.language}</span>
                                </div>
                            </div>
                        ` : ''}
                        ${movie.country ? `
                            <div class="detail-item">
                                <i class="fas fa-globe"></i>
                                <div>
                                    <strong>Country</strong>
                                    <span>${movie.country}</span>
                                </div>
                            </div>
                        ` : ''}
                    </div>
                </section>
            </div>
            
            <div class="modal-navigation">
                <button class="btn-nav btn-prev" onclick="navigateToAdjacentMovie(-1)" ${currentMovieIndex <= 0 ? 'disabled' : ''}>
                    <i class="fas fa-chevron-left"></i> Previous Movie
                </button>
                <button class="btn-nav btn-next" onclick="navigateToAdjacentMovie(1)" ${currentMovieIndex >= filteredMovies.length - 1 ? 'disabled' : ''}>
                    Next Movie <i class="fas fa-chevron-right"></i>
                </button>
            </div>
        </div>
    `;

    // Setup star rating hover effects
    setupStarRating();
}

function setupModalNavigation() {
    const modalKeyHandler = function(e) {
        if (e.key === 'Escape') {
            closeModal();
        }
        if (e.key === 'ArrowLeft') {
            navigateToAdjacentMovie(-1);
        } else if (e.key === 'ArrowRight') {
            navigateToAdjacentMovie(1);
        }
    };

    document.addEventListener('keydown', modalKeyHandler);
    
    // Store reference to remove later
    window.currentModalKeyHandler = modalKeyHandler;
}

function navigateToAdjacentMovie(direction) {
    const newIndex = currentMovieIndex + direction;
    if (newIndex >= 0 && newIndex < filteredMovies.length) {
        const nextMovieId = filteredMovies[newIndex].id;
        openMovieModal(nextMovieId);
    }
}

function setupStarRating() {
    const stars = document.querySelectorAll('.star');
    let currentHover = 0;

    window.highlightStars = function(upTo) {
        stars.forEach((star, index) => {
            star.classList.toggle('hover', index < upTo);
        });
        currentHover = upTo;
    };

    window.resetStars = function() {
        stars.forEach((star, index) => {
            const userRating = getUserRating(parseInt(document.querySelector('.movie-modal').dataset.movieId));
            star.classList.toggle('hover', index < currentHover);
            star.classList.toggle('active', index < (userRating || 0));
        });
    };
}

function playMovieTrailer(movieId) {
    // In a real app, this would play the actual trailer
    showSuccess('Playing trailer for movie...');
    console.log('Playing trailer for movie:', movieId);
}

function updateMovieStats() {
    const statsElement = document.querySelector('.movies-stats');
    if (!statsElement) return;

    statsElement.innerHTML = `
        <div class="stats-grid">
            <div class="stat">
                <span class="stat-number">${filteredMovies.length}</span>
                <span class="stat-label">Movies Found</span>
            </div>
            <div class="stat">
                <span class="stat-number">${Math.ceil(filteredMovies.length / currentState.itemsPerPage)}</span>
                <span class="stat-label">Pages</span>
            </div>
            <div class="stat">
                <span class="stat-number">${new Set(filteredMovies.flatMap(m => m.genres || [])).size}</span>
                <span class="stat-label">Genres</span>
            </div>
        </div>
    `;
}

// Export functions for global use
window.loadMovies = loadMovies;
window.searchMovies = searchMovies;
window.filterMovies = filterMovies;
window.showMovieDetails = showMovieDetails;
window.openMovieModal = openMovieModal;
window.clearMovieFilters = clearMovieFilters;
window.navigateToAdjacentMovie = navigateToAdjacentMovie;
window.playMovieTrailer = playMovieTrailer;
window.highlightStars = highlightStars;
window.resetStars = resetStars;

// Initialize when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeMovies);
} else {
    initializeMovies();
}