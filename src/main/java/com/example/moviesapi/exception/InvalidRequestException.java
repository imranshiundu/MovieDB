package com.example.moviesapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidRequestException extends RuntimeException {
    
    private String errorCode;
    private String fieldName;
    private Object rejectedValue;

    // Basic constructor
    public InvalidRequestException(String message) {
        super(message);
    }

    // Constructor with error code
    public InvalidRequestException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    // Constructor with field details
    public InvalidRequestException(String message, String fieldName, Object rejectedValue) {
        super(message);
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
    }

    // Constructor with all details
    public InvalidRequestException(String message, String errorCode, String fieldName, Object rejectedValue) {
        super(message);
        this.errorCode = errorCode;
        this.fieldName = fieldName;
        this.rejectedValue = rejectedValue;
    }

    // Getters
    public String getErrorCode() {
        return errorCode;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    // Static factory methods for common validation errors
    public static InvalidRequestException duplicateGenre(String name) {
        return new InvalidRequestException(
            "Genre with name '" + name + "' already exists",
            "DUPLICATE_GENRE",
            "name",
            name
        );
    }

    public static InvalidRequestException duplicateMovie(String title, Integer releaseYear) {
        return new InvalidRequestException(
            "Movie with title '" + title + "' and release year '" + releaseYear + "' already exists",
            "DUPLICATE_MOVIE",
            "title",
            title
        );
    }

    public static InvalidRequestException duplicateActor(String name, String birthDate) {
        return new InvalidRequestException(
            "Actor with name '" + name + "' and birth date '" + birthDate + "' already exists",
            "DUPLICATE_ACTOR",
            "name",
            name
        );
    }

    public static InvalidRequestException futureBirthDate() {
        return new InvalidRequestException(
            "Birth date cannot be in the future",
            "FUTURE_BIRTH_DATE",
            "birthDate",
            null
        );
    }

    public static InvalidRequestException invalidReleaseYear(Integer year) {
        return new InvalidRequestException(
            "Release year must be between 1888 and " + (java.time.Year.now().getValue() + 1),
            "INVALID_RELEASE_YEAR",
            "releaseYear",
            year
        );
    }

    public static InvalidRequestException cannotDeleteWithRelationships(String resourceType, int relationshipCount, String relationshipName) {
        String message = String.format(
            "Cannot delete %s because it has %d associated %s. Use force=true to delete anyway.",
            resourceType.toLowerCase(),
            relationshipCount,
            relationshipName + (relationshipCount > 1 ? "s" : "")
        );
        return new InvalidRequestException(message, "HAS_RELATIONSHIPS");
    }

    public static InvalidRequestException invalidPaginationParameters(int page, int size) {
        return new InvalidRequestException(
            String.format("Invalid pagination parameters: page=%d, size=%d. Page must be >= 0 and size between 1 and 100", page, size),
            "INVALID_PAGINATION",
            "page/size",
            "page=" + page + ", size=" + size
        );
    }
}