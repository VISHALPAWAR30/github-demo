package com.example.notification.controller;

import lombok.Data;

@Data
public class SmsRequest {
    private String to;
    private String body;
}
