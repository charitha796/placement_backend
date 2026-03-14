package com.example.intern_backend.dto;

import com.example.intern_backend.enums.ApplicationStatus;
import lombok.Data;

public class ApplicationRequest {
    private String companyName;
    private String roleApplied;
    private String stipend;
    private ApplicationStatus status;
    private String notes;

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public String getRoleApplied() { return roleApplied; }
    public void setRoleApplied(String roleApplied) { this.roleApplied = roleApplied; }
    public String getStipend() { return stipend; }
    public void setStipend(String stipend) { this.stipend = stipend; }
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
