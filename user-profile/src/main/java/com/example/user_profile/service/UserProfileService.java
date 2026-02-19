package com.example.user_profile.service;


import com.example.user_profile.model.UserProfile;
import com.example.user_profile.repository.UserProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfileService(UserProfileRepository repository) {
        this.repository = repository;
    }

    public UserProfile getProfile(String userId) {
        return repository.findByUserId(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Profile not found for user: " + userId
                        )
                );
    }

    public UserProfile updateProfile(String userId, UserProfile profile) {

        UserProfile existing = repository.findByUserId(userId)
                .orElse(new UserProfile());

        existing.setUserId(userId);
        existing.setFirstName(profile.getFirstName());
        existing.setLastName(profile.getLastName());
        existing.setPhone(profile.getPhone());
        existing.setAddress(profile.getAddress());
        existing.setUpdatedAt(Instant.now());

        return repository.save(existing);
    }
}
