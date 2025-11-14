package com.example.moviesapi.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.moviesapi.model.Actor;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    
    // Check if actor with same name and birth date exists
    boolean existsByNameAndBirthDate(String name, LocalDate birthDate);
    
    // Search actors by name (case insensitive) - REQUIRED
    Page<Actor> findByNameContainingIgnoreCase(String name, Pageable pageable);
    
    // Find actors by exact name (case insensitive)
    List<Actor> findByNameIgnoreCase(String name);
    
    // Find actors by birth date range
    List<Actor> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);

    List<Actor> findByIdIn(List<Long> ids);
    
    // NEW METHOD: Find the actor with the highest ID
    Actor findTopByOrderByIdDesc();
    
    // Advanced search with multiple criteria
    @Query("SELECT a FROM Actor a WHERE " +
           "(:name IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
           "(:minBirthDate IS NULL OR a.birthDate >= :minBirthDate) AND " +
           "(:maxBirthDate IS NULL OR a.birthDate <= :maxBirthDate)")
    Page<Actor> findBySearchCriteria(@Param("name") String name,
                                    @Param("minBirthDate") LocalDate minBirthDate,
                                    @Param("maxBirthDate") LocalDate maxBirthDate,
                                    Pageable pageable);
    
    // Find actors by movie ID
    @Query("SELECT a FROM Actor a JOIN a.movies m WHERE m.id = :movieId")
    List<Actor> findByMovieId(@Param("movieId") Long movieId);
    
    // REQUIRED: Find actors by name with pagination support
    default Page<Actor> findByNameIgnoreCaseContaining(String name, Pageable pageable) {
        return findByNameContainingIgnoreCase(name, pageable);
    }
}