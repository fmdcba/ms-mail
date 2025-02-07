package com.mindhub.ms_mail.sevices.Impl;

import com.lowagie.text.DocumentException;
import com.mindhub.ms_mail.config.RabbitMQConfig;
import com.mindhub.ms_mail.dtos.NewUserDTO;
import com.mindhub.ms_mail.dtos.OrderEntityDTO;
import com.mindhub.ms_mail.sevices.MailService;
import com.mindhub.ms_mail.sevices.PDFGeneratorService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;

@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private PDFGeneratorService pdfGeneratorService;

    @Override
    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("testgrupo001@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);

            System.out.println("Simple email sent to " + to + " with subject: " + subject);
        } catch (Exception e) {
            System.err.println("Error sending simple email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RabbitMQConfig.USER_CREATED_QUEUE)
    @Override
    public void sendWelcomeEmail(NewUserDTO user) {
        try {
            String to = user.email();
            String subject = "Welcome to OrdersAPP, " + user.username() + "!";
            String text = "Hello '" + user.username()+ "'" +"\n\nWelcome to our platform! We're happy to have you.\nNext step is verifying your account.\n\nBest regards. Grupo 1 Team";

            sendSimpleMessage(to, subject, text);

            System.out.println("Welcome email sent to " + to + " for User ID: " + user.id());
        } catch (Exception e) {
            System.err.println("Error sending welcome email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RabbitMQConfig.USER_VALIDATED_QUEUE)
    @Override
    public void sendUserValidationKey(String validation) {
        try {
            String to = "daniaranda.003@gmail.com";
            String subject = "Please validate your account to login";
            String text = "For validating your account make a GET request at localhost:8080/api/auth/validate?token=" + validation;

            sendSimpleMessage(to, subject, text);

            System.out.println("Validation mail sent to " + to);
        } catch (Exception e) {
            System.err.println("Error sending welcome email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
    @Override
    public void sendOrderEmailWithPdf(OrderEntityDTO order) {
        try {
            String to = "daniaranda.003@gmail.com";

            System.out.println("Preparing to send email to: " + to);

            MimeMessage message = emailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("testgrupo001@gmail.com");
            helper.setTo(to);
            helper.setSubject("Order ID: " + order.getId());
            helper.setText("Please find attached the details of your order.", false);

            ByteArrayInputStream bis = pdfGeneratorService.export(order);
            if (bis != null) {
                byte[] pdfBytes = bis.readAllBytes();

                ByteArrayDataSource dataSource = new ByteArrayDataSource(pdfBytes, "application/pdf");

                helper.addAttachment("Order_" + order.getId() + ".pdf", dataSource);
            }

            emailSender.send(message);
            System.out.println("Email with PDF sent to " + to + " for Order ID: " + order.getId());

        } catch (MessagingException | DocumentException e) {
            System.err.println("Error sending email with PDF: " + e.getMessage());
            e.printStackTrace();
        }
    }
}