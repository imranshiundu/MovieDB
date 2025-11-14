package com.example.moviesapi.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

public class ActorRequest {
    
    @NotBlank(message = "Actor name is required")
    @Size(max = 255, message = "Actor name must not exceed 255 characters")
    private String name;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private List<Long> movieIds = new ArrayList<>();

    // Constructors
    public ActorRequest() {}

    public ActorRequest(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public ActorRequest(String name, LocalDate birthDate, List<Long> movieIds) {
        this.name = name;
        this.birthDate = birthDate;
        this.movieIds = movieIds != null ? movieIds : new ArrayList<>();
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<Long> getMovieIds() {
        return movieIds;
    }

    public void setMovieIds(List<Long> movieIds) {
        this.movieIds = movieIds != null ? movieIds : new ArrayList<>();
    }

    // Validation methods
    public boolean hasMovieIds() {
        return movieIds != null && !movieIds.isEmpty();
    }

    @Override
    public String toString() {
        return "ActorRequest{" +
                "name='" + name + '\'' +
                ", birthDate=" + birthDate +
                ", movieIds=" + movieIds +
                '}';
    }
}