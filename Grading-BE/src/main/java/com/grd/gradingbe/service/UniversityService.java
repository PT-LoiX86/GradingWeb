package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.UniversityRequest;
import com.grd.gradingbe.dto.response.PageResponse;
import com.grd.gradingbe.dto.response.UniversityResponse;

public interface UniversityService {
    PageResponse<UniversityResponse> getAllUniversities(int page, int size, String sortBy, String sortDir, String search);
    UniversityResponse getUniversityById(Long id);
    UniversityResponse createUniversity(UniversityRequest universityRequest);
    UniversityResponse updateUniversity(Long id, UniversityRequest universityRequest);
    void deleteUniversity(Long id);
}
