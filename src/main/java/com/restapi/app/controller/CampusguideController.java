package com.restapi.app.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.app.exception.MySQLIntegrityConstraintViolationException;
import com.restapi.app.exception.StudentNotException;
import com.restapi.app.pojo.Course;
import com.restapi.app.pojo.RequestStatus;
import com.restapi.app.pojo.Student;
import com.restapi.app.service.AdminService;
import com.restapi.app.service.StudentService;

@RestController
@RequestMapping("/admin")
public class CampusguideController {
	@Autowired
	AdminService adminService;
	@Autowired
	StudentService studentService;

	@GetMapping("/hello")
	public String hello() {
		return "WELCOME TO RESTAPIAPP";
	}

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value = "/addCourse", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addCourse(@RequestBody Course newcourse)
			throws MySQLIntegrityConstraintViolationException {
		Course course = adminService.addCourse(newcourse);
		if (course != null) {
			return new ResponseEntity<>(new RequestStatus("Record SucessFully Added"), HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/getCourses")
	public List<Course> getCourses() {
		List<Course> courseList = adminService.getCourses();
		if (courseList.size() != 0) {
			return courseList;
		}
		return (List<Course>) new ResponseEntity(new RequestStatus("Records Not Available"), HttpStatus.BAD_REQUEST);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/getStudents")
	public List<Student> getStudent() {
		List<Student> studentList = studentService.getStudents();
		if (studentList.size() != 0) {
			return studentList;
		}
		return (List<Student>) new ResponseEntity(new RequestStatus("Records Not Available"), HttpStatus.BAD_REQUEST);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "deleteCourse/{course_id}")
	public ResponseEntity<?> deleteCourse(@PathVariable long course_id, Course course) {
		course.setcId(course_id);
		if (adminService.getCourseById(course) != null) {
			Course courses = adminService.deleteCourse(course);
			if (courses == null) {
				return new ResponseEntity<>(new RequestStatus("Record Not Deleted"), HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(new RequestStatus("Record Deleted Sucessfully"), HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(new RequestStatus("Record For This " + course.getCourseName() + " Not Found"),
				HttpStatus.BAD_REQUEST);
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping(value = "updateCourse/{course_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCourses(@PathVariable long course_id, @RequestBody Course course) {
		course.setcId(course_id);
		if (adminService.getCourseById(course) != null) {
			Course courses = adminService.updateCourseByid(course);
			if (courses == null) {
				return new ResponseEntity<>(new RequestStatus("Record Not Updated"), HttpStatus.BAD_REQUEST);
			}
			return new ResponseEntity<>(new RequestStatus("Record Updated Sucessfully"), HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(new RequestStatus("Record For This " + course.getCourseName() + " Not Found"),
				HttpStatus.BAD_REQUEST);

	}

	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping(value = "deleteStudent/{s_id}")
	public ResponseEntity<?> deleteStudentById(@PathVariable long s_id, Student student)
			throws StudentNotException, com.restapi.app.exception.MySQLIntegrityConstraintViolationException {
		student.setsId(s_id);

		if (studentService.checkStudentAvailability(student)) {
			boolean deleted = studentService.deleteStudentById(student);
			if (deleted) {
				return new ResponseEntity<>(new RequestStatus("Record Deleted Sucessfully"), HttpStatus.ACCEPTED);
			}
		}
		return new ResponseEntity<>(new RequestStatus("Record Not Available Please Check And Retry"),
				HttpStatus.BAD_REQUEST);
	}

	@PutMapping(value = "updateStudent/{s_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateStudentById(@PathVariable long s_id, @RequestBody Student newstudent)
			throws StudentNotException, MySQLIntegrityConstraintViolationException {
		newstudent.setsId(s_id);
		if (studentService.checkStudentAvailability(newstudent)) {
			Student updatestatus = adminService.updateStudentById(newstudent);
			if (updatestatus != null) {
				return new ResponseEntity<>(new RequestStatus("Record Updated Sucessfully"), HttpStatus.ACCEPTED);
			}
		}
		return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	}

}
