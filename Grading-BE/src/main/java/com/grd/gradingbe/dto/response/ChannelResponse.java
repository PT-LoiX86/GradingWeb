package com.grd.gradingbe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "CategoryResponse", description = "Response payload containing category information")
public record ChannelResponse(
        @Schema(description = "Unique identifier of the category", example = "1")
        Long id,

        @Schema(description = "Name of the category", example = "Q&A")
        String name,

        @Schema(description = "Description of the category", example = "Quest and Answer about how to be rich")
        String description,

        @Schema(description = "Slug of the category", example = "question-and-answer")
        String slug,

        @Schema(description = "Is the channel active", example = "true")
        Boolean is_active,

        @Schema(description = "Creation timestamp", example = "2024-01-01T10:00:00")
        String createdAt,

        @Schema(description = "User who created this channel", example = "admin")
        String createdBy,

        @Schema(description = "Last update timestamp", example = "2024-01-01T10:00:00")
        String updatedAt,

        @Schema(description = "User who last updated this channel", example = "admin")
        String updatedBy
)
{ }
