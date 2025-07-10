// ✅ NEW: StatusEndDateUpdateDTO.java
package com.example.pmflow.dto;

public class StatusEndDateUpdateDTO {
    private String status;
    private String endDate;  // Format: MM/dd/yyyy

    public StatusEndDateUpdateDTO() {}

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
