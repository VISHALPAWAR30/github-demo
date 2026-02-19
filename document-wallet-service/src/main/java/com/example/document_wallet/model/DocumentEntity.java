package com.example.document_wallet.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;
import java.util.Set;

@Data
@Document(collection = "documents")
@CompoundIndex(
        name = "owner_type_idx",
        def = "{'ownerUserId': 1, 'type': 1}"
)
public class DocumentEntity {

    @Id
    private String id;

    private String ownerUserId;

    private String type;
    private String name;
    private Set<String> tags;

    private Map<String, Object> metadata;

    private String fileBase64;
    private String fileType;
    private String fileName;




    private boolean deleted = false;


    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
}
