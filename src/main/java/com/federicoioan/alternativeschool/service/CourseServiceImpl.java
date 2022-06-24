package com.federicoioan.alternativeschool.service;

import com.federicoioan.alternativeschool.model.Course;
import com.federicoioan.alternativeschool.model.User;
import com.federicoioan.alternativeschool.model.dto.CourseDto;
import com.federicoioan.alternativeschool.repository.CourseRepository;
import com.federicoioan.alternativeschool.repository.UserRepository;
import com.federicoioan.alternativeschool.service.IService.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    public List<Course> findCourses() {
        return courseRepository.findAll();
    }

    public List<Course> findTutorCourses(Long ownerId) {
        User owner = userRepository.findById(ownerId)
            .orElseThrow(() -> new RuntimeException("Owner not found"));

        return courseRepository.findCoursesByOwner(owner);
    }

    public Course findCourse(Long id) {
        return courseRepository.findById(id).orElseThrow(() -> new RuntimeException("Course not found"));
    }

    public Course createCourse(CourseDto course) {
        Course courseToCreate = new Course();

        courseToCreate.setName(course.getName());
        courseToCreate.setDescription(course.getDescription());

        User owner = userRepository.findById(course.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        courseToCreate.setOwner(owner);

        return courseRepository.save(courseToCreate);
    }

    public Course updateCourse(Long id, CourseDto course) {
        Course courseToUpdate = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        courseToUpdate.setName(course.getName());
        courseToUpdate.setDescription(course.getDescription());

        return courseRepository.save(courseToUpdate);
    }

    public Course deleteCourse(Long id) {
        Course courseToDelete = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        courseRepository.delete(courseToDelete);
        return courseToDelete;
    }
}