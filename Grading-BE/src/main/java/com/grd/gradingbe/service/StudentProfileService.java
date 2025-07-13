package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.response.StudentProfileResponse;
import com.grd.gradingbe.mapper.StudentProfileMapper;
import com.grd.gradingbe.repository.StudentProfileRepository;
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
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final StudentProfileMapper studentProfileMapper;

    /**
     * Lấy danh sách tất cả student profiles
     * Cache với key "all" trong 30 phút
     * TODO: Nên implement pagination cho method này
     */
    @Cacheable(value = "student-profiles", key = "'all'")
    public List<StudentProfileResponse> getAllStudentProfiles() {
        log.info("Fetching all student profiles from database (cache miss)");
        return studentProfileRepository.findAll().stream()
                .map(studentProfileMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy student profile theo ID
     * Cache với key là ID trong 30 phút
     */
    @Cacheable(value = "student-profiles", key = "#id")
    public Optional<StudentProfileResponse> getStudentProfileById(Long id) {
        log.info("Fetching student profile with id: {} from database (cache miss)", id);
        return studentProfileRepository.findById(id)
                .map(studentProfileMapper::toResponse);
    }
}