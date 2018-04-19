package com.factory.exceptions;

@SuppressWarnings("serial")
public class InternalServerException extends RuntimeException {
	
	public InternalServerException(Throwable cause) {
		super(cause);
	}
	
	public ErrorType getErrorType(){
		return ErrorType.UNKNOWN_ERROR;
	}

}
