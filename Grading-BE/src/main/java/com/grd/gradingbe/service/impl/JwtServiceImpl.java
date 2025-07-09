package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.enums.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grd.gradingbe.exception.JwtManagementException;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService
{
    private final SecretKey key;
    private final String serverIss;

    private final long authTokenExpiryMinutes = 15; // 15 minutes for access token
    private final long refreshTokenExpiryDays = 7; // 7 days for refresh token


    public JwtServiceImpl(@Value("${env.jwt.secret}") String secretKey,
                          @Value("${env.jwt.issuer}") String serverIss)
    {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.serverIss = serverIss;
    }

    public String generateAuthenticationToken(User user) {
        Instant now = Instant.now();
        Instant authTokenExpiry = now.plus(authTokenExpiryMinutes, ChronoUnit.MINUTES);
        
        return Jwts.builder()
                .header().add("typ", "access")
                .and()
                .claim("role", user.getRole().toString())
                .issuer(serverIss)
                .subject(user.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(authTokenExpiry))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        Instant refreshTokenExpiry = now.plus(refreshTokenExpiryDays, ChronoUnit.DAYS);
        
        return Jwts.builder()
                .header().add("typ", "refresh")
                .and()
                .issuer(serverIss)
                .subject(user.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(refreshTokenExpiry))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    public String generatePayloadToken(User user, Map<String, Object> claims, long time, ChronoUnit unit)
    {
        Instant now = Instant.now();
        Instant refreshTokenExpiry = now.plus(refreshTokenExpiryDays, ChronoUnit.DAYS);
        
        return Jwts.builder()
                .header().add("typ", "payload")
                .and()
                .claims(claims)
                .issuer(serverIss)
                .subject(user.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(payloadTokenExpiry))
                .signWith(key, Jwts.SIG.HS512)
                .compact();
    }

    public boolean validateToken(String token)
    {
        try
        {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    public boolean isTokenExpired(TokenType type, String token)
    {
        try
        {
            return extractClaim(type, token, Claims::getExpiration).before(Date.from(Instant.now()));
        }
        catch (ExpiredJwtException e)
        {
            return true;
        }
    }

    public <T> T extractClaim(TokenType type, String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(type, token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(TokenType type, String token)
    {
        try
        {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        }
        catch (Exception e)
        {
            throw new JwtManagementException(type, "Extract claims", "Failed to extract all token claims");
        }
    }

    public Map<String, Object> extractHeader(TokenType type, String token)
    {
        try
        {
            String[] parts = token.split("\\.");
            String tokenHeader = parts[0];
            String headerDecoded = new String(Base64.getUrlDecoder().decode(tokenHeader));

            ObjectMapper mapper = new ObjectMapper();

            return mapper.readValue(headerDecoded, Map.class);
        }
        catch (Exception e)
        {
            throw new JwtManagementException(type, "Extract Header", "Failed to extract token's header");
        }
    }
}
