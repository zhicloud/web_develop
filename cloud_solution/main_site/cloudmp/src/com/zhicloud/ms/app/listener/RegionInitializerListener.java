package com.zhicloud.ms.app.listener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader; 

import com.zhicloud.ms.app.propeties.AppProperties;
import com.zhicloud.ms.exception.MyException;
import com.zhicloud.ms.httpGateway.HttpGatewayReceiveChannel;
import com.zhicloud.ms.util.RegionHelper;
import com.zhicloud.ms.util.RegionHelper.RegionData;

public class RegionInitializerListener implements ServletContextListener
{

	public static final Logger logger = Logger.getLogger(RegionInitializerListener.class);
	
	@SuppressWarnings("unchecked")
	@Override
	public void contextInitialized(ServletContextEvent event)
	{
		logger.info("RegionInitializerListener.contextInitialized() > ["+Thread.currentThread().getId()+"] ");
		try
		{
			Document doc = new SAXReader().read(event.getServletContext().getResource("/META-INF/regions.xml"));
			Element root = doc.getRootElement();
			Iterator<Element> regionIter = root.elementIterator("region");
			while (regionIter.hasNext())
			{
				Element paramElement = regionIter.next();
				
				// 构造region数据
				RegionData regionData = new RegionData();
				regionData.setId(Integer.valueOf(paramElement.attributeValue("id")));
				regionData.setName(paramElement.attributeValue("name"));
				regionData.setHttpGatewayAddr(paramElement.attributeValue("http_gateway_addr"));
				RegionHelper.singleton.putRegionData(regionData);
				
				// 向http gateway注册消息推送地址
				String addressOfThisSystem = AppProperties.getValue("address_of_this_system");
				if( addressOfThisSystem.endsWith("/")==false )
				{
					addressOfThisSystem += "/";
				}
				String registerUrl = addressOfThisSystem + "hgMessage/receive.do";
				HttpGatewayReceiveChannel receiveChannel = new HttpGatewayReceiveChannel(regionData.getId());
				receiveChannel.messagePushRegisterThreadly(registerUrl);
			}
		}
		catch (MyException e)
		{
			logger.error("RegionInitializerListener.contextInitialized() > ["+Thread.currentThread().getId()+"] ",e);
			throw e;
		} catch (MalformedURLException e) { 
			e.printStackTrace();
			
		} catch (IOException e) { 
			e.printStackTrace();
			
		} catch (DocumentException e) { 
			e.printStackTrace();
			
		} 
	}
	

	@Override
	public void contextDestroyed(ServletContextEvent arg0)
	{
		logger.info("RegionInitializerListener.contextDestroyed() > ["+Thread.currentThread().getId()+"] ");
	}


	

}
