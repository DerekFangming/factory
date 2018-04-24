package com.factory.dao.impl;

import com.factory.dao.SchemaTable;
import com.factory.utils.Pair;

public enum QueryType{
	//	CREATE TABLE principal_types (id SERIAL NOT NULL, CONSTRAINT principal_types_pkey PRIMARY KEY (id), type TEXT)
	CREATE_TABLE() {
		@Override
		protected StringBuilder finalizeQueryString(QueryBuilder builder){
			SchemaTable table = builder.getSchemaTable();
			String tableName = table.getTableName();

			StringBuilder querySB = new StringBuilder("CREATE TABLE ")
				.append(tableName)
				.append(" (");

			// To avoid adding a trailing delim after the last column definition - when we don't want it -
			// We'll add the delim _before_ the current item - and start with an empty delim string. :-)
			String delim = "";

			String primaryKeyName = table.getPrimaryKeyName();

			for (Pair<Enum<?>, String> item : table.getColumnDefns()){
				Enum<?> first = item.getFirst();

				String colName = SchemaTable.getName(first);

				querySB.append(delim)
					.append(colName)
					.append(" ")
					.append(item.getSecond());

				delim = ", ";

				if (primaryKeyName != null){
					querySB.append(delim)
						.append("CONSTRAINT ")
						.append(tableName)
						.append("_pkey PRIMARY KEY (")
						.append(primaryKeyName)
						.append(")");

					primaryKeyName = null;
				}

			}

			querySB.append(")");

			return querySB;
		}
	},
	
	// UPDATE access_tokens SET x_user_id = :x_user_id, partner_id = :partner_id WHERE id = :id
	UPDATE_BY_ID() {
		@Override
		protected StringBuilder finalizeQueryString(QueryBuilder builder){
			SchemaTable table = builder.getSchemaTable();
			String tableName = table.getTableName();

			// NOTE & TODO:
			// Currently expecting the WHERE expression to be "id = value"
			// That is because you cannot set the id to some other value and thus the symbolic :id
			// appears in the Params map at most once.
			//
			// If and when we want to allow a more robust expression - for example find all inactive rows
			// meeting some additional criteria - and then set all those rows to be active - then we will
			// need some clever processing so that the Params map will contain :is_active and :is_active_2
			StringBuilder querySB = new StringBuilder("UPDATE ")
				.append(tableName)
				.append(" SET ")
				.append(builder.getFieldNameSymbolicNamePairsAsCSV())
				.append(" WHERE ")
				.append(builder.getSymbolicExpression());

			return querySB;
		}
	},
	
	// INSERT INTO access_tokens (x_user_id, partner_id, expiration, token) VALUES(:x_user_id, :partner_id, :expiration, :token)
	INSERT() {
		@Override
		protected StringBuilder finalizeQueryString(QueryBuilder builder){
			SchemaTable table = builder.getSchemaTable();
			String tableName = table.getTableName();

			StringBuilder querySB = new StringBuilder("INSERT INTO ")
				.append(tableName)
				.append(" ")
				.append(builder.getFieldNamesAsCSV())
				.append(" VALUES ")
				.append(builder.getSymbolicNamesAsCSV());

			
			return querySB;
		}		
	},
	
//	FIND_BY_ID() {
//		@Override
//		protected StringBuilder getQuery(SchemaTable table, ActiveTerm includeActiveTerm){
//			String pkid = table.getPrimaryKeyName();
//			
//			StringBuilder query = new StringBuilder("SELECT * FROM ")
//				.append(table.getTableName())
//				.append(" WHERE ") 
//				.append(pkid)
//				.append(RelationalOpType.EQ.asString)
//				.append(pkid);
//			
//			return query;
//		}
//
//		@Override
//		protected StringBuilder finalizeQueryString(QueryBuilder builder){
//			StringBuilder query =	new StringBuilder(builder.getQueryRoot())
//				.append(builder.getSynmbolicTermsSB());
//			
//			return query;
//		}
//		
//	},
	
	FIND() {
		@Override
		protected StringBuilder finalizeQueryString(QueryBuilder builder){
			SchemaTable table = builder.getSchemaTable();
					 
			StringBuilder querySB = new StringBuilder("SELECT ")
				.append(builder.getReturnField())
				.append(" FROM ")
				.append(table.getTableName());
			
			querySB = super.commonFindAndCountFinalizeQueryString(querySB, builder);
			
			return querySB;
		}
	},

	FIND_ID() {
		@Override
		protected StringBuilder finalizeQueryString(QueryBuilder builder){
			SchemaTable table = builder.getSchemaTable();

			StringBuilder querySB = new StringBuilder("SELECT ")
				.append(table.getPrimaryKeyName())
				.append(" FROM ")
				.append(table.getTableName());
			
			querySB = super.commonFindAndCountFinalizeQueryString(querySB, builder);
			
			return querySB;
		}		
	},
	
	COUNT_ALL() {
		@Override
		protected StringBuilder finalizeQueryString(QueryBuilder builder){
			SchemaTable table = builder.getSchemaTable();
			String tableName = table.getTableName();

			StringBuilder querySB = new StringBuilder("SELECT COUNT(*) FROM ")
				.append(tableName);

			querySB = super.commonFindAndCountFinalizeQueryString(querySB, builder);
			
			return querySB;
		}		
	},
	
	DELETE() {
		@Override
		protected StringBuilder finalizeQueryString(QueryBuilder builder){
			SchemaTable table = builder.getSchemaTable();
			String tableName = table.getTableName();

			StringBuilder querySB = new StringBuilder("DELETE FROM ")
				.append(tableName);

			querySB = super.commonFindAndCountFinalizeQueryString(querySB, builder);
			
			return querySB;
		}		
	};
	
	protected abstract StringBuilder finalizeQueryString(QueryBuilder builder);
	
	QueryType(){
	}
		
	public static QueryBuilder getQueryBuilder(SchemaTable table, QueryType qType){
		QueryBuilder qb = new QueryBuilder(table, qType);
		
		return qb;
	}
	
	private StringBuilder commonFindAndCountFinalizeQueryString(StringBuilder querySB, QueryBuilder qb){
		StringBuilder symbolicExpression = qb.getSymbolicExpression();
		
		if(symbolicExpression.length() > 0){
			querySB.append(" WHERE " )
				.append(symbolicExpression);			
		}
		
		return querySB;
	}

}

