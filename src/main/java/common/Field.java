package common;

import java.util.*;

public class Field 
{
	public static final int IntType = 1;
	public static final int StringType = 2;
	
	public int HTMLType = -1;
	
	public int DBType = -1;
	
	public static final int DBcharType = 1;
	public static final int DBintType = 2;
	
	public Vector values = new Vector();
	int nValues = 0;
	
	public String name;
	
	public String label;
	
	public boolean pk = false;
	
	public int order = -1; // for placement in HTML form
	
	
	public String procStringReturn (String stringIn)
	{
		if (stringIn == null)
		{ stringIn = ""; }
		
		return (stringIn);
	}
	
	/**
	 * Gets the values
	 * @return Returns a Vector
	 */
	public Vector getValues() {
		return values;
	}
	/**
	 * Sets the values
	 * @param values The values to set
	 */
	public void setValues(Vector values)
	{
		this.values = values;
	}

	/**
	 * Gets the name
	 * @return Returns a String
	 */
	public String getName() {
		return (procStringReturn(name));
	}
	/**
	 * Sets the name
	 * @param name The name to set
	 */
	public void setName(String name)
	{
		this.name = name;
	}

	/**
	 * Gets the label
	 * @return Returns a String
	 */
	public String getLabel() {
		return (procStringReturn(label));
	}
	/**
	 * Sets the label
	 * @param label The label to set
	 */
	public void setLabel(String label)
	{
		this.label = label;
	}
	
	public Object getValue ()
	{ // common special case where we are only handling one value
		Object retObj = null;
		
		if ( (values != null) && (values.size() >= 1) )
		{
			retObj = (values.elementAt(0));
		}
		 
		return (retObj);
	}
	
	public void clearVals ()
	{
		values.clear();
	}
	
	public Field (int HTMLType)
	{
		this.HTMLType = HTMLType;
	}
	
	public Field (String name, int HTMLType)
	{ // simple default type
		this.name = name;
		this.HTMLType = HTMLType;
	}
	
	public Field (String name, int HTMLType, String label)
	{ // simple default type
		this (name, HTMLType);
		this.label = label;
	}
	
	public Field (String name)
	{
		this.name = name;
	}
	
	public Field (String name, int HTMLType, String label, String value)
	{
		this (name, HTMLType, label);
		
		setValue (value);
	}

	public void setValue (String value)
	{
		values.insertElementAt (value, 0);
		nValues = 1;
	}
	/**
	 * Gets the order
	 * @return Returns a int
	 */
	public int getOrder() {
		return order;
	}
	/**
	 * Sets the order
	 * @param order The order to set
	 */
	public void setOrder(int order)
	{
		this.order = order;
	}

	/**
	 * Gets the hTMLType
	 * @return Returns a int
	 */
	public int getHTMLType() {
		return HTMLType;
	}
	/**
	 * Sets the hTMLType
	 * @param hTMLType The hTMLType to set
	 */
	public void setHTMLType(int hTMLType) {
		HTMLType = hTMLType;
	}

	/**
	 * Gets the dBType
	 * @return Returns a int
	 */
	public int getDBType() {
		return DBType;
	}
	/**
	 * Sets the dBType
	 * @param dBType The dBType to set
	 */
	public void setDBType(int dBType)
	{
		DBType = dBType;
	}

	/**
	 * Gets the pk
	 * @return Returns a boolean
	 */
	public boolean getPk() {
		return pk;
	}
	/**
	 * Sets the pk
	 * @param pk The pk to set
	 */
	public void setPk(boolean pk)
	{
		this.pk = pk;
	}

}

