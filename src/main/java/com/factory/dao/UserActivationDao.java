package com.factory.dao;

import java.util.Arrays;
import java.util.List;

import com.factory.dao.impl.QueryTerm;
import com.factory.dao.impl.RelationalOpType;
import com.factory.domain.UserActivation;
import com.factory.utils.Pair;

public interface UserActivationDao extends CommonDao<UserActivation>{
	enum Field implements DaoFieldEnum{
		ID(true),
		REQUESTER_ID,
		RESPONDER_ID,
		CREATED_AT;

		public boolean isPK = false;
		public String name;

		Field(boolean isPK) {
			this.isPK = isPK;
			this.name = this.name().toLowerCase();
		}

		Field() {
			this(false);
		}

		@Override
		public QueryTerm getQueryTerm(Object value) {
			return new QueryTerm(this.name, value);
		}

		@Override
		public QueryTerm getQueryTerm(RelationalOpType op, Object value) {
			return new QueryTerm(this.name, op, value);
		}
	}

	List<Pair<Enum<?>, String>> FieldTypes = Arrays.asList(
		new Pair<Enum<?>, String>(Field.ID, "SERIAL NOT NULL"),
		new Pair<Enum<?>, String>(Field.REQUESTER_ID, "INTEGER NOT NULL"),
		new Pair<Enum<?>, String>(Field.RESPONDER_ID, "INTEGER NOT NULL"),
		new Pair<Enum<?>, String>(Field.CREATED_AT, "TIMESTAMP WITHOUT TIME ZONE NOT NULL"));

}
