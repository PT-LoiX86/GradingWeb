package com.grd.gradingbe.repository;

import com.grd.gradingbe.model.StudentProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentProfileRepository extends JpaRepository<StudentProfile, Long> {
}