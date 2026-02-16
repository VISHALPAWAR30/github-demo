package com.example.user_profile.controller;

import com.example.user_profile.model.UserProfile;
import com.example.user_profile.security.SecurityUtil;
import com.example.user_profile.service.UserProfileService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profiles")
public class UserProfileController {

    private final UserProfileService service;

    public UserProfileController(UserProfileService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public UserProfile getMyProfile(Authentication authentication) {
        String userId = SecurityUtil.getCurrentUserId(authentication);
        return service.getProfile(userId);
    }

    @PutMapping("/me")
    public UserProfile updateMyProfile(
            @RequestBody UserProfile profile,
            Authentication authentication) {

        String userId = SecurityUtil.getCurrentUserId(authentication);
        return service.updateProfile(userId, profile);
    }

//    public UserProfile sendSms
}