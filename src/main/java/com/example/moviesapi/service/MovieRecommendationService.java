package com.example.moviesapi.service;

import com.example.moviesapi.model.Movie;
import com.example.moviesapi.model.Genre;
import com.example.moviesapi.model.Actor;
import com.example.moviesapi.repository.MovieRepository;
import com.example.moviesapi.repository.GenreRepository;
import com.example.moviesapi.repository.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovieRecommendationService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private ActorRepository actorRepository;

    /**
     * Get movie recommendations based on a favorite movie
     * Finds movies with similar genres, actors, or release years
     */
    public List<Movie> getRecommendationsByMovie(Long movieId, int limit) {
        Optional<Movie> favoriteMovie = movieRepository.findById(movieId);
        if (favoriteMovie.isEmpty()) {
            return Collections.emptyList();
        }

        Movie movie = favoriteMovie.get();
        
        // Get genre IDs from favorite movie
        Set<Long> favoriteGenreIds = movie.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        
        // Get actor IDs from favorite movie
        Set<Long> favoriteActorIds = movie.getActors().stream()
                .map(Actor::getId)
                .collect(Collectors.toSet());
        
        Integer favoriteYear = movie.getReleaseYear();

        // Find movies with similar characteristics using database queries
        List<Movie> recommendations = new ArrayList<>();
        
        // 1. Movies with same genres (highest priority)
        if (!favoriteGenreIds.isEmpty()) {
            Pageable pageable = PageRequest.of(0, limit * 2); // Get more to allow for deduplication
            List<Movie> genreBased = movieRepository.findByGenreIds(
                new ArrayList<>(favoriteGenreIds), pageable).getContent();
            recommendations.addAll(genreBased);
        }
        
        // 2. Movies with same actors
        if (!favoriteActorIds.isEmpty()) {
            Pageable pageable = PageRequest.of(0, limit * 2);
            List<Movie> actorBased = movieRepository.findByActorIds(
                new ArrayList<>(favoriteActorIds), pageable).getContent();
            recommendations.addAll(actorBased);
        }
        
        // 3. Movies from same decade
        if (favoriteYear != null) {
            int decadeStart = (favoriteYear / 10) * 10;
            int decadeEnd = decadeStart + 9;
            List<Movie> yearBased = movieRepository.findByReleaseYearBetween(decadeStart, decadeEnd);
            recommendations.addAll(yearBased);
        }

        // Remove duplicates and the original movie, then sort by relevance
        return recommendations.stream()
                .filter(m -> !m.getId().equals(movieId))
                .distinct()
                .sorted((m1, m2) -> {
                    int score1 = calculateSimilarityScore(m1, favoriteGenreIds, favoriteActorIds, favoriteYear);
                    int score2 = calculateSimilarityScore(m2, favoriteGenreIds, favoriteActorIds, favoriteYear);
                    return Integer.compare(score2, score1);
                })
                .limit(limit)
                .collect(Collectors.toList());
    }

    private int calculateSimilarityScore(Movie movie, Set<Long> favoriteGenreIds, 
                                       Set<Long> favoriteActorIds, Integer favoriteYear) {
        int score = 0;
        
        // Genre similarity (2 points per matching genre)
        Set<Long> movieGenreIds = movie.getGenres().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());
        for (Long genreId : movieGenreIds) {
            if (favoriteGenreIds.contains(genreId)) {
                score += 2;
            }
        }
        
        // Actor similarity (3 points per matching actor)
        Set<Long> movieActorIds = movie.getActors().stream()
                .map(Actor::getId)
                .collect(Collectors.toSet());
        for (Long actorId : movieActorIds) {
            if (favoriteActorIds.contains(actorId)) {
                score += 3;
            }
        }
        
        // Release year similarity (1 point for same decade, 2 points for same year)
        if (favoriteYear != null && movie.getReleaseYear() != null) {
            if (movie.getReleaseYear().equals(favoriteYear)) {
                score += 2;
            } else {
                int decade1 = favoriteYear / 10;
                int decade2 = movie.getReleaseYear() / 10;
                if (decade1 == decade2) {
                    score += 1;
                }
            }
        }
        
        return score;
    }

    /**
     * Get trending movies (recent movies with most actors)
     */
    public List<Movie> getTrendingMovies(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        
        // Get recent movies first
        List<Movie> recentMovies = movieRepository.findByOrderByReleaseYearDesc(pageable).getContent();
        
        // If we don't have enough recent movies, supplement with popular ones (most actors)
        if (recentMovies.size() < limit) {
            List<Movie> popularMovies = movieRepository.findAll().stream()
                    .sorted((m1, m2) -> Integer.compare(m2.getActors().size(), m1.getActors().size()))
                    .limit(limit - recentMovies.size())
                    .collect(Collectors.toList());
            
            // Combine and remove duplicates
            Set<Movie> combined = new LinkedHashSet<>(recentMovies);
            combined.addAll(popularMovies);
            return new ArrayList<>(combined).stream().limit(limit).collect(Collectors.toList());
        }
        
        return recentMovies;
    }

    /**
     * Get movies by mood/category using real genre data
     */
    public List<Movie> getMoviesByMood(String mood, int limit) {
        Map<String, List<String>> moodToGenres = createMoodToGenresMapping();
        List<String> targetGenreNames = moodToGenres.getOrDefault(mood.toLowerCase(), 
            Arrays.asList("Drama")); // Default to Drama if mood not found

        // FIXED: Use findAll and filter by name instead of findByNameIn
        List<Genre> allGenres = genreRepository.findAll();
        List<Long> targetGenreIds = allGenres.stream()
                .filter(genre -> targetGenreNames.contains(genre.getName()))
                .map(Genre::getId)
                .collect(Collectors.toList());

        if (targetGenreIds.isEmpty()) {
            return Collections.emptyList();
        }

        Pageable pageable = PageRequest.of(0, limit);
        return movieRepository.findByGenreIds(targetGenreIds, pageable).getContent().stream()
                .sorted((m1, m2) -> Integer.compare(m2.getReleaseYear(), m1.getReleaseYear())) // Recent first
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Create a mapping from moods to actual genres in the database
     */
    private Map<String, List<String>> createMoodToGenresMapping() {
        Map<String, List<String>> moodToGenres = new HashMap<>();
        
        // Use actual genre names that exist in the database
        moodToGenres.put("action", Arrays.asList("Action", "Adventure", "Thriller"));
        moodToGenres.put("comedy", Arrays.asList("Comedy", "Romantic Comedy"));
        moodToGenres.put("drama", Arrays.asList("Drama", "Romance"));
        moodToGenres.put("sci-fi", Arrays.asList("Sci-Fi", "Fantasy", "Science Fiction"));
        moodToGenres.put("horror", Arrays.asList("Horror", "Thriller", "Suspense"));
        moodToGenres.put("family", Arrays.asList("Family", "Adventure", "Fantasy", "Animation"));
        moodToGenres.put("romance", Arrays.asList("Romance", "Romantic Comedy", "Drama"));
        moodToGenres.put("thriller", Arrays.asList("Thriller", "Suspense", "Mystery"));
        moodToGenres.put("fantasy", Arrays.asList("Fantasy", "Adventure", "Sci-Fi"));
        moodToGenres.put("adventure", Arrays.asList("Adventure", "Action", "Fantasy"));
        
        return moodToGenres;
    }

    /**
     * Get similar movies based on director style (using genre and actor similarities)
     */
    public List<Movie> getSimilarDirectorsStyle(Long movieId, int limit) {
        Optional<Movie> movie = movieRepository.findById(movieId);
        if (movie.isEmpty()) {
            return Collections.emptyList();
        }

        Movie targetMovie = movie.get();
        
        // Get the movie's primary genre (the first genre)
        String primaryGenre = targetMovie.getGenres().stream()
                .findFirst()
                .map(Genre::getName)
                .orElse("Drama");
        
        // Get movies with the same primary genre but different from the target
        List<Movie> similarGenreMovies = movieRepository.findAll().stream()
                .filter(m -> !m.getId().equals(movieId))
                .filter(m -> m.getGenres().stream()
                        .anyMatch(genre -> genre.getName().equals(primaryGenre)))
                .sorted((m1, m2) -> {
                    // Sort by release year (newer first) and then by actor count
                    int yearCompare = Integer.compare(m2.getReleaseYear(), m1.getReleaseYear());
                    if (yearCompare != 0) return yearCompare;
                    return Integer.compare(m2.getActors().size(), m1.getActors().size());
                })
                .limit(limit)
                .collect(Collectors.toList());

        // If we don't have enough similar genre movies, add some from similar years
        if (similarGenreMovies.size() < limit) {
            Integer targetYear = targetMovie.getReleaseYear();
            if (targetYear != null) {
                List<Movie> sameYearMovies = movieRepository.findByReleaseYearBetween(
                    targetYear - 5, targetYear + 5).stream()
                        .filter(m -> !m.getId().equals(movieId))
                        .filter(m -> !similarGenreMovies.contains(m))
                        .sorted((m1, m2) -> Integer.compare(m2.getActors().size(), m1.getActors().size()))
                        .limit(limit - similarGenreMovies.size())
                        .collect(Collectors.toList());
                
                similarGenreMovies.addAll(sameYearMovies);
            }
        }

        return similarGenreMovies.stream().limit(limit).collect(Collectors.toList());
    }

    /**
     * Get popular movies (most actors + highest rated)
     */
    public List<Movie> getPopularMovies(int limit) {
        // Get movies with most actors
        List<Movie> moviesByActorCount = movieRepository.findAll().stream()
                .sorted((m1, m2) -> Integer.compare(m2.getActors().size(), m1.getActors().size()))
                .limit(limit)
                .collect(Collectors.toList());

        return moviesByActorCount;
    }

    /**
     * Get movies featuring a specific actor
     */
    public List<Movie> getMoviesByActor(Long actorId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return movieRepository.findByActorsId(actorId, pageable).getContent();
    }

    /**
     * Get movies from a specific genre
     */
    public List<Movie> getMoviesByGenre(Long genreId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return movieRepository.findByGenresId(genreId, pageable).getContent();
    }
}