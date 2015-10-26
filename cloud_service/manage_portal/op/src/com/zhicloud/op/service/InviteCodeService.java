package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;

public interface InviteCodeService 
{
	public String terminalInviteCodePage(HttpServletRequest request,HttpServletResponse response);
	
	public String agentInviteCodePage(HttpServletRequest request,HttpServletResponse response);
	
	public String operatorInviteCodePage(HttpServletRequest request,HttpServletResponse response);
	
	public String addInviteCodePageByAgent(HttpServletRequest request,HttpServletResponse response);
	
	public String addInviteCodePageByOperator(HttpServletRequest request,HttpServletResponse response);
	
	public String sendInviteCodeByEmailPage(HttpServletRequest request, HttpServletResponse response);
	
	public String sendInviteCodeByPhonePage(HttpServletRequest request, HttpServletResponse response);
	
	public String sendInviteCodeByEmailTerminalUserPage(HttpServletRequest request, HttpServletResponse response);
	
	public String sendInviteCodeByPhoneTerminalUserPage(HttpServletRequest request, HttpServletResponse response);
	
	//------------------------------------------
	
	public void queryInviteCodeForTerminalUser(HttpServletRequest request, HttpServletResponse response);
	
	public void queryInviteCodeForAgent(HttpServletRequest request, HttpServletResponse response);
	
	public void queryInviteCodeForOperator(HttpServletRequest request, HttpServletResponse response);
	
	//------------------------------------------
	public MethodResult addInviteCode(Map<String, String> parameter);
	
	public MethodResult deleteInviteCodeByIds(List<String> InviteCodeIds);
	
	public MethodResult sendInviteCodeByPhone(Map<String, String> parameter);
	
	public MethodResult sendInviteCodeByEmail(Map<String, String> parameter);
}
