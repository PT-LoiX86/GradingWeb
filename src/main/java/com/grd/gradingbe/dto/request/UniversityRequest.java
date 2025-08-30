package com.grd.gradingbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
@Schema(name = "UniversityRequest", description = "Request payload for creating or updating a university")
public class UniversityRequest {

    @Schema(description = "Name of the university", example = "Vietnam-Korea University of Information and Communication Technology", required = true)
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @Schema(description = "Unique code for the university", example = "VKU", required = true)
    @NotBlank(message = "Code cannot be blank")
    @Size(min = 1, max = 20, message = "Code must be between 1 and 20 characters")
    private String code;

    @Schema(description = "Physical address of the university", example = "470 Tran Dai Nghia, Hoa Quy, Ngu Hanh Son, Da Nang")
    @Size(max = 255, message = "Address must not exceed 255 characters")
    private String address;

    @Schema(description = "Contact phone number", example = "+84236123456")
    @Size(min = 1, max = 15, message = "Phone number must be between 1 and 15 characters")
    @Pattern(regexp = "^\\+?[0-9]*$", message = "Phone number must be a valid format")
    private String phoneNumber;

    @Schema(description = "Contact email address", example = "info@vku.udn.vn")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Email must be a valid format")
    private String email;

    @Schema(description = "University website URL", example = "https://vku.udn.vn")
    @Size(max = 100, message = "Website must not exceed 100 characters")
    private String website;

    @Schema(description = "URL to the university logo", example = "https://vku.udn.vn/logo.png")
    private String logoUrl;

    @Schema(description = "Description of the university", example = "A leading university in information and communication technology")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @Schema(description = "Type of university (Public/Private)", example = "Public", required = true)
    @NotBlank(message = "Type of university cannot be blank")
    private String typeUniversity;
}
