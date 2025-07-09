package com.grd.gradingbe.mapper;

import com.grd.gradingbe.dto.request.SchoolRequest;
import com.grd.gradingbe.dto.response.SchoolResponse;
import com.grd.gradingbe.model.School;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = ProvinceMapper.class)
public abstract class SchoolMapper {

    @Autowired
    private ProvinceMapper provinceMapper;

    public School toEntity(SchoolRequest schoolRequest) {
        if (schoolRequest == null) {
            return null;
        }

        return School.builder()
                .code(schoolRequest.getCode())
                .name(schoolRequest.getName())
                .address(schoolRequest.getAddress())
                .phoneNumber(schoolRequest.getPhoneNumber())
                .email(schoolRequest.getEmail())
                .province(provinceMapper.toEntity(schoolRequest.getProvinceId()))
                .build();
    }


    @Mapping(target = "provinceId", source = "province.id")
    public abstract SchoolResponse toResponse(School school);

    public void updateEntityFromRequest(SchoolRequest schoolRequest, School school) {
        if (schoolRequest == null || school == null) {
            return;
        }

        if (schoolRequest.getCode() != null) {
            school.setCode(schoolRequest.getCode());
        }
        if (schoolRequest.getName() != null) {
            school.setName(schoolRequest.getName());
        }
        if (schoolRequest.getAddress() != null) {
            school.setAddress(schoolRequest.getAddress());
        }
        if (schoolRequest.getPhoneNumber() != null) {
            school.setPhoneNumber(schoolRequest.getPhoneNumber());
        }
        if (schoolRequest.getEmail() != null) {
            school.setEmail(schoolRequest.getEmail());
        }

        school.setProvince(provinceMapper.toEntity(schoolRequest.getProvinceId()));
    }

}