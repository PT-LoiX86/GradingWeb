package com.grd.gradingbe.dto.response;

import com.grd.gradingbe.dto.enums.GenderType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class StudentProfileResponse {
    private Long id;
    private String code;
    private String name;
    private LocalDate birthDate;
    private GenderType gender;
    private String address;
    private Long schoolId; // Trả về ID của School
    private Long provinceId; // Trả về ID của Province
    private Integer year;
    private String ethnic;
    private String religion;
}