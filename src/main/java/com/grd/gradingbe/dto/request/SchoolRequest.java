package com.grd.gradingbe.dto.request;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SchoolRequest {
    private String code;
    private String name;
    private String address;
    private String phoneNumber;
    private String email;
    private Long provinceId;
}