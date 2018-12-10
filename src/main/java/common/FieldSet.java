package common;

import java.util.*;

public class FieldSet 
{
	Hashtable fieldSetHT;
	
	Vector fieldSetVec = null;
	
	int nFields = 0;
	int order = -1;
	
	public FieldSet (Hashtable fieldSetHT)
	{
		this.fieldSetHT = fieldSetHT;
		setupFieldSet();
	}
	
	public Vector setupFieldSet ()
	{
		java.util.Enumeration en = fieldSetHT.elements();
		
		Hashtable bufHT = null;
		
		Field field;
		
		fieldSetVec = new Vector();
		
		while ( en.hasMoreElements() ) 
		{ 
    		field = (Field) en.nextElement(); 
  			order = field.getOrder();
  			
  			if (order >= 0)
  			{
  				//fieldSetVec.insertElementAt(field, order);
  				if (bufHT == null)
  				{ bufHT = new Hashtable(); }
  				
  				bufHT.put(new Integer (order), field);
  			}
  			else
  			{
  				fieldSetVec.addElement(field);
  			}
  			nFields++;
		}
		
		if (bufHT != null)
		{
			Integer intObj;
			
			for (int k=0, kVec=0; k<nFields; k++)
			{
				intObj = new Integer(k);
				field = (Field) bufHT.get (intObj);
				
				if (field != null)
				{ fieldSetVec.insertElementAt(field, k); }
			}
		}
		
		return (fieldSetVec);
	}
	
	public Object getName (int k)
	{
		if (k >= nFields)
		{ return (null); }
		
		Field field = (Field) fieldSetVec.elementAt (k);
		return (field.getName());
	} 
	
	public Object getValue(int k)
	{
		Field field = (Field) fieldSetVec.elementAt (k);
		return (field.getValue());
	} 
	
	public Field getField(int k)
	{
		Field field = (Field) fieldSetVec.elementAt (k);
		return (field);
	} 
	
	
	/**
	 * Gets the fieldSetHT
	 * @return Returns a Hashtable
	 */
	public Hashtable getFieldSetHT() {
		return fieldSetHT;
	}
	/**
	 * Sets the fieldSetHT
	 * @param fieldSetHT The fieldSetHT to set
	 */
	public void setFieldSetHT(Hashtable fieldSetHT) {
		this.fieldSetHT = fieldSetHT;
	}

	/**
	 * Gets the fieldSetVec
	 * @return Returns a Vector
	 */
	public Vector getFieldSetVec() {
		return fieldSetVec;
	}
	/**
	 * Sets the fieldSetVec
	 * @param fieldSetVec The fieldSetVec to set
	 */
	public void setFieldSetVec(Vector fieldSetVec) {
		this.fieldSetVec = fieldSetVec;
	}

	/**
	 * Gets the fields
	 * @return Returns a int
	 */
	public int getnFields() {
		return nFields;
	}
}

