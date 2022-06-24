package com.federicoioan.alternativeschool.repository;

import com.federicoioan.alternativeschool.model.Course;
import com.federicoioan.alternativeschool.model.CourseEnrollment;
import com.federicoioan.alternativeschool.model.CourseEnrollmentKey;
import com.federicoioan.alternativeschool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CourseEnrollmentRepository extends JpaRepository<CourseEnrollment, CourseEnrollmentKey> {
    List<CourseEnrollment> findCourseEnrollmentByCourse(Course course);
    List<CourseEnrollment> findCourseEnrollmentByStudent(User student);
}
