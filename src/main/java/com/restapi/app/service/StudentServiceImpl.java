package com.restapi.app.service;

import java.util.List;
import java.util.NoSuchElementException;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.restapi.app.exception.ErrorCode;
import com.restapi.app.exception.MySQLIntegrityConstraintViolationException;
import com.restapi.app.exception.StudentNotException;
import com.restapi.app.pojo.Student;
import com.restapi.app.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StudentService {
	@Autowired
	StudentRepository studentRes;
	@Autowired
	EntityManager entityManager;

	ErrorCode errorcode;

	@Override
	public String checkMobileNoForReg(String mobileNo) throws UsernameNotFoundException {
		Query query = null;
		try {
			String qureyStr = "Select s.mobileNo FROM Student s where s.mobileNo like '%" + mobileNo + "%'";
			query = entityManager.createQuery(qureyStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (String) query.getSingleResult();
	}

	@Override
	public Student findUserByMobileNo(String mobileNo) {
		return studentRes.findUserByMobileNo(mobileNo);
	}

	@Override
	public Student registerStudent(Student student) throws StudentNotException {
		try {
			studentRes.save(student);
		} catch (Exception e) {
			throw new StudentNotException("This Student Criteria Already Exist ");
		}
		return student;
	}

	@Override
	public boolean deleteStudentById(Student student) throws MySQLIntegrityConstraintViolationException {
		try {
			studentRes.deleteById(student.getsId());
		} catch (Exception e) {

			throw new MySQLIntegrityConstraintViolationException("Student Not Deleted");
		}
		return true;
	}

	@Override
	public boolean checkStudentAvailability(Student student) throws StudentNotException {
		try {
			return studentRes.findById(student.getsId()).get() != null ? true : false;
		} catch (NoSuchElementException e) {
			throw new StudentNotException("STUDENT NOT FOUND");

		}

	}

	@Override
	public Student updateStudentById(Student student) throws MySQLIntegrityConstraintViolationException {
		try {
			Student currentStudent = studentRes.findById(student.getsId()).get();
			currentStudent.setStudentName(student.getStudentName());
			currentStudent.setStudentType(student.getStudentType());
			studentRes.save(currentStudent);
		} catch (Exception e) {
			throw new MySQLIntegrityConstraintViolationException("RECORD NOT UPDATED SUCESSFULLY");
		}
		return student;
	}

	@Override
	public Student getStudentById(Student student) {

		try {
			studentRes.findById(student.getsId());
		} catch (Exception e) {
			student = null;
		}
		return student;
	}

	@Override
	public List<Student> getStudents() {
		return studentRes.findAll();
	}

}
