package com.example.moviesapi.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.example.moviesapi.model.Genre;
import com.example.moviesapi.model.Movie;
import com.example.moviesapi.service.GenreService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/genres")
@CrossOrigin(origins = "*")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    // CREATE
    @PostMapping
    public ResponseEntity<?> createGenre(@Valid @RequestBody Genre genre) {
        try {
            Genre createdGenre = genreService.createGenre(genre);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGenre);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create genre: " + e.getMessage()));
        }
    }

    // READ - All genres
    @GetMapping
    public ResponseEntity<List<Genre>> getAllGenres() {
        List<Genre> genres = genreService.getAllGenres();
        return ResponseEntity.ok(genres);
    }

    // READ - All genres with pagination
    @GetMapping("/paged")
    public ResponseEntity<?> getAllGenresPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0 || size <= 0 || size > 100) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid pagination parameters: page must be >= 0, size between 1 and 100"));
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Genre> genres = genreService.getAllGenres(pageable);
            return ResponseEntity.ok(genres);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to retrieve genres: " + e.getMessage()));
        }
    }

    // READ - Genre by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getGenreById(@PathVariable Long id) {
        try {
            Genre genre = genreService.getGenreById(id);
            return ResponseEntity.ok(genre);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Genre not found with id: " + id));
        }
    }

    // READ - Movies by genre ID
    @GetMapping("/{id}/movies")
    public ResponseEntity<?> getMoviesByGenreId(@PathVariable Long id) {
        try {
            List<Movie> movies = genreService.getMoviesByGenreId(id);
            return ResponseEntity.ok(movies);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Genre not found with id: " + id));
        }
    }

    // READ - Search genres by name
    @GetMapping("/search")
    public ResponseEntity<?> searchGenresByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0 || size <= 0 || size > 100) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid pagination parameters: page must be >= 0, size between 1 and 100"));
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Genre> genres = genreService.searchGenresByName(name, pageable);
            return ResponseEntity.ok(genres);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Search failed: " + e.getMessage()));
        }
    }

    // READ - Genres with movie count
    @GetMapping("/stats/with-movie-count")
    public ResponseEntity<?> getAllGenresWithMovieCount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0 || size <= 0 || size > 100) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid pagination parameters: page must be >= 0, size between 1 and 100"));
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Object[]> genresWithCount = genreService.getAllGenresWithMovieCount(pageable);
            return ResponseEntity.ok(genresWithCount);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to retrieve genre statistics: " + e.getMessage()));
        }
    }

    // READ - Top genres by movie count
    @GetMapping("/stats/top-by-movies")
    public ResponseEntity<?> getTopGenresByMovieCount(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0 || size <= 0 || size > 100) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid pagination parameters: page must be >= 0, size between 1 and 100"));
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Object[]> topGenres = genreService.getTopGenresByMovieCount(pageable);
            return ResponseEntity.ok(topGenres);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to retrieve top genres: " + e.getMessage()));
        }
    }

    // READ - Genres with no movies
    @GetMapping("/stats/no-movies")
    public ResponseEntity<List<Genre>> getGenresWithNoMovies() {
        List<Genre> genres = genreService.getGenresWithNoMovies();
        return ResponseEntity.ok(genres);
    }

    // UPDATE
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateGenre(
            @PathVariable Long id,
            @Valid @RequestBody Genre genreDetails) {
        try {
            if (genreDetails.getId() != null && !genreDetails.getId().equals(id)) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID in body does not match path ID"));
            }
            Genre updatedGenre = genreService.updateGenre(id, genreDetails);
            return ResponseEntity.ok(updatedGenre);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update genre: " + e.getMessage()));
        }
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean force) {
        try {
            genreService.deleteGenre(id, force);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", "Genre not found with id: " + id));
        }
    }

    // BULK CREATE
    @PostMapping("/bulk")
    public ResponseEntity<?> createGenres(@Valid @RequestBody List<Genre> genres) {
        try {
            List<Genre> createdGenres = genreService.createGenres(genres);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGenres);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to create genres: " + e.getMessage()));
        }
    }

    // CHECK EXISTENCE
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> genreExists(@PathVariable Long id) {
        boolean exists = genreService.genreExists(id);
        return ResponseEntity.ok(exists);
    }

    // FIND BY NAME
    @GetMapping("/by-name/{name}")
    public ResponseEntity<?> getGenreByName(@PathVariable String name) {
        try {
            Optional<Genre> genre = genreService.getGenreByName(name);
            return genre.map(ResponseEntity::ok)
                   .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to find genre: " + e.getMessage()));
        }
    }
}