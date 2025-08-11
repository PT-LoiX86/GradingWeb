package com.grd.gradingbe.service;

import com.grd.gradingbe.dto.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.CreateBucketResponse;

import java.io.IOException;
import java.util.List;

public interface MediaService {
    CreateBucketResponse createBucket(String bucketName);

    List<FileMetadata> uploadFiles(List<MultipartFile> files, String folder) throws IOException;

    void deleteFile(List<String> fileUrl);

    FileMetadata uploadByUrl(String url, String folder) throws IOException;

    List<FileMetadata> uploadByUrls(List<String> urls, String folder);
}