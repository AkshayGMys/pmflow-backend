package com.example.pmflow.controller;

import com.example.pmflow.dto.*;
import com.example.pmflow.enums.TaskPriority;
import com.example.pmflow.enums.TaskStatus;
import com.example.pmflow.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTask() {
        TaskRequest request = new TaskRequest();
        request.setName("Build API");

        TaskResponse response = new TaskResponse();
        response.setId(1L);
        response.setName("Build API");

        when(taskService.createTask(request)).thenReturn(response);

        ResponseEntity<TaskResponse> result = taskController.createTask(request);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Build API", result.getBody().getName());
    }

    @Test
    void testGetTaskById() {
        TaskResponse response = new TaskResponse();
        response.setId(1L);
        response.setName("Fix UI");

        when(taskService.getTaskById(1L)).thenReturn(response);

        ResponseEntity<TaskResponse> result = taskController.getTaskById(1L);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Fix UI", result.getBody().getName());
    }

    @Test
    void testGetTasksByProject() {
        when(taskService.getTasksByProjectId(1L)).thenReturn(List.of(new TaskResponse()));
        ResponseEntity<List<TaskResponse>> result = taskController.getTasksByProject(1L);
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void testGetTasksByUser() {
        when(taskService.getTasksByUserId(1L)).thenReturn(List.of(new TaskResponse()));
        ResponseEntity<List<TaskResponse>> result = taskController.getTasksByUser(1L);
        assertEquals(200, result.getStatusCodeValue());
    }

    @Test
    void testUpdateTaskStatus() {
        TaskResponse response = new TaskResponse();
        response.setStatus(TaskStatus.COMPLETED);

        when(taskService.updateTaskStatus(1L, TaskStatus.COMPLETED)).thenReturn(response);

        ResponseEntity<TaskResponse> result = taskController.updateTaskStatus(1L, TaskStatus.COMPLETED);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(TaskStatus.COMPLETED, result.getBody().getStatus());
    }

    @Test
    void testAssignTask() {
        TaskResponse response = new TaskResponse();
        response.setAssigneeId(2L);

        when(taskService.assignTask(1L, 2L)).thenReturn(response);

        ResponseEntity<TaskResponse> result = taskController.assignTask(1L, 2L);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals(2L, result.getBody().getAssigneeId());
    }

    @Test
    void testUpdateTask() {
        UpdateTaskRequest req = new UpdateTaskRequest();
        req.setName("Updated");

        TaskResponse response = new TaskResponse();
        response.setName("Updated");

        when(taskService.updateTaskDetails(1L, req)).thenReturn(response);

        ResponseEntity<TaskResponse> result = taskController.updateTask(1L, req);
        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Updated", result.getBody().getName());
    }

    @Test
    void testUpdateTaskByAdmin() {
        AdminUpdateTaskRequest req = new AdminUpdateTaskRequest();
        req.setName("Admin Update");

        TaskResponse response = new TaskResponse();
        response.setName("Admin Update");

        when(taskService.adminUpdateTask(1L, req)).thenReturn(response);

        ResponseEntity<TaskResponse> result = taskController.updateTaskByAdmin(1L, req);
        assertEquals("Admin Update", result.getBody().getName());
    }

    @Test
    void testDeleteTask() {
        doNothing().when(taskService).deleteTask(1L);

        ResponseEntity<String> result = taskController.deleteTask(1L);

        assertEquals(200, result.getStatusCodeValue());
        assertEquals("Task deleted successfully.", result.getBody());
    }
}
