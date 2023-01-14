package com.todolist.email;


import com.todolist.SensitiveData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service
public class EmailServiceImpl{


    private final JavaMailSender mailSender;
    private final String sender;


    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
        this.sender = SensitiveData.USERNAME;
    }

    public String sendSimpleMail(String recipient, String message, String subject) {
        try{
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom(this.sender);
            mailMessage.setTo(recipient);
            mailMessage.setSubject(subject);
            mailMessage.setText(message);


            mailSender.send(mailMessage);
            return "Mail sent";
        } catch (Exception e){
            return "Error sending mail";
        }

    }



}
