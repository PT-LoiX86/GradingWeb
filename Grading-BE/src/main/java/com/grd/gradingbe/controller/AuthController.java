package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.request.ForgotPasswordRequest;
import com.grd.gradingbe.dto.request.LoginRequest;
import com.grd.gradingbe.dto.request.RegisterRequest;
import com.grd.gradingbe.dto.request.ResetPasswordRequest;
import com.grd.gradingbe.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/auth")
public class AuthController
{
    private final AuthService authService;

    public AuthController(AuthService authService)
    {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login (@Valid @RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping("/register/verify")
    public ResponseEntity<Map<String, String>> verifyRegistration(@RequestParam String token)
    {
        return ResponseEntity.ok(authService.verifyRegistration(token));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@RequestBody Map<String, String> request)
    {
        String refreshToken = request.get("refreshToken");
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword (@Valid @RequestBody ForgotPasswordRequest request)
    {
        return ResponseEntity.ok(authService.forgotPassword(request));
    }

    @PutMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword (@Valid @RequestBody ResetPasswordRequest request)
    {
        return ResponseEntity.ok(authService.resetPassword(request));
    }
}
