package com.zhicloud.ms.app.propeties;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

public class Properties
{

	// 属性列表
	private List<Property> propertyList = new ArrayList<Property>();

	// 做为parameterList的索引用,可以快速查找
	private Map<String, Property> propertyMap = new HashMap<String, Property>();

	/*
	 * 
	 */
	public synchronized int size()
	{
		return this.propertyList.size();
	}

	/*
	 * 
	 */
	public synchronized Property getPropertyAt(int index)
	{
		return this.propertyList.get(index);
	}

	/*
	 * 
	 */
	public synchronized Property getProperty(String key)
	{
		Property parameter = this.propertyMap.get(key);
		return parameter;
	}

	/*
	 * 
	 */
	public synchronized void addProperty(Property parameter)
	{
		this.propertyMap.put(parameter.getKey(), parameter);
		this.propertyList.add(parameter);
	}

	/*
	 * 
	 */
	public synchronized String getValue(String key)
	{
		return getValue(key, null);
	}

	/*
	 * 
	 */
	public synchronized String getValue(String key, String defaultValue)
	{
		Property property = getProperty(key);
		return (property == null) ? defaultValue : property.getValue();
	}

	/*
	 * 
	 */
	public synchronized String getValueAt(int index)
	{
		return getPropertyAt(index).getValue();
	}

	/*
	 * 
	 */
	public synchronized String getValueAt(int index, String defaultValue)
	{
		String value = getValueAt(index);
		return (value == null) ? defaultValue : value;
	}

	/*
	 * 
	 */
	public synchronized Document toXML()
	{
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("properties");
		for( Property property : propertyList )
		{
			Element propertyElement = root.addElement("property");
			propertyElement.addAttribute("key", property.getKey());
			propertyElement.addAttribute("value", property.getValue());
		}
		return doc;
	}

	/*
	 * 
	 */
	public synchronized String toXMLString()
	{
		return toXML().asXML();
	}

	/*
	 * 
	 */
	public synchronized byte[] toXMLBytes(String encoding) throws UnsupportedEncodingException, IOException
	{
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		new XMLWriter(bos, new OutputFormat("\t", true, encoding)).write(toXML());
		return bos.toByteArray();
	}

	// -------------

}
