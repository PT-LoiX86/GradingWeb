package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.request.MajorRequest;
import com.grd.gradingbe.dto.response.ApiResponse;
import com.grd.gradingbe.dto.response.MajorResponse;
import com.grd.gradingbe.dto.response.PageResponse;
import com.grd.gradingbe.service.MajorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/api/majors", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "Major Management", description = "API endpoints for managing academic majors")
public class MajorController {

    private final MajorService majorService;

    @Operation(
            summary = "Get all majors",
            description = "Retrieve a paginated list of all majors with optional search and sorting capabilities"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all majors",
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
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<MajorResponse>>> getAllMajors(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field", example = "name")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Search keyword for major name or code")
            @RequestParam(required = false) String search
    ) {
        PageResponse<MajorResponse> majorPageResponse = majorService.getAllMajors(page, size, sortBy, sortDir, search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                "Successfully retrieved all majors", majorPageResponse
        ));
    }

    @Operation(
            summary = "Get major by ID",
            description = "Retrieve a specific major by its unique identifier"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved major",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Major not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<MajorResponse>> getMajorById(
            @Parameter(description = "Major ID", required = true, example = "1")
            @PathVariable Long id) {
        MajorResponse majorResponse = majorService.getMajorById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                "Successfully retrieved major", majorResponse
        ));
    }

    @Operation(
            summary = "Create a new major",
            description = "Create a new academic major with the provided information"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Successfully created major",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "University not found"
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<MajorResponse>> createMajor(
            @Parameter(description = "Major request data", required = true)
            @Valid @RequestBody MajorRequest majorRequest) {
        MajorResponse majorResponse = majorService.createMajor(majorRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Successfully created major", majorResponse
        ));
    }

    @Operation(
            summary = "Update an existing major",
            description = "Update an existing major with the provided information"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully updated major",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Major or University not found"
            )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<MajorResponse>> updateMajor(
            @Parameter(description = "Major ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Major request data", required = true)
            @Valid @RequestBody MajorRequest majorRequest
    ) {
        MajorResponse majorResponse = majorService.updateMajor(id, majorRequest);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                "Successfully updated major", majorResponse
        ));
    }

    @Operation(
            summary = "Delete a major",
            description = "Delete an existing major by its ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Successfully deleted major"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "Major not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteMajor(
            @Parameter(description = "Major ID", required = true, example = "1")
            @PathVariable Long id) {
        majorService.deleteMajor(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(
                "Successfully deleted major", null
        ));
    }

}
