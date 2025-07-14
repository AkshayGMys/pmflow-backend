package com.example.pmflow.controller;

import com.example.pmflow.dto.AdminUpdateTaskRequest;
import com.example.pmflow.dto.TaskRequest;
import com.example.pmflow.dto.TaskResponse;
import com.example.pmflow.dto.UpdateTaskRequest;
import com.example.pmflow.enums.TaskStatus;
import com.example.pmflow.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;

    // ✅ Create a new task
    @PostMapping
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskRequest request) {
        TaskResponse createdTask = taskService.createTask(request);
        return ResponseEntity.ok(createdTask);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId));
    }

    // ✅ Get all tasks for a specific project
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponse>> getTasksByProject(@PathVariable Long projectId) {
        return ResponseEntity.ok(taskService.getTasksByProjectId(projectId));
    }

    // ✅ Get all tasks assigned to a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TaskResponse>> getTasksByUser(@PathVariable Long userId) {
        return ResponseEntity.ok(taskService.getTasksByUserId(userId));
    }

    // ✅ Update the status of a task
    @PutMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(@PathVariable Long taskId,
                                                         @RequestParam TaskStatus status) {
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, status));
    }

    // ✅ Assign a task to a user
    @PutMapping("/{taskId}/assign")
    public ResponseEntity<TaskResponse> assignTask(@PathVariable Long taskId,
                                                   @RequestParam Long userId) {
        return ResponseEntity.ok(taskService.assignTask(taskId, userId));
    }
    @PutMapping("/{taskId}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long taskId,
                                                   @RequestBody UpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.updateTaskDetails(taskId, request));
    }
    @PutMapping("/admin/{taskId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TaskResponse> updateTaskByAdmin(@PathVariable Long taskId,
                                                          @RequestBody AdminUpdateTaskRequest request) {
        return ResponseEntity.ok(taskService.adminUpdateTask(taskId, request));
    }
    @DeleteMapping("/{taskId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('PROJECT_MANAGER')") // Optional
    public ResponseEntity<String> deleteTask(@PathVariable Long taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.ok("Task deleted successfully.");
    }

}
