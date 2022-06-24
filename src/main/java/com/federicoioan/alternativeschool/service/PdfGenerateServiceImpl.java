package com.federicoioan.alternativeschool.service;

import com.federicoioan.alternativeschool.service.IService.PdfGenerateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;


@Service
public class PdfGenerateServiceImpl implements PdfGenerateService {

    @Autowired
    private TemplateEngine templateEngine;

    @Value("${federico_ioan.pdf.directory}")
    private String folderPath;

    @Override
    public Resource generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName) {
        Context context = new Context();
        context.setVariables(data);

        String htmlContent = templateEngine.process(templateName, context);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(folderPath + "/" + pdfFileName);

            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(fileOutputStream, false);
            renderer.finishPDF();

            Path file = Paths.get(folderPath).resolve(pdfFileName);

            file.toFile().deleteOnExit();

            return new UrlResource(file.toUri());

        } catch (Exception e) {
            throw new RuntimeException("Could not generate the certificate!");
        }
    }
}