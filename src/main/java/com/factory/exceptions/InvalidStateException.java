package com.factory.exceptions;

@SuppressWarnings("serial")
public class InvalidStateException extends RuntimeException {
	private ErrorType errorType = ErrorType.UNKNOWN_ERROR;
	boolean writeToLog = true; 
	
	public InvalidStateException(){
		super(ErrorType.UNKNOWN_ERROR.getMessage());
	}

	public InvalidStateException(ErrorType errorType){
		super(errorType.getMessage());
		this.errorType = errorType;
	}
	
	public InvalidStateException(ErrorType errorType, boolean writeToLog){
		super(errorType.getMessage());
		this.errorType = errorType;
		this.writeToLog = writeToLog;
	}
	
	public ErrorType getErrorType() {
		return this.errorType;
	}
	
	public boolean getWriteToLog() {
		return this.writeToLog;
	}

}
