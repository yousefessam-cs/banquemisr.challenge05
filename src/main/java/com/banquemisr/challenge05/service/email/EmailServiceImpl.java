package com.banquemisr.challenge05.service.email;

import com.banquemisr.challenge05.exception.EmailServiceException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailServiceImpl implements IEmailService {

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendEmail(String to, String subject, String textBody) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("testyousef33@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(textBody);

            log.info("Preparing to send email to: {}", to);
            mailSender.send(message);
            log.info("email sent successfully to: {}", to);
        } catch (MailException e) {
            log.error("Failed to send email to {}: {}", to, e.getMessage(), e);
            throw new EmailServiceException("Failed to send email to " + to, e);
        }
    }
}