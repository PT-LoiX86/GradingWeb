package com.grd.gradingbe.mapper;

import com.grd.gradingbe.dto.request.SubjectCreateRequest;
import com.grd.gradingbe.dto.request.SubjectUpdateRequest;
import com.grd.gradingbe.dto.response.SubjectResponse;
import com.grd.gradingbe.model.Subject;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface SubjectMapper {
    SubjectResponse toDto(Subject subject);
    Subject toEntity(@Valid SubjectCreateRequest subjectDto);
    void updateEntity(@MappingTarget Subject subject, @Valid SubjectUpdateRequest subjectDto);
}