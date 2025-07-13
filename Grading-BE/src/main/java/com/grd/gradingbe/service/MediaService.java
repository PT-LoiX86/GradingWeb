package com.grd.gradingbe.service;

import com.amazonaws.services.s3.model.Bucket;
import com.grd.gradingbe.dto.entity.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface MediaService {
    Bucket createBucket(String bucketName);

    List<FileMetadata> uploadFiles(List<MultipartFile> files, String folder) throws IOException;

    void deleteFile(List<String> fileUrl);

    FileMetadata uploadByUrl(String url, String folder) throws IOException;

    List<FileMetadata> uploadByUrls(List<String> urls, String folder);
}