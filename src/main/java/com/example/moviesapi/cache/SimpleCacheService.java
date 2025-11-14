package com.example.moviesapi.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

@Service
public class SimpleCacheService {
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();
    private final long DEFAULT_TTL = TimeUnit.MINUTES.toMillis(10); // 10 minutes

    public void put(String key, Object value) {
        put(key, value, DEFAULT_TTL);
    }

    public void put(String key, Object value, long ttlMillis) {
        cache.put(key, new CacheEntry(value, System.currentTimeMillis() + ttlMillis));
    }

    public Object get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry != null && !entry.isExpired()) {
            return entry.getValue();
        }
        if (entry != null) {
            cache.remove(key); // Remove expired entry
        }
        return null;
    }

    public boolean contains(String key) {
        CacheEntry entry = cache.get(key);
        if (entry != null && entry.isExpired()) {
            cache.remove(key);
            return false;
        }
        return entry != null;
    }

    public void remove(String key) {
        cache.remove(key);
    }

    public void clear() {
        cache.clear();
    }

    public int size() {
        // Clean up expired entries while calculating size
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        return cache.size();
    }

    public Map<String, Object> getStats() {
        // Clean up expired entries before getting stats
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
        
        int totalEntries = cache.size();
        int activeEntries = totalEntries; // All remaining are active after cleanup
        int expiredEntries = 0; // All expired entries have been removed

        return Map.of(
            "totalEntries", totalEntries,
            "activeEntries", activeEntries,
            "expiredEntries", expiredEntries,
            "cacheSize", size()
        );
    }

    private static class CacheEntry {
        private final Object value;
        private final long expirationTime;

        public CacheEntry(Object value, long expirationTime) {
            this.value = value;
            this.expirationTime = expirationTime;
        }

        public Object getValue() {
            return value;
        }

        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
    }
}