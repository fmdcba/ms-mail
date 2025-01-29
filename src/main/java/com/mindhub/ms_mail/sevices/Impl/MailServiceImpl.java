package com.mindhub.ms_mail.sevices.Impl;

import com.mindhub.ms_mail.config.RabbitMQConfig;
import com.mindhub.ms_mail.dtos.OrderEntityDTO;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl {

    @Autowired
    private JavaMailSender emailSender;

    public void sendSimpleMessage(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("testgrupo001@gmail.com");
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            emailSender.send(message);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @RabbitListener(queues = RabbitMQConfig.ORDER_CREATED_QUEUE)
    public void handleOrderCreated(OrderEntityDTO order) {
        try {
            // Define email details
            String to = "daniaranda.003@gmail.com";
            String subject = "Order ID: " + order.getId();
            String text = "User ID: " + order.getUserId() + "\nOrder Status: " + order.getStatus();

            // Send Email
            sendSimpleMessage(to, subject, text);

            System.out.println("Email sent to " + to + " for Order ID: " + order.getId());
        } catch (Exception e) {
            System.err.println("Error sending email: " + e.getMessage());
            // Optionally, implement retry logic or move the message to a dead-letter queue
        }
    }
}
