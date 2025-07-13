package com.grd.gradingbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class UpdateChannelRequest
{
    @Schema(description = "Name of the category", example = "Q&A")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @Schema(description = "Description of the category", example = "Question and Answer channel")
    private String description;

    @Schema(description = "Slug of the category", example = "question-and-answer")
    @Size(min = 1, max = 50, message = "Slug must be between 1 and 50 characters")
    private String slug;

    @Schema(description = "Is the channel active", example = "true")
    @Pattern(regexp = "true|false", message = "Value must be 'true' or 'false'")
    private Boolean is_active;
}
