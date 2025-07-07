package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.response.UserDataResponse;
import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.JwtService;
import com.grd.gradingbe.service.UserService;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService
{
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, JwtService jwtService)
    {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public UserDataResponse getUserData(String header)
    {
        String token = null;
        if (header != null && header.startsWith("Bearer "))
        {
            token = header.substring(7);
        }

        Integer userId = Integer.parseInt(jwtService.extractClaim(token, Claims::getSubject));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        return (UserDataResponse.builder()
                .username(user.getUsername())
                .full_name(user.getFull_name())
                .role(user.getRole())
                .phone(user.getPhone())
                .email(user.getEmail())
                .avatar_url(user.getAvatar_url())
                .updated_at(user.getUpdated_at())
                .created_at(user.getCreated_at())
                .build());
    }
}
