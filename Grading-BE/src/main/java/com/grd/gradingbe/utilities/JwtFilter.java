package com.grd.gradingbe.utilities;

import com.grd.gradingbe.enums.TokenTypes;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
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
        if (request.getServletPath().startsWith("/api/auth")
            || request.getServletPath().startsWith("/login"))
        {
            try
            {
                chain.doFilter(request, response);
                return;
            }
            catch (Exception e)
            {
                response.setStatus(HttpStatus.BAD_REQUEST.value());
                response.setContentType("application/json");
                String errorJson = String.format(
                        "{\"status\":%d,\"errorMessage\":\"%s\",\"timestamp\":\"%s\"}",
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage(),
                        LocalDateTime.now()
                );
                response.getWriter().write(errorJson);
            }
        }

        String token = extractToken(request);
        if (token == null || !jwtService.validateToken(token))
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"errorMessage\": \"Token is invalid\"}");
            return;
        }
        else if (jwtService.isTokenExpired(TokenTypes.ACCESS, token))
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"errorMessage\": \"Token is expired\"}");
            return;
        }

        Integer userId = jwtService.extractClaim(TokenTypes.ACCESS ,token, claims -> Integer.parseInt(claims.getSubject()));
        User user = userRepository.findById(userId).orElse(null);

        if (user == null)
        {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"errorMessage\": \"User's own token is not exist\"}");
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
