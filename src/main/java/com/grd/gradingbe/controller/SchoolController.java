package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.request.SchoolRequest;
import com.grd.gradingbe.dto.response.ApiResponse;
import com.grd.gradingbe.dto.response.SchoolResponse;
import com.grd.gradingbe.service.SchoolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schools")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class SchoolController {

    private final SchoolService schoolService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<SchoolResponse>>> getAllSchools() {
        List<SchoolResponse> schools = schoolService.getAllSchools();
        return ResponseEntity.ok(ApiResponse.success("Fetched school list successfully", schools));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SchoolResponse>> getSchoolById(@PathVariable Long id) {
        return schoolService.getSchoolById(id)
                .map(school -> ResponseEntity.ok(ApiResponse.success("Fetched school successfully", school)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("School not found with ID = " + id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SchoolResponse>> createSchool(@RequestBody SchoolRequest schoolRequest) {
        try {
            SchoolResponse createdSchool = schoolService.createSchool(schoolRequest);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(ApiResponse.success("School created successfully", createdSchool));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error while creating school", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to create school"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSchool(@PathVariable Long id) {
        try {
            schoolService.deleteSchool(id);
            return ResponseEntity.ok(ApiResponse.success("School deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error while deleting school", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete school"));
        }
    }
}
