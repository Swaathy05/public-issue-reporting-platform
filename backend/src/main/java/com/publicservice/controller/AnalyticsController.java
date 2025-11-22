package com.publicservice.controller;

import com.publicservice.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = analyticsService.getDashboardStats();
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/issues/trends")
    public ResponseEntity<Map<String, Object>> getIssueTrends(
            @RequestParam(defaultValue = "30") int days) {
        Map<String, Object> trends = analyticsService.getIssueTrends(days);
        return ResponseEntity.ok(trends);
    }

    @GetMapping("/issues/by-type")
    public ResponseEntity<Map<String, Object>> getIssuesByType() {
        Map<String, Object> data = analyticsService.getIssuesByType();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/issues/by-status")
    public ResponseEntity<Map<String, Object>> getIssuesByStatus() {
        Map<String, Object> data = analyticsService.getIssuesByStatus();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/response-times")
    public ResponseEntity<Map<String, Object>> getResponseTimes() {
        Map<String, Object> data = analyticsService.getResponseTimes();
        return ResponseEntity.ok(data);
    }

    @GetMapping("/top-locations")
    public ResponseEntity<Map<String, Object>> getTopLocations(
            @RequestParam(defaultValue = "10") int limit) {
        Map<String, Object> data = analyticsService.getTopLocations(limit);
        return ResponseEntity.ok(data);
    }
}
