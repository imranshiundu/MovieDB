package com.example.moviesapi.metrics;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ApiMetricsService {
    private final Map<String, AtomicLong> endpointCallCounts = new ConcurrentHashMap<>();
    private final AtomicLong totalApiCalls = new AtomicLong(0);
    private final long startTime = System.currentTimeMillis();

    public void recordApiCall(String endpoint) {
        endpointCallCounts.computeIfAbsent(endpoint, k -> new AtomicLong(0)).incrementAndGet();
        totalApiCalls.incrementAndGet();
    }

    public Map<String, Object> getMetrics() {
        Map<String, Object> metrics = new java.util.HashMap<>();
        
        metrics.put("totalApiCalls", totalApiCalls.get());
        metrics.put("uptime", (System.currentTimeMillis() - startTime) / 1000 + " seconds");
        metrics.put("endpointStats", new java.util.HashMap<>(endpointCallCounts));
        
        return metrics;
    }

    public Map<String, Object> getHealth() {
        Map<String, Object> health = new java.util.HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", java.time.Instant.now().toString());
        health.put("memoryUsage", 
            (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024) + " MB");
        health.put("activeEndpoints", endpointCallCounts.size());
        return health;
    }
}