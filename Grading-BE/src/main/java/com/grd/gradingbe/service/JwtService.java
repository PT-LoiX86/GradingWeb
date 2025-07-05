package com.grd.gradingbe.service;

import com.grd.gradingbe.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public class JwtService
{
    private final SecretKey key;
    @Value("${app.jwt.issuer}") String serverIss;

    public JwtService(@Value("${jwt.secret}") String secretKey)
    {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAuthenticationToken(User user) {
        return Jwts.builder()
                .claims()
                .issuer(serverIss)
                .subject(account.getUser_id().toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + authTokenExpirationMs))
                .and()
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String generateVerificationToken(Map<String, Object> claims, String email)
    {
        return  Jwts.builder()
                .claims(claims)
                .issuer(serverIss)
                .subject(email)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + regTokenExpirationMs))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public Claims extractAllClaims(String token)
    {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Integer extractAuthenticationClaim(String token)
    {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return Integer.parseInt(claims.getSubject());
    }

    private Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }

    public boolean isTokenExpired(String token)
    {
        try
        {
            return extractExpiration(token).before(new Date());
        }
        catch (ExpiredJwtException e)
        {
            throw new ApiRequestException("Token expired");
        }
    }
}
