package com.publicservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "issue_updates")
public class IssueUpdate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "issue_id")
    @NotNull
    private Issue issue;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by")
    @NotNull
    private User updatedBy;
    
    @Enumerated(EnumType.STRING)
    private Issue.Status oldStatus;
    
    @Enumerated(EnumType.STRING)
    private Issue.Status newStatus;
    
    @Size(max = 1000)
    private String comments;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    public IssueUpdate() {
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Issue getIssue() {
        return issue;
    }
    
    public void setIssue(Issue issue) {
        this.issue = issue;
    }
    
    public User getUpdatedBy() {
        return updatedBy;
    }
    
    public void setUpdatedBy(User updatedBy) {
        this.updatedBy = updatedBy;
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
}
