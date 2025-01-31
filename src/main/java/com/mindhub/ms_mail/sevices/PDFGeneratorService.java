package com.mindhub.ms_mail.sevices;

import com.lowagie.text.DocumentException;
import com.mindhub.ms_mail.dtos.OrderEntityDTO;

import java.io.ByteArrayInputStream;

public interface PDFGeneratorService {

    ByteArrayInputStream export(OrderEntityDTO order) throws DocumentException;
}
