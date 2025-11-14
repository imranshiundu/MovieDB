package com.example.moviesapi.exception;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle ResourceNotFoundException - Improved for Issue 4
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            "Resource Not Found",
            ex.getMessage(),
            getRequestPath(request),
            LocalDateTime.now(),
            "RESOURCE_NOT_FOUND"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // Handle InvalidRequestException - Improved for Issue 3
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Request",
            ex.getMessage(),
            getRequestPath(request),
            LocalDateTime.now(),
            "INVALID_REQUEST"
        );
        
        // Add extended metadata if available
        if (ex.getErrorCode() != null) {
            errorResponse.setErrorCode(ex.getErrorCode());
        }
        if (ex.getFieldName() != null) {
            errorResponse.setFieldName(ex.getFieldName());
        }
        if (ex.getRejectedValue() != null) {
            errorResponse.setRejectedValue(ex.getRejectedValue());
        }
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle IllegalStateException (for force delete scenarios)
    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Operation",
            ex.getMessage(),
            getRequestPath(request),
            LocalDateTime.now(),
            "INVALID_OPERATION"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle validation errors from @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                    FieldError::getField,
                    fieldError -> fieldError.getDefaultMessage() != null ? fieldError.getDefaultMessage() : "Invalid value",
                    (existing, replacement) -> existing
                ));

        String errorMessage = "Validation failed for " + errors.size() + " field(s)";
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation Failed",
            errorMessage,
            getRequestPath(request),
            LocalDateTime.now()
        );
        errorResponse.setErrorCode("VALIDATION_ERROR");
        errorResponse.setValidationErrors(errors);
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle JSON parsing errors including invalid date formats
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, WebRequest request) {
        String errorMessage = "Invalid JSON format in request body";
        
        // Check if the error is due to invalid date format
        if (ex.getCause() instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException) ex.getCause();
            if (invalidFormatException.getTargetType() != null && 
                invalidFormatException.getTargetType().equals(java.time.LocalDate.class)) {
                errorMessage = "Invalid birthDate format. Expected format: yyyy-MM-dd (e.g., 1990-12-31)";
            }
        }
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid JSON",
            errorMessage,
            getRequestPath(request),
            LocalDateTime.now()
        );
        errorResponse.setErrorCode("INVALID_JSON_FORMAT");
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle type mismatch exceptions (e.g., string instead of number)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatchException(MethodArgumentTypeMismatchException ex, WebRequest request) {
        String errorMessage = String.format(
            "Parameter '%s' should be of type '%s'",
            ex.getName(),
            ex.getRequiredType() != null ? ex.getRequiredType().getSimpleName() : "unknown"
        );
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Type Mismatch",
            errorMessage,
            getRequestPath(request),
            LocalDateTime.now()
        );
        errorResponse.setErrorCode("TYPE_MISMATCH");
        errorResponse.setFieldName(ex.getName());
        errorResponse.setRejectedValue(ex.getValue());
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle illegal argument exceptions
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Invalid Argument",
            ex.getMessage(),
            getRequestPath(request),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ex.printStackTrace();
        
        ErrorResponse errorResponse = new ErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error",
            "An unexpected error occurred. Please try again later.",
            getRequestPath(request),
            LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Helper method to extract request path
    private String getRequestPath(WebRequest request) {
        String description = request.getDescription(false);
        if (description.startsWith("uri=")) {
            return description.substring(4); // Remove "uri=" prefix
        }
        return description;
    }

    // Inner class for standardized error response
    public static class ErrorResponse {
        private final int status;
        private final String error;
        private final String message;
        private final String path;
        private final LocalDateTime timestamp;
        private String errorCode;
        private String fieldName;
        private Object rejectedValue;
        private Map<String, String> validationErrors;

        // Constructor for basic errors
        public ErrorResponse(int status, String error, String message, String path, LocalDateTime timestamp) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.path = path;
            this.timestamp = timestamp;
        }

        // Constructor with error code
        public ErrorResponse(int status, String error, String message, String path, LocalDateTime timestamp, String errorCode) {
            this(status, error, message, path, timestamp);
            this.errorCode = errorCode;
        }

        // Getters
        public int getStatus() {
            return status;
        }

        public String getError() {
            return error;
        }

        public String getMessage() {
            return message;
        }

        public String getPath() {
            return path;
        }

        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public String getFieldName() {
            return fieldName;
        }

        public Object getRejectedValue() {
            return rejectedValue;
        }

        public Map<String, String> getValidationErrors() {
            return validationErrors;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public void setRejectedValue(Object rejectedValue) {
            this.rejectedValue = rejectedValue;
        }

        public void setValidationErrors(Map<String, String> validationErrors) {
            this.validationErrors = validationErrors;
        }
    }
}