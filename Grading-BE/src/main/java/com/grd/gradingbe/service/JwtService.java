package com.grd.gradingbe.service;

import com.grd.gradingbe.enums.TokenType;
import com.grd.gradingbe.model.User;
import io.jsonwebtoken.Claims;

import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.function.Function;

public interface JwtService
{
    String generateAuthenticationToken(User user);

    String generateRefreshToken(User user);

    String generatePayloadToken(User user, Map<String, Object> claims, long time, ChronoUnit unit);

    boolean validateToken(String token);

    boolean isTokenExpired(TokenType type, String token);

    <T> T extractClaim(TokenType type, String token, Function<Claims, T> claimsResolver);

    Map<String, Object> extractHeader(TokenType type, String token);
}
