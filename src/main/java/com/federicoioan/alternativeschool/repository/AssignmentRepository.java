package com.federicoioan.alternativeschool.repository;

import com.federicoioan.alternativeschool.model.Assignment;
import com.federicoioan.alternativeschool.model.DeliveryFolder;
import com.federicoioan.alternativeschool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findAllByDeliveryFolder(DeliveryFolder deliveryFolder);
    List<Assignment> findAllByDeliveryFolderAndStudent(DeliveryFolder deliveryFolder, User student);
}
