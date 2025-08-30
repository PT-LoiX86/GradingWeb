package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.ProvinceRequest;
import com.grd.gradingbe.dto.response.ProvinceResponse;
import com.grd.gradingbe.mapper.ProvinceMapper;
import com.grd.gradingbe.model.Province;
import com.grd.gradingbe.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProvinceService {

    private final ProvinceRepository provinceRepository;
    private final ProvinceMapper provinceMapper;

    /**
     * Lấy danh sách tất cả tỉnh thành
     * Cache với key "all" trong 48 giờ
     */
    @Cacheable(value = "provinces", key = "'all'")
    public List<ProvinceResponse> getAllProvinces() {
        log.info("Fetching all provinces from database (cache miss)");
        return provinceRepository.findAll().stream()
                .map(provinceMapper::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Lấy tỉnh thành theo ID
     * Cache với key là ID trong 48 giờ
     */
    @Cacheable(value = "provinces", key = "#id")
    public Optional<ProvinceResponse> getProvinceById(Long id) {
        log.info("Fetching province with id: {} from database (cache miss)", id);
        return provinceRepository.findById(id)
                .map(provinceMapper::toResponse);
    }

    /**
     * Tạo tỉnh thành mới
     * Xóa cache "all" sau khi tạo
     */
    @CacheEvict(value = "provinces", key = "'all'")
    public ProvinceResponse createProvince(ProvinceRequest provinceRequest) {
        log.info("Creating new province: {}", provinceRequest.getName());
        Province province = provinceMapper.toEntity(provinceRequest);
        Province savedProvince = provinceRepository.save(province);
        return provinceMapper.toResponse(savedProvince);
    }

    /**
     * Cập nhật tỉnh thành
     * Xóa cache của item cụ thể và cache "all"
     */
    @CacheEvict(value = "provinces", allEntries = true)
    public Optional<ProvinceResponse> updateProvince(Long id, ProvinceRequest provinceRequest) {
        log.info("Updating province with id: {}", id);
        return provinceRepository.findById(id)
                .map(province -> {
                    provinceMapper.updateEntityFromRequest(provinceRequest, province);
                    return provinceMapper.toResponse(provinceRepository.save(province));
                });
    }

    /**
     * Xóa tỉnh thành
     * Xóa toàn bộ cache provinces
     */
    @CacheEvict(value = "provinces", allEntries = true)
    public void deleteProvince(Long id) {
        log.info("Deleting province with id: {}", id);
        provinceRepository.deleteById(id);
    }
}