package com.example.moviesapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    
    private String resourceName;
    private String fieldName;
    private Object fieldValue;

    // Basic constructor
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Detailed constructor for resource not found scenarios
    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s : '%s'", resourceName, fieldName, fieldValue));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    // Constructor with just resource name
    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id: %d", resourceName, id));
        this.resourceName = resourceName;
        this.fieldName = "id";
        this.fieldValue = id;
    }

    // Getters
    public String getResourceName() {
        return resourceName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }

    // Static factory methods for common scenarios
    public static ResourceNotFoundException forGenre(Long id) {
        return new ResourceNotFoundException("Genre", "id", id);
    }

    public static ResourceNotFoundException forMovie(Long id) {
        return new ResourceNotFoundException("Movie", "id", id);
    }

    public static ResourceNotFoundException forActor(Long id) {
        return new ResourceNotFoundException("Actor", "id", id);
    }

    public static ResourceNotFoundException forGenreName(String name) {
        return new ResourceNotFoundException("Genre", "name", name);
    }

    public static ResourceNotFoundException forMovieTitle(String title) {
        return new ResourceNotFoundException("Movie", "title", title);
    }

    public static ResourceNotFoundException forActorName(String name) {
        return new ResourceNotFoundException("Actor", "name", name);
    }
}