package com.zhicloud.op.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.service.ClientService;

@Controller
public class EasyInterfaceController
{
	
	private static final Logger logger = Logger.getLogger(EasyInterfaceController.class);

	@RequestMapping("/easyInterface/client.do")
	public void client(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.info("EasyInterfaceController.client() > ["+Thread.currentThread().getId()+"] ");
		String command = request.getParameter("command");
		// 鉴权
		if( "authenticate".equals(command) )
		{
			authenticate(request, response);
			return;
		}
		// 获取用户云主机
		else if( "get_cloud_host".equals(command) )
		{
			getCloudHost(request, response);
			return;
		}
		//云主机操作
		else if ("cloud_host_operation".equals(command))
		{
			cloudHostOperation(request,response);
			return;
		}
		//编辑云主机
		else if ("cloud_host_change".equals(command)) 
		{
			cloudHostChange(request,response);
			return;
		}
		//云主机每个月价格
		else if ("get_cloud_host_price".equals(command)) 
		{
			getCloudHostPrice(request,response);
			return;
		}
		//获取客户端版本号
		else if ("get_client_version".equals(command)) 
		{
			getClientVersion(request,response);
			return;
		}
		//更改云主机显示名
		else if ("change_display_name".equals(command)) 
		{
			changeDisplayName(request,response);
			return;
		}
		else
		{
			throw new AppException("wrong command: "+command);
		}
	}
	
	private void authenticate(HttpServletRequest request, HttpServletResponse response)
	{
		logger.info("EasyInterfaceController.authenticate() > ["+Thread.currentThread().getId()+"] received request");
		ClientService clientService = CoreSpringContextManager.getClientService();
		clientService.authenticate(request, response);
	}
	
	private void getCloudHost(HttpServletRequest request, HttpServletResponse response)
	{
		logger.info("EasyInterfaceController.getCloudHost() > ["+Thread.currentThread().getId()+"] received request");
		ClientService clientService = CoreSpringContextManager.getClientService();
		clientService.getCloudHost(request, response);
	}

	private void cloudHostOperation(HttpServletRequest request, HttpServletResponse response)
	{
		logger.info("EasyInterfaceController.cloudHostOperation() > ["+Thread.currentThread().getId()+"] received request");
		ClientService clientService = CoreSpringContextManager.getClientService();
		clientService.cloudHostOperation(request, response);
	}
	
	private void cloudHostChange(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.info("EasyInterfaceController.cloudHostChange() > ["+Thread.currentThread().getId()+"] received request");
		ClientService clientService = CoreSpringContextManager.getClientService();
		clientService.cloudHostChange(request, response);
	}
	
	private void getCloudHostPrice(HttpServletRequest request,HttpServletResponse response)
	{
		logger.info("EasyInterfaceController.getCloudHostPrice() > ["+Thread.currentThread().getId()+"] received request");
		ClientService clientService = CoreSpringContextManager.getClientService();
		clientService.getCloudHostPrice(request, response);
	}
	
	private void getClientVersion(HttpServletRequest request, HttpServletResponse response)
	{
		logger.info("EasyInterfaceController.getCloudHostPrice() > ["+Thread.currentThread().getId()+"] received request");
		ClientService clientService = CoreSpringContextManager.getClientService();
		clientService.getClientVersion(request, response);
	}
	
	private void changeDisplayName(HttpServletRequest request, HttpServletResponse response)
	{ 
		logger.info("EasyInterfaceController.changeDisplayName() > ["+Thread.currentThread().getId()+"] received request");
		ClientService clientService = CoreSpringContextManager.getClientService();
		clientService.changeDisplayName(request, response);
	}
}
