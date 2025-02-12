package com.example.Job_Hunter.controller;

import com.example.Job_Hunter.service.EmailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/email")
    public String sendSimpleEmail() {
        emailService.sendEmail();
        return "Hello World";
    }
}
