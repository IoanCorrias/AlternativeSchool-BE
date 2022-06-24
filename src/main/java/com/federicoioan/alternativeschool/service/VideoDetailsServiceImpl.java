package com.federicoioan.alternativeschool.service;

import com.clickntap.vimeo.Vimeo;
import com.clickntap.vimeo.VimeoException;
import com.clickntap.vimeo.VimeoResponse;
import com.federicoioan.alternativeschool.model.CourseModule;
import com.federicoioan.alternativeschool.model.VideoDetails;
import com.federicoioan.alternativeschool.repository.CourseModuleRepository;
import com.federicoioan.alternativeschool.repository.VideoDetailsRepository;
import com.federicoioan.alternativeschool.service.IService.VideoDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;


@Service
public class VideoDetailsServiceImpl implements VideoDetailsService {

    private final Vimeo vimeo = new Vimeo("33cf31f02fad348a5ff79f204c215ce7");

    private String subDirectory = "videos";

    @Autowired
    private CourseModuleRepository courseModuleRepository;

    @Autowired
    private VideoDetailsRepository videoDetailsRepository;

    @Autowired
    private VimeoStorageServiceImpl vimeoStorageService;

    public VideoDetails uploadVideo(Long courseModuleId, MultipartFile file) {

        // Check if course module exists
        if (!courseModuleRepository.existsById(courseModuleId))
            throw new IllegalArgumentException("Course module not found");

        // Check if file is not empty
        if (file.isEmpty())
            throw new IllegalArgumentException("File is empty");

        // Save video to subdirectory
        File video = vimeoStorageService.save(file, subDirectory);

        try {
            // Upload video to Vimeo
            String videoEndPoint = vimeo.addVideo(video, true);

            // Delete video from local storage
            vimeoStorageService.deleteAll(subDirectory);

            // Get video info from Vimeo
            VimeoResponse videoInfo = vimeo.getVideoInfo(videoEndPoint);

            // Create VideoDetails object
            VideoDetails videoDetails = new VideoDetails();

            // Set video details properties
            videoDetails.setName(file.getOriginalFilename());
            videoDetails.setUrl(videoInfo.getJson().getString("link"));
            videoDetails.setEndPointVimeo(videoEndPoint);

            CourseModule courseModule = courseModuleRepository.findById(courseModuleId)
                    .orElseThrow(() -> new IllegalArgumentException("Course module not found"));
            videoDetails.setCourseModule(courseModule);

            // Save video details
            return videoDetailsRepository.save(videoDetails);

        } catch (IOException | VimeoException e) {
            // Delete video from subdirectory even if upload failed
            vimeoStorageService.deleteAll(subDirectory);

            throw new RuntimeException("Could not upload video to Vimeo");
        }
    }

    public List<VideoDetails> findVideos(Long courseModuleId) {
        if(!courseModuleRepository.existsById(courseModuleId))
            throw new IllegalArgumentException("Course module not found");

        CourseModule courseModule = courseModuleRepository.findById(courseModuleId)
                .orElseThrow(() -> new IllegalArgumentException("Course module not found"));

        return videoDetailsRepository.findByCourseModule(courseModule);
    }

    public VideoDetails findVideo(Long id) {
        return videoDetailsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Video not found"));
    }

    public VideoDetails deleteVideo(Long id) {
        // Get video details
        VideoDetails videoDetails = videoDetailsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));

        String videoEndPoint = videoDetails.getEndPointVimeo();

        try {
            // Delete video from Vimeo using Vimeo API
            vimeo.removeVideo(videoEndPoint);

            // Delete video details from database
            videoDetailsRepository.delete(videoDetails);

            return videoDetails;

        } catch (IOException e) {
            throw new RuntimeException("Error deleting video from Vimeo");
        }
    }
}