// recommendations.js - Premium Recommendations Experience
let recommendationState = {
    currentType: 'trending',
    currentMood: '',
    currentMovie: null,
    recommendations: [],
    isLoading: false
};

// Initialize recommendations functionality
document.addEventListener('DOMContentLoaded', function() {
    initializeRecommendations();
});

async function initializeRecommendations() {
    await setupRecommendationMovies();
    setupRecommendationEventListeners();
    loadPersonalizedRecommendations();
    initializeRecommendationAnimations();
}

function setupRecommendationEventListeners() {
    // Recommendation type changes
    const recommendationType = document.getElementById('recommendationType');
    if (recommendationType) {
        recommendationType.addEventListener('change', handleRecommendationTypeChange);
    }

    // Mood selection
    const moodSelect = document.getElementById('moodSelect');
    if (moodSelect) {
        moodSelect.addEventListener('change', handleMoodChange);
    }

    // Movie selection
    const movieSelect = document.getElementById('movieSelect');
    if (movieSelect) {
        movieSelect.addEventListener('change', handleMovieChange);
    }

    // Get recommendations button
    const getRecsBtn = document.getElementById('getRecommendations');
    if (getRecsBtn) {
        getRecsBtn.addEventListener('click', getPersonalizedRecommendations);
    }

    // Quick mood buttons
    setupQuickMoodButtons();
}

function setupQuickMoodButtons() {
    const moodButtons = document.querySelectorAll('.mood-quick-btn');
    moodButtons.forEach(btn => {
        btn.addEventListener('click', function() {
            const mood = this.dataset.mood;
            setActiveMood(mood);
            getMoodRecommendations(mood);
        });
    });
}

function setActiveMood(mood) {
    // Update mood select
    const moodSelect = document.getElementById('moodSelect');
    if (moodSelect) {
        moodSelect.value = mood;
    }

    // Update active quick button
    document.querySelectorAll('.mood-quick-btn').forEach(btn => {
        btn.classList.toggle('active', btn.dataset.mood === mood);
    });
}

function initializeRecommendationAnimations() {
    // Add staggered animation to recommendation cards
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.animationDelay = `${entry.target.dataset.index * 0.1}s`;
                entry.target.classList.add('animate-in');
            }
        });
    }, { threshold: 0.1 });

    // Observe recommendation grid for future cards
    const grid = document.getElementById('recommendationsGrid');
    if (grid) {
        observer.observe(grid);
    }
}

async function setupRecommendationMovies() {
    const movieSelect = document.getElementById('movieSelect');
    if (!movieSelect) return;

    showLoading(movieSelect);

    try {
        const data = await apiCall('/movies?size=100');
        if (data && data.content) {
            const movies = data.content.sort((a, b) => a.title.localeCompare(b.title));
            movieSelect.innerHTML = '<option value="">Choose a movie you like...</option>' +
                movies.map(movie => `
                    <option value="${movie.id}">${movie.title} (${movie.releaseYear})</option>
                `).join('');
            
            // Load trending movies by default
            getTrendingRecommendations();
        }
    } catch (error) {
        console.error('Failed to load movies for recommendations:', error);
        movieSelect.innerHTML = '<option value="">Failed to load movies</option>';
    }
}

function handleRecommendationTypeChange() {
    const type = document.getElementById('recommendationType').value;
    const moodSection = document.getElementById('moodSelector');
    const movieSection = document.getElementById('movieSelector');
    const personalizedSection = document.querySelector('.personalized-controls');

    // Hide all sections first
    if (moodSection) moodSection.classList.add('hidden');
    if (movieSection) movieSection.classList.add('hidden');
    if (personalizedSection) personalizedSection.classList.add('hidden');

    recommendationState.currentType = type;

    switch(type) {
        case 'mood':
            if (moodSection) moodSection.classList.remove('hidden');
            getMoodRecommendations(document.getElementById('moodSelect')?.value || 'action');
            break;
        case 'movie':
            if (movieSection) movieSection.classList.remove('hidden');
            break;
        case 'personalized':
            if (personalizedSection) personalizedSection.classList.remove('hidden');
            getPersonalizedRecommendations();
            break;
        case 'trending':
            getTrendingRecommendations();
            break;
        case 'popular':
            getPopularRecommendations();
            break;
        case 'similar':
            getSimilarRecommendations();
            break;
    }
}

function handleMoodChange() {
    const mood = document.getElementById('moodSelect').value;
    if (mood) {
        getMoodRecommendations(mood);
    }
}

function handleMovieChange() {
    const movieId = document.getElementById('movieSelect').value;
    if (movieId) {
        getMovieBasedRecommendations(movieId);
    }
}

async function getTrendingRecommendations() {
    if (recommendationState.isLoading) return;
    
    recommendationState.currentType = 'trending';
    const container = document.getElementById('recommendationsGrid');
    if (!container) return;

    showRecommendationLoading(container, 'Discovering trending movies...');
    recommendationState.isLoading = true;

    try {
        const data = await apiCall('/recommendations/trending?limit=12');
        if (data && data.movies) {
            recommendationState.recommendations = data.movies;
            displayRecommendations(data.movies, '🔥 Trending Now', 'Movies everyone is watching right now');
            updateRecommendationStats(data.movies);
        }
    } catch (error) {
        console.error('Failed to load trending recommendations:', error);
        showRecommendationError('Failed to load trending recommendations');
    } finally {
        recommendationState.isLoading = false;
    }
}

async function getPopularRecommendations() {
    if (recommendationState.isLoading) return;
    
    recommendationState.currentType = 'popular';
    const container = document.getElementById('recommendationsGrid');
    if (!container) return;

    showRecommendationLoading(container, 'Finding popular movies...');
    recommendationState.isLoading = true;

    try {
        const data = await apiCall('/recommendations/popular?limit=12');
        if (data && data.movies) {
            recommendationState.recommendations = data.movies;
            displayRecommendations(data.movies, '⭐ All-Time Popular', 'Classics and crowd favorites');
            updateRecommendationStats(data.movies);
        }
    } catch (error) {
        console.error('Failed to load popular recommendations:', error);
        showRecommendationError('Failed to load popular recommendations');
    } finally {
        recommendationState.isLoading = false;
    }
}

async function getMoodRecommendations(mood) {
    if (recommendationState.isLoading) return;
    
    recommendationState.currentType = 'mood';
    recommendationState.currentMood = mood;
    const container = document.getElementById('recommendationsGrid');
    if (!container) return;

    const moodTitles = {
        action: '💥 Action Packed',
        comedy: '😂 Hilarious Comedy',
        drama: '🎭 Powerful Drama',
        'sci-fi': '🚀 Sci-Fi Adventure',
        horror: '👻 Spine-Chilling Horror',
        family: '👨‍👩‍👧‍👦 Family Fun',
        romance: '💖 Heartwarming Romance',
        thriller: '🔍 Edge-of-Your-Seat Thriller'
    };

    showRecommendationLoading(container, `Finding ${mood} movies...`);
    recommendationState.isLoading = true;

    try {
        const data = await apiCall(`/recommendations/by-mood/${mood}?limit=12`);
        if (data && data.movies) {
            recommendationState.recommendations = data.movies;
            displayRecommendations(data.movies, moodTitles[mood] || `${mood.charAt(0).toUpperCase() + mood.slice(1)} Movies`, `Perfect for your ${mood} mood`);
            updateRecommendationStats(data.movies);
        }
    } catch (error) {
        console.error('Failed to load mood recommendations:', error);
        showRecommendationError('Failed to load mood-based recommendations');
    } finally {
        recommendationState.isLoading = false;
    }
}

async function getMovieBasedRecommendations(movieId) {
    if (recommendationState.isLoading) return;
    
    recommendationState.currentType = 'movie';
    const container = document.getElementById('recommendationsGrid');
    if (!container) return;

    const movieSelect = document.getElementById('movieSelect');
    const selectedMovie = movieSelect.options[movieSelect.selectedIndex].text;

    showRecommendationLoading(container, `Finding movies similar to "${selectedMovie}"...`);
    recommendationState.isLoading = true;

    try {
        const data = await apiCall(`/recommendations/by-movie/${movieId}?limit=12`);
        if (data && data.recommendations) {
            recommendationState.recommendations = data.recommendations;
            recommendationState.currentMovie = movieId;
            displayRecommendations(data.recommendations, `🎬 If You Liked "${selectedMovie}"`, 'You might enjoy these similar movies');
            updateRecommendationStats(data.recommendations);
        }
    } catch (error) {
        console.error('Failed to load movie-based recommendations:', error);
        showRecommendationError('Failed to load similar movies');
    } finally {
        recommendationState.isLoading = false;
    }
}

async function getPersonalizedRecommendations() {
    if (recommendationState.isLoading) return;
    
    recommendationState.currentType = 'personalized';
    const container = document.getElementById('recommendationsGrid');
    if (!container) return;

    const recommendationType = document.getElementById('recommendationType')?.value;
    const movieSelect = document.getElementById('movieSelect')?.value;

    showRecommendationLoading(container, 'Creating your personalized recommendations...');
    recommendationState.isLoading = true;

    try {
        let data;
        if (recommendationType === 'similar' && movieSelect) {
            data = await apiCall(`/recommendations/by-movie/${movieSelect}?limit=12`);
        } else if (recommendationType === 'mood') {
            const mood = document.getElementById('moodSelect')?.value || 'action';
            data = await apiCall(`/recommendations/by-mood/${mood}?limit=12`);
        } else {
            // Fallback to trending
            data = await apiCall('/recommendations/trending?limit=12');
        }

        if (data && (data.movies || data.recommendations)) {
            const recommendations = data.movies || data.recommendations;
            recommendationState.recommendations = recommendations;
            displayRecommendations(recommendations, '🎯 Personalized For You', 'Curated based on your preferences');
            updateRecommendationStats(recommendations);
            showSuccess('Personalized recommendations ready!');
        }
    } catch (error) {
        console.error('Failed to load personalized recommendations:', error);
        showRecommendationError('Failed to create personalized recommendations');
    } finally {
        recommendationState.isLoading = false;
    }
}

async function getSimilarRecommendations() {
    // Get recommendations based on watchlist and ratings
    const watchlist = JSON.parse(localStorage.getItem('watchlist') || '[]');
    const ratings = JSON.parse(localStorage.getItem('movieRatings') || '{}');
    
    if (watchlist.length === 0 && Object.keys(ratings).length === 0) {
        showInfo('Rate some movies or add them to your watchlist to get better recommendations!');
        getTrendingRecommendations();
        return;
    }

    // For demo purposes, we'll use trending movies
    // In a real app, this would call a personalized recommendation endpoint
    getTrendingRecommendations();
}

function displayRecommendations(movies, title, subtitle) {
    const container = document.getElementById('recommendationsGrid');
    const sectionTitle = document.querySelector('#recommendations .section-title');
    const sectionSubtitle = document.querySelector('#recommendations .section-subtitle');

    if (!container || !movies) return;

    if (movies.length === 0) {
        container.innerHTML = `
            <div class="no-recommendations">
                <i class="fas fa-search fa-3x"></i>
                <h3>No Recommendations Found</h3>
                <p>Try adjusting your criteria or try a different category</p>
                <button class="btn-primary" onclick="getTrendingRecommendations()">
                    Show Trending Movies
                </button>
            </div>
        `;
        return;
    }

    // Update section titles
    if (sectionTitle) sectionTitle.textContent = title;
    if (sectionSubtitle) sectionSubtitle.textContent = subtitle;

    container.innerHTML = movies.map((movie, index) => `
        <div class="recommendation-card" data-movie-id="${movie.id}" data-index="${index}">
            <div class="recommendation-badge">
                ${getRecommendationBadge(movie, index)}
            </div>
            <div class="movie-poster">
                <img src="${getMoviePoster(movie)}" alt="${movie.title}" 
                     onerror="this.src='data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjgwIiBoZWlnaHQ9IjQwMCIgdmlld0JveD0iMCAwIDI4MCA0MDAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIyODAiIGhlaWdodD0iNDAwIiBmaWxsPSIjMUMyQzJDIi8+CjxwYXRoIGQ9Ik0xNDAgMjAwTDE2MCAyMjBMMTQwIDI0MEwxMjAgMjIwTDE0MCAyMDBaIiBmaWxsPSIjOTk5Ii8+Cjx0ZXh0IHg9IjE0MCIgeT0iMjgwIiB0ZXh0LWFuY2hvcj0ibWlkZGxlIiBmaWxsPSIjOTk5IiBmb250LXNpemU9IjE0IiBmb250LWZhbWlseT0iQXJpYWwsIHNhbnMtc2VyaWYiPk5vIEltYWdlPC90ZXh0Pgo8L3N2Zz4K'">
                <div class="poster-overlay">
                    <div class="recommendation-score">
                        <i class="fas fa-heart"></i>
                        <span>${Math.floor(Math.random() * 20) + 80}% Match</span>
                    </div>
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
            <div class="recommendation-info">
                <h3 class="movie-title">${movie.title}</h3>
                <div class="movie-meta">
                    <span class="meta-year">${movie.releaseYear}</span>
                    <span class="meta-duration">${formatDuration(movie.duration)}</span>
                    <span class="meta-rating">
                        <i class="fas fa-star"></i> ${getMovieRating(movie)}
                    </span>
                </div>
                ${movie.genres && movie.genres.length > 0 ? `
                    <div class="genre-tags">
                        ${movie.genres.slice(0, 2).map(genre => `
                            <span class="genre-tag">${genre.name}</span>
                        `).join('')}
                    </div>
                ` : ''}
                ${movie.description ? `
                    <p class="movie-description">${truncateText(movie.description, 120)}</p>
                ` : ''}
                <div class="recommendation-reason">
                    <i class="fas fa-lightbulb"></i>
                    <span>${getRecommendationReason(movie, index)}</span>
                </div>
                <div class="recommendation-actions">
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

    // Add animation delays
    const cards = container.querySelectorAll('.recommendation-card');
    cards.forEach((card, index) => {
        card.style.animationDelay = `${index * 0.1}s`;
    });
}

function getRecommendationBadge(movie, index) {
    if (index < 3) {
        const badges = ['🏆 Top Pick', '🔥 Hot', '⭐ Featured'];
        return badges[index] || '💫 Recommended';
    }
    
    if (movie.rating >= 8) return '🌟 Must Watch';
    if (movie.releaseYear >= new Date().getFullYear() - 1) return '🆕 New';
    
    return '💫 Recommended';
}

function getRecommendationReason(movie, index) {
    const reasons = [
        'Similar to movies you enjoyed',
        'Highly rated by viewers like you',
        'Trending in your area',
        'Matches your favorite genres',
        'From directors you follow',
        'Critics are raving about this',
        'Perfect for your current mood',
        'Award-winning performance'
    ];
    
    return reasons[index % reasons.length];
}

function truncateText(text, length) {
    if (!text || text.length <= length) return text;
    return text.substring(0, length) + '...';
}

function showRecommendationLoading(container, message = 'Loading recommendations...') {
    container.innerHTML = `
        <div class="recommendation-loading">
            <div class="loading-spinner-large">
                <div class="spinner-ring"></div>
                <i class="fas fa-film"></i>
            </div>
            <h3>${message}</h3>
            <p>Finding the perfect movies for you...</p>
        </div>
    `;
}

function showRecommendationError(message) {
    const container = document.getElementById('recommendationsGrid');
    if (!container) return;

    container.innerHTML = `
        <div class="recommendation-error">
            <i class="fas fa-exclamation-triangle fa-3x"></i>
            <h3>Unable to Load Recommendations</h3>
            <p>${message}</p>
            <div class="error-actions">
                <button class="btn-primary" onclick="getTrendingRecommendations()">
                    Try Trending Movies
                </button>
                <button class="btn-secondary" onclick="location.reload()">
                    Reload Page
                </button>
            </div>
        </div>
    `;
}

function updateRecommendationStats(movies) {
    const statsElement = document.querySelector('.recommendation-stats');
    if (!statsElement || !movies) return;

    const avgRating = movies.reduce((sum, movie) => sum + (movie.rating || 0), 0) / movies.length;
    const recentMovies = movies.filter(movie => movie.releaseYear >= new Date().getFullYear() - 2).length;
    const highlyRated = movies.filter(movie => (movie.rating || 0) >= 8).length;

    statsElement.innerHTML = `
        <div class="stats-grid">
            <div class="stat">
                <span class="stat-number">${movies.length}</span>
                <span class="stat-label">Recommendations</span>
            </div>
            <div class="stat">
                <span class="stat-number">${avgRating.toFixed(1)}</span>
                <span class="stat-label">Avg Rating</span>
            </div>
            <div class="stat">
                <span class="stat-number">${recentMovies}</span>
                <span class="stat-label">Recent</span>
            </div>
            <div class="stat">
                <span class="stat-number">${highlyRated}</span>
                <span class="stat-label">Highly Rated</span>
            </div>
        </div>
    `;
}

function refreshRecommendations() {
    switch (recommendationState.currentType) {
        case 'trending':
            getTrendingRecommendations();
            break;
        case 'popular':
            getPopularRecommendations();
            break;
        case 'mood':
            getMoodRecommendations(recommendationState.currentMood);
            break;
        case 'movie':
            if (recommendationState.currentMovie) {
                getMovieBasedRecommendations(recommendationState.currentMovie);
            }
            break;
        case 'personalized':
            getPersonalizedRecommendations();
            break;
        default:
            getTrendingRecommendations();
    }
}

function showInfo(message) {
    const infoToast = document.createElement('div');
    infoToast.className = 'info-toast';
    infoToast.innerHTML = `
        <div class="toast-content">
            <i class="fas fa-info-circle"></i>
            <span>${message}</span>
        </div>
    `;
    
    document.body.appendChild(infoToast);

    setTimeout(() => {
        if (infoToast.parentNode) {
            infoToast.remove();
        }
    }, 4000);
}

// Export functions for global use
window.changeRecommendationType = changeRecommendationType;
window.getTrendingMovies = getTrendingRecommendations;
window.getPopularMovies = getPopularRecommendations;
window.getMoviesByMood = getMoodRecommendations;
window.getMovieRecommendations = getMovieBasedRecommendations;
window.getPersonalizedRecommendations = getPersonalizedRecommendations;
window.refreshRecommendations = refreshRecommendations;

// Initialize when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeRecommendations);
} else {
    initializeRecommendations();
}