package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.StudentProfileRequest;
import com.grd.gradingbe.dto.response.StudentProfileResponse;
import com.grd.gradingbe.mapper.StudentProfileMapper;
import com.grd.gradingbe.model.StudentProfile;
import com.grd.gradingbe.repository.StudentProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentProfileService {

    private final StudentProfileRepository studentProfileRepository;
    private final StudentProfileMapper studentProfileMapper;

    @Transactional(readOnly = true)
    public List<StudentProfileResponse> getAllStudentProfiles() {
        return studentProfileRepository.findAll().stream()
                .map(studentProfileMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<StudentProfileResponse> getStudentProfileById(Long id) {
        return studentProfileRepository.findById(id)
                .map(studentProfileMapper::toResponse);
    }

    public StudentProfileResponse createStudentProfile(StudentProfileRequest request) {
        validateRequiredFields(request);
        StudentProfile profile = studentProfileMapper.toEntity(request);
        StudentProfile savedProfile = studentProfileRepository.save(profile);
        return studentProfileMapper.toResponse(savedProfile);
    }

    public StudentProfileResponse updateStudentProfile(Long id, StudentProfileRequest request) {
        StudentProfile existingProfile = studentProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Student profile not found with id: " + id));
        studentProfileMapper.updateEntityFromRequest(request, existingProfile);

        StudentProfile updatedProfile = studentProfileRepository.save(existingProfile);
        return studentProfileMapper.toResponse(updatedProfile);
    }

    public void deleteStudentProfile(Long id) {
        if (!studentProfileRepository.existsById(id)) {
            throw new IllegalArgumentException("Student profile not found with id: " + id);
        }
        studentProfileRepository.deleteById(id);
    }

    private void validateRequiredFields(StudentProfileRequest request) {
        if (request.getDateOfBirth() == null || request.getGender() == null ||
                request.getSchoolId() == null || request.getProvinceId() == null || request.getUserId() == null) {
            throw new IllegalArgumentException("Required fields are missing: name, dateOfBirth, gender, schoolId, provinceId, userId");
        }
    }
}