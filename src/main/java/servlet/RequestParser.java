package servlet;

import java.util.*;
import javax.servlet.http.*;
/**
 * Insert the type's description here.
 * Creation date: (4/17/00 6:18:16 PM)
 * @author: Administrator
 */
public class RequestParser {
/**
 * Insert the method's description here.
 * Creation date: (4/17/00 6:19:35 PM)
 * @return java.util.Hashtable
 */
public static Hashtable parseRequest(HttpServletRequest req) {

	Hashtable reqParams = new Hashtable();

	String reqString = req.getQueryString();

	if (reqString == null)
	{ return (reqParams); }
	
	StringTokenizer st1 = new StringTokenizer(reqString,"&");

	while(st1.hasMoreTokens()){
		
		StringTokenizer st2 = 
			new StringTokenizer(st1.nextToken(),"=");  

		reqParams.put(st2.nextToken(),st2.nextToken());	
	}
		
	return reqParams;
}
/**
 * RequestParser constructor comment.
 */
public RequestParser() {
	super();
}
}
