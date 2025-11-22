package com.publicservice.service;

import com.publicservice.entity.Issue;
import com.publicservice.repository.IssueRepository;
import com.publicservice.repository.IssueUpdateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class AnalyticsService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private IssueUpdateRepository issueUpdateRepository;

    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // Total issues
        long totalIssues = issueRepository.count();
        stats.put("totalIssues", totalIssues);
        
        // Issues by status
        Map<String, Long> statusCounts = new HashMap<>();
        statusCounts.put("PENDING", issueRepository.countByStatus(Issue.Status.PENDING));
        statusCounts.put("IN_PROGRESS", issueRepository.countByStatus(Issue.Status.IN_PROGRESS));
        statusCounts.put("RESOLVED", issueRepository.countByStatus(Issue.Status.RESOLVED));
        statusCounts.put("REJECTED", issueRepository.countByStatus(Issue.Status.REJECTED));
        stats.put("statusCounts", statusCounts);
        
        // Recent issues (last 7 days)
        LocalDateTime weekAgo = LocalDateTime.now().minus(7, ChronoUnit.DAYS);
        long recentIssues = issueRepository.countByCreatedAtAfter(weekAgo);
        stats.put("recentIssues", recentIssues);
        
        // Average resolution time
        double avgResolutionTime = calculateAverageResolutionTime();
        stats.put("avgResolutionTime", avgResolutionTime);
        
        return stats;
    }

    public Map<String, Object> getIssueTrends(int days) {
        Map<String, Object> trends = new HashMap<>();
        
        // Issues created per day for the last N days
        List<Map<String, Object>> dailyTrends = new ArrayList<>();
        for (int i = days - 1; i >= 0; i--) {
            LocalDateTime startOfDay = LocalDateTime.now().minus(i, ChronoUnit.DAYS).withHour(0).withMinute(0).withSecond(0);
            LocalDateTime endOfDay = startOfDay.plus(1, ChronoUnit.DAYS);
            
            long count = issueRepository.countByCreatedAtBetween(startOfDay, endOfDay);
            
            Map<String, Object> dayData = new HashMap<>();
            dayData.put("date", startOfDay.toLocalDate().toString());
            dayData.put("count", count);
            dailyTrends.add(dayData);
        }
        
        trends.put("dailyTrends", dailyTrends);
        return trends;
    }

    public Map<String, Object> getIssuesByType() {
        Map<String, Object> data = new HashMap<>();
        
        // Get issue types and their counts
        List<Object[]> results = issueRepository.countIssuesByType();
        List<Map<String, Object>> typeData = new ArrayList<>();
        
        for (Object[] result : results) {
            Map<String, Object> typeInfo = new HashMap<>();
            typeInfo.put("type", result[0]);
            typeInfo.put("count", result[1]);
            typeData.add(typeInfo);
        }
        
        data.put("issueTypes", typeData);
        return data;
    }

    public Map<String, Object> getIssuesByStatus() {
        Map<String, Object> data = new HashMap<>();
        
        List<Object[]> results = issueRepository.countIssuesByStatus();
        List<Map<String, Object>> statusData = new ArrayList<>();
        
        for (Object[] result : results) {
            Map<String, Object> statusInfo = new HashMap<>();
            statusInfo.put("status", result[0]);
            statusInfo.put("count", result[1]);
            statusData.add(statusInfo);
        }
        
        data.put("statusData", statusData);
        return data;
    }

    public Map<String, Object> getResponseTimes() {
        Map<String, Object> data = new HashMap<>();
        
        // Calculate average response time for different statuses
        Map<String, Double> responseTimes = new HashMap<>();
        
        // For resolved issues, calculate time from creation to resolution
        List<Object[]> resolvedTimes = issueRepository.getResolutionTimes();
        if (!resolvedTimes.isEmpty()) {
            double avgResolutionTime = resolvedTimes.stream()
                    .mapToDouble(result -> ((Number) result[0]).doubleValue())
                    .average()
                    .orElse(0.0);
            responseTimes.put("avgResolutionTime", avgResolutionTime);
        }
        
        data.put("responseTimes", responseTimes);
        return data;
    }

    public Map<String, Object> getTopLocations(int limit) {
        Map<String, Object> data = new HashMap<>();
        
        List<Object[]> results = issueRepository.getTopLocations(limit);
        List<Map<String, Object>> locationData = new ArrayList<>();
        
        for (Object[] result : results) {
            Map<String, Object> locationInfo = new HashMap<>();
            locationInfo.put("location", result[0]);
            locationInfo.put("count", result[1]);
            locationData.add(locationInfo);
        }
        
        data.put("topLocations", locationData);
        return data;
    }

    private double calculateAverageResolutionTime() {
        List<Object[]> results = issueRepository.getResolutionTimes();
        if (results.isEmpty()) {
            return 0.0;
        }
        
        return results.stream()
                .mapToDouble(result -> ((Number) result[0]).doubleValue())
                .average()
                .orElse(0.0);
    }
}




