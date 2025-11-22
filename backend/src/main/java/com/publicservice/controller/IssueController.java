package com.publicservice.controller;

import com.publicservice.dto.IssueRequest;
import com.publicservice.dto.IssueResponse;
import com.publicservice.dto.IssueStatusUpdateRequest;
import com.publicservice.dto.IssueUpdateResponse;
import com.publicservice.entity.Issue;
import com.publicservice.entity.User;
import com.publicservice.service.IssueService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/issues")
@CrossOrigin(origins = "*")
public class IssueController {
    
    @Autowired
    private IssueService issueService;
    
    @Autowired
    private ObjectMapper objectMapper;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createIssue(@RequestPart("issue") String issueJson,
                                         @RequestPart(value = "photo", required = false) MultipartFile photo,
                                         Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            IssueRequest issueRequest = objectMapper.readValue(issueJson, IssueRequest.class);
            IssueResponse issue = issueService.createIssue(issueRequest, currentUser, photo);
            return ResponseEntity.ok(issue);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/my-issues")
    public ResponseEntity<List<IssueResponse>> getMyIssues(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        List<IssueResponse> issues = issueService.getIssuesByUser(currentUser);
        return ResponseEntity.ok(issues);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getIssueById(@PathVariable Long id) {
        Optional<IssueResponse> issue = issueService.getIssueById(id);
        if (issue.isPresent()) {
            return ResponseEntity.ok(issue.get());
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Issue not found");
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/{id}/updates")
    public ResponseEntity<List<IssueUpdateResponse>> getIssueUpdates(@PathVariable Long id) {
        List<IssueUpdateResponse> updates = issueService.getIssueUpdates(id);
        return ResponseEntity.ok(updates);
    }
    
    @GetMapping
    public ResponseEntity<Page<IssueResponse>> getAllIssues(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<IssueResponse> issues = issueService.getAllIssues(pageable);
        return ResponseEntity.ok(issues);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<Page<IssueResponse>> getIssuesByStatus(
            @PathVariable Issue.Status status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<IssueResponse> issues = issueService.getIssuesByStatus(status, pageable);
        return ResponseEntity.ok(issues);
    }
    
    @GetMapping("/type/{issueType}")
    public ResponseEntity<Page<IssueResponse>> getIssuesByType(
            @PathVariable String issueType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<IssueResponse> issues = issueService.getIssuesByType(issueType, pageable);
        return ResponseEntity.ok(issues);
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateIssueStatus(@PathVariable Long id,
                                               @Valid @RequestBody IssueStatusUpdateRequest request,
                                               Authentication authentication) {
        try {
            User currentUser = (User) authentication.getPrincipal();
            IssueResponse issue = issueService.updateIssueStatus(id, request, currentUser);
            return ResponseEntity.ok(issue);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getIssueStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("pending", issueService.getIssueCountByStatus(Issue.Status.PENDING));
        stats.put("inProgress", issueService.getIssueCountByStatus(Issue.Status.IN_PROGRESS));
        stats.put("resolved", issueService.getIssueCountByStatus(Issue.Status.RESOLVED));
        stats.put("rejected", issueService.getIssueCountByStatus(Issue.Status.REJECTED));
        return ResponseEntity.ok(stats);
    }
}
