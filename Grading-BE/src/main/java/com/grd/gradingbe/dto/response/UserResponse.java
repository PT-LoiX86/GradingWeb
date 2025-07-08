package com.grd.gradingbe.dto.response;

import com.grd.gradingbe.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Integer id;
    private String username;
    private String email;
    private String fullName;
    private Role role;
    private String avatarUrl;
    private Boolean isActive;
}
