package com.grd.gradingbe.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class RateLimitingService {
    
    private final Map<String, RequestInfo> requestTracker = new ConcurrentHashMap<>();
    
    public boolean isRequestAllowed(String clientIp, int maxRequests, int timeWindowMinutes) {
        LocalDateTime now = LocalDateTime.now();
        String key = clientIp + "_" + timeWindowMinutes;
        RequestInfo requestInfo = requestTracker.computeIfAbsent(key, k -> new RequestInfo());
        
        // Clean up old requests
        if (requestInfo.lastRequestTime != null && 
            ChronoUnit.MINUTES.between(requestInfo.lastRequestTime, now) >= timeWindowMinutes) {
            requestInfo.requestCount = 0;
        }
        
        if (requestInfo.requestCount >= maxRequests) {
            return false;
        }
        
        requestInfo.requestCount++;
        requestInfo.lastRequestTime = now;
        return true;
    }
    
    @Scheduled(fixedRate = 300000) // Clean up every 5 minutes
    public void cleanupOldEntries() {
        LocalDateTime now = LocalDateTime.now();
        requestTracker.entrySet().removeIf(entry -> {
            RequestInfo info = entry.getValue();
            return info.lastRequestTime != null && 
                   ChronoUnit.MINUTES.between(info.lastRequestTime, now) >= 10;
        });
        
        log.debug("Cleaned up rate limiting cache. Current entries: {}", requestTracker.size());
    }
    
    public static class RequestInfo {
        public int requestCount = 0;
        public LocalDateTime lastRequestTime;
    }
}
