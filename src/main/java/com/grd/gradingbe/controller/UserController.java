package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.request.UpdatePasswordRequest;
import com.grd.gradingbe.dto.request.UpdateUserRequest;
import com.grd.gradingbe.dto.response.UserDataResponse;
import com.grd.gradingbe.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping(path = "api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Management", description = "API endpoints for managing users")
public class UserController
{
    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDataResponse> getUserData(Principal principal)
    {
        return ResponseEntity.ok(userService.getUserData(Integer.parseInt(principal.getName())));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDataResponse> updateUserInfo(Principal principal, @Valid @RequestBody UpdateUserRequest request)
    {
        return ResponseEntity.ok(userService.updateUserInfo(Integer.parseInt(principal.getName()), request));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(Principal principal, @Valid @RequestBody UpdatePasswordRequest request)
    {
        return ResponseEntity.ok(userService.changePassword(Integer.parseInt(principal.getName()), request));
    }
}
