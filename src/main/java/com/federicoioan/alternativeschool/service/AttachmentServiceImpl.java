package com.federicoioan.alternativeschool.service;

import com.federicoioan.alternativeschool.model.Attachment;
import com.federicoioan.alternativeschool.model.CourseModule;
import com.federicoioan.alternativeschool.repository.AttachmentRepository;
import com.federicoioan.alternativeschool.repository.CourseModuleRepository;
import com.federicoioan.alternativeschool.service.IService.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private StorageServiceImpl storageService;

    @Autowired
    private CourseModuleRepository courseModuleRepository;

    public Attachment uploadAttachment(Long courseModuleId, MultipartFile file) {
        // Check if file is empty
        if (file.isEmpty())
            throw new RuntimeException("File is empty");

        // Get course module
        CourseModule courseModule = courseModuleRepository.findById(courseModuleId)
                .orElseThrow(() -> new RuntimeException("Course module not found"));

        // Create attachment
        Attachment attachment = new Attachment();

        // Build attachment code
        String code = courseModule.getCourse().getName() + "-" +
                courseModule.getName() + "-" +
                file.getOriginalFilename();

        code = code.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        // Get all module attachments
        List<Attachment> attachments = attachmentRepository.findAllByCourseModule(courseModule);

        //If code already exists in module throw exception
        for (Attachment a : attachments) {
            if (a.getCode().equals(code))
                throw new RuntimeException("This file already exists");
        }

        // Set attachment properties
        attachment.setCode(code);
        attachment.setName(file.getOriginalFilename() );
        attachment.setType(file.getContentType());
        attachment.setSize(file.getSize());
        attachment.setCourseModule(courseModule);

        // Save file to storage
        storageService.save(file, code);

        return attachmentRepository.save(attachment);
    }

    public List<Attachment> findAllAttachments(Long courseModuleId) {
        // Get course module
        CourseModule courseModule = courseModuleRepository.findById(courseModuleId)
                .orElseThrow(() -> new RuntimeException("Course module not found"));

        // Get all attachments for course module
        return attachmentRepository.findAllByCourseModule(courseModule);
    }

    public Attachment findAttachment(Long id) {
        return attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));
    }

    public Resource loadAttachment(Long id) {
        // Get attachment
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));

        // Get file from storage
        return storageService.load(attachment.getCode());
    }

    public Attachment deleteAttachment(Long id) {
        // Get attachment
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attachment not found"));

        // Delete file from storage
        storageService.delete(attachment.getCode());

        // Delete attachment from database
        attachmentRepository.delete(attachment);

        return attachment;
    }

    public void deleteAllAttachments(Long courseModuleId) {
        // Get course module
        CourseModule courseModule = courseModuleRepository.findById(courseModuleId)
                .orElseThrow(() -> new RuntimeException("Course module not found"));

        // Get all attachments for course module
        List<Attachment> attachments = attachmentRepository.findAllByCourseModule(courseModule);

        // Delete all attachments from storage
        for (Attachment attachment : attachments) {
            storageService.delete(attachment.getCode());
        }

        // Delete all attachments from database
        attachmentRepository.deleteAll(attachments);
    }
}
