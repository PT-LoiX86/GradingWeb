package com.grd.gradingbe.dto.request;

import com.grd.gradingbe.dto.enums.GenderType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentProfileRequest {
//    private String name;
    private LocalDate dateOfBirth;
    private GenderType gender;
    private String address;
    private Long schoolId;
    private Long provinceId;
    private Integer graduationYear;
    private String ethnic;
    private String religion;
    private Integer userId;
}