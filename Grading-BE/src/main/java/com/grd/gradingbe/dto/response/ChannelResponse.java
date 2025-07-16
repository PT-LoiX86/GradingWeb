package com.grd.gradingbe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "ChannelResponse", description = "Response payload containing channel information")
public record ChannelResponse(
        @Schema(description = "Unique identifier of the channel", example = "1")
        Long id,

        @Schema(description = "Name of the channel", example = "Q&A")
        String name,

        @Schema(description = "Description of the channel", example = "Quest and Answer about how to be rich")
        String description,

        @Schema(description = "Slug of the channel", example = "question-and-answer")
        String slug,

        @Schema(description = "Is the channel active", example = "true")
        Boolean isActive,

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
