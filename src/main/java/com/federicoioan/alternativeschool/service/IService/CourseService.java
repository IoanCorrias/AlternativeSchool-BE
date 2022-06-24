package com.federicoioan.alternativeschool.service.IService;


import com.federicoioan.alternativeschool.model.Course;
import com.federicoioan.alternativeschool.model.dto.CourseDto;

import java.util.List;

public interface CourseService {

    List<Course> findCourses();

    List<Course> findTutorCourses(Long ownerId);

    Course findCourse(Long id);

    Course createCourse(CourseDto course);

    Course updateCourse(Long id, CourseDto course);

    Course deleteCourse(Long id);
}
