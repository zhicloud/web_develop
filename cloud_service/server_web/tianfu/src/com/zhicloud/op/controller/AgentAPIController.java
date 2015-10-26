package com.zhicloud.op.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
 

import com.zhicloud.op.core.CoreSpringContextManager; 
import com.zhicloud.op.service.AgentAPIService;

@Controller
public class AgentAPIController {
public static final Logger logger = Logger.getLogger(AdminController.class);
	

	@RequestMapping("/agentlogin.do")
	public void agentLogin(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
 		AgentAPIService agentAPIService = CoreSpringContextManager.getAgentAPIService();
 		agentAPIService.agentGetLogin(request, response);
	}
	@RequestMapping("/agentviewuserhost.do")
	public void agentViewUserHost(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
 		AgentAPIService agentAPIService = CoreSpringContextManager.getAgentAPIService();
 		agentAPIService.agentViewUserHost(request, response);
	}
	@RequestMapping("/agentadduser.do")
	public void agentAddUser(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
 		AgentAPIService agentAPIService = CoreSpringContextManager.getAgentAPIService();
 		agentAPIService.agentAddUser(request, response);
	}
	@RequestMapping("/agentcreateuserhost.do")
	public void agentCreateUserHost(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AgentAPIService agentAPIService = CoreSpringContextManager.getAgentAPIService();
		agentAPIService.agentCreateUserHost(request, response);
	}

	@RequestMapping("/agentviewuser.do")
	public void agentViewUser(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
 		AgentAPIService agentAPIService = CoreSpringContextManager.getAgentAPIService();
 		agentAPIService.agentViewUser(request, response);
	}
	
	@RequestMapping("/agentmodifyuser.do")
	public void agentModifyUser(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
 		AgentAPIService agentAPIService = CoreSpringContextManager.getAgentAPIService();
 		agentAPIService.agentModifyUser(request, response);
	}
	
	@RequestMapping("/agentviewagent.do")
	public void agentViewAgent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
 		AgentAPIService agentAPIService = CoreSpringContextManager.getAgentAPIService();
 		agentAPIService.agentViewAgent(request, response);
	}
	
	@RequestMapping("/agentmodifyagent.do")
	public void agentModifyAgent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
 		AgentAPIService agentAPIService = CoreSpringContextManager.getAgentAPIService();
 		agentAPIService.agentModifyAgent(request, response);
	}
	@RequestMapping("/agentviewuseroper.do")
	public void agentViewUserOper(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AgentAPIService agentAPIService = CoreSpringContextManager.getAgentAPIService();
		agentAPIService.agentViewUserOper(request, response);
	}
		@RequestMapping("/agentgetimage.do")
	public void agentGetImage(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
 		AgentAPIService agentAPIService = CoreSpringContextManager.getAgentAPIService();
 		agentAPIService.agentGetImage(request, response);
	}
	@RequestMapping("/agentoperateuserhost.do")
	public void agentOperateUserHost(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
 		AgentAPIService agentAPIService = CoreSpringContextManager.getAgentAPIService();
 		agentAPIService.agentOperateUserHost(request, response);
	}
	@RequestMapping("/agentmodifyuserhost.do")
	public void agentModifyUserHost(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
 		AgentAPIService agentAPIService = CoreSpringContextManager.getAgentAPIService();
 		agentAPIService.agentModifyUserHost(request, response);
	}
	@RequestMapping("/agentconsumption.do")
	public void agentConsumption(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		AgentAPIService agentAPIService = CoreSpringContextManager.getAgentAPIService();
		agentAPIService.agentConsumption(request, response);
	}
	@RequestMapping("/agentviewuserpayexpenses.do")
	public void agentViewUserPayExpenses(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
 		AgentAPIService agentAPIService = CoreSpringContextManager.getAgentAPIService();
 		agentAPIService.agentViewUserPayExpenses(request, response);
	}
}
