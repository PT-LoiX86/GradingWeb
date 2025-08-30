package com.grd.gradingbe.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "UniversityResponse", description = "Response payload containing university information")
public record UniversityResponse (
        @Schema(description = "Unique identifier of the university", example = "1")
        Long id,
        
        @Schema(description = "Name of the university", example = "Vietnam-Korea University of Information and Communication Technology")
        String name,
        
        @Schema(description = "Unique code for the university", example = "VKU")
        String code,
        
        @Schema(description = "Physical address of the university", example = "470 Tran Dai Nghia, Hoa Quy, Ngu Hanh Son, Da Nang")
        String address,
        
        @Schema(description = "University website URL", example = "https://vku.udn.vn")
        String website,
        
        @Schema(description = "Type of university", example = "Public")
        String typeUniversity,
        
        @Schema(description = "Contact phone number", example = "+84236123456")
        String phoneNumber,
        
        @Schema(description = "Contact email address", example = "info@vku.udn.vn")
        String email,
        
        @Schema(description = "Description of the university", example = "A leading university in information and communication technology")
        String description,
        
        @Schema(description = "URL to the university logo", example = "https://vku.udn.vn/logo.png")
        String logoUrl,
        
        @Schema(description = "Creation timestamp", example = "2024-01-01T10:00:00")
        String createdAt,
        
        @Schema(description = "User who created this university", example = "admin")
        String createdBy,
        
        @Schema(description = "Last update timestamp", example = "2024-01-01T10:00:00")
        String updatedAt,
        
        @Schema(description = "User who last updated this university", example = "admin")
        String updatedBy
) {
}
