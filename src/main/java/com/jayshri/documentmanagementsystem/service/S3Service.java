package com.jayshri.documentmanagementsystem.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public String uploadFileToS3(MultipartFile file) {

        try {

            PutObjectRequest putObjectRequest =
                    PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(file.getOriginalFilename())
                            .build();

            s3Client.putObject(
                    putObjectRequest,
                    RequestBody.fromBytes(file.getBytes()));

            return "File uploaded to S3 successfully";

        } catch (IOException e) {

            throw new RuntimeException(
                    "S3 upload failed");
        }
    }

}