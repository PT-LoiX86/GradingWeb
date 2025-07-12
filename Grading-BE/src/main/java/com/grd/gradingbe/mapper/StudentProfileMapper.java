package com.grd.gradingbe.mapper;

import com.grd.gradingbe.dto.request.StudentProfileRequest;
import com.grd.gradingbe.dto.response.StudentProfileResponse;
import com.grd.gradingbe.model.Province;
import com.grd.gradingbe.model.School;
import com.grd.gradingbe.model.StudentProfile;
import com.grd.gradingbe.model.User;
import com.grd.gradingbe.repository.ProvinceRepository;
import com.grd.gradingbe.repository.SchoolRepository;
import com.grd.gradingbe.repository.UserRepository;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class StudentProfileMapper {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected SchoolRepository schoolRepository;

    @Autowired
    protected ProvinceRepository provinceRepository;
//ánh xạ
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", source = "userId", qualifiedByName = "mapUserIdToUser")
    @Mapping(target = "school", source = "schoolId", qualifiedByName = "mapSchoolIdToSchool")
    @Mapping(target = "province", source = "provinceId", qualifiedByName = "mapProvinceIdToProvince")
    public abstract StudentProfile toEntity(StudentProfileRequest request);

    @Mapping(target = "name", source = "user.username")//để ý thằng này sẽ láy name ở bảng user nên ánh xạ với thằng user.username
    @Mapping(target = "schoolId", source = "school.id")
    @Mapping(target = "provinceId", source = "province.id")
    public abstract StudentProfileResponse toResponse(StudentProfile profile);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "user", ignore = true) // User không thay đổi khi update
    @Mapping(target = "school", source = "schoolId", qualifiedByName = "mapSchoolIdToSchool")
    @Mapping(target = "province", source = "provinceId", qualifiedByName = "mapProvinceIdToProvince")
    public abstract void updateEntityFromRequest(StudentProfileRequest request, @MappingTarget StudentProfile profile);

    @Named("mapUserIdToUser")
    protected User mapUserIdToUser(Integer userId) {
        if (userId == null) return null;
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
    }

    @Named("mapSchoolIdToSchool")
    protected School mapSchoolIdToSchool(Long schoolId) {
        if (schoolId == null) return null;
        return schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("School not found with id: " + schoolId));
    }

    @Named("mapProvinceIdToProvince")
    protected Province mapProvinceIdToProvince(Long provinceId) {
        if (provinceId == null) return null;
        return provinceRepository.findById(provinceId)
                .orElseThrow(() -> new IllegalArgumentException("Province not found with id: " + provinceId));
    }
}