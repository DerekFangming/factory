package com.factory.dao;

import java.util.List;
import java.util.Map;

public interface SdkDataSource{ 
	/**
	 * Returns the database's actual name (as seen, for ex, in PgAdmin).
	 */
	String getDbName();
	
	String getServerName();
	
	/**
	 * @return the data source name
	 */
	String getNickname();

	/**
	 * @param dst The type of this data source 
	 */
//	void setDataSourceType(DataSourceType dst);

	void addTable(SchemaTable table);
	
	SchemaTable getTable(String tableName);
	
	List<String> getTableNames();
	
	List<SchemaTable> getSchemaTables();
	
	Map<String,SchemaTable> getTables();

	String toString();

	String toString(boolean verbose);
}
