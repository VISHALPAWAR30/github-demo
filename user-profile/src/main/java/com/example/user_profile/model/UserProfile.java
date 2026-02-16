package com.example.user_profile.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Document(collection = "profiles")
public class UserProfile {

    @Id
    private String id;

    private String userId;

    private String firstName;
    private String lastName;
    private String phone;
    private String address;

    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
}
