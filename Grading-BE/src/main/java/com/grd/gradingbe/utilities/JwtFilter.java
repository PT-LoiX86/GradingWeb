package com.grd.gradingbe.utilities;

import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter
{
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtFilter(JwtService jwtService, UserRepository userRepository)
    {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException
    {
        if (request.getServletPath().startsWith("/api/auth/login")
                || request.getServletPath().startsWith("/api/auth/register")
                || request.getServletPath().startsWith("/api/auth/reset-password")
                || request.getServletPath().startsWith("/api/auth/forgot-password"))
        {
            chain.doFilter(request, response);
            return;
        }

        String token = extractToken(request);
        if (token == null || !jwtService.validateToken(token) || jwtService.isTokenExpired(token))
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Invalid or missing JWT token\"}");
            return;
        }

        Integer userId = jwtService.extractClaim(token, claims -> Integer.parseInt(claims.getSubject()));
        User user = userRepository.findById(userId).orElse(null);
        if (user == null)
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"User not found\"}");
            return;
        }

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                userId, null, List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
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
