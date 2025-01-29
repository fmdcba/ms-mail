package com.mindhub.ms_mail.sevices.Impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.mindhub.ms_mail.dtos.OrderEntityDTO;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class PDFGeneratorServiceImpl {

    public ByteArrayInputStream export(OrderEntityDTO order) throws DocumentException {
        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(20);
        Paragraph title = new Paragraph("New Order Successfully Created: " + order.getId(), fontTitle);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        document.add(title);

        document.add(Chunk.NEWLINE);

        Font fontHeader = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontHeader.setSize(16);
        Paragraph userHeader = new Paragraph("User Details", fontHeader);
        userHeader.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(userHeader);

        Font fontParagraph = FontFactory.getFont(FontFactory.HELVETICA);
        fontParagraph.setSize(14);
        Paragraph userDetails = new Paragraph(
                String.format("User ID: %s\nStatus: %s",
                        order.getUserId(),
                        order.getStatus()),
                fontParagraph);
        userDetails.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(userDetails);

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}
