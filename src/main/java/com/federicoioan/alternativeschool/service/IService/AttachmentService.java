package com.federicoioan.alternativeschool.service.IService;

import com.federicoioan.alternativeschool.model.Attachment;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface AttachmentService {

    Attachment uploadAttachment(Long courseModuleId, MultipartFile file);

    List<Attachment> findAllAttachments(Long courseModuleId);

    Attachment findAttachment(Long id);

    Resource loadAttachment(Long id);

    Attachment deleteAttachment(Long id);

    void deleteAllAttachments(Long courseModuleId);
}
