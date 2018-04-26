package com.factory.dao;

import java.util.Arrays;
import java.util.List;

import com.factory.dao.impl.QueryTerm;
import com.factory.dao.impl.RelationalOpType;
import com.factory.domain.UserDetail;
import com.factory.utils.Pair;

public interface UserDetailDao extends CommonDao<UserDetail>{
	enum Field implements DaoFieldEnum{
		ID("USERS"),
		PASSWORD,
		ACCESS_TOKEN,
		REMEMBER,
		CONFIRMED,
		SALT,
		ACTIVATED,
		NAME("USERS"),
		PHONE,
		WORK_ID,
		AVATAR_ID,
		BIRTHDAY,
		JOINED_DATE,
		
		COMPANY_NAME("COMPANIES", "NAME"),
		INDUSTRY,
		
		ROLE_NAME("ROLES", "NAME"),
		CAN_CREATE_TASK,
		CAN_CREATE_PRODUCT;

		public boolean isPK = false;
		public String expression;
		public String name;

		Field(String tableName, String columnName) {
			this.name = this.name().toLowerCase();
			this.expression = (tableName + "." + columnName + " as " + this.name()).toLowerCase();
		}

		Field(String tableName) {
			this.name = this.name().toLowerCase();
			this.expression = (tableName + "." + this.name()).toLowerCase();
		}
		
		Field() {
			this.name = this.name().toLowerCase();
			this.expression = this.name;
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
		new Pair<Enum<?>, String>(Field.PASSWORD, "TEXT NOT NULL"),
		new Pair<Enum<?>, String>(Field.ACCESS_TOKEN, "TEXT"),
		new Pair<Enum<?>, String>(Field.REMEMBER, "BOOLEAN NOT NULL"),
		new Pair<Enum<?>, String>(Field.CONFIRMED, "BOOLEAN NOT NULL DEFAULT FALSE"),
		new Pair<Enum<?>, String>(Field.SALT, "TEXT NOT NULL"),
		new Pair<Enum<?>, String>(Field.ACTIVATED, "BOOLEAN"),
		new Pair<Enum<?>, String>(Field.NAME, "TEXT NOT NULL"),
		new Pair<Enum<?>, String>(Field.PHONE, "TEXT"),
		new Pair<Enum<?>, String>(Field.WORK_ID, "TEXT"),
		new Pair<Enum<?>, String>(Field.AVATAR_ID, "INTEGER"),
		new Pair<Enum<?>, String>(Field.BIRTHDAY, "TIMESTAMP WITHOUT TIME ZONE"),
		new Pair<Enum<?>, String>(Field.JOINED_DATE, "TIMESTAMP WITHOUT TIME ZONE"),
		new Pair<Enum<?>, String>(Field.COMPANY_NAME, "TEXT NOT NULL"),
		new Pair<Enum<?>, String>(Field.INDUSTRY, "TEXT"),
		new Pair<Enum<?>, String>(Field.ROLE_NAME, "TEXT NOT NULL"),
		new Pair<Enum<?>, String>(Field.CAN_CREATE_TASK, "BOOLEAN NOT NULL"),
		new Pair<Enum<?>, String>(Field.CAN_CREATE_PRODUCT, "BOOLEAN NOT NULL"));

}
