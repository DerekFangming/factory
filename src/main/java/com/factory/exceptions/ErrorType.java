package com.factory.exceptions;

public enum ErrorType {
	//General
	UNKNOWN_ERROR(100, "Unknown error. Please contact support."),
	INVALID_PARAMS(101, "The input paramters are not correct. Please contact support."),
	INTERNAL_ERROR(102, "Internal error. Please contact support.")
	;
	
	private int code;
	private final String message;
	
	ErrorType(int code, String message) {
		this.code = code;
        this.message = message;
    }
	
	public int getCode() {
		return code;
	}
	
	public String getMessage() {
		return message;
	}

}

