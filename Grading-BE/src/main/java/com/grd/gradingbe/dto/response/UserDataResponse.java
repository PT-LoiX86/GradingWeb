package com.grd.gradingbe.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.grd.gradingbe.dto.enums.AuthenticationType;
import com.grd.gradingbe.dto.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDataResponse
{
    private String username;

    private String email;

    private Role role;

    private String fullName;

    private String phone;

    private String avatarUrl;

    private AuthenticationType authType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updatedAt;

    private Boolean verified;

    private Boolean isActive;
}
