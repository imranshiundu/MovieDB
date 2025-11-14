package com.example.moviesapi.dto;

import com.example.moviesapi.model.Movie;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class ActorResponse {
    private Long id;
    private String name;
    private LocalDate birthDate;
    private Integer age;
    private List<String> movies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructors
    public ActorResponse() {}

    public ActorResponse(Long id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.age = calculateAge(birthDate);
    }

    // Static factory method from Entity
    public static ActorResponse fromEntity(com.example.moviesapi.model.Actor actor) {
        ActorResponse response = new ActorResponse();
        response.setId(actor.getId());
        response.setName(actor.getName());
        response.setBirthDate(actor.getBirthDate());
        response.setAge(calculateAge(actor.getBirthDate()));
        
        // Convert movies to titles
        if (actor.getMovies() != null) {
            response.setMovies(actor.getMovies().stream()
                    .map(Movie::getTitle)
                    .collect(Collectors.toList()));
        }
        
        return response;
    }

    private static int calculateAge(LocalDate birthDate) {
        if (birthDate == null) return 0;
        return java.time.Period.between(birthDate, LocalDate.now()).getYears();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public LocalDate getBirthDate() { return birthDate; }
    public void setBirthDate(LocalDate birthDate) { 
        this.birthDate = birthDate;
        this.age = calculateAge(birthDate);
    }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public List<String> getMovies() { return movies; }
    public void setMovies(List<String> movies) { this.movies = movies; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}