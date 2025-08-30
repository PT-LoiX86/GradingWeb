package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.SchoolRequest;
import com.grd.gradingbe.dto.response.SchoolResponse;
import com.grd.gradingbe.mapper.SchoolMapper;
import com.grd.gradingbe.model.Province;
import com.grd.gradingbe.model.School;
import com.grd.gradingbe.repository.ProvinceRepository;
import com.grd.gradingbe.repository.SchoolRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SchoolService {

    private final SchoolRepository schoolRepository;
    private final SchoolMapper schoolMapper;
    private final ProvinceRepository provinceRepository;

    /**
     * Lấy danh sách tất cả trường học
     * Cache với key "all" trong 24 giờ
     */
    @Cacheable(value = "schools", key = "'all'")
    public List<SchoolResponse> getAllSchools() {
        log.info("Fetching all schools from database (cache miss)");
        return schoolRepository.findAll().stream()
                .map(schoolMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy trường học theo ID
     * Cache với key là ID trong 24 giờ
     */
    @Cacheable(value = "schools", key = "#id")
    public Optional<SchoolResponse> getSchoolById(Long id) {
        log.info("Fetching school with id: {} from database (cache miss)", id);
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