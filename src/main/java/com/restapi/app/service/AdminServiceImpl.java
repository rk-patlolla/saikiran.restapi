package com.restapi.app.service;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restapi.app.exception.MySQLIntegrityConstraintViolationException;
import com.restapi.app.pojo.Course;
import com.restapi.app.pojo.Student;
import com.restapi.app.repository.CourseRepository;
import com.restapi.app.repository.StudentRepository;


@Service
public class AdminServiceImpl implements AdminService {
	@Autowired
	CourseRepository courseReps;
	@Autowired
	StudentRepository studentRes;

	@Override
	public Course addCourse(Course course)throws MySQLIntegrityConstraintViolationException {
		try {
			courseReps.save(course);
		} catch (Exception e) {
			throw new MySQLIntegrityConstraintViolationException("This Course  Criteria Already Exits");
		}
		return course;
	}

	@Override
	public List<Course> getCourses() {
		List<Course> courselist = courseReps.findAll();
		return courselist;
	}

	@Override
	public Course deleteCourse(Course course) {
		try {
			courseReps.deleteById(course.getcId());
		} catch (Exception e) {
			course = null;
		}
		return course;
	}

	@Override
	public Course getCourseById(Course course) {
		try {
			courseReps.findById(course.getcId());
		} catch (Exception e) {
			e.printStackTrace();
			course = null;
		}
		return course;
	}

	@Override
	public Course updateCourseByid(Course course) {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {
			course.setUpdatedDate(timestamp);
			courseReps.save(course);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			course = null;
		}
		return course;
	}

	@Override
	public Student updateStudentById(Student student)throws MySQLIntegrityConstraintViolationException {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		try {
			Student currentStudent = studentRes.findById(student.getsId()).get();
			currentStudent.setStudentName(student.getStudentName());
			currentStudent.setStudentType(student.getStudentType());
			currentStudent.setUpdated(timestamp);
			studentRes.save(currentStudent);
		} catch (Exception e) {
			throw new MySQLIntegrityConstraintViolationException("Can't Update With This Data Please Check ");
		}
		return student;
	}
}
