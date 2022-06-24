package com.federicoioan.alternativeschool.controller;

import com.federicoioan.alternativeschool.model.Attachment;
import com.federicoioan.alternativeschool.service.AttachmentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/courses/{courseId}/modules/{moduleId}/attachments")
@CrossOrigin("http://localhost:8081")
public class AttachmentController {

    @Autowired
    private AttachmentServiceImpl attachmentService;

    @PostMapping
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> uploadAttachment(@PathVariable Long moduleId,
                                              @RequestParam("file") MultipartFile file) {
        try {
            Attachment attachment = attachmentService.uploadAttachment(moduleId, file);
            return ResponseEntity.ok(attachment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAttachments(@PathVariable Long moduleId) {
        try {
            List<Attachment> attachments = attachmentService.findAllAttachments(moduleId);
            return ResponseEntity.ok(attachments);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAttachment(@PathVariable Long id) {

        try {
            Attachment attachmentDetails = attachmentService.findAttachment(id);
            Resource attachment = attachmentService.loadAttachment(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + attachmentDetails.getName() + "\"")
                    .body(attachment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteAttachment(@PathVariable Long id) {
        try {
            Attachment attachment = attachmentService.deleteAttachment(id);
            return ResponseEntity.ok(attachment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
