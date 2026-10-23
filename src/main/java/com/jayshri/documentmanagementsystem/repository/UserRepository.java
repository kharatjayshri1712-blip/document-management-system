package com.jayshri.documentmanagementsystem.repository;

import com.jayshri.documentmanagementsystem.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository
        extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
}