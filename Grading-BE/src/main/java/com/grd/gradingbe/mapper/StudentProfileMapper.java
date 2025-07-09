package com.grd.gradingbe.mapper;

import com.grd.gradingbe.dto.request.ProvinceRequest;
import com.grd.gradingbe.dto.request.SchoolRequest;
import com.grd.gradingbe.dto.request.StudentProfileRequest;
import com.grd.gradingbe.dto.response.StudentProfileResponse;
import com.grd.gradingbe.model.Province;
import com.grd.gradingbe.model.School;
import com.grd.gradingbe.model.StudentProfile;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, uses = {SchoolMapper.class, ProvinceMapper.class})
public abstract class StudentProfileMapper {

    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private ProvinceMapper provinceMapper;

    @Mapping(target = "school", source = "schoolId", qualifiedByName = "mapSchoolIdToSchool")
    @Mapping(target = "province", source = "provinceId", qualifiedByName = "mapProvinceIdToProvince")
    public abstract StudentProfile toEntity(StudentProfileRequest studentProfileRequest);

    @Mapping(target = "schoolId", source = "school.id")
    @Mapping(target = "provinceId", source = "province.id")
    public abstract StudentProfileResponse toResponse(StudentProfile studentProfile);

    @Mapping(target = "school", source = "schoolId", qualifiedByName = "mapSchoolIdToSchool")
    @Mapping(target = "province", source = "provinceId", qualifiedByName = "mapProvinceIdToProvince")
    public abstract void updateEntityFromRequest(StudentProfileRequest studentProfileRequest, @MappingTarget StudentProfile studentProfile);

    @Named("mapSchoolIdToSchool")
    School mapSchoolIdToSchool(Long schoolId) {
        if (schoolId == null) {
            return null;
        }
        // Tạo một SchoolRequest tạm thời với trường tối thiểu (không cần id)
        SchoolRequest schoolRequest = new SchoolRequest();
        // Vì không có id, chỉ tạo một đối tượng rỗng và để service/repository xử lý sau
        return schoolMapper.toEntity(schoolRequest); // Cần điều chỉnh logic ở service
    }

    @Named("mapProvinceIdToProvince")
    Province mapProvinceIdToProvince(Long provinceId) {
        if (provinceId == null) {
            return null;
        }
        ProvinceRequest provinceRequest = new ProvinceRequest();
        return provinceMapper.toEntity(provinceRequest);
    }
}