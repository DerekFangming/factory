package com.factory.dao.impl;

import java.util.List;
import java.util.HashMap;

// key = field name / value = next symbolic field name
public class SymbolicFieldNames extends HashMap<String,Integer> {
	private static final long serialVersionUID = 1L;

	public SymbolicFieldNames(){

	}

	public List<NVPair> processNVPairs(List<NVPair> list){
		for(NVPair item : list){
			item = this.process(item);
		}
		
		return list;
	}
	
	public NVPair process(NVPair nvp){
		String symbolicName = this.add(nvp.getField());
		
		nvp.setSymbolicName(symbolicName);
		
		return nvp;
	}
	
	public List<QueryTerm> processQueryTerms(List<QueryTerm> list){
		for(QueryTerm item : list){
			item = this.process(item);
		}
		
		return list;
	}

	public QueryTerm process(QueryTerm qt){
		String symbolicName;

		symbolicName = this.add(qt.getField());

		qt.setSymbolicName(symbolicName);

		return qt;
	}
	
	/**
	 * Adds a field to the 
	 * @param field
	 * @return
	 */
	private synchronized String add(String field){
		String symbolicName;
		int count = 0;
		
		// If we have previously seen this field name...
		if(this.containsKey(field)){
			// Get the previously seen count and increment it.
			count = super.get(field) + 1;
		}
	 
		// Save the field name and count
		// (adding it for the first time - or replacing a previous version)
		super.put(field, count);

		symbolicName = field + "_" + count;

		return symbolicName;
	}
}

