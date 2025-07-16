package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.enums.ContentType;
import com.grd.gradingbe.dto.enums.ReasonType;
import com.grd.gradingbe.dto.request.ReportRequest;
import com.grd.gradingbe.dto.response.PageResponse;
import com.grd.gradingbe.dto.response.ReportResponse;
import com.grd.gradingbe.exception.ResourceNotFoundException;
import com.grd.gradingbe.model.Report;
import com.grd.gradingbe.repository.ReportRepository;
import com.grd.gradingbe.service.ReportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReportServiceImpl implements ReportService
{
    private final ReportRepository reportRepository;

    public ReportServiceImpl(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    public PageResponse<ReportResponse> getReports(int page, int size, String sortBy, String sortDir, ContentType contentType, ReasonType reason)
    {
        Sort sorter = sortBy.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sorter);
        Page<Report> reportPages = reportRepository.findAllByContentTypeAndReason(pageable, contentType, reason);
        List<Report> reportContent = reportPages.getContent();

        return PageResponse.<ReportResponse>builder()
                .content(responseMapping(reportContent))
                .page(reportPages.getNumber())
                .size(reportPages.getSize())
                .totalElements((int) reportPages.getTotalElements())
                .totalPages(reportPages.getTotalPages())
                .last(reportPages.isLast())
                .build();
    }

    public ReportResponse createReport(ReportRequest request)
    {
        Report report = Report.builder()
                .content_id(request.getContentId())
                .content_type(request.getContentType())
                .reason(request.getReason())
                .description(request.getDescription())
                .build();

        return responseMapping(reportRepository.save(report));
    }

    public void deleteReport(Long id)
    {
        reportRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Report", "Id", id.toString()));

        reportRepository.deleteById(id);
    }

    private List<ReportResponse> responseMapping(List<Report> reports)
    {
        return reports.stream()
                .map(this::responseMapping)
                .toList();
    }

    private ReportResponse responseMapping(Report reports)
    {
        return ReportResponse.builder()
                .id(reports.getId())
                .contentId(reports.getContent_id())
                .contentType(reports.getContent_type())
                .reason(reports.getReason())
                .description(reports.getDescription())
                .build();
    }
}
