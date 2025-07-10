package com.example.pmflow.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.*;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {}) // ✅ Enable CORS globally
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                // ✅ ADMIN endpoints
                .requestMatchers(HttpMethod.POST, "/api/projects/create").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/projects/all").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/projects/filter").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/projects/count").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/projects/{projectId}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/api/projects/by_name").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/projects/{projectId}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/api/projects/by_name/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/projects/{projectId}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/api/projects/by_name/**").hasRole("ADMIN")

                // ✅ PROJECT_MANAGER endpoints
                .requestMatchers(HttpMethod.GET, "/api/projects/manager/**/filter").hasRole("PROJECT_MANAGER")
                .requestMatchers(HttpMethod.GET, "/api/projects/manager/**/by_name").hasRole("PROJECT_MANAGER")
                .requestMatchers(HttpMethod.GET, "/api/projects/manager/**/team_members/*").hasRole("PROJECT_MANAGER")
                .requestMatchers(HttpMethod.GET, "/api/projects/manager/**/count").hasRole("PROJECT_MANAGER")
                .requestMatchers(HttpMethod.PUT, "/api/projects/manager/**/update_status_enddate/**").hasRole("PROJECT_MANAGER")
                .requestMatchers(HttpMethod.GET, "/api/projects/manager/**").hasRole("PROJECT_MANAGER")

                // ✅ All others require authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("*")); 
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("*"));
        config.setAllowCredentials(false); 

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
