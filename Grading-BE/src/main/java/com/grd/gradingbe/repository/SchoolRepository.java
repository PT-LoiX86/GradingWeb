package com.grd.gradingbe.repository;

import com.grd.gradingbe.model.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}