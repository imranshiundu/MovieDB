package com.example.moviesapi.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> home() {
        return ResponseEntity.ok(Map.of(
            "message", "🎬 Movies API is running successfully!",
            "status", "OK",
            "version", "1.0.0",
            "database", "SQLite",
            "endpoints", Map.of(
                "movies", "/api/movies",
                "actors", "/api/actors", 
                "genres", "/api/genres",
                "recommendations", "/api/recommendations"
            ),
            "documentation", "Check README.md for complete API documentation"
        ));
    }
}