package com.factory.dao.impl;

public enum LogicalOpType
{
	AND,
	OR;

	protected String asString;
	
	LogicalOpType()
	{
		this.asString = " " + this.name() + " ";			
	}		
}
