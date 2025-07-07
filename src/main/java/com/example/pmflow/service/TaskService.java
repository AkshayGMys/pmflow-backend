package com.example.pmflow.service;

import com.example.pmflow.dto.AdminUpdateTaskRequest;
import com.example.pmflow.dto.TaskRequest;
import com.example.pmflow.dto.TaskResponse;
import com.example.pmflow.dto.UpdateTaskRequest;
import com.example.pmflow.entity.Project;
import com.example.pmflow.entity.Task;
import com.example.pmflow.entity.User;
import com.example.pmflow.enums.TaskStatus;
import com.example.pmflow.repository.ProjectRepository;
import com.example.pmflow.repository.TaskRepository;
import com.example.pmflow.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    // ✅ Add new task
    public TaskResponse createTask(TaskRequest request) {
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User assignee = null;
        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
        }

        Task task = new Task();
        task.setName(request.getName());
        task.setDescription(request.getDescription());
        task.setPriority(request.getPriority());
        task.setStatus(request.getStatus());
        task.setDueDate(request.getDueDate()); 
        task.setProject(project);
        task.setAssignee(assignee);


        Task savedTask = taskRepository.save(task);
        return mapToResponse(savedTask);
    }

    // ✅ Get tasks by project ID
    public List<TaskResponse> getTasksByProjectId(Long projectId) {
        return taskRepository.findByProjectId(projectId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ Get tasks by assignee (user) ID
    public List<TaskResponse> getTasksByUserId(Long userId) {
        return taskRepository.findByAssigneeId(userId)
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ Update task status
    public TaskResponse updateTaskStatus(Long taskId, TaskStatus status) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setStatus(status);
        Task updated = taskRepository.save(task);
        return mapToResponse(updated);
    }
    public TaskResponse getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        return mapToResponse(task);
    }


    // ✅ Assign task to a user
    public TaskResponse assignTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        User assignee = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setAssignee(assignee);
        Task updated = taskRepository.save(task);
        return mapToResponse(updated);
    }

    private TaskResponse mapToResponse(Task task) {
        TaskResponse response = new TaskResponse();
        response.setId(task.getId());
        response.setName(task.getName());
        response.setDescription(task.getDescription());
        response.setPriority(task.getPriority());
        response.setStatus(task.getStatus());
        response.setProjectId(task.getProject().getId());

        if (task.getAssignee() != null) {
            response.setAssigneeId(task.getAssignee().getId());
            response.setAssigneeFirstName(task.getAssignee().getFirstName());
            response.setAssigneeLastName(task.getAssignee().getLastName());
        }
        if (task.getProject().getManager() != null) {
            User manager = task.getProject().getManager();
            response.setProjectManagerName(manager.getFirstName() + " " + manager.getLastName());
        }
        if (task.getProject().getName() != null) {
            response.setProjectName(task.getProject().getName());
         }
        response.setDueDate(task.getDueDate());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());

        return response;
    }
    public TaskResponse updateTaskDetails(Long taskId, UpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if (request.getName() != null) task.setName(request.getName());
        if (request.getDescription() != null) task.setDescription(request.getDescription());
        if (request.getPriority() != null) task.setPriority(request.getPriority());
        if (request.getDueDate() != null) task.setDueDate(request.getDueDate());

        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
            task.setAssignee(assignee);
        }

        Task updatedTask = taskRepository.save(task);
        return mapToResponse(updatedTask);
    }
    public TaskResponse adminUpdateTask(Long taskId, AdminUpdateTaskRequest request) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));

        if (request.getName() != null) {
            task.setName(request.getName());
        }
        if (request.getStatus() != null) {
            task.setStatus(request.getStatus());
        }

        Task updatedTask = taskRepository.save(task);
        return mapToResponse(updatedTask);
    }
    public void deleteTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found with ID: " + taskId));
        taskRepository.delete(task);
    }





}

