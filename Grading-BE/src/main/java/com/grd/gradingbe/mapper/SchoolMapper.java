package com.grd.gradingbe.mapper;

import com.grd.gradingbe.dto.request.SchoolRequest;
import com.grd.gradingbe.dto.response.SchoolResponse;
import com.grd.gradingbe.model.School;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public abstract class SchoolMapper {

    @Mapping(target = "province", ignore = true) // Province sẽ được gán trong service
    public abstract School toEntity(SchoolRequest schoolRequest);

    @Mapping(target = "provinceId", source = "province.id")
    public abstract SchoolResponse toResponse(School school);

    @Mapping(target = "province", ignore = true)
    public abstract void updateEntityFromRequest(SchoolRequest schoolRequest, @MappingTarget School school);
}