package com.example.intern_backend.dto;

import com.example.intern_backend.enums.Role;
import lombok.Data;

public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private String rollNumber;
    private String department;
    private String section;
    private Role role;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getRollNumber() { return rollNumber; }
    public void setRollNumber(String rollNumber) { this.rollNumber = rollNumber; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
