package com.example.notification.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
@Data
@Document(collection = "notifications")
public class Notification {

    @Id
    private String id;

    private String type;
    private String to;
    private String subject;
    private String body;
    private String status;
    private String correlationId;

    private Instant createdAt = Instant.now();
}
