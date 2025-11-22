package com.publicservice.dto;

import com.publicservice.entity.Issue;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class IssueStatusUpdateRequest {
    
    @NotNull(message = "Status is required")
    private Issue.Status status;
    
    @Size(max = 1000, message = "Comments must not exceed 1000 characters")
    private String comments;
    
    private Long assignedToUserId;
    
    public IssueStatusUpdateRequest() {}
    
    public IssueStatusUpdateRequest(Issue.Status status, String comments, Long assignedToUserId) {
        this.status = status;
        this.comments = comments;
        this.assignedToUserId = assignedToUserId;
    }
    
    // Getters and Setters
    public Issue.Status getStatus() {
        return status;
    }
    
    public void setStatus(Issue.Status status) {
        this.status = status;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public Long getAssignedToUserId() {
        return assignedToUserId;
    }
    
    public void setAssignedToUserId(Long assignedToUserId) {
        this.assignedToUserId = assignedToUserId;
    }
}
