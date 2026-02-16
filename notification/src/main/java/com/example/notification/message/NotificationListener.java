package com.example.notification.message;

import com.example.notification.model.NotificationEvent;
import com.example.notification.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationListener {

    private final NotificationService service;

    public NotificationListener(NotificationService service) {
        this.service = service;
    }

    @KafkaListener(
            topics = "notification-events",
            groupId = "notification-group"
    )
    public void receive(NotificationEvent event) {
        service.receive(event);
    }
}