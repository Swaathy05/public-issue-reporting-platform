package com.publicservice.controller;

import com.publicservice.dto.NotificationMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/issue.update")
    @SendTo("/topic/issue.updates")
    public NotificationMessage handleIssueUpdate(NotificationMessage message) {
        // Broadcast the update to all connected clients
        return message;
    }

    @MessageMapping("/admin.notification")
    @SendTo("/topic/admin.notifications")
    public NotificationMessage handleAdminNotification(NotificationMessage message) {
        // Send admin-specific notifications
        return message;
    }

    // Method to send notifications programmatically
    public void sendIssueUpdateNotification(Long issueId, String message, String type) {
        NotificationMessage notification = new NotificationMessage();
        notification.setIssueId(issueId);
        notification.setMessage(message);
        notification.setType(type);
        notification.setTimestamp(System.currentTimeMillis());
        
        messagingTemplate.convertAndSend("/topic/issue.updates", notification);
    }

    public void sendAdminNotification(String message, String type) {
        NotificationMessage notification = new NotificationMessage();
        notification.setMessage(message);
        notification.setType(type);
        notification.setTimestamp(System.currentTimeMillis());
        
        messagingTemplate.convertAndSend("/topic/admin.notifications", notification);
    }
}
