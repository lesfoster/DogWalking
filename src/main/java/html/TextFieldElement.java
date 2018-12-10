/*
 * Created on Oct 25, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package html;

/**
 * @author whitteng
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TextFieldElement 
{

static public String outputHTMLtableCell
	(String displayName,
	 String paramName)
{
	return (outputHTMLtableCell 
				(displayName,
				 paramName,
				 "",
				 true,
				 false) );
}	
	
static public String outputHTMLtableCell
	(String displayName,
	 String paramName,
	 String paramValue)
{
	return (outputHTMLtableCell 
				(displayName,
				 paramName,
				 paramValue,
				 true,
				 false) );
}
	
static public String outputHTMLtableCell
	(String displayName,
	 String paramName,
	 String paramValue,
	 boolean enable)
{
	return (outputHTMLtableCell 
				(displayName,
				 paramName,
				 paramValue,
				 enable,
				 false) );
}
	
static public String outputHTMLtableCell
	(String displayName,
	 String paramName,
	 String paramValue,
	 boolean enable,
	 boolean password)
{
	StringBuffer out = new StringBuffer();
	
	if ( (paramName == null) || (paramName.equals("")) )
	{
		paramName = "";
		paramValue = null;
	}
	
	String type = "TEXT";
	
	if (password)
	{
		type = "PASSWORD";
	}
	out.append("\n");
	out.append("    <TD>\n");
	out.append("      " + displayName +"\n");
	out.append("    </TD>\n");
	out.append("    <TD>\n");
    out.append
    ("            <INPUT TYPE=\""+ type + "\" SIZE=\"10\" NAME=\""+ paramName +"\" ");
    if (paramValue != null)
    { out.append (" VALUE=\""+paramValue+ "\" "); }
     if (!enable)
    { out.append (" DISABLED "); }
    out.append(">\n");
    out.append("        </TD>\n");
    
    return (out.toString());
}

static public String outputHTML
(String paramName,
 String paramValue,
 int kChars)
{
    return outputHTML ("", paramName, paramValue, true, false, kChars);
}

static public String outputHTML
(String displayName,
 String paramName,
 String paramValue,
 boolean enable,
 boolean password,
 int kChars)
{
    StringBuffer out = new StringBuffer();

    if ( (paramName == null) || (paramName.equals("")) )
    {
        paramName = "";
        paramValue = null;
    }

    String type = "TEXT";

    if (password)
    {
        type = "PASSWORD";
    }
    
	out.append
	(displayName +  " <INPUT TYPE=\""+ type + "\" SIZE=\"" + 
	        String.valueOf(kChars) +  "\" NAME=\""+ paramName +"\" ");
	if (paramValue != null)
	{ out.append (" VALUE=\""+paramValue+ "\" "); }
	 if (!enable)
	{ out.append (" DISABLED "); }
	out.append("\n");

return (out.toString());
}


}
