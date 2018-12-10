package servlet;

import javax.servlet.http.*;

import util.StringProc;

import servlet.RequestParser;



/**
 * Insert the type's description here.
 * Creation date: (04/06/00 15:33:54)
 * @author: 
 */

 
 public class ProcessHTTPinput 
{
	private Object objectIn = null;

	private boolean isObject = false;
		
	private HttpServletRequest servletRequest = null;

	private String actionParamVal = null;
	
	private String typeParamVal = null;
	
	public java.util.Hashtable paramPairs = null;

	//-------------------------------------------
	private String SID = "nullSID";

/**
 * Insert the method's description here.
 * Creation date: (04/06/00 16:34:00)
 * @return java.lang.String
 */
public String getActionParamVal() {
	return actionParamVal;
}

/**
 * Insert the method's description here.
 * Creation date: (05/01/00 16:34:00)
 * @return java.lang.String
 */
public String getOperation() {
	return actionParamVal;
}

public String getOp() {
	return actionParamVal;
}

void setOp(String opVal) 
{
	actionParamVal = opVal;
}

public String getType()
{
	return typeParamVal;
}

/**
 * Insert the method's description here.
 * Creation date: (04/06/00 16:34:00)
 * @return java.lang.Object
 */
public Object getObjectIn() {
	return objectIn;
}
/**
 * Insert the method's description here.
 * Creation date: (4/17/00 6:46:10 PM)
 * @return java.lang.String
 * @param paramName java.lang.String
 */

/*
public String getParameter(String paramName) {

	String returnVal = null;
		
	if(paramPairs != null){

		returnVal = (String)paramPairs.get(paramName);
		
	}else{

		returnVal = servletRequest.getParameter(paramName);
	}
	
	return returnVal;
}
*/

public String getParameter(String paramName) 
{
	String returnVal = null;
		
	returnVal = 
		servletRequest.getParameter(paramName);
	
	return returnVal;
}

public String getParameterStripped (String paramName) 
{
	String paramVal = getParameter(paramName);

	return (stripHTMLheader(paramVal));
}
	

public String stripHTMLheader (String inString)
{   
	if (inString == null)
	{ return ((String) null); }
	      
	inString = inString.trim();
         
     // DS_#_
     // 01234
     int begin = inString.indexOf('_', 3);
     
     String strippedString = inString.substring(begin + 1);
     
     return (strippedString);    
}


public int getIntParameter(String paramName) 
{
	String stringVal = null;		
	int intVal = -1;

	stringVal = getParameter(paramName);
	if (stringVal != null)
	{	
		intVal = StringProc.stringToInt(stringVal);
	}
	
	return intVal;
}

public int getIntParameterStripped(String paramName) 
{
	String stringVal = null;		
	int intVal = -1;

	stringVal = getParameterStripped(paramName);
	if (stringVal != null)
	{	
		intVal = StringProc.stringToInt(stringVal);
	}
	
	return intVal;
}

/**
 * Insert the method's description here.
 * Creation date: (04/06/00 16:34:00)
 * @return HTTPServletRequest
 */
public HttpServletRequest getServletRequest() {
	return servletRequest;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/00 1:29:36 PM)
 * @return java.lang.String
 */
public java.lang.String getSID() {
	return SID;
}
/**
 * Insert the method's description here.
 * Creation date: (4/11/00 7:36:42 PM)
 * @param param javax.servlet.http.HttpServletRequest
 */
public ProcessHTTPinput(HttpServletRequest req) {

	setServletRequest(req);	
}
/**
 * Insert the method's description here.
 * Creation date: (04/06/00 15:35:39)
 */
public void process
	(HttpServletRequest req)throws java.io.IOException 
{
	objectIn = null;
	isObject = false;
		
	//servletRequest = req;

  	String objectParamVal = req.getParameter ("OBJECT");


  	//***********************
  	if(objectParamVal == null){
	  	
  		paramPairs = RequestParser.parseRequest(req);

  		objectParamVal = getParameter("OBJECT");
  	}
	//***********************
  		
	if ( (objectParamVal != null) &&
	   (objectParamVal.equalsIgnoreCase("TRUE") ) )
	{
		try
		{
		  java.io.ObjectInputStream objectInStream =
			new java.io.ObjectInputStream(req.getInputStream());

		  objectIn = objectInStream.readObject();

		  isObject = true;
		}
		catch (ClassNotFoundException e)
		{
		  e.printStackTrace();
		}
	}
	 
	SID = getParameter("SID");
  	
	actionParamVal  = getParameter ("OPERATION");
	
	if (actionParamVal == null)
	{ // try Class/Op format
		typeParamVal = getParameter ("TYPE");
		actionParamVal = getParameter("OP");
	}	  
}
/**
 * Insert the method's description here.
 * Creation date: (04/06/00 15:35:39)
 */
public void processText
	(HttpServletRequest req)throws java.io.IOException 
{
	paramPairs = RequestParser.parseRequest(req);
	  
}
/**
 * Insert the method's description here.
 * Creation date: (04/06/00 16:34:00)
 * @param newServletRequest HTTPServletRequest
 */
void setServletRequest(HttpServletRequest newServletRequest) {
	servletRequest = newServletRequest;
}
/**
 * Insert the method's description here.
 * Creation date: (8/4/00 1:29:36 PM)
 * @param newSID java.lang.String
 */
public void setSID(java.lang.String newSID) {
	SID = newSID;
}
/**
 * ProcessHTTPinput constructor comment.
 */
public ProcessHTTPinput() {
	super();
}
}
