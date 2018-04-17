package com.factory.exceptions;

@SuppressWarnings("serial")
public class NotFoundException extends RuntimeException {
	
	private ErrorType errorType = ErrorType.UNKNOWN_ERROR;
	
	public NotFoundException(){
		super(ErrorType.UNKNOWN_ERROR.getMessage());
	}

	public NotFoundException(ErrorType errorType){
		super(errorType.getMessage());
		this.errorType = errorType;
	}

	public NotFoundException(String message){
		super(message);
	}
	
	public NotFoundException(Throwable cause) {
		super(cause);
	}
	
	public ErrorType getErrorType(){
		return this.errorType;
	}
	
}
