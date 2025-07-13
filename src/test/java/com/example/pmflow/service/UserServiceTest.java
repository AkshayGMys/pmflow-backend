package com.example.pmflow.service;

import com.example.pmflow.dto.AdminUpdateUserRequest;
import com.example.pmflow.dto.UserDTO;
import com.example.pmflow.entity.Role;
import com.example.pmflow.entity.User;
import com.example.pmflow.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("akshay");
        user.setEmail("akshay@example.com");
        user.setFirstName("Akshay");
        user.setLastName("G");
        user.setRole(Role.MEMBER);
    }

    // ✅ Get User Profile
    @Test
    void testGetUserProfile() {
        when(userRepository.findByUsernameOrEmail("akshay", "akshay"))
                .thenReturn(Optional.of(user));

        UserDTO dto = userService.getUserProfile("akshay");

        assertEquals("akshay", dto.getUsername());
        assertEquals(Role.MEMBER, dto.getRole());
    }

    // ✅ Update Profile
    @Test
    void testUpdateUserProfile() {
        when(userRepository.findByUsernameOrEmail("akshay", "akshay"))
                .thenReturn(Optional.of(user));

        UserDTO update = new UserDTO();
        update.setFirstName("Updated");
        update.setLastName("Name");
        update.setEmail("new@example.com");

        UserDTO result = userService.updateUserProfile("akshay", update);

        assertEquals("Updated", result.getFirstName());
        assertEquals("new@example.com", result.getEmail());
        verify(userRepository).save(any(User.class));
    }

    // ✅ Get All Users
    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserDTO> users = userService.getAllUsers();

        assertEquals(1, users.size());
        assertEquals("akshay", users.get(0).getUsername());
    }

    // ✅ Get Users by Role
    @Test
    void testGetUsersByRole() {
        when(userRepository.findByRole(Role.MEMBER)).thenReturn(List.of(user));

        List<UserDTO> users = userService.getUsersByRole("MEMBER");

        assertEquals(1, users.size());
        assertEquals(Role.MEMBER, users.get(0).getRole());
    }

    // ❌ Invalid Role Test
    @Test
    void testGetUsersByRole_InvalidRole() {
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                userService.getUsersByRole("invalid"));

        assertTrue(exception.getMessage().contains("Invalid role"));
    }

    // ✅ Admin Update User
    @Test
    void testAdminUpdateUser() {
        AdminUpdateUserRequest request = new AdminUpdateUserRequest();
        request.setFirstName("Admin");
        request.setLastName("Updated");
        request.setRole(Role.PROJECT_MANAGER);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserDTO updated = userService.adminUpdateUser(1L, request);

        assertEquals("akshay", updated.getUsername());
        verify(userRepository).save(any(User.class));
    }
}
