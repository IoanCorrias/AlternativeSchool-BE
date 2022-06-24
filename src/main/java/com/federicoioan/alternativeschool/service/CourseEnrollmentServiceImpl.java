package com.federicoioan.alternativeschool.service;

import com.federicoioan.alternativeschool.model.Course;
import com.federicoioan.alternativeschool.model.CourseEnrollment;
import com.federicoioan.alternativeschool.model.CourseEnrollmentKey;
import com.federicoioan.alternativeschool.model.User;
import com.federicoioan.alternativeschool.model.dto.EnrolledUserDto;
import com.federicoioan.alternativeschool.repository.CourseEnrollmentRepository;
import com.federicoioan.alternativeschool.repository.CourseRepository;
import com.federicoioan.alternativeschool.repository.UserRepository;
import com.federicoioan.alternativeschool.service.IService.CourseEnrollmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CourseEnrollmentServiceImpl implements CourseEnrollmentService {

    @Autowired
    CourseEnrollmentRepository courseEnrollmentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    public CourseEnrollment enrollUser(Long courseId, Long userId) {

        if (!courseRepository.existsById(courseId))
            throw new RuntimeException("Course not found");

        if (!userRepository.existsById(userId))
            throw new RuntimeException("User not found");

        Course course = courseRepository.findById(courseId).get();
        User student = userRepository.findById(userId).get();

        CourseEnrollment courseEnrollment = new CourseEnrollment();

        courseEnrollment.setId(new CourseEnrollmentKey(courseId, userId));
        courseEnrollment.setCourse(course);
        courseEnrollment.setStudent(student);
        courseEnrollment.setCertificateEnabled(false);

        courseEnrollmentRepository.save(courseEnrollment);

        return courseEnrollment;
    }

    public CourseEnrollment unenrollUser(Long courseId, Long userId) {
        if (!courseRepository.existsById(courseId))
        throw new RuntimeException("Course not found");

        if (!userRepository.existsById(userId))
            throw new RuntimeException("User not found");

        CourseEnrollment courseEnrollment = courseEnrollmentRepository
                .findById(new CourseEnrollmentKey(courseId, userId))
                .orElseThrow(() -> new RuntimeException("User is not enrolled in this course"));

        courseEnrollmentRepository.delete(courseEnrollment);

        return courseEnrollment;
    }

    public CourseEnrollment rateCourse(Long courseId, Long userId, Integer rating) {
        if (!courseRepository.existsById(courseId))
            throw new RuntimeException("Course not found");

        if (!userRepository.existsById(userId))
            throw new RuntimeException("User not found");

        CourseEnrollment courseEnrollment = courseEnrollmentRepository
                .findById(new CourseEnrollmentKey(courseId, userId))
                .orElseThrow(() -> new RuntimeException("User is not enrolled in this course"));

        courseEnrollment.setRating(rating);
        courseEnrollmentRepository.save(courseEnrollment);

        return courseEnrollment;
    }

    public CourseEnrollment enableCertificate(Long courseId, Long userId) {
        if (!courseRepository.existsById(courseId))
            throw new RuntimeException("Course not found");

        if (!userRepository.existsById(userId))
            throw new RuntimeException("User not found");

        CourseEnrollment courseEnrollment = courseEnrollmentRepository
                .findById(new CourseEnrollmentKey(courseId, userId))
                .orElseThrow(() -> new RuntimeException("User is not enrolled in this course"));

        courseEnrollment.setCertificateEnabled(!courseEnrollment.isCertificateEnabled());
        courseEnrollmentRepository.save(courseEnrollment);

        return courseEnrollment;
    }

    public Boolean isUserCertificateEnabled(Long courseId, Long userId) {
        if (!courseRepository.existsById(courseId))
            throw new RuntimeException("Course not found");

        if (!userRepository.existsById(userId))
            throw new RuntimeException("User not found");

        CourseEnrollment courseEnrollment = courseEnrollmentRepository
                .findById(new CourseEnrollmentKey(courseId, userId))
                .orElseThrow(() -> new RuntimeException("User is not enrolled in this course"));

        return courseEnrollment.isCertificateEnabled();
    }

    public List<EnrolledUserDto> findEnrolledUsers(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        List<CourseEnrollment> courseEnrollments = courseEnrollmentRepository.findCourseEnrollmentByCourse(course);

        List<EnrolledUserDto> enrolledStudents = new ArrayList<>();

        for (CourseEnrollment courseEnrollment: courseEnrollments) {
            User student = courseEnrollment.getStudent();
            EnrolledUserDto enrolledUserDto = new EnrolledUserDto();

            enrolledUserDto.setId(student.getId());
            enrolledUserDto.setName(student.getName());
            enrolledUserDto.setSurname(student.getSurname());
            enrolledUserDto.setUsername(student.getUsername());
            enrolledUserDto.setEmail(student.getEmail());
            enrolledUserDto.setCertificateEnabled(courseEnrollment.isCertificateEnabled());

            enrolledStudents.add(enrolledUserDto);
        }

        return  enrolledStudents;
    }

    public List<Course> findEnrolledCourses(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        List<CourseEnrollment> courseEnrollments = courseEnrollmentRepository.findCourseEnrollmentByStudent(user);

        List<Course> enrolledCourses = new ArrayList<>();

        for (CourseEnrollment courseEnrollment: courseEnrollments)
            enrolledCourses.add(courseEnrollment.getCourse());

        return  enrolledCourses;
    }

    public boolean isEnrolled(Long courseId, Long studentId) {
        if (!courseRepository.existsById(courseId))
            throw new RuntimeException("Course not found");

        if (!userRepository.existsById(studentId))
            throw new RuntimeException("User not found");

        CourseEnrollment courseEnrollment = courseEnrollmentRepository
                .findById(new CourseEnrollmentKey(courseId, studentId))
                .orElseThrow(() -> new RuntimeException("User is not enrolled in this course"));

        return true;
    }
}