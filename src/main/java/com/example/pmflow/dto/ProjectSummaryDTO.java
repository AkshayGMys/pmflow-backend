package com.example.pmflow.dto;

public class ProjectSummaryDTO {
    private Long id;
    private String name;
    private String startDate;
    private String endDate;
    private String status;
    private String managerName;

    public ProjectSummaryDTO() {}

    // ✅ Add this constructor for service layer usage
    public ProjectSummaryDTO(Long id, String name, String startDate, String endDate,
                             String status, String managerName) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
        this.managerName = managerName;
    }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getStartDate() { return startDate; }

    public void setStartDate(String startDate) { this.startDate = startDate; }

    public String getEndDate() { return endDate; }

    public void setEndDate(String endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public String getManagerName() { return managerName; }

    public void setManagerName(String managerName) { this.managerName = managerName; }
}
