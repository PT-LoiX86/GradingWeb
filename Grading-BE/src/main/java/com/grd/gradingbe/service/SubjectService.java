package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.SubjectCreateRequest;
import com.grd.gradingbe.dto.request.SubjectUpdateRequest;
import com.grd.gradingbe.dto.response.SubjectResponse;
import com.grd.gradingbe.mapper.SubjectMapper;
import com.grd.gradingbe.model.Subject;
import com.grd.gradingbe.repository.SubjectRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapper subjectMapper;

    public SubjectResponse createSubject(@Valid SubjectCreateRequest subjectDto) {
        if (subjectRepository.existsByCode(subjectDto.getCode())) {
            throw new IllegalArgumentException("Subject code already exists");
        }

        Subject subject = subjectMapper.toEntity(subjectDto);
        Subject savedSubject = subjectRepository.save(subject);
        return subjectMapper.toDto(savedSubject);
    }

    @Transactional(readOnly = true)
    public SubjectResponse getSubjectById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        return subjectMapper.toDto(subject);
    }

    @Transactional(readOnly = true)
    public List<SubjectResponse> getAllSubjects() {
        return subjectRepository.findAll().stream()
                .map(subjectMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubjectResponse getSubjectByCode(String code) {
        Subject subject = subjectRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Subject not found"));
        return subjectMapper.toDto(subject);
    }

    @Transactional(readOnly = true)
    public List<SubjectResponse> searchSubjectsByName(String name) {
        return subjectRepository.findByNameContaining(name).stream()
                .map(subjectMapper::toDto)
                .collect(Collectors.toList());
    }

    public SubjectResponse updateSubject(Long id, @Valid SubjectUpdateRequest subjectDto) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found"));

        subjectMapper.updateEntity(subject, subjectDto);
        Subject updatedSubject = subjectRepository.save(subject);
        return subjectMapper.toDto(updatedSubject);
    }

    public void deleteSubject(Long id) {
        if (!subjectRepository.existsById(id)) {
            throw new RuntimeException("Subject not found");
        }
        subjectRepository.deleteById(id);
    }
}
