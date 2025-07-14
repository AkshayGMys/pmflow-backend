package com.example.pmflow.dto;

public class MemberProjectDTO {
    private String projectName;
    private String status;
    private Long projectId;
    
    public MemberProjectDTO(Long projectId, String projectName, String status) {
        this.projectName = projectName;
        this.status = status;
        this.projectId = projectId;
    }

    public Long getProjectId(){
        return projectId;
    }

    public void setprojectId(Long projectId)
    {
        this.projectId = projectId;
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
