package com.federicoioan.alternativeschool.service.IService;

import com.federicoioan.alternativeschool.model.CourseModule;
import com.federicoioan.alternativeschool.model.dto.CourseModuleDto;

import java.util.List;


public interface CourseModuleService {

    CourseModule createCourseModule(Long courseId, CourseModuleDto courseModule);

    List<CourseModule> findCourseModules(Long courseId);

    CourseModule findCourseModule(Long id);

    CourseModule updateCourseModule(Long id, CourseModuleDto courseModule);

    CourseModule deleteCourseModule(Long id);
}
