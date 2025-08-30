package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.request.SubjectCreateRequest;
import com.grd.gradingbe.dto.request.SubjectUpdateRequest;
import com.grd.gradingbe.dto.response.ApiResponse;
import com.grd.gradingbe.dto.response.SubjectResponse;
import com.grd.gradingbe.service.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<ApiResponse<SubjectResponse>> createSubject(
            @Valid @RequestBody SubjectCreateRequest createDto) {
        SubjectResponse createdSubject = subjectService.createSubject(createDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Subject created successfully", createdSubject));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> getSubjectById(@PathVariable Long id) {
        SubjectResponse subject = subjectService.getSubjectById(id);
        return ResponseEntity.ok(ApiResponse.success("Fetched subject by ID successfully", subject));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> getAllSubjects() {
        List<SubjectResponse> subjects = subjectService.getAllSubjects();
        return ResponseEntity.ok(ApiResponse.success("Fetched all subjects successfully", subjects));
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<ApiResponse<SubjectResponse>> getSubjectByCode(@PathVariable String code) {
        SubjectResponse subject = subjectService.getSubjectByCode(code);
        return ResponseEntity.ok(ApiResponse.success("Fetched subject by code successfully", subject));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<SubjectResponse>>> searchSubjects(@RequestParam String name) {
        List<SubjectResponse> subjects = subjectService.searchSubjectsByName(name);
        return ResponseEntity.ok(ApiResponse.success("Searched subjects by name successfully", subjects));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubjectResponse>> updateSubject(
            @PathVariable Long id,
            @Valid @RequestBody SubjectUpdateRequest updateDto) {
        SubjectResponse updatedSubject = subjectService.updateSubject(id, updateDto);
        return ResponseEntity.ok(ApiResponse.success("Updated subject successfully", updatedSubject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSubject(@PathVariable Long id) {
        subjectService.deleteSubject(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted subject successfully", null));
    }
}
