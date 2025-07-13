package com.example.pmflow.controller;

import com.example.pmflow.dto.AdminUpdateUserRequest;
import com.example.pmflow.dto.UserDTO;
import com.example.pmflow.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ /api/users/me
    @Test
    void testGetCurrentUser() {
        when(userDetails.getUsername()).thenReturn("testuser");

        UserDTO mockUser = new UserDTO();
        mockUser.setUsername("testuser");

        when(userService.getUserProfile("testuser")).thenReturn(mockUser);

        ResponseEntity<UserDTO> response = userController.getCurrentUser(userDetails);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("testuser", response.getBody().getUsername());
    }

    // ✅ PUT /api/users/me
    @Test
    void testUpdateUserProfile() {
        when(userDetails.getUsername()).thenReturn("testuser");

        UserDTO update = new UserDTO();
        update.setFirstName("Akshay");

        UserDTO updated = new UserDTO();
        updated.setFirstName("Akshay");

        when(userService.updateUserProfile("testuser", update)).thenReturn(updated);

        ResponseEntity<UserDTO> response = userController.updateProfile(userDetails, update);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Akshay", response.getBody().getFirstName());
    }

    // ✅ GET /api/users (Admin only)
    @Test
    void testGetAllUsers() {
        List<UserDTO> users = List.of(new UserDTO(), new UserDTO());

        when(userService.getAllUsers()).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    // ✅ GET /api/users/role/{role}
    @Test
    void testGetUsersByRole() {
        String role = "MEMBER";
        List<UserDTO> users = List.of(new UserDTO());

        when(userService.getUsersByRole(role)).thenReturn(users);

        ResponseEntity<List<UserDTO>> response = userController.getUsersByRole(role);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
    }

    // ✅ PUT /api/users/admin/{id}
    @Test
    void testAdminUpdateUser() {
        AdminUpdateUserRequest request = new AdminUpdateUserRequest();
        request.setFirstName("Updated Name");

        UserDTO updatedUser = new UserDTO();
        updatedUser.setFirstName("Updated Name");

        when(userService.adminUpdateUser(1L, request)).thenReturn(updatedUser);

        ResponseEntity<UserDTO> response = userController.updateUserByAdmin(1L, request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Name", response.getBody().getFirstName());
    }
}
