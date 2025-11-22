package com.publicservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "issues")
public class Issue {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Size(max = 50)
    @Column(name = "issue_type", length = 50)
    private String issueType;
    
    @NotBlank
    @Size(max = 1000)
    @Column(length = 1000)
    private String description;
    
    @NotBlank
    @Size(max = 500)
    @Column(length = 500)
    private String location;
    
    @Column(name = "photo_url", length = 500)
    private String photoUrl;
    
    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDING;
    
    @Column(name = "admin_comments", length = 1000)
    private String adminComments;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported_by")
    @NotNull
    private User reportedBy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;
    
    @OneToMany(mappedBy = "issue", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<IssueUpdate> updates = new ArrayList<>();
    
    public enum Status {
        PENDING, IN_PROGRESS, RESOLVED, REJECTED
    }
    
    public enum IssueType {
        POTHOLE("Pothole"),
        BROKEN_STREETLIGHT("Broken Streetlight"),
        GARBAGE("Garbage"),
        WATER_STAGNATION("Water Stagnation");
        
        private final String displayName;
        
        IssueType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    public Issue() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
        if (this.status == Status.RESOLVED && this.resolvedAt == null) {
            this.resolvedAt = LocalDateTime.now();
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
    
    public Status getStatus() {
        return status;
    }
    
    public void setStatus(Status status) {
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
    
    public User getReportedBy() {
        return reportedBy;
    }
    
    public void setReportedBy(User reportedBy) {
        this.reportedBy = reportedBy;
    }
    
    public User getAssignedTo() {
        return assignedTo;
    }
    
    public void setAssignedTo(User assignedTo) {
        this.assignedTo = assignedTo;
    }
    
    public List<IssueUpdate> getUpdates() {
        return updates;
    }
    
    public void setUpdates(List<IssueUpdate> updates) {
        this.updates = updates;
    }
}
