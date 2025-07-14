package com.example.pmflow.controller;

public class ProjectControllerTest {package com.example.pmflow.controller;

import com.example.pmflow.dto.*;
import com.example.pmflow.entity.ProjectStatus;
import com.example.pmflow.service.ProjectService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class ProjectControllerTest {

    @Mock
    private ProjectService projectService;

    @InjectMocks
    private ProjectController projectController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProject() {
        ProjectCreateRequestDTO request = new ProjectCreateRequestDTO();
        ProjectDetailDTO expected = new ProjectDetailDTO();
        when(projectService.createProject(any())).thenReturn(expected);

        ResponseEntity<ProjectDetailDTO> response = projectController.createProject(request);
        assertEquals(expected, response.getBody());
    }

    @Test
    void testGetAllProjects() {
        List<ProjectSummaryDTO> expected = Collections.singletonList(new ProjectSummaryDTO());
        when(projectService.getAllProjects()).thenReturn(expected);

        ResponseEntity<List<ProjectSummaryDTO>> response = projectController.getAllProjects();
        assertEquals(expected, response.getBody());
    }

    @Test
    void testGetProjectById() {
        ProjectDetailDTO expected = new ProjectDetailDTO();
        when(projectService.getProjectById(1L)).thenReturn(expected);

        ResponseEntity<ProjectDetailDTO> response = projectController.getProjectById(1L);
        assertEquals(expected, response.getBody());
    }

    @Test
    void testGetProjectByName() {
        when(projectService.getProjectByName("Alpha", false)).thenReturn(new ProjectSummaryDTO());
        ResponseEntity<?> response = projectController.getProjectByName("Alpha", false);
        assertEquals(ProjectSummaryDTO.class, response.getBody().getClass());
    }

    @Test
    void testUpdateProject() {
        ProjectUpdateRequestDTO request = new ProjectUpdateRequestDTO();
        ProjectDetailDTO expected = new ProjectDetailDTO();
        when(projectService.updateProject(eq(1L), any())).thenReturn(expected);

        ResponseEntity<ProjectDetailDTO> response = projectController.updateProject(1L, request);
        assertEquals(expected, response.getBody());
    }

    @Test
    void testUpdateProjectByName() {
        ProjectUpdateRequestDTO request = new ProjectUpdateRequestDTO();
        ProjectDetailDTO expected = new ProjectDetailDTO();
        when(projectService.updateProjectByName(eq("Alpha"), any())).thenReturn(expected);

        ResponseEntity<ProjectDetailDTO> response = projectController.updateProjectByName("Alpha", request);
        assertEquals(expected, response.getBody());
    }

    @Test
    void testDeleteProject() {
        ResponseEntity<Void> response = projectController.deleteProject(1L);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testDeleteProjectByName() {
        ResponseEntity<Void> response = projectController.deleteProjectByName("Alpha");
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testGetProjectsByManagerId() {
        when(projectService.getProjectsByManagerId(2L)).thenReturn(Collections.emptyList());
        ResponseEntity<List<ProjectSummaryDTO>> response = projectController.getProjectsByManagerId(2L);
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testGetProjectByNameForManager() {
        when(projectService.getProjectByNameForManager("Alpha", 2L, false)).thenReturn(new ProjectSummaryDTO());
        ResponseEntity<?> response = projectController.getProjectByNameForManager(2L, "Alpha", false);
        assertEquals(ProjectSummaryDTO.class, response.getBody().getClass());
    }

    @Test
    void testUpdateStatusAndEndDate() {
        ProjectDetailDTO expected = new ProjectDetailDTO();
        when(projectService.updateProjectEndDateAndStatusByManager(eq(1L), eq(2L), any(), any())).thenReturn(expected);
        ResponseEntity<ProjectDetailDTO> response = projectController.updateStatusAndEndDate(2L, 1L, "12/31/2025", "IN_PROGRESS");
        assertEquals(expected, response.getBody());
    }

    @Test
    void testGetTeamMembers() {
        when(projectService.getTeamMembersOfProject(1L, 2L)).thenReturn(Collections.emptyList());
        ResponseEntity<List<TeamMemberDTO>> response = projectController.getTeamMembers(2L, 1L);
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testFilterProjects() {
        when(projectService.filterProjects(null, null, null, null)).thenReturn(Collections.emptyList());
        ResponseEntity<List<ProjectSummaryDTO>> response = projectController.filterProjects(null, null, null, null);
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testCountProjectsByStatus() {
        when(projectService.countProjectsByStatus(ProjectStatus.NOT_STARTED)).thenReturn(3L);
        ResponseEntity<Long> response = projectController.countProjectsByStatus(ProjectStatus.NOT_STARTED);
        assertEquals(3L, response.getBody());
    }

    @Test
    void testFilterProjectsForManager() {
        when(projectService.filterProjectsByManager(2L, null, null, null)).thenReturn(Collections.emptyList());
        ResponseEntity<List<ProjectSummaryDTO>> response = projectController.filterProjectsForManager(2L, null, null, null);
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testCountProjectsByStatusForManager() {
        when(projectService.countProjectsByStatusForManager(2L, ProjectStatus.IN_PROGRESS)).thenReturn(5L);
        ResponseEntity<Long> response = projectController.countProjectsByStatusForManager(2L, ProjectStatus.IN_PROGRESS);
        assertEquals(5L, response.getBody());
    }
}

}
