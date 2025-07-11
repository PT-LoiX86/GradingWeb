package com.grd.gradingbe.dto.request;

import com.grd.gradingbe.dto.enums.GenderType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentProfileRequest {
    private String code;
    private String name;
    private LocalDate birthDate;
    private GenderType gender;
    private String address;
    private Long schoolId; // Tham chiếu đến School
    private Long provinceId; // Tham chiếu đến Province
    private Integer year;
    private String ethnic;
    private String religion;
}