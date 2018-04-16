package com.factory.dao.impl;

import java.util.ArrayList;
import java.util.List;

import com.factory.dao.CompanyDao;
import com.factory.dao.RoleDao;
import com.factory.dao.ImageDao;
import com.factory.dao.UserDao;
import com.factory.dao.UserActivationDao;
import com.factory.dao.ProductDao;
import com.factory.dao.ProductCombinationDao;

import com.factory.dao.DataSourceRegistry;
import com.factory.dao.SchemaTable;
import com.factory.dao.SdkDataSource;
import com.factory.dao.SdkDataSourceType;
import com.factory.utils.Pair;


public enum CoreTableType implements SchemaTable
{
	COMPANIES(SdkDataSourceType.CORE, CompanyDao.FieldTypes),
	ROLES(SdkDataSourceType.CORE, RoleDao.FieldTypes),
	IMAGES(SdkDataSourceType.CORE, ImageDao.FieldTypes),
	USERS(SdkDataSourceType.CORE, UserDao.FieldTypes),
	USER_ACTIVATIONS(SdkDataSourceType.CORE, UserActivationDao.FieldTypes),
	PRODUCTS(SdkDataSourceType.CORE, ProductDao.FieldTypes),
	PRODUCT_COMBINATIONS(SdkDataSourceType.CORE, ProductCombinationDao.FieldTypes) 
	;


	private String dsNickname;
	  private SdkDataSource dataSource; 
	  private String tableName;
	  private List<Pair<Enum<?>, String>> columnDefns;
	  private List<String> columnNames = new ArrayList<String>();
	  private boolean isExactFieldCountRequired = true;
	  
	  private String pkName;
	  private Enum<?>[] types;

	  CoreTableType(SdkDataSourceType dsType, List<Pair<Enum<?>, String>> columnDefns)
	  {
	    this.dsNickname = dsType.getNickname();
	    this.columnDefns = columnDefns;
	    this.types = new Enum<?>[0];
	    
	    this.tableName = this.name().toLowerCase();
	    this.columnNames = SchemaTable.getColumnNames(this.columnDefns);
	    this.pkName = SchemaTable.getPkName(this.columnDefns);
	  }

	  CoreTableType(SdkDataSourceType dsType, List<Pair<Enum<?>, String>> columnDefns, boolean isExactFieldCountRequired)
	  {
	   this(dsType, columnDefns);
	   this.isExactFieldCountRequired = isExactFieldCountRequired;
	  }
	  
	  CoreTableType(SdkDataSourceType dsType, List<Pair<Enum<?>, String>> columnDefns, Enum<?>... types)
	  {
	    this(dsType, columnDefns);
	    this.types = types;
	  }

	  @Override
	  public void init(DataSourceRegistry dsr)
	  {
	    this.dataSource = dsr.getDataSource(this.dsNickname);

	    // Keep this at the end so that "this" is fully populated before adding it.
	    // In particular addTable() requires that this.tableName have been set
	    this.dataSource.addTable(this);
	  }

	  /**
	   * @return the columnDefns
	   */
	  @Override
	  public List<Pair<Enum<?>, String>> getColumnDefns( )
	  {
	    return columnDefns;
	  }

	  @Override
	  public Enum<?>[] getTypes( )
	  {
	    return this.types;
	  }

	  /**
	   * @return the data source type
	   */
	  @Override
	  public String getDataSourceType( )
	  {
	    return dsNickname;
	  }

	  @Override
	  public SdkDataSource getDataSource( )
	  {
	    return this.dataSource;
	  }
	  
	  @Override
	  public String getTableName( )
	  {
	    return this.tableName;
	  }

	  @Override
	  public String getPrimaryKeyName( )
	  {
	    return this.pkName;
	  }

	  /**
	   * Returns a list of the table's field (aka column) names. This include the primary key name.
	   */
	  @Override
	  public List<String> getColumnNames( )
	  {
	    return this.columnNames;
	  }

	  @Override
	  public boolean isExactFieldCountRequired( )
	  {    
	    return this.isExactFieldCountRequired;
	  }
	  
	//  @Component
	//  public static class DSInjector
	//  { 
//	    private static String CLASS_NAME = DSInjector.class.getSimpleName();
	//    
//	    @Autowired DataSourceRegistry dsr;
	//    
//	    DSInjector()
//	    {
//	      System.out.println(CLASS_NAME + " constructor");
//	    }
	//    
//	    @PostConstruct
//	    void init()
//	    {
//	      for(CoreTableType tt : EnumSet.allOf(CoreTableType.class))
//	      {
//	        tt.init(dsr);
//	      }
//	      System.out.println("database count: " + dsr.getDatabaseCount());
//	    }    
	//  }  

	}
