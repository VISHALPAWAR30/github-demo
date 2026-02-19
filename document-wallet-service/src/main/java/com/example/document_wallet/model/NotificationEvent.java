package com.example.document_wallet.model;


import lombok.Data;

@Data
public class NotificationEvent {
    private String eventType;
    private String userId;
    private String email;
    private String message;
}
