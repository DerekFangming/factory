package com.factory.dao;

import java.util.List;

import com.factory.dao.impl.NVPair;
import com.factory.dao.impl.QueryInstance;
import com.factory.dao.impl.QueryTerm;
import com.factory.exceptions.NotFoundException;
import com.factory.utils.Pair;

public interface CommonDao<T extends Object>
{
 
  /////////////////////////////////////////////////////////////////////
  //
  //  Table-level Methods
  //
  /////////////////////////////////////////////////////////////////////

  /**
   * Creates a single table.
   * @param table Table to create.
   * @throws AlreadyExistsException 
   */
//  public void tableCreate() throws AlreadyExistsException;
  
  /**
   * Returns whether the specified table exists in the database
   */
//  public boolean tableExists();
  
  /**
   * Drops a single table.
   * @param table Table to drop.
   * @throws NotFoundException 
   */
//  public void tableDrop() throws NotFoundException;
  
  /////////////////////////////////////////////////////////////////////
  //
  //  Row-level Methods
  //
  /////////////////////////////////////////////////////////////////////

  // Note: Toyed with replacing all of the varargs: QueryTerm... values with
  // a Map<String, Object> in the hope replacing calls to the Pair() constructor for each
  // argument. However, for a Map to work with MapSqlParameterSource (which is where
  // the Pairs or Map are consumed), it means the Map's value needs to be a SqlParameterValue
  // (using java.sql.Type). Thus, we would be trading calls to the Pair constructor with
  // calls to the SqlParameterValue constructor.
  
  List<Pair<Enum<?>, String>> FieldTypes = null;

  /**
   * Inserts a row in the current table. Assumes the table has a primary key defines.
   * 
   * @param obj The object forming the basis of the new row.
   * 
   * @return For tables with a primary key, returns the internal 
   * database ID of the newly created row; otherwise 0. 
   * 
   * @throws IllegalArgumentException If the number of values does not
   * match <i>exactly</i> the number of columns in the table, less the 
   * primary key field (if applicable).
   */
  int persist(T obj) throws IllegalArgumentException;  
  
  /**
   * Updates a row in the current table
   * 
   * @param dbId The internal database ID for the row to modify.
   * 
   * @param values A list of <tt>Pairs</tt> containing the field names (from the <tt>enum</tt> element
   * defined in the concrete class) and values to change.
   * 
   * @throws NotFoundException If no such row exists
   */
  void update(int dbId, List<NVPair> values) throws NotFoundException;
  void update(int dbId, NVPair value) throws NotFoundException;
  
  /**
   * Updates a row in the current table.
   * @param dbId The internal database ID for the row to modify. A caller should be able to 
   * obtain this value by getting it directly from the <tt>obj</tt>.
   * 
   * @param obj Source object for updating the table row's fields.
   *  
   * @throws NotFoundException If no such row exists
   */
  void update(int dbId, T obj) throws NotFoundException;
    
  /**
   * Returns the domain object for the specified internal database ID. 
   * @param dbId Internal database ID for the row to return.
   * 
   * @throws NotFoundException If no such row exists
   */
  T findById(int dbId) throws NotFoundException;

  /** NOTE: Not easily supported:
   * Short story: Needs custom & potentially complicated RowMapper(s)
   * Long story comes from trying to use SchemaTable.FIND_BY_XID_SQL and SchemaTable.FIND_BY_XREF_SQL which look,
   * for example, like this:
   *  SELECT * FROM users WHERE id = (SELECT target_id FROM external_references WHERE partner_id = :partner_id AND ext_ref_type_id = :ext_ref_type_id AND id = :id)
   * 
   * That query works only in the context of the UserDao which has a row mapper to deal with a User object.
   * If we execute the above query from here (the ExternalReferencesDao), the row mapper will fail - it understands 
   * ExternalReferences, not Users.
   * 
   * btw: If we tried to execute either FIND_BY_XID_SQL or FIND_BY_XREF_SQL, they would be null in the context of
   * the ExternalReferencesDao as they are generated only for objects that get an entry in external_references (and
   * not for an ExternalReference itself). So to fall into the trap of having the wrong row mapper, the
   * implementation of findByXinfo() would need to do this for ex:
   * 
   *   if(Util.isNullOrEmpty(FIND_BY_XREF_SQL))
   *   {
   *     SchemaTable table = SchemaTable.getTableForType(type);
   *     query = table.getFindByXrefSqlCmd();
   *   }
   *   
   * And as noted, would need access to a User-specific custom row mapper.
   */
  //   T findByXinfo(long partnerId, ExternalReferenceType type, XPair xinfo) throws NotFoundException;
  
  
  /**
   * Returns the first IDs matching all of the specified fields with exact values.
   * That is to say the resulting query is of the form:
   * <br/> 
   * <i>field1 = value1</i> AND <i>field2 = value2</i> AND ...  
   * @param values A list of <tt>Pairs</tt> containing field names (as the <tt>enum</tt> element
   * defined in the concrete class) and values to search on.
   * 
   * @return The first internal database IDs matching the <tt>values</tt>. 
   * 
   * @throws NotFoundException If no such row exists
   */
  int findId(QueryTerm term) throws NotFoundException;
  int findId(List<QueryTerm> terms) throws NotFoundException;
  
  /**
   * Returns the list of row IDs matching all of the specified fields with exact values.
   * That is to say the resulting query is of the form:
   * <br/> 
   * <i>field1 = value1</i> AND <i>field2 = value2</i> AND ...  
   * @param terms A list of <tt>Pairs</tt> containing field names (as the <tt>enum</tt> element
   * defined in the concrete class) and values to search on.
   * 
   * @return A list of internal database IDs matching the <tt>values</tt>.  
   * 
   * @throws NotFoundException If no rows exist
   */
  List<Integer> findAllIds(List<QueryTerm> terms) throws NotFoundException;
  
  List<Integer> findAllIds(QueryInstance qi) throws NotFoundException;
  
  /**
   * Returns the first object matching all of the specified fields with exact values.
   * That is to say the resulting query is of the form:
   * <br/> 
   * <i>field1 = value1</i> AND <i>field2 = value2</i> AND ...  
   * @param terms A list of <tt>Pairs</tt> containing field names (as the <tt>enum</tt> element
   * defined in the concrete class) and values to search on.
   * 
   * @return The first result matching the <tt>values</tt>. 
   * 
   * @throws NotFoundException If no such row exists
   */
  T findObject(List<QueryTerm> terms) throws NotFoundException;

  T findObject(QueryTerm term) throws NotFoundException;
  
    /**
   * Returns the list of domain objects from all rows in the table.
   * <p>
   * <b>Note:</b> Use with caution. <tt>findAllObjects()</tt> is intentende for use on tables
   * containing a modest number of rows. 
   * @return The list of domain object instances.  
   * @throws NotFoundException If no rows exist
   */
  List<T> findAllObjects() throws NotFoundException;
  

  /**
   * Returns the list of domain objects matching all of the specified fields with exact values.
   * That is to say the resulting query is of the form:
   * <br/> 
   * <i>field1 = value1</i> AND <i>field2 = value2</i> AND ...  
   * @param terms A list of <tt>Pairs</tt> containing field names (as the <tt>enum</tt> element
   * defined in the concrete class) and values to search on.
   * 
   * @return A list of domain object instances matching the <tt>values</tt>.  
   * 
   * @throws NotFoundException If no rows exist
   */
  List<T> findAllObjects(List<QueryTerm> terms) throws NotFoundException;
  
  List<T> findAllObjects(QueryTerm term) throws NotFoundException;
  
  List<T> findAllObjects(QueryInstance qi) throws NotFoundException;

  List<T> findMultipleObjects(List<QueryTerm> terms, int limit) throws NotFoundException;
  
  // TODO: Actually this to support "paging" of results.
  List<T> findMultipleObjects(List<QueryTerm> terms, int start, int limit);
  
  /**
   * Determines whether one or more rows exist matching all of the specified fields with exact values.
   * That is to say the resulting query is of the form:
   * <br/> 
   * <i>field1 = value1</i> AND <i>field2 = value2</i> AND ...  
   * @param values A list of <tt>Pairs</tt> containing field names (as the <tt>enum</tt> element
   * defined in the concrete class) and values to search on.
   * 
   * @return <tt>true</tt> if at least one row matches the <tt>values</tt>; otherwise <tt>false</tt>.  
   */
  boolean exists(QueryTerm term);
  
  boolean exists(List<QueryTerm> terms);
  
//  boolean tableExists();
  
  /**
   * Returns the number of rows matching all of the specified fields with exact values.
   * That is to say the resulting query is of the form:
   * <br/> 
   * <i>field1 = value1</i> AND <i>field2 = value2</i> AND ...  
   * @param values A list of <tt>Pairs</tt> containing field names (as the <tt>enum</tt> element
   * defined in the concrete class) and values to search on.
   * 
   * @return The number of rows matching the <tt>values</tt>.  
   */
  int getCount(QueryTerm term);
  int getCount(List<QueryTerm> terms);
  int getCount(QueryInstance qi);

  //TODO: @NeedsAutority("SYS_ADMIN")
  int deleteById(int dbid);
  int delete(QueryTerm term);
  // ANDed terms
  int delete(List<QueryTerm> terms);
  int delete(QueryInstance qi);
  
   
}
  


