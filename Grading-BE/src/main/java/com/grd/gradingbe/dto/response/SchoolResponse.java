package com.grd.gradingbe.dto.response;
import lombok.Data;

@Data
public class SchoolResponse {
    private Long id;
    private String code;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private Long provinceId;
}