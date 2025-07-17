package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.request.ProvinceRequest;
import com.grd.gradingbe.dto.response.ApiResponse;
import com.grd.gradingbe.dto.response.ProvinceResponse;
import com.grd.gradingbe.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provinces")
@RequiredArgsConstructor
@Slf4j
public class ProvinceController {

    private final ProvinceService provinceService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProvinceResponse>>> getAllProvinces() {
        List<ProvinceResponse> provinces = provinceService.getAllProvinces();
        return ResponseEntity.ok(ApiResponse.success("Fetched province list successfully", provinces));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProvinceResponse>> getProvinceById(@PathVariable Long id) {
        return provinceService.getProvinceById(id)
                .map(province -> ResponseEntity.ok(ApiResponse.success("Fetched province successfully", province)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Province not found with ID = " + id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ProvinceResponse>> createProvince(@RequestBody ProvinceRequest provinceRequest) {
        ProvinceResponse createdProvince = provinceService.createProvince(provinceRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Province created successfully", createdProvince));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProvinceResponse>> updateProvince(
            @PathVariable Long id,
            @RequestBody ProvinceRequest provinceRequest) {
        return provinceService.updateProvince(id, provinceRequest)
                .map(updatedProvince -> ResponseEntity.ok(ApiResponse.success("Province updated successfully", updatedProvince)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ApiResponse.error("Province not found with ID = " + id)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProvince(@PathVariable Long id) {
        try {
            provinceService.deleteProvince(id);
            return ResponseEntity.ok(ApiResponse.success("Province deleted successfully", null));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            log.error("Error while deleting province", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error("Failed to delete province"));
        }
    }
}
