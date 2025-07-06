package com.grd.gradingbe.service.impl;

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
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService
{
    private final SecretKey key;
    private final String serverIss;

    private final Instant now = Instant.now();
    private final Instant authTokenExpiry = now.plus(7, ChronoUnit.DAYS);
    private final Instant refreshTokenExpiry = now.plus(30, ChronoUnit.DAYS);


    public JwtServiceImpl(@Value("${env.jwt.secret}") String secretKey,
                          @Value("${env.jwt.issuer}") String serverIss)
    {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.serverIss = serverIss;
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
