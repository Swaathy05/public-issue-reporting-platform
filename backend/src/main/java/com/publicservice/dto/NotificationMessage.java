package com.publicservice.dto;

public class NotificationMessage {
    private Long issueId;
    private String message;
    private String type;
    private Long timestamp;
    private String userId;
    private String userRole;

    // Constructors
    public NotificationMessage() {}

    public NotificationMessage(Long issueId, String message, String type) {
        this.issueId = issueId;
        this.message = message;
        this.type = type;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and Setters
    public Long getIssueId() {
        return issueId;
    }

    public void setIssueId(Long issueId) {
        this.issueId = issueId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }
}
