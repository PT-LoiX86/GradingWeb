package com.grd.gradingbe.dto.response;

import com.grd.gradingbe.dto.enums.ScoreType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectScoreResponse {
    private Long id;

    @NotNull(message = "Grade record ID is required")
    private Long gradeRecordId;

    @NotNull(message = "Subject ID is required")
    private Long subjectId;

    @NotNull(message = "Score is required")
    private BigDecimal score;

    @NotNull(message = "Score type is required")
    private ScoreType scoreType;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private SubjectResponse subject;
}
