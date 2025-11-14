package com.example.moviesapi.dto;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class MovieRequest {
    
    @NotBlank(message = "Movie title is required")
    @Size(max = 255, message = "Movie title must not exceed 255 characters")
    private String title;

    @NotNull(message = "Release year is required")
    @Min(value = 1888, message = "Release year must be 1888 or later")
    private Integer releaseYear;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 500, message = "Duration must not exceed 500 minutes")
    private Integer duration; // in minutes

    private List<Long> genreIds = new ArrayList<>();
    private List<Long> actorIds = new ArrayList<>();

    // Constructors
    public MovieRequest() {}

    public MovieRequest(String title, Integer releaseYear, Integer duration) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }

    public MovieRequest(String title, Integer releaseYear, Integer duration, List<Long> genreIds, List<Long> actorIds) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
        this.genreIds = genreIds != null ? genreIds : new ArrayList<>();
        this.actorIds = actorIds != null ? actorIds : new ArrayList<>();
    }

    // Getters and Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public List<Long> getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(List<Long> genreIds) {
        this.genreIds = genreIds != null ? genreIds : new ArrayList<>();
    }

    public List<Long> getActorIds() {
        return actorIds;
    }

    public void setActorIds(List<Long> actorIds) {
        this.actorIds = actorIds != null ? actorIds : new ArrayList<>();
    }

    // Validation methods
    public boolean hasGenreIds() {
        return genreIds != null && !genreIds.isEmpty();
    }

    public boolean hasActorIds() {
        return actorIds != null && !actorIds.isEmpty();
    }

    @Override
    public String toString() {
        return "MovieRequest{" +
                "title='" + title + '\'' +
                ", releaseYear=" + releaseYear +
                ", duration=" + duration +
                ", genreIds=" + genreIds +
                ", actorIds=" + actorIds +
                '}';
    }
}