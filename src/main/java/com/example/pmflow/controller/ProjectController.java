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

    @PostMapping("/create")
    public ResponseEntity<ProjectDetailDTO> createProject(@RequestBody ProjectCreateRequestDTO request) {
        logger.info("[POST] /api/projects/create - Creating new project");
        return ResponseEntity.ok(projectService.createProject(request));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProjectSummaryDTO>> getAllProjects() {
        logger.info("[GET] /api/projects/all - Fetching all projects");
        return ResponseEntity.ok(projectService.getAllProjects());
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectDetailDTO> getProjectById(@PathVariable Long projectId) {
        logger.info("[GET] /api/projects/{} - Fetching project by ID", projectId);
        return ResponseEntity.ok(projectService.getProjectById(projectId));
    }

    @GetMapping("/by_name")
    public ResponseEntity<?> getProjectByName(@RequestParam String name,
                                              @RequestParam(defaultValue = "false") boolean detailed) {
        logger.info("[GET] /api/projects/by_name - name={}, detailed={}", name, detailed);
        return ResponseEntity.ok(projectService.getProjectByName(name, detailed));
    }

    @PutMapping("/{projectId}")
    public ResponseEntity<ProjectDetailDTO> updateProject(@PathVariable Long projectId,
                                                          @RequestBody ProjectUpdateRequestDTO request) {
        logger.info("[PUT] /api/projects/{} - Updating project", projectId);
        return ResponseEntity.ok(projectService.updateProject(projectId, request));
    }

    @PutMapping("/by_name/{projectName}")
    public ResponseEntity<ProjectDetailDTO> updateProjectByName(@PathVariable String projectName,
                                                                @RequestBody ProjectUpdateRequestDTO request) {
        logger.info("[PUT] /api/projects/by_name/{} - Updating project", projectName);
        return ResponseEntity.ok(projectService.updateProjectByName(projectName, request));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long projectId) {
        logger.info("[DELETE] /api/projects/{} - Deleting project", projectId);
        projectService.deleteProject(projectId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/by_name/{projectName}")
    public ResponseEntity<Void> deleteProjectByName(@PathVariable String projectName) {
        logger.info("[DELETE] /api/projects/by_name/{} - Deleting project", projectName);
        projectService.deleteProjectByName(projectName);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/manager/{managerId}")
    public ResponseEntity<List<ProjectSummaryDTO>> getProjectsByManagerId(@PathVariable Long managerId) {
        logger.info("[GET] /api/projects/manager/{} - Fetching manager's projects", managerId);
        return ResponseEntity.ok(projectService.getProjectsByManagerId(managerId));
    }

    @GetMapping("/manager/{managerId}/by_name")
    public ResponseEntity<?> getProjectByNameForManager(@PathVariable Long managerId,
                                                        @RequestParam String name,
                                                        @RequestParam(defaultValue = "false") boolean detailed) {
        logger.info("[GET] /api/projects/manager/{}/by_name?name={}, detailed={} - Getting project for manager", managerId, name, detailed);
        return ResponseEntity.ok(projectService.getProjectByNameForManager(name, managerId, detailed));
    }

    @PutMapping("/manager/{managerId}/update_status_enddate/{projectId}")
    public ResponseEntity<ProjectDetailDTO> updateStatusAndEndDate(@PathVariable Long managerId,
                                                                    @PathVariable Long projectId,
                                                                    @RequestParam(required = false) String endDate,
                                                                    @RequestParam(required = false) String status) {
        logger.info("[PUT] /api/projects/manager/{}/update_status_enddate/{} - Updating status and endDate", managerId, projectId);
        return ResponseEntity.ok(projectService.updateProjectEndDateAndStatusByManager(projectId, managerId, endDate, status));
    }

    @GetMapping("/manager/{managerId}/team_members/{projectId}")
    public ResponseEntity<List<TeamMemberDTO>> getTeamMembers(@PathVariable Long managerId,
                                                               @PathVariable Long projectId) {
        logger.info("[GET] /api/projects/manager/{}/team_members/{} - Fetching team members", managerId, projectId);
        return ResponseEntity.ok(projectService.getTeamMembersOfProject(projectId, managerId));
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProjectSummaryDTO>> filterProjects(@RequestParam(required = false) String projectName,
                                                                  @RequestParam(required = false) String managerName,
                                                                  @RequestParam(required = false) String status,
                                                                  @RequestParam(required = false) String endDate) {
        logger.info("[GET] /api/projects/filter - Filtering projects");
        return ResponseEntity.ok(projectService.filterProjects(projectName, managerName, status, endDate));
    }

    @GetMapping("/count")
    public ResponseEntity<Long> countProjectsByStatus(@RequestParam ProjectStatus status) {
        logger.info("[GET] /api/projects/count?status={} - Counting all projects by status", status);
        return ResponseEntity.ok(projectService.countProjectsByStatus(status));
    }

    // ✅ ADDED: Filter for Project Manager
    @GetMapping("/manager/{managerId}/filter")
    public ResponseEntity<List<ProjectSummaryDTO>> filterProjectsForManager(
            @PathVariable Long managerId,
            @RequestParam(required = false) String projectName,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String endDate) {
        logger.info("[GET] /api/projects/manager/{}/filter - Filtering projects for manager", managerId);
        return ResponseEntity.ok(projectService.filterProjectsByManager(managerId, projectName, status, endDate));
    }

    // ✅ ADDED: Count for Project Manager
    @GetMapping("/manager/{managerId}/count")
    public ResponseEntity<Long> countProjectsByStatusForManager(
            @PathVariable Long managerId,
            @RequestParam ProjectStatus status) {
        logger.info("[GET] /api/projects/manager/{}/count?status={} - Counting projects for manager", managerId, status);
        return ResponseEntity.ok(projectService.countProjectsByStatusForManager(managerId, status));
    }
}
