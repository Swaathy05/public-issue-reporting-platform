package com.publicservice.service;

import com.publicservice.dto.IssueRequest;
import com.publicservice.dto.IssueResponse;
import com.publicservice.dto.IssueStatusUpdateRequest;
import com.publicservice.dto.IssueUpdateResponse;
import com.publicservice.entity.Issue;
import com.publicservice.entity.IssueUpdate;
import com.publicservice.entity.User;
import com.publicservice.repository.IssueRepository;
import com.publicservice.repository.IssueUpdateRepository;
import com.publicservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class IssueService {
    
    @Autowired
    private IssueRepository issueRepository;
    
    @Autowired
    private IssueUpdateRepository issueUpdateRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;
    
    private final String uploadDir = "uploads/";
    
    public IssueResponse createIssue(IssueRequest issueRequest, User reportedBy, MultipartFile photo) {
        Issue issue = new Issue();
        issue.setIssueType(issueRequest.getIssueType());
        issue.setDescription(issueRequest.getDescription());
        issue.setLocation(issueRequest.getLocation());
        issue.setReportedBy(reportedBy);
        issue.setStatus(Issue.Status.PENDING);
        
        // Handle photo upload
        if (photo != null && !photo.isEmpty()) {
            String photoUrl = savePhoto(photo);
            issue.setPhotoUrl(photoUrl);
        }
        
        Issue savedIssue = issueRepository.save(issue);
        
        // Create initial update record
        IssueUpdate initialUpdate = new IssueUpdate();
        initialUpdate.setIssue(savedIssue);
        initialUpdate.setUpdatedBy(reportedBy);
        initialUpdate.setOldStatus(null);
        initialUpdate.setNewStatus(Issue.Status.PENDING);
        initialUpdate.setComments("Issue reported");
        issueUpdateRepository.save(initialUpdate);
        
        return new IssueResponse(savedIssue);
    }
    
    public IssueResponse updateIssueStatus(Long issueId, IssueStatusUpdateRequest request, User updatedBy) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found with id: " + issueId));
        
        Issue.Status oldStatus = issue.getStatus();
        Issue.Status newStatus = request.getStatus();
        
        issue.setStatus(newStatus);
        issue.setAdminComments(request.getComments());
        
        // Assign to user if provided
        if (request.getAssignedToUserId() != null) {
            User assignedTo = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getAssignedToUserId()));
            issue.setAssignedTo(assignedTo);
        }
        
        Issue savedIssue = issueRepository.save(issue);
        
        // Create update record
        IssueUpdate update = new IssueUpdate();
        update.setIssue(savedIssue);
        update.setUpdatedBy(updatedBy);
        update.setOldStatus(oldStatus);
        update.setNewStatus(newStatus);
        update.setComments(request.getComments());
        issueUpdateRepository.save(update);
        
        // Send email notification
        emailService.sendStatusUpdateNotification(savedIssue, oldStatus, newStatus, request.getComments());
        
        return new IssueResponse(savedIssue);
    }
    
    public List<IssueResponse> getIssuesByUser(User user) {
        List<Issue> issues = issueRepository.findByReportedByOrderByCreatedAtDesc(user);
        return issues.stream()
                .map(IssueResponse::new)
                .collect(Collectors.toList());
    }
    
    public Page<IssueResponse> getAllIssues(Pageable pageable) {
        Page<Issue> issues = issueRepository.findAllOrderByCreatedAtDesc(pageable);
        return issues.map(IssueResponse::new);
    }
    
    public Page<IssueResponse> getIssuesByStatus(Issue.Status status, Pageable pageable) {
        Page<Issue> issues = issueRepository.findByStatusOrderByCreatedAtDesc(status, pageable);
        return issues.map(IssueResponse::new);
    }
    
    public Page<IssueResponse> getIssuesByType(String issueType, Pageable pageable) {
        Page<Issue> issues = issueRepository.findByIssueTypeOrderByCreatedAtDesc(issueType, pageable);
        return issues.map(IssueResponse::new);
    }
    
    public Optional<IssueResponse> getIssueById(Long id) {
        return issueRepository.findById(id)
                .map(IssueResponse::new);
    }
    
    public List<IssueUpdateResponse> getIssueUpdates(Long issueId) {
        Issue issue = issueRepository.findById(issueId)
                .orElseThrow(() -> new RuntimeException("Issue not found with id: " + issueId));
        
        List<IssueUpdate> updates = issueUpdateRepository.findByIssueOrderByCreatedAtDesc(issue);
        return updates.stream()
                .map(IssueUpdateResponse::new)
                .collect(Collectors.toList());
    }
    
    public long getIssueCountByStatus(Issue.Status status) {
        return issueRepository.countByStatus(status);
    }
    
    private String savePhoto(MultipartFile photo) {
        try {
            // Create upload directory if it doesn't exist
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            // Generate unique filename
            String originalFilename = photo.getOriginalFilename();
            String fileExtension = originalFilename != null ? 
                originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String filename = UUID.randomUUID().toString() + fileExtension;
            
            // Save file
            Path filePath = uploadPath.resolve(filename);
            Files.copy(photo.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            
            return uploadDir + filename;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save photo: " + e.getMessage());
        }
    }
}
