package com.federicoioan.alternativeschool.repository;

import com.federicoioan.alternativeschool.model.CourseModule;
import com.federicoioan.alternativeschool.model.VideoDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoDetailsRepository extends JpaRepository<VideoDetails, Long> {
    List<VideoDetails> findByCourseModule(CourseModule courseModule);
}
