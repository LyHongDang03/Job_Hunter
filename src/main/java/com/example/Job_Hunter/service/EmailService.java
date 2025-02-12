package com.example.Job_Hunter.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    public void sendEmail() {
       SimpleMailMessage message = new SimpleMailMessage();
       message.setTo("lyhongdang03@gmail.com");
       message.setSubject("Hello World");
       message.setText("LyHongDang");
       mailSender.send(message);
    }
}
