package com.example.document_wallet.service;


import com.example.document_wallet.model.DocumentEntity;
import com.example.document_wallet.model.NotificationEvent;
import com.example.document_wallet.repository.DocumentRepository;
import com.example.document_wallet.message.NotificationProducer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class DocumentService {

    private final DocumentRepository repository;
    private final NotificationProducer producer;

    public DocumentService(DocumentRepository repository,
                           NotificationProducer producer) {
        this.repository = repository;
        this.producer = producer;
    }

    public DocumentEntity upload(MultipartFile file,
                                 String type,
                                 String name,
                                 String userId) throws IOException {

        // Create document
        DocumentEntity document = new DocumentEntity();
        document.setOwnerUserId(userId);
        document.setType(type);
        document.setName(name);
        document.setFileName(file.getOriginalFilename());
        document.setFileType(file.getContentType());
        document.setFileBase64(
                Base64.getEncoder().encodeToString(file.getBytes())
        );

        DocumentEntity saved = repository.save(document);

        NotificationEvent event = new NotificationEvent();
        event.setEventType("DOCUMENT_UPLOADED");
        event.setUserId(userId);
        event.setEmail("user@email.com");
        event.setMessage("Document uploaded successfully");

        producer.send(event);

        return saved;
    }
}
