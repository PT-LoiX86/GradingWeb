package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.UpdatePasswordRequest;
import com.grd.gradingbe.dto.request.UpdateUserRequest;
import com.grd.gradingbe.dto.response.UserDataResponse;

import java.util.Map;

public interface UserService
{
    UserDataResponse getUserData(Integer userId);

    UserDataResponse updateUserInfo (Integer userId, UpdateUserRequest request);

    Map<String, String> changePassword(Integer userId, UpdatePasswordRequest request);
}
