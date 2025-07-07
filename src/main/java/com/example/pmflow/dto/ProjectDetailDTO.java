package com.example.pmflow.dto;

import java.util.List;

public class ProjectDetailDTO {
    private Long id;
    private String name;
    private String description;
    private String startDate;
    private String endDate;
    private String status;
    private String managerName;
    private List<String> teamMembers;

    public ProjectDetailDTO() {}

    // ✅ Add this constructor for service layer usage
    public ProjectDetailDTO(Long id, String name, String description, String startDate, String endDate,
                            String status, String managerName, List<String> teamMembers) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.managerName = managerName;
        this.teamMembers = teamMembers;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public String getStartDate() { return startDate; }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }

    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getManagerName() { return managerName; }

    public void setManagerName(String managerName) { this.managerName = managerName; }

    public List<String> getTeamMembers() { return teamMembers; }

    public void setTeamMembers(List<String> teamMembers) { this.teamMembers = teamMembers; }
}
