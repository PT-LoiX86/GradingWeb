package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.request.ChangePasswordRequest;
import com.grd.gradingbe.dto.response.UserDataResponse;
import com.grd.gradingbe.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "api/users")
public class UserController
{
    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDataResponse> getUserData(@RequestHeader("Authorization") String header)
    {
        return ResponseEntity.ok(userService.getUserData(header));
    }

    @PutMapping("/change-password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestHeader("Authorization") String header,
                                                              @Valid @RequestBody ChangePasswordRequest request)
    {
        return ResponseEntity.ok(userService.changePassword(header, request));
    }
}
