package com.example.moviesapi.cache;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/cache")
public class CacheController {

    private final SimpleCacheService cacheService;

    public CacheController(SimpleCacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getCacheStats() {
        return ResponseEntity.ok(cacheService.getStats());
    }

    @PostMapping("/clear")
    public ResponseEntity<Map<String, Object>> clearCache() {
        cacheService.clear();
        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "Cache cleared successfully",
            "timestamp", System.currentTimeMillis()
        ));
    }

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getCacheInfo() {
        return ResponseEntity.ok(Map.of(
            "service", "Simple In-Memory Cache",
            "defaultTTL", "10 minutes",
            "implementation", "ConcurrentHashMap",
            "threadSafe", true,
            "autoExpiry", true
        ));
    }
}