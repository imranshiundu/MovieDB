package com.example.moviesapi.service;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.moviesapi.cache.SimpleCacheService;
import com.example.moviesapi.dto.MovieRequest;
import com.example.moviesapi.exception.InvalidRequestException;
import com.example.moviesapi.exception.ResourceNotFoundException;
import com.example.moviesapi.model.Actor;
import com.example.moviesapi.model.Genre;
import com.example.moviesapi.model.Movie;
import com.example.moviesapi.repository.ActorRepository;
import com.example.moviesapi.repository.GenreRepository;
import com.example.moviesapi.repository.MovieRepository;

@Service
@Transactional
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final SimpleCacheService cacheService;

    @Autowired
    public MovieService(MovieRepository movieRepository, GenreRepository genreRepository, ActorRepository actorRepository, SimpleCacheService cacheService) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
        this.cacheService = cacheService;
    }

    public Movie createMovieFromRequest(MovieRequest movieRequest) {
        Movie movie = new Movie();
        movie.setTitle(movieRequest.getTitle());
        movie.setReleaseYear(movieRequest.getReleaseYear());
        movie.setDuration(movieRequest.getDuration());
        applyMetadata(movie, movieRequest);

        validateNewMovie(movie);
        movie.setId(findNextAvailableId());

        if (movieRequest.hasGenreIds()) {
            List<Genre> genres = genreRepository.findAllById(movieRequest.getGenreIds());
            movie.setGenres(new HashSet<>(genres));
        }

        if (movieRequest.hasActorIds()) {
            List<Actor> actors = actorRepository.findAllById(movieRequest.getActorIds());
            movie.setActors(new HashSet<>(actors));
        }

        clearMovieCache(movie.getId());
        return movieRepository.save(movie);
    }

    public Movie createMovie(Movie movie) {
        validateNewMovie(movie);
        movie.setId(findNextAvailableId());
        clearMovieCache(movie.getId());
        return movieRepository.save(movie);
    }

    private void validateNewMovie(Movie movie) {
        if (movieRepository.existsByTitleAndReleaseYear(movie.getTitle(), movie.getReleaseYear())) {
            throw new InvalidRequestException("Movie with title '" + movie.getTitle() + "' and release year '" + movie.getReleaseYear() + "' already exists");
        }
        validateReleaseYear(movie.getReleaseYear());
        validateDuration(movie.getDuration());
    }

    private Long findNextAvailableId() {
        Movie lastMovie = movieRepository.findTopByOrderByIdDesc();
        return lastMovie != null && lastMovie.getId() != null ? lastMovie.getId() + 1 : 1L;
    }

    public Movie createMovieWithRelations(Movie movie, List<Long> genreIds, List<Long> actorIds) {
        Movie savedMovie = createMovie(movie);
        if (genreIds != null && !genreIds.isEmpty()) addGenresToMovie(savedMovie.getId(), genreIds);
        if (actorIds != null && !actorIds.isEmpty()) addActorsToMovie(savedMovie.getId(), actorIds);
        return movieRepository.findById(savedMovie.getId()).orElseThrow(() -> new ResourceNotFoundException("Movie not found after creation"));
    }

    @Transactional(readOnly = true)
    public List<Movie> getAllMovies() { return movieRepository.findAll(); }

    @Transactional(readOnly = true)
    public Page<Movie> getAllMovies(Pageable pageable) { return movieRepository.findAll(pageable); }

    @Transactional(readOnly = true)
    public Movie getMovieById(Long id) { return movieRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Movie not found with id: " + id)); }

    @Transactional(readOnly = true)
    public List<Actor> getActorsByMovieId(Long movieId) { return List.copyOf(getMovieById(movieId).getActors()); }

    @Transactional(readOnly = true)
    public List<Genre> getGenresByMovieId(Long movieId) { return List.copyOf(getMovieById(movieId).getGenres()); }

    @Transactional(readOnly = true)
    public List<Movie> getAllMoviesCached() {
        String cacheKey = "all_movies_cached";
        List<Movie> cachedMovies = (List<Movie>) cacheService.get(cacheKey);
        if (cachedMovies != null) return cachedMovies;
        List<Movie> movies = getAllMovies();
        cacheService.put(cacheKey, movies);
        return movies;
    }

    @Transactional(readOnly = true)
    public Movie getMovieByIdCached(Long id) {
        String cacheKey = "movie_" + id;
        Movie cachedMovie = (Movie) cacheService.get(cacheKey);
        if (cachedMovie != null) return cachedMovie;
        Movie movie = getMovieById(id);
        cacheService.put(cacheKey, movie);
        return movie;
    }

    @Transactional(readOnly = true)
    public Page<Movie> getMoviesByGenreId(Long genreId, Pageable pageable) {
        if (!genreRepository.existsById(genreId)) throw new ResourceNotFoundException("Genre not found with id: " + genreId);
        return movieRepository.findByGenresId(genreId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Movie> getMoviesByActorId(Long actorId, Pageable pageable) {
        if (!actorRepository.existsById(actorId)) throw new ResourceNotFoundException("Actor not found with id: " + actorId);
        return movieRepository.findByActorsId(actorId, pageable);
    }

    @Transactional(readOnly = true)
    public Page<Movie> getMoviesByReleaseYear(Integer releaseYear, Pageable pageable) { return movieRepository.findByReleaseYear(releaseYear, pageable); }

    @Transactional(readOnly = true)
    public Page<Movie> searchMoviesByTitle(String title, Pageable pageable) { return movieRepository.findByTitleContainingIgnoreCase(title, pageable); }

    @Transactional(readOnly = true)
    public Page<Movie> advancedSearch(String title, Integer minYear, Integer maxYear, Integer minDuration, Integer maxDuration, Pageable pageable) {
        return movieRepository.findByAdvancedSearch(title, minYear, maxYear, minDuration, maxDuration, pageable);
    }

    public Movie partialUpdateMovie(Long id, Map<String, Object> updates) {
        Movie movie = getMovieById(id);
        updates.forEach((key, value) -> {
            switch (key) {
                case "title" -> updateTitle(movie, value);
                case "releaseYear" -> updateReleaseYear(movie, value);
                case "duration" -> updateDuration(movie, value);
                case "director" -> movie.setDirector(cleanString(value));
                case "language" -> movie.setLanguage(cleanString(value));
                case "country" -> movie.setCountry(cleanString(value));
                case "imdbRating" -> movie.setImdbRating(parseDouble(value, "IMDb rating"));
                case "mpaaRating" -> movie.setMpaaRating(cleanString(value));
                case "posterUrl" -> movie.setPosterUrl(cleanString(value));
                case "overview" -> movie.setOverview(cleanString(value));
                case "actorIds" -> { if (value instanceof List) updateMovieActors(movie, coerceLongList((List<?>) value)); }
                case "genreIds" -> { if (value instanceof List) updateMovieGenres(movie, coerceLongList((List<?>) value)); }
                default -> { }
            }
        });
        clearMovieCache(id);
        return movieRepository.save(movie);
    }

    public Movie updateMovieFromRequest(Long id, MovieRequest movieRequest) {
        Movie movie = getMovieById(id);
        String newTitle = movieRequest.getTitle() != null ? movieRequest.getTitle() : movie.getTitle();
        Integer newYear = movieRequest.getReleaseYear() != null ? movieRequest.getReleaseYear() : movie.getReleaseYear();
        if ((!movie.getTitle().equals(newTitle) || !movie.getReleaseYear().equals(newYear)) && movieRepository.existsByTitleAndReleaseYear(newTitle, newYear)) {
            throw new InvalidRequestException("Movie with title '" + newTitle + "' and release year '" + newYear + "' already exists");
        }
        if (movieRequest.getTitle() != null) movie.setTitle(movieRequest.getTitle());
        if (movieRequest.getReleaseYear() != null) movie.setReleaseYear(movieRequest.getReleaseYear());
        if (movieRequest.getDuration() != null) movie.setDuration(movieRequest.getDuration());
        applyMetadata(movie, movieRequest);
        if (movieRequest.hasGenreIds()) updateMovieGenres(movie, movieRequest.getGenreIds());
        if (movieRequest.hasActorIds()) updateMovieActors(movie, movieRequest.getActorIds());
        clearMovieCache(id);
        return movieRepository.save(movie);
    }

    public Movie updateMovie(Long id, Movie movieDetails) {
        Movie movie = getMovieById(id);
        if (movieDetails.getTitle() != null) movie.setTitle(movieDetails.getTitle());
        if (movieDetails.getReleaseYear() != null) movie.setReleaseYear(movieDetails.getReleaseYear());
        if (movieDetails.getDuration() != null) movie.setDuration(movieDetails.getDuration());
        if (movieDetails.getDirector() != null) movie.setDirector(movieDetails.getDirector());
        if (movieDetails.getLanguage() != null) movie.setLanguage(movieDetails.getLanguage());
        if (movieDetails.getCountry() != null) movie.setCountry(movieDetails.getCountry());
        if (movieDetails.getImdbRating() != null) movie.setImdbRating(movieDetails.getImdbRating());
        if (movieDetails.getMpaaRating() != null) movie.setMpaaRating(movieDetails.getMpaaRating());
        if (movieDetails.getPosterUrl() != null) movie.setPosterUrl(movieDetails.getPosterUrl());
        if (movieDetails.getOverview() != null) movie.setOverview(movieDetails.getOverview());
        clearMovieCache(id);
        return movieRepository.save(movie);
    }

    private void applyMetadata(Movie movie, MovieRequest request) {
        movie.setDirector(request.getDirector());
        movie.setLanguage(request.getLanguage());
        movie.setCountry(request.getCountry());
        movie.setImdbRating(request.getImdbRating());
        movie.setMpaaRating(request.getMpaaRating());
        movie.setPosterUrl(request.getPosterUrl());
        movie.setOverview(request.getOverview());
    }

    private void updateTitle(Movie movie, Object value) {
        String newTitle = cleanString(value);
        if (newTitle == null || newTitle.isBlank()) throw new InvalidRequestException("Movie title cannot be empty");
        if (movieRepository.existsByTitleAndReleaseYear(newTitle, movie.getReleaseYear()) && !movie.getTitle().equals(newTitle)) {
            throw new InvalidRequestException("Movie with title '" + newTitle + "' and release year '" + movie.getReleaseYear() + "' already exists");
        }
        movie.setTitle(newTitle);
    }

    private void updateReleaseYear(Movie movie, Object value) {
        Integer newYear = parseInteger(value, "release year");
        validateReleaseYear(newYear);
        if (movieRepository.existsByTitleAndReleaseYear(movie.getTitle(), newYear) && !movie.getReleaseYear().equals(newYear)) {
            throw new InvalidRequestException("Movie with title '" + movie.getTitle() + "' and release year '" + newYear + "' already exists");
        }
        movie.setReleaseYear(newYear);
    }

    private void updateDuration(Movie movie, Object value) {
        Integer newDuration = parseInteger(value, "duration");
        validateDuration(newDuration);
        movie.setDuration(newDuration);
    }

    private String cleanString(Object value) { return value == null ? null : value.toString().trim(); }
    private Integer parseInteger(Object value, String field) { try { return Integer.parseInt(value.toString()); } catch (Exception e) { throw new InvalidRequestException("Invalid " + field + " format"); } }
    private Double parseDouble(Object value, String field) { try { return value == null ? null : Double.parseDouble(value.toString()); } catch (Exception e) { throw new InvalidRequestException("Invalid " + field + " format"); } }
    private void validateReleaseYear(Integer year) { if (year < 1888 || year > java.time.Year.now().getValue() + 1) throw new InvalidRequestException("Release year must be between 1888 and " + (java.time.Year.now().getValue() + 1)); }
    private void validateDuration(Integer duration) { if (duration < 1 || duration > 500) throw new InvalidRequestException("Duration must be between 1 and 500 minutes"); }
    private List<Long> coerceLongList(List<?> values) { return values.stream().map(value -> Long.parseLong(value.toString())).toList(); }

    private void updateMovieActors(Movie movie, List<Long> actorIds) {
        movie.getActors().clear();
        if (actorIds != null && !actorIds.isEmpty()) {
            List<Actor> actors = actorRepository.findAllById(actorIds);
            if (actors.size() != actorIds.size()) throw new ResourceNotFoundException("Some actors not found");
            actors.forEach(movie::addActor);
        }
    }

    private void updateMovieGenres(Movie movie, List<Long> genreIds) {
        movie.getGenres().clear();
        if (genreIds != null && !genreIds.isEmpty()) {
            List<Genre> genres = genreRepository.findAllById(genreIds);
            if (genres.size() != genreIds.size()) throw new ResourceNotFoundException("Some genres not found");
            genres.forEach(movie::addGenre);
        }
    }

    public Movie addGenresToMovie(Long movieId, List<Long> genreIds) {
        Movie movie = getMovieById(movieId);
        List<Genre> genres = genreRepository.findAllById(genreIds);
        if (genres.size() != genreIds.size()) throw new ResourceNotFoundException("Some genres not found");
        genres.forEach(movie::addGenre);
        clearMovieCache(movieId);
        return movieRepository.save(movie);
    }

    public Movie removeGenresFromMovie(Long movieId, List<Long> genreIds) {
        Movie movie = getMovieById(movieId);
        genreRepository.findAllById(genreIds).forEach(movie::removeGenre);
        clearMovieCache(movieId);
        return movieRepository.save(movie);
    }

    public Movie addActorsToMovie(Long movieId, List<Long> actorIds) {
        Movie movie = getMovieById(movieId);
        List<Actor> actors = actorRepository.findAllById(actorIds);
        if (actors.size() != actorIds.size()) throw new ResourceNotFoundException("Some actors not found");
        actors.forEach(movie::addActor);
        clearMovieCache(movieId);
        return movieRepository.save(movie);
    }

    public Movie removeActorsFromMovie(Long movieId, List<Long> actorIds) {
        Movie movie = getMovieById(movieId);
        actorRepository.findAllById(actorIds).forEach(movie::removeActor);
        clearMovieCache(movieId);
        return movieRepository.save(movie);
    }

    public Movie updateMovieRelations(Long movieId, List<Long> genreIds, List<Long> actorIds) {
        Movie movie = getMovieById(movieId);
        if (genreIds != null) updateMovieGenres(movie, genreIds);
        if (actorIds != null) updateMovieActors(movie, actorIds);
        clearMovieCache(movieId);
        return movieRepository.save(movie);
    }

    public void deleteMovie(Long id, boolean force) {
        Movie movie = getMovieById(id);
        if (!force && (!movie.getGenres().isEmpty() || !movie.getActors().isEmpty())) {
            throw new InvalidRequestException("Cannot delete movie '" + movie.getTitle() + "' because it has relationships. Use force=true to delete anyway.");
        }
        if (force) {
            List.copyOf(movie.getGenres()).forEach(movie::removeGenre);
            List.copyOf(movie.getActors()).forEach(movie::removeActor);
        }
        clearMovieCache(id);
        movieRepository.delete(movie);
    }

    public void deleteMovie(Long id) { deleteMovie(id, false); }

    @Transactional(readOnly = true)
    public Page<Object[]> getAllMoviesWithActorCount(Pageable pageable) { return movieRepository.findAllWithActorCount(pageable); }

    @Transactional(readOnly = true)
    public Page<Movie> getLatestMovies(Pageable pageable) { return movieRepository.findByOrderByReleaseYearDesc(pageable); }

    @Transactional(readOnly = true)
    public List<Movie> getMoviesByDurationRange(Integer minDuration, Integer maxDuration) { return movieRepository.findByDurationBetween(minDuration, maxDuration); }

    @Transactional(readOnly = true)
    public List<Movie> getMoviesWithNoGenres() { return movieRepository.findMoviesWithNoGenres(); }

    @Transactional(readOnly = true)
    public List<Movie> getMoviesWithNoActors() { return movieRepository.findMoviesWithNoActors(); }

    @Transactional(readOnly = true)
    public boolean movieExists(Long id) { return movieRepository.existsById(id); }

    private void clearMovieCache(Long id) {
        cacheService.remove("all_movies");
        cacheService.remove("all_movies_cached");
        if (id != null) cacheService.remove("movie_" + id);
    }
}
