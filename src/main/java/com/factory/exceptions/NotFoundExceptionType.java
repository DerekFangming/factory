package com.factory.exceptions;

public enum NotFoundExceptionType{
	ACCESS_TOKEN("Access Token"),
	ASSIGNMENT("Assignment"),
	DATABASE_ID("Database ID"),
	EXTERNAL_REFERENCE("External Reference"),
	USER("User"),
	DATABASE_TABLE("Database table"),
	NOT_SPECIFIED("Type not sepcified");
	
	private String displayName;
	
	NotFoundExceptionType(String displayName){
		this.displayName = displayName;
	}
	
	// TODO: Could add + " not found."
	public String toString(){
		return this.displayName;
	}
}

