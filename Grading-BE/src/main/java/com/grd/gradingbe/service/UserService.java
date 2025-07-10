package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.ChangePasswordRequest;
import com.grd.gradingbe.dto.request.UpdateUserInfoRequest;
import com.grd.gradingbe.dto.response.UserDataResponse;

import java.util.Map;

public interface UserService
{
    UserDataResponse getUserData(String header);

    UserDataResponse updateUserInfo (String header, UpdateUserInfoRequest request);

    Map<String, String> changePassword(String header, ChangePasswordRequest request);
}
