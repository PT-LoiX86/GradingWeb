package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.enums.ContentType;
import com.grd.gradingbe.dto.enums.ReasonType;
import com.grd.gradingbe.dto.request.ReportRequest;
import com.grd.gradingbe.dto.response.ApiResponse;
import com.grd.gradingbe.dto.response.PageResponse;
import com.grd.gradingbe.dto.response.ReportResponse;
import com.grd.gradingbe.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/reports", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Reports Management", description = "API endpoints for managing reports")
public class ReportController
{
    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @Operation(
            summary = "Get all reports",
            description = "Retrieve a paginated list of all reports"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all reports",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request"
            )
    })
    @GetMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<PageResponse<ReportResponse>>> getReports(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field", example = "name")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Content type", example = "post")
            @RequestParam(required = false) ContentType contentType,
            @Parameter(description = "ReasonType", example = "asc")
            @RequestParam(required = false) ReasonType reason)
    {

        PageResponse<ReportResponse> reportResponse = reportService.getReports(page, size, sortBy, sortDir, contentType, reason);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                "Successfully retrieved all reports", reportResponse
        ));
    }

    @Operation(
            summary = "Create a report",
            description = "Create a report with required information"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Successfully created a report",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid request parameters"
            )
    })
    @PostMapping("")
    public ResponseEntity<ApiResponse<ReportResponse>> createReport(
            @Parameter(description = "Report creating request data", required = true)
            @Valid @RequestBody ReportRequest request)
    {
        ReportResponse reportResponse = reportService.createReport(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Successfully created a report", reportResponse
        ));
    }

    @Operation(
            summary = "Delete a report",
            description = "Delete the provided report"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully delete the report",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Report not found"
            )
    })
    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteReport(
            @Parameter(description = "Report ID", required = true, example = "1")
            @PathVariable Long id)
    {
        reportService.deleteReport(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                "Successfully deleted the report", null
        ));
    }
}
