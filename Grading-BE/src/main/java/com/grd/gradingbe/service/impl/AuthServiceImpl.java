package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.request.ForgotPasswordRequest;
import com.grd.gradingbe.dto.request.LoginRequest;
import com.grd.gradingbe.dto.request.RegisterRequest;
import com.grd.gradingbe.dto.request.ResetPasswordRequest;
import com.grd.gradingbe.enums.AuthenticationType;
import com.grd.gradingbe.enums.MailType;
import com.grd.gradingbe.enums.Role;
import com.grd.gradingbe.enums.TokenType;
import com.grd.gradingbe.exception.*;
import com.grd.gradingbe.dto.response.LoginResponse;
import com.grd.gradingbe.dto.response.UserResponse;
import com.grd.gradingbe.dto.enums.AuthenticationType;
import com.grd.gradingbe.dto.enums.Role;
import com.grd.gradingbe.exception.ResourceAlreadyExistException;
import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.model.RefreshToken;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.AuthService;
import com.grd.gradingbe.service.JwtService;
import com.grd.gradingbe.service.MailService;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import com.grd.gradingbe.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

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

    private final MailService mailService;

    @Value("${env.app.backend.base-url}")
    private String backendUrl;
    @Value("${env.app.front-end.reset-password}")
    private String resetPasswordFrontendURL;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtService jwtService, UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           MailService mailService)
    {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

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

        try
        {
            User user = userRepository.save(
                User.builder()
                        .username(request.getUsername())
                        .password_hash(passwordEncoder.encode(request.getPassword()))
                        .email(request.getEmail())
                        .role(Role.USER)
                        .full_name(request.getFullName())
                        .updated_at(LocalDateTime.now())
                        .created_at(LocalDateTime.now())
                        .authType(AuthenticationType.LOCAL)
                        .verified(false)
                        .is_active(false)
                        .build());

            String verifyToken = jwtService.generatePayloadToken(user, Map.of("email", user.getEmail()), 1, ChronoUnit.HOURS);
            String verifyLink = String.format("%s/api/auth/register/verify?token=%s", backendUrl, verifyToken);

            mailService.sendLinkEmail(
                    MailType.REGISTRATION,
                    user.getEmail(),
                    verifyLink
            );
        }
        catch (DataAccessException | MessagingException e)
        {
            if (e instanceof DataAccessException) {
                throw new ResourceManagementException("save()", "New registered user", "Failed to save user to the database");
            }
            else {
                throw new MailSendException("Failed to sent registration email");
            }
        }

        return Map.of("message", "Email sent");
    }

    public Map<String, String> verifyRegistration(String token)
    {
        if(!validatePayloadToken(token))
        {
            throw new JwtManagementException(TokenType.PAYLOAD, "Validate token", "Invalid or expired verification token");
        }

        Integer userId = Integer.parseInt(jwtService.extractClaim(TokenType.PAYLOAD, token, Claims::getSubject));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId.toString()));

        user.setVerified(true);
        user.setIs_active(true);
        user.setUpdated_at(LocalDateTime.now());
        userRepository.save(user);
        
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

    public Map<String, String> forgotPassword(ForgotPasswordRequest request)
    {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ArgumentValidationException("Username or email is invalid"));

        if (!user.getEmail().equals(request.getEmail()))
        {
            throw new ArgumentValidationException("Username or email is invalid");
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("purpose", "reset-password");

        String verifyToken = jwtService.generatePayloadToken(user, claims,15, ChronoUnit.MINUTES);

        try
        {
            String verifyLink = String.format("%s?token=%s", resetPasswordFrontendURL, verifyToken);

            mailService.sendLinkEmail(
                    MailType.CHANGE_PASSWORD,
                    user.getEmail(),
                    verifyLink
            );
        }
        catch (MessagingException e)
        {
            throw new MailSendException("Failed to sent registration email");
        }

        return Map.of("message", "Email sent");
    }

    public Map<String, String> resetPassword(ResetPasswordRequest request)
    {
        String token = request.getToken();

        if (!validatePayloadToken(token))
        {
            throw new ArgumentValidationException("Token is not valid");
        }

        Claims claims = jwtService.extractClaim(TokenType.PAYLOAD, token, Function.identity());

        if (!claims.get("purpose").equals("reset-password"))
        {
            System.out.println("hehe");
            throw new ArgumentValidationException("Token is not valid");
        }

        User user = userRepository.findById(Integer.parseInt(jwtService.extractClaim(TokenType.PAYLOAD, token, Claims::getSubject)))
                .orElseThrow(() -> new ArgumentValidationException("Failed to reset password"));

        user.setPassword_hash(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdated_at(LocalDateTime.now());

        try
        {
            userRepository.save(user);
        }
        catch (DataAccessException e)
        {
            throw new ResourceManagementException("save()", String.format("User with id: %d", user.getId()), "Failed to update user");
        }

        return Map.of("message","Success");
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

    public Boolean validatePayloadToken(String token)
    {
        return (jwtService.validateToken(token)
                && !(token == null)
                && !jwtService.isTokenExpired(TokenType.PAYLOAD, token)
                && "payload".equals(jwtService.extractHeader(TokenType.PAYLOAD, token).get("typ")));
    }
}
