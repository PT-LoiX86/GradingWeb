package com.grd.gradingbe.service.impl;

import com.grd.gradingbe.dto.entity.FileMetadata;
import com.grd.gradingbe.service.MediaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class MediaServiceImpl implements MediaService {

    @Value("${aws.s3.bucketName}")
    private String BUCKET_NAME;

    @Value("${aws.s3.endpoint}")
    private String urlStorage;

    private final S3Client s3Client;

    private final Tika tika = new Tika();

    @Override
    public CreateBucketResponse createBucket(String bucketName) {
        try {
            HeadBucketRequest headBucketRequest = HeadBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            s3Client.headBucket(headBucketRequest);
            return null; // Bucket already exists
        } catch (NoSuchBucketException e) {
            CreateBucketRequest createBucketRequest = CreateBucketRequest.builder()
                    .bucket(bucketName)
                    .build();
            return s3Client.createBucket(createBucketRequest);
        }
    }

    @Override
    public List<FileMetadata> uploadFiles(List<MultipartFile> files, String folder) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }

        log.info("Uploading {} files to S3 in folder: {}", files.size(), folder);

        List<FileMetadata> uploadedFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            if (file != null && !file.isEmpty()) {
                String extension = StringUtils.getFilenameExtension(file.getOriginalFilename());
                String key = generateKey(folder, extension);
                FileMetadata metadata = putByMultipartFile(BUCKET_NAME, key, file);
                uploadedFiles.add(metadata);
            }
        }
        return uploadedFiles;
    }

    @Override
    public void deleteFile(List<String> keyName) {
        if (keyName == null || keyName.isEmpty()) {
            return;
        }

        log.info("Deleting files from S3: {}", keyName);

        try {
            List<ObjectIdentifier> keys = new ArrayList<>();
            for (String key : keyName) {
                keys.add(ObjectIdentifier.builder().key(key).build());
            }

            Delete delete = Delete.builder()
                    .objects(keys)
                    .build();

            DeleteObjectsRequest deleteObjectsRequest = DeleteObjectsRequest.builder()
                    .bucket(BUCKET_NAME)
                    .delete(delete)
                    .build();

            s3Client.deleteObjects(deleteObjectsRequest);
            log.info("Successfully deleted files from S3: {}", keyName);
        } catch (S3Exception e) {
            log.error("Error deleting files from S3: {}", e.getMessage());
        }
    }

    @Override
    public FileMetadata uploadByUrl(String url, String folder) throws IOException {
        if (url == null || url.isEmpty()) {
            return null;
        }

        log.info("Uploading image from URL to S3: {}", url);

        try {
            // Open connection to the URL
            java.net.URI uri = java.net.URI.create(url);
            HttpURLConnection connection = (HttpURLConnection) uri.toURL().openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            // Get content type and determine file extension
            String contentType = connection.getContentType();
            String extension = "jpg"; // Default extension

            if (contentType != null) {
                if (contentType.contains("image/png")) {
                    extension = "png";
                } else if (contentType.contains("image/jpeg") || contentType.contains("image/jpg")) {
                    extension = "jpg";
                } else if (contentType.contains("image/gif")) {
                    extension = "gif";
                } else if (contentType.contains("image/webp")) {
                    extension = "webp";
                } else if (contentType.contains("image/svg+xml")) {
                    extension = "svg";
                }
            }

            // Download image data
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            try (InputStream inputStream = connection.getInputStream()) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            byte[] imageBytes = outputStream.toByteArray();

            // Check if image data is valid
            if (imageBytes.length == 0) {
                throw new IOException("Downloaded image is empty");
            }

            // Generate unique key for S3
            String key = generateKey(folder, extension);

            // Create put request with RequestBody
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .contentType(contentType != null ? contentType : "image/jpeg")
                    .contentLength((long) imageBytes.length)
                    .build();

            // Upload to S3
            PutObjectResponse putObjectResult = s3Client.putObject(putObjectRequest,
                    RequestBody.fromBytes(imageBytes));

            // Generate URL
            String objectUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", 
                    BUCKET_NAME, "ap-southeast-1", key);

            // Log successful upload
            log.info("Successfully uploaded URL image to S3: {}", key);

            // Create and return FileMetadata
            return FileMetadata.builder()
                    .bucket(BUCKET_NAME)
                    .key(key)
                    .name(key.substring(key.lastIndexOf('/') + 1))
                    .extension(extension)
                    .mime(contentType != null ? contentType : "image/jpeg")
                    .size((long) imageBytes.length)
                    .url(objectUrl)
                    .etag(putObjectResult.eTag())
                    .publicAccess(true)
                    .build();

        } catch (Exception e) {
            log.error("Error uploading URL image to S3: {}", e.getMessage());
            throw new IOException("Failed to upload URL image to S3: " + e.getMessage(), e);
        }
    }

    @Override
    public List<FileMetadata> uploadByUrls(List<String> urls, String folder) {
        if (urls == null || urls.isEmpty()) {
            return List.of();
        }

        log.info("Uploading {} files from URLs to S3", urls.size());

        List<FileMetadata> uploadedFiles = new ArrayList<>();
        for (String url : urls) {
            if (url != null && !url.isEmpty()) {
                try {
                    FileMetadata metadata = uploadByUrl(url, folder);
                    if (metadata != null) {
                        uploadedFiles.add(metadata);
                    }
                } catch (IOException e) {
                    log.error("Error uploading file from URL {}: {}", url, e.getMessage());
                }
            }
        }
        return uploadedFiles;
    }

    private FileMetadata putByMultipartFile(String bucket, String key, MultipartFile file) {
        FileMetadata metadata = FileMetadata.builder()
                .bucket(bucket)
                .key(key)
                .name(file.getOriginalFilename())
                .extension(StringUtils.getFilenameExtension(file.getOriginalFilename()))
                .mime(tika.detect(file.getOriginalFilename()))
                .size(file.getSize())
                .build();

        log.info("Uploading file to S3: {}", metadata.getName());

        try {
            // Create put request
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(metadata.getMime())
                    .contentLength(metadata.getSize())
                    .build();

            // Upload to S3
            PutObjectResponse putObjectResult = s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // Generate URL
            String objectUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", 
                    bucket, "ap-southeast-1", key);

            metadata.setUrl(objectUrl);
            metadata.setEtag(putObjectResult.eTag());
            metadata.setPublicAccess(true);

        } catch (IOException e) {
            log.error("Error uploading file to S3", e);
        }
        return metadata;
    }

    private String generateKey(String folder, String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomId = UUID.randomUUID().toString().substring(0, 8);
        return folder + "/" + timestamp + "-" + randomId + "." + extension;
    }
}