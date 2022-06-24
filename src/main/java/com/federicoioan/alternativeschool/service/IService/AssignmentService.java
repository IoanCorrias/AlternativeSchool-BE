package com.federicoioan.alternativeschool.service.IService;

import com.federicoioan.alternativeschool.model.Assignment;
import com.federicoioan.alternativeschool.model.dto.ScoreDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface AssignmentService {

    Assignment uploadAssignment(Long studentId, Long deliveryFolderId, MultipartFile file);

    List<Assignment> findAllAssignments(Long deliveryFolderId);

    Assignment findAssignment(Long id);

    Resource loadAssignment(Long id);

    Assignment deleteAssignment(Long id);

    Assignment scoreAssignment(Long id, ScoreDto score);
}
