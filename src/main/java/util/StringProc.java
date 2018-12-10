package util;

import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class for String processing
 *
 * @see RWComp
 * @author  Gary Whitten
 */

public class StringProc implements Cloneable
{
  BufferedReader buffReader;
  FileReader fileReader;
  StringReader stringReader;
  String filePathName;
  String fileString;
  int nChars;
  String line;
  StreamTokenizer sttTokener;
  char[] fileChars;

  /**
   * clone the string proc
   *
   * @return the cloned object
   */
  public Object clone() {
	  StringProc stringProc = null;
	  try {
		  stringProc = (StringProc)super.clone();
		  stringProc.fileString = new String (this.fileChars);
		  stringProc.stringReader = new StringReader (stringProc.fileString);
		  stringProc.buffReader = new BufferedReader(stringProc.stringReader, this.nChars);
	  } catch (CloneNotSupportedException cnse) {
		  cnse.printStackTrace();
	  }
	  return stringProc;
  }      
  public void close ()
	throws IOException
  {
   // clean up file stuff
//   buffReader.close();
//   stringReader.close();
   fileReader.close();
  }      
  /*
  public String readFile (String filePathName)
	throws FileNotFoundException, IOException
  {
   openFile (filePathName);
   return (fileString);
  }
  */

 // String Parsing Methods:
 public static boolean equiv
	(String stringA, String stringB)
  {
	stringA = stringA.trim();
	stringB = stringB.trim();
	
	return (stringA.equalsIgnoreCase(stringB));
  }      
 
  static public boolean contains
	(String parentString, String matchString)
  {
	boolean found = false;
	
	if (parentString == null)
	{ return (false); }

	if (parentString.indexOf (matchString) != -1)
	  found = true;

	return (found);
  }      
  // String Parsing Methods:
  public boolean contains
	(String matchString)
  {
	boolean found = false;

	if (line.indexOf (matchString) != -1)
	  found = true;

	return (found);
  }      
  public static int getIntVal (Integer intObj) {
	if (intObj == null) {
	  return (-1);
	}
	else {
	  return (intObj.intValue());
	}
  }      
  public String getLine ()
	throws IOException
  {
	String line = null;
	line = buffReader.readLine();
	return (line);
  } // end getLine()      
  //:String Parsing Methods


  //---------------------------------------------

 /* 11/15/98 What is this?:
  public String getLine (String matchString)
  {
   boolean found = false;
   String line = null;

   while (!found)
   {
	try
	{
	 line = buffReader.readLine();
	}
	catch (IOException e) {}

	if (line.indexOf (matchString) != -1)
	{ found = true; }

	if (found)
	{
	 return (line);
	}
	else
	{
	    return (null);
	}
   }
   return (line);
  } // end getLine(String matchString)
  */

  public String getLine (String matchString)
	throws IOException
  {
   boolean cont = true;
   String line = null;

   while (cont)
   {
	line = buffReader.readLine();

	if (line == null)
	{
	 cont = false;
	 return (null);
	}

	if (line.indexOf (matchString) != -1)
	{ return (line); }

   }

   return (null);
  } // end getLine(String matchString)      
  public static String getLocalDirName()
  {
	String localDir;

	localDir = System.getProperty("user.dir");

	// System.out.println("Local Directory: " + localDir);

	return (localDir);
  }
        
  public static String getSysFile (String sysFileName)
	throws FileNotFoundException, IOException
  {
	String sysFilePath =
	  ("Smartsite/Jscripts/" + sysFileName);

	return (readFile (sysFilePath));
  }
        
  public StringProc (String filePathName)
	throws FileNotFoundException, IOException
  {
	openFile (filePathName);
  }
  
  public StringProc (String filePathName, boolean JAR)
	throws FileNotFoundException, IOException
  {
  	if (JAR)
  	{
		openFileFromJAR (filePathName);
  	}
  	else
  	{
  		openFile (filePathName);
  	}
  }  
  
  
  public static BufferedReader openWebFileForReading (String urlString)
  { 
	InputStream inStream = null;
 	InputStreamReader inStreamReader = null;
 	URL url = null;
 	BufferedReader buffReader = null;
 	
 	try 
 	{  
 		url = new URL (urlString);
 		
 		inStream = url.openStream();
 		
 		inStreamReader = new InputStreamReader (inStream);
 			
		buffReader = new BufferedReader (inStreamReader);			
 	 
	} catch (Exception e) 
	{
		System.out.println (" Problem reading: " + urlString + " from Web");
		System.out.println (e); 
	}
	
	return (buffReader);
  }
        
  public void openFile (String filePathName)
	throws FileNotFoundException, IOException
  {
	String firstWord;
	StringTokenizer tok;
	int nRead;
	File file;

	String localDir;

	localDir = System.getProperty("user.dir");

	System.out.println("Local Directory: " + localDir);

	this.filePathName = filePathName;

	// check file for all sorts of errors
	if (filePathName == null)
	{
	  //throw Exception("No File Name Given");
	  System.out.println ("No File Name Given");
	}

	file =  new File(filePathName);

	if ( !file.exists() || !file.canRead() )
	{
	  //throw Exception("Can't read " + filePathName );
	  throw new FileNotFoundException();
	}

	if ( file.isDirectory() )
	{
	  //throw Exception(filePathName + "is a directory");
	  throw new FileNotFoundException();
	}

	// Start reading the file
	nChars = (int) file.length();
	fileReader = new FileReader (file);
	fileChars = new char[nChars];
	nRead = 0;

	nRead += fileReader.read(fileChars, 0, nChars);
	if (nRead != nChars)
	{
	  //throw Exception("Problem reading to end of file.");
	  throw new IOException();
	}

	fileString = new String (fileChars);
	stringReader = new StringReader (fileString);
	buffReader = new BufferedReader(stringReader, nChars);
  }  
  
  public static String readFileFromJAR (String filePathName)
  {

//throws IOException { 
    // absolute from the classpath 
    /*
    URL url = FindResources.class.getResource("/mypackage/foo.txt");
    // relative to the class location 
    url = FindResources.class.getResource("foo.txt"); 
    // another relative document 
    url = FindResources.class.getResource("docs/bar.txt"); 
    */
    
 	StringBuffer stringBuf = new StringBuffer();
 	String line;
 	InputStream inStream;
 	InputStreamReader inStreamReader;
 	
 	try {  
 		
 		//inStream = resourceProvider.getClass().getResourceAsStream(filePathName);
		inStream = StringProc.class.getResourceAsStream(filePathName);
		
		inStreamReader = new InputStreamReader (inStream);
		
		BufferedReader bin = new BufferedReader (inStreamReader);			
		
	 		
	// URL url = resourceProvider.getClass().getResource(filePathName);

    //BufferedReader bin = new BufferedReader ( 
    //    new InputStreamReader( url.openStream( ) )); 
  
 	line = bin.readLine();
 	
    while ( line != null )  
    {
    	stringBuf.append(line + "\n");	 
        //System.out.println( line );
        line = bin.readLine();
    } 
	} catch (Exception e) 
	{
		System.out.println (" Problem reading: " + filePathName);
		System.out.println (e); 
	}

	return (stringBuf.toString());  	
  }
  
  public void openFileFromJAR (String filePathName)
  {
	//throws IOException { 
    // absolute from the classpath 
    /*
    URL url = FindResources.class.getResource("/mypackage/foo.txt");
    // relative to the class location 
    url = FindResources.class.getResource("foo.txt"); 
    // another relative document 
    url = FindResources.class.getResource("docs/bar.txt"); 
    */
    
	InputStream inStream;
 	InputStreamReader inStreamReader;
 	
 	try {  
 		//inStream = resourceProvider.getClass().getResourceAsStream(filePathName);
		inStream = StringProc.class.getResourceAsStream(filePathName);
		
		inStreamReader = new InputStreamReader (inStream);
		
		buffReader = new BufferedReader (inStreamReader);			
		
	 		
	// URL url = resourceProvider.getClass().getResource(filePathName);

    //BufferedReader bin = new BufferedReader ( 
    //    new InputStreamReader( url.openStream( ) )); 
  
 	// line = bin.readLine();
 	 
	} catch (Exception e) 
	{
		System.out.println (" Problem reading: " + filePathName + " from JAR");
		System.out.println (e); 
	}

  }

      
  public static String readFile (String filePathName)
	//throws FileNotFoundException, IOException
  {
	int nRead;
	File file;
	char[] fileChars;
	String fileString = null;
	int nChars;
	FileReader fileReader;

	String localDir;

	try {
	localDir = System.getProperty("user.dir");

	System.out.println("Local Directory: " + localDir);

	// check file for all sorts of errors
	if (filePathName == null)
	{
	  //throw Exception("No File Name Given");
	  System.out.println ("No File Name Given");
	}

	file =  new File(filePathName);

	if ( !file.exists() || !file.canRead() )
	{
	  //throw Exception("Can't read " + filePathName );
	  throw new FileNotFoundException();
	}

	if ( file.isDirectory() )
	{
	  //throw Exception(filePathName + "is a directory");
	  throw new FileNotFoundException();
	}

	// Start reading the file
	nChars = (int) file.length();
	fileReader = new FileReader (file);
	fileChars = new char[nChars];
	nRead = 0;

	nRead += fileReader.read(fileChars, 0, nChars);
	if (nRead != nChars)
	{
	  //throw Exception("Problem reading to end of file.");
	  throw new IOException();
	}

	fileString = new String (fileChars);

	// clean up file stuff
	fileReader.close();
	}
	catch (Exception e)
	{
		System.out.println ("could NOT read file: " + filePathName);
	}
	return (fileString);
  }      
  public String readLine ()
	throws IOException
  {
   //String line = null;
   line = buffReader.readLine();

  // setup tokenizer for readLineRW routine
   if (line != null) {
	 sttTokener = new StreamTokenizer (new StringReader(line));
	 sttTokener.quoteChar('"');
	 sttTokener.slashSlashComments(false);
	 sttTokener.slashStarComments(false);
	 sttTokener.eolIsSignificant(false);
	 sttTokener.ordinaryChar('/');
   }
   return (line);
  }      
  /**
   *  This method makes extensive use of "StreamTokenizer"
   *  <BR>  <I>See "The Java Programming Language" Second Edition,
   * <BR> Arnold, Gosling, Addison Wessley, Section 12.20, p. 250
   *  StreamTokenizer breaks a string into tokens <BR>
   *  - A Token is a string between delimiters
   *  - <ol> Delimiters are
   *      <li> White Space
   *      <li> Special (mostly symbols)
   *      <li> User Specified  (with "quoteChar" method)
   *    </ol>
   *  running nextToken() results in moving to the next delimter
   *  The type of delimiter found in the returned int, (ttype),
   *  if a normal word is found, the int returned is TT_WORD,
   *  in which case the word can be retrieved in the "sval" variable
   *  if a special character is the current delimiter, it is returned
   *  if a word contained between the user specified "quoteChar"s is the token
   *  it is returnd with the quoteChars stripped
   *  Example:
   *   message="hello world"
   *    would result in
   *      ttype     sval
   * 1    TT_WORD   message
   * 2    `=`       null
   * 3    TT_WORD   hello world
   *
   * <I>Note: a "special" character can be specified as "ordinary" but we <BR>
   * haven't found a way to make an "ordinary" character "special". </I>
   *
   */
  public String readLineRW() throws IOException {
	int intType;
	String lineOut;
	final int conLESSTHAN = 60;
	final int conEQUALS = 61;
	final int conGREATERTHAN = 62;
	final int conSLASH = 47;

// make sure the current line has data - otherwise get the next line
	intType = sttTokener.nextToken();
	if (intType == StreamTokenizer.TT_EOF) { // end of the line
	  readLine();
	  if (line == null) // end of file
	  { return ( (String) null ); }
	  while (line.trim().length() <= 0)
	  {
		readLine();
		if (line == null)
		{ return ( (String) null ); }  // end of file
	  }
	  line = line.trim();
	  intType = sttTokener.nextToken();
	}

	if (intType == conLESSTHAN) { // start of a tag or end tag set
	  lineOut="<";
	  intType = sttTokener.nextToken(); // get tag name or slash
	  if (intType == conSLASH) {
		intType = sttTokener.nextToken(); // get tag name
		lineOut = lineOut + '/' + sttTokener.sval +">";
		intType = sttTokener.nextToken(); // get >
	  }
	  else {
		lineOut=lineOut+sttTokener.sval;
	  }
	}
	else if (intType == conGREATERTHAN) { // could be just >
	  lineOut=">";
	}
	else { // must be a parameter
	  lineOut = sttTokener.sval;
	  intType = sttTokener.nextToken();
	  if (intType == conEQUALS) {
		intType = sttTokener.nextToken();
		lineOut = lineOut + "=" + sttTokener.sval;
	  }
	  else {
		sttTokener.pushBack();
	  }
	}
	return lineOut;
  }      
/*
  public int getInt (String matchString)
	throws PublisherException
  {
	String line = null;
	try {
	  line = getLine(matchString);
	}
	catch(IOException e) {
	  throw new PublisherException(PublisherException.TEMPLFILEERR);
	}
	String firstWord = getFirstWord (line);
	return (stringToInt (firstWord));
  }

  public double getDouble(String matchString)
	throws PublisherException
  {
	String line = null;
	try {
	  line = getLine(matchString);
	}
	catch(IOException e) {
	  throw new PublisherException(PublisherException.TEMPLFILEERR);
	}

	String firstWord = getFirstWord (line);
	return (stringToDouble (firstWord));
  }
*/
  static public String removeBrackets (String text) {
	String buf = text.trim();
	int start, end;

	start = buf.indexOf("<") + 1;
	end = buf. indexOf(">");

	if (end == -1) {
	    end = buf.length();
	}

	buf = buf.substring(start, end);

	return (buf);
  } 
  
  static public String getSubstring 
  	(String text, String startString, String endString) 
  { // Returns all text between startString and endString
	String buf = text.trim();
	int start, end;

	start = buf.indexOf(startString) + 1;
	
	if (start <= 0)
	{ return (null); }

	if (endString.equals(""))
	{ end = buf.length(); }
	else
	{
		end = buf.indexOf(endString);

		if (end == -1)
		{
		    return (null);
		}
	}
	
	buf = buf.substring(start, end);

	return (buf.trim());
  } 
    
	static public String findLineThatContains
		(BufferedReader bufReader, String matchString)
	{
		boolean cont = true;
		
		String line = null;
		
		while (cont)
		{
			try
			{
				line = bufReader.readLine();
				if (line == null)
				{ return (null); }
			} 
			catch (IOException e)
			{
				System.out.println(" XXX PROBLEM GETTTING DOGS FROM WHS SITE XXX");
				e.printStackTrace();
				return (null);
			}
			
			if (StringProc.contains(line, matchString))
			return (line);
		}
		
		return (null);
	}
	
	static public String findLineWithBeginEnd
		(BufferedReader bufReader, String beginString, String endString)
	{
		boolean cont = true;
		
		String line = null;
		String foundString = null;
		
		while (cont)
		{
			try
			{
				line = bufReader.readLine();
				
				if (line == null)
				{ return (null); }
			} 
			catch (IOException e)
			{
				System.out.println(" XXX PROBLEM GETTTING DOGS FROM WHS SITE XXX");
				e.printStackTrace();
				return (null);
			}
			
			 Pattern pattern = Pattern.compile(beginString + "(.*)" + endString);
			 Matcher matcher = pattern.matcher(line);
			 boolean matched = matcher.find();
			 
			 if (matched)
			 { // return the first match:
				 foundString = matcher.group(1); 
				 
				 // 11/30/11 Clean Up `"`"
				 //foundString = foundString.replace('"', ' ');
					
				 foundString = foundString.replaceAll("\"", "");
				 foundString.trim();
				 
				 return (foundString);
			 }
		}
		
		return (null);
	}
	
	static public String findBeginEndInString
		(String stringIn, String beginString, String endString)
	{
		String foundString = null;
		
		Pattern pattern = Pattern.compile(beginString + "(.*?)" + endString);
		Matcher matcher = pattern.matcher(stringIn);
		boolean matched = matcher.find();
		 
		 if (matched)
		 { // return the first match:
			 foundString = matcher.group(1); 
		 }
	
		 return (foundString);
    }


  static public String getSubstringFromStart 
  	(String text, char delimiter) 
  { // Returns all text from start to the delimiter, NOT including delimiter
	String buf = text.trim();
	int start, end;

	end = buf.indexOf(delimiter);
	
	// Return the input string if the delimiter is not found
	if (end == -1)
	{
		return text;
	}
	
	buf = buf.substring(0, end);

	return (buf.trim());
  } 
            

  static public String getSubstringFromEnd
  	(String text, char delimiter) 
  { // Returns all text from delimiter to end with search starting from end of string, 
  	// String returned is NOT including delimiter
	String buf = text.trim();
	int start, end;

	start = buf.lastIndexOf(delimiter);
	
	// Return the input string if the delimiter is not found
	if (start == -1)
	{
		return text;
	}
	
	end = buf.length();
	buf = buf.substring(start+1, end);

	return (buf.trim());
  } 
            

  static public String getSubstringToEnd
  	(String text, char delimiter) 
  { // Returns all text from delimiter to end with search starting from beginning of string, 
  	// String returned is NOT including delimiter
	String buf = text.trim();
	int start, end;

	start = buf.indexOf(delimiter);
	
	// Return the input string if the delimiter is not found
	if (start == -1)
	{
		return text;
	}
	
	end = buf.length();
	buf = buf.substring(start+1, end);

	return (buf.trim());
  } 
            

  static public String getSubstring 
  	(String text, char delimiter) 
  { // Returns all text between delimiter
	String buf = text.trim();
	int start, end;

	start = buf.indexOf(delimiter);
	
	// Return the input string if the delimiter is not found
	if (start == -1)
	{
		return text;
	}
	
	end = buf.lastIndexOf(delimiter);
	
	// Only 1 delimiter present.  Return string starting at after the
	// 1st delimiter to the end of the string.
	if (end == start)
	{
		end = buf.length();
	}

	buf = buf.substring(start+1, end);

	return (buf.trim());
  } 
            


  /**
   * Return a vector af all substrings separated by the delimiter.
   */
  static public Vector getAllSubstrings
  	(String text, char delimiter) 
  {
  	Vector subStrs = new Vector();
  	String inStr = text.trim();
  	
  	while (true)
  	{
  		// Obtain a sub-string from start.
  		String subStr = getSubstringFromStart (inStr, delimiter);
  		subStrs.addElement(subStr);
  		
  		if (subStr.length() == inStr.length())
  			break; // exit when the sub-string is the only one.
  			
  		// Remove the sub-string just obtained
  		inStr = removeStringFromStart (inStr, delimiter);
  	} // while (true)
  	
  	return (subStrs);
  }


  /**
   * Return a vector of all substrings between delimiter, including the delimiters at both ends.
   * 	If input string is null or of zero length, return null.
   */
  static public Vector getSubstringsBetweenChar 
  	(String text, char delimiter) 
  { // Returns all substrings between delimiter
  	if ((text == null) || (text.length() == 0))
  	{
  		return null;
  	}
  	
  	Vector subStrs = new Vector();
  	String inStr = text.trim();
  	int start, end;
  	int isubStart = 0;
  	
  	while (true)
  	{
  		start = inStr.indexOf(delimiter, isubStart);
  		if (start == -1)
  		{
  			break; // starting delimiter MUST be present
  		}
  		
  		end = inStr.indexOf(delimiter, start+1);
  		if (end == -1)
  		{
  			break; // ending delimiter MUST be present
  		}
  		
  		String subStr = inStr.substring(start, end+1);
  		subStrs.addElement(subStr);
  		
  		// Update starting search point
  		isubStart = end + 1;
  		
  	} // while (true)

	return (subStrs);
  } 
            


  static public String removeRW (String text) {
	int start;

	start = text.indexOf("RW") + 2;
	return (text.substring(start));
  }      
  /**
   * reset the string reader
   */
  public void reset() {
	  try {
		  stringReader.reset();
		  buffReader.mark(0);
		  buffReader.reset();
	  } catch (IOException ioe) {
		  ioe.printStackTrace();
	  }
  }      
  static public double stringToDouble(String stringVal) 
  	throws NumberFormatException
  {
	return ( (Double.valueOf(stringVal)).doubleValue());
  }      
/*
  static public String getFirstWord (String line) {
	StringTokenizer tok = new StringTokenizer (line);
	return (tok.nextToken());
  }
*/
  static public int stringToInt(String stringVal) {
	return (Integer.parseInt (stringVal));
  }      
  public StringProc ()
  {
	super();
  }      


  public static boolean writeFile
	(String writeString, String filePathName)
  {
  	boolean ok = true;
	try
	{
	/*
	  File file = new File(filePathName);
	  FileOutputStream output = new FileOutputStream(file);
	  output.close();
	 */

	  FileWriter fileWriter = new FileWriter(filePathName);
	  PrintWriter writer = new PrintWriter(fileWriter, true);

	  writer.print (writeString);

	  writer.flush();
	  writer.close();

	  } catch (IOException ioe)
	  {
		ioe.printStackTrace();
		ok = false;
	  }
	  
	  return (ok);
  } 
  
       
  public static void writeVecToFile (Vector vec, String filePathName)
  {
	int k;

	try
	{
	/*
	  File file = new File(filePathName);
	  FileOutputStream output = new FileOutputStream(file);
	  output.close();
	 */

	  FileWriter fileWriter = new FileWriter(filePathName);
	  PrintWriter writer = new PrintWriter(fileWriter, true);

	  for (k=0; k<vec.size(); k++)
	  {
		writer.println((vec.elementAt(k)).toString());
	  }
	  
	  writer.flush();
	  writer.close();

	  } catch (IOException ioe)
	  {
		ioe.printStackTrace();
	  }
  }      


  // Remove an attribute from the input string.
  // An attribute might be CHARACTER SET charSetName.
  // Since charSetName is varying, only CHARACTER SET needs be input.
  // Right after the attribute coulbe be a space (if there's more attribute after this one), 
  // a comma (if this is the end of current column specification), 
  // or a closed paranthesis (for end of sql command).
  // Note: This method MUST NOT be used if the attribute contains a closed paranthesis.
  static public String removeAttribute (String inString, String attribute) 
  {
	  int istart = -1;
	  int iend = -1;
	  int inext = -1;
	  String outString = inString;
	  String frontStr;
	  String endStr = "";
		
	  istart = inString.indexOf(attribute);
	  if (istart != -1)
	  {
		iend = istart + attribute.length();
		if (inString.indexOf(' ', iend) != -1)
		{
			inext = inString.indexOf(' ', iend);
		} else if (inString.indexOf(',', iend) != -1)
		{
			inext = inString.indexOf(',', iend);
		} else 
		if (inString.indexOf(')', iend) != -1)
		{
			inext = inString.indexOf(')', iend);
		} 
		else
		{
			inext = inString.length() -1; // delete till end of inString
		}
		
		outString = removeString (inString, istart, inext); 

	  } // if (istart != -1)
	  
	  return outString;
	  
  }
	
	
	// Remove a string from the start of the input string a to the delimiter
	static public String removeStringFromStart (String inString, char delimiter) 
	{
		int istart, iend;
		String outString = "";
		
		istart = inString.indexOf(delimiter);
		
		// Find the substring from the delimiter to the end of the string.
		if (istart != -1)
		{
			iend = inString.length();
			outString = inString.substring(istart+1, iend);
			outString = outString.trim();
		}
		return (outString);
	}
	
	
	// Remove a string and a space right after it, if any, from the input string.
	static public String removeString (String inString, String tobeRemovedStr) 
	{
		int istart, iend;
		String outString = inString;
		String frontStr;
		String endStr = "";
		
		istart = inString.indexOf(tobeRemovedStr);
		iend = istart + tobeRemovedStr.length();
		if (inString.indexOf(' ', iend-1) != -1)
		{
			iend++; // include the space after tobeRemovedStr
		}
		
		// Remove tobeRemovedStr
		if (istart != -1)
		{
			frontStr = inString.substring(0, istart-1);
			if (iend < inString.length())
			{
				endStr = inString.substring(iend);
			}
			outString = frontStr + endStr;
			return outString;
		}
		else
		{
			// tobeRemovedStr is not in the input string
			return inString;
		}
	}
	
	
	// Remove a string from the input string. 
	static public String removeStringNoSpace (String inString, String tobeRemovedStr) 
	{
		int istart, iend;
		String outString = inString;
		String frontStr;
		String endStr = "";
		
		istart = inString.indexOf(tobeRemovedStr);
		iend = istart + tobeRemovedStr.length();
		
		// Remove tobeRemovedStr
		if (istart != -1)
		{
			frontStr = inString.substring(0, istart-1);
			if (iend < inString.length())
			{
				endStr = inString.substring(iend);
			}
			outString = frontStr + endStr;
			return outString;
		}
		else
		{
			// tobeRemovedStr is not in the input string
			return inString;
		}
	}
	
	
	// Remove a string between sStr and eStr from the input string.
	static public String removeString (String inString, String sStr, String eStr) 
	{
		int istart, iend;
		String outString = inString;
		String frontStr;
		String endStr = "";
		
		istart = inString.indexOf(sStr);
		iend = inString.indexOf(eStr);
		if (iend == -1)
		{
			iend = inString.length(); // eStr is not in inString
		}
		
		// Remove tobeRemovedStr
		if (istart != -1)
		{
			frontStr = inString.substring(0, istart-1);
			if (iend < inString.length())
			{
				endStr = inString.substring(iend+1);
			}
			outString = frontStr + endStr;
			return outString;
		}
		else
		{
			// sStr is not in the input string
			return inString;
		}
	}
	
	
	// Remove a string between indices istart and iend from the input string.
	static public String removeString (String inString, int istart, int iend) 
	{
		String outString = inString;
		String frontStr;
		String endStr = "";
		
		// Remove the substring
		if ((istart != -1) && (iend != -1))
		{
			frontStr = inString.substring(0, istart-1);
			if (iend < inString.length())
			{
				endStr = inString.substring(iend+1);
			}
			outString = frontStr + endStr;
			return outString;
		}
		else
		{
			// sStr is not in the input string
			return inString;
		}
	}
	
	
	static public String matchInsertString (String inStr, String matchStr, String insertStr)
	{
		StringBuffer outStr = new StringBuffer(inStr);
		int iindex = inStr.indexOf(matchStr);
		
		if (iindex != -1)
		{
			// Found the match string.
			int jindex = iindex +  matchStr.length();
			outStr = outStr.insert(jindex, insertStr);
		}
	
		return (new String(outStr));
	}
	
	
}
