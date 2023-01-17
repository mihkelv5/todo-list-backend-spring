package com.todolist.email;

import com.todolist.SensitiveData;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class CustomJavaMailSender {

    @Bean
    public JavaMailSender gmailMailSender(){
        JavaMailSenderImpl gmailMailSender = new JavaMailSenderImpl();
        gmailMailSender.setHost("smtp.gmail.com");
        gmailMailSender.setPort(587);
        gmailMailSender.setUsername(SensitiveData.MAIL_USERNAME);
        gmailMailSender.setPassword(SensitiveData.MAIL_PASSWORD);

        Properties props = gmailMailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "false");

        return gmailMailSender;
    }
}
