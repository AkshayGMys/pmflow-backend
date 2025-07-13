package com.example.pmflow.controller;

import com.example.pmflow.dto.AdminUpdateTaskRequest;
import com.example.pmflow.dto.TaskRequest;
import com.example.pmflow.dto.TaskResponse;
import com.example.pmflow.dto.UpdateTaskRequest;
import com.example.pmflow.enums.TaskStatus;
import com.example.pmflow.service.TaskService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);

    @Autowired
    private TaskService taskService;

    // ✅ Create a new task
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        logger.info("Creating new task: {}", request.getName());
        TaskResponse createdTask = taskService.createTask(request);
        logger.info("Task created with ID: {}", createdTask.getId());
        return ResponseEntity.ok(createdTask);
    }

    // ✅ Get task by ID
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long taskId) {
        logger.info("Fetching task with ID: {}", taskId);
        TaskResponse response = taskService.getTaskById(taskId);
        logger.info("Retrieved task: {}", response.getName());
        return ResponseEntity.ok(response);
    }

    // ✅ Get tasks for a project
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponse>> getTasksByProject(@PathVariable Long projectId) {
        logger.info("Getting tasks for project ID: {}", projectId);
        List<TaskResponse> tasks = taskService.getTasksByProjectId(projectId);
        logger.info("Found {} tasks for project {}", tasks.size(), projectId);
        return ResponseEntity.ok(tasks);
    }

    // ✅ Get tasks assigned to a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksByUser(@PathVariable Long userId) {
        logger.info("Getting tasks assigned to user ID: {}", userId);
        List<TaskResponse> tasks = taskService.getTasksByUserId(userId);
        logger.info("Found {} tasks for user {}", tasks.size(), userId);
        return ResponseEntity.ok(tasks);
    }

    // ✅ Update the status of a task
    @PutMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long taskId,
                                                         @RequestParam TaskStatus status) {
        logger.info("Updating status of task {} to {}", taskId, status);
        TaskResponse response = taskService.updateTaskStatus(taskId, status);
        logger.info("Updated task {} to status {}", taskId, response.getStatus());
        return ResponseEntity.ok(response);
    }

    // ✅ Assign a task to a user
    @PutMapping("/{taskId}/assign")
    public ResponseEntity<TaskResponse> assignTask(@PathVariable Long taskId,
                                                   @RequestParam Long userId) {
        logger.info("Assigning task {} to user {}", taskId, userId);
        TaskResponse response = taskService.assignTask(taskId, userId);
        logger.info("Task {} assigned to user {}", taskId, response.getAssigneeId());
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long taskId,
                                                   @RequestBody UpdateTaskRequest request) {
        logger.info("Updating task {} with new data", taskId);
        TaskResponse response = taskService.updateTaskDetails(taskId, request);
        logger.info("Updated task {} - name: {}, dueDate: {}", taskId, response.getName(), response.getDueDate());
        return ResponseEntity.ok(response);
    }

    // ✅ Admin: Update task name & status
    @PutMapping("/admin/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> updateTaskByAdmin(@PathVariable Long taskId,
                                                          @RequestBody AdminUpdateTaskRequest request) {
        logger.info("Admin updating task {} with name '{}' and status '{}'", taskId, request.getName(), request.getStatus());
        TaskResponse response = taskService.adminUpdateTask(taskId, request);
        logger.info("Task {} updated by admin. New name: {}, new status: {}", taskId, response.getName(), response.getStatus());
        return ResponseEntity.ok(response);
    }
    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')")
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        logger.warn("Deleting task with ID: {}", taskId);
        taskService.deleteTask(taskId);
        logger.info("Task {} deleted successfully", taskId);
        return ResponseEntity.ok("Task deleted successfully.");
    }

}
