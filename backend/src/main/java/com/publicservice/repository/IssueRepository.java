package com.publicservice.repository;

import com.publicservice.entity.Issue;
import com.publicservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue, Long> {
    
    List<Issue> findByReportedBy(User reportedBy);
    
    List<Issue> findByAssignedTo(User assignedTo);
    
    List<Issue> findByStatus(Issue.Status status);
    
    @Query("SELECT i FROM Issue i WHERE i.reportedBy = :user ORDER BY i.createdAt DESC")
    List<Issue> findByReportedByOrderByCreatedAtDesc(@Param("user") User user);
    
    @Query("SELECT i FROM Issue i ORDER BY i.createdAt DESC")
    Page<Issue> findAllOrderByCreatedAtDesc(Pageable pageable);
    
    @Query("SELECT i FROM Issue i WHERE i.status = :status ORDER BY i.createdAt DESC")
    Page<Issue> findByStatusOrderByCreatedAtDesc(@Param("status") Issue.Status status, Pageable pageable);
    
    @Query("SELECT i FROM Issue i WHERE i.issueType = :issueType ORDER BY i.createdAt DESC")
    Page<Issue> findByIssueTypeOrderByCreatedAtDesc(@Param("issueType") String issueType, Pageable pageable);
    
    @Query("SELECT COUNT(i) FROM Issue i WHERE i.status = :status")
    long countByStatus(@Param("status") Issue.Status status);
    
    // Analytics queries
    long countByCreatedAtAfter(LocalDateTime date);
    long countByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
    @Query("SELECT i.issueType, COUNT(i) FROM Issue i GROUP BY i.issueType")
    List<Object[]> countIssuesByType();
    
    @Query("SELECT i.status, COUNT(i) FROM Issue i GROUP BY i.status")
    List<Object[]> countIssuesByStatus();
    
    @Query("SELECT i.location, COUNT(i) FROM Issue i WHERE i.location IS NOT NULL GROUP BY i.location ORDER BY COUNT(i) DESC")
    List<Object[]> getTopLocations(int limit);
    
    @Query("SELECT TIMESTAMPDIFF(HOUR, i.createdAt, i.resolvedAt) FROM Issue i WHERE i.resolvedAt IS NOT NULL")
    List<Object[]> getResolutionTimes();
}
