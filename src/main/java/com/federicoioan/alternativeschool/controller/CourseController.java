package com.federicoioan.alternativeschool.controller;

import com.federicoioan.alternativeschool.model.Course;
import com.federicoioan.alternativeschool.model.CourseEnrollment;
import com.federicoioan.alternativeschool.model.Role;
import com.federicoioan.alternativeschool.model.User;
import com.federicoioan.alternativeschool.model.dto.CourseDto;
import com.federicoioan.alternativeschool.model.dto.EnrolledUserDto;
import com.federicoioan.alternativeschool.model.dto.RatingDto;
import com.federicoioan.alternativeschool.service.CourseEnrollmentServiceImpl;
import com.federicoioan.alternativeschool.service.CourseServiceImpl;
import com.federicoioan.alternativeschool.service.PdfGenerateServiceImpl;
import com.federicoioan.alternativeschool.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/courses")
public class CourseController {

	@Autowired
	private CourseServiceImpl courseService;

	@Autowired
	private UserServiceImpl userService;

	@Autowired
	private CourseEnrollmentServiceImpl courseEnrollmentService;

	@Autowired
	private PdfGenerateServiceImpl pdfGenerateService;

	@GetMapping
	@PreAuthorize("hasRole('STUDENT') or hasRole('ADMIN')")
	public ResponseEntity<?> getCourses() {
		try {
			List<Course> courses = courseService.findCourses();
			return ResponseEntity.ok(courses);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/manage")
	public ResponseEntity<?> getUserCourses() {
		try {
			User user = userService.getUserWithAuthorities()
					.orElseThrow(() -> new IllegalArgumentException("User not found"));

			Role role = user.getRoles().iterator().next();
			String roleName = role.getName().name();

			switch (roleName) {
				case "ROLE_TUTOR":
					List<Course> ownedCourses = courseService.findTutorCourses(user.getId());
					return ResponseEntity.ok(ownedCourses);
				case "ROLE_STUDENT":
					List<Course> enrolledCourses = courseEnrollmentService.findEnrolledCourses(user.getId());
					return ResponseEntity.ok(enrolledCourses);
				default:
					return ResponseEntity.badRequest().body("Current user has no course to manage");
			}

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{courseId}")
	public ResponseEntity<?> getCourse(@PathVariable Long courseId) {
		try {
			Course course = courseService.findCourse(courseId);
			return ResponseEntity.ok(course);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping
	@PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
	public ResponseEntity<?> createCourse(@Valid @RequestBody CourseDto course) {
		try {
			Course createdCourse = courseService.createCourse(course);
			return ResponseEntity.ok(createdCourse);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PutMapping("/{courseId}")
	@PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
	public ResponseEntity<?> updateCourse(@PathVariable Long courseId, @Valid @RequestBody CourseDto course) {
		try {
			Course updatedCourse = courseService.updateCourse(courseId, course);
			return ResponseEntity.ok(updatedCourse);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("/{courseId}")
	@PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
	public ResponseEntity<?> deleteCourse(@PathVariable Long courseId) {
		try {
			Course deletedCourse = courseService.deleteCourse(courseId);
			return ResponseEntity.ok(deletedCourse);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/{courseId}/certificate/{userId}")
	public ResponseEntity<?> getCertificate(@PathVariable Long courseId, @PathVariable Long userId) {
		try {
			Map<String, Object> certificateData = new HashMap<>();

			Course course = courseService.findCourse(courseId);
			User student = userService.findUser(userId);

			// Check if user is enrolled in the course
			if (! courseEnrollmentService.isEnrolled(courseId, userId))
				return ResponseEntity.badRequest().body("User is not enrolled in the course");

			if (!courseEnrollmentService.isUserCertificateEnabled(courseId, userId))
				return ResponseEntity.badRequest()
						.body("Certificate is not available, ask to the course owner for permission");

			certificateData.put("course", course);
			certificateData.put("student", student);

			String certificateName = student.getUsername() + "_" + course.getName() + ".pdf";

			certificateName = certificateName.replaceAll(" ", "_");

			Resource certificate = pdfGenerateService.generatePdfFile("certificate",
					certificateData, certificateName);

			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + certificate.getFilename() + "\"")
					.body(certificate);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("{courseId}/enroll/{userId}")
	public ResponseEntity<?> enrollUserToCourse(@PathVariable Long courseId, @PathVariable Long userId) {
		try {
			CourseEnrollment courseEnrollment = courseEnrollmentService.enrollUser(courseId, userId);
			return ResponseEntity.ok(courseEnrollment);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@DeleteMapping("{courseId}/unenroll/{userId}")
	public ResponseEntity<?> unenrollUserToCourse(@PathVariable Long courseId, @PathVariable Long userId) {
		try {
			CourseEnrollment courseEnrollment = courseEnrollmentService.unenrollUser(courseId, userId);
			return ResponseEntity.ok(courseEnrollment);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("{courseId}/rate")
	@PreAuthorize("hasRole('STUDENT')")
	public ResponseEntity<?> rateCourse(@PathVariable Long courseId, @Valid @RequestBody RatingDto rating) {
		try {
			User student = userService.getUserWithAuthorities()
					.orElseThrow(() -> new IllegalArgumentException("User not found"));

			CourseEnrollment courseEnrollment = courseEnrollmentService
					.rateCourse(courseId, student.getId(), rating.getRating());

			return ResponseEntity.ok(courseEnrollment);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@PostMapping("{courseId}/enable_certificate/{userId}")
	@PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
	public ResponseEntity<?> enableCertificate(@PathVariable Long courseId, @PathVariable Long userId) {
		try {
			CourseEnrollment courseEnrollment = courseEnrollmentService.enableCertificate(courseId, userId);
			return ResponseEntity.ok(courseEnrollment);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("{courseId}/students")
	@PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
	public ResponseEntity<?> getEnrolledStudents(@PathVariable Long courseId) {
		try {
			List<EnrolledUserDto> students = courseEnrollmentService.findEnrolledUsers(courseId);
			return ResponseEntity.ok(students);

		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
