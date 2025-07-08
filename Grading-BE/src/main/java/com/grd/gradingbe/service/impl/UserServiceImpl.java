package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.request.ChangePasswordRequest;
import com.grd.gradingbe.dto.response.UserDataResponse;
import com.grd.gradingbe.enums.TokenType;
import com.grd.gradingbe.exception.ArgumentValidationException;
import com.grd.gradingbe.exception.ResourceManagementException;
import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.JwtService;
import com.grd.gradingbe.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDataResponse getUserData(String header)
    {
        String token = extractToken(header);

        Integer userId = Integer.parseInt(jwtService.extractClaim(TokenType.ACCESS, token, Claims::getSubject));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        return (UserDataResponse.builder()
                .username(user.getUsername())
                .fullName(user.getFull_name())
                .role(user.getRole())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatarUrl(user.getAvatar_url())
                .authType(user.getAuthType())
                .updatedAt(user.getUpdated_at())
                .createdAt(user.getCreated_at())
                .verified(user.getVerified())
                .isActive(user.getIs_active())
                .build());
    }

    public Map<String, String> changePassword(String header, ChangePasswordRequest request)
    {
        String token = extractToken(header);

        Integer userId = Integer.parseInt(jwtService.extractClaim(TokenType.ACCESS, token, Claims::getSubject));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        if (!user.getUsername().equals(request.getUsername()))
        {
            throw new ArgumentValidationException ("Username does not match the authenticated user");
        }

        if (!user.getEmail().equals(request.getEmail()))
        {
            throw new ArgumentValidationException ("Email does not match the authenticated user");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
        {
            throw new ArgumentValidationException("Old password does not match the authenticated user");
        }

        user.setPassword_hash(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdated_at(LocalDateTime.now());

        try
        {
            userRepository.save(user);
        }
        catch (DataAccessException e)
        {
            throw new ResourceManagementException("save()", String.format("User with id: %d", userId), "Failed to update user");
        }

        return Map.of("message", "Success");
    }

    private String extractToken(String header)
    {
        String token = null;
        if (header != null && header.startsWith("Bearer "))
        {
            token = header.substring(7);
        }
        return token;
    }
}
