package com.zhicloud.op.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AgentAPIService {
	public void agentGetLogin(HttpServletRequest request, HttpServletResponse response);
	public void agentViewUserHost(HttpServletRequest request, HttpServletResponse response);
	public void agentCreateUserHost(HttpServletRequest request, HttpServletResponse response);
	public void agentAddUser(HttpServletRequest request,HttpServletResponse response);
	public void agentViewUser(HttpServletRequest request,HttpServletResponse response);
	public void agentModifyUser(HttpServletRequest request,HttpServletResponse response);
	public void agentViewAgent(HttpServletRequest request, HttpServletResponse response);
	public void agentModifyAgent(HttpServletRequest request, HttpServletResponse response);
	public void agentViewUserOper(HttpServletRequest request, HttpServletResponse response);
		public void agentGetImage(HttpServletRequest request, HttpServletResponse response);
	public void agentOperateUserHost(HttpServletRequest request, HttpServletResponse response);
	public void agentModifyUserHost(HttpServletRequest request, HttpServletResponse response);
	public void agentConsumption(HttpServletRequest request, HttpServletResponse response);
		public void agentViewUserPayExpenses(HttpServletRequest request, HttpServletResponse response);

}
