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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UniversityServiceImpl implements UniversityService {

    private final UniversityRepository universityRepository;

    @Override
    public PageResponse<UniversityResponse> getAllUniversities(int page, int size, String sortBy, String sortDir, String search) {
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

    @Override
    public UniversityResponse getUniversityById(Long id) {
        University university = universityRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("University", "id", id.toString())
        );

        return mapToUniversityResponse(university);
    }

    @Override
    public UniversityResponse createUniversity(UniversityRequest universityRequest) {
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

    @Override
    public UniversityResponse updateUniversity(Long id, UniversityRequest universityRequest) {
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

    @Override
    public void deleteUniversity(Long id) {
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
