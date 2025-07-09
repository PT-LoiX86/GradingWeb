package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.response.StudentProfileResponse;
import com.grd.gradingbe.service.StudentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class StudentProfileController {

    private final StudentProfileService studentProfileService;

    @GetMapping
    public ResponseEntity<List<StudentProfileResponse>> getAllStudentProfiles() {
        List<StudentProfileResponse> studentProfiles = studentProfileService.getAllStudentProfiles();
        return new ResponseEntity<>(studentProfiles, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentProfileResponse> getStudentProfileById(@PathVariable Long id) {
        return studentProfileService.getStudentProfileById(id)
                .map(studentProfile -> new ResponseEntity<>(studentProfile, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}