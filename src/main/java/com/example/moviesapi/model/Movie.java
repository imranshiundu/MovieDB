package com.example.moviesapi.model;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "movies")
public class Movie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Movie title is required")
    @Size(max = 255, message = "Movie title must not exceed 255 characters")
    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @NotNull(message = "Release year is required")
    @Min(value = 1888, message = "Release year must be 1888 or later")
    @Column(name = "release_year", nullable = false)
    private Integer releaseYear;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    @Max(value = 500, message = "Duration must not exceed 500 minutes")
    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Size(max = 160, message = "Director name must not exceed 160 characters")
    @Column(name = "director", length = 160)
    private String director;

    @Size(max = 80, message = "Language must not exceed 80 characters")
    @Column(name = "language", length = 80)
    private String language;

    @Size(max = 80, message = "Country must not exceed 80 characters")
    @Column(name = "country", length = 80)
    private String country;

    @DecimalMin(value = "0.0", message = "IMDb rating must be 0 or higher")
    @DecimalMax(value = "10.0", message = "IMDb rating must be 10 or lower")
    @Column(name = "imdb_rating")
    private Double imdbRating;

    @Size(max = 40, message = "MPAA rating must not exceed 40 characters")
    @Column(name = "mpaa_rating", length = 40)
    private String mpaaRating;

    @Size(max = 500, message = "Poster URL must not exceed 500 characters")
    @Column(name = "poster_url", length = 500)
    private String posterUrl;

    @Size(max = 1200, message = "Overview must not exceed 1200 characters")
    @Column(name = "overview", length = 1200)
    private String overview;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "movie_genres",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "movie_actors",
        joinColumns = @JoinColumn(name = "movie_id"),
        inverseJoinColumns = @JoinColumn(name = "actor_id")
    )
    private Set<Actor> actors = new HashSet<>();

    public Movie() {}

    public Movie(String title, Integer releaseYear, Integer duration) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }

    public Movie(Long id, String title, Integer releaseYear, Integer duration) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Integer getReleaseYear() { return releaseYear; }
    public void setReleaseYear(Integer releaseYear) { this.releaseYear = releaseYear; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public Double getImdbRating() { return imdbRating; }
    public void setImdbRating(Double imdbRating) { this.imdbRating = imdbRating; }

    public String getMpaaRating() { return mpaaRating; }
    public void setMpaaRating(String mpaaRating) { this.mpaaRating = mpaaRating; }

    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }

    public String getOverview() { return overview; }
    public void setOverview(String overview) { this.overview = overview; }

    public Set<Genre> getGenres() { return genres; }
    public void setGenres(Set<Genre> genres) { this.genres = genres; }

    public Set<Actor> getActors() { return actors; }
    public void setActors(Set<Actor> actors) { this.actors = actors; }

    public void addGenre(Genre genre) {
        if (this.genres == null) this.genres = new HashSet<>();
        this.genres.add(genre);
        if (genre.getMovies() != null) genre.getMovies().add(this);
    }

    public void removeGenre(Genre genre) {
        if (this.genres != null) this.genres.remove(genre);
        if (genre.getMovies() != null) genre.getMovies().remove(this);
    }

    public void addActor(Actor actor) {
        if (this.actors == null) this.actors = new HashSet<>();
        this.actors.add(actor);
        if (actor.getMovies() != null) actor.getMovies().add(this);
    }

    public void removeActor(Actor actor) {
        if (this.actors != null) this.actors.remove(actor);
        if (actor.getMovies() != null) actor.getMovies().remove(this);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseYear=" + releaseYear +
                ", duration=" + duration +
                ", director='" + director + '\'' +
                ", imdbRating=" + imdbRating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return id != null && id.equals(movie.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
