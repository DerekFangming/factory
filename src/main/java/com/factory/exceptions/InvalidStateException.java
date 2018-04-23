package com.factory.exceptions;

@SuppressWarnings("serial")
public class InvalidStateException extends RuntimeException {
	private ErrorType errorType = ErrorType.UNKNOWN_ERROR;
	
	public InvalidStateException(){
		super(ErrorType.UNKNOWN_ERROR.getMessage());
	}

	public InvalidStateException(ErrorType errorType){
		super(errorType.getMessage());
		this.errorType = errorType;
	}
	
	public ErrorType getErrorType(){
		return this.errorType;
	}

}
