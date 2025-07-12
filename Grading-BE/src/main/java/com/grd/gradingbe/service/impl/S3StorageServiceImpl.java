package com.grd.gradingbe.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.grd.gradingbe.dto.entity.FileMetadata;
import com.grd.gradingbe.service.S3StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3StorageServiceImpl implements S3StorageService {

    @Value("${aws.s3.bucketName}")
    private String BUCKET_NAME;

    @Value("${aws.s3.endpoint}")
    private String urlStorage;

    private final AmazonS3 amazonS3;

    private final Tika tika = new Tika();

    @Override
    public Bucket createBucket(String bucketName) {
        amazonS3.doesBucketExistV2(bucketName);
        return amazonS3.createBucket(bucketName);
    }

    @Override
    public FileMetadata uploadFile(MultipartFile image) {
        if (image == null) {
            return null;
        }

        String fileKey = "grad-" + image.getOriginalFilename();
        if (Objects.requireNonNull(image.getOriginalFilename()).contains(fileKey)) {
            fileKey = image.getOriginalFilename();
        }

        return putByMultipartFile(BUCKET_NAME, fileKey, image, true);
    }

    @Override
    public List<FileMetadata> uploadFiles(List<MultipartFile> files) {
        List<FileMetadata> fileMetadata = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileKey = "grad-" + file.getOriginalFilename();
            if (Objects.requireNonNull(file.getOriginalFilename()).contains(fileKey)) {
                fileKey = file.getOriginalFilename();
            }
            fileMetadata.add(putByMultipartFile(BUCKET_NAME, fileKey, file, true));
        }

        return fileMetadata;
    }

    public FileMetadata putByMultipartFile(String bucket, String key, MultipartFile file, Boolean publicAccess) {
        FileMetadata metadata = FileMetadata.builder()
                .bucket(bucket)
                .key(key)
                .name(file.getOriginalFilename())
                .extension(StringUtils.getFilenameExtension(file.getOriginalFilename()))
                .mime(tika.detect(file.getOriginalFilename()))
                .size(file.getSize())
                .build();

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(metadata.getSize());
        objectMetadata.setContentType(metadata.getMime());

        log.info("Uploading file to S3: {}", metadata.getName());

        try {
            InputStream stream = file.getInputStream();
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, key, stream, objectMetadata);
            PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);
            metadata.setUrl(amazonS3.getUrl(bucket, key).toString());
            metadata.setHash(putObjectResult.getContentMd5());
            metadata.setEtag(putObjectResult.getETag());
            metadata.setPublicAccess(publicAccess);
            stream.close();
        } catch (IOException e) {
            log.error("Error uploading file to S3", e);
        }
        return metadata;
    }

    @Override
    public void deleteFile(String keyName) {
        try {
            log.info("Deleting file from S3: {}", keyName);

            if (keyName == null) {
                return;
            } else if (keyName.contains(urlStorage)) {
                keyName = keyName.replace(urlStorage, "");
            }

            log.info("Deleting file from S3: {}", keyName);

            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(BUCKET_NAME, keyName);
            amazonS3.deleteObject(deleteObjectRequest);
        } catch (AmazonServiceException e) {

            log.error("Error deleting file from S3", e);

        }

        log.info("File deleted from S3: {}", keyName);
    }

    @Override
    public FileMetadata uploadByUrl(String url, String folder) throws IOException {
        if (url == null || url.isEmpty()) {
            return null;
        }

        log.info("Uploading image from URL to S3: {}", url);

        try {
            // Open connection to the URL
            URL imageUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
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

            // Configure the request to upload to S3
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(contentType != null ? contentType : "image/jpeg");
            metadata.setContentLength(imageBytes.length);

            // Create put request
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                    BUCKET_NAME,
                    key,
                    new ByteArrayInputStream(imageBytes),
                    metadata
            );

            // Upload to S3
            PutObjectResult putObjectResult = amazonS3.putObject(putObjectRequest);

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
                    .url(amazonS3.getUrl(BUCKET_NAME, key).toString())
                    .hash(putObjectResult.getContentMd5())
                    .etag(putObjectResult.getETag())
                    .publicAccess(true)
                    .build();

        } catch (Exception e) {
            log.error("Error uploading URL image to S3: {}", e.getMessage());
            throw new IOException("Failed to upload URL image to S3: " + e.getMessage(), e);
        }
    }

    @Override
    public String getPublicUrl(String s3Url) {
        if (s3Url == null || s3Url.trim().isEmpty()) {
            return null;
        }

        // Nếu đã là URL đầy đủ, trả về nguyên trạng
        if (s3Url.startsWith("http://") || s3Url.startsWith("https://")) {
            return s3Url;
        }

        // Xử lý các trường hợp s3Url có thể là key hoặc đường dẫn đầy đủ
        String key = s3Url;

        // Nếu URL chứa tên bucket, trích xuất phần key
        if (s3Url.contains(BUCKET_NAME)) {
            // Trích xuất phần key từ URL chứa tên bucket
            int bucketIndex = s3Url.indexOf(BUCKET_NAME);
            if (bucketIndex + BUCKET_NAME.length() < s3Url.length()) {
                // +1 để bỏ qua dấu '/' sau tên bucket
                key = s3Url.substring(bucketIndex + BUCKET_NAME.length() + 1);
            }
        }

        try {
            // Kiểm tra xem key đã được chuẩn hóa chưa
            if (key.startsWith("/")) {
                key = key.substring(1);
            }

            // Tạo URL công khai
            String publicUrl;
            if (urlStorage.endsWith("/")) {
                publicUrl = urlStorage + key;
            } else {
                publicUrl = urlStorage + "/" + key;
            }

            log.info("Generated public URL for key {}: {}", key, publicUrl);

            return publicUrl;
        } catch (Exception e) {
            log.error("Error generating public URL for key {}: {}", key, e.getMessage());
            return urlStorage + "/" + key;
        }
    }

    private String generateKey(String folder, String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String randomId = UUID.randomUUID().toString().substring(0, 8);
        return folder + "/" + timestamp + "-" + randomId + "." + extension;
    }
}