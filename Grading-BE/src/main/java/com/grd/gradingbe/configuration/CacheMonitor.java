package com.grd.gradingbe.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Cache Monitoring Component
 * Monitor cache performance và log cache statistics
 */
@Component
@Slf4j
public class CacheMonitor {
    
    private final CacheManager cacheManager;
    
    public CacheMonitor(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }
    
    /**
     * Log cache statistics mỗi 30 phút
     */
    @Scheduled(fixedRate = 1800000) // 30 phút
    public void logCacheStatistics() {
        log.info("=== Cache Statistics ===");
        
        Collection<String> cacheNames = cacheManager.getCacheNames();
        
        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                logCacheInfo(cacheName, cache);
            }
        }
        
        log.info("=== End Cache Statistics ===");
    }
    
    /**
     * Log cache info cho từng cache
     */
    private void logCacheInfo(String cacheName, Cache cache) {
        try {
            // Để đơn giản, chỉ log cache name
            // Có thể extend để log thêm hit/miss ratios nếu Redis cache support
            log.info("Cache [{}] is active", cacheName);
            
        } catch (Exception e) {
            log.warn("Error getting statistics for cache [{}]: {}", cacheName, e.getMessage());
        }
    }
    
    /**
     * Clear all caches (useful for development/debugging)
     */
    public void clearAllCaches() {
        log.info("Clearing all caches...");
        
        Collection<String> cacheNames = cacheManager.getCacheNames();
        
        for (String cacheName : cacheNames) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                cache.clear();
                log.info("Cleared cache: {}", cacheName);
            }
        }
        
        log.info("All caches cleared");
    }
    
    /**
     * Clear specific cache
     */
    public void clearCache(String cacheName) {
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.clear();
            log.info("Cleared cache: {}", cacheName);
        } else {
            log.warn("Cache not found: {}", cacheName);
        }
    }
}
