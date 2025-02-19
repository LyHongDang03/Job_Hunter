package com.example.Job_Hunter.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public EmailService(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public void sendEmail() {
       SimpleMailMessage message = new SimpleMailMessage();
       message.setTo("lyhongdang03@gmail.com");
       message.setSubject("Hello World");
       message.setText("LyHongDang");
       mailSender.send(message);
    }
    public void sendEmailSync(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content, isHtml);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Error sending mail", e);
        }
    }
    public void sendEmailFromTemplateSync(String to, String subject, String templateName) {
        Context context = new Context();
        String content = this.templateEngine.process(templateName, context);
        this.sendEmailSync(to, subject, content, false, true);
    }
}
