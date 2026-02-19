package com.example.notification.model;


import lombok.Data;

@Data
public class NotificationEvent {
    private String eventType;
    private String email;
    private String message;
    private String correlationId;
}
