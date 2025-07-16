package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.enums.ContentType;
import com.grd.gradingbe.dto.enums.ReasonType;
import com.grd.gradingbe.dto.request.ReportRequest;
import com.grd.gradingbe.dto.response.PageResponse;
import com.grd.gradingbe.dto.response.ReportResponse;

public interface ReportService
{
    PageResponse<ReportResponse> getReports(int page, int size, String sortBy, String sortDir, ContentType contentType, ReasonType reason);

    ReportResponse createReport(ReportRequest request);

    void deleteReport(Long id);
}
