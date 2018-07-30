package com.restapi.app.exception;

public class StudentNotException extends Exception {

	private static final long serialVersionUID = 7718828512143293558L;

	private String exceptionMsg;
	
	public StudentNotException(String exceptionMsg) {
		super(exceptionMsg);
		this.exceptionMsg=exceptionMsg;
		
	}
	
	public String getExceptionMsg() {
		return exceptionMsg;
	}
	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

}
