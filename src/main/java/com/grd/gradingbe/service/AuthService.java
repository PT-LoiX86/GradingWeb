package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.ForgotPasswordRequest;
import com.grd.gradingbe.dto.request.LoginRequest;
import com.grd.gradingbe.dto.request.RegisterRequest;
import com.grd.gradingbe.dto.request.ResetPasswordRequest;
import com.grd.gradingbe.dto.response.LoginResponse;

import java.util.Map;

public interface AuthService
{
    LoginResponse login(LoginRequest request);

    public Map<String, String> register(RegisterRequest request);
  
    LoginResponse refreshToken(String refreshToken);
  
    void logout(String refreshToken);
    
    Map<String, String> verifyRegistration(String token);

    Map<String, String> resetPassword(ResetPasswordRequest request);

    Map<String, String> forgotPassword(ForgotPasswordRequest request);
}
