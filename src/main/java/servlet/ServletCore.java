/*
 * Created on May 5, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package servlet;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author whitteng
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ServletCore extends HttpServlet
{
	static ServletContext servletContext = null;
	
	public static String WebFileRoot = null;

	Class thisClass = null;
	
	public static String ContextName = "Dogs"; // OVERRIDE
	public static String  ServletName = "none"; 
	public static String JSPprefix = "/"; // OVERRIDE
	
	static
		{
			formLinks();
		}
	
	  /**
		 * 
		 */
	public ServletCore()
	{
		super();
		
	}
	
	public void init(ServletConfig conf) throws ServletException
	{
		
		try
		{
			super.init(conf);
			
			servletContext = getServletContext();
			
			ContextName = servletContext.getServletContextName();
			
			ServletName = getServletName();
	
			//?? ServletName = getServletName();
	
			//Util.printVersionInfo();
	
			//??servletContext = conf.getServletContext();
	
			WebFileRoot = servletContext.getRealPath("/");
			
			 // register this servlet with the servletContext so it 
			 // can be referenced from ManageLic servlet when a
			 // new License is installed
			 servletContext.setAttribute(getServletNameLoc(), this);

			// setup message processing:
			thisClass = this.getClass();
			
			formLinks();
		}
		catch (Exception ex)
		//catch (SQLException ex)
		{
			System.out.println 
			  ("***ERROR (ServletCore.init()): Did NOT initialize properly.. ");
			ex.printStackTrace();
		}
	}
		
	Object invokeActionMethod (ProcessHTTPinput processInput, 
							   HttpServletRequest req,
							   HttpServletResponse res)
	{
		Object returnObj = null;
		final Class[] paramTypeArray = 
			{ProcessHTTPinput.class, HttpServletRequest.class, HttpServletResponse.class};
			
		Object[] paramArray = new Object[3];
		
		try
		{
			String action = processInput.getOp();
			if (action != null)
			{			
				Method actionMethod = 
					thisClass.getMethod(action, paramTypeArray);
			
				paramArray[0] = processInput;
				paramArray[1] = req;
				paramArray[2] = res;
			
				returnObj = actionMethod.invoke(this, paramArray);
			}
			else
			{
			    
			    transferToJSP ("Dogs.jsp", req, res);
		    }
		}
		catch (SecurityException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (NoSuchMethodException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IllegalAccessException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (ServletException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return (returnObj);
	}
	
	public static String formServletURL 
				(String typeValue, String opValue)
	{
		String URLstring = 
				getServletNameLoc() + "?TYPE=" 
				+ typeValue
				+ "&OP="
				+ opValue;
			
		return 
			(URLstring);
	}
	
	public static String formServletURL 
					(String opValue)
	{
		String URLstring = 
				getServletNameLoc() + "?OP=" 
				+ opValue;
		
		return 
			(URLstring);
	}
	
	public static String formForwardURL 
						(String forwardResourceName)
   {	
		String URLstring = 
					getServletNameLoc() + "?Forward=" 
					+ forwardResourceName;
		
			return 
				(URLstring);
   }
   
   public static String formForwardJSP 
						   (String JSPname)
	  {	
		   String URLstring = 
					   getServletNameLoc() + "?ForwardJSP=" 
					   + JSPname;
		
			   return 
				   (URLstring);
	  }
   
	public static String formLink (String linkURL, String linkDisplay)
	{
		return 
			("<A href=\"" + linkURL + "\">" + linkDisplay + "</A>\n");
	}
	
	public static String formLink 
		(String typeValue, String opValue, String linkDisplay)
	{	
		return 
			(formLink (formServletURL (typeValue, opValue), linkDisplay));
	}
		
	protected void forwardToURL
			(String URLstring, 
			 HttpServletRequest request,
			 HttpServletResponse response) 
			 throws ServletException, IOException 
	{ 
		RequestDispatcher dispatcher = 
			getServletContext().getRequestDispatcher("/" + URLstring); // ???

		dispatcher.forward(request, response); 
	}
	
	protected void sendObjToJSP
			(String objName, 
			 Object obj, 
			 HttpServletRequest request)
	{
		request.setAttribute(objName, obj);		
	}
		
	protected void transferToJSP
	   (String page, 
		 HttpServletRequest request,
		 HttpServletResponse response) 
		 throws ServletException, IOException 
	{ 
		RequestDispatcher dispatcher = 
			getServletContext().getRequestDispatcher(getJSPprefix() + page);

		dispatcher.forward(request, response); 
	}

	/**
	 * @return
	 */
	public static String getServletNameLoc()
	{
		return ServletName;
	}

	/**
	 * @param string
	 */
	public static void setServletNameLocal (String string)
	{
		ServletName = string;
	}
	
	static void formLinks ()
	{ // override
	}

	/**
	 * @return
	 */
	public static String getJSPprefix()
	{
		return JSPprefix;
	}

}
