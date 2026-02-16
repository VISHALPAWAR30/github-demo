package com.example.demo.FeignClient;

import com.example.notification.controller.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "notification-service",
        url = "http://localhost:8086"
)
public interface
NotificationFeign {

    @PostMapping("/notifications/email")
    void sendEmail(@RequestBody EmailRequest request);
}
