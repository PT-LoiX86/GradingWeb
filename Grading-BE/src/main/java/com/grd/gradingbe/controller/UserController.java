package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.response.UserDataResponse;
import com.grd.gradingbe.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/users")
public class UserController
{
    private final UserService userService;

    public UserController(UserService userService)
    {
        this.userService = userService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserDataResponse> getUserData(@RequestHeader("Authorization") String header)
    {
        return ResponseEntity.ok(userService.getUserData(header));
    }
}
