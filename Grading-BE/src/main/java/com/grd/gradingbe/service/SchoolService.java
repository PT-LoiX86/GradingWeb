package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.SchoolRequest;
import com.grd.gradingbe.dto.response.SchoolResponse;
import com.grd.gradingbe.mapper.SchoolMapper;
import com.grd.gradingbe.model.Province;
import com.grd.gradingbe.model.School;
import com.grd.gradingbe.repository.ProvinceRepository;
import com.grd.gradingbe.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final SchoolMapper schoolMapper;
    private final ProvinceRepository provinceRepository;

    public List<SchoolResponse> getAllSchools() {
        return schoolRepository.findAll().stream()
                .map(schoolMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<SchoolResponse> getSchoolById(Long id) {
        return schoolRepository.findById(id)
                .map(schoolMapper::toResponse);
    }

    public SchoolResponse createSchool(SchoolRequest schoolRequest) {
        if (schoolRequest.getCode() == null || schoolRequest.getName() == null || schoolRequest.getProvinceId() == null) {
            throw new IllegalArgumentException("Code, name, and provinceId are required");
        }

        Province province = provinceRepository.findById(schoolRequest.getProvinceId())
                .orElseThrow(() -> new IllegalArgumentException("Province not found"));

        School school = schoolMapper.toEntity(schoolRequest); // Sử dụng mapper
        school.setProvince(province); // Gán province thủ công sau khi mapper

        School savedSchool = schoolRepository.save(school);
        return schoolMapper.toResponse(savedSchool);
    }

    public void deleteSchool(Long id) {
        if (!schoolRepository.existsById(id)) {
            throw new IllegalArgumentException("School not found with id: " + id);
        }
        schoolRepository.deleteById(id);
    }
}