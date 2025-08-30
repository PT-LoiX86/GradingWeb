package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.enums.TypeUniversity;
import com.grd.gradingbe.dto.request.UniversityRequest;
import com.grd.gradingbe.dto.response.PageResponse;
import com.grd.gradingbe.dto.response.UniversityResponse;
import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.model.University;
import com.grd.gradingbe.repository.UniversityRepository;
import com.grd.gradingbe.service.UniversityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;

    /**
     * Lấy danh sách tất cả universities với pagination và search
     * Cache theo page, size, sortBy, sortDir và search
     * Chỉ cache những page đầu tiên (< 5) và khi không có search
     */
    @Override
    @Cacheable(value = "universities",
            key = "'page:' + #page + ':size:' + #size + ':sort:' + #sortBy + ':' + #sortDir + ':search:' + (#search ?: 'none')",
            condition = "#page < 5 && (#search == null || #search.isEmpty())")
    public PageResponse<UniversityResponse> getAllUniversities(int page, int size, String sortBy, String sortDir, String search) {
        log.info("Fetching universities from database - page: {}, size: {}, search: {} (cache miss)", page, size, search);
        Sort sortObj = sortBy.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<University> pageUniversity = universityRepository.findAll(pageable);
        List<University> universityContent = pageUniversity.getContent();

        return PageResponse.<UniversityResponse>builder()
                .content(mapToUniversityResponse(universityContent))
                .page(pageUniversity.getNumber())
                .size(pageUniversity.getSize())
                .totalElements((int) pageUniversity.getTotalElements())
                .totalPages(pageUniversity.getTotalPages())
                .last(pageUniversity.isLast())
                .build();
    }

    /**
     * Lấy university theo ID
     * Cache với key là ID trong 12 giờ
     */
    @Override
    @Cacheable(value = "universities", key = "#id")
    public UniversityResponse getUniversityById(Long id) {
        log.info("Fetching university with id: {} from database (cache miss)", id);
        University university = universityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("University", "id", id.toString())
        );

        return mapToUniversityResponse(university);
    }

    /**
     * Tạo university mới
     * Xóa toàn bộ cache universities sau khi tạo
     */
    @Override
    @CacheEvict(value = "universities", allEntries = true)
    public UniversityResponse createUniversity(UniversityRequest universityRequest) {
        log.info("Creating new university: {}", universityRequest.getName());
        University university = University.builder()
                .name(universityRequest.getName())
                .description(universityRequest.getDescription())
                .code(universityRequest.getCode())
                .address(universityRequest.getAddress())
                .logoUrl(universityRequest.getLogoUrl())
                .phoneNumber(universityRequest.getPhoneNumber())
                .email(universityRequest.getEmail())
                .website(universityRequest.getWebsite())
                .typeUniversity(TypeUniversity.fromValue(universityRequest.getTypeUniversity()))
                .build();

        University savedUniversity = universityRepository.save(university);
        return mapToUniversityResponse(savedUniversity);
    }

    /**
     * Cập nhật university
     * Xóa toàn bộ cache universities sau khi update
     */
    @Override
    @CacheEvict(value = "universities", allEntries = true)
    public UniversityResponse updateUniversity(Long id, UniversityRequest universityRequest) {
        log.info("Updating university with id: {}", id);
        University university = universityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("University", "id", id.toString())
        );

        university.setName(universityRequest.getName());
        university.setDescription(universityRequest.getDescription());
        university.setCode(universityRequest.getCode());
        university.setAddress(universityRequest.getAddress());
        university.setLogoUrl(universityRequest.getLogoUrl());
        university.setPhoneNumber(universityRequest.getPhoneNumber());
        university.setEmail(universityRequest.getEmail());
        university.setWebsite(universityRequest.getWebsite());
        university.setTypeUniversity(TypeUniversity.fromValue(universityRequest.getTypeUniversity()));

        University updatedUniversity = universityRepository.save(university);
        return mapToUniversityResponse(updatedUniversity);
    }

    /**
     * Xóa university
     * Xóa toàn bộ cache universities sau khi delete
     */
    @Override
    @CacheEvict(value = "universities", allEntries = true)
    public void deleteUniversity(Long id) {
        log.info("Deleting university with id: {}", id);
        University university = universityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("University", "id", id.toString())
        );
        universityRepository.delete(university);
    }

    private List<UniversityResponse> mapToUniversityResponse(List<University> universityContent) {
        return universityContent.stream()
                .map(this::mapToUniversityResponse)
                .toList();
    }

    private UniversityResponse mapToUniversityResponse(University university) {
        return UniversityResponse.builder()
                .id(university.getId())
                .name(university.getName())
                .description(university.getDescription() != null ? university.getDescription() : "")
                .code(university.getCode())
                .address(university.getAddress() != null ? university.getAddress() : "")
                .logoUrl(university.getLogoUrl() != null ? university.getLogoUrl() : "")
                .phoneNumber(university.getPhoneNumber() != null ? university.getPhoneNumber() : "")
                .email(university.getEmail() != null ? university.getEmail() : "")
                .website(university.getWebsite() != null ? university.getWebsite() : "")
                .typeUniversity(university.getTypeUniversity().getValue())
                .createdAt(university.getCreatedAt().toString())
                .createdBy(university.getCreatedBy())
                .updatedAt(university.getUpdatedAt() != null ? university.getUpdatedAt().toString() : null)
                .updatedBy(university.getUpdatedBy())
                .build();
    }
}
