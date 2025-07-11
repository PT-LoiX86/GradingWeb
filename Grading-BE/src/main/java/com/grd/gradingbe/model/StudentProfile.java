package com.grd.gradingbe.model;

import com.grd.gradingbe.dto.entity.BaseEntity;
import com.grd.gradingbe.dto.enums.GenderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "student_profiles")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentProfile extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code;

    @Column(nullable = false)
    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private GenderType gender;

    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    private School school;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false)
    private Province province;

    private Integer year; // Năm nhập học
    private String ethnic;
    private String religion;
}