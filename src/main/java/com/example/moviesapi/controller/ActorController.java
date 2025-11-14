package com.example.moviesapi.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.moviesapi.dto.ActorRequest;
import com.example.moviesapi.exception.InvalidRequestException;
import com.example.moviesapi.exception.ResourceNotFoundException;
import com.example.moviesapi.model.Actor;
import com.example.moviesapi.model.Movie;
import com.example.moviesapi.service.ActorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/actors")
@CrossOrigin(origins = "*")
public class ActorController {

    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    // CREATE - POST /api/actors 
    @PostMapping
    public ResponseEntity<?> createActor(@Valid @RequestBody ActorRequest actorRequest) {
        try {
            Actor createdActor = actorService.createActor(actorRequest);
            URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdActor.getId())
                .toUri();
            return ResponseEntity.created(location).body(createdActor);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Failed to create actor: " + e.getMessage()));
        }
    }

    // READ ALL - GET /api/actors
    @GetMapping
    public ResponseEntity<List<Actor>> getAllActors() {
        List<Actor> actors = actorService.getAllActors();
        return ResponseEntity.ok(actors);
    }

    // READ BY ID - GET /api/actors/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getActorById(@PathVariable Long id) {
        try {
            Actor actor = actorService.getActorById(id);
            return ResponseEntity.ok(actor);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve actor: " + e.getMessage()));
        }
    }

    // UPDATE - PATCH /api/actors/{id}
    @PatchMapping("/{id}")
    public ResponseEntity<?> updateActor(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            if (updates.containsKey("id")) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "ID field cannot be updated"));
            }
            
            Actor updatedActor = actorService.partialUpdateActor(id, updates);
            return ResponseEntity.ok(updatedActor);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        } catch (InvalidRequestException e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to update actor: " + e.getMessage()));
        }
    }

    // DELETE - DELETE /api/actors/{id} - FIXED for Issue 3 & 4
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteActor(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean force) {
        try {
            actorService.deleteActor(id, force);
            return ResponseEntity.noContent().build();
        } catch (ResourceNotFoundException e) {
            // Issue 4: Return 404 only when actor truly doesn't exist
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        } catch (InvalidRequestException e) {
            // Issue 3: Return 400 when actor exists but has relationships
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to delete actor: " + e.getMessage()));
        }
    }

    // SEARCH - GET /api/actors/search?name={name}
    @GetMapping("/search")
    public ResponseEntity<?> searchActorsByName(
            @RequestParam String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0 || size <= 0 || size > 100) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid pagination parameters: page must be >= 0, size between 1 and 100"));
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Actor> actors = actorService.searchActorsByName(name, pageable);
            return ResponseEntity.ok(actors);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Search failed: " + e.getMessage()));
        }
    }

    // GET MOVIES BY ACTOR - GET /api/actors/{id}/movies
    @GetMapping("/{id}/movies")
    public ResponseEntity<?> getMoviesByActorId(@PathVariable Long id) {
        try {
            List<Movie> movies = actorService.getMoviesByActorId(id);
            return ResponseEntity.ok(movies);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to retrieve movies: " + e.getMessage()));
        }
    }

    // PAGINATED ACTORS - GET /api/actors/paged
    @GetMapping("/paged")
    public ResponseEntity<?> getAllActorsPaged(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            if (page < 0 || size <= 0 || size > 100) {
                return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid pagination parameters: page must be >= 0, size between 1 and 100"));
            }
            Pageable pageable = PageRequest.of(page, size);
            Page<Actor> actors = actorService.getAllActors(pageable);
            return ResponseEntity.ok(actors);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(Map.of("error", "Failed to retrieve actors: " + e.getMessage()));
        }
    }

    // CHECK IF ACTOR EXISTS - GET /api/actors/{id}/exists
    @GetMapping("/{id}/exists")
    public ResponseEntity<Boolean> actorExists(@PathVariable Long id) {
        boolean exists = actorService.actorExists(id);
        return ResponseEntity.ok(exists);
    }
}