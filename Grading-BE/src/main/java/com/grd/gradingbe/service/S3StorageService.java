package com.grd.gradingbe.service;

import com.amazonaws.services.s3.model.Bucket;
import com.grd.gradingbe.dto.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface S3StorageService {
    Bucket createBucket(String bucketName);

    FileMetadata uploadFile(MultipartFile file) throws IOException;

    List<FileMetadata> uploadFiles(List<MultipartFile> files) throws IOException;

    void deleteFile(String fileUrl);

    FileMetadata uploadByUrl(String url, String folder) throws IOException;

    String getPublicUrl(String s3Url);

}