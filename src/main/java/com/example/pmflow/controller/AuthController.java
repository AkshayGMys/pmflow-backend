package com.example.pmflow.controller;

import com.example.pmflow.dto.AuthRequest;
import com.example.pmflow.dto.AuthResponse;
import com.example.pmflow.dto.RegisterRequest;
import com.example.pmflow.security.TokenBlacklistService;
import com.example.pmflow.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @Autowired
    private AuthService authService;

    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        logger.info("Registering user: {}", request.getUsername());
        try {
            authService.register(request);
            logger.info("User registered successfully: {}", request.getUsername());
            return ResponseEntity.ok("User registered successfully.");
        } catch (RuntimeException e) {
            logger.error("Registration failed: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        logger.info("Login attempt for: {}", request.getUsernameOrEmail());
        AuthResponse response = authService.login(request);
        logger.info("Login successful for: {}", request.getUsernameOrEmail());
        return ResponseEntity.ok(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            tokenBlacklistService.blacklistToken(token);
            logger.info("Logout successful, token blacklisted.");
            return ResponseEntity.ok("Logged out successfully.");
        } else {
            logger.warn("Logout failed due to missing/invalid header.");
            return ResponseEntity.badRequest().body("Invalid or missing Authorization header.");
        }
    }

}
