package com.publicservice.controller;

import com.publicservice.dto.IssueResponse;
import com.publicservice.dto.IssueStatusUpdateRequest;
import com.publicservice.dto.UserRequest;
import com.publicservice.entity.Issue;
import com.publicservice.entity.User;
import com.publicservice.service.IssueService;
import com.publicservice.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    
    @Autowired
    private IssueService issueService;
    
    @Autowired
    private UserService userService;
    
    @GetMapping("/issues")
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
    
    @GetMapping("/issues/status/{status}")
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
    
    @GetMapping("/issues/{id}")
    public ResponseEntity<?> getIssueById(@PathVariable Long id) {
        try {
            IssueResponse issue = issueService.getIssueById(id)
                    .orElseThrow(() -> new RuntimeException("Issue not found with id: " + id));
            return ResponseEntity.ok(issue);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
    
    @GetMapping("/issues/type/{issueType}")
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
    
    @PutMapping("/issues/{id}/status")
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
    
    @PostMapping("/users")
    public ResponseEntity<?> createAdminUser(@Valid @RequestBody UserRequest userRequest) {
        try {
            User user = userService.createAdmin(userRequest);
            Map<String, Object> response = new HashMap<>();
            response.put("id", user.getId());
            response.put("username", user.getUsername());
            response.put("email", user.getEmail());
            response.put("fullName", user.getFullName());
            response.put("role", user.getRole());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}
