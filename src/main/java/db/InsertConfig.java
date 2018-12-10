package db;

import java.util.*;

public class InsertConfig 
{
	Hashtable insertFields;
	String tableName;
	
	public InsertConfig (String tableName)
	{
		this.tableName = tableName;
	}
	
	public InsertConfig (String tableName, Hashtable updateFields)
	{
		this (tableName);
		this.insertFields = updateFields;
	} 

	/**
	 * Gets the updateFields
	 * @return Returns a Hashtable
	 */
	public Hashtable getInsertFields() {
		return insertFields;
	}
	/**
	 * Sets the updateFields
	 * @param updateFields The updateFields to set
	 */
	public void setInsertFields(Hashtable updateFields) {
		this.insertFields = updateFields;
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

}

