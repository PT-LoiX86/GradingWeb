package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.request.RefreshTokenRequest;
import com.grd.gradingbe.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "/api/oauth2")
@Tag(name = "OAuth2", description = "OAuth2 related endpoints")
@RequiredArgsConstructor
public class OAuth2Controller {

    @Value("${env.app.backend.oauth2-login-url}")
    private String oauth2LoginUrl;
    
    private final AuthService authService;
    
    @GetMapping("/login-url")
    @Operation(summary = "Get OAuth2 login URL", description = "Get the OAuth2 login URL for Google authentication")
    @ApiResponse(responseCode = "200", description = "OAuth2 login URL retrieved successfully")
    public ResponseEntity<Map<String, String>> getOAuth2LoginUrl() {
        return ResponseEntity.ok(Map.of(
            "loginUrl", oauth2LoginUrl,
            "provider", "google"
        ));
    }

    @GetMapping("/callback/status")
    @Operation(summary = "OAuth2 callback status", description = "Check OAuth2 callback status")
    @ApiResponse(responseCode = "200", description = "Callback status retrieved successfully")
    public ResponseEntity<Map<String, String>> callbackStatus(
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String accessToken,
            @RequestParam(required = false) String refreshToken) {
        
        if (error != null) {
            return ResponseEntity.badRequest().body(Map.of(
                "status", "error",
                "message", error
            ));
        }
        
        if (accessToken != null && refreshToken != null) {
            return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "OAuth2 authentication successful"
            ));
        }
        
        return ResponseEntity.ok(Map.of(
            "status", "pending",
            "message", "OAuth2 authentication in progress"
        ));
    }

    @PostMapping("/logout")
    @Operation(summary = "OAuth2 logout", description = "Logout OAuth2 user and revoke refresh token")
    @ApiResponse(responseCode = "200", description = "Logout successful")
    public ResponseEntity<Map<String, String>> oauth2Logout(@Valid @RequestBody RefreshTokenRequest request) {
        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok(Map.of("message", "OAuth2 logout successful"));
    }
}
