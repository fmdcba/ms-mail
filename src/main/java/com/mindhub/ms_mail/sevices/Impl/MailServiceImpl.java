package com.mindhub.ms_mail.sevices.Impl;

import com.lowagie.text.DocumentException;
import com.mindhub.ms_mail.config.RabbitMQConfig;
import com.mindhub.ms_mail.dtos.OrderEntityDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.util.ByteArrayDataSource;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Service
public class MailServiceImpl {

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    PDFGeneratorServiceImpl pdfGeneratorService;

//    public void sendSimpleMessage(String to, String subject, String text) {
//        try {
//            SimpleMailMessage message = new SimpleMailMessage();
//            message.setFrom("testgrupo001@gmail.com");
//            message.setTo(to);
//            message.setSubject(subject);
//            message.setText(text);
//            emailSender.send(message);
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }

//    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
//    public void handleOrderCreated(OrderEntityDTO order) {
//        try {
//            String to = "daniaranda.003@gmail.com";
//            String subject = "Order ID: " + order.getId();
//            String text = "User ID: " + order.getUserId() + "\nOrder Status: " + order.getStatus();
//
//            sendSimpleMessage(to, subject, text);
//
//
//            System.out.println("Email sent to " + to + " for Order ID: " + order.getId());
//        } catch (Exception e) {
//            System.err.println("Error sending email: " + e.getMessage());
//        }
//    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
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
