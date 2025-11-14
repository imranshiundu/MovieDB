package com.example.moviesapi.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.moviesapi.dto.MovieRequest;
import com.example.moviesapi.model.Actor;
import com.example.moviesapi.model.Genre;
import com.example.moviesapi.model.Movie;
import com.example.moviesapi.service.MovieService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    private final MovieService movieService;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> createMovie(@Valid @RequestBody Movie movie) {
        try {
            Movie createdMovie = movieService.createMovie(movie);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create movie: " + e.getMessage()));
        }
    }

    // CREATE with DTO
    @PostMapping("/with-dto")
    public ResponseEntity<?> createMovieWithDto(@Valid @RequestBody MovieRequest movieRequest) {
        try {
            Movie createdMovie = movieService.createMovieFromRequest(movieRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create movie: " + e.getMessage()));
        }
    }

    // CREATE with relationships
    @PostMapping("/with-relationships")
    public ResponseEntity<?> createMovieWithRelations(
            @Valid @RequestBody Movie movie,
            @RequestParam(required = false) List<Long> genreIds,
            @RequestParam(required = false) List<Long> actorIds) {
        try {
            Movie createdMovie = movieService.createMovieWithRelations(movie, genreIds, actorIds);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create movie with relationships: " + e.getMessage()));
        }
    }

    // READ - All movies
    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    // READ - All movies with pagination (required functionality)
    @GetMapping("/paged")
    public ResponseEntity<?> getAllMoviesPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0 || size <= 0 || size > 100) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid pagination parameters: page must be >= 0, size between 1 and 100"));
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Movie> movies = movieService.getAllMovies(pageable);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to retrieve movies: " + e.getMessage()));
        }
    }

    // READ - Movie by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable Long id) {
        try {
            Movie movie = movieService.getMovieById(id);
            return ResponseEntity.ok(movie);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Movie not found with id: " + id));
        }
    }

    // READ - Actors by movie ID (required functionality)
    @GetMapping("/{id}/actors")
    public ResponseEntity<?> getActorsByMovieId(@PathVariable Long id) {
        try {
            List<Actor> actors = movieService.getActorsByMovieId(id);
            return ResponseEntity.ok(actors);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Movie not found with id: " + id));
        }
    }

    // READ - Genres by movie ID
    @GetMapping("/{id}/genres")
    public ResponseEntity<?> getGenresByMovieId(@PathVariable Long id) {
        try {
            List<Genre> genres = movieService.getGenresByMovieId(id);
            return ResponseEntity.ok(genres);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Movie not found with id: " + id));
        }
    }

    // CACHED ENDPOINTS - NEW
    @GetMapping("/cached")
    public ResponseEntity<List<Movie>> getAllMoviesCached() {
        try {
            List<Movie> movies = movieService.getAllMoviesCached();
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            List<Movie> movies = movieService.getAllMovies();
            return ResponseEntity.ok(movies);
        }
    }

    @GetMapping("/{id}/cached")
    public ResponseEntity<?> getMovieByIdCached(@PathVariable Long id) {
        try {
            Movie movie = movieService.getMovieByIdCached(id);
            if (movie != null) {
                return ResponseEntity.ok(movie);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            try {
                Movie movie = movieService.getMovieById(id);
                if (movie != null) {
                    return ResponseEntity.ok(movie);
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Movie not found with id: " + id));
            }
        }
    }

    // FILTERING - Movies by genre ID (required functionality)
    @GetMapping("/by-genre/{genreId}")
    public ResponseEntity<?> getMoviesByGenreId(
            @PathVariable Long genreId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0 || size <= 0 || size > 100) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid pagination parameters: page must be >= 0, size between 1 and 100"));
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Movie> movies = movieService.getMoviesByGenreId(genreId, pageable);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to filter movies by genre: " + e.getMessage()));
        }
    }

    // FILTERING - Movies by actor ID (required functionality)
    @GetMapping("/by-actor/{actorId}")
    public ResponseEntity<?> getMoviesByActorId(
            @PathVariable Long actorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0 || size <= 0 || size > 100) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid pagination parameters: page must be >= 0, size between 1 and 100"));
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Movie> movies = movieService.getMoviesByActorId(actorId, pageable);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to filter movies by actor: " + e.getMessage()));
        }
    }

    // FILTERING - Movies by release year (required functionality)
    @GetMapping("/by-year/{releaseYear}")
    public ResponseEntity<?> getMoviesByReleaseYear(
            @PathVariable Integer releaseYear,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0 || size <= 0 || size > 100) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid pagination parameters: page must be >= 0, size between 1 and 100"));
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Movie> movies = movieService.getMoviesByReleaseYear(releaseYear, pageable);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to filter movies by year: " + e.getMessage()));
        }
    }

    // SEARCH - Movies by title (required functionality)
    @GetMapping("/search")
    public ResponseEntity<?> searchMoviesByTitle(
            @RequestParam String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0 || size <= 0 || size > 100) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid pagination parameters: page must be >= 0, size between 1 and 100"));
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Movie> movies = movieService.searchMoviesByTitle(title, pageable);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Search failed: " + e.getMessage()));
        }
    }

    // ADVANCED SEARCH
    @GetMapping("/advanced-search")
    public ResponseEntity<?> advancedSearchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) Integer minYear,
            @RequestParam(required = false) Integer maxYear,
            @RequestParam(required = false) Integer minDuration,
            @RequestParam(required = false) Integer maxDuration,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0 || size <= 0 || size > 100) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid pagination parameters: page must be >= 0, size between 1 and 100"));
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Movie> movies = movieService.advancedSearch(title, minYear, maxYear, minDuration, maxDuration, pageable);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Advanced search failed: " + e.getMessage()));
        }
    }

    // UPDATE - Enhanced to handle partial updates including actor list
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateMovie(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            Movie updatedMovie = movieService.partialUpdateMovie(id, updates);
            return ResponseEntity.ok(updatedMovie);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update movie: " + e.getMessage()));
        }
    }

    // UPDATE with DTO
    @PatchMapping("/{id}/with-dto")
    public ResponseEntity<?> updateMovieWithDto(
            @PathVariable Long id,
            @Valid @RequestBody MovieRequest movieRequest) {
        try {
            Movie updatedMovie = movieService.updateMovieFromRequest(id, movieRequest);
            return ResponseEntity.ok(updatedMovie);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update movie: " + e.getMessage()));
        }
    }

    // UPDATE relationships
    @PatchMapping("/{id}/relationships")
    public ResponseEntity<?> updateMovieRelations(
            @PathVariable Long id,
            @RequestParam(required = false) List<Long> genreIds,
            @RequestParam(required = false) List<Long> actorIds) {
        try {
            Movie updatedMovie = movieService.updateMovieRelations(id, genreIds, actorIds);
            return ResponseEntity.ok(updatedMovie);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update movie relationships: " + e.getMessage()));
        }
    }

    // ADD genres to movie
    @PostMapping("/{id}/genres")
    public ResponseEntity<?> addGenresToMovie(
            @PathVariable Long id,
            @RequestBody List<Long> genreIds) {
        try {
            Movie updatedMovie = movieService.addGenresToMovie(id, genreIds);
            return ResponseEntity.ok(updatedMovie);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to add genres to movie: " + e.getMessage()));
        }
    }

    // REMOVE genres from movie
    @DeleteMapping("/{id}/genres")
    public ResponseEntity<?> removeGenresFromMovie(
            @PathVariable Long id,
            @RequestBody List<Long> genreIds) {
        try {
            Movie updatedMovie = movieService.removeGenresFromMovie(id, genreIds);
            return ResponseEntity.ok(updatedMovie);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to remove genres from movie: " + e.getMessage()));
        }
    }

    // ADD actors to movie
    @PostMapping("/{id}/actors")
    public ResponseEntity<?> addActorsToMovie(
            @PathVariable Long id,
            @RequestBody List<Long> actorIds) {
        try {
            Movie updatedMovie = movieService.addActorsToMovie(id, actorIds);
            return ResponseEntity.ok(updatedMovie);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to add actors to movie: " + e.getMessage()));
        }
    }

    // REMOVE actors from movie
    @DeleteMapping("/{id}/actors")
    public ResponseEntity<?> removeActorsFromMovie(
            @PathVariable Long id,
            @RequestBody List<Long> actorIds) {
        try {
            Movie updatedMovie = movieService.removeActorsFromMovie(id, actorIds);
            return ResponseEntity.ok(updatedMovie);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to remove actors from movie: " + e.getMessage()));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean force) {
        try {
            movieService.deleteMovie(id, force);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Movie not found with id: " + id));
        }
    }

    // STATISTICS - Movies with actor count
    @GetMapping("/stats/with-actor-count")
    public ResponseEntity<?> getAllMoviesWithActorCount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0 || size <= 0 || size > 100) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid pagination parameters: page must be >= 0, size between 1 and 100"));
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Object[]> moviesWithCount = movieService.getAllMoviesWithActorCount(pageable);
            return ResponseEntity.ok(moviesWithCount);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to retrieve movie statistics: " + e.getMessage()));
        }
    }

    // STATISTICS - Latest movies
    @GetMapping("/stats/latest")
    public ResponseEntity<?> getLatestMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0 || size <= 0 || size > 100) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid pagination parameters: page must be >= 0, size between 1 and 100"));
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Movie> latestMovies = movieService.getLatestMovies(pageable);
            return ResponseEntity.ok(latestMovies);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to retrieve latest movies: " + e.getMessage()));
        }
    }

    // STATISTICS - Movies by duration range
    @GetMapping("/stats/by-duration")
    public ResponseEntity<?> getMoviesByDurationRange(
            @RequestParam Integer minDuration,
            @RequestParam Integer maxDuration) {
        try {
            List<Movie> movies = movieService.getMoviesByDurationRange(minDuration, maxDuration);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to filter movies by duration: " + e.getMessage()));
        }
    }

    // STATISTICS - Movies with no genres
    @GetMapping("/stats/no-genres")
    public ResponseEntity<List<Movie>> getMoviesWithNoGenres() {
        List<Movie> movies = movieService.getMoviesWithNoGenres();
        return ResponseEntity.ok(movies);
    }

    // STATISTICS - Movies with no actors
    @GetMapping("/stats/no-actors")
    public ResponseEntity<List<Movie>> getMoviesWithNoActors() {
        List<Movie> movies = movieService.getMoviesWithNoActors();
        return ResponseEntity.ok(movies);
    }

    // CHECK EXISTENCE
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> movieExists(@PathVariable Long id) {
        boolean exists = movieService.movieExists(id);
        return ResponseEntity.ok(exists);
    }
}