package com.example.moviesapi.dto;

import com.example.moviesapi.model.Movie;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class GenreResponse {
    private Long id;
    private String name;
    private List<String> movies;
    private Integer movieCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public GenreResponse() {}

    public GenreResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    // Static factory method from Entity
    public static GenreResponse fromEntity(com.example.moviesapi.model.Genre genre) {
        GenreResponse response = new GenreResponse();
        response.setId(genre.getId());
        response.setName(genre.getName());
        
        // Convert movies to titles
        if (genre.getMovies() != null) {
            response.setMovies(genre.getMovies().stream()
                    .map(Movie::getTitle)
                    .collect(Collectors.toList()));
            response.setMovieCount(genre.getMovies().size());
        } else {
            response.setMovieCount(0);
        }
        
        return response;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<String> getMovies() { return movies; }
    public void setMovies(List<String> movies) { this.movies = movies; }

    public Integer getMovieCount() { return movieCount; }
    public void setMovieCount(Integer movieCount) { this.movieCount = movieCount; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}