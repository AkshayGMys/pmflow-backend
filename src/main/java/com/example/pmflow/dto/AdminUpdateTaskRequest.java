package com.example.pmflow.dto;

import com.example.pmflow.enums.TaskStatus;
import lombok.Data;

@Data
public class AdminUpdateTaskRequest {
    public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TaskStatus getStatus() {
		return status;
	}
	public void setStatus(TaskStatus status) {
		this.status = status;
	}
	private String name;
    private TaskStatus status;
}
