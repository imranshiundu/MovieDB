package com.example.moviesapi.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.moviesapi.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    // Basic CRUD operations are inherited from JpaRepository

    // Find movie by exact title match
    Optional<Movie> findByTitle(String title);

    // Case-insensitive search by title containing (for search functionality) - REQUIRED
    Page<Movie> findByTitleContainingIgnoreCase(String title, Pageable pageable);

    // Find movies by release year - REQUIRED
    List<Movie> findByReleaseYear(Integer releaseYear);

    // Find movies by release year with pagination - REQUIRED
    Page<Movie> findByReleaseYear(Integer releaseYear, Pageable pageable);

    // Find movies released between years
    List<Movie> findByReleaseYearBetween(Integer startYear, Integer endYear);

    // Find movies by duration range
    List<Movie> findByDurationBetween(Integer minDuration, Integer maxDuration);

    // Find movies longer than specified duration
    List<Movie> findByDurationGreaterThanEqual(Integer duration);

    // Find movies shorter than specified duration
    List<Movie> findByDurationLessThanEqual(Integer duration);

    // Find movies by genre ID (required functionality) - REQUIRED
    @Query("SELECT m FROM Movie m JOIN m.genres g WHERE g.id = :genreId")
    Page<Movie> findByGenresId(@Param("genreId") Long genreId, Pageable pageable);

    // Find movies by actor ID (required functionality) - REQUIRED
    @Query("SELECT m FROM Movie m JOIN m.actors a WHERE a.id = :actorId")
    Page<Movie> findByActorsId(@Param("actorId") Long actorId, Pageable pageable);

    // Find movies by multiple genres
    @Query("SELECT DISTINCT m FROM Movie m JOIN m.genres g WHERE g.id IN :genreIds")
    Page<Movie> findByGenreIds(@Param("genreIds") List<Long> genreIds, Pageable pageable);

    // Find movies by multiple actors
    @Query("SELECT DISTINCT m FROM Movie m JOIN m.actors a WHERE a.id IN :actorIds")
    Page<Movie> findByActorIds(@Param("actorIds") List<Long> actorIds, Pageable pageable);

    // Advanced search with multiple criteria
    @Query("SELECT m FROM Movie m WHERE " +
           "(:title IS NULL OR LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))) AND " +
           "(:minYear IS NULL OR m.releaseYear >= :minYear) AND " +
           "(:maxYear IS NULL OR m.releaseYear <= :maxYear) AND " +
           "(:minDuration IS NULL OR m.duration >= :minDuration) AND " +
           "(:maxDuration IS NULL OR m.duration <= :maxDuration)")
    Page<Movie> findByAdvancedSearch(
        @Param("title") String title,
        @Param("minYear") Integer minYear,
        @Param("maxYear") Integer maxYear,
        @Param("minDuration") Integer minDuration,
        @Param("maxDuration") Integer maxDuration,
        Pageable pageable);

    // Find movies with actor count
    @Query("SELECT m, COUNT(a) as actorCount FROM Movie m LEFT JOIN m.actors a GROUP BY m ORDER BY m.title")
    Page<Object[]> findAllWithActorCount(Pageable pageable);

    // Find latest movies
    Page<Movie> findByOrderByReleaseYearDesc(Pageable pageable);

    // Find oldest movies
    Page<Movie> findByOrderByReleaseYearAsc(Pageable pageable);

    // Find movies by title length
    @Query("SELECT m FROM Movie m WHERE LENGTH(m.title) >= :minLength")
    List<Movie> findByTitleMinLength(@Param("minLength") int minLength);

    // Find movies with specific genre names
    @Query("SELECT DISTINCT m FROM Movie m JOIN m.genres g WHERE g.name IN :genreNames")
    Page<Movie> findByGenreNames(@Param("genreNames") List<String> genreNames, Pageable pageable);

    // Find movies with actor names containing
    @Query("SELECT DISTINCT m FROM Movie m JOIN m.actors a WHERE LOWER(a.name) LIKE LOWER(CONCAT('%', :actorName, '%'))")
    Page<Movie> findByActorNameContaining(@Param("actorName") String actorName, Pageable pageable);

    // Count movies by release year
    @Query("SELECT m.releaseYear, COUNT(m) FROM Movie m GROUP BY m.releaseYear ORDER BY m.releaseYear DESC")
    List<Object[]> countMoviesByReleaseYear();

    // Find duplicate movies by title and year
    @Query("SELECT m.title, m.releaseYear, COUNT(m) FROM Movie m GROUP BY m.title, m.releaseYear HAVING COUNT(m) > 1")
    List<Object[]> findDuplicateMovies();

    // Check if movie exists by title and release year
    boolean existsByTitleAndReleaseYear(String title, Integer releaseYear);

    // Bulk operations
    List<Movie> findByIdIn(List<Long> ids);

    List<Movie> findByTitleIn(List<String> titles);
    
    // Find movies with no genres
    @Query("SELECT m FROM Movie m WHERE m.genres IS EMPTY")
    List<Movie> findMoviesWithNoGenres();
    
    // Find movies with no actors
    @Query("SELECT m FROM Movie m WHERE m.actors IS EMPTY")
    List<Movie> findMoviesWithNoActors();

    // NEW METHOD: Find the movie with the highest ID for SQLite
    Movie findTopByOrderByIdDesc();

    // REQUIRED: Alternative method names for compatibility
    default Page<Movie> findByGenres_Id(Long genreId, Pageable pageable) {
        return findByGenresId(genreId, pageable);
    }

    default Page<Movie> findByActors_Id(Long actorId, Pageable pageable) {
        return findByActorsId(actorId, pageable);
    }
}