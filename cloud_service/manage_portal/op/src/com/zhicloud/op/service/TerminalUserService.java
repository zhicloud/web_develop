package com.zhicloud.op.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.TerminalUserVO;

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
	
	public String agentViewVpcPage(HttpServletRequest request,HttpServletResponse response);
	
	public void viewVpc(HttpServletRequest request,HttpServletResponse response);
	
	public String agentViewDiskPage(HttpServletRequest request,HttpServletResponse response);
	
	public void viewDisk(HttpServletRequest request,HttpServletResponse response);
	/**
	 * 
	 * agentRechargeForTerminalUser: 代理商为用户续费
	 *
	 * @author sasa
	 * @param userId
	 * @param fee
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult  agentRechargeForTerminalUser(String userId, String fee);
	
	/**
	 * 跳转到用户详情页
	 * @param request
	 * @param response
	 * @return
	 */
	public String userDetailPage(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * @Description:查询代理商所属用户信息
	 * @param request
	 * @param response
	 * @throws
	 */
    public void queryUserByAgentID(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * @Description:批量设置用户标签信息
     * @param terminalUserIds 用户ID数组
     * @param markID 标签ID
     * @return
     */
    public MethodResult updateTerminalUserWithMarkID(Map<String, Object> parameter);

    /**
     * @Description:跳转标签设置窗口
     * @param request
     * @param response
     * @return String
     */
    public String setMarkPage(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * @Description:跳转现金券发放窗口
     * @param request
     * @param response
     * @return String
     */
    public String operatorCashCouponPage(HttpServletRequest request,HttpServletResponse response); 

    /**
     * @Description:发放现金券到手机
     * @param parameter
     * @return
     */
    public MethodResult sendCashCouponByPhone(Map<String, String> parameter);
    
    /**
     * @Description:发放现金券到邮箱
     * @param parameter
     * @return
     */
    public MethodResult sendCashCouponByEmail(Map<String, String> parameter);
    
    /**
     * @Description:查询所有终端用户数据
     * @param request
     * @param response
     * @return List<TerminalUserVO>
     */
    public  List<TerminalUserVO> queryTerminalUserForExport(HttpServletRequest request, HttpServletResponse response);

    /**
     * @Description:查询符合条件的终端用户的消费用户
     * @param request
     * @param response
     * @param condition
     * @return
     */
    public List<TerminalUserVO> queryTerminalUserForConsum(HttpServletRequest request, HttpServletResponse response,
            Map<String, Object> condition);
}
