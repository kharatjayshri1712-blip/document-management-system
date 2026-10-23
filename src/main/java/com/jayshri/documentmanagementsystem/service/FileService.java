package com.jayshri.documentmanagementsystem.service;

import com.jayshri.documentmanagementsystem.dto.FileResponseDto;
import com.jayshri.documentmanagementsystem.exception.FileNotFoundCustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import com.jayshri.documentmanagementsystem.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jayshri.documentmanagementsystem.repository.FileMetadataRepository;
import com.jayshri.documentmanagementsystem.entity.FileMetadata;

@Service
@RequiredArgsConstructor
public class FileService {

    private static final String UPLOAD_DIR =
            System.getProperty("user.dir") + "/uploads/";
    private final S3Service s3Service;
    private final FileMetadataRepository fileMetadataRepository;


    public FileResponseDto uploadFile(MultipartFile file) {

        try {
            // Step 1: Validate the uploaded file
            if (file == null || file.isEmpty()) {
                throw new IllegalArgumentException(
                        "Uploaded file cannot be empty");
            }

            String fileName = file.getOriginalFilename();

            if (fileName == null || fileName.isBlank()) {
                throw new IllegalArgumentException(
                        "File name cannot be empty");
            }

            // Step 2: Create the uploads directory if needed
            File directory = new File(UPLOAD_DIR);

            if (!directory.exists() && !directory.mkdirs()) {
                throw new IOException(
                        "Could not create uploads directory");
            }

            // Step 3: Prevent overwriting an existing file
            File destinationFile = new File(directory, fileName);

            if (destinationFile.exists()) {
                throw new IllegalArgumentException(
                        "File already exists: " + fileName);
            }

            // Step 4: Save the file locally
            logger.info("Uploading file: {}", fileName);

            file.transferTo(destinationFile);

            // Step 5: Save file metadata in the database
            FileMetadata fileMetadata = new FileMetadata();

            fileMetadata.setFileName(fileName);
            fileMetadata.setFileType(file.getContentType());
            fileMetadata.setFileSize(file.getSize());
            fileMetadata.setUploadTime(LocalDateTime.now());

            fileMetadataRepository.save(fileMetadata);

            logger.info("File uploaded successfully: {}", fileName);

            // Step 6: Return success response
            return new FileResponseDto(
                    "File uploaded successfully",
                    fileName,
                    "SUCCESS"
            );

        } catch (IOException e) {
            logger.error("File upload failed", e);

            throw new RuntimeException("File upload failed", e);
        }
    }

    public File downloadFile(String fileName) {

        String filePath = UPLOAD_DIR + fileName;

        File file = new File(filePath);

        if (!file.exists()) {

            throw new FileNotFoundCustomException(
                    "File not found: " + fileName);
        }

        return file;
    }
    public List<String> getAllFiles() {

        File folder = new File(UPLOAD_DIR);

        File[] files = folder.listFiles();

        if (files == null) {
            return List.of();
        }

        return Arrays.stream(files)
                .map(File::getName)
                .toList();
    }
    private static final Logger logger =
            LoggerFactory.getLogger(FileService.class);


    public String deleteFile(String fileName) {

        File file = new File(UPLOAD_DIR + fileName);

        if (!file.exists()) {
            throw new FileNotFoundCustomException(
                    "File not found: " + fileName);
        }

        boolean deleted = file.delete();

        if (!deleted) {
            throw new RuntimeException(
                    "File deletion failed");
        }

        // Delete the corresponding metadata from the database
        FileMetadata metadata =
                fileMetadataRepository.findByFileName(fileName);

        if (metadata != null) {
            fileMetadataRepository.delete(metadata);
        }

        logger.info("File deleted successfully: {}", fileName);

        return "File deleted successfully";
    }
    public List<FileMetadata> getAllFileMetadata() {
        return fileMetadataRepository.findAll();
    }
}