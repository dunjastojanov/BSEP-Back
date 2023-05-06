package com.myhouse.MyHouse.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class MailService {

    @Autowired
    private final JavaMailSender emailSender;

    @Value("${frontend.link}")
    private String FRONTEND_LINK;

    public MailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }


    void sendCertificate(String to, String name, String certificatePath) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("myhouse.marko@gmail.com");
        helper.setTo(to);
        helper.setSubject("Your certificate | MyHouse Security");
        helper.setText(
                String.format("Dear %s,\n\nthe certificate you requested is in the attachment.\n\nThank you for using our service,\nMyHouse Security team", name));
        FileSystemResource certificate
                = new FileSystemResource(new File(certificatePath));
        helper.addAttachment("my-house-certificate.pem", certificate);
        emailSender.send(message);
    }

    void sendWelcomeEmail(String to, String name, String surname, String link) {
        link = FRONTEND_LINK + "api/user/register/verification/" + link;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("myhouse.marko@gmail.com");
        message.setTo(to);
        message.setSubject("Thank you for registering | MyHouse Security");
        message.setText(
                String.format("Dear %s %s,\n\nthank you for taking the time to register with the MyHouse Home Security service" +
                                "\n\nThis is the link to verify your registration" + link + "\n\nSincerely,\nMyHouse team",
                        surname,
                        name));
        emailSender.send(message);
    }
}
