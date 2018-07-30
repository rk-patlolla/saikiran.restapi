package com.restapi.app.service;

import java.util.List;

import com.restapi.app.exception.MySQLIntegrityConstraintViolationException;
import com.restapi.app.pojo.Course;
import com.restapi.app.pojo.Student;

public interface AdminService {

	public Course addCourse(Course course) throws MySQLIntegrityConstraintViolationException;

	public List<Course> getCourses();

	public Course deleteCourse(Course course);

	public Course getCourseById(Course course);

	public Course updateCourseByid(Course course);

	public Student updateStudentById(Student student) throws MySQLIntegrityConstraintViolationException;
}
