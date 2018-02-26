package com.factory.exceptions;

import com.factory.utils.ErrorMessage;

@SuppressWarnings("serial")
public class NotFoundException extends SdkException{
	private NotFoundExceptionType typeNotFound = NotFoundExceptionType.NOT_SPECIFIED;
	
	
	// TODO: Could modify message to prepend with typeNotFound.toString()
	public NotFoundException(){
		super(ErrorMessage.UNKNOWN_ERROR.getMsg());
	}

	public NotFoundException(NotFoundExceptionType typeNotFound){
		super();
		this.typeNotFound = typeNotFound;
	}

	public NotFoundException(String message){
		super(message);
	}

	public NotFoundException(NotFoundExceptionType typeNotFound, String message){
		super(message);
		
		this.typeNotFound = typeNotFound;
	}

	public NotFoundException(Throwable cause){
		super(cause);
	}

	public NotFoundException(NotFoundExceptionType typeNotFound, Throwable cause){
		super(cause);
		this.typeNotFound = typeNotFound;
	}

	public NotFoundException(String message, Throwable cause){
		super(message, cause);
	}

	public NotFoundException(NotFoundExceptionType typeNotFound, String message, Throwable cause){
		super(message, cause);
		this.typeNotFound = typeNotFound;
	}
	
	public NotFoundExceptionType getType(){
		return this.typeNotFound;
	}
	
	/*
	@Override
	public String getMessage()
	{
		return this.typeNotFound.toString() + ": " + super.getMessage();
	}
	*/
}

