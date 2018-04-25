package com.factory.dao.impl;

import com.factory.dao.SchemaTable;

public class TableJoinExpression {
	
	private SchemaTable sourceTable;
	private String sourceColumn;
	private TableJoinType tableJoinType;
	private SchemaTable targetTable;
	private String targetColumn;
	
	public TableJoinExpression(SchemaTable sourceTable, String sourceColumn, TableJoinType tableJoinType, SchemaTable targetTable, String targetColumn) {
		this.sourceTable = sourceTable;
		this.sourceColumn = sourceColumn;
		this.tableJoinType = tableJoinType;
		this.targetTable = targetTable;
		this.targetColumn = targetColumn;
	}
	
	public StringBuilder getTableJoinString() {
		StringBuilder querySB = new StringBuilder(tableJoinType.asString)
				.append(targetTable.getTableName())
				.append(" ON ")
				.append(sourceTable.getTableName())
				.append("." + sourceColumn + " = ")
				.append(targetTable)
				.append("." + targetColumn);
		
		return querySB;
	}

}
