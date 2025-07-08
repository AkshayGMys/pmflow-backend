package com.example.pmflow.repository;

import com.example.pmflow.entity.Project;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatProjectRepository extends JpaRepository<Project, Long> {

    // ✅ Works for both manager or team member
    @Query("SELECT p FROM Project p " +
           "WHERE p.manager.id = :userId " +
           "OR :userId IN (SELECT u.id FROM p.teamMembers u)")
    List<Project> findProjectsAssignedToUser(@Param("userId") Long userId);
}
