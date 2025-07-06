package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.LoginRequest;

import java.util.Map;

public interface AuthService
{
    Map<String,String> login(LoginRequest request);
}
