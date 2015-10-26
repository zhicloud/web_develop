package com.zhicloud.op.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;

public interface TerminalUserService
{

	// --------------------

	public String managePageForOperator(HttpServletRequest request, HttpServletResponse response);
	
	public String managePageForAgent(HttpServletRequest request, HttpServletResponse response);

	public String addPage(HttpServletRequest request, HttpServletResponse response);

	public String modPage(HttpServletRequest request, HttpServletResponse response);
	
	public String activated(HttpServletRequest request, HttpServletResponse response);
	
	public String setStrToResetPasswordPage(HttpServletRequest request, HttpServletResponse response);
	
	public String resetPasswordPage(HttpServletRequest request,HttpServletResponse response);
	
	public String baseInfoPageAccountEdit(HttpServletRequest request,HttpServletResponse response);
	
	public String baseInfoPageEmailEdit(HttpServletRequest request,HttpServletResponse response);
	
	public String baseInfoPagePhoneEdit(HttpServletRequest request,HttpServletResponse response);
	
	public String terminalUserChangeEmail(HttpServletRequest request,HttpServletResponse response);
	
	public String changePasswordPage(HttpServletRequest request, HttpServletResponse response);
	
	public String uerForgetPassword(HttpServletRequest request, HttpServletResponse response);
	
	// --------------

	public void queryTerminalUserForOperator(HttpServletRequest request, HttpServletResponse response);
	
	public void queryTerminalUserForAgent(HttpServletRequest request, HttpServletResponse response);

	public boolean queryTerminalUserByName(String terminalUserName);

	// --------------------

	public MethodResult addTerminalUser(Map<String, String> parameter);

	public MethodResult updateTerminalUserById(Map<String, Object> parameter);

	public MethodResult deleteTerminalUserByIds(List<String> terminalUserIds);
	
	public MethodResult updateBaseInfo(Map<String, Object> parameter);
	
	public MethodResult updateBaseInfoAccount(Map<String, Object> parameter);
	
	public MethodResult updateBaseInfoEmail(Map<String, Object> parameter);
	
	public MethodResult updateBaseInfoEmailSendCode(Map<String, Object> parameter);
	
	public MethodResult updateBaseInfoEmailSendCodeAgain(Map<String, Object> parameter);
	
	public MethodResult updateBaseInfoPhone(Map<String, Object> parameter);
	
	public MethodResult updateBaseInfoPhoneEmailCode(Map<String, Object> parameter);
	
	public MethodResult updateBaseInfoPhoneCheckOldMail(Map<String, Object> parameter);
	
//	public MethodResult updateBaseInfoPhoneCheckNewPhone(Map<String, Object> parameter);
	
	public MethodResult updateBaseInfoPhoneSendMessage(Map<String, String> parameter);
	
	public MethodResult changePasswordById(Map<String, Object> parameter);
	
	public MethodResult resetPasswordById(Map<String, Object> parameter);
	
	public MethodResult addTerminalUserWithoutLogin(Map<String, String> parameter);
	
	public MethodResult checkSmsRegister(Map<String, String> parameter);
	
	public MethodResult resetPasswordSendPhoneCode(Map<String, String> parameter);
	
	public MethodResult checkAccount(Map<String, String> parameter);
	
	public MethodResult optionChangePassword(Map<String, String> parameter);
	
	public MethodResult resetPasswordSendEmailCode(Map<String, String> parameter);
	
	public MethodResult resetPasswordBySendMail(Map<String, Object> parameter);
	
	public MethodResult resetPasswordCheckEmailOrPhoneCode(Map<String, String> parameter);
	
	public MethodResult resetPasswordByPhone(Map<String, Object> parameter);
	
	//-------------------
	public MethodResult changeEmailCheckEmailOrPhone(Map<String, String> parameter);
	
	public MethodResult changeEmailOrPhone(Map<String, String> parameter);
	
	public MethodResult getPhoneAndEmailForSendMessage(String userId, int day);

	public MethodResult informUserFeeInfo(String userId, String hostName, Date startTime, Date endTime, BigDecimal remainBalance, BigDecimal fee, BigDecimal monthlyPrice);
	
	public void sendHint();
	
	public String viewItemPage(HttpServletRequest request,HttpServletResponse response);
	
	public void viewItem(HttpServletRequest request,HttpServletResponse response);
}
