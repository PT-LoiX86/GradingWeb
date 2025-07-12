package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.request.StudentProfileRequest;
import com.grd.gradingbe.dto.response.ApiResponse;
import com.grd.gradingbe.dto.response.StudentProfileResponse;
import com.grd.gradingbe.service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-profiles")
@RequiredArgsConstructor
@Slf4j
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudentProfileResponse>>> getAllStudentProfiles() {
        log.info("Fetching all student profiles");
        List<StudentProfileResponse> responses = studentProfileService.getAllStudentProfiles();
        return ResponseEntity.ok(ApiResponse.success("Successfully fetched student profile list", responses));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentProfileResponse>> getStudentProfileById(@PathVariable Long id) {
        log.info("Fetching student profile with ID: {}", id);
        return studentProfileService.getStudentProfileById(id)
                .map(profile -> ResponseEntity.ok(ApiResponse.success("Successfully fetched student profile", profile)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Student profile not found with ID = " + id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<StudentProfileResponse>> createStudentProfile(
            @RequestBody StudentProfileRequest request) {
        log.info("Creating student profile for user ID: {}", request.getUserId());
        StudentProfileResponse response = studentProfileService.createStudentProfile(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Student profile created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<StudentProfileResponse>> updateStudentProfile(
            @PathVariable Long id,
            @RequestBody StudentProfileRequest request) {
        log.info("Updating student profile with ID: {}", id);
        StudentProfileResponse response = studentProfileService.updateStudentProfile(id, request);
        return ResponseEntity.ok(ApiResponse.success("Student profile updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteStudentProfile(@PathVariable Long id) {
        log.info("Deleting student profile with ID: {}", id);
        studentProfileService.deleteStudentProfile(id);
        return ResponseEntity.ok(ApiResponse.success("Student profile deleted successfully", null));
    }
}
