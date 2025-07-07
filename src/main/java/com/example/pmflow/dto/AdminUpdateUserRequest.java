package com.example.pmflow.dto;

import com.example.pmflow.entity.Role;
import lombok.Data;

@Data
public class AdminUpdateUserRequest {
    private String firstName;
    public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	private String lastName;
    private Role role;
}
