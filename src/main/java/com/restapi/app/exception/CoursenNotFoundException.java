package com.restapi.app.exception;

public class CoursenNotFoundException {

	
	private static final long serialVersionUID = 7718828512143293558L;

	private String exceptionMsg;
	
	public CoursenNotFoundException(String exceptionMsg) {
		super();
		this.exceptionMsg=exceptionMsg;
		
	}
	
	public String getExceptionMsg() {
		return exceptionMsg;
	}
	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

}
