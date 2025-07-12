package com.grd.gradingbe.dto.request;

import com.grd.gradingbe.dto.enums.ScoreType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectScoreUpdateRequest {
    private Long gradeRecordId;
    private Long subjectId;
    private BigDecimal score;
    private ScoreType scoreType;
}