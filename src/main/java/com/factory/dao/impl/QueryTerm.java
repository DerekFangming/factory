package com.factory.dao.impl;

import com.factory.utils.Pair;

public class QueryTerm extends NVPair{
	private RelationalOpType op;
	
	public QueryTerm(String field, RelationalOpType op, Object value){
		super(field, value);
		this.op = op;
	}

	public QueryTerm(String field, Object value){
		this(field, RelationalOpType.EQ, value);		
	}
	
	public QueryTerm(String special){
		this(special, RelationalOpType.SPE, "");
	}

	// For backwards compatibility...
	public QueryTerm(Pair<String, Object> term){
		this(term.getFirst(), RelationalOpType.EQ, term.getSecond());
	}
	
	/**
	 * @return the op
	 */
	public RelationalOpType getOp( ){
		return op;
	}
}

