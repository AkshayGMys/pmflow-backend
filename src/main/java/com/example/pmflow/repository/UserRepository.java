package com.example.pmflow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pmflow.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	  Optional<User> findByUsernameOrEmail(String username, String email);
	}