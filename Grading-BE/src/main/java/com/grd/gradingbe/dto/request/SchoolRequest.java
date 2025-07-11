package com.grd.gradingbe.dto.request;
import lombok.Data;

@Data
public class SchoolRequest {
    private String code;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private Long provinceId;
}