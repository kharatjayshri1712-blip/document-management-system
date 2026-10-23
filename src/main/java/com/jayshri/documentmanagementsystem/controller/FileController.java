package com.jayshri.documentmanagementsystem.controller;

import com.jayshri.documentmanagementsystem.dto.FileResponseDto;
import com.jayshri.documentmanagementsystem.entity.FileMetadata;
import com.jayshri.documentmanagementsystem.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(
            summary = "Upload a file",
            description = "Upload a file to the document management system"
    )
    @ApiResponse(
            responseCode = "200",
            description = "File uploaded successfully",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(implementation = FileResponseDto.class)
            )
    )
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public FileResponseDto uploadFile(
            @RequestParam("file") MultipartFile file) {

        return fileService.uploadFile(file);
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<InputStreamResource> downloadFile(
            @PathVariable String fileName) throws FileNotFoundException {

        File file = fileService.downloadFile(fileName);

        InputStreamResource resource =
                new InputStreamResource(new FileInputStream(file));

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment;filename=" + file.getName()
                )
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length())
                .body(resource);
    }

    @GetMapping
    public List<String> getAllFiles() {
        return fileService.getAllFiles();
    }

    @DeleteMapping("/{fileName}")
    public String deleteFile(@PathVariable String fileName) {
        return fileService.deleteFile(fileName);
    }

    @GetMapping("/metadata")
    public List<FileMetadata> getAllMetadata() {
        return fileService.getAllFileMetadata();
    }
}