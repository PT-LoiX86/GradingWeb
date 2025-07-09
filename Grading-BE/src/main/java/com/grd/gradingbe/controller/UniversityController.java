package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.request.UniversityRequest;
import com.grd.gradingbe.dto.response.ApiResponse;
import com.grd.gradingbe.dto.response.PageResponse;
import com.grd.gradingbe.dto.response.UniversityResponse;
import com.grd.gradingbe.service.UniversityService;
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
@RequestMapping(value = "/api/universities", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = "University Management", description = "API endpoints for managing universities")
public class UniversityController {
    private final UniversityService universityService;

    @Operation(
            summary = "Get all universities",
            description = "Retrieve a paginated list of all universities with optional search and sorting capabilities"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved all universities",
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
    public ResponseEntity<ApiResponse<PageResponse<UniversityResponse>>> getAllUniversities(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort by field", example = "name")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc/desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir,
            @Parameter(description = "Search keyword for university name or code")
            @RequestParam(required = false) String search
    ) {
        PageResponse<UniversityResponse> universityPageResponse = universityService.getAllUniversities(page, size, sortBy, sortDir, search);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                "Successfully retrieved all universities", universityPageResponse
        ));
    }

    @Operation(
            summary = "Get university by ID",
            description = "Retrieve a specific university by its unique identifier"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully retrieved university",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "University not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UniversityResponse>> getUniversityById(
            @Parameter(description = "University ID", required = true, example = "1")
            @PathVariable Long id) {
        UniversityResponse universityResponse = universityService.getUniversityById(id);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                "Successfully retrieved university", universityResponse
        ));
    }

    @Operation(
            summary = "Create a new university",
            description = "Create a new university with the provided information"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Successfully created university",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiResponse.class)
                    )
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "400",
                    description = "Invalid input data"
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<UniversityResponse>> createUniversity(
            @Parameter(description = "University request data", required = true)
            @Valid @RequestBody UniversityRequest universityRequest) {
        UniversityResponse universityResponse = universityService.createUniversity(universityRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(
                "Successfully created university", universityResponse
        ));
    }

    @Operation(
            summary = "Update an existing university",
            description = "Update an existing university with the provided information"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "Successfully updated university",
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
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<UniversityResponse>> updateUniversity(
            @Parameter(description = "University ID", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "University request data", required = true)
            @Valid @RequestBody UniversityRequest universityRequest
    ) {
        UniversityResponse universityResponse = universityService.updateUniversity(id, universityRequest);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(
                "Successfully updated university", universityResponse
        ));
    }

    @Operation(
            summary = "Delete a university",
            description = "Delete an existing university by its ID"
    )
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "204",
                    description = "Successfully deleted university"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "404",
                    description = "University not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUniversity(
            @Parameter(description = "University ID", required = true, example = "1")
            @PathVariable Long id) {
        universityService.deleteUniversity(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ApiResponse.success(
                "Successfully deleted university", null
        ));
    }
}
