package com.federicoioan.alternativeschool.service;

import com.federicoioan.alternativeschool.model.Assignment;
import com.federicoioan.alternativeschool.model.DeliveryFolder;
import com.federicoioan.alternativeschool.model.User;
import com.federicoioan.alternativeschool.model.dto.ScoreDto;
import com.federicoioan.alternativeschool.repository.AssignmentRepository;
import com.federicoioan.alternativeschool.repository.DeliveryFolderRepository;
import com.federicoioan.alternativeschool.repository.UserRepository;
import com.federicoioan.alternativeschool.service.IService.AssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class AssignmentServiceImpl implements AssignmentService {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Autowired
    private StorageServiceImpl storageService;

    @Autowired
    private DeliveryFolderRepository deliveryFolderRepository;

    @Autowired
    private UserRepository userRepository;

    public Assignment uploadAssignment(Long studentId, Long deliveryFolderId, MultipartFile file) {
        // Check if file is not empty
        if (file.isEmpty())
            throw new RuntimeException("File is empty");

        // Get student
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        // Get delivery folder
        DeliveryFolder deliveryFolder = deliveryFolderRepository.findById(deliveryFolderId)
                .orElseThrow(() -> new RuntimeException("Delivery folder not found"));

        // Check if now is the right time to upload assignments
        if (deliveryFolder.getEndDeliveryTime().isBefore(LocalDateTime.now()))
            throw new RuntimeException("It is too late to upload assignments");

        if (deliveryFolder.getStartDeliveryTime().isAfter(LocalDateTime.now()))
            throw new RuntimeException("It is too early to upload assignments");

        // Create assignment
        Assignment assignment = new Assignment();

        // Build assignment code
        String code = deliveryFolder.getCourse().getName() + "-" +
                deliveryFolder.getName() + "-" +
                file.getOriginalFilename();

        code = code.replaceAll("[^a-zA-Z0-9]", "").toLowerCase();

        // Get all folder assignments
        List<Assignment> assignments = assignmentRepository.findAllByDeliveryFolder(deliveryFolder);

        //If code already exists in folder throw exception
        for (Assignment a : assignments) {
            if (a.getCode().equals(code))
                throw new RuntimeException("This file already exists");
        }

        // Set assignment properties
        assignment.setCode(code);
        assignment.setName(file.getOriginalFilename() );
        assignment.setType(file.getContentType());
        assignment.setSize(file.getSize());
        assignment.setDeliveryFolder(deliveryFolder);
        assignment.setStudent(student);

        // Save assignment to storage
        storageService.save(file, code);

        return assignmentRepository.save(assignment);

    }

    public List<Assignment> findAllAssignments(Long deliveryFolderId) {
        // Get delivery folder
        DeliveryFolder deliveryFolder = deliveryFolderRepository.findById(deliveryFolderId)
                .orElseThrow(() -> new RuntimeException("Delivery folder not found"));

        // Get all assignments for delivery folder
        return assignmentRepository.findAllByDeliveryFolder(deliveryFolder);
    }

    public List<Assignment> findStudentAssignments(Long deliveryFolderId, Long studentId) {
        // Get delivery folder
        DeliveryFolder deliveryFolder = deliveryFolderRepository.findById(deliveryFolderId)
                .orElseThrow(() -> new RuntimeException("Delivery folder not found"));

        // Get student
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return assignmentRepository.findAllByDeliveryFolderAndStudent(deliveryFolder, student);
    }

    public Assignment findAssignment(Long id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
    }

    public Resource loadAssignment(Long id) {
        // Get assignment
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Get file from storage
        return storageService.load(assignment.getCode());
    }

    public Assignment deleteAssignment(Long id) {
        // Get assignment
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Delete file from storage
        storageService.delete(assignment.getCode());

        // Delete assignment
        assignmentRepository.delete(assignment);

        return assignment;
    }

    public Assignment scoreAssignment(Long id, ScoreDto score) {
        // Get assignment
        Assignment assignment = assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));

        // Set score
        assignment.setScore(score.getScore());

        // Save assignment
        return assignmentRepository.save(assignment);
    }
}