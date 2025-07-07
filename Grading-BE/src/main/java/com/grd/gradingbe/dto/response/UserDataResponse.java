package com.grd.gradingbe.dto.response;

import com.grd.gradingbe.enums.AuthenticationType;
import com.grd.gradingbe.enums.Role;
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

    private String full_name;

    private String phone;

    private String avatar_url;

    private AuthenticationType authType;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;
}
