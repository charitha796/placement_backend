package com.example.intern_backend.dto;

import com.example.intern_backend.enums.ApplicationStatus;
import lombok.Data;

import java.util.List;

public class BulkStatusUpdateRequest {

    private List<Long> applicationIds;
    private ApplicationStatus status;

    public List<Long> getApplicationIds() { return applicationIds; }
    public void setApplicationIds(List<Long> applicationIds) { this.applicationIds = applicationIds; }
    public ApplicationStatus getStatus() { return status; }
    public void setStatus(ApplicationStatus status) { this.status = status; }
}

