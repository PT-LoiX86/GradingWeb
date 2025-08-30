package com.grd.gradingbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "MajorRequest", description = "Request payload for creating or updating a major")
public class MajorRequest {

    @Schema(description = "Name of the major", example = "Computer Science", required = true)
    @NotBlank(message = "Name cannot be blank")
    @Size(min = 1, max = 50, message = "Name must be between 1 and 50 characters")
    private String name;

    @Schema(description = "Unique code for the major", example = "CS", required = true)
    @NotBlank(message = "Code cannot be blank")
    @Size(min = 1, max = 20, message = "Code must be between 1 and 20 characters")
    private String code;

    @Schema(description = "Description of the major", example = "A comprehensive program in computer science and software engineering")
    @Size(max = 255, message = "Description must not exceed 255 characters")
    private String description;

    @Schema(description = "Duration of the major in years", example = "4", required = true)
    @NotBlank(message = "Duration cannot be blank")
    @Size(min = 1, max = 5, message = "Duration must be between 1 and 5 years")
    private int durationYears;

    @Schema(description = "ID of the university this major belongs to", example = "1", required = true)
    @NotNull(message = "University ID cannot be null")
    private Long universityId;
}
