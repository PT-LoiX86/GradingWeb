package com.grd.gradingbe.model;

import com.grd.gradingbe.dto.entity.BaseEntity;
import com.grd.gradingbe.dto.enums.ScoreType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "subject_scores")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectScore extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "grade_record_id", nullable = false)
    private Long gradeRecordId;

    @Column(name = "subject_id", nullable = false)
    private Long subjectId;

    @Column(name = "score", precision = 3, scale = 1, nullable = false)
    private BigDecimal score;

    @Enumerated(EnumType.STRING)
    @Column(name = "score_type", nullable = false)
    private ScoreType scoreType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_record_id", insertable = false, updatable = false)
    private GradeRecord gradeRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", insertable = false, updatable = false)
    private Subject subject;

}