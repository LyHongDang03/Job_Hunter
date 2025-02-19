package com.example.Job_Hunter.controller;

import com.example.Job_Hunter.service.EmailService;
import com.example.Job_Hunter.service.SubscriberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    public String sendSimpleEmail() {
//        emailService.sendEmail();
//        emailService.sendEmailSync("lyhongdang03@gmail.com", "test", "<h1>Hello</h1>", false, true);
//        emailService.sendEmailFromTemplateSync("lyhongdang03@gmail.com", "test", "test");
        subscriberService.sendSubscribersEmailJobs();
        return "OK";
    }
}
