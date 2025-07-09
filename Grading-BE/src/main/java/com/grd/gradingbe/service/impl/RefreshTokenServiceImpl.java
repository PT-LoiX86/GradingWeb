package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.model.RefreshToken;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.RefreshTokenRepository;
import com.grd.gradingbe.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {
    
    private final RefreshTokenRepository refreshTokenRepository;
    
    @Value("${env.jwt.refresh-token-validity-days:7}")
    private long refreshTokenValidityDays;
    
    @Value("${env.jwt.max-tokens-per-user:5}")
    private long maxTokensPerUser;
    
    @Override
    public RefreshToken createRefreshToken(User user) {
        log.info("Creating refresh token for user: {}", user.getId());
        
        cleanupUserTokensIfNeeded(user);
        
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(generateRefreshTokenValue())
                .expiryDate(LocalDateTime.now().plusDays(refreshTokenValidityDays))
                .build();
        
        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        log.info("Created refresh token with ID: {} for user: {}", savedToken.getId(), user.getId());
        
        return savedToken;
    }
    
    @Override
    @Transactional(readOnly = true)
    public RefreshToken validateRefreshToken(String token) {
        log.debug("Validating refresh token");
        
        RefreshToken refreshToken = refreshTokenRepository.findByTokenAndRevokedFalse(token)
                .orElseThrow(() -> {
                    log.warn("Refresh token not found or revoked");
                    return new ResourceNotFoundException("Refresh token", "token", "***");
                });
        
        if (refreshToken.isExpired()) {
            log.warn("Refresh token expired for user: {}", refreshToken.getUser().getId());
            refreshTokenRepository.delete(refreshToken);
            throw new RuntimeException("Refresh token is expired");
        }
        
        log.debug("Refresh token validated successfully for user: {}", refreshToken.getUser().getId());
        return refreshToken;
    }
    
    @Override
    public void deleteRefreshToken(String token) {
        log.info("Deleting refresh token");
        
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Refresh token", "token", "***"));
        
        refreshTokenRepository.delete(refreshToken);
        log.info("Deleted refresh token for user: {}", refreshToken.getUser().getId());
    }
    
    @Override
    public void deleteAllUserTokens(User user) {
        log.info("Deleting all refresh tokens for user: {}", user.getId());
        refreshTokenRepository.deleteByUser(user);
    }
    
    @Override
    public void revokeAllUserTokens(User user) {
        log.info("Revoking all refresh tokens for user: {}", user.getId());
        refreshTokenRepository.revokeAllUserTokens(user);
    }
    
    @Override
    public void cleanupExpiredTokens() {
        log.info("Cleaning up expired refresh tokens");
        refreshTokenRepository.deleteExpiredTokens(LocalDateTime.now());
    }
    
    @Override
    public RefreshToken updateLastUsed(String token) {
        RefreshToken refreshToken = validateRefreshToken(token);
        refreshToken.setLastUsed(LocalDateTime.now());
        return refreshTokenRepository.save(refreshToken);
    }
    
    private void cleanupUserTokensIfNeeded(User user) {
        long activeTokensCount = refreshTokenRepository.countActiveTokensByUser(user, LocalDateTime.now());
        
        if (activeTokensCount >= maxTokensPerUser) {
            log.info("User {} has {} active tokens, cleaning up old ones", user.getId(), activeTokensCount);
            revokeAllUserTokens(user);
        }
    }
    
    private String generateRefreshTokenValue() {
        return UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
    }
}
