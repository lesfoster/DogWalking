package db;

import common.*;
import java.util.*;

public class UpdateConfig
{
	String tableName;
	Vector cols;
	int nCols;
	String pk; // Primary Key
	
	public UpdateConfig (String tableName, FieldSet fieldSet)
	{
		this.tableName = tableName;
		String fieldName = null;
		Field field = null;
		cols = new Vector();
		
		int nFields = fieldSet.getnFields();
		
		for (int k=0; k<nFields; k++)
		{
			field = fieldSet.getField(k);
			if ( (field.DBType >= 0) && (!field.getPk()) )
			{
				cols.addElement(field.getName());
			}	
		}
		
		nCols = cols.size();
	}
	
	public String createStatement (Vector vals, int pkVal)
	{
		StringBuffer outBuf = new StringBuffer("UPDATE ");
		String col;
		 
		outBuf.append (tableName + " SET ");
		
		String val;
		
		for (int k=0; k<nCols; k++)
		{
		 val = (String) vals.elementAt(k);
		 
		 if (val != null)
		 {
		 	val = val.trim();
		 	if (!val.equals(""))
		 	{
		 		col = (String) cols.elementAt(k);
		 		
		 		if (k>0)
		 		{ outBuf.append (", "); }
		 		
		 		outBuf.append (col + " = \"" + val + "\"");
		 	}
		 }
		 
		}
		
		outBuf.append (" WHERE " + pk + " = " + Integer.toString(pkVal) );
		
		return (outBuf.toString());
	}
	
	/**
	 * Gets the cols
	 * @return Returns a Vector
	 */
	public Vector getCols() {
		return cols;
	}
	/**
	 * Sets the cols
	 * @param cols The cols to set
	 */
	public void setCols(Vector cols)
	{
		this.cols = cols;
	}

	/**
	 * Gets the pk
	 * @return Returns a String
	 */
	public String getPk() {
		return pk;
	}
	/**
	 * Sets the pk
	 * @param pk The pk to set
	 */
	public void setPk(String pk)
	{
		this.pk = pk;
	}

}

