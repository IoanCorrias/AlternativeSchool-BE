package com.federicoioan.alternativeschool.repository;

import com.federicoioan.alternativeschool.model.Attachment;
import com.federicoioan.alternativeschool.model.CourseModule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    List<Attachment> findAllByCourseModule(CourseModule courseModule);
}
