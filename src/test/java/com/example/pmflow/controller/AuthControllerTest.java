package com.example.pmflow.controller;

import com.example.pmflow.dto.AuthRequest;
import com.example.pmflow.dto.AuthResponse;
import com.example.pmflow.dto.RegisterRequest;
import com.example.pmflow.security.TokenBlacklistService;
import com.example.pmflow.service.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private AuthService authService;

    @Mock
    private TokenBlacklistService tokenBlacklistService;

    @Mock
    private HttpServletRequest httpServletRequest;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@example.com");
        request.setPassword("password");

        doNothing().when(authService).register(any());

        ResponseEntity<?> response = authController.register(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User registered successfully.", response.getBody());
    }

    @Test
    void testRegister_Failure() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");

        doThrow(new RuntimeException("User already exists")).when(authService).register(any());

        ResponseEntity<?> response = authController.register(request);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("User already exists", response.getBody());
    }

    @Test
    void testLogin_Success() {
        AuthRequest request = new AuthRequest();
        request.setUsernameOrEmail("user");
        request.setPassword("pass");

        AuthResponse mockResponse = new AuthResponse("mock-jwt", 3600L);
        when(authService.login(any())).thenReturn(mockResponse);

        ResponseEntity<AuthResponse> response = authController.login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("mock-jwt", response.getBody().getToken());
    }

    @Test
    void testLogout_Success() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Bearer test.jwt.token");

        ResponseEntity<String> response = authController.logout(httpServletRequest);

        verify(tokenBlacklistService).blacklistToken("test.jwt.token");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Logged out successfully.", response.getBody());
    }

    @Test
    void testLogout_BadRequest() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        ResponseEntity<String> response = authController.logout(httpServletRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid or missing Authorization header.", response.getBody());
    }
}
