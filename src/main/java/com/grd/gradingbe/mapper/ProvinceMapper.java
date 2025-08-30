package com.grd.gradingbe.mapper;

import com.grd.gradingbe.dto.request.ProvinceRequest;
import com.grd.gradingbe.dto.response.ProvinceResponse;
import com.grd.gradingbe.model.Province;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ProvinceMapper {

    Province toEntity(ProvinceRequest provinceRequest);

    @Mapping(target = "id", source = "id")
    ProvinceResponse toResponse(Province province);

    void updateEntityFromRequest(ProvinceRequest provinceRequest, @MappingTarget Province province);
    default Province toEntity(Long id) {
        if (id == null) return null;
        return Province.builder().id(id).build();
    }

}