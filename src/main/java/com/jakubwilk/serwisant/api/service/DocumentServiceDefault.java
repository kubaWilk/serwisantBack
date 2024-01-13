package com.jakubwilk.serwisant.api.service;

import com.jakubwilk.serwisant.api.utils.ApplicationProperties;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Service
public class DocumentServiceDefault implements DocumentService {
    private final ApplicationProperties applicationProperties;

    public DocumentServiceDefault(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }
    @Override
    public File getTempPdfFromAString(String source, String prefix, String suffix){
        try {
            File myPdf = File.createTempFile(prefix, suffix);
            OutputStream stream = new FileOutputStream(myPdf);

            ITextRenderer renderer = new ITextRenderer();
            renderer.getFontResolver().addFont(applicationProperties.getFont(), BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            renderer.setDocumentFromString(source);
            renderer.layout();
            renderer.createPDF(stream);

            return myPdf;
        } catch (DocumentException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
