package com.banquemisr.challenge05.service.email;

public interface IEmailService {
    void sendEmail(String to, String subject, String textBody);
}
