package com.federicoioan.alternativeschool.service.IService;

import com.federicoioan.alternativeschool.model.VideoDetails;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VideoDetailsService {

    VideoDetails uploadVideo(Long courseModuleId, MultipartFile file);

    List<VideoDetails> findVideos(Long courseModuleId);

    VideoDetails findVideo(Long id);

    VideoDetails deleteVideo(Long id);
}
