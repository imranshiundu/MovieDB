package com.example.moviesapi.dto;

import com.example.moviesapi.model.Actor;
import com.example.moviesapi.model.Genre;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class MovieResponse {
    private Long id;
    private String title;
    private Integer releaseYear;
    private Integer duration;
    private List<String> genres;
    private List<String> actors;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public MovieResponse() {}

    public MovieResponse(Long id, String title, Integer releaseYear, Integer duration) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }

    
    public static MovieResponse fromEntity(com.example.moviesapi.model.Movie movie) {
        MovieResponse response = new MovieResponse();
        response.setId(movie.getId());
        response.setTitle(movie.getTitle());
        response.setReleaseYear(movie.getReleaseYear());
        response.setDuration(movie.getDuration());
        
        // Convert genres to names
        if (movie.getGenres() != null) {
            response.setGenres(movie.getGenres().stream()
                    .map(Genre::getName)
                    .collect(Collectors.toList()));
        }
        
        // Convert actors to names
        if (movie.getActors() != null) {
            response.setActors(movie.getActors().stream()
                    .map(Actor::getName)
                    .collect(Collectors.toList()));
        }
        
        return response;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public List<String> getGenres() { return genres; }
    public void setGenres(List<String> genres) { this.genres = genres; }

    public List<String> getActors() { return actors; }
    public void setActors(List<String> actors) { this.actors = actors; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}