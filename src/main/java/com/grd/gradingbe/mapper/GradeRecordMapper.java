package com.grd.gradingbe.mapper;

import com.grd.gradingbe.dto.request.GradeRecordCreateRequest;
import com.grd.gradingbe.dto.request.GradeRecordUpdateRequest;
import com.grd.gradingbe.dto.response.GradeRecordResponse;
import com.grd.gradingbe.model.GradeRecord;
import jakarta.validation.Valid;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = {SubjectScoreMapper.class})
public interface GradeRecordMapper {
    GradeRecordResponse toDto(GradeRecord gradeRecord);
    GradeRecord toEntity(@Valid GradeRecordCreateRequest gradeRecordDto);
    void updateEntity(@MappingTarget GradeRecord gradeRecord, @Valid GradeRecordUpdateRequest gradeRecordDto);
}
