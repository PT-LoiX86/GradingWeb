package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.request.GradeRecordRequest;
import com.grd.gradingbe.dto.response.ApiResponse;
import com.grd.gradingbe.dto.response.GradeRecordResponse;
import com.grd.gradingbe.service.GradeRecordService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/grade-records")
@RequiredArgsConstructor
public class GradeRecordController {

    private final GradeRecordService gradeRecordService;

    @PostMapping
    public ResponseEntity<ApiResponse<GradeRecordResponse>> createGradeRecord(
            @Valid @RequestBody GradeRecordRequest createDto) {
        GradeRecordResponse created = gradeRecordService.createGradeRecord(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Grade record created successfully", created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<GradeRecordResponse>> getGradeRecordById(@PathVariable Long id) {
        GradeRecordResponse response = gradeRecordService.getGradeRecordById(id);
        return ResponseEntity.ok(ApiResponse.success("Fetched grade record successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<GradeRecordResponse>>> getAllGradeRecords() {
        List<GradeRecordResponse> records = gradeRecordService.getAllGradeRecords();
        return ResponseEntity.ok(ApiResponse.success("Fetched all grade records successfully", records));
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<ApiResponse<List<GradeRecordResponse>>> getGradeRecordsByStudentId(
            @PathVariable Long studentId) {
        List<GradeRecordResponse> records = gradeRecordService.getGradeRecordsByStudentId(studentId);
        return ResponseEntity.ok(ApiResponse.success("Fetched grade records by student ID", records));
    }

    @GetMapping("/student/{studentId}/grade/{grade}")
    public ResponseEntity<ApiResponse<List<GradeRecordResponse>>> getGradeRecordsByStudentIdAndGrade(
            @PathVariable Long studentId, @PathVariable Integer grade) {
        List<GradeRecordResponse> records = gradeRecordService.getGradeRecordsByStudentIdAndGrade(studentId, grade);
        return ResponseEntity.ok(ApiResponse.success("Fetched grade records by student ID and grade", records));
    }

    @GetMapping("/student/{studentId}/year/{year}")
    public ResponseEntity<ApiResponse<List<GradeRecordResponse>>> getGradeRecordsByStudentIdAndYear(
            @PathVariable Long studentId, @PathVariable Integer year) {
        List<GradeRecordResponse> records = gradeRecordService.getGradeRecordsByStudentIdAndYear(studentId, year);
        return ResponseEntity.ok(ApiResponse.success("Fetched grade records by student ID and year", records));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<GradeRecordResponse>> updateGradeRecord(
            @PathVariable Long id,
            @Valid @RequestBody GradeRecordRequest updateDto) {
        GradeRecordResponse updated = gradeRecordService.updateGradeRecord(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success("Updated grade record successfully", updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteGradeRecord(@PathVariable Long id) {
        gradeRecordService.deleteGradeRecord(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted grade record successfully", null));
    }
}
