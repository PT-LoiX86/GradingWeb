package com.grd.gradingbe.controller;

import com.grd.gradingbe.configuration.CacheMonitor;
import com.grd.gradingbe.configuration.CacheWarmer;
import com.grd.gradingbe.dto.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/admin")
@RequiredArgsConstructor
@Tag(name = "Admin Management", description = "API endpoints for admin operations")
public class AdminController
{
    private final CacheMonitor cacheMonitor;
    private final CacheWarmer cacheWarmer;
    
    @GetMapping("/system32")
    public void testRole ()
    {
    }
    
    /**
     * Clear all caches
     */
    @PostMapping("/cache/clear-all")
    @Operation(summary = "Clear all caches", description = "Clear all cached data from Redis")
    public ResponseEntity<ApiResponse<String>> clearAllCaches() {
        cacheMonitor.clearAllCaches();
        return ResponseEntity.ok(ApiResponse.success("All caches cleared successfully", null));
    }
    
    /**
     * Clear specific cache
     */
    @PostMapping("/cache/clear/{cacheName}")
    @Operation(summary = "Clear specific cache", description = "Clear specific cache by name")
    public ResponseEntity<ApiResponse<String>> clearCache(@PathVariable String cacheName) {
        cacheMonitor.clearCache(cacheName);
        return ResponseEntity.ok(ApiResponse.success("Cache '" + cacheName + "' cleared successfully", null));
    }
    
    /**
     * Warm all caches
     */
    @PostMapping("/cache/warm")
    @Operation(summary = "Warm caches", description = "Pre-load frequently accessed data into cache")
    public ResponseEntity<ApiResponse<String>> warmCaches() {
        cacheWarmer.warmCache();
        return ResponseEntity.ok(ApiResponse.success("Cache warming initiated", null));
    }
    
    /**
     * Get cache statistics
     */
    @GetMapping("/cache/stats")
    @Operation(summary = "Get cache statistics", description = "Get current cache statistics")
    public ResponseEntity<ApiResponse<String>> getCacheStats() {
        cacheMonitor.logCacheStatistics();
        return ResponseEntity.ok(ApiResponse.success("Cache statistics logged to server logs", null));
    }
}
