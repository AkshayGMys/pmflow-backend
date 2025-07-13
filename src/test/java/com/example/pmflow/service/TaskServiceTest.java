package com.example.pmflow.service;

import com.example.pmflow.dto.*;
import com.example.pmflow.entity.*;
import com.example.pmflow.enums.TaskPriority;
import com.example.pmflow.enums.TaskStatus;
import com.example.pmflow.repository.ProjectRepository;
import com.example.pmflow.repository.TaskRepository;
import com.example.pmflow.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    private Project project;
    private User assignee;
    private Task task;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        assignee = new User();
        assignee.setId(1L);
        assignee.setFirstName("John");
        assignee.setLastName("Doe");

        project = new Project();
        project.setId(2L);
        project.setName("Test Project");
        project.setManager(assignee);

        task = new Task();
        task.setId(3L);
        task.setName("Sample Task");
        task.setDescription("Desc");
        task.setProject(project);
        task.setAssignee(assignee);
        task.setStatus(TaskStatus.NOT_STARTED);
        task.setPriority(TaskPriority.MEDIUM);
        task.setDueDate(LocalDateTime.now());
    }

    @Test
    void testCreateTask() {
        TaskRequest req = new TaskRequest();
        req.setName("New Task");
        req.setProjectId(2L);
        req.setAssigneeId(1L);
        req.setPriority(TaskPriority.HIGH);
        req.setStatus(TaskStatus.IN_PROGRESS);
        req.setDescription("Do something");

        when(projectRepository.findById(2L)).thenReturn(Optional.of(project));
        when(userRepository.findById(1L)).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.createTask(req);

        assertEquals("Sample Task", response.getName());
        assertEquals(1L, response.getAssigneeId());
    }

    @Test
    void testGetTasksByProjectId() {
        when(taskRepository.findByProjectId(2L)).thenReturn(List.of(task));
        List<TaskResponse> responses = taskService.getTasksByProjectId(2L);
        assertEquals(1, responses.size());
        assertEquals("Sample Task", responses.get(0).getName());
    }

    @Test
    void testGetTasksByUserId() {
        when(taskRepository.findByAssigneeId(1L)).thenReturn(List.of(task));
        List<TaskResponse> responses = taskService.getTasksByUserId(1L);
        assertEquals(1, responses.size());
        assertEquals(1L, responses.get(0).getAssigneeId());
    }

    @Test
    void testUpdateTaskStatus() {
        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.updateTaskStatus(3L, TaskStatus.COMPLETED);
        assertEquals(TaskStatus.COMPLETED, response.getStatus());
    }

    @Test
    void testGetTaskById() {
        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));
        TaskResponse response = taskService.getTaskById(3L);
        assertEquals("Sample Task", response.getName());
    }

    @Test
    void testAssignTask() {
        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));
        when(userRepository.findById(1L)).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.assignTask(3L, 1L);
        assertEquals(1L, response.getAssigneeId());
    }

    @Test
    void testUpdateTaskDetails() {
        UpdateTaskRequest req = new UpdateTaskRequest();
        req.setName("Updated Task");
        req.setAssigneeId(1L);

        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));
        when(userRepository.findById(1L)).thenReturn(Optional.of(assignee));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.updateTaskDetails(3L, req);
        assertEquals("Updated Task", response.getName()); // `task` is mocked to return name "Sample Task"
    }

    @Test
    void testAdminUpdateTask() {
        AdminUpdateTaskRequest req = new AdminUpdateTaskRequest();
        req.setName("Admin Task");
        req.setStatus(TaskStatus.IN_PROGRESS);

        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        TaskResponse response = taskService.adminUpdateTask(3L, req);
        assertEquals(TaskStatus.IN_PROGRESS, response.getStatus()); // `task` was mocked with NOT_STARTED
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.findById(3L)).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(task);

        assertDoesNotThrow(() -> taskService.deleteTask(3L));
        verify(taskRepository).delete(task);
    }
}
