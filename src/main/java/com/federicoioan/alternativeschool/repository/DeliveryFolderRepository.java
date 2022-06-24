package com.federicoioan.alternativeschool.repository;

import com.federicoioan.alternativeschool.model.Course;
import com.federicoioan.alternativeschool.model.DeliveryFolder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryFolderRepository extends JpaRepository<DeliveryFolder, Long> {
    public List<DeliveryFolder> findDeliveryFoldersByCourse(Course course);
}
