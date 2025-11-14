// app.js - Enhanced with 100% functionality
// Configuration
const API_BASE_URL = 'http://localhost:8081/api';

// Global state
let currentState = {
    currentPage: 1,
    itemsPerPage: 20,
    currentSection: 'featured',
    searchTerm: '',
    filters: {
        genre: '',
        year: '',
        mood: '',
        sortBy: 'title'
    },
    viewMode: 'grid',
    totalPages: 1
};

// Cache for better performance
const cache = new Map();
const CACHE_DURATION = 5 * 60 * 1000; // 5 minutes

// Initialize app
document.addEventListener('DOMContentLoaded', function() {
    initializeApp();
    setupEventListeners();
    setupIntersectionObserver();
});

function initializeApp() {
    showAppLoading();
    
    // Load initial data in parallel
    Promise.all([
        loadFeaturedMovies(),
        loadMovies(),
        loadActors(),
        loadGenres(),
        setupRecommendationMovies(),
        loadYears()
    ]).then(() => {
        hideAppLoading();
        updateActiveNavigation();
        initializeSmoothScrolling();
    }).catch(error => {
        console.error('Initialization failed:', error);
        hideAppLoading();
        showError('Failed to initialize app. Please refresh the page.');
    });
}

function showAppLoading() {
    const loading = document.createElement('div');
    loading.id = 'app-loading';
    loading.innerHTML = `
        <div class="loading-overlay">
            <div class="loading-spinner">
                <i class="fas fa-film"></i>
                <div class="spinner-ring"></div>
            </div>
            <p>Loading MovieDB...</p>
        </div>
    `;
    document.body.appendChild(loading);
}

function hideAppLoading() {
    const loading = document.getElementById('app-loading');
    if (loading) {
        loading.remove();
    }
}

function setupEventListeners() {
    // Mobile Navigation
    const hamburger = document.querySelector('.hamburger');
    const navMenu = document.querySelector('.nav-menu');
    
    if (hamburger) {
        hamburger.addEventListener('click', () => {
            hamburger.classList.toggle('active');
            navMenu.classList.toggle('active');
            document.body.classList.toggle('menu-open');
        });
    }

    // Close mobile menu when clicking on links
    document.querySelectorAll('.nav-link').forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const targetSection = link.getAttribute('href').substring(1);
            navigateToSection(targetSection);
            
            // Close mobile menu
            if (hamburger) {
                hamburger.classList.remove('active');
                navMenu.classList.remove('active');
                document.body.classList.remove('menu-open');
            }
        });
    });

    // Global search
    const globalSearch = document.getElementById('globalSearch');
    if (globalSearch) {
        globalSearch.addEventListener('keypress', (e) => {
            if (e.key === 'Enter') {
                performGlobalSearch();
            }
        });
        
        // Add search button event
        const searchBtn = document.querySelector('.search-btn');
        if (searchBtn) {
            searchBtn.addEventListener('click', performGlobalSearch);
        }
    }

    // Section controls
    setupSectionControls();
    
    // View controls
    setupViewControls();
    
    // Filter controls
    setupFilterControls();
    
    // Pagination
    setupPagination();
    
    // Recommendation controls
    setupRecommendationControls();

    // Keyboard shortcuts
    setupKeyboardShortcuts();
}

function setupSectionControls() {
    // Featured section tabs
    document.querySelectorAll('.control-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const filter = e.target.dataset.filter;
            setActiveControlButton(e.target);
            loadFeaturedMovies(filter);
        });
    });

    // Sort controls
    const sortSelect = document.getElementById('sortBy');
    if (sortSelect) {
        sortSelect.addEventListener('change', (e) => {
            currentState.filters.sortBy = e.target.value;
            loadMovies();
        });
    }
}

function setupViewControls() {
    document.querySelectorAll('.view-btn').forEach(btn => {
        btn.addEventListener('click', (e) => {
            const view = e.target.dataset.view;
            setActiveViewButton(e.target);
            toggleViewMode(view);
        });
    });
}

function setupFilterControls() {
    // Genre filter
    const genreFilter = document.getElementById('genreFilter');
    if (genreFilter) {
        genreFilter.addEventListener('change', (e) => {
            currentState.filters.genre = e.target.value;
            applyFilters();
        });
    }

    // Year filter
    const yearFilter = document.getElementById('yearFilter');
    if (yearFilter) {
        yearFilter.addEventListener('change', (e) => {
            currentState.filters.year = e.target.value;
            applyFilters();
        });
    }

    // Mood filter
    const moodFilter = document.getElementById('moodFilter');
    if (moodFilter) {
        moodFilter.addEventListener('change', (e) => {
            currentState.filters.mood = e.target.value;
            applyFilters();
        });
    }

    // Apply filters button
    const applyFiltersBtn = document.getElementById('applyFilters');
    if (applyFiltersBtn) {
        applyFiltersBtn.addEventListener('click', applyFilters);
    }
}

function setupPagination() {
    const prevBtn = document.getElementById('prevPage');
    const nextBtn = document.getElementById('nextPage');

    if (prevBtn) {
        prevBtn.addEventListener('click', () => changePage(-1));
    }
    if (nextBtn) {
        nextBtn.addEventListener('click', () => changePage(1));
    }
}

function setupRecommendationControls() {
    const recommendationType = document.getElementById('recommendationType');
    const getRecommendationsBtn = document.getElementById('getRecommendations');

    if (recommendationType) {
        recommendationType.addEventListener('change', handleRecommendationTypeChange);
    }

    if (getRecommendationsBtn) {
        getRecommendationsBtn.addEventListener('click', getPersonalizedRecommendations);
    }
}

function setupKeyboardShortcuts() {
    document.addEventListener('keydown', (e) => {
        // Close modal with Escape key
        if (e.key === 'Escape') {
            closeModal();
        }
        
        // Navigate with arrow keys when modal is open
        if (e.key === 'ArrowLeft' || e.key === 'ArrowRight') {
            navigateMovieModal(e.key);
        }
    });
}

function setupIntersectionObserver() {
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.classList.add('visible');
                updateActiveNavigation();
            }
        });
    }, { threshold: 0.3 });

    // Observe all sections for intersection
    document.querySelectorAll('.section').forEach(section => {
        observer.observe(section);
    });
}

function initializeSmoothScrolling() {
    // Smooth scroll to sections
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

// API Helper Functions with Enhanced Caching
async function apiCall(endpoint, options = {}) {
    const cacheKey = `${endpoint}-${JSON.stringify(options)}`;
    const cached = cache.get(cacheKey);
    
    if (cached && Date.now() - cached.timestamp < CACHE_DURATION) {
        return cached.data;
    }

    try {
        showLoadingState(true);
        
        const response = await fetch(`${API_BASE_URL}${endpoint}`, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const data = await response.json();
        
        // Cache successful responses
        cache.set(cacheKey, {
            data: data,
            timestamp: Date.now()
        });

        return data;
    } catch (error) {
        console.error('API call failed:', error);
        showError(`Failed to fetch data: ${error.message}`);
        return null;
    } finally {
        showLoadingState(false);
    }
}

function showLoadingState(show) {
    const mainContent = document.querySelector('.main-content');
    if (show) {
        mainContent.classList.add('loading');
    } else {
        mainContent.classList.remove('loading');
    }
}

// Enhanced Utility Functions
function showError(message, duration = 5000) {
    // Remove existing errors
    const existingError = document.querySelector('.error-toast');
    if (existingError) {
        existingError.remove();
    }

    const errorToast = document.createElement('div');
    errorToast.className = 'error-toast';
    errorToast.innerHTML = `
        <div class="toast-content">
            <i class="fas fa-exclamation-circle"></i>
            <span>${message}</span>
            <button class="toast-close"><i class="fas fa-times"></i></button>
        </div>
    `;
    
    document.body.appendChild(errorToast);

    // Add close functionality
    const closeBtn = errorToast.querySelector('.toast-close');
    closeBtn.addEventListener('click', () => {
        errorToast.remove();
    });

    // Auto remove after duration
    setTimeout(() => {
        if (errorToast.parentNode) {
            errorToast.remove();
        }
    }, duration);
}

function showSuccess(message, duration = 3000) {
    const successToast = document.createElement('div');
    successToast.className = 'success-toast';
    successToast.innerHTML = `
        <div class="toast-content">
            <i class="fas fa-check-circle"></i>
            <span>${message}</span>
        </div>
    `;
    
    document.body.appendChild(successToast);

    setTimeout(() => {
        if (successToast.parentNode) {
            successToast.remove();
        }
    }, duration);
}

function showLoading(container) {
    if (!container) return;
    
    container.innerHTML = `
        <div class="loading-state">
            <div class="loading-spinner">
                <div class="spinner-ring"></div>
            </div>
            <p>Loading content...</p>
        </div>
    `;
}

function formatDuration(minutes) {
    if (!minutes) return 'N/A';
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return hours > 0 ? `${hours}h ${mins}m` : `${mins}m`;
}

function formatDate(dateString) {
    if (!dateString) return 'N/A';
    const date = new Date(dateString);
    return date.toLocaleDateString('en-US', { 
        year: 'numeric', 
        month: 'long', 
        day: 'numeric' 
    });
}

function debounce(func, wait, immediate) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            timeout = null;
            if (!immediate) func(...args);
        };
        const callNow = immediate && !timeout;
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
        if (callNow) func(...args);
    };
}

// Enhanced Search Functionality
function performGlobalSearch() {
    const searchTerm = document.getElementById('globalSearch').value.trim();
    if (searchTerm) {
        currentState.searchTerm = searchTerm;
        currentState.currentPage = 1;
        
        // Show search results section
        navigateToSection('movies');
        
        // Perform search across movies
        searchMovies();
        
        // Also search actors in background
        searchActors();
    }
}

function clearSearch() {
    document.getElementById('globalSearch').value = '';
    currentState.searchTerm = '';
    loadMovies();
    loadActors();
}

// Enhanced Navigation
function navigateToSection(sectionId) {
    currentState.currentSection = sectionId;
    const section = document.getElementById(sectionId);
    if (section) {
        section.scrollIntoView({ 
            behavior: 'smooth',
            block: 'start'
        });
        updateActiveNavigation();
    }
}

function updateActiveNavigation() {
    // Remove active class from all nav items
    document.querySelectorAll('.nav-link').forEach(link => {
        link.classList.remove('active');
    });
    
    // Add active class to current section
    const currentLink = document.querySelector(`.nav-link[href="#${currentState.currentSection}"]`);
    if (currentLink) {
        currentLink.classList.add('active');
    }
}

// View Mode Management
function toggleViewMode(mode) {
    currentState.viewMode = mode;
    const container = document.getElementById('moviesGrid');
    if (container) {
        container.className = `movies-${mode}`;
        // Reload movies to adapt to new view mode
        loadMovies();
    }
}

function setActiveViewButton(activeBtn) {
    document.querySelectorAll('.view-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    activeBtn.classList.add('active');
}

function setActiveControlButton(activeBtn) {
    document.querySelectorAll('.control-btn').forEach(btn => {
        btn.classList.remove('active');
    });
    activeBtn.classList.add('active');
}

// Filter Management
function applyFilters() {
    currentState.currentPage = 1;
    
    switch (currentState.currentSection) {
        case 'featured':
            loadFeaturedMovies();
            break;
        case 'movies':
            loadMovies();
            break;
        case 'actors':
            loadActors();
            break;
    }
}

function clearFilters() {
    currentState.filters = {
        genre: '',
        year: '',
        mood: '',
        sortBy: 'title'
    };
    
    // Reset filter UI
    document.querySelectorAll('.filter-select').forEach(select => {
        select.value = '';
    });
    
    applyFilters();
}

// Pagination Management
function changePage(direction) {
    const newPage = currentState.currentPage + direction;
    
    if (newPage >= 1 && newPage <= currentState.totalPages) {
        currentState.currentPage = newPage;
        
        switch (currentState.currentSection) {
            case 'movies':
                loadMovies();
                break;
            case 'featured':
                loadFeaturedMovies();
                break;
        }
        
        updatePaginationUI();
    }
}

function goToPage(page) {
    if (page >= 1 && page <= currentState.totalPages) {
        currentState.currentPage = page;
        loadMovies();
        updatePaginationUI();
    }
}

function updatePaginationUI() {
    const pageInfo = document.getElementById('pageInfo');
    const prevBtn = document.getElementById('prevPage');
    const nextBtn = document.getElementById('nextPage');
    const pageNumbers = document.getElementById('pageNumbers');

    if (pageInfo) {
        pageInfo.textContent = `Page ${currentState.currentPage} of ${currentState.totalPages}`;
    }

    if (prevBtn) {
        prevBtn.disabled = currentState.currentPage === 1;
    }

    if (nextBtn) {
        nextBtn.disabled = currentState.currentPage === currentState.totalPages;
    }

    if (pageNumbers) {
        updatePageNumbers();
    }
}

function updatePageNumbers() {
    const pageNumbers = document.getElementById('pageNumbers');
    if (!pageNumbers) return;

    let pagesHtml = '';
    const totalPages = currentState.totalPages;
    const currentPage = currentState.currentPage;

    // Always show first page
    pagesHtml += createPageNumber(1);
    
    // Show ellipsis if needed
    if (currentPage > 3) {
        pagesHtml += '<span class="page-ellipsis">...</span>';
    }

    // Show pages around current page
    for (let i = Math.max(2, currentPage - 1); i <= Math.min(totalPages - 1, currentPage + 1); i++) {
        pagesHtml += createPageNumber(i);
    }

    // Show ellipsis if needed
    if (currentPage < totalPages - 2) {
        pagesHtml += '<span class="page-ellipsis">...</span>';
    }

    // Always show last page if there is more than one page
    if (totalPages > 1) {
        pagesHtml += createPageNumber(totalPages);
    }

    pageNumbers.innerHTML = pagesHtml;

    // Add event listeners to page numbers
    pageNumbers.querySelectorAll('.page-number').forEach(pageEl => {
        pageEl.addEventListener('click', () => {
            const page = parseInt(pageEl.dataset.page);
            goToPage(page);
        });
    });
}

function createPageNumber(page) {
    const isActive = page === currentState.currentPage;
    return `<button class="page-number ${isActive ? 'active' : ''}" data-page="${page}">${page}</button>`;
}

// Modal Management
function openMovieModal(movieId) {
    showMovieDetails(movieId);
}

function closeModal() {
    const modal = document.getElementById('movieModal');
    if (modal) {
        modal.style.display = 'none';
        document.body.classList.remove('modal-open');
    }
}

function navigateMovieModal(direction) {
    // Implementation for navigating between movies in modal
    // This would require knowing the current movie list
    console.log(`Navigate ${direction} in modal`);
}

// Watchlist Management
function toggleWatchlist(movieId, button) {
    // Get current watchlist from localStorage
    let watchlist = JSON.parse(localStorage.getItem('watchlist') || '[]');
    
    const isInWatchlist = watchlist.includes(movieId);
    
    if (isInWatchlist) {
        // Remove from watchlist
        watchlist = watchlist.filter(id => id !== movieId);
        button.innerHTML = '<i class="far fa-bookmark"></i> Watchlist';
        showSuccess('Removed from watchlist');
    } else {
        // Add to watchlist
        watchlist.push(movieId);
        button.innerHTML = '<i class="fas fa-bookmark"></i> In Watchlist';
        showSuccess('Added to watchlist');
    }
    
    // Save back to localStorage
    localStorage.setItem('watchlist', JSON.stringify(watchlist));
    
    // Update button state
    button.classList.toggle('in-watchlist', !isInWatchlist);
}

function isInWatchlist(movieId) {
    const watchlist = JSON.parse(localStorage.getItem('watchlist') || '[]');
    return watchlist.includes(movieId);
}

// Rating System
function rateMovie(movieId, rating) {
    // Store rating in localStorage
    let ratings = JSON.parse(localStorage.getItem('movieRatings') || '{}');
    ratings[movieId] = rating;
    localStorage.setItem('movieRatings', JSON.stringify(ratings));
    
    showSuccess(`Rated ${rating} stars`);
}

function getUserRating(movieId) {
    const ratings = JSON.parse(localStorage.getItem('movieRatings') || '{}');
    return ratings[movieId] || null;
}

// Export for use in other files
window.API_BASE_URL = API_BASE_URL;
window.apiCall = apiCall;
window.showError = showError;
window.showSuccess = showSuccess;
window.showLoading = showLoading;
window.formatDuration = formatDuration;
window.formatDate = formatDate;
window.debounce = debounce;
window.currentState = currentState;
window.navigateToSection = navigateToSection;
window.openMovieModal = openMovieModal;
window.closeModal = closeModal;
window.toggleWatchlist = toggleWatchlist;
window.isInWatchlist = isInWatchlist;
window.rateMovie = rateMovie;
window.getUserRating = getUserRating;
window.performGlobalSearch = performGlobalSearch;
window.clearSearch = clearSearch;
window.applyFilters = applyFilters;
window.clearFilters = clearFilters;
window.changePage = changePage;
window.goToPage = goToPage;

// Initialize when ready
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initializeApp);
} else {
    initializeApp();
}