package com.jayshri.documentmanagementsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileResponseDto {

    private String message;

    private String fileName;

    private String status;
}