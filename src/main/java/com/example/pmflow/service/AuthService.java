package com.example.pmflow.service;

import com.example.pmflow.dto.AuthRequest;
import com.example.pmflow.dto.AuthResponse;
import com.example.pmflow.dto.RegisterRequest;
import com.example.pmflow.entity.Role;
import com.example.pmflow.entity.User;
import com.example.pmflow.repository.UserRepository;
import com.example.pmflow.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthResponse login(AuthRequest request) {
        logger.info("Attempting login for username/email: {}", request.getUsernameOrEmail());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsernameOrEmail(),
                            request.getPassword()
                    )
            );

            String token = jwtService.generateToken((org.springframework.security.core.userdetails.UserDetails) authentication.getPrincipal());
            logger.info("Authentication successful for user: {}", request.getUsernameOrEmail());

            return new AuthResponse(token, 86400000); // Token valid for 24h (in ms)
        } catch (Exception e) {
            logger.error("Authentication failed for user: {}", request.getUsernameOrEmail(), e);
            throw e; // Let the exception propagate or customize as needed
        }
    }

    public void register(RegisterRequest request) {
        logger.info("Registering new user: username={}, email={}", request.getUsername(), request.getEmail());

        Optional<User> existingUser = userRepository.findByUsernameOrEmail(request.getUsername(), request.getEmail());
        if (existingUser.isPresent()) {
            logger.warn("User registration failed: username/email already exists - username={}, email={}",
                        request.getUsername(), request.getEmail());
            throw new RuntimeException("User already exists with this username or email");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setRole(Role.MEMBER);

        userRepository.save(user);
        logger.info("User registered successfully: {}", request.getUsername());
    }
}
