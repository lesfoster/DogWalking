/*
 * Created on Jan 2, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package main;

import html.CheckRadioElementSet;
import html.ComboListElement;
import html.SelectElement;
import html.TextFieldElement;
import html.TimeElement;

import java.awt.event.TextEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import sort.ContainsSortable;
import sun.java2d.pipe.TextPipe;
import time.CalCustom;
import time.Duration;
import time.TimeLength;
import util.StringProc;

/**
 * @author whitteng
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Dog
  implements ContainsSortable
{
    // Access Values:
    static public final int All             = 0;
    static public final int StaffOnly       = 1;
    static public final int None            = 2;
    static public final String[] allowedValues = 
    	{"No Restriction", "Staff Only", "No One"};
    
    static public Hashtable allowedIndexes = new Hashtable ();
    
    int outWalkID = -1;
    
    static
    {
        for (int k=0; k<3; k++)
        { allowedIndexes.put (allowedValues[k], new Integer(k)); }
    }
    
    /*
     * FROM Original Data Source:
     */
    public String id = "";
    
    public String name = "";
    
    public String breed = "";
    
    public String age = "";
    
    public String sex = "";
    
    public String SN = "";
     
    public Vector walks = new Vector ();

    public TimeLength totalWalkTime = null;
    
    /**
     * specialAttention - double value used to scale relative need <br>
     * default is 1.0
     */
    public double specialAttentionFactor = 1.0;
    
    public int access = 0;

    public String notes = "-";
    
    static final private String[] noneCBvals = {"None"};
    static final private String[] noneCBdisplays = {"NONE"};
    
    boolean out = false;
    
    /**
     * Used for temporary index of current dog location in list
     */
    public int kElTemp = -1;
    
    public Dog (ResultSet rs)
    {
    	try {
				id = rs.getString("ID");
				name = rs.getString("Name");
				notes = rs.getString("Notes");
				sex = rs.getString("Sex");
				SN = rs.getString("SpayNeuter");
				breed = rs.getString("Breed");
				age = rs.getString("Age");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public Dog (String id,
            	String name,
            	String specialAttentionFactor,
            	String access,
            	String notes)
    {
        this.id = id;
        this.name = name;
        this.specialAttentionFactor = 
            StringProc.stringToDouble(specialAttentionFactor);
        this.access = StringProc.stringToInt(access);
        this.notes = notes;
        
    }
    
    public Dog()
	{
		super();
	}

	public Walk out (int outWalkID)
    {
    	Walk lastWalk = null;
    	this.outWalkID = outWalkID;
    	
        out = true;
        lastWalk = new Walk(outWalkID);
        addLastWalk(lastWalk);

        return (lastWalk);
    }
    
    public Walk getLastWalk ()
    {
    	if ( (walks == null) || (walks.size() <= 0))
    	{ return ((Walk) null); }
    	
    	return ((Walk) walks.lastElement());
    }
    
    public Walk in ()
    {
    	Walk lastWalkLoc = (Walk) walks.lastElement();
    	
    	if ((!out) || (lastWalkLoc == null))
    	{ return (null); }
    	
        out = false;
        outWalkID = -1;
        lastWalkLoc.stopTiming(); 
        if (totalWalkTime == null)
        { totalWalkTime = new TimeLength (); }
        totalWalkTime.add(lastWalkLoc.getElapsedTime());
        
        return (lastWalkLoc);
    }
    
    public void addLastWalk (Walk walk)
    {
    	walks.addElement(walk);
    }
    
    public void addWalkPeriod (Duration walkPeriod, int outWalkID)
    {
    	TimeLength elapsedTime = walkPeriod.getElapsedTime();
    	if (elapsedTime == null)
    	{ // Dog out:
    		return;
    	}
    	
    	 if (totalWalkTime == null)
         { totalWalkTime = new TimeLength (); }
    	     
    	 totalWalkTime.add(elapsedTime);
    }
    
    public void addWalk (Walk walk)
    {
    	walks.addElement(walk);
    	
    	TimeLength elapsedTime = walk.getElapsedTime();
    	if (elapsedTime == null)
    	{ // Dog out:
    		out = true;
    		this.outWalkID = walk.walkID;

    		return;
    	}
    	
    	 if (totalWalkTime == null)
         { totalWalkTime = new TimeLength (); }
    	     
    	 totalWalkTime.add(elapsedTime);
    }
    
    static public String outputHTMLdogsTable (Dogs dogs)
    {
        Vector dogsV = dogs.getDogsV();
        
    	StringBuffer buf = new StringBuffer();
        
        buf.append ("<TABLE cellpadding=\"4\" Border=\"1\" WIDTH=\"100%\">\n");
        buf.append(outputHTMLlabelRow());
        
        Dog dog = null;
        
        
        int nDogs = dogsV.size();
        
        for (int k=0; k<nDogs; k++)
        {
            dog = (Dog) dogsV.elementAt(k);
            buf.append(dog.outputHTMLrow(k));
        }
        
        buf.append ("\n</TABLE>\n");
        
        return (buf.toString());
    }
    
    static public String outputHTMLlabelRow ()
    {
        return (outputHTMLlabelRow(true));
    }
    
    static public String outputHTMLlabelRow (boolean padColumns)
    {
        StringBuffer buf = new StringBuffer ();
        
        buf.append(beginRow());
       
        if (padColumns)
        {
            buf.append(beginCellRowSpan(2));
            buf.append("   ");
            buf.append(endCell());
        }
        
        buf.append(beginCellRowSpan(2));
        buf.append("Id");
        buf.append(endCell());
        
        buf.append(beginCellRowSpan(2));
        buf.append("Name");
        buf.append(endCell());

        buf.append(beginCellColSpan(3));
        buf.append("Walks");
        buf.append(endCell());
        
        buf.append(beginCellRowSpan(2));
        buf.append("Total Walk Time");
        buf.append(endCell());      
                
        buf.append(beginCellRowSpan(2));
        buf.append("Special <br>Attention<br> Factor");
        buf.append(endCell());
        
        buf.append(beginCellRowSpan(2));
        buf.append("Notes");
        buf.append(endCell());
        
        buf.append(beginCellRowSpan(2));
        buf.append("Breed");
        buf.append(endCell());
        
        buf.append(beginCellRowSpan(2));
        buf.append("Sex");
        buf.append(endCell());
        
        buf.append(beginCellRowSpan(2));
        buf.append("Age");
        buf.append(endCell());
        
        buf.append(beginCellRowSpan(2));
        buf.append("Spay/Neuter");
        buf.append(endCell());
        
        buf.append(beginCellRowSpan(2));
        buf.append("Who Has Access");
        buf.append(endCell());
        
        if (padColumns)
        {
            buf.append(beginCellRowSpan(2));
            buf.append(" ");
            buf.append(endCell());
        }
        
        buf.append(endRow());

        buf.append(beginRow());
        
        buf.append(beginCell());
        buf.append("#");
        buf.append(endCell());
        
        buf.append(beginCell());
        buf.append("Begin Time");
        buf.append(endCell());

        buf.append(beginCell());
        buf.append("End Time");
        buf.append(endCell());

        buf.append(endRow());
                
        return (buf.toString());
    }
    
    static public String outputEditHTMLlabelRow (boolean padColumns)
    {    	
        StringBuffer buf = new StringBuffer ();
        
        buf.append(beginRow());
       
        if (padColumns)
        {
            buf.append(beginCellRowSpan(2));
            buf.append("   ");
            buf.append(endCell());
        }
        
        buf.append(beginCellRowSpan(2));
        buf.append("Id");
        buf.append(endCell());
        
        buf.append(beginCellRowSpan(2));
        buf.append("Name");
        buf.append(endCell());

        buf.append(beginCellColSpan(3));
        buf.append("Walks");
        buf.append(endCell());
        
        buf.append(beginCellRowSpan(2));
        buf.append("Total Walk Time");
        buf.append(endCell());      
                
        buf.append(beginCellRowSpan(2));
        buf.append("Special <br>Attention<br> Factor");
        buf.append(endCell());
        
        buf.append(beginCellRowSpan(2));
        buf.append("Who Has Access");
        buf.append(endCell());
        
        buf.append(beginCellRowSpan(2));
        buf.append("Notes");
        buf.append(endCell());
        
        if (padColumns)
        {
            buf.append(beginCellRowSpan(2));
            buf.append(" ");
            buf.append(endCell());
        }
        
        buf.append(endRow());

        buf.append(beginRow());
        
        buf.append(beginCell());
        buf.append("#");
        buf.append(endCell());
        
        buf.append(beginCell());
        buf.append("Begin Time");
        buf.append(endCell());

        buf.append(beginCell());
        buf.append("End Time");
        buf.append(endCell());

        buf.append(endRow());
                
        return (buf.toString());
    }


    public String outputHTMLrow (int k)
    {
        StringBuffer buf = new StringBuffer ();
        
        int nWalks = walks.size();
        int nRows = Math.max(1, nWalks);
        
        buf.append(beginRow());
        
        buf.append(beginCellProcRowSpan(nRows));
        buf.append
        	("<INPUT TYPE=\"SUBMIT\" NAME=\"OUT:" + id + "\" VALUE=\"OUT\">");
        
        buf.append ("<P> ");
       
        buf.append
        	("<INPUT TYPE=\"SUBMIT\" NAME=\"IN:" + id + "\" VALUE=\"IN\">");
        
        buf.append(endCell());
        
        buf.append(beginCellProcRowSpan(nRows));
        buf.append(id);
        buf.append(endCell());
        
        buf.append(beginCellProcRowSpan(nRows));
        
        buf.append
        	("<a href =\"/Dogs/Action?OP=EditDogForm&El=" +  id + "\"> " 
             +  name + " </a>");

        buf.append(endCell());
     
        if (nWalks <= 0)
        {
        	 buf.append(beginCellProcColSpan(3));
        	 buf.append("NONE");
        }
        else
        {
        	outputWalk(buf, 0);
        }
        buf.append(endCell());
                
        buf.append(beginCellProcRowSpan(nRows));
        if ( (nWalks > 0) &&
        	 (totalWalkTime != null) )
        {
            buf.append(totalWalkTime.outputTimeLength());
        }
        else
        {
            buf.append("NONE");
        }
        buf.append(endCell());
        
        buf.append(beginCellProcRowSpan(nRows));
        buf.append(specialAttentionFactor);
        buf.append(endCell());
        
        buf.append(beginCellProcRowSpanNoFormat(nRows));
        buf.append(notes);
        buf.append(endCellNoFormat());
        
        buf.append(beginCellProcRowSpan(nRows));
        buf.append(breed);
        buf.append(endCell());
        
        buf.append(beginCellProcRowSpan(nRows));
        buf.append(sex);
        buf.append(endCell());
        
        buf.append(beginCellProcRowSpan(nRows));
        buf.append(age);
        buf.append(endCell());
        
        buf.append(beginCellProcRowSpan(nRows));
        buf.append(SN);
        buf.append(endCell());
        
        buf.append(beginCellProcRowSpan(nRows));
        buf.append(allowedValues[access]);
        buf.append(endCell());
        
        buf.append(endRow());
        
        outputWalks (buf, 1);
        
        return (buf.toString());
    }
    
    void outputWalks (StringBuffer buf, int kStart)
    {
        int nWalks = walks.size();
        Walk walk = null;
         
        for (int k=kStart; k<nWalks; k++)
        {
        	walk = (Walk) walks.elementAt(k);
 
        	buf.append(beginRow());
        
        	outputWalk (buf, k);
        	
            buf.append(endRow());
        }
    }

    void outputWalk (StringBuffer buf, int kWalk)
    {
        Walk walk = null;
         
        walk = (Walk) walks.elementAt(kWalk);
 
        // buf.append(beginRow());
        
        buf.append (beginCellProc());
        buf.append (String.valueOf(kWalk + 1));
        buf.append(endCell());
        
        buf.append(beginCellProc());
        buf.append(walk.outputBeginTime());
        buf.append(endCell());
            
        buf.append(beginCellProc());
        buf.append(walk.outputEndTime());
        buf.append(endCell());
            
        // buf.append(endRow());
    }
    
    void outputWalkEdit (StringBuffer buf, int kWalk)
    {
    	String[] deleteVals =  {"Delete"};
    	String[] deleteDisplays =  {"DELETE"};
    	String[] addVals =  {"Add"};
    	String[] addDisplays =  {"ADD"};
    	
    	int nWalks = walks.size();
    	
    	if (kWalk < nWalks)
    	{ // existing walk:    		
    		CheckRadioElementSet walkDeleteCheckBox = 
    			new CheckRadioElementSet
    				("walkDeleteCB:" + Integer.toString(kWalk) ,
    				 deleteVals,
    				 deleteDisplays);
        
    		Calendar timeBegin = null;
    		Calendar timeEnd = null;
    
    		Walk walk = (Walk) walks.elementAt(kWalk);
    
    		buf.append(beginCell());
    		
    		buf.append("<TABLE><TR><TD><CENTER>\n");
    
    		buf.append(walkDeleteCheckBox.outputHTML());
    
    		buf.append("</CENTER></TD></TR><TR><TD><CENTER>");
        	
    		buf.append(Integer.toString(kWalk + 1));
    
    		buf.append("</CENTER></TD></TR></TABLE>");       
    		buf.append(endCell());
        	
    		buf.append(beginCell());
    
    		buf.append("<TABLE><TR><TD><CENTER>\n");
    
    		buf.append("<BR>\n");
    
    		buf.append("</CENTER></TD></TR><TR><TD><CENTER>");
    	
    		timeBegin = walk.getBeginTime();
    	
    		if (timeBegin != null)
    		{
    			TimeElement beginWalkEl = 
    				new TimeElement ("walkBegin:" + Integer.toString(kWalk), timeBegin); 
        	
    			buf.append(beginWalkEl.outputHTML());
    		}
    		else
    		{	buf.append("NONE"); }
    
    		buf.append("</CENTER></TD></TR></TABLE>");       
      
    		buf.append(endCell());
    
    		buf.append(beginCell());
    		
    		buf.append("<TABLE><TR><TD><CENTER>\n");
    		
    		timeEnd = walk.getEndTime();
    	
    		CheckRadioElementSet endTimeCheckBox = 
    			new CheckRadioElementSet
        			("endTimeNoneCB:" + Integer.toString(kWalk),
        			 noneCBvals,
        			 noneCBdisplays);
    			
    		if (timeEnd == null)
    	    { 
    			endTimeCheckBox.setSelectedEntry("None");
    			timeEnd = new CalCustom (); 
    		}
    	
    		buf.append(endTimeCheckBox.outputHTML());
    		
    		buf.append("</CENTER></TD></TR><TR><TD><CENTER>");
    
    		if (timeEnd != null)
    		{
    			TimeElement endWalkEl = 
    				new TimeElement ("walkEnd:" + Integer.toString(kWalk), timeEnd); 
        	
    			buf.append(endWalkEl.outputHTML());
    		}
    		else
    		{	buf.append("NONE"); }
    	
    		buf.append("</CENTER></TD></TR></TABLE>"); 

    		buf.append(endCell());
    	}
    	else
    	{ // Add Walk:
    		CheckRadioElementSet addWalkCheckBox = 
    			new CheckRadioElementSet
    				("addWalkOutCB:ADD",
    				 addVals,
    				 addDisplays);
        
    		Calendar timeBegin = new GregorianCalendar();
    		Calendar timeEnd = new GregorianCalendar();;
        
    		buf.append(beginCell());
    		
    		buf.append("<TABLE><TR><TD><CENTER>\n");
    
    		buf.append(addWalkCheckBox.outputHTML());
    
    		buf.append("</CENTER></TD></TR><TR><TD>");
        	
    		buf.append("<BR>\n");
    
    		buf.append("</TD></TR></TABLE>");       
    		buf.append(endCell());
        	
    		buf.append(beginCell());
    
    		buf.append("<TABLE><TR><TD><CENTER>\n");
    
    		buf.append("<BR>\n");
    
    		buf.append("</CENTER></TD></TR><TR><TD><CENTER>");
    	    	
    		if (timeBegin != null)
    		{
    			TimeElement beginWalkEl = 
    				new TimeElement ("walkBegin:ADD", timeBegin); 
        	
    			buf.append(beginWalkEl.outputHTML());
    		}
    		else
    		{	buf.append("NONE"); }
    
    		buf.append("</CENTER></TD></TR></TABLE>");       
      
    		buf.append(endCell());
    
    		buf.append(beginCell());
    		
    		buf.append("<TABLE><TR><TD><CENTER>\n");
    	
    		if (timeEnd != null)
    		{
    			CheckRadioElementSet endTimeCheckBox = 
    				new CheckRadioElementSet
        				("endTimeNoneCB:ADD",
        				 noneCBvals,
        				 noneCBdisplays);
    	
    			buf.append(endTimeCheckBox.outputHTML());
    		}
    		else
    		{
    			buf.append("<BR>\n ");
    		}
    		buf.append("</CENTER></TD></TR><TR><TD><CENTER>");
    
    		if (timeEnd != null)
    		{
    			TimeElement endWalkEl = 
    				new TimeElement ("walkEnd:ADD", timeEnd); 
        	
    			buf.append(endWalkEl.outputHTML());
    		}
    		else
    		{	buf.append("NONE"); }
    	
    		buf.append("</CENTER></TD></TR></TABLE>"); 

    		buf.append(endCell());
    	}
    }

    public String outputEditHTML ()
	{
	    StringBuffer buf = new StringBuffer ();
	    
	    int nWalks = walks.size();
        //int nRows = Math.max(1, nWalks);
	    int nRows = nWalks + 1;
	
	    buf.append ("<TABLE cellpadding=\"4\" Border=\"1\">\n");
	    
	    buf.append(outputEditHTMLlabelRow(false));
	    
	    buf.append(beginRow());
	    	    
	    buf.append(beginCellProcRowSpan(nRows));
	    
	   // buf.append (TextFieldElement.outputHTML ("id", id, 5));
	    buf.append (id);
	    
	    buf.append(endCell());
	    
	    buf.append(beginCellProcRowSpan(nRows));
	    
	    // buf.append (TextFieldElement.outputHTML ("name", name, 10));
	    buf.append (name);
	    
	    buf.append(endCell());
    
	    int kWalk = 0;
	    
	    outputWalkEdit(buf, 0);
	    
	    kWalk++;
	    
	    buf.append((beginCellProcRowSpan(nRows)));
	    	    	    
	    buf.append("<TABLE><TR><TD><CENTER>\n");
	    
	    if (totalWalkTime == null)
        { buf.append ("NONE"); }
	    else
	    { buf.append(totalWalkTime.outputTimeLength()); }
	    
	    buf.append("</CENTER></TD></TR></TABLE>"); 
	
	    buf.append(endCell());
	    
	    buf.append(beginCellProcRowSpan(nRows));
	    buf.append(TextFieldElement.outputHTML
	            ("specialAttentionFactor",
	              String.valueOf(specialAttentionFactor),
	              5));
	    buf.append(endCell());
	
	    buf.append(beginCellProcRowSpan(nRows));
	    ComboListElement allowedValuesComboEl = new ComboListElement
	    	("Allowed", allowedValues);
	    allowedValuesComboEl.setMultiple(false);
	    allowedValuesComboEl.setSelectedEntry(allowedValues[access]);
	    buf.append(allowedValuesComboEl.outputHTML());
	    buf.append(endCell());
	    
	    buf.append((beginCellProcRowSpan(nRows)));
	    buf.append
	    	("<TEXTAREA NAME=\"Notes\" ROWS=\"20\" COLS=\"60\">" +
	    	        notes + "</TEXTAREA>\n");
	    buf.append(endCell());
	    
	    buf.append(endRow());
	    
	    for ( ; kWalk<nRows; kWalk++)
	    {
	    	buf.append(beginRow());
	    
	    	outputWalkEdit(buf, kWalk);
	    	
	    	buf.append(endRow());
	    }
	    
	    buf.append ("\n</TABLE>\n");
	    
	    return (buf.toString());
	}

	public String outputEditHTMLold ()
    {
        StringBuffer buf = new StringBuffer ();
     
        buf.append ("<TABLE cellpadding=\"4\" Border=\"1\">\n");
        
        buf.append(outputHTMLlabelRow(false));
        
        buf.append(beginRow());
        
        buf.append(beginCell());
        buf.append
        	(TextFieldElement.outputHTML
        	  ("id", id, 5));
        buf.append(endCell());
        
        buf.append(beginCell());
        buf.append
    	(TextFieldElement.outputHTML
    	  ("name", name, 10));
        buf.append(endCell());
    
        buf.append(beginCell());
        
        String[] deleteVals =  {"Delete"};
        String[] deleteDisplays =  {"DELETE"};
            
        CheckRadioElementSet noneLastWalkOutCheckBox = 
            new CheckRadioElementSet
             ("noneLastWalkOutCB", deleteVals, deleteDisplays);
            
        Calendar lastOutTime = null;
        
        Walk lastWalk = getLastWalk();
        
        if (lastWalk != null)
        { lastOutTime = lastWalk.getBeginTime(); }
        
        if (lastOutTime == null)
        { noneLastWalkOutCheckBox.setSelectedEntry(noneCBvals[0]); }
            
        TimeElement lastWalkOutEl = 
            new TimeElement ("LastWalkOut", lastOutTime); 
        
        buf.append("<TABLE><TR><TD><CENTER>\n");
        
        buf.append(noneLastWalkOutCheckBox.outputHTML());
        
        buf.append("</CENTER></TD></TR><TR><TD>");
            	
        buf.append(lastWalkOutEl.outputHTML());
        
        buf.append("</TD></TR></TABLE>");       
        buf.append(endCell());
        
        buf.append(beginCell());
        
        CheckRadioElementSet noneLastWalkInCheckBox = 
            new CheckRadioElementSet
             ("noneLastWalkInCB", noneCBvals, noneCBdisplays);
            
        Calendar lastInTime = null;
            
        if (lastWalk != null)
        { lastInTime = lastWalk.getEndTime(); }
        
        if (lastInTime == null)
        { noneLastWalkInCheckBox.setSelectedEntry(noneCBvals[0]); }
            
        TimeElement lastWalkInEl = 
            new TimeElement ("LastWalkIn", lastInTime); 
        
        buf.append("<TABLE><TR><TD><CENTER>\n");
        
        buf.append(noneLastWalkInCheckBox.outputHTML());
        
        buf.append("</CENTER></TD></TR><TR><TD>");
            	
        buf.append(lastWalkInEl.outputHTML());
        
        buf.append("</TD></TR></TABLE>");       
          
        buf.append(endCell());
        
        buf.append(beginCell());
        
        CheckRadioElementSet noneTotalTimeCheckBox = 
            new CheckRadioElementSet
             ("noneTotalTimeCB", noneCBvals, noneCBdisplays);
        
        TimeElement totalWalkTimeEl = 
    	    new TimeElement 
        		("TotalWalkTime", totalWalkTime);
        
        if (totalWalkTime == null)
        { noneTotalTimeCheckBox.setSelectedEntry(noneCBvals[0]); }
        
        buf.append("<TABLE><TR><TD><CENTER>\n");
        
        buf.append(noneTotalTimeCheckBox.outputHTML());
        
        buf.append("</CENTER></TD></TR><TR><TD>");
        
        buf.append(totalWalkTimeEl.outputHTML());
        
        buf.append("</TD></TR></TABLE>"); 

        buf.append(endCell());
        
        buf.append(beginCell());
        buf.append(TextFieldElement.outputHTML
                ("specialAttentionFactor",
                  String.valueOf(specialAttentionFactor),
                  5));
        buf.append(endCell());
    
        buf.append(beginCell());
        ComboListElement allowedValuesComboEl = new ComboListElement
        	("Allowed", allowedValues);
        allowedValuesComboEl.setMultiple(false);
        allowedValuesComboEl.setSelectedEntry(allowedValues[access]);
        buf.append(allowedValuesComboEl.outputHTML());
        buf.append(endCell());
        
        buf.append(beginCell());
        buf.append
        	("<TEXTAREA NAME=\"Notes\" ROWS=\"4\" COLS=\"20\">" +
        	        notes + "</TEXTAREA>\n");
        buf.append(endCell());
        
        buf.append(endRow());
        
        buf.append ("\n</TABLE>\n");
        
        
        return (buf.toString());
    }
    
    static private String beginRow ()
    {
      return ("<TR>\n");  
    }
    
    static private String beginCell ()
    {
      return ("  <TD ALIGN=\"CENTER\">\n");
    }
    
    static private String beginCell (int rowSpan, int colSpan)
    {
      return ("  <TD ALIGN=\"CENTER\" ROWSPAN=\"" + String.valueOf(rowSpan) +
    		  				    "\" + COLSPAN=\"" + String.valueOf(colSpan) + "\" >\n");
    }
    
    static private String beginCellRowSpan (int rowSpan)
    {
      return ("  <TD ALIGN=\"CENTER\" ROWSPAN=\"" + String.valueOf(rowSpan) + "\" >\n");
    }
    
    static private String beginCellColSpan (int colSpan)
    {
      return ("  <TD ALIGN=\"CENTER\" + COLSPAN=\"" + String.valueOf(colSpan) + "\" >\n");
    }

    private String beginCellProc ()
    {
      if (out)
      {
          return ("  <TD ALIGN=\"CENTER\" BGCOLOR= \"#FFAAAA\">\n");
      }
      else
      {
          return ("  <TD ALIGN=\"CENTER\">\n");
      }
    }
    
    private String beginCellProcColSpan (int colSpan)
    {
      if (out)
      {
          return ("  <TD ALIGN=\"CENTER\" BGCOLOR= \"#FFAAAA\" COLSPAN=\"" 
        		  + String.valueOf(colSpan) + "\">\n");
      }
      else
      {
          return ("  <TD ALIGN=\"CENTER\" COLSPAN=\"" 
        		  + String.valueOf(colSpan) + "\">\n");
      }
    }
    
    private String beginCellProcRowSpan (int rowSpan)
    {
      if (out)
      {
          return ("  <TD ALIGN=\"CENTER\" BGCOLOR= \"#FFAAAA\" ROWSPAN=\"" 
        		  + String.valueOf(rowSpan) + "\">\n");
      }
      else
      {
          return ("  <TD ALIGN=\"CENTER\" ROWSPAN=\"" 
        		  + String.valueOf(rowSpan) + "\">\n");
      }
    }
    
    private String beginCellProcRowSpanNoFormat (int rowSpan)
    {
      if (out)
      {
          return ("  <TD WIDTH=\"100px\" HEIGHT=\"80px\" ALIGN=\"LEFT\" BGCOLOR= \"#FFAAAA\" ROWSPAN=\"" 
        		  + String.valueOf(rowSpan) + "\"> <PRE>\n");
      }
      else
      {
          return ("  <TD WIDTH=\"100px\" HEIGHT=\"80px\" ALIGN=\"LEFT\" ROWSPAN=\"" 
        		  + String.valueOf(rowSpan) + "\"> <PRE>\n");
      }
    }
    
    private String beginCellProc (int rowSpan, int colSpan)
    {
      if (out)
      {
          return ("  <TD ALIGN=\"CENTER\" BGCOLOR=\"#FFAAAA\"  ROWSPAN=\"" + String.valueOf(rowSpan) +
    		  				    "\" COLSPAN=\"" + String.valueOf(colSpan) + "\" >\n");
      }
      else
      {
          return ("  <TD ALIGN=\"CENTER\" ROWSPAN=\"" + String.valueOf(rowSpan) +
    		  				    "\" COLSPAN=\"" + String.valueOf(colSpan) + "\" >\n");
      }
    }
    
    static private String endCell ()
    {
      return ("  </TD>\n");  
    }
    
    static private String endCellNoFormat ()
    {
      return ("  </PRE></TD>\n");  
    }
    
    static private String endRow ()
    {
      return ("</TR>\n");  
    }
    
    public Comparable getSortableElement ()
    {
        long totalWalkTimeMillis = 1000;
        long totalWalkTimeAdjustedMillis = 0;
        
        if (totalWalkTime != null)
        {
            totalWalkTimeMillis = 
                totalWalkTime.getTotalMilliseconds();
        }
        
        totalWalkTimeAdjustedMillis = 
           (long) (totalWalkTimeMillis / specialAttentionFactor);
      
        return (new Long (totalWalkTimeAdjustedMillis));
    }
    
    public Vector resetWalks ()
    {
    	walks = new Vector();
    	
    	totalWalkTime = new TimeLength();
    	
    	return (walks);
    }
 
    /**
     * @return Returns the all.
     */
    public static int getAll()
    {
        return All;
    }
  
    /**
     * @return Returns the none.
     */
    public static int getNone()
    {
        return None;
    }
    
    /**
     * @return Returns the staffOnly.
     */
    public static int getStaffOnly()
    {
        return StaffOnly;
    }

    /**
     * @return Returns the id.
     */
    public String getId()
    {
        return id;
    }
    /**
     * @param id The id to set.
     */
    public void setId(String id)
    {
        this.id = id;
    }
    
    /**
	 * @return the breed
	 */
	public synchronized String getBreed()
	{
		return breed;
	}

	/**
	 * @param breed the breed to set
	 */
	public synchronized void setBreed(String breed)
	{
		this.breed = breed;
	}

	/**
	 * @return the age
	 */
	public synchronized String getAge()
	{
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public synchronized void setAge(String age)
	{
		this.age = age;
	}

	/**
	 * @return the sex
	 */
	public synchronized String getSex()
	{
		return sex;
	}

	/**
	 * @param sex the sex to set
	 */
	public synchronized void setSex(String sex)
	{
		this.sex = sex;
	}

	/**
	 * @return the sN
	 */
	public synchronized String getSN()
	{
		return SN;
	}

	/**
	 * @param sn the sN to set
	 */
	public synchronized void setSN(String sn)
	{
		SN = sn;
	}

	/**
     * @return Returns the name.
     */
    public String getName()
    {
        return name;
    }
    /**
     * @param name The name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }
    /**
     * @return Returns the notes.
     */
    public String getNotes()
    {
        return notes;
    }
    /**
     * @param notes The notes to set.
     */
    public void setNotes(String notes)
    {
        this.notes = notes;
    }
  
    /**
     * @return Returns the specialAttention.
     */
    public double getSpecialAttentionFactor()
    {
        return specialAttentionFactor;
    }
    
    /**
     * @return Returns the specialAttention.
     */
    public String getSpecialAttentionFactorString()
    {
        return (Double.toString (specialAttentionFactor));
    }
    
    /**
     * @param specialAttention The specialAttention to set.
     */
    public void setSpecialAttentionFactor 
    	(double specialAttentionFactor)
    {
        this.specialAttentionFactor = specialAttentionFactor;
    }
    /**
     * @return Returns the totalWalkTime.
     */
    public TimeLength getTotalWalkTime()
    {
        return totalWalkTime;
    }
    /**
     * @param totalWalkTime The totalWalkTime to set.
     */
    public void setTotalWalkTime(TimeLength totalWalkTime)
    {
        this.totalWalkTime = totalWalkTime;
    }
    /**
     * @return Returns the access.
     */
    public int getAccess()
    {
        return access;
    }
    /**
     * @param access The access to set.
     */
    public void setAccess(int access)
    {
        this.access = access;
    }
    /**
     * @return Returns the kElTemp.
     */
    public int getKElTemp()
    {
        return kElTemp;
    }
    
    /**
     * @return Returns the kElTemp.
     */
    public String getKElTempString()
    {
        return String.valueOf(kElTemp);
    }
    
    /**
     * @param elTemp The kElTemp to set.
     */
    public void setKElTemp(int elTemp)
    {
        kElTemp = elTemp;
    }
    
    static public int getAllowedIndex (String allowedString)
    {
        Integer indexObj = 
            (Integer) allowedIndexes.get (allowedString);
        
        return (indexObj.intValue());
    }
    /**
     * @return Returns the out.
     */
    public boolean isOut()
    {
        return out;
    }
    /**
     * @param out The out to set.
     */
    public void setOut(boolean out)
    {
        this.out = out;
    }

	public int getOutWalkID() {
		return outWalkID;
	}

	public void setOutWalkID(int outWalkID) {
		this.outWalkID = outWalkID;
	}

	public Vector getWalks()
	{
		return walks;
	}

	public void setWalks(Vector walks)
	{
		this.walks = walks;
	}
}