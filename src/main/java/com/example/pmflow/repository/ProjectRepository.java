package com.example.pmflow.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.pmflow.entity.Project;
import com.example.pmflow.entity.ProjectStatus;
import com.example.pmflow.entity.User;

public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    // 🔍 Admin: Find project by exact name
    Optional<Project> findByName(String name);

    // 🔍 Admin: Filter projects by name (partial match)
    List<Project> findByNameContainingIgnoreCase(String name);

    // 🔍 Admin: Filter by status
    List<Project> findByStatus(ProjectStatus status);

    // 🔍 Admin: Filter by endDate
    List<Project> findByEndDate(LocalDate endDate);

    // 🔍 Admin & Manager: Find by project manager username
    List<Project> findByManagerUsername(String username);

    // 🔍 Manager: Get all projects by manager object
    List<Project> findByManager(User manager);

    // 🔢 Admin/Manager: Get count of projects by status
    Long countByStatus(ProjectStatus status);

    // 🔢 Manager: Count projects managed by a manager
    Long countByManager(User manager);
    
   
    
}
