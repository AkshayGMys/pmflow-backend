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

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO getUserProfile(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return toDTO(user);
    }

    public UserDTO updateUserProfile(String username, UserDTO dto) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        userRepository.save(user);
        return toDTO(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDTO).toList();
    }
    
    public List<UserDTO> getUsersByRole(String role) {
        Role enumRole;
        try {
            enumRole = Role.valueOf(role.toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new RuntimeException("Invalid role: " + role);
        }

        List<User> users = userRepository.findByRole(enumRole);
        return users.stream().map(this::toDTO).collect(Collectors.toList());
    }

    private UserDTO toDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setRole(user.getRole());
        dto.setId(user.getId());
        return dto;
    }
    public UserDTO adminUpdateUser(Long userId, AdminUpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

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

        // Convert to DTO
        UserDTO dto = new UserDTO();
        dto.setUsername(updated.getUsername());
        dto.setEmail(updated.getEmail());
        dto.setFirstName(updated.getFirstName());
        dto.setLastName(updated.getLastName());
        dto.setRole(updated.getRole());
        return dto;
    }

}
