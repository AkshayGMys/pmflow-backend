package com.example.pmflow.controller;

import com.example.pmflow.dto.*;
import com.example.pmflow.entity.ProjectStatus;
import com.example.pmflow.service.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    // ✅ Admin: Create new project
    @PostMapping("/create")
    public ResponseEntity<ProjectDetailDTO> createProject(@RequestBody ProjectCreateRequestDTO request) {
        logger.info("[POST] /api/projects/create - Creating new project");
        return ResponseEntity.ok(projectService.createProject(request));
    }

    // ✅ Admin: Get all projects
    @GetMapping("/all")
    public ResponseEntity<List<ProjectSummaryDTO>> getAllProjects() {
        logger.info("[GET] /api/projects/all - Fetching all projects");
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    // ✅ Admin: Get project by ID
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailDTO> getProjectById(@PathVariable Long projectId) {
        logger.info("[GET] /api/projects/{} - Fetching project by ID", projectId);
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    // ✅ Admin: Get project by name (summary/detailed)
    @GetMapping("/by_name")
    public ResponseEntity<?> getProjectByName(@RequestParam String name,
                                              @RequestParam(defaultValue = "false") boolean detailed) {
        logger.info("[GET] /api/projects/by_name - name={}, detailed={}", name, detailed);
        return ResponseEntity.ok(projectService.getProjectByName(name, detailed));
    }

    // ✅ Admin: Update by ID
    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDetailDTO> updateProject(@PathVariable Long projectId,
                                                          @RequestBody ProjectUpdateRequestDTO request) {
        logger.info("[PUT] /api/projects/{} - Updating project", projectId);
        return ResponseEntity.ok(projectService.updateProject(projectId, request));
    }

    // ✅ Admin: Update by name
    @PutMapping("/by_name/{projectName}")
    public ResponseEntity<ProjectDetailDTO> updateProjectByName(@PathVariable String projectName,
                                                                @RequestBody ProjectUpdateRequestDTO request) {
        logger.info("[PUT] /api/projects/by_name/{} - Updating project", projectName);
        return ResponseEntity.ok(projectService.updateProjectByName(projectName, request));
    }

    // ✅ Admin: Delete by ID
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        logger.info("[DELETE] /api/projects/{} - Deleting project", projectId);
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    // ✅ Admin: Delete by name
    @DeleteMapping("/by_name/{projectName}")
    public ResponseEntity<Void> deleteProjectByName(@PathVariable String projectName) {
        logger.info("[DELETE] /api/projects/by_name/{} - Deleting project", projectName);
        projectService.deleteProjectByName(projectName);
        return ResponseEntity.noContent().build();
    }

    // ✅ Manager: Get assigned projects (summary)
    @GetMapping("/manager/{username}")
    public ResponseEntity<List<ProjectSummaryDTO>> getProjectsByManager(@PathVariable String username) {
        logger.info("[GET] /api/projects/manager/{} - Fetching manager's projects", username);
        return ResponseEntity.ok(projectService.getProjectsByManager(username));
    }

    // ✅ Manager: Filter assigned projects (name/status/endDate are optional)
    @GetMapping("/manager/{username}/filter")
    public ResponseEntity<List<ProjectSummaryDTO>> filterProjectsForManager(@PathVariable String username,
                                                                            @RequestParam(required = false) String projectName,
                                                                            @RequestParam(required = false) String status,
                                                                            @RequestParam(required = false) String endDate) {
        logger.info("[GET] /api/projects/manager/{}/filter - Filtering own projects", username);
        return ResponseEntity.ok(projectService.filterProjectsByManager(username, projectName, status, endDate));
    }

    // ✅ Manager: Get project details (only if manager is assigned)
    @GetMapping("/manager/{username}/by_name")
    public ResponseEntity<ProjectDetailDTO> getProjectByNameForManager(@PathVariable String username,
                                                                       @RequestParam String name) {
        logger.info("[GET] /api/projects/manager/{}/by_name?name={} - Getting project detail for manager", username, name);
        return ResponseEntity.ok(projectService.getProjectByNameForManager(name, username));
    }

    // ✅ Admin: Filter all projects
    @GetMapping("/filter")
    public ResponseEntity<List<ProjectSummaryDTO>> filterProjects(@RequestParam(required = false) String projectName,
                                                                  @RequestParam(required = false) String managerName,
                                                                  @RequestParam(required = false) String status,
                                                                  @RequestParam(required = false) String endDate) {
        logger.info("[GET] /api/projects/filter - Filtering projects");
        return ResponseEntity.ok(projectService.filterProjects(projectName, managerName, status, endDate));
    }

    // ✅ Manager: Count own projects by status
    @GetMapping("/manager/{username}/count")
    public ResponseEntity<Long> countProjectsByManagerAndStatus(@PathVariable String username,
                                                                @RequestParam ProjectStatus status) {
        logger.info("[GET] /api/projects/manager/{}/count?status={} - Counting manager's projects", username, status);
        return ResponseEntity.ok(projectService.countProjectsByManagerAndStatus(username, status));
    }

    // ✅ Admin: Count all projects by status
    @GetMapping("/count")
    public ResponseEntity<Long> countProjectsByStatus(@RequestParam ProjectStatus status) {
        logger.info("[GET] /api/projects/count?status={} - Counting all projects by status", status);
        return ResponseEntity.ok(projectService.countProjectsByStatus(status));
    }
}
