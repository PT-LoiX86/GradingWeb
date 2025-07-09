package com.grd.gradingbe.dto.response;

import lombok.Data;

@Data
public class ProvinceResponse {
    private Long id;
    private String code;
    private String name;
    private String countryCode;
}