package com.grd.gradingbe.dto.request;

import com.grd.gradingbe.dto.enums.ContentType;
import com.grd.gradingbe.dto.enums.ReasonType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequest
{
    @NotNull(message = "Content id is required")
    private Long contentId;

    @NotNull(message = "Content type is required")
    private ContentType contentType;

    @NotNull(message = "Reason is required")
    private ReasonType reason;

    @NotBlank(message = "Description is required")
    private String description;
}
