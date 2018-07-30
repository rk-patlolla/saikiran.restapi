package com.restapi.app.exception;

public class MySQLIntegrityConstraintViolationException extends Exception{
	private static final long serialVersionUID = 7718828512143293558L;

	private String exceptionMsg;

	public MySQLIntegrityConstraintViolationException(String exceptionMsg) {
		super(exceptionMsg);
		this.exceptionMsg = exceptionMsg;

	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

}
