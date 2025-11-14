package com.example.moviesapi.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "actors")
public class Actor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Actor name is required")
    @Size(max = 255, message = "Actor name must not exceed 255 characters")
    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @NotNull(message = "Birth date is required")
    @Past(message = "Birth date must be in the past")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Column(name = "birth_date", nullable = false, columnDefinition = "VARCHAR(255)")
    private LocalDate birthDate;

    @ManyToMany(mappedBy = "actors", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<Movie> movies = new HashSet<>();

    // Constructors
    public Actor() {}

    public Actor(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

    public Actor(Long id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Set<Movie> getMovies() {
        return movies;
    }

    public void setMovies(Set<Movie> movies) {
        this.movies = movies;
    }

    // Helper methods for managing relationships
    public void addMovie(Movie movie) {
        if (this.movies == null) {
            this.movies = new HashSet<>();
        }
        this.movies.add(movie);
        if (movie.getActors() != null) {
            movie.getActors().add(this);
        }
    }

    public void removeMovie(Movie movie) {
        if (this.movies != null) {
            this.movies.remove(movie);
        }
        if (movie.getActors() != null) {
            movie.getActors().remove(this);
        }
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Actor)) return false;
        Actor actor = (Actor) o;
        return id != null && id.equals(actor.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}