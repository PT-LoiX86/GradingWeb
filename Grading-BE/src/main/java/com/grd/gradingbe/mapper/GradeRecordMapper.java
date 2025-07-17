package com.grd.gradingbe.mapper;

import com.grd.gradingbe.dto.request.GradeRecordRequest;
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
    GradeRecord toEntity(@Valid GradeRecordRequest gradeRecordDto);

    // Phương thức cập nhật entity từ GradeRecordCreateRequest
    void updateEntityFromCreate(@MappingTarget GradeRecord gradeRecord, @Valid GradeRecordRequest gradeRecordDto);
}