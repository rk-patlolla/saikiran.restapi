package com.restapi.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.restapi.app.exception.MySQLIntegrityConstraintViolationException;
import com.restapi.app.exception.StudentNotException;
import com.restapi.app.pojo.AuthToken;
import com.restapi.app.pojo.RequestStatus;
import com.restapi.app.pojo.Student;
import com.restapi.app.service.StudentService;
import com.restapi.app.utils.JwtTokenUtil;

@RestController
@RequestMapping("/student")
public class StudentController {
	@Autowired
	StudentService studentService;
	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@RequestMapping(value = "/generate-token", method = RequestMethod.POST)
	public ResponseEntity<?> register(@RequestBody Student loginUser) throws AuthenticationException {
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUser.getMobileNo(), loginUser.getStudentpassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		final Student user = studentService.findUserByMobileNo(loginUser.getMobileNo());
		final String token = jwtTokenUtil.generateToken(user);
		return ResponseEntity.ok(new AuthToken(token));
	}

	@RequestMapping(value = "/registerStudent", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> registerStudent(@RequestBody Student newstudent) throws StudentNotException {
		Student student = studentService.registerStudent(newstudent);
		if (student != null) {
			return new ResponseEntity<>(new RequestStatus("Record SucessFully Added"), HttpStatus.ACCEPTED);
		}
		return new ResponseEntity<>(new RequestStatus("Please Try Again"), HttpStatus.BAD_REQUEST);
	}

	@PutMapping(value = "updateStudent/{s_id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateStudentById(@PathVariable long s_id, @RequestBody Student newstudent)
			throws StudentNotException, MySQLIntegrityConstraintViolationException {
		newstudent.setsId(s_id);
		if (studentService.checkStudentAvailability(newstudent)) {
			if (studentService.updateStudentById(newstudent) != null) {
				return new ResponseEntity<>(new RequestStatus("Record Updated Sucessfully"), HttpStatus.ACCEPTED);
			}
		}
		return new ResponseEntity<>(new RequestStatus("Please Try Again"), HttpStatus.BAD_REQUEST);

	}

}
