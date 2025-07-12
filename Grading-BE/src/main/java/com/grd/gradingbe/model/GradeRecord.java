package com.grd.gradingbe.model;

import com.grd.gradingbe.dto.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "grade_records")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GradeRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "student_profile_id", nullable = false)
    private Long studentProfileId;

    @Column(name = "semester", nullable = false)
    private Integer semester;

    @Column(name = "grade", nullable = false)
    private Integer grade;

    @Column(name = "year", nullable = false)
    private Integer year;

    @Column(name = "is_uploaded", nullable = false)
    private Boolean isUploaded;

    @Column(name = "file_url", length = 255)
    private String fileUrl;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @OneToMany(mappedBy = "gradeRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SubjectScore> subjectScores;
}