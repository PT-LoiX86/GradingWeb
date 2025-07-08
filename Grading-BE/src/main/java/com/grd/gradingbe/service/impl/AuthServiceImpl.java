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
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.AuthService;
import com.grd.gradingbe.service.JwtService;
import com.grd.gradingbe.service.MailService;
import io.jsonwebtoken.Claims;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.mail.MailSendException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class AuthServiceImpl implements AuthService
{
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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

    public Map<String, String> login(LoginRequest request)
    {
        Authentication authentication;

        try
        {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        }
        catch (Exception e)
        {
            throw new BadCredentialsException("Username or password is incorrect");
        }

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
        if (userRepository.findByUsername(request.getUsername()).isPresent())
        {
            throw new ResourceAlreadyExistException("Username already exist");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent())
        {
            throw new ResourceAlreadyExistException("Email already exist");
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

        String accessToken = jwtService.generateAuthenticationToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        return Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );
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

    public Map<String, String> refreshToken(String refreshToken) {
        if (refreshToken == null || !jwtService.validateToken(refreshToken) || jwtService.isTokenExpired(TokenType.REFRESH, refreshToken)) {
            throw new JwtManagementException(TokenType.REFRESH, "Authenticate token", "Invalid or expired refresh token");
        }

        Integer userId = Integer.parseInt(jwtService.extractClaim(TokenType.REFRESH, refreshToken , Claims::getSubject));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        String newAccessToken = jwtService.generateAuthenticationToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return Map.of(
                "accessToken", newAccessToken,
                "refreshToken", newRefreshToken
        );
    }

    public Boolean validatePayloadToken(String token)
    {
        return (jwtService.validateToken(token)
                && !(token == null)
                && !jwtService.isTokenExpired(TokenType.PAYLOAD, token)
                && "payload".equals(jwtService.extractHeader(TokenType.PAYLOAD, token).get("typ")));
    }
}
