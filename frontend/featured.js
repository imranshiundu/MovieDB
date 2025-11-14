// featured.js - Featured Movies Functionality
let featuredMovies = [];
let currentFeaturedFilter = 'trending';

async function loadFeaturedMovies(filter = 'trending') {
    const container = document.getElementById('featuredGrid');
    if (!container) return;

    showLoading(container);
    currentFeaturedFilter = filter;

    try {
        let data;
        switch(filter) {
            case 'trending':
                data = await apiCall('/movies/trending?size=12');
                break;
            case 'popular':
                data = await apiCall('/movies/popular?size=12');
                break;
            case 'new':
                data = await apiCall('/movies?sort=releaseYear,desc&size=12');
                break;
            default:
                data = await apiCall('/movies?size=12');
        }

        if (data && data.content) {
            featuredMovies = data.content;
            displayFeaturedMovies();
        } else if (data && Array.isArray(data)) {
            featuredMovies = data;
            displayFeaturedMovies();
        } else {
            showError('No featured movies found');
        }
    } catch (error) {
        console.error('Failed to load featured movies:', error);
        showError('Failed to load featured movies');
    }
}

function displayFeaturedMovies() {
    const container = document.getElementById('featuredGrid');
    if (!container) return;

    if (featuredMovies.length === 0) {
        container.innerHTML = `
            <div class="no-results">
                <i class="fas fa-film fa-3x"></i>
                <h3>No Featured Movies</h3>
                <p>Try a different filter</p>
            </div>
        `;
        return;
    }

    container.innerHTML = featuredMovies.map((movie, index) => `
        <div class="featured-card" data-movie-id="${movie.id}">
            <div class="featured-badge">
                ${getFeaturedBadge(index)}
            </div>
            <div class="movie-poster">
                <img src="${getMoviePoster(movie)}" alt="${movie.title}">
                <div class="poster-overlay">
                    <div class="featured-score">
                        <i class="fas fa-fire"></i>
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
            <div class="featured-info">
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
                <div class="featured-actions">
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
}

function getFeaturedBadge(index) {
    const badges = ['🏆 #1 Trending', '🔥 Hot', '⭐ Popular', '🎬 New'];
    return badges[index] || '💫 Featured';
}

// Export functions
window.loadFeaturedMovies = loadFeaturedMovies;