package com.publicservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class IssueRequest {
    
    @NotBlank(message = "Issue type is required")
    private String issueType;
    
    @NotBlank(message = "Description is required")
    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;
    
    @NotBlank(message = "Location is required")
    @Size(max = 500, message = "Location must not exceed 500 characters")
    private String location;
    
    public IssueRequest() {}
    
    public IssueRequest(String issueType, String description, String location) {
        this.issueType = issueType;
        this.description = description;
        this.location = location;
    }
    
    // Getters and Setters
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
}
