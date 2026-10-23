package com.jayshri.documentmanagementsystem.controller;

import com.jayshri.documentmanagementsystem.dto.LoginRequest;
import com.jayshri.documentmanagementsystem.dto.RegisterRequest;
import com.jayshri.documentmanagementsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(
            @RequestBody RegisterRequest request) {

        return authService.register(request);
    }
    @PostMapping("/login")
    public String login(
            @RequestBody LoginRequest request) {

        return authService.login(request);
    }
}