package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.request.LoginRequest;
import com.grd.gradingbe.dto.request.RegisterRequest;
import com.grd.gradingbe.dto.response.LoginResponse;
import com.grd.gradingbe.dto.response.UserResponse;
import com.grd.gradingbe.enums.AuthenticationType;
import com.grd.gradingbe.enums.Role;
import com.grd.gradingbe.exception.ResourceAlreadyExistException;
import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.model.RefreshToken;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.AuthService;
import com.grd.gradingbe.service.JwtService;
import com.grd.gradingbe.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthServiceImpl implements AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenService refreshTokenService;

    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("User login attempt: {}", request.getUsername());
        
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt for user: {}", request.getUsername());
            throw new BadCredentialsException("Invalid username or password");
        }
        
        User user = (User) authentication.getPrincipal();
        log.info("User logged in successfully: {}", user.getId());
        
        String accessToken = jwtService.generateAuthenticationToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        
        UserResponse userResponse = buildUserResponse(user);
        
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .expiresIn(15 * 60) // 15 minutes in seconds
                .user(userResponse)
                .build();
    }

    @Override
    public LoginResponse register(RegisterRequest request) {
        log.info("User registration attempt: {}", request.getUsername());
        
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResourceAlreadyExistException("Email already exists");
        }
        
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistException("Username already exists");
        }
        
        User user;
        try {
            user = userRepository.save(
                    User.builder()
                            .username(request.getUsername())
                            .password_hash(passwordEncoder.encode(request.getPassword()))
                            .email(request.getEmail())
                            .role(userRepository.findUserByRole(Role.ADMIN).isPresent() 
                                    ? Role.USER : Role.ADMIN)
                            .full_name(request.getFullName())
                            .is_active(true)
                            .authType(AuthenticationType.LOCAL)
                            .updated_at(LocalDateTime.now())
                            .created_at(LocalDateTime.now())
                            .build()
            );
        } catch (DataAccessException e) {
            log.error("Failed to save user to database", e);
            throw new IllegalStateException("Failed to save user to the database.", e);
        }
        
        log.info("User registered successfully: {}", user.getId());
        
        String accessToken = jwtService.generateAuthenticationToken(user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);
        
        UserResponse userResponse = buildUserResponse(user);
        
        return LoginResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .expiresIn(15 * 60) // 15 minutes in seconds
                .user(userResponse)
                .build();
    }

    @Override
    public LoginResponse refreshToken(String refreshTokenValue) {
        log.debug("Refresh token attempt");
        
        RefreshToken refreshToken = refreshTokenService.validateRefreshToken(refreshTokenValue);
        User user = refreshToken.getUser();
        
        refreshTokenService.updateLastUsed(refreshTokenValue);
        
        String newAccessToken = jwtService.generateAuthenticationToken(user);
        
        RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(user);
        
        UserResponse userResponse = buildUserResponse(user);
        
        log.info("Token refreshed successfully for user: {}", user.getId());
        
        return LoginResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken.getToken())
                .expiresIn(15 * 60) // 15 minutes in seconds
                .user(userResponse)
                .build();
    }

    @Override
    public void logout(String refreshTokenValue) {
        log.info("User logout attempt");
        
        try {
            refreshTokenService.deleteRefreshToken(refreshTokenValue);
            log.info("User logged out successfully");
        } catch (ResourceNotFoundException e) {
            log.warn("Refresh token not found during logout");
        }
    }

    private UserResponse buildUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFull_name())
                .role(user.getRole())
                .avatarUrl(user.getAvatar_url())
                .isActive(user.getIs_active())
                .build();
    }
}
