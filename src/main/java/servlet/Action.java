/*
 * Created on Jan 3, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package servlet;

import db.DBAccess;
import db.ExecuteResults;
import db.InsertConfig;
import html.TimeElement;
import main.Dog;
import main.Dogs;
import main.DogsDBproc;
import main.Walk;
import sort.Sort;
import time.CalCustom;
import time.TimeLength;
import util.RefreshDogsDBandLocalThread;
import util.StringProc;
import util.Util;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.*;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author whitteng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Action extends ServletCore
{
    public static final String TARGET_JSP = "jsp/Dogs.jsp";
    static public Dogs dogs = null;
    
    boolean synchDataFlag = false;
    
	static DBAccess dbAccessDogs = null;
	static DBAccess dbAccessArk = null;
	String dbDogsPropsFile = "dbDogsProps.txt";
	String dbArkPropsFile = "dbArkProps.txt";
	static int walksIDlast = -1;
	
	static DogsDBproc dogsDBproc = 
		new DogsDBproc();

	boolean EnableArk = false;
	boolean ArkAvailable = false;
	
	public String webRoot = null;
	
    public void init(ServletConfig conf) throws ServletException
    {
        super.init (conf);

	    webRoot = conf.getServletContext().getRealPath("/");

	    Util.setWebRoot(webRoot);

	    	dbAccessDogs = new DBAccess(dbDogsPropsFile);
	    	
	    	dogsDBproc.setDbAccessDogs(dbAccessDogs);
	    	dogsDBproc.setParent(this);
	    	
	    	// TEST FOR DogsMain Table:
	    	Connection conDogs = dbAccessDogs.getCon();
	    try
	    {
	    	try 
	    	{

	    		DatabaseMetaData dbm = conDogs.getMetaData();
	    		ResultSet tablesRS = dbm.getTables(null, null, "DogsMain", null);
	    		if (!(tablesRS.next())) 
	    		{ // DogsMain does NOT exist
	    			dogsDBproc.createDogsMainTable(conDogs);
	    		}
	    		tablesRS = dbm.getTables(null, null, "walks", null);
	    		if (!(tablesRS.next())) 
	    		{ // DogsMain does NOT exist
	    			dogsDBproc.createWalksTable(conDogs);
	    		}

	    	}
	    	catch (SQLException sqlE)
	    	{
	    		//if (sqlE.getErrorCode() == 1146)
	    		sqlE.printStackTrace();
	    	}
	    	
	    	if (EnableArk)
	    	{
	    		dbAccessArk = new DBAccess (dbArkPropsFile);
	    		ArkAvailable = dbAccessArk.isConnectionsMade();
	    		if (dbAccessArk.isConnectionsMade())
	    		{ // ONLY WHEN FoxPro ARK is available:
	    			dogsDBproc.setDbAccessArk(dbAccessArk);	    	
	    			dogsDBproc.applyArkDBtoDogsDB();
	    		}
	    	}
	    	else
	    	{
	    		try
				{
					dogsDBproc.applyOrigWHSdataToDogsDB();
				} catch (Exception e)
				{
					e.printStackTrace();
				}
	    	}
	    	
	    	dogs = dogsDBproc.updateDogsLocalWithDogsDB();
	    	
	    	/************************/

	    try
	      {
	    	// get  "walks" primary key value:
	    	ExecuteResults execResults = dbAccessDogs.queryData 
	    		("SELECT id from walks WHERE id = (SELECT MAX(id) from walks)");
	    		//("SELECT id from walks WHERE id = MAX(id)");

	    	ResultSet idResults = execResults.getRs();
	    	
	    	boolean rsValid = idResults.next();

	    	if (rsValid)
	    	{ // Make sure there are at least some walks already:
	    		walksIDlast = idResults.getInt("id"); 
	    	}
	    	else
	    	{ // No walks yet
	    		walksIDlast = 1; 
	    	}
	    	
	    	execResults.cleanUp();
	      }
	      catch (Exception e)
	      {
	    	  e.printStackTrace();
	    	  walksIDlast = 0;
	      }
	    }
	    catch (Exception e)
	    {
	    	e.printStackTrace();
	    	//System.out.println("XXX Could NOT make DB connections XXX");
	    }
	    
        dogs.sort();
        
        dbAccessDogs.returnCon(conDogs);
        
        RefreshDogsDBandLocalThread refreshDogsDBandLocalThread =
        		new RefreshDogsDBandLocalThread (dogsDBproc);
        
        Thread refreshDogsDBandLocalThreadWrapper =
        		new Thread (refreshDogsDBandLocalThread);
        
        refreshDogsDBandLocalThreadWrapper.start();
    }
    
    /**
	 * @return the arkAvailable
	 */
	public synchronized boolean isArkAvailable()
	{
		return (ArkAvailable && EnableArk);
	}

	public Object FormProc
	   (ProcessHTTPinput processInput,
		HttpServletRequest req,
		HttpServletResponse res) throws Exception
	{	
    	Enumeration enumParams = req.getParameterNames();
    	boolean contOut = true;
    	boolean contIn = true;
    	String selectedElement = null;
    	Walk walkTime = null;
    	InsertConfig insertConfig = null;
    
      while ( enumParams.hasMoreElements() && contOut && contIn) 
      { 
        String paramName = (String) enumParams.nextElement();
        
        if (StringProc.contains(paramName, "OUT:"))
        { // found it
            contOut = false;
            
            selectedElement = 
                StringProc.getSubstringFromEnd(paramName, ':');
            
            Dog dog = (Dog) dogs.getDog(selectedElement);
            
            if (dog.isOut())
            {
            	transferToJSP(TARGET_JSP, req, res);
            	return (null);
            }
            
            walksIDlast++;
            
            walkTime = dog.out(walksIDlast);
            
            Hashtable insertVals = new Hashtable ();
            
            String[] idVals = {dog.getId()};
            
            insertVals.put
            	("dogsmainID", idVals);
            
            String[] walksIDlastVals = {Integer.toString (walksIDlast)};
            
            insertVals.put
        		("id", walksIDlastVals);

            String[] beginVals = {walkTime.outputBeginDateString()};
            
            insertVals.put 
            	("walkStartTime", beginVals);
            
            insertConfig = new InsertConfig("walks", insertVals);
            
            dbAccessDogs.insertData (insertConfig);
        }
        
        if (StringProc.contains(paramName, "IN:"))
        { // found it
            contIn = false;
            selectedElement = 
                StringProc.getSubstringFromEnd(paramName, ':');
            
            Dog dog = (Dog) dogs.getDog(selectedElement);
            
            if (!dog.isOut())
            {  
            	transferToJSP(TARGET_JSP, req, res);
            	return (null);
            }
            
            int outWalkID = dog.getOutWalkID();
            
            walkTime = dog.in();
            
            if (walkTime == null)
            { break; }
            
            /* INSERT - no longer used, now: UPDATE:
            Hashtable insertVals = new Hashtable ();
            
            String[] idVals = {dog.getId()};
            
            insertVals.put
            	("dogsmainID", idVals);

            String[] endVals = {walkTime.outputEndDateString()};
            insertVals.put 
        	("walkEndTime", endVals);
            
            insertConfig = new InsertConfig("walks", insertVals);
            
            dbAccessDogs.insertData (insertConfig);
            
            INSERT - no longer used, now: UPDATE: */
            
            Connection conDogs = dbAccessDogs.getCon();
            
            updateWalkInDB (walkTime, dog, conDogs);
          
            dbAccessDogs.returnCon(conDogs);
          
            dogs.sort();
        }
    }
      
    transferToJSP(TARGET_JSP, req, res);
    
    //updateDogsDBwithArkDB.applyArkDBtoDogsDB();
    
     return (null);
	}
    
    void updateWalkInDB (Walk walk, Dog dog, Connection conDogs)
    {	
    	String setWalkEndString = "";
    	String setWalkStartString = "";
    		    		
    	String walkStartString = walk.outputBeginDateString();
    		
    	String delim = "";
    	
    	String sqlString = null;
    		
    	try
        {	   	
    		Statement updateStatement =
    			conDogs.createStatement();
    		
    		if (walkStartString != null)
    		{
    			setWalkStartString = 
    				("SET walkStartTime = \"" + walkStartString + "\" ");
    			
    			delim = ", ";
    		}
    		
    		String walkEndString = walk.outputEndDateString();
    		
    		if (walkEndString != null)
    		{
    			setWalkEndString = 
    				(" walkEndTime = \"" + walkEndString + "\" ");
    		}
    		else
    		{
    			delim = " ";
    		}
    		

    		sqlString = 
    			("UPDATE walks " + setWalkStartString + delim + setWalkEndString + 
    					" WHERE ID = \"" + Integer.toString (walk.getWalkID()) 
    					+ "\";");
  	
    		int nRows = updateStatement.executeUpdate (sqlString);
    		
    		if (nRows < 1)
    		{ // No update - new Walk - do INSERT
    			
    			Hashtable insertVals = new Hashtable ();
    			
    			insertVals.put
    				("dogsMainID", stringToStringArray (dog.id));
    			
    			insertVals.put
    				("ID", stringToStringArray (Integer.toString (walk.getWalkID())));
                
                if (walkStartString != null)
                {
                	insertVals.put
                		("walkStartTime", stringToStringArray (walkStartString));
                }
                
                if (walkEndString != null)
                {
                	insertVals.put
                		("walkEndTime", stringToStringArray (walkEndString));
                }
                                
                InsertConfig insertConfig = new InsertConfig("walks", insertVals);
                
                dbAccessDogs.insertData (insertConfig);
    		}
    	}
    	catch (Exception e)
    	{
    		System.out.println(" XXX Problem with Row: " + sqlString); 
    		
    		e.printStackTrace();
    	}
    }
 
    public Object EditDogForm
	   (ProcessHTTPinput processInput,
		HttpServletRequest req,
		HttpServletResponse res) throws Exception
	{
      String elKstring = (String) processInput.getParameter("El");
           
      Dog dog = (Dog) dogs.getDog(elKstring);
      //dog.setKElTemp(elK);
      
      sendObjToJSP("Dog", dog, req);
       
      transferToJSP("EditDog.jsp", req, res);
       
      return (null);
	}
    
    public Object EditDogProcOLD
		   (ProcessHTTPinput processInput,
			HttpServletRequest req,
			HttpServletResponse res) throws Exception
	{
	   String dogID = (String) processInput.getParameter("El");
	        
	   Dog dog = (Dog) dogs.getDog(dogID);
	   
	   String paramBuf = null;
	   Walk lastWalk= dog.getLastWalk();
	   
	   // Start Walk:
	   paramBuf = (String) processInput.getParameter("noneLastWalkOutCB");
	    
	   if (paramBuf != null)
	   { // NONE selected - no Last Walk Out
	       if (lastWalk != null)
	       { lastWalk.setBeginTime(null); }
	   }
	   else
	   {
	      if (lastWalk == null)
	      { 
	        lastWalk = new Walk ();
	      	dog.addLastWalk(lastWalk);
	      }
	      CalCustom lastWalkBeginTime =
	          TimeElement.retrieveCalCustom("LastWalkOut", processInput); 
	
	      lastWalk.setBeginTime(lastWalkBeginTime);
	   }
	   // :Begin Walk
	   
	   // End Walk:
	   paramBuf = (String) processInput.getParameter("noneLastWalkInCB");
	    
	   if (paramBuf != null)
	   { // NONE selected - no Last Walk In
	       if (lastWalk != null)
	       { lastWalk.setEndTime(null); }
	   }
	   else
	   {
	      if (lastWalk == null)
	      { 
	        lastWalk = new Walk ();
	      	dog.addLastWalk(lastWalk);
	      }
	      CalCustom lastWalkEndTime =
	          TimeElement.retrieveCalCustom("LastWalkIn", processInput); 
	
	      lastWalk.setEndTime(lastWalkEndTime);
	   }
	   // :End Walk
	   
	   // Total Time:
	   paramBuf = (String) processInput.getParameter("noneTotalTimeCB");
	    
	   if (paramBuf != null)
	   { // NONE selected - no Total Time
	       dog.setTotalWalkTime(null); 
	   }
	   else
	   {      
	      TimeLength totalWalkTime =
	       TimeElement.retrieveTimeLength("TotalWalkTime", processInput);
	      
	      dog.setTotalWalkTime(totalWalkTime);
	   }
	   // :Total Time
	
	   paramBuf =
	       (String) processInput.getParameter("specialAttentionFactor");
	   
	   double specialAttentionFactor = 1.0;
	   try
	   {
	    specialAttentionFactor = StringProc.stringToDouble(paramBuf);
	   } catch (NumberFormatException e)
	   {
	       specialAttentionFactor = 1;
	       //e.printStackTrace();
	   }
	   
	   dog.setSpecialAttentionFactor(specialAttentionFactor);
	   
	   paramBuf = (String) processInput.getParameter("Allowed");
	
	   dog.setAccess(Dog.getAllowedIndex(paramBuf));
	   
	   paramBuf = (String) processInput.getParameter("Notes");
	   
	   dog.setNotes(paramBuf);
	   
	   dogs.sort();
	
	   transferToJSP(TARGET_JSP, req, res);
	    
	   return (null);
	}

	public Object EditDogProc
	   (ProcessHTTPinput processInput,
		HttpServletRequest req,
		HttpServletResponse res) throws Exception
{		
   String dogID = (String) processInput.getParameter("El");
        
   Dog dog = (Dog) dogs.getDog(dogID);
   
   int k=0;

   // dog.getWalks()
   // for (eachWalk)
   // { remove walk from DB }
   
   Connection conDogs = null;
   
   Statement deleteStatement = null;
   
   Vector walks = dog.getWalks();
   
   Walk walk = null;
   
   int nRows = -1;
   
   int nWalksCurrent = dog.walks.size();
   
   try 
   {
	   conDogs = dbAccessDogs.getCon();
	   
	   int idInt = -1;
	   String idString = null;
	   String sqlString = null;
   
	   	for (k=0; k<nWalksCurrent; k++)
	   	{
	   		walk = (Walk) walks.elementAt(k);
	   		
	   		idInt = walk.getWalkID();
	   		
	   		idString = Integer.toString(idInt);
	   		
	   		// remove current walk from DB and replace with form values:
	   		deleteStatement =
	   			conDogs.createStatement();
		
	   		sqlString = 
	   			("DELETE from walks WHERE id = \"" + idString + "\";");
	  
	   		nRows = deleteStatement.executeUpdate (sqlString);
	   	}
   }
   catch (Exception sqlDeleteException)
   {
		System.out.println(sqlDeleteException);
   }
   
   dog.resetWalks();
   
   boolean outLocal = false;
   
   String paramBuf = null;
      
   for (k=0; k<nWalksCurrent; k++)
   {
	   paramBuf = (String) processInput.getParameter("walkDeleteCB:" + k);
	   	   
	   if (paramBuf == null)
	   { // NOT Delete:
		   
		   String timeElementName = ("walkBegin:" + k);
		   CalCustom beginTime =
		          TimeElement.retrieveCalCustom(timeElementName, processInput);
		   
		   paramBuf = (String) processInput.getParameter("endTimeNoneCB:" + k);
		   
		   CalCustom endTime = null;
		   
		   if (paramBuf == null)
		   {
			   timeElementName = ("walkEnd:" + k);
			   
			    endTime =
		          TimeElement.retrieveCalCustom(timeElementName, processInput); 
		   }
		   
		   if (endTime == null)
		   { outLocal = true; }
			   
		   Walk newWalk = new Walk(beginTime, endTime);
		   
		   dog.addWalk(newWalk);
	   }
   }
   
   // Check for Add Walk:
   paramBuf = (String) processInput.getParameter("addWalkOutCB:ADD");
	   
   if (paramBuf != null)
   { // Add Walk
	   String timeElementName = ("walkBegin:ADD");
	   CalCustom beginTime =
	          TimeElement.retrieveCalCustom(timeElementName, processInput); 
	   
	   paramBuf = (String) processInput.getParameter("endTimeNoneCB:ADD");
	   
	   CalCustom endTime = null;
	   
	   if (paramBuf == null)
	   {   
		  timeElementName = ("walkEnd:ADD");

		  endTime =
	          TimeElement.retrieveCalCustom(timeElementName, processInput); 
	   }
	    
	   Walk newWalk = new Walk(beginTime, endTime);
	   
	   if (endTime == null)
	   { outLocal = true; }
	   
	   dog.addWalk(newWalk);
   }
   
   Vector newWalks = Sort.sort(dog.getWalks());
   int nWalks = newWalks.size();
   
   walk = null;
   
   for (k=0; k<nWalks; k++)
   { // assign primary key WalkID in order
	walk = (Walk) newWalks.elementAt(k);   
	walksIDlast++;
	walk.setWalkID(walksIDlast);
	
	updateWalkInDB (walk, dog, conDogs);
   }
   
   dbAccessDogs.returnCon(conDogs);
   
   dog.setWalks(newWalks);
   dog.setOut(outLocal);
         
   // :Total Time
   paramBuf =
       (String) processInput.getParameter("specialAttentionFactor");
   
   double specialAttentionFactor = 1.0;

   try
   {
    specialAttentionFactor = StringProc.stringToDouble(paramBuf);
   } catch (NumberFormatException e)
   {
       specialAttentionFactor = 1;
       //e.printStackTrace();
   }
   
   dog.setSpecialAttentionFactor(specialAttentionFactor);
   
   paramBuf = (String) processInput.getParameter("Allowed");

   dog.setAccess(Dog.getAllowedIndex(paramBuf));
   
   paramBuf = (String) processInput.getParameter("Notes");
   
   dog.setNotes(paramBuf);
   
   dogs.sort();
   
   transferToJSP(TARGET_JSP, req, res);
   
   dogsDBproc.updateDogDynamic(dogID, dog.getNotes());
   
   return (null);
}

    public void service
	(HttpServletRequest req, 
	 HttpServletResponse res) 
	 	throws ServletException, IOException
   {
	Object returnObject = null;
	ProcessHTTPinput processInput = new ProcessHTTPinput(req);
	processInput.process(req);
	// String action = processInput.getParameter("ACTION");

	String operation = processInput.getOperation();
	
	String type = processInput.getType();
	String op = processInput.getOp();

	try
   {	
	      returnObject = invokeActionMethod(processInput, req, res);
   } // try
	catch (Exception e)
	{
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	if (returnObject == null)
	{ return; }
	
	 // return any required object back through the
	 // existing HTTP connection:	
	ObjectOutputStream outObjStream = 
		new ObjectOutputStream(res.getOutputStream());
	
	outObjStream.writeObject(returnObject);
	
	outObjStream.flush();
  }

    
    public Object ShowDogs
	   (ProcessHTTPinput processInput,
		HttpServletRequest req,
		HttpServletResponse res) throws Exception
	{	
        return (null);
	}
    
    public Object ShowArkDBInfo
	   (ProcessHTTPinput processInput,
		HttpServletRequest req,
		HttpServletResponse res) throws Exception
	{
        ServletOutputStream out = null;
        res.setContentType("text/html");
        out = res.getOutputStream();
        out.println("<HTML>");
        out.println("<CENTER>");
        out.println("<H3>DB Info</H3>");
        out.println("</CENTER>");
        //out.println("<PRE>\n\n");
        
        out.println(outputDBInfo(dbAccessArk));
        
        //out.println("</PRE>");
        out.println("</HTML>");
        
        return (null);
	}
    
    public Object ShowDogsDBInfo
	   (ProcessHTTPinput processInput,
		HttpServletRequest req,
		HttpServletResponse res) throws Exception
	{
     ServletOutputStream out = null;
     res.setContentType("text/html");
     out = res.getOutputStream();
     out.println("<HTML>");
     out.println("<CENTER>");
     out.println("<H3>DB Info</H3>");
     out.println("</CENTER>");
     //out.println("<PRE>\n\n");
     
     out.println(outputDBInfo(dbAccessDogs));
     
     //out.println("</PRE>");
     out.println("</HTML>");
     
     return (null);
	}
    
    public static String outputDBInfo ()
    {
      return (outputDBInfo(dbAccessArk));   
    }
    
    public static String outputDBInfo (DBAccess dbAccessLoc)
    {
      return (dbAccessLoc.getDBInfo());   
    }
    
    public String[] stringToStringArray (String stringVal)
    {
    	String[] stringArray = { stringVal };
    	return (stringArray);
    }
    
    /**
     * Updates dogs - recreates dogs object from dogs DB
     * @return dogs object
     */
    public static Dogs updateAndGetDogs()
    {
    	dogs = 
    		dogsDBproc.updateDogsLocalWithDogsDB();
    	
    	dogs.sort();
    	
        return dogs;
    }
    
    /**
     * @return Returns the dogs.
     */
    public synchronized static Dogs getDogs()
    {
    	/*
    	try
		{
    		Thread synchThread = new Thread(dogsDBproc, "synchData");
    	    synchThread.start();
    		
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		*/
    	
        return dogs;
    }
    /**
     * @param dogs The dogs to set.
     */
    public synchronized static void setDogs(Dogs dogs)
    {
        Action.dogs = dogs;
    }
}
