package com.grd.gradingbe.mapper;

import com.grd.gradingbe.dto.request.SubjectScoreCreateRequest;
import com.grd.gradingbe.dto.response.SubjectScoreResponse;
import com.grd.gradingbe.dto.request.SubjectScoreUpdateRequest;
import com.grd.gradingbe.model.SubjectScore;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;


@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {SubjectMapper.class})
public interface SubjectScoreMapper {
    SubjectScoreResponse toDto(SubjectScore subjectScore);
    SubjectScore toEntity(@Valid SubjectScoreCreateRequest subjectScoreDto);
    void updateEntity(@MappingTarget SubjectScore subjectScore, @Valid SubjectScoreUpdateRequest subjectScoreDto);
}
