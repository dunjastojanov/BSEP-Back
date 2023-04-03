package com.myhouse.MyHouse.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender emailSender;

    void sendCertificate(String to, String name, String subject, String certificatePath) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("myhouse.marko@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(
                String.format("Dear %s,\n\nthe certificate you requested is in the attachment.\n\nThank you for using our service", name));

        FileSystemResource certificate
                = new FileSystemResource(new File(certificatePath));
        helper.addAttachment("Certificate", certificate);
        emailSender.send(message);
    }

    void sendWelcomeEmail(String to, String name, String surname) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("noreply@baeldung.com");
        message.setTo(to);
        message.setSubject("Thank you for registering | MyHouse Security");
        message.setText(
                String.format("Dear %s %s,\n\nthank you for taking the time to register with the MyHouse Home Security service\n\nSincerely,\nMyHouse team",
                        surname,
                        name));
        emailSender.send(message);
    }
}
