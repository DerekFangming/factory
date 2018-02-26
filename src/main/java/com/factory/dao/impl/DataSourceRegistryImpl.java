package com.factory.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.factory.dao.DataSourceRegistry;
import com.factory.dao.SchemaTable;
import com.factory.dao.SdkDataSource;

public class DataSourceRegistryImpl implements DataSourceRegistry{
	public final static String classtype =	DataSourceRegistryImpl.class.getSimpleName();
	
	private Map<String, SdkDataSource> databases;
	
	public DataSourceRegistryImpl(){
		databases = new HashMap<String, SdkDataSource>();
	}
	
	public int getDatabaseCount( ){
		return databases.size();
	}
	
	public SdkDataSource putDataSource(SdkDataSource ds){
		String nickname = ds.getNickname();
		
		return databases.put(nickname, ds);
	}

	public SdkDataSource getDataSource(String nickname){
		return databases.get(nickname);
	}

	public String getDbName(SdkDataSourceImpl ds){
		String url = ds.getUrl();

		String dbName = url.substring(url.lastIndexOf('/') + 1);

		return dbName;
	}

	// Invoked during startup via the spring config xml.
	// See the MethodInvokingFactoryBean bean in the SDK's XML and
	// in all apps / services that define their own db tables
	public <TT extends Enum<TT> & SchemaTable> 
	void dataSourcesAndTablesInitialize(List<Class<TT>> ttList, List<SdkDataSource> datasources){
		for (SdkDataSource ds : datasources){
			this.putDataSource(ds);
		}

		for (Class<TT> tt : ttList){
			for (TT type : EnumSet.allOf(tt)){
				type.init(this);
			}
		}
	}
	
	public <TT extends Enum<TT> & SchemaTable> 
	void dataSourcesAndTablesInitialize(List<Class<TT>> ttList){
		dataSourcesAndTablesInitialize(ttList, new ArrayList<SdkDataSource>());
	}
	
	public <TT extends Enum<TT> & SchemaTable> 
	void dataSourcesAndTablesInitialize(Class<TT> tt){
		dataSourcesAndTablesInitialize(Arrays.asList(tt));
	}
	
	public <TT extends Enum<TT> & SchemaTable> 
	void dataSourcesAndTablesInitialize(Class<TT> tt, List<SdkDataSource> datasources){
		dataSourcesAndTablesInitialize(Arrays.asList(tt), datasources);
	}
	
	public <TT extends Enum<TT> & SchemaTable> 
	void dataSourcesAndTablesInitialize(Class<TT> tt, SdkDataSource datasource){
		dataSourcesAndTablesInitialize(Arrays.asList(tt), Arrays.asList(datasource));
	}
	
	public <TT extends Enum<TT> & SchemaTable> 
	void dataSourcesAndTablesInitialize(List<Class<TT>> ttList, SdkDataSource datasource){
		dataSourcesAndTablesInitialize(ttList, Arrays.asList(datasource));
	}

	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		for(SdkDataSource ds : databases.values()){
			sb.append(ds.toString(false));
		}
		
		return sb.toString();
	}
}
