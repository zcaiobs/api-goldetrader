package com.project.api.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mail;

    public String sendSimpleMessage(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("no-reply@goldetrader.com.br");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mail.send(message);
        return "Email forwarded";
    }

    public String teste() {
    
        return "ok";
    }
}
