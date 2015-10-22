package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;


public interface AgentService {

	// --------------------

		public String managePage(HttpServletRequest request,HttpServletResponse response);

		public String addPage(HttpServletRequest request,HttpServletResponse response);

		public String modPage(HttpServletRequest request,HttpServletResponse response);
		
		public String toRecharge(HttpServletRequest request,HttpServletResponse response);
		
		public String basicInformationPage(HttpServletRequest request,HttpServletResponse response);
		
		public String updatePasswordPage(HttpServletRequest request,HttpServletResponse response);
		
		public String resetPasswordPage(HttpServletRequest request,HttpServletResponse response);
		
		public String rechargePage(HttpServletRequest request,HttpServletResponse response);
		
		// --------------

		public void queryAgent(HttpServletRequest request,HttpServletResponse response);

		public boolean queryAgentByAccount(String account);

		// --------------------

		public MethodResult addAgent(Map<String, String> parameter);

		public MethodResult updateAgentById(Map<String, Object> parameter);

		public MethodResult deleteAgentById(String agentId);

		public MethodResult deleteAgentByIds(List<?> agentIds);
		
		public MethodResult updateBasicInformation(Map<String, Object> parameter);
		
		public MethodResult updatePasswordById(Map<String, Object> parameter); 
		
		public MethodResult resetPasswordById(Map<String, Object> parameter); 
		
		public void CheckBalanceForAgent();
		
		public MethodResult optionChangePassword(Map<String, String> parameter);
		
		public MethodResult resetPasswordSendEmailCode(Map<String, String> parameter);
		
		public MethodResult resetPasswordSendPhoneCode(Map<String, String> parameter);
		
		public MethodResult resetPasswordCheckEmailOrPhoneCode(Map<String, String> parameter);
		
		public String baseInfoPageEmailEdit(HttpServletRequest request,HttpServletResponse response);
		
		public MethodResult updateBaseInfoEmail(Map<String, Object> parameter);
		
		public MethodResult changeEmailOrPhone(Map<String, String> parameter);
		
		public MethodResult updateBaseInfoEmailSendCode(Map<String, Object> parameter);
		
		public MethodResult updateBaseInfoEmailSendCodeAgain(Map<String, Object> parameter);
		
		public MethodResult changeEmailCheckEmailOrPhone(Map<String, String> parameter);
		
		public String baseInfoPagePhoneEdit(HttpServletRequest request,HttpServletResponse response);
		
		public MethodResult updateBaseInfoPhoneEmailCode(Map<String, Object> parameter);
		
		public MethodResult updateBaseInfoPhoneSendMessage(Map<String, String> parameter);
		
		public MethodResult updateBaseInfoPhone(Map<String, Object> parameter);
		
		public MethodResult operatorRecharge(Map<String, Object> parameter);
		
		
}
