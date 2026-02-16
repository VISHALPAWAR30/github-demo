package com.example.notification.controller;

import com.example.notification.model.Notification;
import com.example.notification.model.NotificationEvent;
import com.example.notification.service.NotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService service;

    public NotificationController(NotificationService service) {
        this.service = service;
    }

    @PostMapping("/email")
    public Notification sendEmail(
            @RequestBody EmailRequest request,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {

        return service.sendEmail(
                request.getTo(),
                request.getSubject(),
                request.getBody(),
                correlationId
        );
    }

    @PostMapping("/sms")
    public Notification sendSms(
            @RequestBody SmsRequest request,
            @RequestHeader(value = "X-Correlation-Id", required = false) String correlationId) {

        return service.sendSms(
                request.getTo(),
                request.getBody(),
                correlationId
        );
    }


    @KafkaListener(topics = "notification-events")
    public void receiveSms(NotificationEvent event) {

        service.receiveSms(event);
    }

}
