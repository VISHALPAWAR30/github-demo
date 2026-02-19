package com.example.document_wallet.message;

import com.example.document_wallet.model.NotificationEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {

    private final KafkaTemplate<String, NotificationEvent> kafkaTemplate;

    public NotificationProducer(KafkaTemplate<String, NotificationEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(NotificationEvent event) {
        kafkaTemplate.send("notification-events", event.getUserId(), event);
    }
}
