package db;

import java.util.*;

public class QueryConfig 
{
	String tableName = null;
	Vector columns = null;
	Hashtable whereFields = null;
	String whereString = null;
	
	public QueryConfig (String tableName, Hashtable whereFields)
	{
		this (tableName);
		this.whereFields = whereFields;
	} 
	
	public QueryConfig (String tableName)
	{
		this.tableName = tableName;
	}
	
	public QueryConfig (String tableName, Vector fromFields)
	{
		this(tableName);
		this.columns = fromFields;
	}
	/**
	 * Gets the tableName
	 * @return Returns a String
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * Sets the tableName
	 * @param tableName The tableName to set
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/**
	 * Gets the fromFields
	 * @return Returns a Vector
	 */
	public Vector getColumns() {
		return columns;
	}
	/**
	 * Sets the fromFields
	 * @param fromFields The fromFields to set
	 */
	public void setColumns(Vector fromFields) {
		this.columns = fromFields;
	}

	/**
	 * Gets the whereFields
	 * @return Returns a Hashtable
	 */
	public Hashtable getWhereFields() {
		return whereFields;
	}
	/**
	 * Sets the whereFields
	 * @param whereFields The whereFields to set
	 */
	public void setWhereFields(Hashtable whereFields) {
		this.whereFields = whereFields;
	}

	public String getWhereString() {
		return whereString;
	}

	public void setWhereString(String whereString) {
		this.whereString = whereString;
	}

}

