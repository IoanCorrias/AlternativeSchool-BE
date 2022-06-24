package com.federicoioan.alternativeschool.controller;

import com.federicoioan.alternativeschool.model.Assignment;
import com.federicoioan.alternativeschool.model.Role;
import com.federicoioan.alternativeschool.model.User;
import com.federicoioan.alternativeschool.model.dto.ScoreDto;
import com.federicoioan.alternativeschool.service.AssignmentServiceImpl;
import com.federicoioan.alternativeschool.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/courses/{courseId}/delivery_folders/{folderId}/assignments")
public class AssignmentController {

    @Autowired
    private AssignmentServiceImpl assignmentService;

    @Autowired
    private UserServiceImpl userService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
    public ResponseEntity<?> createAssignment(@PathVariable Long folderId, @RequestParam("file") MultipartFile file) {
        try {
            User user = userService.getUserWithAuthorities()
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            Long studuntId = user.getId();

            Assignment assignment = assignmentService.uploadAssignment(studuntId, folderId, file);
            return ResponseEntity.ok(assignment);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllAssignments(@PathVariable Long folderId) {
        try {

            User user = userService.getUserWithAuthorities()
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            Role role = user.getRoles().iterator().next();
            String roleName = role.getName().name();

            List<Assignment> assignments;

            if (roleName.equals("ROLE_STUDENT"))
                assignments = assignmentService.findStudentAssignments(folderId, user.getId());
            else
                assignments = assignmentService.findAllAssignments(folderId);

            return ResponseEntity.ok(assignments);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssignment(@PathVariable Long id) {
        try {
            Assignment assignmentDetails = assignmentService.findAssignment(id);
            Resource attachment = assignmentService.loadAssignment(id);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + assignmentDetails.getName() + "\"")
                    .body(attachment);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteAssignment(@PathVariable Long id) {
        try {
            Assignment assignment = assignmentService.deleteAssignment(id);
            return ResponseEntity.ok(assignment);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> scoreAssignment(@PathVariable Long id, @Valid @RequestBody ScoreDto score) {
        try {
            Assignment assignment = assignmentService.scoreAssignment(id, score);
            return ResponseEntity.ok(assignment);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
