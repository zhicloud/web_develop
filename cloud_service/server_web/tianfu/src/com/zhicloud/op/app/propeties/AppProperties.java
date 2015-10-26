package com.zhicloud.op.app.propeties;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class AppProperties
{

	private static Properties properties = new Properties();

	/*
	 * 
	 */
	public synchronized static int size()
	{
		return properties.size();
	}

	/*
	 * 
	 */
	public synchronized static Property getPropertyAt(int index)
	{
		return properties.getPropertyAt(index);
	}

	/*
	 * 
	 */
	public synchronized static Property getProperty(String key)
	{
		return properties.getProperty(key);
	}

	/*
	 * 
	 */
	public synchronized static void addProperty(Property parameter)
	{
		properties.addProperty(parameter);
	}

	/*
	 * 
	 */
	public synchronized static String getValue(String key, String defaultValue)
	{
		return properties.getValue(key, defaultValue);
	}

	/*
	 * 
	 */
	public synchronized static String getValue(String key)
	{
		return properties.getValue(key);
	}
	
	/*
	 * 
	 */
	public synchronized static void setValue(String key, String value)
	{
		Property property = properties.getProperty(key);
		if( property!=null )
		{
			property.setValue(value);
		}
	}

	/*
	 * 
	 */
	public synchronized static String toXMLString()
	{
		return properties.toXMLString();
	}

	/*
	 * 
	 */
	public synchronized static void save() throws UnsupportedEncodingException, IOException
	{
		SaveAppPropertiesManager.saveAppProperties(properties);
	}

}
