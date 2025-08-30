package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.enums.ScoreType;
import com.grd.gradingbe.dto.request.SubjectScoreCreateRequest;
import com.grd.gradingbe.dto.request.SubjectScoreUpdateRequest;
import com.grd.gradingbe.dto.response.SubjectScoreResponse;
import com.grd.gradingbe.exception.BadRequestException;
import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.mapper.SubjectScoreMapper;
import com.grd.gradingbe.model.SubjectScore;
import com.grd.gradingbe.repository.SubjectScoreRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SubjectScoreService {

    private final SubjectScoreRepository subjectScoreRepository;
    private final SubjectScoreMapper subjectScoreMapper;

    public SubjectScoreResponse createSubjectScore(@Valid SubjectScoreCreateRequest dto) {
        boolean exists = subjectScoreRepository
                .existsByGradeRecordIdAndSubjectIdAndScoreType(
                        dto.getGradeRecordId(),
                        dto.getSubjectId(),
                        dto.getScoreType());

        if (exists) {
            throw new BadRequestException("Subject score already exists for this grade record, subject and score type.");
        }

        SubjectScore entity = subjectScoreMapper.toEntity(dto);
        SubjectScore saved = subjectScoreRepository.save(entity);
        return subjectScoreMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public SubjectScoreResponse getSubjectScoreById(Long id) {
        SubjectScore entity = subjectScoreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubjectScore", "id", id.toString()));
        return subjectScoreMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<SubjectScoreResponse> getAllSubjectScores() {
        return subjectScoreRepository.findAll().stream()
                .map(subjectScoreMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubjectScoreResponse> getSubjectScoresByGradeRecordId(Long gradeRecordId) {
        return subjectScoreRepository.findByGradeRecordId(gradeRecordId).stream()
                .map(subjectScoreMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubjectScoreResponse> getSubjectScoresBySubjectId(Long subjectId) {
        return subjectScoreRepository.findBySubjectId(subjectId).stream()
                .map(subjectScoreMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<SubjectScoreResponse> getSubjectScoresByScoreType(ScoreType scoreType) {
        return subjectScoreRepository.findByScoreType(scoreType).stream()
                .map(subjectScoreMapper::toDto)
                .collect(Collectors.toList());
    }

    public SubjectScoreResponse updateSubjectScore(Long id, @Valid SubjectScoreUpdateRequest dto) {
        SubjectScore entity = subjectScoreRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("SubjectScore", "id", id.toString()));

        subjectScoreMapper.updateEntity(entity, dto);
        SubjectScore updated = subjectScoreRepository.save(entity);
        return subjectScoreMapper.toDto(updated);
    }

    public void deleteSubjectScore(Long id) {
        if (!subjectScoreRepository.existsById(id)) {
            throw new ResourceNotFoundException("SubjectScore", "id", id.toString());
        }
        subjectScoreRepository.deleteById(id);
    }
}
