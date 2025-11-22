package com.publicservice.dto;

import com.publicservice.entity.Issue;
import com.publicservice.entity.IssueUpdate;
import com.publicservice.entity.User;

import java.time.LocalDateTime;

public class IssueUpdateResponse {
    
    private Long id;
    private Issue.Status oldStatus;
    private Issue.Status newStatus;
    private String comments;
    private LocalDateTime createdAt;
    private UserInfo updatedBy;
    
    public IssueUpdateResponse() {}
    
    public IssueUpdateResponse(IssueUpdate issueUpdate) {
        this.id = issueUpdate.getId();
        this.oldStatus = issueUpdate.getOldStatus();
        this.newStatus = issueUpdate.getNewStatus();
        this.comments = issueUpdate.getComments();
        this.createdAt = issueUpdate.getCreatedAt();
        
        if (issueUpdate.getUpdatedBy() != null) {
            this.updatedBy = new UserInfo(issueUpdate.getUpdatedBy());
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Issue.Status getOldStatus() {
        return oldStatus;
    }
    
    public void setOldStatus(Issue.Status oldStatus) {
        this.oldStatus = oldStatus;
    }
    
    public Issue.Status getNewStatus() {
        return newStatus;
    }
    
    public void setNewStatus(Issue.Status newStatus) {
        this.newStatus = newStatus;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public UserInfo getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(UserInfo updatedBy) {
        this.updatedBy = updatedBy;
    }
    
    // Inner class for user information
    public static class UserInfo {
        private Long id;
        private String username;
        private String fullName;
        
        public UserInfo() {}
        
        public UserInfo(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.fullName = user.getFullName();
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
    }
}
