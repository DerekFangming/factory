package com.factory.dao.impl;

import java.util.ArrayList;
import java.util.List;

public class QueryExpression {
	private LogicalOpType beforeOp;
	private List<QueryTerm> terms;
	private LogicalOpType joinOp;

	// Master
	public QueryExpression(LogicalOpType beforeOp, List<QueryTerm> terms, LogicalOpType joinOp) {
		this.beforeOp = beforeOp;
		this.terms = terms;
		this.joinOp = joinOp;
	}

	// For use on the first expression
	public QueryExpression(List<QueryTerm> terms, LogicalOpType joinOp) {
		this(null, terms, joinOp);
	}

	// Solo term as the first term
	public QueryExpression(QueryTerm term) {
		this(null, toTermsList(term), null);
	}
 
	// Add a single term
	public QueryExpression(LogicalOpType beforeOp, QueryTerm term) {
		this(beforeOp, toTermsList(term), null);
	}
	
	/**x
	 * @return the beforeOp
	 */
	protected LogicalOpType getBeforeOp() {
		return beforeOp;
	}

	/**
	 * @return the terms
	 */
	protected List<QueryTerm> getTerms() {
		return terms;
	}

	/**
	 * @return the joinOp
	 */
	protected LogicalOpType getJoinOp() {
		return joinOp;
	}

	private static List<QueryTerm> toTermsList(QueryTerm term){
		List<QueryTerm> list = new ArrayList<QueryTerm>(1);
		list.add(term);
		
		return list;
	}
}

