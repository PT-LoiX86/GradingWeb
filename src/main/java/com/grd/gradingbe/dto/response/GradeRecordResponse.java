package com.grd.gradingbe.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeRecordResponse {
    private Long id;
    @NotNull(message = "Student profile ID is required")
    private Long studentProfileId;
    private Integer semester;
    @NotNull(message = "Grade is required")
    private Integer grade;
    @NotNull(message = "Year is required")
    private Integer year;
    private Boolean isUploaded;
    private String fileUrl;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<SubjectScoreResponse> subjectScores;
}
