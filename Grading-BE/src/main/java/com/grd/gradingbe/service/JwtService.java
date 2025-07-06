package com.grd.gradingbe.service;

import com.grd.gradingbe.model.User;
import io.jsonwebtoken.Claims;

import java.util.function.Function;

public interface JwtService
{
    String generateAuthenticationToken(User user);
    String generateRefreshToken(User user);
    String generatePayloadToken(User user, Claims claims);
    boolean validateToken(String token);
    boolean isTokenExpired(String token);
    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
}
