package com.factory.dao;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.factory.dao.DaoFieldEnum.OnPersist;
import com.factory.dao.impl.CoreTableType;
import com.factory.utils.Pair;

/**
 * SchemaTable specifies a database table definition. 
 * <p>
 * While not required, the SDK recommends defining the database tables in your 
 * application (or service) via an enum class (as does the SDK itself).
 */
public interface SchemaTable{
	/**
	 * Sets the data source registry and provides post-constructor setup 
	 * which depends on that registry.
	 *	
	 * @param dsr The data source registry
	 */
	void init(DataSourceRegistry dsr);
	
	/**
	 * Returns a the data source containing this table.
	 */
	SdkDataSource getDataSource();

	/**
	 * Returns the DataSourceType of this table.
	 */
	String getDataSourceType();
	
	/**
	 * Return this table's name as it appears in the database
	 */
	String getTableName();

	/**
	 * Returns this table's column definitions.
	 */
	List<Pair<Enum<?>, String>> getColumnDefns( );
	
	/**
	 * Returns the name of this table's primary key; or <tt>null</tt> if none.
	 */
	String getPrimaryKeyName();

	/**
	 * Returns a list of the table's field (aka column) names. This include the primary key name.	
	 */
	List<String> getColumnNames();
	
	boolean isExactFieldCountRequired();
	
	/**
	 * Returns whether this table contains a primary key.
	 */
	default boolean hasPrimaryKey(){
		return (this.getPrimaryKeyName() != null);
	}

	/**
	 * Returns the list of enum values associated with this table.
	 */
	default Enum<?>[] getTypes( ){
		throw new UnsupportedOperationException(
				"If you see this, you need to override " + SchemaTable.class.getSimpleName() + ".getTypes()");
	}

	/**
	 * Returns the count of the table's fields (aka columns). This include the primary key field.	
	 */
	default int getColumnCount(){
		return this.getColumnNames().size();
	}
	
	/**
	 * Returns a list of table column name extracted from the list of table column definitions. 
	 * @param defns Column definitions
	 */
	static List<String> getColumnNames(List<Pair<Enum<?>, String>> defns){
		List<String> columnNames = new ArrayList<String>();
		
		for (Pair<Enum<?>, String> item : defns){
			Enum<?> first = item.getFirst();
			
			String colName = SchemaTable.getName(first);

			columnNames.add(colName);			
		}
		
		return columnNames;
	}

	static int getRequiredColumnCount(List<Pair<Enum<?>, String>> defns){		
		int required = 0;
		
		for (Pair<Enum<?>, String> item : defns){
			DaoFieldEnum dfe = (DaoFieldEnum) item.getFirst();

			if(dfe.getOnPersist() == OnPersist.REQUIRED){
				++required;
			}
		}
			
		return required;
	}
	
	/**
	 * Returns the name of the primary key found in a table's column definitions 
	 * @param defns Column definitions
	 * @return The primary key name or <tt>null</tt> if none is defined for the table.
	 */
	static String getPkName(List<Pair<Enum<?>, String>> defns){
		for (Pair<Enum<?>, String> item : defns){
			Enum<?> first = item.getFirst();

			String colName = SchemaTable.getName(first);

			if (SchemaTable.isPrimaryKey(first)){
				return colName;
			}
		}

		return null;
	}

	/**
	 * Returns the <tt>SchemaTable</tt> type tagged with a specified Enum type.
	 * <p>
	 * For example: Find the <tt>SchemaTable</tt> type tagged with <tt>ExternalReferenceType.GROUP</tt>.
	 * 
	 * @param type Enum type to look for among the schema tables. 
	 * 
	 * @return The <tt>SchemaTable</tt> type tagged with <tt>type</tt>, or <tt>null</tt> if none.
	 */
	public static SchemaTable getTableForType(Enum<?> type){
		boolean foundIt = false;
		SchemaTable result = null;
		
		List<SchemaTable[]> values = new ArrayList<SchemaTable[]>();
		values.add(CoreTableType.values());
		//values.add(LegacyTableType.values());
		//values.add(ContentTableType.values());
		
		for(SchemaTable[] enumList : values){
			for(SchemaTable table : enumList){
				for(Enum<?> e : table.getTypes()){
					if(e == type){
						if(foundIt){
							throw new IllegalStateException(
								"Something is not right: Mutliple tables have been tagged with the enum: " + type.toString());
						}
						
						foundIt = true;
						result = table;
					}
				}
			}
		}
		
		return result;
	}

	// Returns the column / field name from the Enum element.
	public static String getName(Enum<?> enumElt){
		String colName;
	
		@SuppressWarnings("rawtypes")
		Class clazz = enumElt.getClass();
	
		try{
			Field f = clazz.getDeclaredField("name");
			colName = (String) f.get(enumElt);
		}catch (Throwable t){
			throw new IllegalStateException(t);
		}
	
		return colName;
	}


	/**
	 * Returns whether a particular column definition specifies that it is a primary key. 
	 * @param enumElt Column definition
	 * @return <tt>true</tt> if the column is the primary key; otherwiser <tt>false</tt>
	 */
	public static boolean isPrimaryKey(Enum<?> enumElt){
		boolean isPK = false;
	
		@SuppressWarnings("rawtypes")
		Class clazz = enumElt.getClass();
	
		try{
			Field f = clazz.getDeclaredField("isPK");
			isPK = f.getBoolean(enumElt);
		}catch (Throwable t){
			throw new IllegalStateException(t);
		}
	
		return isPK;
	}
}
