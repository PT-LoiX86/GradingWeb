package com.grd.gradingbe.utilities;

import com.grd.gradingbe.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.util.Collections;

public class JwtFilter
{
    private final JwtService jwtService;

    public JwtFilter(JwtService jwtService) {this.jwtService = jwtService;}

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException
    {
        if (request.getServletPath().startsWith("/api/user/login"))
        {
            chain.doFilter(request, response);
            return;
        }

        String token = extractToken(request);
        if (token == null || !jwtService.validateToken(token)) {
            chain.doFilter(request, response);
            return;
        }

        Integer userId = jwtService.extractAuthenticationClaim(token);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userId, null, Collections.emptyList()
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
        chain.doFilter(request, response);
    }

    private String extractToken(HttpServletRequest request)
    {
        String header = request.getHeader("Authorization");
        return (header != null && header.startsWith("Bearer "))
                ? header.substring(7)
                : null;
    }
}
