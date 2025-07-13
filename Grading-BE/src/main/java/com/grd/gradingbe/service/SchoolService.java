package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.response.SchoolResponse;
import com.grd.gradingbe.mapper.SchoolMapper;
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
}