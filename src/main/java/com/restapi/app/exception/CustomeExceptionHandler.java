package com.restapi.app.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@ControllerAdvice
public class CustomeExceptionHandler {
	@ExceptionHandler(StudentNotException.class)
	public ResponseEntity<ErrorCode> handleUserNotFoundException(Exception ex) {
		ErrorCode errorResponse = new ErrorCode();
		errorResponse.setErrorCode(HttpStatus.PRECONDITION_FAILED.value());
		errorResponse.setExceptionMsg(ex.getMessage());
		return new ResponseEntity<ErrorCode>(errorResponse, HttpStatus.OK);
	}

	@ExceptionHandler(com.restapi.app.exception.MySQLIntegrityConstraintViolationException.class)
	public ResponseEntity<ErrorCode> handleMysqlException(Exception ex) {
		ErrorCode errorResponse = new ErrorCode();
		errorResponse.setErrorCode(HttpStatus.NOT_ACCEPTABLE.value());
		errorResponse.setExceptionMsg(ex.getMessage());
		return new ResponseEntity<ErrorCode>(errorResponse, HttpStatus.OK);
	}

}
