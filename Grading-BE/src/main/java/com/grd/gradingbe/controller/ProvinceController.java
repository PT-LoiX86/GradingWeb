package com.grd.gradingbe.controller;

import com.grd.gradingbe.dto.request.ProvinceRequest;
import com.grd.gradingbe.dto.response.ProvinceResponse;
import com.grd.gradingbe.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/provinces")
@RequiredArgsConstructor
public class ProvinceController {

    private final ProvinceService provinceService;

    @GetMapping
    public ResponseEntity<List<ProvinceResponse>> getAllProvinces() {
        List<ProvinceResponse> provinces = provinceService.getAllProvinces();
        return new ResponseEntity<>(provinces, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProvinceResponse> getProvinceById(@PathVariable Long id) {
        return provinceService.getProvinceById(id)
                .map(province -> new ResponseEntity<>(province, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<ProvinceResponse> createProvince(@RequestBody ProvinceRequest provinceRequest) {
        ProvinceResponse createdProvince = provinceService.createProvince(provinceRequest);
        return new ResponseEntity<>(createdProvince, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProvinceResponse> updateProvince(@PathVariable Long id, @RequestBody ProvinceRequest provinceRequest) {
        return provinceService.updateProvince(id, provinceRequest)
                .map(updatedProvince -> new ResponseEntity<>(updatedProvince, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProvince(@PathVariable Long id) {
        provinceService.deleteProvince(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}