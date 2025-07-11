package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.response.StudentProfileResponse;
import com.grd.gradingbe.repository.StudentProfileRepository;
import com.grd.gradingbe.mapper.StudentProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final StudentProfileMapper studentProfileMapper;

    public List<StudentProfileResponse> getAllStudentProfiles() {
        return studentProfileRepository.findAll().stream()
                .map(studentProfileMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<StudentProfileResponse> getStudentProfileById(Long id) {
        return studentProfileRepository.findById(id)
                .map(studentProfileMapper::toResponse);
    }
}