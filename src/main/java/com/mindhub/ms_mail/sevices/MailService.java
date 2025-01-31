package com.mindhub.ms_mail.sevices;

import com.mindhub.ms_mail.dtos.NewUserDTO;
import com.mindhub.ms_mail.dtos.OrderEntityDTO;

public interface MailService {

    void sendSimpleMessage(String to, String subject, String text);

    void sendWelcomeEmail(NewUserDTO user);

    void sendOrderEmailWithPdf(OrderEntityDTO order);
}
