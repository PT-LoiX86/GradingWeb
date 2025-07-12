package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.ProvinceRequest;
import com.grd.gradingbe.dto.response.ProvinceResponse;
import com.grd.gradingbe.mapper.ProvinceMapper;
import com.grd.gradingbe.model.Province;
import com.grd.gradingbe.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final ProvinceMapper provinceMapper;

    public List<ProvinceResponse> getAllProvinces() {
        return provinceRepository.findAll().stream()
                .map(provinceMapper::toResponse)
                .collect(Collectors.toList());
    }

    public Optional<ProvinceResponse> getProvinceById(Long id) {
        return provinceRepository.findById(id)
                .map(provinceMapper::toResponse);
    }

    public ProvinceResponse createProvince(ProvinceRequest provinceRequest) {
        Province province = provinceMapper.toEntity(provinceRequest);
        Province savedProvince = provinceRepository.save(province);
        return provinceMapper.toResponse(savedProvince);
    }

    public Optional<ProvinceResponse> updateProvince(Long id, ProvinceRequest provinceRequest) {
        return provinceRepository.findById(id)
                .map(province -> {
                    provinceMapper.updateEntityFromRequest(provinceRequest, province);
                    return provinceMapper.toResponse(provinceRepository.save(province));
                });
    }

    public void deleteProvince(Long id) {
        provinceRepository.deleteById(id);
    }
}