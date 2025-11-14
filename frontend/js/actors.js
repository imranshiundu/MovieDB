// actors.js - Fixed with Enhanced Actor Modal UI
let actors = [];
let filteredActors = [];
let currentActorIndex = 0;

// Initialize actors functionality
document.addEventListener('DOMContentLoaded', function() {
    initializeActors();
});

async function initializeActors() {
    await loadActors();
    setupActorEventListeners();
    initializeActorSearch();
}

function setupActorEventListeners() {
    // Actor card interactions
    document.addEventListener('click', function(e) {
        if (e.target.closest('.btn-follow')) {
            e.preventDefault();
            e.stopPropagation();
            const actorId = e.target.closest('.actor-card').dataset.actorId;
            const button = e.target.closest('.btn-follow');
            toggleFollowActor(actorId, button);
        }
        
        if (e.target.closest('.btn-actor-info')) {
            e.preventDefault();
            e.stopPropagation();
            const actorId = e.target.closest('.actor-card').dataset.actorId;
            openActorModal(actorId);
        }
    });

    // Modal close button event listener
    const modalClose = document.getElementById('modalClose');
    if (modalClose) {
        modalClose.addEventListener('click', closeModal);
    }

    // Quick view on hover (desktop only)
    if (window.innerWidth > 768) {
        setupActorQuickView();
    }
}

function setupActorQuickView() {
    let quickViewTimer;
    
    document.addEventListener('mouseover', function(e) {
        const actorCard = e.target.closest('.actor-card');
        if (actorCard && !actorCard.classList.contains('show-quick-view')) {
            clearTimeout(quickViewTimer);
            quickViewTimer = setTimeout(() => {
                showActorQuickView(actorCard);
            }, 600);
        }
    });

    document.addEventListener('mouseout', function(e) {
        const actorCard = e.target.closest('.actor-card');
        if (actorCard) {
            clearTimeout(quickViewTimer);
            hideActorQuickView(actorCard);
        }
    });
}

function showActorQuickView(actorCard) {
    actorCard.classList.add('show-quick-view');
}

function hideActorQuickView(actorCard) {
    actorCard.classList.remove('show-quick-view');
}

async function loadActors() {
    const container = document.getElementById('actorsGrid');
    if (!container) return;

    showLoading(container);
    
    try {
        const data = await apiCall('/actors?page=0&size=50');
        if (data && data.content) {
            actors = data.content;
            filteredActors = [...actors];
            displayActors();
            updateActorStats();
            populateActorFilters();
        } else {
            // Fallback if data structure is different
            actors = Array.isArray(data) ? data : [];
            filteredActors = [...actors];
            displayActors();
            updateActorStats();
        }
    } catch (error) {
        console.error('Failed to load actors:', error);
        showError('Failed to load actors. Please try again.');
        // Show empty state
        displayActors();
    }
}

function displayActors() {
    const container = document.getElementById('actorsGrid');
    if (!container) return;

    if (filteredActors.length === 0) {
        container.innerHTML = `
            <div class="no-results">
                <i class="fas fa-user fa-3x"></i>
                <h3>No Actors Found</h3>
                <p>Try adjusting your search</p>
                <button class="btn-primary" onclick="clearActorFilters()">Clear Search</button>
            </div>
        `;
        return;
    }

    container.innerHTML = filteredActors.map(actor => `
        <div class="actor-card" data-actor-id="${actor.id}">
            <div class="actor-image">
                <div class="image-container">
                    <img src="${getActorImage(actor)}" alt="${actor.name}" onerror="this.src='data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjgwIiBoZWlnaHQ9IjM1MCIgdmlld0JveD0iMCAwIDI4MCAzNTAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+CjxyZWN0IHdpZHRoPSIyODAiIGhlaWdodD0iMzUwIiBmaWxsPSIjMUMyQzJDIi8+CjxjaXJjbGUgY3g9IjE0MCIgY3k9IjEyMCIgcj0iNTAiIGZpbGw9IiM5OTk5OTkiLz4KPHBhdGggZD0iTTE0MCAyMDBMMTYwIDIyMEwxNDAgMjQwTDEyMCAyMjBMMTQwIDIwMFoiIGZpbGw9IiM5OTk5OTkiLz4KPHRleHQgeD0iMTQwIiB5PSIyODAiIHRleHQtYW5jaG9yPSJtaWRkbGUiIGZpbGw9IiM5OTk5OTkiIGZvbnQtc2l6ZT0iMTQiIGZvbnQtZmFtaWx5PSJBcmlhbCwgc2Fucy1zZXJpZiI+Tm8gSW1hZ2U8L3RleHQ+Cjwvc3ZnPgo='">
                    <div class="image-overlay">
                        <div class="actor-badge">
                            <i class="fas fa-film"></i>
                            <span>${actor.movies ? actor.movies.length : 0}</span>
                        </div>
                        <div class="overlay-actions">
                            <button class="btn-follow ${isFollowingActor(actor.id) ? 'following' : ''}" 
                                    onclick="event.stopPropagation(); toggleFollowActor(${actor.id}, this)">
                                <i class="fas ${isFollowingActor(actor.id) ? 'fa-user-check' : 'fa-user-plus'}"></i>
                            </button>
                            <button class="btn-actor-info" onclick="event.stopPropagation(); openActorModal(${actor.id})">
                                <i class="fas fa-info-circle"></i>
                            </button>
                        </div>
                    </div>
                </div>
                <div class="actor-quick-view">
                    <div class="quick-stats">
                        <div class="stat">
                            <i class="fas fa-film"></i>
                            <span>${actor.movies ? actor.movies.length : 0} Movies</span>
                        </div>
                        <div class="stat">
                            <i class="fas fa-birthday-cake"></i>
                            <span>${calculateAge(actor.birthDate)} years</span>
                        </div>
                    </div>
                    <button class="btn-quick-view" onclick="event.stopPropagation(); openActorModal(${actor.id})">
                        <i class="fas fa-eye"></i> View Profile
                    </button>
                </div>
            </div>
            <div class="actor-info">
                <h3 class="actor-name">${actor.name}</h3>
                <div class="actor-meta">
                    <span class="meta-birthdate">${formatDate(actor.birthDate)}</span>
                    <span class="meta-age">${calculateAge(actor.birthDate)} years old</span>
                </div>
                ${actor.movies && actor.movies.length > 0 ? `
                    <div class="actor-movies-preview">
                        <p class="preview-title">Known For:</p>
                        <div class="movie-tags">
                            ${actor.movies.slice(0, 3).map(movie => `
                                <span class="movie-tag">${movie.title}</span>
                            `).join('')}
                            ${actor.movies.length > 3 ? `<span class="movie-tag-more">+${actor.movies.length - 3} more</span>` : ''}
                        </div>
                    </div>
                ` : ''}
                <div class="actor-actions">
                    <button class="btn-follow ${isFollowingActor(actor.id) ? 'following' : ''}" 
                            onclick="toggleFollowActor(${actor.id}, this)">
                        <i class="fas ${isFollowingActor(actor.id) ? 'fa-user-check' : 'fa-user-plus'}"></i>
                        ${isFollowingActor(actor.id) ? 'Following' : 'Follow'}
                    </button>
                    <button class="btn-actor-info" onclick="openActorModal(${actor.id})">
                        <i class="fas fa-info-circle"></i> Profile
                    </button>
                </div>
            </div>
        </div>
    `).join('');

    // Add intersection observer for lazy loading
    setupActorLazyLoading();
}

function getActorImage(actor) {
    // In a real app, this would come from the API
    return `https://via.placeholder.com/280x350/1C2C2C/999999?text=${encodeURIComponent(actor.name.split(' ')[0])}`;
}

function setupActorLazyLoading() {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                const img = entry.target;
                img.src = img.dataset.src;
                observer.unobserve(img);
            }
        });
    });

    document.querySelectorAll('.actor-image img[data-src]').forEach(img => {
        observer.observe(img);
    });
}

function calculateAge(birthDate) {
    if (!birthDate) return 'Unknown';
    
    const today = new Date();
    const birth = new Date(birthDate);
    let age = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();
    
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) {
        age--;
    }
    
    return age;
}

function calculateCareerLength(birthDate, firstMovieYear) {
    if (!birthDate || !firstMovieYear) return 'Unknown';
    
    const birthYear = new Date(birthDate).getFullYear();
    const careerStart = Math.max(birthYear + 18, firstMovieYear); // Assume career starts at 18
    const currentYear = new Date().getFullYear();
    
    return currentYear - careerStart;
}

function populateActorFilters() {
    // Could add filters for age range, movie count, etc.
    console.log('Actor filters populated');
}

function initializeActorSearch() {
    const searchInput = document.getElementById('actorSearch');
    if (searchInput) {
        searchInput.addEventListener('input', debounce(searchActors, 300));
    }
}

const searchActors = debounce(function() {
    const searchTerm = document.getElementById('actorSearch')?.value.toLowerCase() || '';

    filteredActors = actors.filter(actor => 
        actor.name.toLowerCase().includes(searchTerm) ||
        (actor.alsoKnownAs && actor.alsoKnownAs.some(alias => 
            alias.toLowerCase().includes(searchTerm)
        ))
    );

    displayActors();
    updateActorStats();
}, 300);

function clearActorFilters() {
    const searchInput = document.getElementById('actorSearch');
    if (searchInput) {
        searchInput.value = '';
        searchActors();
    }
}

function updateActorStats() {
    const statsElement = document.querySelector('.actors-stats');
    if (!statsElement) return;

    const totalActors = filteredActors.length;
    const averageAge = Math.round(filteredActors.reduce((sum, actor) => {
        const age = calculateAge(actor.birthDate);
        return sum + (typeof age === 'number' ? age : 0);
    }, 0) / totalActors);

    statsElement.innerHTML = `
        <div class="stats-grid">
            <div class="stat">
                <span class="stat-number">${totalActors}</span>
                <span class="stat-label">Actors</span>
            </div>
            <div class="stat">
                <span class="stat-number">${averageAge || 'N/A'}</span>
                <span class="stat-label">Avg Age</span>
            </div>
            <div class="stat">
                <span class="stat-number">${new Set(filteredActors.flatMap(a => a.movies || [])).size}</span>
                <span class="stat-label">Total Movies</span>
            </div>
        </div>
    `;
}

async function openActorModal(actorId) {
    const modal = document.getElementById('movieModal');
    const modalContent = document.getElementById('modalContent');
    
    if (!modal || !modalContent) return;

    showLoading(modalContent);
    modal.style.display = 'block';
    document.body.classList.add('modal-open');

    try {
        const actor = await apiCall(`/actors/${actorId}`);
        if (actor) {
            currentActorIndex = filteredActors.findIndex(a => a.id === actorId);
            const movies = await apiCall(`/actors/${actorId}/movies`);
            displayActorModal(actor, movies, modalContent);
            setupActorModalNavigation();
        } else {
            throw new Error('Actor not found');
        }
    } catch (error) {
        console.error('Failed to load actor details:', error);
        modalContent.innerHTML = `
            <div class="modal-error">
                <i class="fas fa-exclamation-triangle"></i>
                <h3>Failed to Load Actor Profile</h3>
                <p>Please try again later.</p>
                <button class="btn-primary" onclick="closeModal()">Close</button>
            </div>
        `;
    }
}

function displayActorModal(actor, movies, container) {
    const age = calculateAge(actor.birthDate);
    const firstMovieYear = movies && movies.length > 0 ? 
        Math.min(...movies.map(m => m.releaseYear)) : null;
    const careerLength = calculateCareerLength(actor.birthDate, firstMovieYear);

    container.innerHTML = `
        <div class="actor-modal">
            <div class="modal-header">
                <div class="modal-photo-large">
                    <img src="${getActorImage(actor)}" alt="${actor.name}" class="modal-actor-photo">
                    <div class="photo-actions-modal">
                        <button class="btn-action btn-follow ${isFollowingActor(actor.id) ? 'following' : ''}" 
                                onclick="toggleFollowActor(${actor.id}, this)">
                            <i class="fas ${isFollowingActor(actor.id) ? 'fa-user-check' : 'fa-user-plus'}"></i>
                            <span>${isFollowingActor(actor.id) ? 'Following' : 'Follow Actor'}</span>
                        </button>
                        <button class="btn-action btn-share" onclick="shareActorProfile(${actor.id})">
                            <i class="fas fa-share-alt"></i>
                            <span>Share Profile</span>
                        </button>
                    </div>
                </div>
                <div class="modal-info">
                    <div class="modal-header-top">
                        <h1 class="modal-title">${actor.name}</h1>
                        <button class="modal-close" onclick="closeModal()">
                            <i class="fas fa-times"></i>
                        </button>
                    </div>
                    
                    <div class="actor-bio-stats-grid">
                        <div class="bio-stat">
                            <div class="bio-icon">
                                <i class="fas fa-birthday-cake"></i>
                            </div>
                            <div class="bio-info">
                                <strong>Born</strong>
                                <span>${formatDate(actor.birthDate)}</span>
                            </div>
                        </div>
                        <div class="bio-stat">
                            <div class="bio-icon">
                                <i class="fas fa-user"></i>
                            </div>
                            <div class="bio-info">
                                <strong>Age</strong>
                                <span>${age} years</span>
                            </div>
                        </div>
                        ${careerLength !== 'Unknown' ? `
                            <div class="bio-stat">
                                <div class="bio-icon">
                                    <i class="fas fa-film"></i>
                                </div>
                                <div class="bio-info">
                                    <strong>Career</strong>
                                    <span>${careerLength} years</span>
                                </div>
                            </div>
                        ` : ''}
                        <div class="bio-stat">
                            <div class="bio-icon">
                                <i class="fas fa-theater-masks"></i>
                            </div>
                            <div class="bio-info">
                                <strong>Movies</strong>
                                <span>${movies ? movies.length : 0}</span>
                            </div>
                        </div>
                    </div>

                    ${actor.biography ? `
                        <div class="actor-biography">
                            <h3><i class="fas fa-scroll"></i> Biography</h3>
                            <p class="biography-text">${actor.biography}</p>
                            ${actor.biography.length > 300 ? `
                                <button class="btn-read-more" onclick="toggleBiography(this)">Read More</button>
                            ` : ''}
                        </div>
                    ` : ''}

                    ${actor.alsoKnownAs && actor.alsoKnownAs.length > 0 ? `
                        <div class="also-known-as">
                            <h4><i class="fas fa-tags"></i> Also Known As</h4>
                            <div class="aka-tags">
                                ${actor.alsoKnownAs.slice(0, 5).map(aka => `
                                    <span class="aka-tag">${aka}</span>
                                `).join('')}
                            </div>
                        </div>
                    ` : ''}
                </div>
            </div>
            
            <div class="modal-body">
                ${movies && movies.length > 0 ? `
                    <section class="modal-section">
                        <div class="section-header">
                            <h3><i class="fas fa-film"></i> Filmography</h3>
                            <span class="section-count">${movies.length} movies</span>
                        </div>
                        <div class="filmography-grid">
                            ${movies.slice(0, 12).map(movie => `
                                <div class="filmography-item" onclick="openMovieModal(${movie.id})">
                                    <div class="movie-poster-small">
                                        <img src="${getMoviePoster(movie)}" alt="${movie.title}">
                                        <div class="poster-overlay-small">
                                            <span class="movie-year">${movie.releaseYear}</span>
                                            <span class="movie-rating-small">
                                                <i class="fas fa-star"></i> ${getMovieRating(movie)}
                                            </span>
                                        </div>
                                    </div>
                                    <div class="movie-info-small">
                                        <h4 class="movie-title-small">${movie.title}</h4>
                                        <div class="movie-meta-small">
                                            <span class="meta-duration">${formatDuration(movie.duration)}</span>
                                            ${movie.genres && movie.genres.length > 0 ? `
                                                <span class="meta-genre">${movie.genres[0].name}</span>
                                            ` : ''}
                                        </div>
                                    </div>
                                </div>
                            `).join('')}
                        </div>
                        ${movies.length > 12 ? `
                            <div class="show-more-container">
                                <button class="btn-show-more" onclick="showAllActorMovies(${actor.id})">
                                    <i class="fas fa-chevron-down"></i>
                                    Show All ${movies.length} Movies
                                </button>
                            </div>
                        ` : ''}
                    </section>
                ` : `
                    <section class="modal-section">
                        <div class="no-filmography">
                            <i class="fas fa-film fa-2x"></i>
                            <h4>No Filmography Available</h4>
                            <p>Movie data for this actor is currently unavailable.</p>
                        </div>
                    </section>
                `}
                
                ${actor.trivia && actor.trivia.length > 0 ? `
                    <section class="modal-section">
                        <h3><i class="fas fa-lightbulb"></i> Trivia</h3>
                        <div class="trivia-list">
                            ${actor.trivia.slice(0, 5).map((fact, index) => `
                                <div class="trivia-item">
                                    <span class="trivia-number">${index + 1}</span>
                                    <p class="trivia-text">${fact}</p>
                                </div>
                            `).join('')}
                        </div>
                    </section>
                ` : ''}
            </div>
            
            <div class="modal-navigation">
                <button class="btn-nav btn-prev" onclick="navigateToAdjacentActor(-1)" ${currentActorIndex <= 0 ? 'disabled' : ''}>
                    <i class="fas fa-chevron-left"></i> Previous Actor
                </button>
                <button class="btn-nav btn-next" onclick="navigateToAdjacentActor(1)" ${currentActorIndex >= filteredActors.length - 1 ? 'disabled' : ''}>
                    Next Actor <i class="fas fa-chevron-right"></i>
                </button>
            </div>
        </div>
    `;

    // Setup biography read more functionality
    setupBiographyToggle();
}

function setupActorModalNavigation() {
    const modalKeyHandler = function(e) {
        if (e.key === 'Escape') {
            closeModal();
        }
        if (e.key === 'ArrowLeft') {
            navigateToAdjacentActor(-1);
        } else if (e.key === 'ArrowRight') {
            navigateToAdjacentActor(1);
        }
    };

    document.addEventListener('keydown', modalKeyHandler);
    
    // Store reference to remove later
    window.currentActorModalKeyHandler = modalKeyHandler;
}

function navigateToAdjacentActor(direction) {
    const newIndex = currentActorIndex + direction;
    if (newIndex >= 0 && newIndex < filteredActors.length) {
        const nextActorId = filteredActors[newIndex].id;
        openActorModal(nextActorId);
    }
}

function setupBiographyToggle() {
    window.toggleBiography = function(button) {
        const biography = button.closest('.actor-biography');
        const text = biography.querySelector('.biography-text');
        
        text.classList.toggle('expanded');
        button.innerHTML = text.classList.contains('expanded') ? 
            '<i class="fas fa-chevron-up"></i> Read Less' : 
            '<i class="fas fa-chevron-down"></i> Read More';
    };
}

function toggleFollowActor(actorId, button) {
    let following = JSON.parse(localStorage.getItem('followingActors') || '[]');
    
    const isFollowing = following.includes(actorId);
    
    if (isFollowing) {
        // Unfollow
        following = following.filter(id => id !== actorId);
        button.innerHTML = `<i class="fas fa-user-plus"></i> Follow`;
        button.classList.remove('following');
        showSuccess(`Unfollowed ${getActorName(actorId)}`);
    } else {
        // Follow
        following.push(actorId);
        button.innerHTML = `<i class="fas fa-user-check"></i> Following`;
        button.classList.add('following');
        showSuccess(`Now following ${getActorName(actorId)}`);
    }
    
    localStorage.setItem('followingActors', JSON.stringify(following));
    
    // Update all instances of this actor's follow button
    updateAllFollowButtons(actorId, !isFollowing);
}

function isFollowingActor(actorId) {
    const following = JSON.parse(localStorage.getItem('followingActors') || '[]');
    return following.includes(actorId);
}

function getActorName(actorId) {
    const actor = actors.find(a => a.id === actorId);
    return actor ? actor.name : 'Actor';
}

function updateAllFollowButtons(actorId, isFollowing) {
    document.querySelectorAll(`.actor-card[data-actor-id="${actorId}"] .btn-follow`).forEach(button => {
        button.innerHTML = `<i class="fas ${isFollowing ? 'fa-user-check' : 'fa-user-plus'}"></i> ${isFollowing ? 'Following' : 'Follow'}`;
        button.classList.toggle('following', isFollowing);
    });
}

function shareActorProfile(actorId) {
    const actor = actors.find(a => a.id === actorId);
    if (actor && navigator.share) {
        navigator.share({
            title: `${actor.name} - MovieDB`,
            text: `Check out ${actor.name}'s profile on MovieDB`,
            url: window.location.href
        });
    } else {
        // Fallback: copy to clipboard
        const profileUrl = `${window.location.origin}#actors`;
        navigator.clipboard.writeText(`Check out ${actor.name} on MovieDB: ${profileUrl}`);
        showSuccess('Profile link copied to clipboard!');
    }
}

function showAllActorMovies(actorId) {
    // In a full implementation, this would show a dedicated movies page for the actor
    openActorModal(actorId);
    showSuccess('Showing all movies for this actor');
}

// Export functions for global use
window.loadActors = loadActors;
window.searchActors = searchActors;
window.showActorDetails = openActorModal;
window.openActorModal = openActorModal;
window.clearActorFilters = clearActorFilters;
window.navigateToAdjacentActor = navigateToAdjacentActor;
window.toggleFollowActor = toggleFollowActor;
window.shareActorProfile = shareActorProfile;
window.showAllActorMovies = showAllActorMovies;
window.toggleBiography = toggleBiography;

// Initialize when DOM is ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeActors);
} else {
    initializeActors();
}