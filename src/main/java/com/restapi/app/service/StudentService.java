package com.restapi.app.service;

import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.restapi.app.exception.MySQLIntegrityConstraintViolationException;
import com.restapi.app.exception.StudentNotException;
import com.restapi.app.pojo.Student;

public interface StudentService {

	/* using this same method user existance while logging in */
	public String checkMobileNoForReg(String mobileNo) throws UsernameNotFoundException;

	public Student findUserByMobileNo(String mobileNo);

	public Student registerStudent(Student student) throws StudentNotException;

	public boolean deleteStudentById(Student student) throws MySQLIntegrityConstraintViolationException;

	public boolean checkStudentAvailability(Student student) throws StudentNotException;

	public Student getStudentById(Student student);

	public Student updateStudentById(Student student) throws MySQLIntegrityConstraintViolationException;

	public List<Student> getStudents();

}
