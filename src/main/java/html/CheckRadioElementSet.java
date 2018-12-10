/*
 * Created on Jul 13, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package html;

import java.util.Vector;

/**
 * @author whitteng
 *
 *  <INPUT TYPE="RADIO"/"CHECKBOX" NAME="creditCard" VALUE="visa" CHECKED> Visa
 *
 * 
 */
public class CheckRadioElementSet extends SelectElement
{
	
	/**
	 * 
	 */
	public CheckRadioElementSet()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public CheckRadioElementSet(String name)
	{
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param entries
	 */
	public CheckRadioElementSet(String name, Vector entries)
	{
		super(name, entries);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param values
	 */
	public CheckRadioElementSet(String name, String[] values)
	{
		super(name, values);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param values
	 * @param displayValues
	 */
	public CheckRadioElementSet(
		String name,
		String[] values,
		String[] displayValues)
	{
		super(name, values, displayValues);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param values
	 * @param displayValues
	 * @param selectedValues
	 */
	public CheckRadioElementSet(
		String name,
		String[] values,
		String[] displayValues,
		String[] selectedValues)
	{
		super(name, values, displayValues, selectedValues);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param entries
	 * @param selectedValues
	 */
	public CheckRadioElementSet(
		String name,
		Vector entries,
		String[] selectedValues)
	{
		super(name, entries, selectedValues);
		// TODO Auto-generated constructor stub
	}

	public String outputHTML ()
	{
		String output = null;
		StringBuffer stringBuf = new StringBuffer();
		Entry entry;
		
		for (int k=0; k<nEntries; k++)
		{	
			stringBuf.append (outputHTML (k) + " <BR>\n");
		}
		
		return (stringBuf.toString());
	}
		
	public String outputHTMLcheckRadio (Entry entry)
	{
		String output = null;
		StringBuffer stringBuf = new StringBuffer();
		
		stringBuf.append
		("<INPUT TYPE=\"" ); 
		
		if (multiple)
		{ stringBuf.append("CHECKBOX\""); }
		else
		{ stringBuf.append("RADIO\""); }
		
		stringBuf.append
				(" NAME=\"" + name + "\" VALUE=\"" + entry.getValue() + "\" ");
				
		if (entry.isSelected())
		{stringBuf.append ("CHECKED ");}
		
		stringBuf.append (">\n");
		
		return (stringBuf.toString());
	}
	
	public String outputHTMLcheckRadio (int kEntry)
	{
		Entry  entry = getEntry(kEntry);
		return (outputHTMLcheckRadio(entry));
	}
	
	public String outputHTMLdisplayText (Entry entry)
	{
		return (entry.getDisplayValue());
	}
	
	public String outputHTMLdisplayText (int kEntry)
	{
		Entry  entry = getEntry(kEntry);
		return (entry.getDisplayValue());
	}
	
	public String outputHTML (int kEntry)
	{
		Entry  entry = getEntry(kEntry);
		return (outputHTMLcheckRadio (entry) + "\n" +
					 outputHTMLdisplayText (entry) );
	}
}
