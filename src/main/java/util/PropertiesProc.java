package util;

import java.util.*;

public class PropertiesProc
{
	Properties props = null;
	
	String propertiesFileName = null;
	
  public PropertiesProc ()
  {
      
  }
	
  /**
   * 
   * @param propertiesFileName - relative to WebFileRoot
   */
  public PropertiesProc (String propertiesFileName)
  {
      this.propertiesFileName = propertiesFileName;
  }
  
/**
 * 
 * @param key - name of property key
 * @return String - property value
 */	
  public String readProperty
  	(String key) 
  {
 	String filePath = null;
 	String filePathLinux;
	
	try
	{
		if (props == null)
		{
			java.io.FileInputStream is = null;
			if (Util.getWebRoot() != null) 
			{
				filePath = 
				    (Util.getWebRoot() + "/" + propertiesFileName);		
			}
			
			is = new java.io.FileInputStream(filePath);
			
			props = new Properties();
			props.load(is);
		}
		return props.getProperty(key);
	}
	catch (Exception e)
	{
		System.out.println("Could NOT read properties file: " + filePath);
		System.out.println(e.getMessage());
		return null;
	}	
  }


}

