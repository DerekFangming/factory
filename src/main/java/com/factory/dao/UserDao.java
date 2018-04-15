package com.factory.dao;

import java.util.Arrays;
import java.util.List;

import com.factory.dao.impl.QueryTerm;
import com.factory.dao.impl.RelationalOpType;
import com.factory.domain.User;
import com.factory.utils.Pair;

public interface UserDao extends CommonDao<User>{
	enum Field implements DaoFieldEnum{
		ID(true),
		USERNAME,
		PASSWORD,
		ACCESS_TOKEN,
		VERIFICATION_CODE,
		CONFIRMED,
		SALT,
		CREATED_AT,
		UPDATED_BY,
		ROLE_ID,
		MANAGER_ID,
		COMPANY_ID,
		REGISTRATION_CODE,
		VERIFICATION_NEEDED,
		ACTIVATED,
		NAME,
		PHONE,
		WORK_ID,
		BIRTHDAY,
		JOINED_DATE;

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
		new Pair<Enum<?>, String>(Field.USERNAME, "TEXT NOT NULL"),
		new Pair<Enum<?>, String>(Field.PASSWORD, "TEXT NOT NULL"),
		new Pair<Enum<?>, String>(Field.ACCESS_TOKEN, "TEXT"),
		new Pair<Enum<?>, String>(Field.VERIFICATION_CODE, "TEXT"),
		new Pair<Enum<?>, String>(Field.CONFIRMED, "BOOLEAN NOT NULL DEFAULT FALSE"),
		new Pair<Enum<?>, String>(Field.SALT, "TEXT NOT NULL"),
		new Pair<Enum<?>, String>(Field.CREATED_AT, "TIMESTAMP WITHOUT TIME ZONE NOT NULL"),
		new Pair<Enum<?>, String>(Field.UPDATED_BY, "INTEGER"),
		new Pair<Enum<?>, String>(Field.ROLE_ID, "INTEGER NOT NULL"),
		new Pair<Enum<?>, String>(Field.MANAGER_ID, "INTEGER"),
		new Pair<Enum<?>, String>(Field.COMPANY_ID, "INTEGER NOT NULL"),
		new Pair<Enum<?>, String>(Field.REGISTRATION_CODE, "INTEGER"),
		new Pair<Enum<?>, String>(Field.VERIFICATION_NEEDED, "BOOLEAN"),
		new Pair<Enum<?>, String>(Field.ACTIVATED, "BOOLEAN"),
		new Pair<Enum<?>, String>(Field.NAME, "TEXT NOT NULL"),
		new Pair<Enum<?>, String>(Field.PHONE, "TEXT"),
		new Pair<Enum<?>, String>(Field.WORK_ID, "TEXT"),
		new Pair<Enum<?>, String>(Field.BIRTHDAY, "TIMESTAMP WITHOUT TIME ZONE"),
		new Pair<Enum<?>, String>(Field.JOINED_DATE, "TIMESTAMP WITHOUT TIME ZONE"));

}
