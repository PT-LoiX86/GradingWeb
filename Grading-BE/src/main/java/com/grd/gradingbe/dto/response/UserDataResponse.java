package com.grd.gradingbe.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime created_at;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime updated_at;
}
