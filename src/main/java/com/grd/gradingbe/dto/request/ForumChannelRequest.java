package com.grd.gradingbe.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ForumChannelRequest
{
    @Schema(description = "Name of the channel", example = "Q&A")
    @NotBlank(message = "Channel name cannot be blank")
    @Size(min = 1, max = 100, message = "Name must be between 1 and 100 characters")
    private String name;

    @Schema(description = "Description of the category", example = "Question and Answer channel")
    @NotBlank(message = "Description cannot be blank")
    private String description;

    @Schema(description = "Slug of the channel", example = "question-and-answer")
    @NotBlank(message = "Slug cannot be blank")
    @Size(min = 1, max = 50, message = "Slug must be between 1 and 50 characters")
    private String slug;

    @Schema(description = "Is the channel active", example = "true")
    private Boolean isActive;
}
