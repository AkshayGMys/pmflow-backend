package com.example.pmflow.controller;


import com.example.pmflow.dto.AdminUpdateUserRequest;
import com.example.pmflow.dto.UserDTO;
import com.example.pmflow.entity.User;
import com.example.pmflow.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    // Get current user's profile
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        logger.info("Fetching profile for current user: {}", userDetails.getUsername());
        UserDTO user = userService.getUserProfile(userDetails.getUsername());
        logger.debug("Fetched user profile: {}", user);
        return ResponseEntity.ok(user);
    }

    // Update current user's profile (firstName, lastName, email)
    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserDTO updatedData) {
        logger.info("Updating profile for user: {}", userDetails.getUsername());
        logger.debug("Update data received: {}", updatedData);
        UserDTO updatedUser = userService.updateUserProfile(userDetails.getUsername(), updatedData);
        logger.debug("Updated user profile: {}", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }

    // Admin-only: get all users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        logger.info("Admin fetching all users");
        List<UserDTO> users = userService.getAllUsers();
        logger.debug("Fetched users: {}", users);
        return ResponseEntity.ok(users);
    }

    // Get users by role
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String role) {
        logger.info("Fetching users with role: {}", role);
        List<UserDTO> usersByRole = userService.getUsersByRole(role);
        logger.debug("Users with role {}: {}", role, usersByRole);
        return ResponseEntity.ok(usersByRole);
    }

    // Admin-only: update user by ID
    @PutMapping("/admin/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUserByAdmin(@PathVariable Long userId,
                                                     @RequestBody AdminUpdateUserRequest request) {
        logger.info("Admin updating user with ID: {}", userId);
        logger.debug("Admin update request: {}", request);
        UserDTO updatedUser = userService.adminUpdateUser(userId, request);
        logger.debug("Updated user: {}", updatedUser);
        return ResponseEntity.ok(updatedUser);
    }
}
