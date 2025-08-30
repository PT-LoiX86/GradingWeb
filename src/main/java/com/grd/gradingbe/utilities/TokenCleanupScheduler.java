package com.grd.gradingbe.utilities;

import com.grd.gradingbe.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class TokenCleanupScheduler {
    
    private final RefreshTokenService refreshTokenService;
    
    @Scheduled(fixedRate = 3600000) // Run every hour (3600000 ms)
    public void cleanupExpiredTokens() {
        log.info("Starting cleanup of expired refresh tokens");
        refreshTokenService.cleanupExpiredTokens();
        log.info("Finished cleanup of expired refresh tokens");
    }
}
