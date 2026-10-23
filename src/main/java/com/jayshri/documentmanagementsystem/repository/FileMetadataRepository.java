package com.jayshri.documentmanagementsystem.repository;

import com.jayshri.documentmanagementsystem.entity.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileMetadataRepository
        extends JpaRepository<FileMetadata, Long> {

    FileMetadata findByFileName(String fileName);
}