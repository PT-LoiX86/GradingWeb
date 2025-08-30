package com.grd.gradingbe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "MajorResponse", description = "Response payload containing major information")
public record MajorResponse(
        @Schema(description = "Unique identifier of the major", example = "1")
        Long id,
        
        @Schema(description = "Name of the major", example = "Computer Science")
        String name,
        
        @Schema(description = "Unique code for the major", example = "CS")
        String code,
        
        @Schema(description = "Description of the major", example = "A comprehensive program in computer science and software engineering")
        String description,
        
        @Schema(description = "Duration of the major in years", example = "4")
        int durationYears,
        
        @Schema(description = "ID of the university this major belongs to", example = "1")
        Long universityId,
        
        @Schema(description = "Creation timestamp", example = "2024-01-01T10:00:00")
        String createdAt,
        
        @Schema(description = "User who created this major", example = "admin")
        String createdBy,
        
        @Schema(description = "Last update timestamp", example = "2024-01-01T10:00:00")
        String updatedAt,
        
        @Schema(description = "User who last updated this major", example = "admin")
        String updatedBy
) {
}
