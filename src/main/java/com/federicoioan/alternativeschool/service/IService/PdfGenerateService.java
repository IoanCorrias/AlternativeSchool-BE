package com.federicoioan.alternativeschool.service.IService;

import org.springframework.core.io.Resource;

import java.util.Map;


public interface PdfGenerateService {
    Resource generatePdfFile(String templateName, Map<String, Object> data, String pdfFileName);
}
