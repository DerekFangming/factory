package com.factory.dao.impl;

public enum TableJoinType {

	LEFT_JOIN("LEFT JOIN"),
	JOIN("JOIN"),
	RIGHT_JOIN("RIGHT JOIN");

	protected String asString;
	
	TableJoinType(String joinType) {
		this.asString = " " + joinType + " ";			
	}		

}
