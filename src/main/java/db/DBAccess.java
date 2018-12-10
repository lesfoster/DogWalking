package db;

import java.util.*;
import java.sql.*;

import common.Field;

import connectionpool.*;

import util.*;

public class DBAccess 
{
	boolean isConnectionPool = false;
	int initialConnections = -1;
	int maxConnections = -1;
	
	boolean connectionsMade = false;
	
	// If no Connection Pooling:	
	Connection conGlobal = null;
	
	ConnectionPool conPool = null;
	
	String DBInfo = null;
	
	String DBpropsFile = null;
	
	public DBAccess ()
	{
		makeConnections();
		discoverDBinfo ();
	}
	
	public boolean isConnectionsMade() {
		return connectionsMade;
	}

	public DBAccess (boolean isConnectionPool)
	{
		this.isConnectionPool = isConnectionPool;
		makeConnections();
		
	//	discoverDBinfo ();
	}
	
	public DBAccess 
		(String DBpropsFile)
	{
		this.DBpropsFile = DBpropsFile;

		makeConnections();
		
	//	discoverDBinfo ();
	}
	
	public int updateData (UpdateConfig updateConfig, Hashtable paramPairs)
	{
		int kRow = 0;
		int kCol = 0;
		String indexSuffix = null;
		String colName = null;
		Vector colNames = updateConfig.getCols();
		int nCols = colNames.size();
		String val;
		String pkName = updateConfig.getPk();
		String pkStringVal;
		int pkIntVal;
		
		Vector updateVals = null;
		
		boolean cont = true;
		boolean empty = true;
		String updateString = null;
		
		Connection con = null;
		Statement statement = null;
		
	  try
	  {
		 con = getCon();
			
		 statement = con.createStatement();
		 
		 String[] valArray = null;
		
		while (cont)
		{
			indexSuffix = ":" + Integer.toString(kRow);
			
			updateVals = new Vector();
			
			empty = true;
			
			for (kCol=0; kCol<nCols; kCol++)
			{ 
				colName = (String) colNames.elementAt(kCol);
			
				valArray = (String[]) paramPairs.get(colName + indexSuffix);
			
				if ( (valArray == null) || (valArray.length < 1) )
				{ continue; }
			
				val = valArray[0];
				
				updateVals.addElement(val);
			
				if (val != null)
				{ empty = false; }
			}
			
			if (empty)
			{ cont = false; }
			else
			{ 
				valArray = (String[]) paramPairs.get(pkName + indexSuffix);
			
				if ( (valArray == null) || (valArray.length < 1) )
				{ continue; }
				
				pkStringVal = valArray[0];
				pkIntVal = Integer.parseInt (pkStringVal);
				updateString = updateConfig.createStatement(updateVals, pkIntVal);
				
				statement.executeUpdate(updateString);
				
				kRow++;
				 
			};
		}
		
		returnCon (con);
	  }
		
	  catch (Exception e)
	  {
		System.out.println("**ERROR UPDATING ROW: "  
			+ updateString + "\n" + e.getMessage());
		e.printStackTrace();
	  }
		
	  return (kRow);
	}
	
	
	public int insertData (InsertConfig insertConfig)
	{
		Connection con = null;
		Statement statement = null;
		Hashtable insertParamPairs = insertConfig.getInsertFields();
		String tableName = insertConfig.getTableName();
		Enumeration en = insertParamPairs.keys(); 
		Field field;
 		String colName;
 		StringBuffer insertString;
 		StringBuffer insertStringColsPart = new StringBuffer();
 		boolean firstCol = true;
 		Vector colValsSet = new Vector();
 		String[] colVals = null;
 		String insertStringPrefix = "INSERT INTO "+ tableName + " ";
 		int nRows = 0;
 		int nCols = 0;
 		String val;
 		int nRowsComplete = 0; 	
 				
 		StringBuffer insertRowStringBuf = null;
 		String insertRowString = null;
 		
 		try
		{
			con	= getCon();
			
			statement = con.createStatement();
 		
 			insertStringColsPart.append ("( ");
 		
			while ( en.hasMoreElements() ) 
			{ 
				colName = (String) en.nextElement();
    		
    			if (!firstCol)
    			{ insertStringColsPart.append(", "); }
    			
    			insertStringColsPart.append(colName);
    			firstCol = false;
   			
   				colVals = (String[]) insertParamPairs.get(colName);
   				colValsSet.addElement(colVals);
   				nCols++;			 	
			}
		
		nRows = colVals.length;
		
		insertStringColsPart.append(") ");
		
		insertStringPrefix = insertStringPrefix + 
							 insertStringColsPart.toString() + 
							 "VALUES (";
		
		for (int kRow=0; kRow<nRows; kRow++)
		{
			insertRowStringBuf = new StringBuffer();
			
			insertRowStringBuf.append(insertStringPrefix);
			
			for (int kCol=0; kCol<nCols; kCol++)
			{
				if (kRow > 0)
				{ insertRowStringBuf.append (", "); }
				// strip off next value set and append to insertStringPrefix
				colVals = (String[]) colValsSet.elementAt(kCol);
				val = colVals[kRow];
				
				insertRowStringBuf.append (" \'" + val + "\'");
				
				if (kCol <nCols-1)
				{ insertRowStringBuf.append (", "); }
				
			}
			
			insertRowStringBuf.append (" );");
			insertRowString = insertRowStringBuf.toString();
			
			// Execute the SQL statement
			statement.executeUpdate(insertRowString);
			nRowsComplete++;
		  }
		
		 statement.close();
		 returnCon (con);
		}
		catch (Exception e)
		{
				System.out.println("**ERROR INSERTING ROW: "  
					+ insertRowString + "\n" + e.getMessage());
				e.printStackTrace();
		}
		
		
		
	  return (nRowsComplete); 
	}
	
	public ExecuteResults queryData (QueryConfig queryConfig)
	{
		String tableName = queryConfig.getTableName();
		Vector columns = queryConfig.getColumns();
		Hashtable whereFields = queryConfig.getWhereFields();
		String whereString = queryConfig.getWhereString();
		int nCols = -1;
		
		Field whereField;
		String[] whereValues = null;
		
		StringBuffer queryStringBuf = new StringBuffer ("SELECT ");
		String queryString;
		
		String column = null;
		String colsString = null;
		
		StringBuffer whereStringBuf = null;
		boolean first = true;
		
		ResultSet rs = null;
		
		if (columns != null)
		{
		 	nCols = columns.size();
		}
		
		if (nCols > 0)
		{
			StringBuffer colsStringBuf = new StringBuffer();
			
			String colVal;
			
			for (int k=0; k<nCols; k++)
			{
				colVal = (String) columns.elementAt(k);
				
				colsStringBuf.append (colVal);
				
				if (k <nCols - 1)
				{
					colsStringBuf.append (", ");
				}
				
			} 
			
			colsString = colsStringBuf.toString();
		}
		else
		{  // All Columns
			colsString = " * ";
			
		}
			
		queryStringBuf.append
			(colsString + " from " + tableName + " ");
		
		if (whereString != null)
		{
			queryStringBuf.append ("WHERE " + whereString);
		}				
		else if (whereFields != null)
		{	
			Enumeration keys = whereFields.keys(); 
			
			whereStringBuf = new StringBuffer ("WHERE ");
			
			// boolean first = true;
			
			String whereName, whereValue;
		
			while ( keys.hasMoreElements() ) 
			{    			
    			// whereField = (Field) en.nextElement(); 
				// whereName = whereField.getName();

    		 	whereName = (String) keys.nextElement();
    		
    		 	/*
    			whereValues = (String[]) whereFields.get(whereName);
    		
   				whereValue = whereValues[0]; 
   				 */
    		 	whereValue= (String) whereFields.get(whereName);
    		 	
   				if (whereValue == null)
   				{ continue; }
   				 
   				whereValue = whereValue.trim();
   				
   				if (whereValue.equals(""))
   				{ continue; }

				if (!first)
    			{
    				whereStringBuf.append ("AND ");
    			} 

				whereStringBuf.append (whereName + " " + whereValue + " ");
    			first = false;
			}
		}
		
		if (!first)
		{
			queryStringBuf.append (whereStringBuf.toString());
		}
		
		queryString = queryStringBuf.toString();
		
		// Execute the SQL statement
		
		Connection con = null;
		Statement statement = null;
		
		try
		{
			con = getCon();
			statement = con.createStatement();
			statement.execute(queryString);
			rs = statement.getResultSet();
		}
		catch (Exception e)
		{
				System.out.println("**ERROR EXECUTING QUERY: "  
					+ queryString + "\n" + e.getMessage());
				e.printStackTrace();
		}
		
		ExecuteResults queryResults = new
			ExecuteResults (rs, statement, con, conPool);
		
		returnCon (con);
			
		return (queryResults);
	}
	

	public ExecuteResults queryData (String queryString)
	{
		// Execute the SQL statement
		Connection con = null;
		Statement statement = null;
		ResultSet rs = null;
		
		try
		{
			con = getCon();
			statement = con.createStatement();
			statement.execute(queryString);
			rs = statement.getResultSet();
		}
		catch (Exception e)
		{
				System.out.println("**ERROR EXECUTING QUERY: "  
					+ queryString + "\n" + e.getMessage());
				e.printStackTrace();
		}
		
		ExecuteResults queryResults = new
			ExecuteResults (rs, statement, con, conPool);
		
		returnCon (con);
			
		return (queryResults);
	}

	
	public boolean makeConnections()
{
	if (connectionsMade)
	{ return (true); }
	
	SQLWarning warn;
	Connection con;
	
	String driverName;

	String driverURLprefix;
	String host;
	String dbName;

	String driverURL;

	String dbDelimiter;
	
	String userName; 
	String password;
	
	String initialConnectionsString = null;
	
	String maxConnectionsString = null;
	
	boolean problems = false;
	
	PropertiesProc propsProc =
		new PropertiesProc (DBpropsFile);
	
	// Read JDBC driver and driver URL info:
	driverName = propsProc.readProperty("JDBC_DRIVER");
		
	driverURLprefix = propsProc.readProperty("DRIVERURL_PREFIX");
	
	host = propsProc.readProperty("HOST");
	
	dbDelimiter = propsProc.readProperty("DB_DELIMITER");
	
	dbName = propsProc.readProperty("DBNAME");
	
	driverURL = driverURLprefix + host + dbDelimiter + dbName;
	
	userName = propsProc.readProperty("USERNAME");
	
	password = propsProc.readProperty("PASSWORD");	
	
	initialConnectionsString = propsProc.readProperty("INITIAL_CONNECTIONS");
	if (initialConnectionsString != null)
	{ initialConnections =
		Integer.parseInt (initialConnectionsString); }
	
	maxConnectionsString = propsProc.readProperty("MAX_CONNECTIONS");
	if (maxConnectionsString != null)
	{ maxConnections = 
		Integer.parseInt (maxConnectionsString); }
	
	if ( (initialConnections > 0) && (maxConnections > 0) )
	{
		isConnectionPool = true;
	}
	else
	{
		isConnectionPool = false;
	}
	
	try
	{
		if (isConnectionPool)
		{
			conPool = new ConnectionPool
				(driverName,
				 driverURL,
				 userName,
				 password,
				 initialConnections,
				 maxConnections,
				 true);
		}
		else
		{ // No Pooling - single connection
				// Load the JDBC driver
				//Class.forName("jdbc.odbc.JdbcOdbcDriver");
			Class.forName(driverName);
				
			conGlobal = DriverManager.getConnection
				  (driverURL,
				   userName,
				   password);
				 	 
			if ((warn = conGlobal.getWarnings()) != null)
			{
				while (warn != null)
				{
					warn = warn.getNextWarning();
				}
			}
			
		}
				 
			// Insert connection into hashtable of all active connections.
//			dbwAccesses.put(DBWURL, dbwAccess);
		}
		catch (ClassNotFoundException e)
		{
			System.out.println
				("**ERROR (DBWaccessManager.makeConnections): "+ e.getMessage());
	
			e.printStackTrace();
			
			problems = true;
		}
		catch (SQLException e)
		{
			System.out.println
				("**ERROR (DBWaccessManager.makeConnections): " + e.getMessage());
	
			e.printStackTrace();
			
			problems = true;
		}
		
		if (!problems)
		{
		    connectionsMade = true;
		
		    System.out.println("** GOOD DB CONNECTION **");
		}
		
		return (connectionsMade);
	}
	
	public java.sql.Connection getCon() 
	{
		if (isConnectionPool)
		{
			Connection conLocal = null;
			try
			{
		 		conLocal = conPool.getConnection();
			} 
			catch(Exception e) 
			{ System.out.println
				("**ERROR (DBWaccess.getCon) No con. from Pool: \n"+ e.getMessage());
				
			  e.printStackTrace();
			}
			return (conLocal);
		}
		else
		{
			return conGlobal;
		}
	}
	
	public void returnCon (Connection con) 
	{
		if (isConnectionPool)
		{
			Connection conLocal = null;
			try
			{
		 		conPool.free(con);
			} 
			catch(Exception e) 
			{ System.out.println
				("**ERROR Returning Connection: \n"+ e.getMessage());
				
			  e.printStackTrace();
			}
		}
		else
		{
			// leave global connection alone
		}
	} 
	
	public boolean discoverDBinfo ()
	{
	    Connection con = getCon();
	    
	    discoverTablesInfoFoxPro (con);
	    
	    returnCon (con);
	    
	    return true; // Connection status
	}
	
	
	
	public boolean discoverTablesInfoGeneral(Connection con)
	{
	    DatabaseMetaData dbMetaData = null;
		Statement selectStatement = null;
		ResultSet rsTables = null;
		ResultSet rsCols = null;
	    String stringTemp = null;
	    StringBuffer stringBuff = new StringBuffer ();
		
		try 
		{
			// Obtain database metadata.
			dbMetaData = con.getMetaData();
			// DO WE NEED THIS??: selectStatement = con.createStatement();

			String[] tabTypes = { "TABLE" }; // get only tables having data
            String tableName = null;
            String tableType = null;
			rsTables = dbMetaData.getTables(null, null, null, tabTypes); // table info
			int nTables = 0;
			
			while (rsTables != null && rsTables.next())
			{   
				// Read a row of the result set
				nTables++;
			    
			    //tableCatalog = rs.getString("TABLE_CAT");
				//tableSchema = rs.getString("TABLE_SCHEM");
				tableName = rsTables.getString("TABLE_NAME");
				tableType = rsTables.getString("TABLE_TYPE");
					
				stringTemp = 	
					("\n ** Table: " + nTables + ":" + 
					 " Name: " + tableName + 
					 " Type: " + tableType + "\n");
				
				System.out.print (stringTemp);
				
				stringBuff.append ("<PRE>/n");
				
				stringBuff.append (stringTemp);
				
				rsCols = dbMetaData.getColumns
					(null, null, tableName, null); // columns info
				
				int nCols = 0;
				
				while (rsCols.next())
				{
				    nCols++;
					//String catalog = rs.getString("TABLE_CAT");
					//String schema = rs.getString("TABLE_SCHEM");
					//String tableName = rs.getString("TABLE_NAME");
					String columnName = rsCols.getString("COLUMN_NAME");
					int dataType = rsCols.getInt("DATA_TYPE"); // SQL type from java.sql.Types
					String typeName = rsCols.getString("TYPE_NAME"); // Data source dependent type name, for a UDT the type name is fully qualified
					int columnSize = rsCols.getInt("COLUMN_SIZE");
					//int numFracDigits = rsCols.getInt("DECIMAL_DIGITS"); // number of fractional digits
					//int radix = rs.getInt("NUM_PREC_RADIX"); // Radix (typically either 10 or 2)
					//int nullable = rs.getInt("NULLABLE");
						//* columnNoNulls - might not allow NULL values
						//* columnNullable - definitely allows NULL values
						//* columnNullableUnknown - nullability unknown 

					//String remarks = rs.getString("REMARKS");
					//int charMaxBytes = rs.getInt("CHAR_OCTET_LENGTH"); // for char types the maximum number of bytes in the column
					//int ordinalPos = rs.getInt("ORDINAL_POSITION");
					//String isNullableStr = rs.getString("IS_NULLABLE"); // "NO" means column definitely does not allow NULL values; "YES" means the column might allow NULL

					//int sqlDataType = rs.getInt("SQL_DATA_TYPE"); // unused
					//int sqlDateTimeSub = rs.getInt("SQL_DATETIME_SUB"); // unused
					stringTemp = 	
						(" **** Column: " + nCols + ":" + 
						 " Name: " + columnName + 
						 " Type: " + typeName + "\n");
					
					System.out.print (stringTemp);
					
					stringBuff.append (stringTemp);			
				}
				rsCols.close();
				
				
				stringBuff.append ("</PRE>/n");
			}
				
			rsTables.close();
		}
		catch (SQLException e) 
		{
			System.out.println(
				"XXX ERROR (DatabaseInfo_Access.encapDbMetadata): " + e.getMessage());
			e.printStackTrace();
		}
		
		DBInfo = stringBuff.toString();
	    
	    return (true); // Connection/DB status
	}
    /**
     * @return Returns the dBInfo.
     */
    public String getDBInfo()
    {
        return DBInfo;
    }

	public boolean discoverTablesInfoFoxPro(Connection con)
	{
	    DatabaseMetaData dbMetaData = null;
		Statement selectStatement = null;
		//ResultSet rsTables = null;
		ResultSet rsCols = null;
	    String stringTemp = null;
	    StringBuffer stringBuff = new StringBuffer ();
	    
	    String tableName = null;
        String tableType = null;
        String columnName = null;
		String typeName = null; 
	    Vector colNames = new Vector();
	    Vector colTypes = new Vector();
		
		try 
		{
			// Obtain database metadata.
			dbMetaData = con.getMetaData();
			// DO WE NEED THIS??: selectStatement = con.createStatement();
	
			String[] tabTypes = { "TABLE" }; // get only tables having data
	        
			//rsTables = dbMetaData.getTables(null, null, null, tabTypes); // table info
			int nTables = 1;
			boolean cont = true;

			tableName = "ANIMALS.DBF";
			//tableName = "NAMES.DBF";
			
			stringTemp = 	
				("\n ** Table: " + nTables + ":" + 
				 " Name: " + tableName + "\n");
				 //" Type: " + tableType + "\n");
			
			System.out.print (stringTemp);
			
			stringBuff.append ("<PRE>/n");
			
			stringBuff.append (stringTemp);
			
			rsCols = dbMetaData.getColumns
				(null, null, tableName, null); // columns info
			
			int nCols = 0;
			
			while (rsCols.next())
			{
				nCols++;
				//String catalog = rs.getString("TABLE_CAT");
				//String schema = rs.getString("TABLE_SCHEM");
				//String tableName = rs.getString("TABLE_NAME");
				columnName = rsCols.getString("COLUMN_NAME");
				//int dataType = rsCols.getInt("DATA_TYPE"); // SQL type from java.sql.Types
				typeName = rsCols.getString("TYPE_NAME"); // Data source dependent type name, for a UDT the type name is fully qualified
				//int columnSize = rsCols.getInt("COLUMN_SIZE");
				
				stringTemp = 	
						(" **** Column: " + nCols + ":" + 
						 " Name: " + columnName + 
						 " Type: " + typeName + "\n");
					
				System.out.print (stringTemp);
					
				stringBuff.append (stringTemp);
				
				colNames.add (columnName);
				colTypes.add (typeName);
			}  // while
				
			rsCols.close();
			
			stringBuff.append ("</PRE>\n");
			
			String queryString = 
				"SELECT * from " + tableName + " ;";
			
		//	String queryString = 
		//		"SELECT * from " + tableName + " where date_dispo is NULL;";
		
//			String queryString = 
//				"SELECT * from " + tableName + " where name =  \"SOLOMON\";";
					
			Statement statement = con.createStatement();
			statement.execute(queryString);
			ResultSet rsTableData = statement.getResultSet();
			
			//StringBuffer stringBuf = new StringBuffer("<TABLE><TR>");
			stringBuff.append("<TABLE BORDER=\"1\">\n<TR>\n");
			
			int k=0;
			
			for (k=0; k<nCols; k++)
			{
			   columnName = (String) colNames.elementAt (k); 
			   stringBuff.append ("<TD>" + columnName + "</TD>");
			}
			
			String colData = null;
			int nRows = 0;
			
			//while ( (rsTableData.next()) && (nRows <= 100) )
			//while ( (rsTableData.next()))
			while ( (rsTableData.next()) && (nRows <= 100) )
			{
			    stringBuff.append ("\n<TR>\n");
			    
			    for (k=0; k<nCols; k++)
				{
				   columnName = (String) colNames.elementAt (k); 
				   colData = rsTableData.getString (columnName);
				   stringBuff.append ("<TD>" + colData + "</TD>");
				}
			    
			    stringBuff.append ("\n</TR>\n");
			    nRows++;
			    
			    System.out.println("<*** " + nRows + " ***>");
			}
			
			stringBuff.append("</TABLE>");
			
			System.out.println("<%%%%%%% FINISHED TABLE DATA %%%%%%%%>");
			
		} // try
		catch (SQLException e) 
		{
			System.out.println(
				"XXX ERROR (DatabaseInfo_Access.encapDbMetadata): " + e.getMessage());
			e.printStackTrace();
		}
		
		DBInfo = stringBuff.toString();
	    
	    return (true); // Connection/DB status
	}

}
		

