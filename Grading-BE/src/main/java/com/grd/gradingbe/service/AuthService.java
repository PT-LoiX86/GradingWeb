package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.LoginRequest;
import com.grd.gradingbe.dto.request.RegisterRequest;

import java.util.Map;

public interface AuthService
{
    Map<String, String> login(LoginRequest request);
    Map<String, String> register(RegisterRequest request);
    Map<String, String> refreshToken(String refreshToken);
}
