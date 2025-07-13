package com.example.pmflow.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.pmflow.dto.AdminUpdateUserRequest;
import com.example.pmflow.dto.UserDTO;
import com.example.pmflow.entity.Role;
import com.example.pmflow.entity.User;
import com.example.pmflow.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    // ✅ Get current user's profile
    public UserDTO getUserProfile(String username) {
        logger.info("Fetching profile for username/email: {}", username);
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> {
                    logger.error("User not found for: {}", username);
                    return new UsernameNotFoundException("User not found");
                });

        logger.debug("User profile found: {}", user.getUsername());
        return toDTO(user);
    }

    // ✅ Update current user's profile
    public UserDTO updateUserProfile(String username, UserDTO dto) {
        logger.info("Updating profile for user: {}", username);

        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> {
                    logger.error("User not found for update: {}", username);
                    return new UsernameNotFoundException("User not found");
                });

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());

        User updated = userRepository.save(user);
        logger.info("Profile updated for user: {}", username);
        return toDTO(updated);
    }

    // ✅ Get all users (admin)
    public List<UserDTO> getAllUsers() {
        logger.info("Fetching all users");
        List<UserDTO> users = userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        logger.debug("Total users fetched: {}", users.size());
        return users;
    }

    // ✅ Get users by role
    public List<UserDTO> getUsersByRole(String role) {
        logger.info("Fetching users with role: {}", role);
        Role enumRole;
        try {
            enumRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException ex) {
            logger.error("Invalid role requested: {}", role);
            throw new RuntimeException("Invalid role: " + role);
        }

        List<User> users = userRepository.findByRole(enumRole);
        logger.debug("Found {} users with role {}", users.size(), role);
        return users.stream().map(this::toDTO).collect(Collectors.toList());
    }

    // ✅ Update user by admin
    public UserDTO adminUpdateUser(Long userId, AdminUpdateUserRequest request) {
        logger.info("Admin updating user ID: {}", userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> {
                    logger.error("User not found with ID: {}", userId);
                    return new RuntimeException("User not found");
                });

        if (request.getFirstName() != null) {
            user.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            user.setLastName(request.getLastName());
        }

        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }

        User updated = userRepository.save(user);
        logger.info("User updated successfully with ID: {}", userId);
        return toDTO(updated);
    }

    // ✅ Helper: Convert User to UserDTO
    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        return dto;
    }

}
