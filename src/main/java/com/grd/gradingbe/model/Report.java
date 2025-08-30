package com.grd.gradingbe.model;

import com.grd.gradingbe.dto.entity.BaseEntity;
import com.grd.gradingbe.dto.enums.ContentType;
import com.grd.gradingbe.dto.enums.ReasonType;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "reports")
public class Report extends BaseEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long content_id;

    private ContentType content_type;

    private ReasonType reason;

    private String description;
}
