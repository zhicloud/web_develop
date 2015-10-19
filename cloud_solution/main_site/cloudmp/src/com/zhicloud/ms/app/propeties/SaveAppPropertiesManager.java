package com.zhicloud.ms.app.propeties;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.zhicloud.ms.app.helper.AppHelper;
import com.zhicloud.ms.common.util.FileUtil;
import com.zhicloud.ms.common.util.StringUtil;

public class SaveAppPropertiesManager {
	
	public static final File app_properties_save_file = new File(AppHelper.getServerHome(), "projects/"+AppHelper.APP_NAME+"/app-properties/app-properties.xml");
	
	//-------------------
	
	public static void saveAppProperties(Properties properties) throws UnsupportedEncodingException, IOException
	{
		synchronized (app_properties_save_file)
		{
			FileUtil.loopSaveToFile(properties.toXMLBytes("utf-8"), app_properties_save_file, 10);
		}
	}
		
	//-------------------
	
	@SuppressWarnings("unchecked")
	public static Properties loadAppProperties() throws DocumentException
	{
		Properties properties = new Properties();
		Document doc = null;
		synchronized (app_properties_save_file)
		{
			if( app_properties_save_file.exists()==false )
			{
				return null;
			}
			doc = new SAXReader().read(app_properties_save_file);
		}
		Element root = doc.getRootElement();
		Iterator<Element> propertyElementIter = root.elementIterator("property");
		while( propertyElementIter.hasNext() )
		{
			Element paramElement = propertyElementIter.next();
			handleParamElement(paramElement, properties);
		}
		return properties;
	}
	
	private static void handleParamElement(Element element, Properties properties)
	{
		Property property = new Property();
		property.setKey( StringUtil.trim(element.attributeValue("key")) );
		property.setValue( StringUtil.trim(element.attributeValue("value")) );
		properties.addProperty(property);
	}
	
	//-------------------
}