package com.federicoioan.alternativeschool.repository;

import com.federicoioan.alternativeschool.model.Course;
import com.federicoioan.alternativeschool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    List<Course> findCoursesByOwner(User owner);
}
