package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.request.MajorRequest;
import com.grd.gradingbe.dto.response.MajorResponse;
import com.grd.gradingbe.dto.response.PageResponse;
import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.model.Major;
import com.grd.gradingbe.model.University;
import com.grd.gradingbe.repository.MajorRepository;
import com.grd.gradingbe.repository.UniversityRepository;
import com.grd.gradingbe.service.MajorService;
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
public class MajorServiceImpl implements MajorService {

    private final MajorRepository majorRepository;
    private final UniversityRepository universityRepository;

    /**
     * Lấy danh sách tất cả majors với pagination và search
     * Cache theo page, size, sortBy, sortDir và search
     * Chỉ cache những page đầu tiên (< 5) và khi không có search
     */
    @Override
    @Cacheable(value = "majors",
            key = "'page:' + #page + ':size:' + #size + ':sort:' + #sortBy + ':' + #sortDir + ':search:' + (#search ?: 'none')",
            condition = "#page < 5 && (#search == null || #search.isEmpty())")
    public PageResponse<MajorResponse> getAllMajors(int page, int size, String sortBy, String sortDir, String search) {
        log.info("Fetching majors from database - page: {}, size: {}, search: {} (cache miss)", page, size, search);
        Sort sortObj = sortBy.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sortObj);
        Page<Major> pageMajor = majorRepository.findAll(pageable);
        List<Major> majorContent = pageMajor.getContent();

        return PageResponse.<MajorResponse>builder()
                .content(mapToMajorResponse(majorContent))
                .page(pageMajor.getNumber())
                .size(pageMajor.getSize())
                .totalElements((int) pageMajor.getTotalElements())
                .totalPages(pageMajor.getTotalPages())
                .last(pageMajor.isLast())
                .build();
    }

    /**
     * Lấy major theo ID
     * Cache với key là ID trong 12 giờ
     */
    @Override
    @Cacheable(value = "majors", key = "#id")
    public MajorResponse getMajorById(Long id) {
        log.info("Fetching major with id: {} from database (cache miss)", id);
        Major major = majorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Major", "id", id.toString())
        );

        return mapToMajorResponse(major);
    }

    /**
     * Tạo major mới
     * Xóa toàn bộ cache majors sau khi tạo
     */
    @Override
    @CacheEvict(value = "majors", allEntries = true)
    public MajorResponse createMajor(MajorRequest majorRequest) {
        log.info("Creating new major: {}", majorRequest.getName());

        University universityRequest = universityRepository.findById(majorRequest.getUniversityId())
                .orElseThrow(() -> new ResourceNotFoundException("University", "id", majorRequest.getUniversityId().toString()));

        Major major = Major.builder()
                .name(majorRequest.getName())
                .code(majorRequest.getCode())
                .description(majorRequest.getDescription())
                .durationYears(majorRequest.getDurationYears())
                .university(universityRequest)
                .build();

        Major savedMajor = majorRepository.save(major);
        return mapToMajorResponse(savedMajor);
    }

    /**
     * Cập nhật major
     * Xóa toàn bộ cache majors sau khi update
     */
    @Override
    @CacheEvict(value = "majors", allEntries = true)
    public MajorResponse updateMajor(Long id, MajorRequest majorRequest) {
        log.info("Updating major with id: {}", id);
        Major existingMajor = majorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Major", "id", id.toString())
        );
        University university = universityRepository.findById(majorRequest.getUniversityId())
                .orElseThrow(() -> new ResourceNotFoundException("University", "id", majorRequest.getUniversityId().toString()));

        existingMajor.setName(majorRequest.getName());
        existingMajor.setCode(majorRequest.getCode());
        existingMajor.setDescription(majorRequest.getDescription());
        existingMajor.setDurationYears(majorRequest.getDurationYears());
        existingMajor.setUniversity(university);

        return mapToMajorResponse(majorRepository.save(existingMajor));
    }

    /**
     * Xóa major
     * Xóa toàn bộ cache majors sau khi delete
     */
    @Override
    @CacheEvict(value = "majors", allEntries = true)
    public void deleteMajor(Long id) {
        log.info("Deleting major with id: {}", id);
        Major major = majorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Major", "id", id.toString())
        );
        majorRepository.delete(major);
    }

    private List<MajorResponse> mapToMajorResponse(List<Major> majorContent) {
        return majorContent.stream()
                .map(this::mapToMajorResponse)
                .toList();
    }

    private MajorResponse mapToMajorResponse(Major major) {
        return MajorResponse.builder()
                .id(major.getId())
                .name(major.getName())
                .description(major.getDescription())
                .universityId(major.getUniversity().getId())
                .createdAt(String.valueOf(major.getCreatedAt()))
                .createdBy(major.getCreatedBy())
                .updatedAt(String.valueOf(major.getUpdatedAt()))
                .updatedBy(major.getUpdatedBy())
                .build();
    }
}
