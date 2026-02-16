package com.example.demo.controller;

import com.example.demo.dto.AdminRegisterRequest;
import com.example.demo.dto.LoginRequest;
import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.RegisterRequest;
import com.example.demo.service.AuthService;
import com.example.notification.controller.NotificationController;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;



    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    //  Register API
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody @Valid RegisterRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.ok("User registered successfully");
        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "user already exist").toString());        }
    }

    //  Login API
//    @PostMapping("/login")
//    public ResponseEntity<LoginResponse> login(
//            @RequestBody @Valid LoginRequest request) {
//        return ResponseEntity.ok(authService.login(request));
//    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (RuntimeException ex) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid email or password"));
        }
    }


    //admin can fetch details

    @PostMapping("/register-admin")
    public ResponseEntity<String> registerAdmin(
          @RequestBody AdminRegisterRequest request) {

      try{  authService.registerAdmin(request);
        return ResponseEntity.ok("Admin registered successfully");}catch (RuntimeException ex){
          return ResponseEntity
                  .status(HttpStatus.BAD_REQUEST)
                  .body(Map.of("error", "No Data Found").toString());


      }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(
            @RequestParam String email) {

        authService.forgotPassword(email);

        return ResponseEntity.ok(
                Map.of("message", "OTP sent successfully")
        );
    }

}




