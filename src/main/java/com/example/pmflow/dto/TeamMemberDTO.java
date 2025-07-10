// ✅ NEW: TeamMemberDTO.java
package com.example.pmflow.dto;

public class TeamMemberDTO {
    private Long id;
    private String username;

    public TeamMemberDTO() {}

    public TeamMemberDTO(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
