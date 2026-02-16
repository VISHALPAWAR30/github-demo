package com.example.demo.service;

import com.example.demo.FeignClient.NotificationFeign;
import com.example.demo.dto.AdminRegisterRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.JWTService;
import com.example.notification.controller.EmailRequest;
import com.example.notification.controller.NotificationController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
public class AuthService {

    @Value("${admin.secret}")
    private String adminSecret;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTService jwtService;
    private final NotificationFeign notificationFeign;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JWTService jwtService,
                       NotificationFeign notificationFeign) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.notificationFeign = notificationFeign;
    }

    // register
    public void register(RegisterRequest request) {
        Optional<User> existingUser =
                userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            throw new RuntimeException("User already exists");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(List.of("USER"));

        userRepository.save(user);
    }

    // login
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(
                user.getId(),
                user.getEmail(),
                user.getRoles()
        );

        return new LoginResponse(token);
    }

    public void registerAdmin(AdminRegisterRequest request) {

        if (!adminSecret.equals(request.getAdminSecret())) {
            throw new RuntimeException("Unauthorized admin creation");
        }

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Admin already exists");
        }

        User admin = new User();
        admin.setEmail(request.getEmail());
        admin.setPassword(passwordEncoder.encode(request.getPassword()));
        admin.setRoles(List.of("ADMIN"));

        userRepository.save(admin);
    }
    // forgot password
    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(email);
        emailRequest.setSubject("Forgot Password");

        notificationFeign.sendEmail(emailRequest);
    }


}


