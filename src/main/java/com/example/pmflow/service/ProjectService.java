package com.example.pmflow.service;

import com.example.pmflow.dto.*;
import com.example.pmflow.entity.Project;
import com.example.pmflow.entity.ProjectStatus;
import com.example.pmflow.entity.User;
import com.example.pmflow.repository.ProjectRepository;
import com.example.pmflow.repository.UserRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectService {

    private static final Logger logger = LoggerFactory.getLogger(ProjectService.class);

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectService(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProjectDetailDTO createProject(ProjectCreateRequestDTO request) {
        logger.info("Creating project: {}", request.getName());
        User manager = userRepository.findById(request.getManagerId())
                .orElseThrow(() -> new RuntimeException("Manager not found"));
        Set<User> teamMembers = new HashSet<>(userRepository.findAllById(request.getTeamMemberIds()));
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setManager(manager);
        project.setTeamMembers(teamMembers);
        project.setStatus(ProjectStatus.NOT_STARTED);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        project.setEndDate(LocalDate.parse(request.getEndDate(), formatter));
        Project saved = projectRepository.save(project);
        logger.info("Project created with ID: {}", saved.getId());
        return convertToDetailDTO(saved);
    }

    public List<ProjectSummaryDTO> getAllProjects() {
        logger.info("Fetching all projects for admin view");
        return projectRepository.findAll().stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    public ProjectDetailDTO getProjectById(Long projectId) {
        logger.info("Fetching project with ID: {}", projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return convertToDetailDTO(project);
    }

    public Object getProjectByName(String projectName, boolean detailed) {
        logger.info("Fetching project by name: {} (detailed: {})", projectName, detailed);
        Project project = projectRepository.findByName(projectName)
                .orElseThrow(() -> new RuntimeException("Project not found with name: " + projectName));
        return detailed ? convertToDetailDTO(project) : convertToSummaryDTO(project);
    }

    public ProjectDetailDTO getProjectByNameForManager(String projectName, String managerUsername) {
        logger.info("Manager {} requesting project by name: {}", managerUsername, projectName);
        Project project = projectRepository.findByName(projectName)
                .orElseThrow(() -> new RuntimeException("Project not found with name: " + projectName));

        if (!project.getManager().getUsername().equals(managerUsername)) {
            throw new RuntimeException("Access denied: You are not the manager of this project.");
        }
        return convertToDetailDTO(project);
    }

    @Transactional
    public ProjectDetailDTO updateProject(Long projectId, ProjectUpdateRequestDTO request) {
        logger.info("Updating project ID: {}", projectId);
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));
        return updateProjectFields(project, request);
    }

    @Transactional
    public ProjectDetailDTO updateProjectByName(String projectName, ProjectUpdateRequestDTO request) {
        logger.info("Updating project by name: {}", projectName);
        Project project = projectRepository.findByName(projectName)
                .orElseThrow(() -> new RuntimeException("Project not found with name: " + projectName));
        return updateProjectFields(project, request);
    }

    private ProjectDetailDTO updateProjectFields(Project project, ProjectUpdateRequestDTO request) {
        if (request.getName() != null) project.setName(request.getName());
        if (request.getDescription() != null) project.setDescription(request.getDescription());
        if (request.getStatus() != null) project.setStatus(ProjectStatus.valueOf(request.getStatus()));
        if (request.getManagerId() != null) {
            User manager = userRepository.findById(request.getManagerId())
                    .orElseThrow(() -> new RuntimeException("Manager not found"));
            project.setManager(manager);
        }
        if (request.getTeamMemberIds() != null) {
            Set<User> teamMembers = new HashSet<>(userRepository.findAllById(request.getTeamMemberIds()));
            project.setTeamMembers(teamMembers);
        }
        if (request.getEndDate() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            project.setEndDate(LocalDate.parse(request.getEndDate(), formatter));
        }
        Project updated = projectRepository.save(project);
        logger.info("Project updated: {}", updated.getId());
        return convertToDetailDTO(updated);
    }

    @Transactional
    public void deleteProject(Long projectId) {
        logger.warn("Deleting project ID: {}", projectId);
        projectRepository.deleteById(projectId);
    }

    @Transactional
    public void deleteProjectByName(String projectName) {
        logger.warn("Deleting project by name: {}", projectName);
        Project project = projectRepository.findByName(projectName)
                .orElseThrow(() -> new RuntimeException("Project not found with name: " + projectName));
        projectRepository.delete(project);
    }

    public List<ProjectSummaryDTO> getProjectsByManager(String managerUsername) {
        logger.info("Fetching projects for manager: {}", managerUsername);
        List<Project> projects = projectRepository.findByManagerUsername(managerUsername);
        return projects.stream().map(this::convertToSummaryDTO).collect(Collectors.toList());
    }

    public List<ProjectSummaryDTO> filterProjects(String projectName, String managerName, String status, String endDate) {
        logger.info("Filtering projects with criteria - name: {}, manager: {}, status: {}, endDate: {}",
                projectName, managerName, status, endDate);

        return projectRepository.findAll().stream()
                .filter(p -> projectName == null || p.getName().toLowerCase().contains(projectName.toLowerCase()))
                .filter(p -> managerName == null || p.getManager().getUsername().equalsIgnoreCase(managerName))
                .filter(p -> status == null || p.getStatus().name().equalsIgnoreCase(status))
                .filter(p -> {
                    if (endDate == null) return true;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                    return p.getEndDate().equals(LocalDate.parse(endDate, formatter));
                })
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    public List<ProjectSummaryDTO> filterProjectsByManager(String managerUsername, String projectName, String status, String endDate) {
        logger.info("Filtering manager's projects - manager: {}, name: {}, status: {}, endDate: {}",
                managerUsername, projectName, status, endDate);

        return projectRepository.findByManagerUsername(managerUsername).stream()
                .filter(p -> projectName == null || p.getName().toLowerCase().contains(projectName.toLowerCase()))
                .filter(p -> status == null || p.getStatus().name().equalsIgnoreCase(status))
                .filter(p -> {
                    if (endDate == null) return true;
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                    return p.getEndDate().equals(LocalDate.parse(endDate, formatter));
                })
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    public long countProjectsByManagerAndStatus(String managerUsername, ProjectStatus status) {
        logger.info("Counting projects for manager: {} with status: {}", managerUsername, status);
        return projectRepository.findByManagerUsername(managerUsername).stream()
                .filter(p -> p.getStatus() == status)
                .count();
    }

    public long countProjectsByStatus(ProjectStatus status) {
        logger.info("Counting all projects with status: {}", status);
        return projectRepository.countByStatus(status);
    }

    private ProjectSummaryDTO convertToSummaryDTO(Project p) {
        ProjectSummaryDTO dto = new ProjectSummaryDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setStatus(p.getStatus().name());
        dto.setEndDate(p.getEndDate() != null ? p.getEndDate().toString() : null);
        dto.setManagerName(p.getManager().getUsername());
        return dto;
    }

    private ProjectDetailDTO convertToDetailDTO(Project p) {
        ProjectDetailDTO dto = new ProjectDetailDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setDescription(p.getDescription());
        dto.setStartDate(p.getStartDate() != null ? p.getStartDate().toString() : null);
        dto.setEndDate(p.getEndDate() != null ? p.getEndDate().toString() : null);
        dto.setStatus(p.getStatus().name());
        dto.setManagerName(p.getManager().getUsername());
        dto.setTeamMembers(p.getTeamMembers().stream().map(User::getUsername).collect(Collectors.toList()));
        return dto;
    }
}
