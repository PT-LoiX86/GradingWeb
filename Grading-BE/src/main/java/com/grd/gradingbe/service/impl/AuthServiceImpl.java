package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.request.LoginRequest;
import com.grd.gradingbe.dto.request.RegisterRequest;
import com.grd.gradingbe.enums.Role;
import com.grd.gradingbe.exception.ResourceAlreadyExistException;
import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.AuthService;
import com.grd.gradingbe.service.JwtService;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class AuthServiceImpl implements AuthService
{
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Map<String, String> login(LoginRequest request)
    {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateAuthenticationToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    public Map<String, String> register(RegisterRequest request)
    {
        if (userRepository.findByEmail(request.getEmail()).isPresent())
        {
            throw new ResourceAlreadyExistException("Email already exist");
        }
        else if (userRepository.findByUsername(request.getUsername()).isPresent())
        {
            throw new ResourceAlreadyExistException("Username already exist");
        }

        User user;
        try
        {
            user = userRepository.save(
                            User.builder()
                                    .username(request.getUsername())
                                    .password_hash(passwordEncoder.encode(request.getPassword()))
                                    .email(request.getEmail())
                                    .role(userRepository.findUserByRole(Role.ADMIN).isPresent()
                                            ? Role.USER : Role.ADMIN)
                                    .full_name(request.getFullName())
                                    .updated_at(LocalDateTime.now())
                                    .created_at(LocalDateTime.now())
                                    .build());
        }
        catch (DataAccessException e)
        {
            throw new IllegalStateException("Failed to save user to the database.", e);
        }

        String accessToken = jwtService.generateAuthenticationToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
    }

    public Map<String, String> refreshToken(String refreshToken) {
        if (refreshToken == null || !jwtService.validateToken(refreshToken) || jwtService.isTokenExpired(refreshToken)) {
            throw new IllegalArgumentException("Invalid or expired refresh token");
        }

        Integer userId = jwtService.extractClaim(refreshToken, claims -> Integer.parseInt(claims.getSubject()));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        String newAccessToken = jwtService.generateAuthenticationToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken
        );
    }
}
