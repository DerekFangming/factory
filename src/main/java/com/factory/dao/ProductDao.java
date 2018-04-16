package com.factory.dao;

import java.util.Arrays;
import java.util.List;

import com.factory.dao.impl.QueryTerm;
import com.factory.dao.impl.RelationalOpType;
import com.factory.domain.Product;
import com.factory.utils.Pair;

public interface ProductDao extends CommonDao<Product>{
	enum Field implements DaoFieldEnum{
		ID(true),
		COMPANY_ID,
		MODEL,
		NAME,
		DESCRIPTION,
		IMAGE_ID,
		COMBINED_PRODUCT,
		LABOR_COST,
		VISIBLE_ROLE_ID,
		SENSITIVE_NET_COST,
		SENSITIVE_MARKET_PRICE,
		SENSITIVE_DESCRIPTION,
		SENSITIVE_VISIBLE_ROLE_ID,
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
		new Pair<Enum<?>, String>(Field.COMPANY_ID, "INTEGER NOT NULL"),
		new Pair<Enum<?>, String>(Field.MODEL, "TEXT NOT NULL"),
		new Pair<Enum<?>, String>(Field.NAME, "TEXT NOT NULL"),
		new Pair<Enum<?>, String>(Field.DESCRIPTION, "TEXT"),
		new Pair<Enum<?>, String>(Field.IMAGE_ID, "INTEGER"),
		new Pair<Enum<?>, String>(Field.COMBINED_PRODUCT, "BOOLEAN NOT NULL DEFAULT FALSE"),
		new Pair<Enum<?>, String>(Field.LABOR_COST, "DECIMAL"),
		new Pair<Enum<?>, String>(Field.VISIBLE_ROLE_ID, "INTEGER"),
		new Pair<Enum<?>, String>(Field.SENSITIVE_NET_COST, "DECIMAL"),
		new Pair<Enum<?>, String>(Field.SENSITIVE_MARKET_PRICE, "DECIMAL"),
		new Pair<Enum<?>, String>(Field.SENSITIVE_DESCRIPTION, "TEXT"),
		new Pair<Enum<?>, String>(Field.SENSITIVE_VISIBLE_ROLE_ID, "INTEGER"),
		new Pair<Enum<?>, String>(Field.CREATED_AT, "TIMESTAMP WITHOUT TIME ZONE NOT NULL"),
		new Pair<Enum<?>, String>(Field.OWNER_ID, "INTEGER NOT NULL"),
		new Pair<Enum<?>, String>(Field.UPDATED_BY, "INTEGER"));

}
