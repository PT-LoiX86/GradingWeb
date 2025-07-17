package com.grd.gradingbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeRecordRequest {
    private Long studentProfileId;
    private Integer semester;
    private Integer grade;
    private Integer year;
    private Boolean isUploaded;
    private String fileUrl;
    private String notes;
}

