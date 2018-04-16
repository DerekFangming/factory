package com.factory.dao;

import java.util.Arrays;
import java.util.List;

import com.factory.dao.impl.QueryTerm;
import com.factory.dao.impl.RelationalOpType;
import com.factory.domain.ProductCombination;
import com.factory.utils.Pair;

public interface ProductCombinationDao extends CommonDao<ProductCombination>{
	enum Field implements DaoFieldEnum{
		ID(true),
		PARENT_ID,
		CHILD_ID,
		COUNT,
		STEP,
		CREATED_AT,
		OWNER_ID,
		UPDATED_BY;

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
		new Pair<Enum<?>, String>(Field.PARENT_ID, "INTEGER NOT NULL"),
		new Pair<Enum<?>, String>(Field.CHILD_ID, "INTEGER NOT NULL"),
		new Pair<Enum<?>, String>(Field.COUNT, "INTEGER NOT NULL"),
		new Pair<Enum<?>, String>(Field.STEP, "INTEGER NOT NULL"),
		new Pair<Enum<?>, String>(Field.CREATED_AT, "TIMESTAMP WITHOUT TIME ZONE NOT NULL"),
		new Pair<Enum<?>, String>(Field.OWNER_ID, "INTEGER NOT NULL"),
		new Pair<Enum<?>, String>(Field.UPDATED_BY, "INTEGER"));

}
