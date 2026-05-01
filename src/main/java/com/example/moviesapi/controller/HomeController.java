package com.example.moviesapi.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "forward:/index.html";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "forward:/index.html";
    }

    @ResponseBody
    @GetMapping("/api/status")
    public ResponseEntity<Map<String, Object>> status() {
        return ResponseEntity.ok(Map.of(
            "message", "MovieDB API is running successfully",
            "status", "OK",
            "version", "2.0.0",
            "database", "SQLite",
            "ui", "/",
            "swagger", "/swagger-ui.html",
            "endpoints", Map.of(
                "movies", "/api/movies",
                "actors", "/api/actors",
                "genres", "/api/genres",
                "recommendations", "/api/recommendations"
            )
        ));
    }
}
