package com.example.moviesapi.service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.moviesapi.dto.ActorRequest;
import com.example.moviesapi.exception.InvalidRequestException;
import com.example.moviesapi.exception.ResourceNotFoundException;
import com.example.moviesapi.model.Actor;
import com.example.moviesapi.model.Movie;
import com.example.moviesapi.repository.ActorRepository;
import com.example.moviesapi.repository.MovieRepository;

@Service
@Transactional
public class ActorService {

    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public ActorService(ActorRepository actorRepository, MovieRepository movieRepository) {
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;
    }

    // CREATE from ActorRequest DTO
    public Actor createActor(ActorRequest actorRequest) {
        if (actorRequest.getName() == null || actorRequest.getName().trim().isEmpty()) {
            throw new InvalidRequestException("Actor name is required");
        }
        if (actorRequest.getBirthDate() == null) {
            throw new InvalidRequestException("Birth date is required");
        }

        if (actorRequest.getBirthDate().isAfter(LocalDate.now())) {
            throw new InvalidRequestException("Birth date cannot be in the future");
        }

        // Check for existing actor with same name and birth date
        if (actorRepository.existsByNameAndBirthDate(actorRequest.getName().trim(), actorRequest.getBirthDate())) {
            throw new InvalidRequestException("Actor with name '" + actorRequest.getName() + 
                "' and birth date '" + actorRequest.getBirthDate() + "' already exists");
        }

        Actor actor = new Actor();
        actor.setName(actorRequest.getName().trim());
        actor.setBirthDate(actorRequest.getBirthDate());

        // Handle movie associations if provided
        if (actorRequest.hasMovieIds()) {
            List<Movie> movies = movieRepository.findAllById(actorRequest.getMovieIds());
            actor.setMovies(new HashSet<>(movies));
        }

        // For SQLite, use the alternative approach directly
        return createActorAlternative(actor);
    }

    // CREATE from Actor entity (keep existing method for backward compatibility)
    public Actor createActor(Actor actor) {
        if (actor.getName() == null || actor.getName().trim().isEmpty()) {
            throw new InvalidRequestException("Actor name is required");
        }
        if (actor.getBirthDate() == null) {
            throw new InvalidRequestException("Birth date is required");
        }

        if (actor.getBirthDate().isAfter(LocalDate.now())) {
            throw new InvalidRequestException("Birth date cannot be in the future");
        }

        // Check for existing actor with same name and birth date
        if (actorRepository.existsByNameAndBirthDate(actor.getName().trim(), actor.getBirthDate())) {
            throw new InvalidRequestException("Actor with name '" + actor.getName() + 
                "' and birth date '" + actor.getBirthDate() + "' already exists");
        }

        actor.setName(actor.getName().trim());

        // For SQLite, use the alternative approach directly
        return createActorAlternative(actor);
    }

    // Alternative creation method for SQLite
    private Actor createActorAlternative(Actor actor) {
        // Get the next available ID manually for SQLite
        Long nextId = findNextAvailableId();
        actor.setId(nextId);
        
        // Save with explicit ID
        return actorRepository.save(actor);
    }

    // Find next available ID for SQLite
    private Long findNextAvailableId() {
        // Get the maximum current ID and add 1
        Actor lastActor = actorRepository.findTopByOrderByIdDesc();
        if (lastActor != null && lastActor.getId() != null) {
            return lastActor.getId() + 1;
        }
        return 1L; // Start with 1 if no actors exist
    }

    // READ ALL
    @Transactional(readOnly = true)
    public List<Actor> getAllActors() {
        return actorRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Actor> getAllActors(Pageable pageable) {
        return actorRepository.findAll(pageable);
    }

    // READ BY ID
    @Transactional(readOnly = true)
    public Actor getActorById(Long id) {
        return actorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Actor not found with id: " + id));
    }

    // GET MOVIES BY ACTOR
    @Transactional(readOnly = true)
    public List<Movie> getMoviesByActorId(Long actorId) {
        Actor actor = getActorById(actorId);
        return List.copyOf(actor.getMovies());
    }

    // SEARCH BY NAME
    @Transactional(readOnly = true)
    public Page<Actor> searchActorsByName(String name, Pageable pageable) {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidRequestException("Search name cannot be empty");
        }
        return actorRepository.findByNameContainingIgnoreCase(name.trim(), pageable);
    }

    // PARTIAL UPDATE
    public Actor partialUpdateActor(Long id, Map<String, Object> updates) {
        Actor actor = getActorById(id);
        
        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    if (value != null) {
                        String newName = value.toString().trim();
                        if (!newName.isEmpty()) {
                            if (actorRepository.existsByNameAndBirthDate(newName, actor.getBirthDate()) && 
                                !actor.getName().equals(newName)) {
                                throw new InvalidRequestException("Actor with name '" + newName + 
                                    "' and birth date '" + actor.getBirthDate() + "' already exists");
                            }
                            actor.setName(newName);
                        } else {
                            throw new InvalidRequestException("Actor name cannot be empty");
                        }
                    }
                    break;
                    
                case "birthDate":
                    if (value != null) {
                        try {
                            LocalDate newBirthDate = LocalDate.parse(value.toString());
                            if (newBirthDate.isAfter(LocalDate.now())) {
                                throw new InvalidRequestException("Birth date cannot be in the future");
                            }
                            if (actorRepository.existsByNameAndBirthDate(actor.getName(), newBirthDate) && 
                                !actor.getBirthDate().equals(newBirthDate)) {
                                throw new InvalidRequestException("Actor with name '" + actor.getName() + 
                                    "' and birth date '" + newBirthDate + "' already exists");
                            }
                            actor.setBirthDate(newBirthDate);
                        } catch (DateTimeParseException e) {
                            throw new InvalidRequestException("Invalid birth date format. Use yyyy-MM-dd");
                        }
                    }
                    break;
                    
                default:
                    break;
            }
        });

        return actorRepository.save(actor);
    }

    // DELETE - Enhanced with better relationship checking
    public void deleteActor(Long id, boolean force) {
        // First check if actor exists - this will throw ResourceNotFoundException if not found
        Actor actor = getActorById(id);
        
        // Check if actor has movie relationships
        if (!force && hasMovieRelationships(actor)) {
            int movieCount = actor.getMovies().size();
            String movieTitles = getMovieTitles(actor);
            
            throw new InvalidRequestException(
                "Cannot delete actor '" + actor.getName() + 
                "' because they are associated with " + movieCount + " movie" + 
                (movieCount > 1 ? "s" : "") + ": " + movieTitles + 
                ". Use force=true to delete anyway."
            );
        }

        // If force is true or no relationships exist, proceed with deletion
        if (force && hasMovieRelationships(actor)) {
            // Remove actor from all movies before deletion
            List<Movie> movies = List.copyOf(actor.getMovies());
            for (Movie movie : movies) {
                actor.removeMovie(movie);
            }
        }

        actorRepository.delete(actor);
    }

    // Helper method to check if actor has movie relationships
    private boolean hasMovieRelationships(Actor actor) {
        return actor.getMovies() != null && !actor.getMovies().isEmpty();
    }

    // Helper method to get movie titles for error message
    private String getMovieTitles(Actor actor) {
        if (actor.getMovies() == null || actor.getMovies().isEmpty()) {
            return "";
        }
        
        List<String> titles = actor.getMovies().stream()
                .map(Movie::getTitle)
                .limit(5) // Limit to 5 titles to avoid overly long error messages
                .toList();
        
        return String.join(", ", titles) + (actor.getMovies().size() > 5 ? " and more..." : "");
    }

    // VALIDATION
    @Transactional(readOnly = true)
    public boolean actorExists(Long id) {
        return actorRepository.existsById(id);
    }

    // BULK GET ACTORS BY IDS
    @Transactional(readOnly = true)
    public List<Actor> getActorsByIds(List<Long> actorIds) {
        return actorRepository.findAllById(actorIds);
    }
}