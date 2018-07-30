package com.restapi.app.exception;

public class TokenError {
	private String exceptionMsg;

	public TokenError(String message) {
	this.exceptionMsg=message;
	}

	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}
}
