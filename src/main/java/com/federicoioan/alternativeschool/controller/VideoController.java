package com.federicoioan.alternativeschool.controller;

import com.federicoioan.alternativeschool.model.VideoDetails;
import com.federicoioan.alternativeschool.service.VideoDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/courses/{courseId}/modules/{moduleId}/videos")
public class VideoController {

    @Autowired
    private VideoDetailsServiceImpl videoDetailsService;

    @PostMapping
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> uploadVideo(@PathVariable Long moduleId, @RequestParam("file") MultipartFile file) {
        try {
            VideoDetails videoDetails = videoDetailsService.uploadVideo(moduleId, file);
            return ResponseEntity.ok(videoDetails);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getVideos(@PathVariable Long moduleId) {
        try {
            List<VideoDetails> videoDetailsList = videoDetailsService.findVideos(moduleId);
            return ResponseEntity.ok(videoDetailsList);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getVideo(@PathVariable Long id) {
        try {
            VideoDetails videoDetails = videoDetailsService.findVideo(id);
            return ResponseEntity.ok(videoDetails);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteVideo(@PathVariable Long id) {
        try {
            VideoDetails videoDetails = videoDetailsService.deleteVideo(id);
            return ResponseEntity.ok(videoDetails);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
