package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.GradeRecordCreateRequest;
import com.grd.gradingbe.dto.request.GradeRecordUpdateRequest;
import com.grd.gradingbe.dto.response.GradeRecordResponse;
import com.grd.gradingbe.exception.BadRequestException;
import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.mapper.GradeRecordMapper;
import com.grd.gradingbe.model.GradeRecord;
import com.grd.gradingbe.repository.GradeRecordRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class GradeRecordService {

    private final GradeRecordRepository gradeRecordRepository;
    private final GradeRecordMapper gradeRecordMapper;

    public GradeRecordResponse createGradeRecord(@Valid GradeRecordCreateRequest dto) {
        if (gradeRecordRepository.existsByStudentProfileIdAndSemesterAndYear(dto.getStudentProfileId(), dto.getSemester(), dto.getYear())) {
            throw new BadRequestException("Grade record already exists for this student in this semester and year.");
        }

        GradeRecord entity = gradeRecordMapper.toEntity(dto);
        GradeRecord saved = gradeRecordRepository.save(entity);
        return gradeRecordMapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public GradeRecordResponse getGradeRecordById(Long id) {
        GradeRecord entity = gradeRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GradeRecord", "id", id.toString()));
        return gradeRecordMapper.toDto(entity);
    }

    @Transactional(readOnly = true)
    public List<GradeRecordResponse> getAllGradeRecords() {
        return gradeRecordRepository.findAll().stream()
                .map(gradeRecordMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GradeRecordResponse> getGradeRecordsByStudentId(Long studentId) {
        return gradeRecordRepository.findByStudentProfileId(studentId).stream()
                .map(gradeRecordMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GradeRecordResponse> getGradeRecordsByStudentIdAndGrade(Long studentId, Integer grade) {
        return gradeRecordRepository.findByStudentProfileIdAndGrade(studentId, grade).stream()
                .map(gradeRecordMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<GradeRecordResponse> getGradeRecordsByStudentIdAndYear(Long studentId, Integer year) {
        return gradeRecordRepository.findByStudentProfileIdAndYear(studentId, year).stream()
                .map(gradeRecordMapper::toDto)
                .collect(Collectors.toList());
    }

    public GradeRecordResponse updateGradeRecord(Long id, @Valid GradeRecordUpdateRequest dto) {
        GradeRecord entity = gradeRecordRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GradeRecord", "id", id.toString()));

        gradeRecordMapper.updateEntity(entity, dto);
        GradeRecord updated = gradeRecordRepository.save(entity);
        return gradeRecordMapper.toDto(updated);
    }

    public void deleteGradeRecord(Long id) {
        if (!gradeRecordRepository.existsById(id)) {
            throw new ResourceNotFoundException("GradeRecord", "id", id.toString());
        }
        gradeRecordRepository.deleteById(id);
    }
}
