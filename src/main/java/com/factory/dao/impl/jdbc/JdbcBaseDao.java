package com.factory.dao.impl.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.factory.dao.CommonDao;
import com.factory.dao.DataSourceRegistry;
import com.factory.dao.SchemaTable;
import com.factory.dao.impl.LogicalOpType;
import com.factory.dao.impl.NVPair;
import com.factory.dao.impl.NVPairList;
import com.factory.dao.impl.QueryBuilder;
import com.factory.dao.impl.QueryInstance;
import com.factory.dao.impl.QueryTerm;
import com.factory.dao.impl.QueryType;
import com.factory.dao.impl.RelationalOpType;
import com.factory.dao.impl.SdkDataSourceImpl;
import com.factory.domain.EnumType;
import com.factory.exceptions.NotFoundException;

//public abstract class JdbcBaseDao<T extends Object> implements CommonDao<T>
/**
 *
 * @param <T> Domain object Type
 */
public abstract class JdbcBaseDao<T extends Object> implements CommonDao<T>
{
  protected SchemaTable myTable;
  protected String myPkName;
  protected String TABLE_NAME;
  protected List<String> COLUMN_NAMES;
  protected String[] RETURN_PK_NAME;
  
  protected NamedParameterJdbcTemplate namedTemplate;
  protected JdbcTemplate jdbcTemplate;

  private boolean calledOnce = false;
  
  private int expectedValues;

  private SdkDataSourceImpl dataSource;
  
  @Autowired DataSourceRegistry dsr;
  
  // NOTE: Cannot wire in a subclass here. 
  
  protected JdbcBaseDao(SchemaTable table)
  {
    this.myTable  = table;   
  }
  
  @PostConstruct
  private void init()
  {
    this.dataSource = (SdkDataSourceImpl) this.myTable.getDataSource();
    
    this.namedTemplate = new NamedParameterJdbcTemplate(dataSource);
    this.jdbcTemplate = new JdbcTemplate(dataSource);
    
    TABLE_NAME              = this.myTable.getTableName();
    COLUMN_NAMES            = this.myTable.getColumnNames();

//    UPDATE_SQL_PREFIX       = "UPDATE " + TABLE_NAME + " ";
    
    if(this.myTable.hasPrimaryKey())
    {
      this.myPkName       = this.myTable.getPrimaryKeyName();
      RETURN_PK_NAME      = new String[]{this.myPkName};
    }
    else
    {
      this.myPkName       = null;
      RETURN_PK_NAME      = null;
    }
    
    initOnce(this.dataSource, TABLE_NAME, COLUMN_NAMES);
    
    // Number of columns expected for using INSERT_SQL
    this.expectedValues = this.myTable.getColumnCount();
    
    if(this.myTable.hasPrimaryKey())
    {
      --this.expectedValues;
    }

  }
    
  // TODO: Currently implemented to add to the Map ONLY when the member of
  // T is not NULL - we may need to reconsider this restriction for some
  // objects / tables.  Perhaps, we need to add NOT NULL to the table schema
  // and thus whatever is sent here is accepted.
  // SHOULD NOT include PK in returned params
  //
  // Do not add T's ID to the Map
  protected abstract NVPairList getNVPairs(T obj);
  
  // dm 10/29
//  private static MapSqlParameterSource getParamsMap(List<QueryTerm> values)
//  {
//    MapSqlParameterSource params = new MapSqlParameterSource();
//    
//    for(QueryTerm changeItem : values)
//    {
//      params.addValue(changeItem.getField(), changeItem.getValue());
//    }
//
//    return params;
//  }

  protected abstract RowMapper<T> getRowMapper( );

  @Override
  public int persist(T obj)
  {
    int expectedCount = this.expectedValues;
    
    if (obj instanceof EnumType)
    {
      ++expectedCount;  
    }
    
    NVPairList values = this.getNVPairs(obj);

    if(values.size() != expectedCount)
    {
      throw new IllegalArgumentException("Inserting a row into "
        + TABLE_NAME
        + " requires exactly "
        + expectedCount
        + " field values (excludes a primary key). The call to getNVPairs("
        + obj.getClass().getName()  
        + ") implementation returned "
        + values.size()
        + " field values."
        );
    }

    KeyHolder generatedKeyHolder = new GeneratedKeyHolder();

    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, QueryType.INSERT);

    qb.addNameValuePairs(values.getList());
    
    QueryInstance qi = qb.createQuery();
  

    // If something goes wrong, the caller will have to deal with it.
    namedTemplate.update(qi.getQueryStr(), qi.getParams(), generatedKeyHolder, RETURN_PK_NAME);

    int dbid = generatedKeyHolder.getKey().intValue();
      
    // Return the row's new ID
    return dbid;
  }
  
  
  @Override
  public void update(int dbId, T obj) throws NotFoundException
  {
    NVPairList values = getNVPairs(obj);

    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, QueryType.UPDATE_BY_ID);
    
    qb.addQueryExpression(new QueryTerm(this.myPkName, dbId));
    qb.addNameValuePairs(values.getList());
    
    QueryInstance qi = qb.createQuery();
    
    int roleAffected = namedTemplate.update(qi.getQueryStr(), qi.getParams());
    if (roleAffected == 0) {
    	throw new NotFoundException();
    }
    
//    Map<String,Object> map = params.getValues();
//        
//    String query = this.getUpdateByIdSQL(map);
//    
//    // Query string was built without the pk ID so we are not attempting
//    // to set the id value.  Now, we need it to satisfy the WHERE clause.
//    params.addValue(myPkName, dbId);
//    
//    namedTemplate.update(query, params);
  }
  
  @Override
  public void update(int dbId, NVPair value) throws NotFoundException
  {
    List<NVPair> values = convertToList(value);
    
    this.update(dbId, values);
  }
  
  @Override
  public void update(int dbId, List<NVPair> values) throws NotFoundException
  {
    QueryBuilder qb =  QueryType.getQueryBuilder(this.myTable, QueryType.UPDATE_BY_ID);
    
    qb.addQueryExpression(new QueryTerm(this.myPkName, dbId));
    
    qb.addNameValuePairs(values);
      
    QueryInstance qi = qb.createQuery();
    
    int roleAffected = namedTemplate.update(qi.getQueryStr(), qi.getParams());
    if (roleAffected == 0) {
    	throw new NotFoundException();
    }
    
//    MapSqlParameterSource params = getParamsMap(values);
//    String query = this.getUpdateByIdSQL(params.getValues());
//    
//    // Need to add it AFTER generating the query so we don't include this in the SET values.
//    params.addValue(this.myTable.getPrimaryKeyName(), dbId);
//    
//    namedTemplate.update(query, params);
  }
  
  @Override
  public T findById(int dbId) throws NotFoundException
  {
//    QueryBuilder qb = QueryType.getQueryBuilder(myTable, QueryType.FIND_BY_ID, ActiveType.ACTIVE);
    QueryBuilder qb = QueryType.getQueryBuilder(myTable, QueryType.FIND);
    
    qb.addQueryExpression(new QueryTerm(myTable.getPrimaryKeyName(), RelationalOpType.EQ, dbId));
    
    QueryInstance qi = qb.createQuery();

    List<T> results = this.namedTemplate.query(qi.getQueryStr(), qi.getParams(), 
      new RowMapperResultSetExtractor<T>(this.getRowMapper(), 1));

    if(results.size() == 0)
    {
      throw new NotFoundException();
    }

    return results.get(0);
  }
  
//  public final static List<QueryTerm> convertPairVarArgsToList(List<QueryTerm> values)
//  {
//    List<QueryTerm> list = new ArrayList<QueryTerm>();
//    
//    for(QueryTerm value : values)
//    {
//      list.add(new QueryTerm(value));
//    }
//    
//    return list;
//  }

  @SafeVarargs
  public final static List<NVPair> convertToList(NVPair... values)
  {
    return new ArrayList<NVPair>(Arrays.asList(values));
  }
  
  @SafeVarargs
  public final static List<QueryTerm> convertToList(QueryTerm... values)
  {
    return new ArrayList<QueryTerm>(Arrays.asList(values));

//    List<QueryTerm> list = new ArrayList<QueryTerm>();
//    
//    for(QueryTerm value : values)
//    {
//      list.add(value);
//    }
//    
//    return list;
  }

  @Override
  public int findId(QueryTerm term) throws NotFoundException
  {
    return this.findId(convertToList(term));
  }
  
  @Override
  public int findId(List<QueryTerm> terms) throws NotFoundException
  {
    int dbId = -1;
    
    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, QueryType.FIND_ID);
    
    qb.addQueryExpression(terms, LogicalOpType.AND);
    
    qb.setLimit(1);
    
    QueryInstance qi = qb.createQuery();
    
    try
    {
      SqlRowSet rows = this.namedTemplate.queryForRowSet(qi.getQueryStr(), qi.getParams());
      
      if(rows.first())
      {
        dbId = rows.getInt(myPkName);
      }
      else
      {
        throw new NotFoundException();
      }
    }
    catch(DataAccessException e)
    {
      throw new NotFoundException(e);
    }
    
    return dbId;    
  }
    

  @Override
  public List<Integer> findAllIds(List<QueryTerm> terms) throws NotFoundException
  {
    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, QueryType.FIND_ID);
    
    qb.addQueryExpression(terms, LogicalOpType.AND);
    
    QueryInstance qi = qb.createQuery();

    return findAllIds(qi);
    
//    SqlRowSet rows = this.namedTemplate.queryForRowSet(qi.getQueryStr(), qi.getParams());
//
//    List<Integer> ids = new ArrayList<Integer>();
//
//    while (rows.next())
//    {
//      ids.add(rows.getInt(myPkName));
//    }
//
//    return ids;    
  }
  
  @Override
  public List<Integer> findAllIds(QueryInstance qi) throws NotFoundException
  {
    SqlRowSet rows = this.namedTemplate.queryForRowSet(qi.getQueryStr(), qi.getParams());

    List<Integer> ids = new ArrayList<Integer>();

    while (rows.next())
    {
      ids.add(rows.getInt(myPkName));
    }

    if (ids.size() == 0)
    	throw new NotFoundException();
    
    return ids;    
  }
  
  @Override
  final public List<T> findAllObjects() throws NotFoundException
  {
    QueryBuilder qb = QueryType.getQueryBuilder(myTable, QueryType.FIND);
    
    QueryInstance qi = qb.createQuery();
    
    List<T> results = this.namedTemplate.query(qi.getQueryStr(), qi.getParams(), 
      new RowMapperResultSetExtractor<T>(this.getRowMapper(), 1));
    
    if(results.size() == 0)
    {
      throw new NotFoundException();
    }
    
    return results;
  }
  
  @Override
  public T findObject(QueryTerm value) throws NotFoundException
  {
    return this.findObject(JdbcBaseDao.convertToList(value));
  }
  
  @Override
  public T findObject(List<QueryTerm> terms) throws NotFoundException
  {
    QueryBuilder qb = QueryType.getQueryBuilder(myTable, QueryType.FIND);
    
    qb.addQueryExpression(terms, LogicalOpType.AND);
    
    qb.setLimit(1);
    
    QueryInstance qi = qb.createQuery();

    return findObject(qi);       
  }
  
  @Override
  public T findObject(QueryInstance qi) throws NotFoundException {
	  List<T> results = this.namedTemplate.query(qi.getQueryStr(), qi.getParams(), 
		      new RowMapperResultSetExtractor<T>(this.getRowMapper(), 1));

		    if(results.size() == 0)
		    {
		      throw new NotFoundException();
		    }

		    return results.get(0);
  }
  
  @Override
  public List<T> findAllObjects(QueryTerm term) throws NotFoundException
  {
    return this.findMultipleObjects(JdbcBaseDao.convertToList(term), 0);
  }
  
  @Override
  public List<T> findAllObjects(List<QueryTerm> terms) throws NotFoundException
  {
    return this.findMultipleObjects(terms, 0);
  }

  @Override
  public List<T> findMultipleObjects(List<QueryTerm> terms, int limit) throws NotFoundException
  {
    return findMultipleObjects(terms, 0, limit);   
  }

  @Override
  public List<T> findMultipleObjects(List<QueryTerm> terms, int start, int limit)
  {
	  QueryBuilder qb = QueryType.getQueryBuilder(myTable, QueryType.FIND);
	    
	    qb.addQueryExpression(terms, LogicalOpType.AND);
	    
	    qb.setLimit(limit);
	    
	    qb.setOffset(start); 
	    
	    QueryInstance qi = qb.createQuery();

	    return findAllObjects(qi); 
  }
  
  @Override
  public List<T> findAllObjects(QueryInstance qi) throws NotFoundException
  {
    List<T> results = this.namedTemplate.query(qi.getQueryStr(), qi.getParams(), 
      new RowMapperResultSetExtractor<T>(this.getRowMapper(), 1));

    if(results.size() == 0)
    {
      throw new NotFoundException();
    }

    return results;    
  }

//  @Override
//  public boolean tableExists()
//  {
//    
//  }
  
  @Override
  public boolean exists(QueryTerm term)
  {    
    return this.exists(JdbcBaseDao.convertToList(term));
  }
  
  @Override
  public boolean exists(List<QueryTerm> terms)
  {
    boolean result = false;
    
    int count = this.getCount(terms);
    
    if(count != 0)
    {
      result = true;
    }
    
    return result;    
  }
    
  @Override
  public int getCount(QueryTerm term)
  {
    return this.getCount(JdbcBaseDao.convertToList(term));
  }
  
  
  @Override
  public int getCount(List<QueryTerm> terms)
  {
    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, 
      QueryType.COUNT_ALL);
    
    qb.addQueryExpression(terms, LogicalOpType.AND);
    
    QueryInstance qi = qb.createQuery();
    
    return this.namedTemplate.queryForObject(qi.getQueryStr(), qi.getParams(), Integer.class);   
  }
  
  @Override
  public int getCount(QueryInstance qi)
  {
    return this.namedTemplate.queryForObject(qi.getQueryStr(), qi.getParams(), Integer.class);
  }
  
  protected void initOnce(SdkDataSourceImpl dataSource, String tableName, List<String> expectedColNames)
  {
    // This is to protect us from ourselves.
    if(calledOnce)
    {
      throw new IllegalStateException("This approach doesn't work");
    }

    Set<String> dbFieldNames = new HashSet<String>();

    boolean pass = true;
    String causeMsg = null;
    
    calledOnce = true;
    
    try
    {
      //In the future, if there is an error saying FATAL too many clients here
      //It is because that too many connections are open at the same time (probably 5-10) and
      // they are not closed fast enough. We may need to change the config file for this.
      Connection connection = dataSource.getConnection();


      DatabaseMetaData metadata = connection.getMetaData();

      
      ResultSet resultSet = metadata.getColumns(null, null, tableName, null);
      
      while (resultSet.next()) 
      {
        dbFieldNames.add(resultSet.getString("COLUMN_NAME"));
      }

      connection.close();

    }
    catch (SQLException e)
    {
      throw new IllegalStateException(e);
    }

    // If we have 0 columns in the db, the table is assumed to not exist.
    if (dbFieldNames.size() > 0)
    {
      // If the table exists, we must have at least the same number of names...
      // NOTE: The metadata appears to include name of deleted columns. Thus we can no longer
      // look for an exact size match. 
      // 
      // I _think_ the mechanism to remove those now defunct columns is to export the table, 
      // drop the table, import the table. Confirm this sometime.
      if (dbFieldNames.size() >= expectedColNames.size())
      {
        // Check they match up exactly
        for (String expectedName : expectedColNames)
        {
          if (!dbFieldNames.contains(expectedName))
          {
            pass = false;
            causeMsg = "Found an expected field name: " + expectedName + " that does not exist in the data from database";

            break;
          }
        }
      }
      else
      {
        causeMsg = "Field count mismatch";
        pass = false;
      }

      if (!pass)
      {
    	 
        throw new IllegalStateException("In table " + tableName
          + ": "
          + causeMsg);
      }
    }
  }
  
  @Override
  public int deleteById(int dbid)
  {
    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, QueryType.DELETE);
    
    qb.addQueryExpression(new QueryTerm(this.myPkName, dbid));
    
    return this.delete(qb.createQuery());
  }
  
  @Override
  public int delete(QueryTerm term)
  {
    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, QueryType.DELETE);
    
    qb.addQueryExpression(term);
    
    return this.delete(qb.createQuery());
  }

  @Override
  public int delete(List<QueryTerm> terms)
  {
    QueryBuilder qb = QueryType.getQueryBuilder(this.myTable, QueryType.DELETE);
    
    qb.addQueryExpression(terms, LogicalOpType.AND);
    
    return this.delete(qb.createQuery());
  }
  
  @Override
  public int delete(QueryInstance qi) 
  {
    int count = this.namedTemplate.update(qi.getQueryStr(), qi.getParams()); 

    return count;    
  }
  

}

