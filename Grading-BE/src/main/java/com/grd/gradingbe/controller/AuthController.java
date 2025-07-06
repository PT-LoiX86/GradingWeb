package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.request.LoginRequest;
import com.grd.gradingbe.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/auth")
public class AuthController
{
    private final AuthService authService;

    public AuthController(AuthService authService)
    {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@RequestBody LoginRequest request)
    {
        return ResponseEntity.ok(authService.login(request));
    }
}
