package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.response.UserDataResponse;

public interface UserService
{
    UserDataResponse getUserData(String header);
}
