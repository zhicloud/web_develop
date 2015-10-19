package com.zhicloud.ms.app.propeties;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.zhicloud.ms.common.util.StringUtil;

public class WarAppPropertiesLoader
{
	
	private static final Logger logger = Logger.getLogger(WarAppPropertiesLoader.class);

	/**
	 * @param appPropertiesURL
	 *            : war包中的路径, /META-INF/app-properties.xml
	 * @throws IOException
	 * @throws UnsupportedEncodingException
	 */
	@SuppressWarnings("unchecked")
	public static void load(URL appPropertiesURL) throws DocumentException, UnsupportedEncodingException, IOException
	{
		Document doc = new SAXReader().read(appPropertiesURL);
		Element root = doc.getRootElement();
		Iterator<Element> propertyElementIter = root.elementIterator("property");
		while (propertyElementIter.hasNext())
		{
			Element paramElement = propertyElementIter.next();
			handleParamElement(paramElement);
		}
		try
		{
			copyValueToAppProperties(SaveAppPropertiesManager.loadAppProperties());
		}
		catch (DocumentException e)
		{
			logger.error("["+Thread.currentThread().getId()+"] ", e);
		}
		AppProperties.save();
	}

	/*
	 * 从<Property>节点读取各种属性
	 */
	@SuppressWarnings("unchecked")
	private static void handleParamElement(Element element)
	{
		Property property = new Property();
		property.setKey(StringUtil.trim(element.attributeValue("key")));
		property.setName(StringUtil.trim(element.attributeValue("name")));
		property.setType(StringUtil.trim(element.attributeValue("type")));
		property.setDescription(StringUtil.trim(element.attributeValue("description")));
		property.setFormat(StringUtil.trim(element.attributeValue("format")));
		//
		if ("String".equals(property.getType()))
		{
			property.setText(StringUtil.trim(element.getText()));
		}
		else if ("select".equals(property.getType()))
		{
			Property.Select select = new Property.Select();
			Iterator<Element> optionIter = element.element("select").elementIterator("option");
			while (optionIter.hasNext())
			{
				Element optionElement = optionIter.next();
				Property.Option option = new Property.Option();
				option.setValue(StringUtil.trim(optionElement.attributeValue("value")));
				option.setText(StringUtil.trim(optionElement.getText()));
				select.addOption(option);
			}
			property.setSelect(select);
		}
		property.initValue();
		AppProperties.addProperty(property);
	}

	/*
	 * 从已经保存的app-properties.xml中读取value, 然后copy给AppProperties
	 */
	private static void copyValueToAppProperties(Properties properties)
	{
		if (properties == null)
		{
			return;
		}
		for (int i = 0; i < AppProperties.size(); i++)
		{
			Property appProperty = AppProperties.getPropertyAt(i);
			Property property = properties.getProperty(appProperty.getKey());
			if (property != null)
			{
				appProperty.setValue(property.getValue());
			}
		}
	}
}