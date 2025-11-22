package com.publicservice.dto;

import com.publicservice.entity.Issue;
import com.publicservice.entity.User;

import java.time.LocalDateTime;
import java.util.List;

public class IssueResponse {
    
    private Long id;
    private String issueType;
    private String description;
    private String location;
    private String photoUrl;
    private Issue.Status status;
    private String adminComments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime resolvedAt;
    private UserInfo reportedBy;
    private UserInfo assignedTo;
    private List<IssueUpdateResponse> updates;
    
    public IssueResponse() {}
    
    public IssueResponse(Issue issue) {
        this.id = issue.getId();
        this.issueType = issue.getIssueType();
        this.description = issue.getDescription();
        this.location = issue.getLocation();
        this.photoUrl = issue.getPhotoUrl();
        this.status = issue.getStatus();
        this.adminComments = issue.getAdminComments();
        this.createdAt = issue.getCreatedAt();
        this.updatedAt = issue.getUpdatedAt();
        this.resolvedAt = issue.getResolvedAt();
        
        if (issue.getReportedBy() != null) {
            this.reportedBy = new UserInfo(issue.getReportedBy());
        }
        
        if (issue.getAssignedTo() != null) {
            this.assignedTo = new UserInfo(issue.getAssignedTo());
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getIssueType() {
        return issueType;
    }
    
    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getPhotoUrl() {
        return photoUrl;
    }
    
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
    
    public Issue.Status getStatus() {
        return status;
    }
    
    public void setStatus(Issue.Status status) {
        this.status = status;
    }
    
    public String getAdminComments() {
        return adminComments;
    }
    
    public void setAdminComments(String adminComments) {
        this.adminComments = adminComments;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public LocalDateTime getResolvedAt() {
        return resolvedAt;
    }
    
    public void setResolvedAt(LocalDateTime resolvedAt) {
        this.resolvedAt = resolvedAt;
    }
    
    public UserInfo getReportedBy() {
        return reportedBy;
    }
    
    public void setReportedBy(UserInfo reportedBy) {
        this.reportedBy = reportedBy;
    }
    
    public UserInfo getAssignedTo() {
        return assignedTo;
    }
    
    public void setAssignedTo(UserInfo assignedTo) {
        this.assignedTo = assignedTo;
    }
    
    public List<IssueUpdateResponse> getUpdates() {
        return updates;
    }
    
    public void setUpdates(List<IssueUpdateResponse> updates) {
        this.updates = updates;
    }
    
    // Inner class for user information
    public static class UserInfo {
        private Long id;
        private String username;
        private String fullName;
        private String email;
        
        public UserInfo() {}
        
        public UserInfo(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullName = user.getFullName();
            this.email = user.getEmail();
        }
        
        // Getters and Setters
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
        
        public String getFullName() {
            return fullName;
        }
        
        public void setFullName(String fullName) {
            this.fullName = fullName;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
    }
}
