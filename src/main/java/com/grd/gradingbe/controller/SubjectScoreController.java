package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.enums.ScoreType;
import com.grd.gradingbe.dto.request.SubjectScoreCreateRequest;
import com.grd.gradingbe.dto.request.SubjectScoreUpdateRequest;
import com.grd.gradingbe.dto.response.ApiResponse;
import com.grd.gradingbe.dto.response.SubjectScoreResponse;
import com.grd.gradingbe.service.SubjectScoreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subject-scores")
@RequiredArgsConstructor
public class SubjectScoreController {

    private final SubjectScoreService subjectScoreService;

    @PostMapping
    public ResponseEntity<ApiResponse<SubjectScoreResponse>> createSubjectScore(
            @Valid @RequestBody SubjectScoreCreateRequest createDto) {
        SubjectScoreResponse created = subjectScoreService.createSubjectScore(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Subject score created successfully", created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectScoreResponse>> getSubjectScoreById(@PathVariable Long id) {
        SubjectScoreResponse response = subjectScoreService.getSubjectScoreById(id);
        return ResponseEntity.ok(ApiResponse.success("Fetched subject score successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectScoreResponse>>> getAllSubjectScores() {
        List<SubjectScoreResponse> scores = subjectScoreService.getAllSubjectScores();
        return ResponseEntity.ok(ApiResponse.success("Fetched all subject scores successfully", scores));
    }

    @GetMapping("/grade-record/{gradeRecordId}")
    public ResponseEntity<ApiResponse<List<SubjectScoreResponse>>> getByGradeRecordId(
            @PathVariable Long gradeRecordId) {
        List<SubjectScoreResponse> scores = subjectScoreService.getSubjectScoresByGradeRecordId(gradeRecordId);
        return ResponseEntity.ok(ApiResponse.success("Fetched subject scores by grade record ID", scores));
    }

    @GetMapping("/subject/{subjectId}")
    public ResponseEntity<ApiResponse<List<SubjectScoreResponse>>> getBySubjectId(@PathVariable Long subjectId) {
        List<SubjectScoreResponse> scores = subjectScoreService.getSubjectScoresBySubjectId(subjectId);
        return ResponseEntity.ok(ApiResponse.success("Fetched subject scores by subject ID", scores));
    }

    @GetMapping("/score-type/{scoreType}")
    public ResponseEntity<ApiResponse<List<SubjectScoreResponse>>> getByScoreType(@PathVariable ScoreType scoreType) {
        List<SubjectScoreResponse> scores = subjectScoreService.getSubjectScoresByScoreType(scoreType);
        return ResponseEntity.ok(ApiResponse.success("Fetched subject scores by score type", scores));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectScoreResponse>> updateSubjectScore(
            @PathVariable Long id,
            @Valid @RequestBody SubjectScoreUpdateRequest updateDto) {
        SubjectScoreResponse updated = subjectScoreService.updateSubjectScore(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success("Updated subject score successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSubjectScore(@PathVariable Long id) {
        subjectScoreService.deleteSubjectScore(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted subject score successfully", null));
    }
}
