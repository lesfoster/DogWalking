/*
 * Created on May 20, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package html;

import java.util.Vector;

/**
 * @author whitteng
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Entry 
{
	String value = null;
	boolean selected = false;
	
	/*
	 * Used for CheckRadioElement - NOT needed for ComboListElement
	 */
	String displayValue = null;
		
	/**
	 * 
	 */
	public Entry()
	{
		super();
		
	}
	
	public Entry(String value)
	{
			super();
			this.value = value;	
	}
	
	public Entry(String value, boolean selected)
	{
			this (value);
			this.selected = selected;
	}
	
	public Entry(String value,  String displayValue)
	{
			this (value);
			this.displayValue = displayValue;
	}

	public Entry(String value,  String displayValue, boolean selected)
	{
			this (value, displayValue);
			this.selected = selected;
	}
	
	static public Vector createEntriesVecFromStringVecs 
		(Vector values, Vector displayValues)
	{
	  Vector entriesVector = new Vector ();
	  int nEntries = values.size();
	  String value = null;
	  String displayValue = null;
	  
	  for (int k=0; k<nEntries; k++)
	  {
	      value = (String) values.elementAt(k);
	      displayValue = (String) displayValues.elementAt(k);
	      
	      entriesVector.add(new Entry (value, displayValue));
	  }
	  
	  return (entriesVector);
	}
	
	static public Vector createEntriesVecFromStringVec 
				(Vector values)
	{
	    Vector entriesVector = new Vector ();
	    int nEntries = values.size();
	    String value = null;
	    String displayValue = null;
  
	    for (int k=0; k<nEntries; k++)
	    {
	        value = (String) values.elementAt(k);
      
	        entriesVector.add(new Entry (value));
	    }
  
	    return (entriesVector);
	}
	

	/**
	 * @return
	 */
	public boolean isSelected()
	{
		return selected;
	}

	/**
	 * @return
	 */
	public String getValue()
	{
		if (value == null)
		{ return (""); }

		return value;
	}

	/**
	 * @param b
	 */
	public void setSelected(boolean b)
	{
		selected = b;
	}

	/**
	 * @param string
	 */
	public void setValue(String string)
	{
		value = string;
	}

	/**
	 * @return
	 */
	public String getDisplayValue()
	{
		if (displayValue == null)
		{ return (""); }
		
		return displayValue;
	}

	/**
	 * @param string
	 */
	public void setDisplayValue(String string)
	{
		displayValue = string;
	}

}
