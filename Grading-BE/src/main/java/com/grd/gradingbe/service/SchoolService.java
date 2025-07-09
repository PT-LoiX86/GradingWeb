package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.response.SchoolResponse;
import com.grd.gradingbe.mapper.SchoolMapper;
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

    public List<SchoolResponse> getAllSchools() {
        return schoolRepository.findAll().stream()
                .map(schoolMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<SchoolResponse> getSchoolById(Long id) {
        return schoolRepository.findById(id)
                .map(schoolMapper::toResponse);
    }
}