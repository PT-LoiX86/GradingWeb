package com.grd.gradingbe.repository;

import com.grd.gradingbe.dto.enums.ScoreType;
import com.grd.gradingbe.model.SubjectScore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubjectScoreRepository extends JpaRepository<SubjectScore, Long> {
    List<SubjectScore> findByGradeRecordId(Long gradeRecordId);

//    List<SubjectScore> findBySubjectId(Long subjectId);

    @Query("SELECT ss FROM SubjectScore ss JOIN FETCH ss.subject WHERE ss.id = :id")
    Optional<SubjectScore> findBySubjectId(@Param("id") Long id);

    List<SubjectScore> findByGradeRecordIdAndSubjectId(Long gradeRecordId, Long subjectId);

    List<SubjectScore> findByScoreType(ScoreType scoreType);

    @Query("SELECT ss FROM SubjectScore ss WHERE ss.gradeRecordId = :gradeRecordId AND ss.scoreType = :scoreType")
    List<SubjectScore> findByGradeRecordIdAndScoreType(
            @Param("gradeRecordId") Long gradeRecordId,
            @Param("scoreType") ScoreType scoreType
    );
    boolean existsByGradeRecordIdAndSubjectIdAndScoreType(Long gradeRecordId, Long subjectId, ScoreType scoreType);

}