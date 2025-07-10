package com.example.pmflow.dto;

public class MemberProjectDTO {
    private String projectName;
    private String status;

    public MemberProjectDTO(String projectName, String status) {
        this.projectName = projectName;
        this.status = status;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
