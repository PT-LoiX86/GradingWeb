package com.grd.gradingbe.repository;

import com.grd.gradingbe.model.GradeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRecordRepository extends JpaRepository<GradeRecord, Long> {
    List<GradeRecord> findByStudentProfileId(Long studentProfileId);

    List<GradeRecord> findByStudentProfileIdAndGrade(Long studentProfileId, Integer grade);

    List<GradeRecord> findByStudentProfileIdAndYear(Long studentProfileId, Integer year);

    boolean existsByStudentProfileIdAndSemesterAndYear(Long studentId, Integer semester, Integer year);

    @Query("SELECT gr FROM GradeRecord gr WHERE gr.studentProfileId = :studentId AND gr.grade = :grade AND gr.semester = :semester")
    Optional<GradeRecord> findByStudentProfileIdAndGradeAndSemester(
            @Param("studentId") Long studentId,
            @Param("grade") Integer grade,
            @Param("semester") Integer semester
    );
}

