package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.LoginRequest;
import com.grd.gradingbe.dto.request.RegisterRequest;
import com.grd.gradingbe.dto.response.LoginResponse;

public interface AuthService
{
    LoginResponse login(LoginRequest request);
    LoginResponse register(RegisterRequest request);
    LoginResponse refreshToken(String refreshToken);
    void logout(String refreshToken);
}
