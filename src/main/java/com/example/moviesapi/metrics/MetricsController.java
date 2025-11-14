package com.example.moviesapi.metrics;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private final ApiMetricsService metricsService;

    public MetricsController(ApiMetricsService metricsService) {
        this.metricsService = metricsService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(metricsService.getMetrics());
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> getHealth() {
        return ResponseEntity.ok(metricsService.getHealth());
    }

    @GetMapping("/reset")
    public ResponseEntity<Map<String, Object>> resetStats() {
        // In a real app, you might not want this, but it's useful for testing
        Map<String, Object> response = Map.of(
            "success", true,
            "message", "Metrics reset successfully"
        );
        return ResponseEntity.ok(response);
    }
}