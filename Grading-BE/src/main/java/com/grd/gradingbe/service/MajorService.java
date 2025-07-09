package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.request.MajorRequest;
import com.grd.gradingbe.dto.response.MajorResponse;
import com.grd.gradingbe.dto.response.PageResponse;

public interface MajorService {
    PageResponse<MajorResponse> getAllMajors(int page, int size, String sortBy, String sortDir, String search);
    MajorResponse getMajorById(Long id);
    MajorResponse createMajor(MajorRequest majorRequest);
    MajorResponse updateMajor(Long id, MajorRequest majorRequest);
    void deleteMajor(Long id);
}
