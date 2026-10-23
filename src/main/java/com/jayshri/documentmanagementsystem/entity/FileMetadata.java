package com.jayshri.documentmanagementsystem.entity;

//import jakarta.persistence.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_metadata")
@Data
public class FileMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;

    private String fileType;

    private Long fileSize;

    private LocalDateTime uploadTime;
}