package com.jayshri.documentmanagementsystem.service;

import com.jayshri.documentmanagementsystem.dto.RegisterRequest;
import com.jayshri.documentmanagementsystem.entity.User;
import com.jayshri.documentmanagementsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.jayshri.documentmanagementsystem.dto.LoginRequest;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public String register(RegisterRequest request) {

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(
                passwordEncoder.encode(
                        request.getPassword()));

        user.setRole("USER");

        userRepository.save(user);

        return "User registered successfully";
    }
    public String login(LoginRequest request) {

        User user = userRepository
                .findByUsername(request.getUsername())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        boolean passwordMatches =
                passwordEncoder.matches(
                        request.getPassword(),
                        user.getPassword());

        if (!passwordMatches) {
            throw new RuntimeException("Invalid password");
        }

        return jwtService.generateToken(
                user.getUsername());
    }
}