package com.grd.gradingbe.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.file-upload")
public class FileUploadConfig {
    
    private int maxRequestsPerMinute = 10;
    private int maxFilesPerRequest = 5;
    private long maxFileSize = 10 * 1024 * 1024; // 10MB
    private String[] allowedExtensions = {"jpg", "jpeg", "png", "gif", "pdf", "doc", "docx"};
    
}
