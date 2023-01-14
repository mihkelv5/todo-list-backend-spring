package com.todolist.email;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl{


    private final JavaMailSender mailSender;
    @Value("${spring.mail.username}")
    private String sender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
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
