package com.grd.gradingbe.controller;

import com.grd.gradingbe.annotation.RateLimited;
import com.grd.gradingbe.configuration.FileUploadConfig;
import com.grd.gradingbe.dto.entity.FileMetadata;
import com.grd.gradingbe.service.S3StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/s3", produces = {MediaType.APPLICATION_JSON_VALUE})
public class S3Controller {

    private final S3StorageService s3StorageService;
    private final FileUploadConfig fileUploadConfig;
    
    @PostMapping("/uploads")
    @RateLimited(maxRequests = 10, timeWindowMinutes = 1, message = "Too many upload requests. Please try again later.")
    public ResponseEntity<?> uploadImage(@RequestPart(value = "images") List<MultipartFile> files,
                                        HttpServletRequest request) {
        String clientIp = getClientIpAddress(request);
        
        // Validate request
        ResponseEntity<?> validationResult = validateUploadRequest(files);
        if (validationResult != null) {
            return validationResult;
        }
        
        try {
            List<FileMetadata> uploadedFiles = s3StorageService.uploadFiles(files);
            log.info("Successfully uploaded {} files from IP: {}", files.size(), clientIp);
            return ResponseEntity.ok(uploadedFiles);
        } catch (Exception e) {
            log.error("Error uploading files from IP {}: {}", clientIp, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to upload files. Please try again."));
        }
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
    
    private ResponseEntity<?> validateUploadRequest(List<MultipartFile> files) {
        // Check number of files
        if (files == null || files.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "No files provided"));
        }
        
        if (files.size() > fileUploadConfig.getMaxFilesPerRequest()) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Too many files. Maximum " + fileUploadConfig.getMaxFilesPerRequest() + " files allowed"));
        }
        
        Set<String> allowedExtensions = Set.of(fileUploadConfig.getAllowedExtensions());
        
        // Validate each file
        for (MultipartFile file : files) {
            // Check file size
            if (file.getSize() > fileUploadConfig.getMaxFileSize()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "File too large: " + file.getOriginalFilename() + 
                                ". Maximum size is " + (fileUploadConfig.getMaxFileSize() / 1024 / 1024) + "MB"));
            }
            
            // Check file extension
            String originalFilename = file.getOriginalFilename();
            if (originalFilename == null || originalFilename.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid filename"));
            }
            
            String extension = getFileExtension(originalFilename).toLowerCase();
            if (!allowedExtensions.contains(extension)) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Invalid file type: " + originalFilename + 
                                ". Allowed types: " + String.join(", ", allowedExtensions)));
            }
            
            // Check for empty files
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Empty file: " + originalFilename));
            }
        }
        
        return null; // All validations passed
    }
    
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        return lastDotIndex > 0 ? filename.substring(lastDotIndex + 1) : "";
    }

}
