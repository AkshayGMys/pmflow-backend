package com.example.pmflow.service;

import com.example.pmflow.dto.AuthRequest;
import com.example.pmflow.dto.AuthResponse;
import com.example.pmflow.dto.RegisterRequest;
import com.example.pmflow.entity.Role;
import com.example.pmflow.entity.User;
import com.example.pmflow.repository.UserRepository;
import com.example.pmflow.security.JwtService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // ✅ Test Login Success
    @Test
    void testLoginSuccess() {
        AuthRequest request = new AuthRequest();
        request.setUsernameOrEmail("testuser");
        request.setPassword("password");

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("mock-jwt");

        AuthResponse response = authService.login(request);

        assertEquals("mock-jwt", response.getToken());
        assertEquals(86400000L, response.getExpiresIn());
    }

    // ✅ Test Register New User
    @Test
    void testRegisterSuccess() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("akshay");
        request.setEmail("akshay@example.com");
        request.setPassword("123456");
        request.setFirstName("Akshay");
        request.setLastName("G");

        when(userRepository.findByUsernameOrEmail("akshay", "akshay@example.com"))
                .thenReturn(Optional.empty());
        when(passwordEncoder.encode("123456")).thenReturn("hashed-password");

        authService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("akshay", savedUser.getUsername());
        assertEquals("akshay@example.com", savedUser.getEmail());
        assertEquals("hashed-password", savedUser.getPasswordHash());
        assertEquals(Role.MEMBER, savedUser.getRole());
        assertNotNull(savedUser.getCreatedAt());
    }

    // ❌ Test Register When User Already Exists
    @Test
    void testRegisterUserAlreadyExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existingUser");
        request.setEmail("existing@example.com");

        when(userRepository.findByUsernameOrEmail("existingUser", "existing@example.com"))
                .thenReturn(Optional.of(new User()));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.register(request);
        });

        assertEquals("User already exists with this username or email", exception.getMessage());
    }
}
