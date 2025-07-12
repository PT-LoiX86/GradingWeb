package com.grd.gradingbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProvinceRequest {
    private String code;
    private String name;
    private String countryCode;
}