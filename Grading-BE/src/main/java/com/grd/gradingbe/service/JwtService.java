package com.grd.gradingbe.service;

import com.grd.gradingbe.model.User;
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
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService
{
    private final SecretKey key;
    @Value("${app.jwt.issuer}") String serverIss;

    private final Instant now = Instant.now();
    private final Instant authTokenExpiry = now.plus(7, ChronoUnit.DAYS);
    private final Instant refreshTokenExpiry = now.plus(30, ChronoUnit.DAYS);


    public JwtService(@Value("${jwt.secret}") String secretKey)
    {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAuthenticationToken(User user) {
        return Jwts.builder()
                .header().add("typ", "access")
                .and()
                .issuer(serverIss)
                .subject(user.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(authTokenExpiry))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(User user) {
        return Jwts.builder()
                .header().add("typ", "refresh")
                .and()
                .issuer(serverIss)
                .subject(user.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(refreshTokenExpiry))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public String generatePayloadToken(User user, Claims claims)
    {
        return Jwts.builder()
                .header().add("typ", "payload")
                .and()
                .claims(claims)
                .issuer(serverIss)
                .subject(user.getId().toString())
                .issuedAt(Date.from(now))
                .expiration(Date.from(refreshTokenExpiry))
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

    public boolean isTokenExpired(String token)
    {
        try
        {
            return extractClaim(token, Claims::getExpiration).before(Date.from(now));
        }
        catch (ExpiredJwtException e)
        {
            //Throw exception
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
}
