package com.factory.dao.impl;

public class ExistQueryTerm extends QueryTerm{

	public ExistQueryTerm(QueryBuilder qb) {
		super("", RelationalOpType.EXT, qb);
	}
	
	@Override
	public QueryBuilder getValue(){
		return (QueryBuilder) super.getValue();
	}

}
