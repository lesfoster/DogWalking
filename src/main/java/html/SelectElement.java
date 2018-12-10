/*
 * Created on May 20, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package html;

import java.util.Hashtable;
import java.util.Vector;

/**
 * @author whitteng
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 * 
 *  Parent Class for <br>
 *  - Checkbox/Radio button Element (CheckRadioElement) <br>
 *  - Combobox/Listbox Element (ComboListElement) <br>
 *  the "outputHTML" method must be overrriden <br>
 *  the "multiple" parameter defaults to "TRUE"

 */
public class SelectElement
{
	Hashtable entriesHT = new Hashtable();
	Vector entriesVec = null;
	boolean multiple = true;
	String name = null;
	int nEntries = -1;
	
	/**
	 * 
	 */
	public SelectElement()
	{
		super();
		// TODO Auto-generated constructor stub
	}
	
	public SelectElement (String name)
	{
			super();
			this.name = name;
	}
	
	public SelectElement (String name, Vector entryStrings)
	{
		super();
		this.name = name;
		
		String value = null;
		Entry entry = null;
		// entries ONLY have "value" - NO "displayValue":
		entriesVec = Entry.createEntriesVecFromStringVec(entryStrings);
		nEntries = entriesVec.size();
		
		for (int k=0; k<nEntries; k++)
		{
			// ??? value = (String) entries.elementAt(k);
		    // ??? value = (String) entries.elementAt(k);
		 	// ??? entry = new Entry(value);
		 	entry = (Entry) entriesVec.elementAt(k);
		 	value = entry.getValue();
		 	entriesHT.put(value, entry);
		}
			
	}
	
	public SelectElement (String name, String[] values)
		{
			super();
			this.name = name;
			nEntries = values.length;
			String value = null;
			Entry entry = null;
			entriesVec = new Vector();
		
			for (int k=0; k<nEntries; k++)
			{
				value = values[k];
				entry = new Entry(value);
				entriesHT.put(value, entry);
				entriesVec.addElement(entry);
			}
			
		}
		
	public SelectElement (String name, String[] values, String[] displayValues)
	{
		super();
		this.name = name;
		nEntries = values.length;
		String value = null;
		String displayValue = null;
		Entry entry = null;
		entriesVec = new Vector();

		for (int k=0; k<nEntries; k++)
		{
			value = values[k];
			displayValue = displayValues[k];
			entry = new Entry(value, displayValue);
			entriesHT.put(value, entry);
			entriesVec.addElement(entry);
		}
	
	}
	
	public SelectElement(String name, String[] values, String[] displayValues, String[] selectedValues)
	{
		this (name, values, displayValues);
	
		setSelectedEntries(selectedValues);
	}
	
	public SelectElement(String name, Vector entries, String[] selectedValues)
	{
		this (name, entries);
		
		setSelectedEntries(selectedValues);
	}
	
	public String outputHTML ()
	{ // MUST BE OVERRIDDEN
		
		return ((String) null);
	}
	
	public void setSelectedEntries (String[] selectedValues)
	{
		String value = null;
		int nSelectedValues = selectedValues.length;
		int k=0;
		for (k=0; k<nSelectedValues; k++)
		{
			value = selectedValues[k];
			setSelectedEntry (value);
		}
	}
	
	public void setSelectedEntry (String selectedValue)
	{
		if (selectedValue == null)
		{ 
			clearAllEntrySelections();
			return;
		}
		
		Entry entry = (Entry) entriesHT.get(selectedValue);
		if (entry == null)
		{ 
			clearAllEntrySelections();
			return;
		}
			
		entry.setSelected(true);
	}
	
	public Entry getEntry (String value)
	{
		return ((Entry) entriesHT.get(value));
	}
	
	public Entry getEntry (int kEntry)
	{
		return ((Entry) entriesVec.elementAt(kEntry));
	}
	
	public void clearAllEntrySelections ()
	{
		Entry entry = null;
		
		for (int k=0; k<nEntries; k++)
		{
			entry = (Entry) entriesVec.elementAt(k);
			entry.setSelected(false);
		}

	}
	/**
	 * @return
	 */
	public boolean isMultiple()
	{
		return multiple;
	}

	/**
	 * @param b
	 */
	public void setMultiple(boolean b)
	{
		multiple = b;
	}

}
