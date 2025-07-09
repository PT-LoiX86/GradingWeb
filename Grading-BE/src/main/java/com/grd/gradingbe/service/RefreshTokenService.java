package com.grd.gradingbe.service;

import com.grd.gradingbe.model.RefreshToken;
import com.grd.gradingbe.model.User;

public interface RefreshTokenService {
    
    RefreshToken createRefreshToken(User user);
    
    RefreshToken validateRefreshToken(String token);
    
    void deleteRefreshToken(String token);
    
    void deleteAllUserTokens(User user);
    
    void revokeAllUserTokens(User user);
    
    void cleanupExpiredTokens();
    
    RefreshToken updateLastUsed(String token);
}
