/*
 * Created on Jul 20, 2004
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
 * 
 *  *  HTML Combo (single selection) or List (multiple selection), depending on "multiple" value
 *    Example:
 *        <SELECT NAME="language" MULTIPLE> 
 *			<OPTION VALUE="c">C 
 *			<OPTION VALUE="c++">C++ 
 *			<OPTION VALUE="java" SELECTED>Java 
 *			<OPTION VALUE="lisp">Lisp 
 *			<OPTION VALUE="perl" SELECTED>Perl 
 *			<OPTION VALUE="smalltalk">Smalltalk 
 *		 </SELECT>
 * 
 */
public class ComboListElement extends SelectElement
{
	
	/**
	 * 
	 */
	public ComboListElement()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public ComboListElement(String name)
	{
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param entries
	 */
	public ComboListElement(String name, Vector entries)
	{
		super(name, entries);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param values
	 */
	public ComboListElement(String name, String[] values)
	{
		super(name, values);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 * @param values
	 * @param displayValues
	 */
	public ComboListElement(
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
	public ComboListElement(
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
	public ComboListElement(
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
			Entry entry = null;
		
			stringBuf.append("<SELECT NAME = \"" + name + "\"");
			if (multiple)
			{ stringBuf.append(" MULTIPLE"); }
			stringBuf.append(" >\n");
		
			for (int k=0; k<nEntries; k++)
			{ 
				entry = getEntry(k);
				stringBuf.append ("  <OPTION VALUE = \"" + entry.getValue() + "\"");
				if (entry.isSelected())
				{ stringBuf.append(" SELECTED"); }
				stringBuf.append(" >" + entry.getValue() + "\n");
			}
		
			stringBuf.append("</SELECT>\n");
			
			return (stringBuf.toString());
		}
}
