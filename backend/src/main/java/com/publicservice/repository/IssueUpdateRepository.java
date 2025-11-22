package com.publicservice.repository;

import com.publicservice.entity.Issue;
import com.publicservice.entity.IssueUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueUpdateRepository extends JpaRepository<IssueUpdate, Long> {
    
    List<IssueUpdate> findByIssueOrderByCreatedAtDesc(Issue issue);
    
    @Query("SELECT iu FROM IssueUpdate iu WHERE iu.issue = :issue ORDER BY iu.createdAt DESC")
    List<IssueUpdate> findLatestUpdatesByIssue(@Param("issue") Issue issue);
}
