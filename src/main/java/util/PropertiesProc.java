package util;

import java.io.IOException;
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

		try
		{
			if (props == null)
			{
				loadPropsViaStream();
			}
			return props.getProperty(key);
		}
		catch (Exception e)
		{
			System.out.println("Could NOT read properties file: " + propertiesFileName);
			System.out.println(e.getMessage());
			return null;
		}
	}

	private void loadPropsViaStream() throws IOException {
		props = new Properties();

		String filePath = null;
		java.io.InputStream is = PropertiesProc.class.getResourceAsStream("/" + propertiesFileName);
		if (is != null) {
			props.load(is);
		}
		else {
			throw new IOException("Failed to create stream for " + propertiesFileName);
		}

	}

	@SuppressWarnings("unused")
	private void loadPropsViaFile() throws IOException {
		String filePath = null;
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
}

