package com.factory.utils;

public enum ErrorMessage {
	//General
	UNKNOWN_ERROR("Unknown error.");
	
	private final String msg;
	
	ErrorMessage(String msg) {
        this.msg = msg;
    }
	
	public String getMsg(){
		return msg;
	}

}

