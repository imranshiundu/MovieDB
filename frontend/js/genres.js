// genres.js - Premium Genres Experience
let genres = [];
let genreMovies = new Map(); // Cache for genre movies

// Initialize genres functionality
document.addEventListener('DOMContentLoaded', function() {
    initializeGenres();
});

async function initializeGenres() {
    await loadGenres();
    setupGenreEventListeners();
    initializeGenreAnimations();
}

function setupGenreEventListeners() {
    // Genre card interactions
    document.addEventListener('click', function(e) {
        if (e.target.closest('.btn-explore')) {
            e.preventDefault();
            e.stopPropagation();
            const genreId = e.target.closest('.genre-card').dataset.genreId;
            openGenreModal(genreId);
        }
        
        if (e.target.closest('.btn-shuffle')) {
            e.preventDefault();
            e.stopPropagation();
            const genreId = e.target.closest('.genre-card').dataset.genreId;
            shuffleGenreMovies(genreId);
        }
    });

    // Quick stats on hover
    if (window.innerWidth > 768) {
        setupGenreHoverEffects();
    }
}

function setupGenreHoverEffects() {
    let hoverTimer;
    
    document.addEventListener('mouseover', function(e) {
        const genreCard = e.target.closest('.genre-card');
        if (genreCard && !genreCard.classList.contains('show-stats')) {
            clearTimeout(hoverTimer);
            hoverTimer = setTimeout(() => {
                showGenreStats(genreCard);
            }, 500);
        }
    });

    document.addEventListener('mouseout', function(e) {
        const genreCard = e.target.closest('.genre-card');
        if (genreCard) {
            clearTimeout(hoverTimer);
            hideGenreStats(genreCard);
        }
    });
}

function showGenreStats(genreCard) {
    genreCard.classList.add('show-stats');
}

function hideGenreStats(genreCard) {
    genreCard.classList.remove('show-stats');
}

function initializeGenreAnimations() {
    // Add staggered animation to genre cards
    const genreCards = document.querySelectorAll('.genre-card');
    genreCards.forEach((card, index) => {
        card.style.animationDelay = `${index * 0.1}s`;
        card.classList.add('animate-in');
    });
}

async function loadGenres() {
    const container = document.getElementById('genresGrid');
    if (!container) return;

    showLoading(container);
    
    try {
        const data = await apiCall('/genres');
        if (data) {
            genres = data;
            displayGenres();
            updateGenreStats();
            preloadPopularGenreMovies();
        }
    } catch (error) {
        console.error('Failed to load genres:', error);
        showError('Failed to load genres. Please try again.');
    }
}

function displayGenres() {
    const container = document.getElementById('genresGrid');
    if (!container) return;

    if (genres.length === 0) {
        container.innerHTML = `
            <div class="no-results">
                <i class="fas fa-theater-masks fa-3x"></i>
                <h3>No Genres Available</h3>
                <p>Genre data is currently unavailable.</p>
            </div>
        `;
        return;
    }

    container.innerHTML = genres.map(genre => `
        <div class="genre-card" data-genre-id="${genre.id}" data-genre-name="${genre.name}">
            <div class="genre-background">
                <div class="genre-gradient" style="${getGenreGradient(genre.name)}"></div>
                <div class="genre-pattern"></div>
            </div>
            
            <div class="genre-content">
                <div class="genre-header">
                    <h3 class="genre-name">${genre.name}</h3>
                    <div class="genre-icon">
                        ${getGenreIcon(genre.name)}
                    </div>
                </div>
                
                <div class="genre-stats">
                    <div class="stat">
                        <i class="fas fa-film"></i>
                        <span class="stat-value" id="movie-count-${genre.id}">...</span>
                        <span class="stat-label">Movies</span>
                    </div>
                    <div class="stat">
                        <i class="fas fa-clock"></i>
                        <span class="stat-value" id="avg-rating-${genre.id}">...</span>
                        <span class="stat-label">Avg Rating</span>
                    </div>
                </div>
                
                <div class="genre-actions">
                    <button class="btn-explore" onclick="openGenreModal(${genre.id})">
                        <i class="fas fa-compass"></i>
                        Explore
                    </button>
                    <button class="btn-shuffle" onclick="shuffleGenreMovies(${genre.id})">
                        <i class="fas fa-random"></i>
                        Shuffle
                    </button>
                </div>
            </div>
            
            <div class="genre-hover-stats">
                <div class="hover-content">
                    <h4>Top Movies</h4>
                    <div class="top-movies-list" id="top-movies-${genre.id}">
                        <div class="loading-dots">
                            <span></span>
                            <span></span>
                            <span></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    `).join('');

    // Load genre statistics asynchronously
    loadGenreStatistics();
}

function getGenreGradient(genreName) {
    const gradients = {
        'Action': 'linear-gradient(135deg, #ff6b6b 0%, #ee5a24 100%)',
        'Comedy': 'linear-gradient(135deg, #feca57 0%, #ff9ff3 100%)',
        'Drama': 'linear-gradient(135deg, #48dbfb 0%, #0abde3 100%)',
        'Sci-Fi': 'linear-gradient(135deg, #a29bfe 0%, #6c5ce7 100%)',
        'Horror': 'linear-gradient(135deg, #fd79a8 0%, #e84393 100%)',
        'Romance': 'linear-gradient(135deg, #ffeaa7 0%, #fab1a0 100%)',
        'Thriller': 'linear-gradient(135deg, #dfe6e9 0%, #b2bec3 100%)',
        'Adventure': 'linear-gradient(135deg, #55efc4 0%, #00b894 100%)',
        'Fantasy': 'linear-gradient(135deg, #74b9ff 0%, #0984e3 100%)',
        'Mystery': 'linear-gradient(135deg, #a29bfe 0%, #6c5ce7 100%)',
        'Crime': 'linear-gradient(135deg, #636e72 0%, #2d3436 100%)',
        'Animation': 'linear-gradient(135deg, #ffeaa7 0%, #fdcb6e 100%)',
        'Family': 'linear-gradient(135deg, #81ecec 0%, #00cec9 100%)',
        'Documentary': 'linear-gradient(135deg, #dfe6e9 0%, #b2bec3 100%)'
    };
    
    return gradients[genreName] || 'linear-gradient(135deg, #a29bfe 0%, #6c5ce7 100%)';
}

function getGenreIcon(genreName) {
    const icons = {
        'Action': '<i class="fas fa-explosion"></i>',
        'Comedy': '<i class="fas fa-laugh-beam"></i>',
        'Drama': '<i class="fas fa-masks-theater"></i>',
        'Sci-Fi': '<i class="fas fa-robot"></i>',
        'Horror': '<i class="fas fa-ghost"></i>',
        'Romance': '<i class="fas fa-heart"></i>',
        'Thriller': '<i class="fas fa-suspense"></i>',
        'Adventure': '<i class="fas fa-mountain"></i>',
        'Fantasy': '<i class="fas fa-dragon"></i>',
        'Mystery': '<i class="fas fa-search"></i>',
        'Crime': '<i class="fas fa-handcuffs"></i>',
        'Animation': '<i class="fas fa-film"></i>',
        'Family': '<i class="fas fa-home"></i>',
        'Documentary': '<i class="fas fa-camera"></i>'
    };
    
    return icons[genreName] || '<i class="fas fa-film"></i>';
}

async function loadGenreStatistics() {
    for (const genre of genres) {
        try {
            const movies = await apiCall(`/movies/by-genre/${genre.id}?size=100`);
            if (movies && movies.content) {
                const movieCount = movies.content.length;
                const avgRating = movies.content.reduce((sum, movie) => sum + (movie.rating || 0), 0) / movieCount;
                const topMovies = movies.content
                    .sort((a, b) => (b.rating || 0) - (a.rating || 0))
                    .slice(0, 3);
                
                // Update genre card stats
                updateGenreCardStats(genre.id, movieCount, avgRating, topMovies);
                
                // Cache the movies
                genreMovies.set(genre.id, movies.content);
            }
        } catch (error) {
            console.error(`Failed to load stats for genre ${genre.name}:`, error);
            updateGenreCardStats(genre.id, 0, 0, []);
        }
    }
}

function updateGenreCardStats(genreId, movieCount, avgRating, topMovies) {
    // Update movie count
    const movieCountElement = document.getElementById(`movie-count-${genreId}`);
    if (movieCountElement) {
        movieCountElement.textContent = movieCount.toLocaleString();
    }
    
    // Update average rating
    const avgRatingElement = document.getElementById(`avg-rating-${genreId}`);
    if (avgRatingElement) {
        avgRatingElement.textContent = avgRating > 0 ? avgRating.toFixed(1) : 'N/A';
    }
    
    // Update top movies for hover
    const topMoviesElement = document.getElementById(`top-movies-${genreId}`);
    if (topMoviesElement && topMovies.length > 0) {
        topMoviesElement.innerHTML = topMovies.map(movie => `
            <div class="top-movie-item" onclick="event.stopPropagation(); openMovieModal(${movie.id})">
                <span class="movie-title">${movie.title}</span>
                <span class="movie-rating">${movie.rating ? movie.rating.toFixed(1) : 'N/A'}</span>
            </div>
        `).join('');
    }
}

async function preloadPopularGenreMovies() {
    // Preload movies for the first 3 genres for better UX
    const popularGenres = genres.slice(0, 3);
    for (const genre of popularGenres) {
        if (!genreMovies.has(genre.id)) {
            try {
                const movies = await apiCall(`/movies/by-genre/${genre.id}?size=20`);
                if (movies && movies.content) {
                    genreMovies.set(genre.id, movies.content);
                }
            } catch (error) {
                console.error(`Failed to preload movies for ${genre.name}:`, error);
            }
        }
    }
}

async function openGenreModal(genreId) {
    const modal = document.getElementById('movieModal');
    const modalContent = document.getElementById('modalContent');
    
    if (!modal || !modalContent) return;

    showLoading(modalContent);
    modal.style.display = 'block';
    document.body.classList.add('modal-open');

    try {
        const genre = genres.find(g => g.id === genreId);
        let movies = genreMovies.get(genreId);
        
        if (!movies) {
            const moviesData = await apiCall(`/movies/by-genre/${genreId}?size=50`);
            movies = moviesData?.content || [];
            genreMovies.set(genreId, movies);
        }
        
        if (genre) {
            displayGenreModal(genre, movies, modalContent);
        }
    } catch (error) {
        console.error('Failed to load genre details:', error);
        modalContent.innerHTML = `
            <div class="modal-error">
                <i class="fas fa-exclamation-triangle"></i>
                <h3>Failed to Load Genre</h3>
                <p>Please try again later.</p>
                <button class="btn-primary" onclick="closeModal()">Close</button>
            </div>
        `;
    }
}

function displayGenreModal(genre, movies, container) {
    const sortedMovies = [...movies].sort((a, b) => (b.rating || 0) - (a.rating || 0));
    const recentMovies = [...movies].sort((a, b) => (b.releaseYear || 0) - (a.releaseYear || 0));
    const popularMovies = sortedMovies.slice(0, 6);
    
    container.innerHTML = `
        <div class="genre-modal">
            <div class="modal-header genre-modal-header" style="${getGenreGradient(genre.name)}">
                <div class="header-content">
                    <div class="genre-icon-large">
                        ${getGenreIcon(genre.name)}
                    </div>
                    <div class="header-text">
                        <h1 class="modal-title">${genre.name}</h1>
                        <div class="genre-stats-large">
                            <div class="stat-large">
                                <span class="stat-number">${movies.length}</span>
                                <span class="stat-label">Total Movies</span>
                            </div>
                            <div class="stat-large">
                                <span class="stat-number">${calculateAverageRating(movies)}</span>
                                <span class="stat-label">Average Rating</span>
                            </div>
                            <div class="stat-large">
                                <span class="stat-number">${calculateAverageDuration(movies)}</span>
                                <span class="stat-label">Avg Duration</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="modal-body">
                <div class="genre-tabs">
                    <button class="tab-btn active" data-tab="popular">Most Popular</button>
                    <button class="tab-btn" data-tab="recent">Recently Added</button>
                    <button class="tab-btn" data-tab="all">All Movies</button>
                </div>
                
                <div class="tab-content active" id="popular-tab">
                    <div class="movies-grid-mini">
                        ${popularMovies.length > 0 ? popularMovies.map(movie => `
                            <div class="movie-card-mini" onclick="openMovieModal(${movie.id})">
                                <div class="poster-mini">
                                    <img src="${getMoviePoster(movie)}" alt="${movie.title}">
                                    <div class="poster-overlay-mini">
                                        <div class="mini-rating">
                                            <i class="fas fa-star"></i>
                                            <span>${getMovieRating(movie)}</span>
                                        </div>
                                    </div>
                                </div>
                                <div class="info-mini">
                                    <h4 class="title-mini">${movie.title}</h4>
                                    <span class="year-mini">${movie.releaseYear}</span>
                                </div>
                            </div>
                        `).join('') : `
                            <div class="no-movies">
                                <i class="fas fa-film"></i>
                                <p>No popular movies found</p>
                            </div>
                        `}
                    </div>
                </div>
                
                <div class="tab-content" id="recent-tab">
                    <div class="movies-list">
                        ${recentMovies.slice(0, 10).length > 0 ? recentMovies.slice(0, 10).map(movie => `
                            <div class="movie-list-item" onclick="openMovieModal(${movie.id})">
                                <div class="list-poster">
                                    <img src="${getMoviePoster(movie)}" alt="${movie.title}">
                                </div>
                                <div class="list-info">
                                    <h4 class="list-title">${movie.title}</h4>
                                    <div class="list-meta">
                                        <span class="list-year">${movie.releaseYear}</span>
                                        <span class="list-duration">${formatDuration(movie.duration)}</span>
                                        <span class="list-rating">
                                            <i class="fas fa-star"></i> ${getMovieRating(movie)}
                                        </span>
                                    </div>
                                </div>
                                <div class="list-actions">
                                    <button class="btn-watchlist-mini ${isInWatchlist(movie.id) ? 'in-watchlist' : ''}" 
                                            onclick="event.stopPropagation(); toggleWatchlist(${movie.id}, this)">
                                        <i class="${isInWatchlist(movie.id) ? 'fas' : 'far'} fa-bookmark"></i>
                                    </button>
                                </div>
                            </div>
                        `).join('') : `
                            <div class="no-movies">
                                <i class="fas fa-film"></i>
                                <p>No recent movies found</p>
                            </div>
                        `}
                    </div>
                </div>
                
                <div class="tab-content" id="all-tab">
                    <div class="movies-table">
                        <div class="table-header">
                            <div class="table-col title-col">Title</div>
                            <div class="table-col year-col">Year</div>
                            <div class="table-col rating-col">Rating</div>
                            <div class="table-col duration-col">Duration</div>
                            <div class="table-col action-col">Actions</div>
                        </div>
                        <div class="table-body">
                            ${movies.length > 0 ? movies.slice(0, 15).map(movie => `
                                <div class="table-row" onclick="openMovieModal(${movie.id})">
                                    <div class="table-col title-col">
                                        <span class="movie-title-table">${movie.title}</span>
                                    </div>
                                    <div class="table-col year-col">
                                        <span class="movie-year-table">${movie.releaseYear}</span>
                                    </div>
                                    <div class="table-col rating-col">
                                        <span class="movie-rating-table">
                                            <i class="fas fa-star"></i> ${getMovieRating(movie)}
                                        </span>
                                    </div>
                                    <div class="table-col duration-col">
                                        <span class="movie-duration-table">${formatDuration(movie.duration)}</span>
                                    </div>
                                    <div class="table-col action-col">
                                        <button class="btn-watchlist-table ${isInWatchlist(movie.id) ? 'in-watchlist' : ''}" 
                                                onclick="event.stopPropagation(); toggleWatchlist(${movie.id}, this)">
                                            <i class="${isInWatchlist(movie.id) ? 'fas' : 'far'} fa-bookmark"></i>
                                        </button>
                                    </div>
                                </div>
                            `).join('') : `
                                <div class="table-row empty-row">
                                    <div class="table-col">No movies found in this genre</div>
                                </div>
                            `}
                        </div>
                    </div>
                    ${movies.length > 15 ? `
                        <div class="show-more-container">
                            <button class="btn-show-more" onclick="loadAllGenreMovies(${genre.id})">
                                Show All ${movies.length} Movies
                            </button>
                        </div>
                    ` : ''}
                </div>
            </div>
            
            <div class="modal-actions">
                <button class="btn-shuffle-large" onclick="shuffleGenreMovies(${genre.id})">
                    <i class="fas fa-random"></i>
                    Shuffle & Discover
                </button>
                <button class="btn-close-genre" onclick="closeModal()">
                    <i class="fas fa-times"></i>
                    Close
                </button>
            </div>
        </div>
    `;

    // Setup tab functionality
    setupGenreTabs();
}

function setupGenreTabs() {
    const tabBtns = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');
    
    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            // Remove active class from all buttons and contents
            tabBtns.forEach(b => b.classList.remove('active'));
            tabContents.forEach(c => c.classList.remove('active'));
            
            // Add active class to clicked button and corresponding content
            btn.classList.add('active');
            const tabName = btn.dataset.tab;
            document.getElementById(`${tabName}-tab`).classList.add('active');
        });
    });
}

function calculateAverageRating(movies) {
    if (!movies.length) return 'N/A';
    const avg = movies.reduce((sum, movie) => sum + (movie.rating || 0), 0) / movies.length;
    return avg.toFixed(1);
}

function calculateAverageDuration(movies) {
    if (!movies.length) return 'N/A';
    const avg = movies.reduce((sum, movie) => sum + (movie.duration || 0), 0) / movies.length;
    return formatDuration(Math.round(avg));
}

async function shuffleGenreMovies(genreId) {
    const genre = genres.find(g => g.id === genreId);
    if (!genre) return;
    
    let movies = genreMovies.get(genreId);
    if (!movies) {
        try {
            const moviesData = await apiCall(`/movies/by-genre/${genreId}?size=50`);
            movies = moviesData?.content || [];
            genreMovies.set(genreId, movies);
        } catch (error) {
            console.error('Failed to load movies for shuffle:', error);
            showError('Failed to load movies for shuffle');
            return;
        }
    }
    
    if (movies.length === 0) {
        showError('No movies found in this genre');
        return;
    }
    
    // Get a random movie
    const randomMovie = movies[Math.floor(Math.random() * movies.length)];
    
    // Show success message and open the movie
    showSuccess(`Discovering random ${genre.name} movie: ${randomMovie.title}`);
    setTimeout(() => {
        openMovieModal(randomMovie.id);
    }, 1500);
}

function loadAllGenreMovies(genreId) {
    // In a full implementation, this would show a dedicated page with all movies
    const genre = genres.find(g => g.id === genreId);
    if (genre) {
        showSuccess(`Loading all ${genre.name} movies...`);
        // This would typically navigate to a movies page filtered by genre
        currentState.filters.genre = genreId;
        navigateToSection('movies');
        closeModal();
    }
}

function updateGenreStats() {
    const statsElement = document.querySelector('.genres-stats');
    if (!statsElement) return;

    statsElement.innerHTML = `
        <div class="stats-grid">
            <div class="stat">
                <span class="stat-number">${genres.length}</span>
                <span class="stat-label">Genres</span>
            </div>
            <div class="stat">
                <span class="stat-number">${new Set(genres.flatMap(g => genreMovies.get(g.id) || [])).size}</span>
                <span class="stat-label">Total Movies</span>
            </div>
        </div>
    `;
}

// Export functions for global use
window.loadGenres = loadGenres;
window.showGenreMovies = openGenreModal;
window.openGenreModal = openGenreModal;
window.shuffleGenreMovies = shuffleGenreMovies;
window.loadAllGenreMovies = loadAllGenreMovies;

// Initialize when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeGenres);
} else {
    initializeGenres();
}