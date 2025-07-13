package com.example.pmflow.dto;

import lombok.Data;

import java.time.LocalDateTime;

import com.example.pmflow.enums.TaskPriority;
import com.example.pmflow.enums.TaskStatus;

@Data
public class TaskResponse {
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public Long getProjectId() {
		return projectId;
	}
	public void setProjectId(Long projectId) {
		this.projectId = projectId;
	}
	public Long getAssigneeId() {
		return assigneeId;
	}
	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}
	public String getAssigneeFirstName() {
		return assigneeFirstName;
	}
	public void setAssigneeFirstName(String assigneeFirstName) {
		this.assigneeFirstName = assigneeFirstName;
	}
	public String getAssigneeLastName() {
		return assigneeLastName;
	}
	public void setAssigneeLastName(String assigneeLastName) {
		this.assigneeLastName = assigneeLastName;
	}
	public LocalDateTime getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	public TaskPriority getPriority() {
		return priority;
	}
	public void setPriority(TaskPriority priority) {
		this.priority = priority;
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	private Long id;
    private String name;
    private String description;
    private TaskPriority priority;
	private TaskStatus status;
    private Long projectId;
    private Long assigneeId;
    private String assigneeFirstName;  
    private String assigneeLastName;  
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectManagerName() {
		return projectManagerName;
	}
	public void setProjectManagerName(String projectManagerName) {
		this.projectManagerName = projectManagerName;
	}
	private String projectName;
    private String projectManagerName;
    public Long getProjectManagerId() {
		return projectManagerId;
	}
	public void setProjectManagerId(Long projectManagerId) {
		this.projectManagerId = projectManagerId;
	}
	private Long projectManagerId;

}
