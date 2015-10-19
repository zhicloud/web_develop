package com.zhicloud.ms.app.listener;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;

public class AllResourceMonitorListener implements Servlet {

	private final static Logger logger = Logger.getLogger(AllResourceMonitorListener.class);

	@Override
	public void init(ServletConfig config) throws ServletException {
		logger.info("init all service fetcher listers");
		try {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(1);
			if(channel!=null){
				JSONObject result = channel.startSystemMonitor();
				if(result.getString("status").equals("success")){
					logger.info(String.format("---start platform monitor success---"));
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		logger.info("destory all the service fetcher listers");
	}

	@Override
	public ServletConfig getServletConfig() {
		throw new AppException("unsupported method");
	}

	@Override
	public String getServletInfo() {
		throw new AppException("unsupported method");
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		throw new AppException("unsupported method");
	}

}
