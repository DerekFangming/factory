package com.factory.exceptions;

@SuppressWarnings("serial")
public class InvalidParamException extends RuntimeException {
	
	private ErrorType errorType = ErrorType.INVALID_PARAMS;
	
	public InvalidParamException(){
		super(ErrorType.INVALID_PARAMS.getMessage());
	}

	public InvalidParamException(ErrorType errorType){
		super(errorType.getMessage());
		this.errorType = errorType;
	}
	
	public ErrorType getErrorType(){
		return this.errorType;
	}

}
