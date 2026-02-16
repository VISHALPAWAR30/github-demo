package com.example.notification.service;

import com.example.notification.model.Notification;
import com.example.notification.model.NotificationEvent;
import com.example.notification.repository.NotificationRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository repository;

    public NotificationService(NotificationRepository repository) {
        this.repository = repository;
    }

    public Notification sendEmail(String to, String subject, String body, String correlationId) {

        Notification notification = new Notification();
        notification.setType("EMAIL");
        notification.setTo(to);
        notification.setSubject(subject);
        notification.setBody(body);
        notification.setCorrelationId(correlationId);
        notification.setStatus("SENT");

        return repository.save(notification);
    }

    public Notification sendSms(
            String to,
            String body,
            String correlationId
    ) {

        Notification notification = new Notification();
        notification.setType("SMS");
        notification.setTo(to);
        notification.setBody(body);
        notification.setCorrelationId(correlationId);
        notification.setStatus("SENT");

        return repository.save(notification);
    }


    @KafkaListener(topics = "notification-events")
    public void receive(NotificationEvent event) {

        Notification notification = new Notification();
        notification.setType(event.getEventType());
        notification.setTo(event.getEmail());
        notification.setBody(event.getMessage());
        notification.setStatus("SENT");

        repository.save(notification);
    }


    public void receiveSms(NotificationEvent event) {
    }
}
