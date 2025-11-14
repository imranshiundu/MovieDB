package com.example.moviesapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.moviesapi.model.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {

    // Find genre by name (exact match, case-sensitive)
    Optional<Genre> findByName(String name);

    // Find genre by name ignoring case
    Optional<Genre> findByNameIgnoreCase(String name);

    // Check if genre exists by name
    boolean existsByName(String name);

    // Check if genre exists by name ignoring case
    boolean existsByNameIgnoreCase(String name);

    // Find genres by name containing (case-insensitive) with pagination
    Page<Genre> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Find all genres with movie count
    @Query("SELECT g, COUNT(m) as movieCount FROM Genre g LEFT JOIN g.movies m GROUP BY g ORDER BY g.name")
    Page<Object[]> findAllWithMovieCount(Pageable pageable);

    // Find genres with minimum number of movies
    @Query("SELECT g FROM Genre g WHERE SIZE(g.movies) >= :minMovies")
    List<Genre> findGenresWithMinimumMovies(@Param("minMovies") int minMovies);

    // Find top genres by movie count
    @Query("SELECT g, COUNT(m) as movieCount FROM Genre g LEFT JOIN g.movies m GROUP BY g ORDER BY movieCount DESC")
    Page<Object[]> findTopGenresByMovieCount(Pageable pageable);

    // Custom query to find genres by movie ID
    @Query("SELECT g FROM Genre g JOIN g.movies m WHERE m.id = :movieId")
    List<Genre> findByMovieId(@Param("movieId") Long movieId);

    // Find genres with no movies
    @Query("SELECT g FROM Genre g WHERE g.movies IS EMPTY")
    List<Genre> findGenresWithNoMovies();

    // Bulk check for genre existence by names
    @Query("SELECT g FROM Genre g WHERE g.name IN :names")
    List<Genre> findByNames(@Param("names") List<String> names);

    // Find genres by multiple IDs
    List<Genre> findByIdIn(List<Long> ids);

    // NEW METHOD: Find the genre with the highest ID for SQLite
    Genre findTopByOrderByIdDesc();
}