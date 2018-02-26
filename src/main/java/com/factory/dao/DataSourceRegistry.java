package com.factory.dao;

import com.factory.dao.impl.SdkDataSourceImpl;

public interface DataSourceRegistry {

	int getDatabaseCount( );

	SdkDataSource putDataSource(SdkDataSource ds);

	SdkDataSource getDataSource(String nickname);

	String getDbName(SdkDataSourceImpl ds);   
}
