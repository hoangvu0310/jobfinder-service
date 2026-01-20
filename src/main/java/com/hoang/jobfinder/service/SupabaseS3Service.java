package com.hoang.jobfinder.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.io.IOException;
import java.time.Duration;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class SupabaseS3Service {
  private S3Client s3Client;

  private String supabaseBucketName;

  private S3Presigner s3Presigner;

  public String uploadFile(MultipartFile file, String folder) throws IOException {
    String key = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();

    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(supabaseBucketName)
        .key(key)
        .contentType(file.getContentType())
        .build();

    s3Client.putObject(putRequest, RequestBody.fromInputStream(
        file.getInputStream(), file.getSize()));
    return key;
  }

  public String updateFile(MultipartFile file, String key) throws IOException {
    PutObjectRequest putRequest = PutObjectRequest.builder()
        .bucket(supabaseBucketName)
        .key(key)
        .contentType(file.getContentType())
        .build();

    s3Client.putObject(putRequest, RequestBody.fromInputStream(
        file.getInputStream(), file.getSize()));
    return key;
  }

  public String generateSignedUrl(String key) {
    GetObjectRequest getRequest = GetObjectRequest.builder()
        .bucket(supabaseBucketName)
        .key(key)
        .build();

    GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
        .signatureDuration(Duration.ofMinutes(10))
        .getObjectRequest(getRequest)
        .build();

    PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
    log.info("Presigned URL: [{}]", presignedRequest.url().toString());
    log.info("HTTP method: [{}]", presignedRequest.httpRequest().method());

    return presignedRequest.url().toExternalForm();

  }

  public void deleteFile(String objectKey) {
    DeleteObjectRequest deleteRequest = DeleteObjectRequest.builder()
        .bucket(supabaseBucketName)
        .key(objectKey)
        .build();

    s3Client.deleteObject(deleteRequest);
  }
}
