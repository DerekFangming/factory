package com.factory.dao;

import com.factory.dao.impl.QueryTerm;
import com.factory.dao.impl.RelationalOpType;

public interface DaoFieldEnum{
	public enum OnPersist {
		OPTIONAL, 
		REQUIRED;
	}
	
	default 
	OnPersist getOnPersist(){
		return OnPersist.REQUIRED;
	}
	
	QueryTerm getQueryTerm(Object value);
	
	QueryTerm getQueryTerm(RelationalOpType op, Object value);	
}

