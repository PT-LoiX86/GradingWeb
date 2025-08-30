package com.grd.gradingbe.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateUserRequest
{
    @Size(min = 3, max = 50, message = "Full name must be between 3 and 50 characters")
    private String fullName;

    @Size(min = 10, max = 10, message = "Wrong phone number format")
    @Pattern(regexp = "^[0-9]+$", message = "Wrong phone number format")
    private String phone;

    //Optional: url pattern
    private String avatarUrl;
}
