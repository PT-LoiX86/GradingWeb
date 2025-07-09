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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MajorServiceImpl implements MajorService {

    private final MajorRepository majorRepository;
    private final UniversityRepository universityRepository;

    @Override
    public PageResponse<MajorResponse> getAllMajors(int page, int size, String sortBy, String sortDir, String search) {
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

    @Override
    public MajorResponse getMajorById(Long id) {
        Major major = majorRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Major", "id", id.toString())
        );

        return mapToMajorResponse(major);
    }

    @Override
    public MajorResponse createMajor(MajorRequest majorRequest) {

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

    @Override
    public MajorResponse updateMajor(Long id, MajorRequest majorRequest) {
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

    @Override
    public void deleteMajor(Long id) {
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
