package com.factory.exceptions;

public enum ErrorType {
	//General
	UNKNOWN_ERROR(100, "Unknown error."),
	INVALID_PARAMS(101, "The input paramters are not correct.");
	
	private final String message;
	
	ErrorType(int code, String message) {
        this.message = message;
    }
	
	public String getMessage(){
		return message;
	}

}

