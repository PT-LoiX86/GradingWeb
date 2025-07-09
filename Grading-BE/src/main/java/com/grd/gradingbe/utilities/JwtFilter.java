package com.grd.gradingbe.utilities;

import com.grd.gradingbe.dto.enums.TokenType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.grd.gradingbe.dto.response.ErrorResponse;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
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
    private final ObjectMapper objectMapper;

    public JwtFilter(JwtService jwtService, UserRepository userRepository, ObjectMapper objectMapper)
    {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain
    ) throws ServletException, IOException
    {
        String path = request.getServletPath();
        
        // Skip JWT validation for public auth endpoints
        if (isPublicEndpoint(path))
        {
            try
            {
                chain.doFilter(request, response);
                return;
            }
            catch (Exception e)
            {
                sendErrorResponse(response, HttpStatus.BAD_REQUEST.value(), e.getMessage());
            }
        }

        String token = extractToken(request);
        // Check if token exists
        if (token == null)
        {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Missing JWT token");
            return;
        }

        // Validate token format and signature
        if (!jwtService.validateToken(token)
            && !"access".equals(jwtService.extractHeader(TokenType.ACCESS, token).get("typ")))
        {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
        }

        // Check if token is expired
        if (jwtService.isTokenExpired(TokenType.ACCESS, token))
        {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "JWT token has expired");
            return;
        }

        // Extract user ID and fetch user
        try {
            Integer userId = Integer.valueOf(jwtService.extractClaim(TokenType.ACCESS, token, Claims::getSubject));
            User user = userRepository.findById(userId).orElse(null);
            
            if (user == null)
            {
                sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "User not found");
                return;
            }

            // Create authentication token
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userId, null, List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
            );
            auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(auth);
            
            chain.doFilter(request, response);
        } catch (Exception e) {
            sendErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid token format");
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        return publicPath(path);
    }

    private boolean publicPath(String path) {

        return  path.startsWith("/api/auth/") ||
                path.startsWith("/oauth2/") ||
                path.startsWith("/api/public/") ||
                path.startsWith("/actuator") ||
                path.startsWith("/webjars/") ||
                path.startsWith("/swagger-resources/") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/swagger-ui") ||
                path.equals("/swagger-ui.html") ||
                path.equals("/") ||
                path.startsWith("/error") ||
                path.equals("/favicon.ico");
    }

    private boolean isPublicEndpoint(String path) {
        return publicPath(path);
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(
                "uri=/api/request",
                HttpStatus.valueOf(status),
                message,
                LocalDateTime.now()
        );

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }

    private String extractToken(HttpServletRequest request)
    {
        String header = request.getHeader("Authorization");
        return (header != null && header.startsWith("Bearer "))
                ? header.substring(7)
                : null;
    }
}
