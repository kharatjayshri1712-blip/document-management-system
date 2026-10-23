package com.jayshri.documentmanagementsystem.exception;

public class FileNotFoundCustomException
        extends RuntimeException {

    public FileNotFoundCustomException(String message) {
        super(message);
    }
}