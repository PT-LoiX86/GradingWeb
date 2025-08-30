package com.grd.gradingbe.dto.response;

import com.grd.gradingbe.dto.enums.ContentType;
import com.grd.gradingbe.dto.enums.ReasonType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(name = "ReportResponse", description = "Response payload containing all reports")
public record ReportResponse(
        @Schema(description = "Unique identifier of the report", example = "1")
        Long id,

        @Schema(description = "Unique identifier of the reported content id", example = "1")
        Long contentId,

        @Schema(description = "Type of the reported content", example = "post")
        ContentType contentType,

        @Schema(description = "Unique identifier of the post", example = "1")
        ReasonType reason,

        @Schema(description = "Unique identifier of the post", example = "1")
        String description
)
{ }
