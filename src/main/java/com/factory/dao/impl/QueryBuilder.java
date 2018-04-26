package com.factory.dao.impl;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import com.factory.dao.SchemaTable;
import com.factory.exceptions.InternalServerException;
import com.factory.utils.Pair;

public class QueryBuilder
{
	private static String className = QueryBuilder.class.getSimpleName();
	private static Logger logger = Logger.getLogger(className);
	
	private SchemaTable table;
	private QueryType qType;

	// May be set externally. If not we'll generate them here.
	private MapSqlParameterSource params = null;
	
	private int limit = 0;
	private int offset = 0;
	private ResultsOrderType ordering = ResultsOrderType.NONE;
	private String orderingField = null;
	
	private List<QueryExpression> expressionList = new ArrayList<QueryExpression>();
	private List<NVPair> nvpList = new ArrayList<NVPair>();
	
	private List<TableJoinExpression> tableJoinList;
	
	// Map containing:
	// key = field name 
	// value = next symbolic name for the key
	private SymbolicFieldNames seenFields;
	
	private String returnField = "*";
	
	/**
	 * Currently {@link QueryType#CREATE_TABLE} is built entirely in QueryTerm.
	 * <p>
	 * Currently {@link QueryType#INSERT} needs only NVPairs set up here.
	 *	
	 * @param table
	 * @param qType
	 */
	protected QueryBuilder(SchemaTable table, QueryType qType){
		this.table = table;
		this.qType = qType;
		this.seenFields = new SymbolicFieldNames();
		this.params = new MapSqlParameterSource();
	}

	private QueryBuilder(SchemaTable table, QueryType qType, 
		SymbolicFieldNames parentSeenFields, MapSqlParameterSource params){
		this.table = table;
		this.qType = qType;
		this.seenFields = parentSeenFields;
		this.params = params;
	}
	
	/**
	 * Returns a QueryBuilder for creating an inner (sub) query.
	 * <p>
	 * After building the inner query, you add it to the parent builder by:
	 * <ol>
	 * <li>Creating an {@link InnerQueryTerm} using the inner QueryBuilder, and</li>
	 * <li>Calling {@link #addFirstQueryExpression(QueryTerm)}, {@link #addFirstQueryExpression(List, LogicalOpType)},
	 *	 {@link #addNextQueryExpression(LogicalOpType, QueryTerm)}, or {@link #addNextQueryExpression(LogicalOpType, List, LogicalOpType)}
	 *	 to add that new <tt>InnerQueryTerm</tt>, as an expression, to the parent builder.</li>
	 * </ol>
	 * @param table Table the inner query operates on
	 * @param qType Query type
	 * @return 
	 */
	public QueryBuilder getInnerQueryBuilder(SchemaTable table, QueryType qType){
		return new QueryBuilder(table, qType, this.seenFields, this.params); 
	}
	
	protected SchemaTable getSchemaTable(){
		return this.table;
	}

	/**
	 * For an inner query, specifies the field name to return.
	 * <p>
	 * 
	 * @param fieldName
	 */
	public QueryBuilder setReturnField(String fieldName){
		this.returnField = fieldName;

		return this;
	}
	
	public QueryBuilder setReturnField(List<Pair<Enum<?>, String>> fieldTypes){
		this.returnField = "";
		
		for (Pair<Enum<?>, String> p : fieldTypes) {
			Enum<?> enumElt = p.getFirst();
		
			try{
				Field f = enumElt.getClass().getDeclaredField("expression");
				this.returnField += (String) f.get(enumElt) + ", ";
			}catch (Throwable t){
				throw new InternalServerException(t);
			}
		}

		this.returnField = StringUtils.removeEnd(this.returnField, ", ");

		return this;
	}
	
	public String getReturnField(){
		return this.returnField;
	}
	
	/**
	 * Adds the query term to your query expression.
	 * @param term Query term
	 */
	public QueryBuilder addQueryExpression(QueryTerm term) {
		return addQueryExpression(LogicalOpType.AND, term);
	}
	
	/**
	 * Adds a single query term to the query expression
	 * @param beforeOp Logical operator to join this term to the expression.
	 * @param term Query term
	 */
	public QueryBuilder addQueryExpression(LogicalOpType beforeOp, QueryTerm term) {
		term = this.seenFields.process(term);
		
		QueryExpression qe;
		
		if(expressionList.isEmpty()){
			qe = new QueryExpression(term);
		}else{
			qe = new QueryExpression(beforeOp, term);
		}
		
		expressionList.add(qe);
		
		return this;
	}

	/**
	 * Adds a group of query terms as start of your query expression. 
	 * @param terms List of query terms
	 * @param joinOp	Logical operator used to join each individual query term.
	 * For example: <tt>&lt;term1&gt; AND &lt;term2&gt; AND &lt;term3&gt;</tt>
	 * <br/>
	 * Or: <tt>&lt;term1&gt; OR &lt;term2&gt; OR &lt;term3&gt;</tt>
	 */
	public QueryBuilder addQueryExpression(List<QueryTerm> terms, LogicalOpType joinOp){
		return addQueryExpression(LogicalOpType.AND, terms, joinOp);
	}

	/**
	 * Adds a single query term to the query expression
	 * @param beforeOp Logical operator to join these terms to the expression.	 * 
	 * @param terms List of query terms
	 * @param joinOp	Logical operator used to join each individual query term.
	 */
	public QueryBuilder addQueryExpression(LogicalOpType beforeOp, List<QueryTerm> terms, LogicalOpType joinOp){
		terms = this.seenFields.processQueryTerms(terms);
		
		QueryExpression qe;
		
		if(expressionList.isEmpty()){
			qe = new QueryExpression(terms, joinOp);
		}else{
			qe = new QueryExpression(beforeOp, terms, joinOp);
		}
		
		expressionList.add(qe);
		
		return this;
	}
	
	public QueryBuilder addTableJoinExpression(TableJoinExpression exp) {
		if (tableJoinList == null)
			tableJoinList = new ArrayList<TableJoinExpression>();
		
		tableJoinList.add(exp);
		
		return this;
	}
	
	protected List<TableJoinExpression> getTableJoinList() {
		return tableJoinList;
	}

	// Nesting the calls so it is clear from what the StringBuilder is derived.
	protected StringBuilder getFieldNamesAsCSV(){
		return this.getFieldNamesAsCSV(this.nvpList);
	}
	
	// Ex: (x_user_id, partner_id, expiration, token)
	protected StringBuilder getFieldNamesAsCSV(List<NVPair> nvps){
		StringBuilder sb = new StringBuilder();
		
		if(nvps.isEmpty()){
			return sb;
		}
		
		sb.append("(");
		
		// To avoid adding a trailing delim after the name - when we don't want it -
		// We'll add the delim _before_ the current item - and start with an empty delim string. :-)
		String delim = "";

		for(NVPair nvp : nvps){
			sb.append(delim).append(nvp.getField());

			delim = ", ";
		}
		
		sb.append(")");
		
		return sb;
	}
	
	// Nesting the calls so it is clear from what the StringBuilder is derived.
	protected StringBuilder getSymbolicNamesAsCSV(){
		return this.getSymbolicNamesAsCSV(this.nvpList);		
	}
	
	//	Ex: ():x_user_id, :partner_id, :expiration, :token)
	protected StringBuilder getSymbolicNamesAsCSV(List<NVPair> nvps){
		StringBuilder sb = new StringBuilder();
		
		if(nvps.isEmpty()){
			return sb;
		}
		
		sb.append("(");
		
		// To avoid adding a trailing delim after the name - when we don't want it -
		// We'll add the delim _before_ the current item - and start with an empty delim string. :-)
		String delim = "";

		for(NVPair nvp : nvps){
			sb.append(delim)
			.append(":")
			.append(nvp.getSymbolicName());

			delim = ", ";
		}
		
		sb.append(")");
		
		return sb;
	}
	
	// Nesting the calls so it is clear from what the StringBuilder is derived.
	protected StringBuilder getFieldNameSymbolicNamePairsAsCSV(){
		return this.getFieldNameSymbolicNamePairsAsCSV(this.nvpList);
	}
	
	// Ex: (x_user_id = :x_user_id, partner_id = :partner_id)
	protected StringBuilder getFieldNameSymbolicNamePairsAsCSV(List<NVPair> nvps){
		StringBuilder sb = new StringBuilder();
		
		if(nvps.isEmpty()){
			return sb;
		}

		// To avoid adding a trailing delim after the pair - when we don't want it -
		// We'll add the delim _before_ the current item - and start with an empty delim string. :-)
		String delim = "";

		for(NVPair nvp : nvps){
			sb.append(delim)
			.append(nvp.getField())
			.append(" = :")
			.append(nvp.getSymbolicName());

			delim = ", ";
		}
		
		return sb;
	}

	
	// Nesting the calls so it is clear from what the StringBuilder is derived.
	protected StringBuilder getSymbolicExpression(){
		return this.getSymbolicExpression(this.expressionList);
	}
	
	// Ex: 
	protected StringBuilder getSymbolicExpression(List<QueryExpression> expressions){
		StringBuilder sb = new StringBuilder();
		
		if(expressions.isEmpty()){
			return sb;
		}
		
		sb.append("(");

		LogicalOpType beforeOp;
		List<QueryTerm> terms;
		LogicalOpType joinOp;
		
		for(QueryExpression qx : expressions){
			beforeOp = qx.getBeforeOp();
			terms = qx.getTerms();
			joinOp = qx.getJoinOp();
			
			if(beforeOp != null){
				sb.append(beforeOp.asString)
				.append("(");
			}
			
			String delim = "";
		 
			for(QueryTerm term : terms){
				sb.append(delim)
				.append(term.getOp().makeSymbolicTerm(term));

				// Not 
				if(joinOp != null){
					delim = joinOp.asString;
				}
			}
			
			if(beforeOp != null){
				sb.append(")");
			}
		}
		
		sb.append(")");
		
		return sb;
	}
	
	public void setParamsSource(MapSqlParameterSource params){
		this.params = params;
	}
	
	// Nesting the calls so it is clear from what the List is derived.
	private List<NVPair> getNVPairsFromExpressions(){
		return this.getNVPairsFromExpressions(this.expressionList);
	}
	
	private List<NVPair> getNVPairsFromExpressions(List<QueryExpression> expressions){
		List<NVPair> result = new ArrayList<NVPair>();
		
		List<QueryTerm> terms;
		
		NVPair nvp;
		
		for(QueryExpression qx : expressions){
			terms = qx.getTerms();
			
			for(QueryTerm term : terms){
				if(term.getOp() == RelationalOpType.IN || term.getOp() == RelationalOpType.SPE){
					continue;
				}
				
				nvp = new NVPair(term.getField(), term.getValue());
				
				nvp.setSymbolicName(term.getSymbolicName());
				
				result.add(nvp);
			}
		}

		return result;
	}
	
	protected MapSqlParameterSource addToParams(List<NVPair> nvps){
		for(NVPair nvp : nvps){
			params.addValue(nvp.getSymbolicName(), nvp.getValue());
		}

		return params;
	}
		
	public void addNameValuePair(NVPair nvp){
		nvp = this.seenFields.process(nvp);
		
		this.nvpList.add(nvp);
	}

	public void addNameValuePairs(List<NVPair> nvps){
		nvps = this.seenFields.processNVPairs(nvps);
		
		this.nvpList.addAll(nvps);
	}
	
	public QueryBuilder setLimit(int limit){
		this.limit = limit;
		
		return this;
	}
	
	public QueryBuilder setOffset(int offset){
		this.offset = offset;
		
		return this;
	}
	
	public QueryBuilder setOrdering(String orderingField, ResultsOrderType orderType){
		this.ordering = orderType;
		this.orderingField = orderingField;
		
		return this;
	}
		
	public QueryInstance createQuery(){		
		StringBuilder querySB = qType.finalizeQueryString(this);

		if(this.ordering != ResultsOrderType.NONE){
			querySB.append(" ORDER BY ")
			.append(this.orderingField)
			.append(this.ordering.asString);
		}
		
		if(this.limit > 0){
			querySB.append(" LIMIT ")
			.append(this.limit);
		}
		
		if(this.offset > 0){
			querySB.append(" OFFSET ")
			.append(this.offset);
		}
		
		String query = querySB.toString();
		
		logger.info(qType.toString() + ": " + query);
		
		// Need to combine symbols and values from both the name, values and expressions given to us 
		List<NVPair> combined = new ArrayList<NVPair>();
		
		combined.addAll(this.nvpList);
		combined.addAll(this.getNVPairsFromExpressions());
		
		this.addToParams(combined);
		
		logger.info("Values: " + this.params.getValues().toString());
		
		return new QueryInstance(query, this.params);
	}
	
	// Mostly for use in Eclipse debugging
	@Override
	public String toString(){
		// Pretty it up for display
		return this.qType.finalizeQueryString(this).toString();
	}
}

