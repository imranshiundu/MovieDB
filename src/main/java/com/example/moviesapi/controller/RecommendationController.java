package com.example.moviesapi.controller;

import com.example.moviesapi.model.Movie;
import com.example.moviesapi.service.MovieRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/recommendations")
@CrossOrigin(origins = "*")
public class RecommendationController {

    @Autowired
    private MovieRecommendationService recommendationService;

    /**
     * Get movie recommendations based on a favorite movie
     */
    @GetMapping("/by-movie/{movieId}")
    public ResponseEntity<Map<String, Object>> getRecommendationsByMovie(
            @PathVariable Long movieId,
            @RequestParam(defaultValue = "5") int limit) {
        
        try {
            List<Movie> recommendations = recommendationService.getRecommendationsByMovie(movieId, limit);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("movieId", movieId);
            response.put("limit", limit);
            response.put("recommendations", recommendations);
            response.put("count", recommendations.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Failed to get recommendations: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get trending movies (recent and popular)
     */
    @GetMapping("/trending")
    public ResponseEntity<Map<String, Object>> getTrendingMovies(
            @RequestParam(defaultValue = "5") int limit) {
        
        try {
            List<Movie> trendingMovies = recommendationService.getTrendingMovies(limit);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("type", "trending");
            response.put("limit", limit);
            response.put("movies", trendingMovies);
            response.put("count", trendingMovies.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Failed to get trending movies: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get popular movies (most actors)
     */
    @GetMapping("/popular")
    public ResponseEntity<Map<String, Object>> getPopularMovies(
            @RequestParam(defaultValue = "5") int limit) {
        
        try {
            List<Movie> popularMovies = recommendationService.getPopularMovies(limit);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("type", "popular");
            response.put("limit", limit);
            response.put("movies", popularMovies);
            response.put("count", popularMovies.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Failed to get popular movies: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get movies by mood
     */
    @GetMapping("/by-mood/{mood}")
    public ResponseEntity<Map<String, Object>> getMoviesByMood(
            @PathVariable String mood,
            @RequestParam(defaultValue = "5") int limit) {
        
        try {
            List<Movie> movies = recommendationService.getMoviesByMood(mood, limit);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("mood", mood);
            response.put("limit", limit);
            response.put("movies", movies);
            response.put("count", movies.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Failed to get movies by mood: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get similar directors style
     */
    @GetMapping("/similar-style/{movieId}")
    public ResponseEntity<Map<String, Object>> getSimilarDirectorsStyle(
            @PathVariable Long movieId,
            @RequestParam(defaultValue = "3") int limit) {
        
        try {
            List<Movie> similarMovies = recommendationService.getSimilarDirectorsStyle(movieId, limit);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("movieId", movieId);
            response.put("limit", limit);
            response.put("similarMovies", similarMovies);
            response.put("count", similarMovies.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Failed to get similar movies: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get movies by actor
     */
    @GetMapping("/by-actor/{actorId}")
    public ResponseEntity<Map<String, Object>> getMoviesByActor(
            @PathVariable Long actorId,
            @RequestParam(defaultValue = "5") int limit) {
        
        try {
            List<Movie> movies = recommendationService.getMoviesByActor(actorId, limit);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("actorId", actorId);
            response.put("limit", limit);
            response.put("movies", movies);
            response.put("count", movies.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Failed to get movies by actor: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get movies by genre
     */
    @GetMapping("/by-genre/{genreId}")
    public ResponseEntity<Map<String, Object>> getMoviesByGenre(
            @PathVariable Long genreId,
            @RequestParam(defaultValue = "5") int limit) {
        
        try {
            List<Movie> movies = recommendationService.getMoviesByGenre(genreId, limit);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("genreId", genreId);
            response.put("limit", limit);
            response.put("movies", movies);
            response.put("count", movies.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Failed to get movies by genre: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Get all available moods
     */
    @GetMapping("/moods")
    public ResponseEntity<Map<String, Object>> getAvailableMoods() {
        try {
            List<String> moods = List.of("action", "comedy", "drama", "sci-fi", "horror", 
                                       "family", "romance", "thriller", "fantasy", "adventure");
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("availableMoods", moods);
            response.put("count", moods.size());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("error", "Failed to get available moods: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}