package com.publicservice.service;

import com.publicservice.entity.Issue;
import com.publicservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired(required = false)
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username:}")
    private String fromEmail;
    
    public void sendStatusUpdateNotification(Issue issue, Issue.Status oldStatus, Issue.Status newStatus, String comments) {
        User reportedBy = issue.getReportedBy();
        String toEmail = reportedBy.getEmail();
        String subject = "Issue Status Update - Report #" + issue.getId();
        
        StringBuilder body = new StringBuilder();
        body.append("Dear ").append(reportedBy.getFullName()).append(",\n\n");
        body.append("Your issue report has been updated:\n\n");
        body.append("Report ID: ").append(issue.getId()).append("\n");
        body.append("Issue Type: ").append(issue.getIssueType()).append("\n");
        body.append("Location: ").append(issue.getLocation()).append("\n");
        body.append("Description: ").append(issue.getDescription()).append("\n\n");
        
        if (oldStatus != null) {
            body.append("Previous Status: ").append(oldStatus).append("\n");
        }
        body.append("Current Status: ").append(newStatus).append("\n\n");
        
        if (comments != null && !comments.trim().isEmpty()) {
            body.append("Admin Comments: ").append(comments).append("\n\n");
        }
        
        body.append("You can track your issue status by logging into the platform.\n\n");
        body.append("Thank you for using our Public Service Issue Reporting Platform.\n\n");
        body.append("Best regards,\n");
        body.append("Public Service Team");
        
        sendEmail(toEmail, subject, body.toString());
    }
    
    public void sendIssueConfirmation(Issue issue) {
        User reportedBy = issue.getReportedBy();
        String toEmail = reportedBy.getEmail();
        String subject = "Issue Report Confirmation - Report #" + issue.getId();
        
        StringBuilder body = new StringBuilder();
        body.append("Dear ").append(reportedBy.getFullName()).append(",\n\n");
        body.append("Thank you for reporting an issue. Your report has been received and is being processed.\n\n");
        body.append("Report Details:\n");
        body.append("Report ID: ").append(issue.getId()).append("\n");
        body.append("Issue Type: ").append(issue.getIssueType()).append("\n");
        body.append("Location: ").append(issue.getLocation()).append("\n");
        body.append("Description: ").append(issue.getDescription()).append("\n");
        body.append("Status: ").append(issue.getStatus()).append("\n");
        body.append("Reported On: ").append(issue.getCreatedAt()).append("\n\n");
        
        body.append("You will receive email notifications when the status of your issue is updated.\n\n");
        body.append("Thank you for helping improve our community.\n\n");
        body.append("Best regards,\n");
        body.append("Public Service Team");
        
        sendEmail(toEmail, subject, body.toString());
    }
    
    private void sendEmail(String to, String subject, String body) {
        if (mailSender == null || fromEmail == null || fromEmail.isEmpty()) {
            System.out.println("Email service not configured. Skipping email to: " + to);
            return;
        }
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            
            mailSender.send(message);
            System.out.println("Email sent successfully to: " + to);
        } catch (Exception e) {
            System.err.println("Failed to send email to: " + to + ", Error: " + e.getMessage());
        }
    }
}
