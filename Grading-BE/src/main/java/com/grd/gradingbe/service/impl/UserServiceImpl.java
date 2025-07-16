package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.request.UpdatePasswordRequest;
import com.grd.gradingbe.dto.request.UpdateUserRequest;
import com.grd.gradingbe.dto.response.UserDataResponse;
import com.grd.gradingbe.exception.ArgumentValidationException;
import com.grd.gradingbe.exception.ResourceManagementException;
import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.UserRepository;
import com.grd.gradingbe.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDataResponse getUserData(Integer userId) {
        log.info("Fetching user data for userId: {}", userId);
        return getUserDataFromCache(userId);
    }

    @Override
    public Map<String, String> changePassword(Integer userId, UpdatePasswordRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        log.info("Changing password for userId: {}", userId);

        evictUserCache(userId);

        if (!user.getUsername().equals(request.getUsername())) {
            throw new ArgumentValidationException("Username does not match the authenticated user");
        }

        if (!user.getEmail().equals(request.getEmail())) {
            throw new ArgumentValidationException("Email does not match the authenticated user");
        }

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new ArgumentValidationException("Old password does not match the authenticated user");
        }

        user.setPassword_hash(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdated_at(LocalDateTime.now());

        try {
            userRepository.save(user);
        } catch (DataAccessException e) {
            throw new ResourceManagementException("save()", String.format("User with username: %d", userId), "Failed to update user");
        }

        return Map.of("message", "Success");
    }

    @Override
    public UserDataResponse updateUserInfo(Integer userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId.toString()));

        log.info("Updating user info for userId: {}", userId);
        evictUserCache(userId);

        try {
            Optional.ofNullable(request.getFullName()).ifPresent(user::setFull_name);
            Optional.ofNullable(request.getPhone()).ifPresent(user::setPhone);
            Optional.ofNullable(request.getAvatarUrl()).ifPresent(user::setAvatar_url);
            user.setUpdated_at(LocalDateTime.now());

            userRepository.save(user);

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
        } catch (Exception e) {
            throw new ResourceManagementException("save()", "User", "Failed to update user info");
        }
    }

    @Cacheable(value = "user-profiles", key = "#userId")
    public UserDataResponse getUserDataFromCache(Integer userId) {
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

    @CacheEvict(value = "user-profiles", key = "#userId")
    public void evictUserCache(Integer userId) {
        log.debug("Evicting cache for userId: {}", userId);
    }

}