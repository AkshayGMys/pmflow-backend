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

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Get current user's profile
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserDTO user = userService.getUserProfile(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    // Update current user's profile (firstName, lastName, email)
    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserDTO updatedData) {
        UserDTO updatedUser = userService.updateUserProfile(userDetails.getUsername(), updatedData);
        return ResponseEntity.ok(updatedUser);
    }

    // Admin-only: get all users
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }
    
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserDTO>> getUsersByRole(@PathVariable String role) {
        return ResponseEntity.ok(userService.getUsersByRole(role));
    }
    @PutMapping("/admin/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> updateUserByAdmin(@PathVariable Long userId,
                                                     @RequestBody AdminUpdateUserRequest request) {
        return ResponseEntity.ok(userService.adminUpdateUser(userId, request));
    }
}
