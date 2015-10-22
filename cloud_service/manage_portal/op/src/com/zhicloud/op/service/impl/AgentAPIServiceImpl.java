package com.zhicloud.op.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map; 
import java.util.regex.Pattern;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;  

import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.FlowUtil;



import com.zhicloud.op.agentapi.vo.PayExpenses;
import com.zhicloud.op.agentapi.vo.AgentConsumption;
import com.zhicloud.op.agentapi.vo.OperLogForApiVO;
import com.zhicloud.op.agentapi.vo.Result;
import com.zhicloud.op.app.helper.CloudHostBillingHelper;
import com.zhicloud.op.app.helper.CloudHostPrice;
import com.zhicloud.op.app.helper.LoginHelper; 
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.RandomPassword;
import com.zhicloud.op.common.util.SendMail;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.XMLUtil;
import com.zhicloud.op.common.util.constant.MailConstant;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.AccountBalanceDetailMapper;
import com.zhicloud.op.mybatis.mapper.AgentMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostBillDetailMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
import com.zhicloud.op.mybatis.mapper.OperLogMapper;
import com.zhicloud.op.mybatis.mapper.SysDiskImageMapper;
import com.zhicloud.op.mybatis.mapper.SysUserMapper; 
import com.zhicloud.op.mybatis.mapper.TerminalUserMapper; 
import com.zhicloud.op.mybatis.mapper.UserMessageMapper; 
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.AgentAPIService; 
import com.zhicloud.op.service.CloudHostService;
import com.zhicloud.op.service.PaymentService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.service.constant.AppInconstant; 
import com.zhicloud.op.vo.AccountBalanceDetailVO;
import com.zhicloud.op.vo.OperLogVO;
import com.zhicloud.op.vo.SysDiskImageVO; 
import com.zhicloud.op.vo.AgentVO;
import com.zhicloud.op.vo.CloudHostBillDetailVO;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.SysDiskImageVO;
import com.zhicloud.op.vo.SysUserVO;
import com.zhicloud.op.vo.TerminalUserVO; 

@Transactional(readOnly = true)
public class AgentAPIServiceImpl implements AgentAPIService {
	public static final Logger logger = Logger.getLogger(AgentServiceImpl.class);

	private SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}  
	public void agentGetLogin(HttpServletRequest request, HttpServletResponse response) {
		try{ 
			logger.info("AgentAPIServiceImpl.agentGetLogin()");
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
 			String name     = StringUtil.trim(request.getParameter("name"));
			String password        = StringUtil.trim(request.getParameter("password")); 
			Result result = new Result();
			if(StringUtil.isBlank(name)){
				result.setStatus("failure");
				result.setMessage("'name' is required");
				result.setErrorCode("1");
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
				
			}
			if(StringUtil.isBlank(password)){
				result.setStatus("failure");
				result.setMessage("'password' is required");
				result.setErrorCode("1");
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
				
			}
			SysUserMapper sysUserMapper     = sqlSession.getMapper(SysUserMapper.class);
 			
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("type",     AppConstant.SYS_USER_TYPE_AGENT);
			condition.put("account",  name);
			condition.put("password", password); 
			
			SysUserVO sysUserVO = sysUserMapper.getByTypeAndAccountAndPassword(condition);
			if( sysUserVO==null ) {
	            result.setStatus("failure");
	            result.setErrorCode("2"); 
	            result.setMessage("'name' or 'password' is wrong");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}else{
				if( sysUserVO.getStatus() == AppConstant.AGENT_STATUS_DISABLED )
				{ 
					result.setStatus("failure");
					result.setErrorCode("3"); 
					result.setMessage("account is disabled ");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				if( sysUserVO.getStatus() == AppConstant.AGENT_STATUS_END )
				{ 
					result.setStatus("failure");
					result.setErrorCode("3"); 
					result.setMessage("account is end");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
 				}
				if( sysUserVO.getType() == AppConstant.SYS_USER_TYPE_AGENT){
					//循环登录信息，如果已经登录，销毁原有登录
					synchronized(AppInconstant.agentLogInfo){
						for (Iterator it =  AppInconstant.agentLogInfo.keySet().iterator();it.hasNext();)
					   {	
						    Object key = it.next();
						    LoginInfo loginInfo = (LoginInfo) AppInconstant.agentLogInfo.get(key);
						    if(loginInfo.getUserId().equals(sysUserVO.getId())){
						    	AppInconstant.agentLogInfo.remove(key);  
						    	logger.info(loginInfo.getAccount() + "user  duplicate entries,remove "+key);
						    }
					   } 
		        	}					
					// 将用户信息设置到LoginInfo，存放于session
					LoginInfo loginInfo = new LoginInfo(true);
					loginInfo.setUserId(sysUserVO.getId());
					loginInfo.setGroupId(sysUserVO.getGroupId());
					loginInfo.setUserType(sysUserVO.getType());
					loginInfo.setAccount(sysUserVO.getAccount());   
					String sessionId = StringUtil.generateUUID();
					LoginHelper.setAgentLoginInfo(sessionId, loginInfo);
					 
					result.setStatus("success"); 
					result.setSessionId(sessionId);
					result.setMessage("login success");   
					response.getWriter().append(XMLUtil.javaToXml(result));	 
//					logger.info("++++++++"+AppInconstant.agentLogInfo.size()+"++++++++++++");
					//定时删除session
					this.setTimeToRemoveSession(sessionId, (long)1800000);
					return ; 
				}else{					 
					result.setStatus("failure");
					result.setErrorCode("2"); 
					result.setMessage("'name' or 'password' is wrong");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				
			}
			 
			
		} 
	catch (Exception e)
	{
		logger.error("ClientServiceImpl.authenticate()",e);
		throw new AppException("失败");
	}

	}
	
	private void setTimeToRemoveSession(String sessionId,long delay){
		Timer timer;
		timer = new Timer(true); 
		timer.schedule(new ReMoveAgentSession(sessionId) , delay);
	}
	
	/**
	 * 时间任务器
	 * 
	 */
	class ReMoveAgentSession extends TimerTask { 
		private String id;
		public ReMoveAgentSession(String sessionId){
			 id = sessionId;
		}
		
	    /**
	     * 把要定时执行的任务就在run中
	     */
	    public void run() {
	        try { 
	        	synchronized(AppInconstant.agentLogInfo){
	        		AppInconstant.agentLogInfo.remove(id);
	        		logger.info("agent login out-of-date "+id);
	        	} 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	}
	@Override
	public void agentViewUserHost(HttpServletRequest request,HttpServletResponse response) {
		try{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
 			String sessionId = StringUtil.trim(request.getParameter("session_id"));
			String userId    = StringUtil.trim(request.getParameter("user_id")); 
			if(StringUtil.isBlank(sessionId)){
				Result result = new Result();
				result.setStatus("failure");
				result.setErrorCode("3"); 
				result.setMessage("'session_id' is required");   
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			LoginInfo loginInfo = LoginHelper.getAgentLoginInfo(sessionId);
			CloudHostMapper cloudHostMapper = sqlSession.getMapper(CloudHostMapper.class);
			TerminalUserMapper terminalUserMapper = sqlSession.getMapper(TerminalUserMapper.class);
			if(loginInfo==null){
				Result result = new Result();
				result.setStatus("failure");
				result.setErrorCode("1");
				result.setMessage("login out-of-date");
				response.getWriter().append(XMLUtil.javaToXml(result));
				return;
			}else{
				if(userId==null || StringUtil.isBlank(userId)){
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("2");
					result.setMessage("'user_id' is required");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserById(userId);
				if(!(terminalUser!=null && terminalUser.getBelongingId()!=null && terminalUser.getBelongingId().equals(loginInfo.getUserId()))){
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("3");
					result.setMessage("The user is not found");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(userId);
				List<CloudHostVO> newCloudHostList = new ArrayList<CloudHostVO>();
				for(CloudHostVO cloudHost : cloudHostList){
					if(cloudHost.getType() == 2){
						newCloudHostList.add(cloudHost);
					}
				}
				Result result = new Result();
				result.setStatus("success");
				result.setCount(newCloudHostList.size()+"");
				result.setCloudHostList(newCloudHostList);
				response.getWriter().append(XMLUtil.javaToXml(result));
				return;
			}
		} 
		catch (Exception e)
		{
			logger.error("ClientServiceImpl.authenticate()",e);
			throw new AppException("失败");
		}
	} 
	/**
	 * 代理商为用户创建云主机
	 */
	@Transactional(readOnly = false)
	public void agentCreateUserHost(HttpServletRequest request, HttpServletResponse response) { 
		try{ 
			logger.info("AgentAPIServiceImpl.agentCreateUserHost()");
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
			String sessionId     = StringUtil.trim(request.getParameter("session_id"));
 			String userId     = StringUtil.trim(request.getParameter("user_id"));
			String cpu        = StringUtil.trim(request.getParameter("cpu_core")); 
			String sysImageId        = StringUtil.trim(request.getParameter("sys_image_id")); 
			String dataDisk        = StringUtil.trim(request.getParameter("data_disk")); 
			String bandwidth        = StringUtil.trim(request.getParameter("bandwidth")); 
			String memory        = StringUtil.trim(request.getParameter("memory")); 
			String region        = StringUtil.trim(request.getParameter("region"));
			String displayName        = StringUtil.trim(request.getParameter("host_name"));
			Result result = new Result();
			if(StringUtil.isBlank(sessionId)){
				result.setStatus("failure");
				result.setErrorCode("1"); 
				result.setMessage("'session_id' is required");   
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}else{
				LoginInfo loginInfo = LoginHelper.getAgentLoginInfo(sessionId);
				if(loginInfo==null){  
					result.setStatus("failure");
					result.setErrorCode("2"); 
					result.setMessage("login out-of-date");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
					
				}
				if(StringUtil.isBlank(userId)){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'user_id' is required");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
					
				}
				if(StringUtil.isBlank(displayName)){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'host_name' is required");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
					
				}
				if(displayName.length()==0||displayName.length()>30){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'host_name' length less than 30 ");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
					
				}
				TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
				
				TerminalUserVO termialUser = terminalUserMapper.getTerminalUserById(userId);
				Map<String, Object> parameter = new LinkedHashMap<String, Object>();
				parameter.put("displayName", displayName);
				if(termialUser==null){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("not find account by user_id");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				} 
				parameter.put("userId", userId);
				if(StringUtil.isBlank(region)){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'region' is required");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				} 
				if(!(StringUtil.isInt(region))){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'region' must be int");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				}  
				if(!(region.equals("1")||region.equals("4"))){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'region' is 1 or 4");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				}  
				parameter.put("region", region);
				if(StringUtil.isBlank(cpu)){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'cpu' is required");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				} 
				if(!(StringUtil.isInt(cpu))){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'cpu' must be int");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				}  
				String [] test = new String[] {  
						"1","2","4" ,"8","12","16"
				};
				List<String> tempList = Arrays.asList(test);
				if(!(tempList.contains(cpu))){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'cpu_core' value is wrong,should be 1,2,4,8,12,16");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				} 
				parameter.put("cpu", cpu);
				if(StringUtil.isBlank(sysImageId)){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'sys_image_id' is required");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				}
				SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
				SysDiskImageVO sysDiskImage = sysDiskImageMapper.getById(sysImageId);
				if(sysDiskImage==null){					
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("not find information by 'sys_image_id'");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				}
				parameter.put("sysImageId", sysImageId);
				
				if(StringUtil.isBlank(dataDisk)){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'data_disk' is required");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				} 
				if(!(StringUtil.isInt(dataDisk))){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'data_disk' must be int");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				}   
				if(dataDisk.length()>4 || Integer.parseInt(dataDisk)>1000 || Integer.parseInt(dataDisk)<0){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'data_disk' is from 1 to 1000");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
					
				}
				parameter.put("dataDisk", dataDisk);
				
				if(StringUtil.isBlank(memory)){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'memory' is required");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				} 
				if(!(StringUtil.isInt(memory))){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'memory' must be int");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				} 
				test = new String[] {  
						"1","2","4","6","8","12","16","32","64"
				};
				tempList = Arrays.asList(test);
				if(!(tempList.contains(memory))){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'memory' value is wrong,should be 1,2,4,6,8,12,16,32,64");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				} 
				parameter.put("memory", memory);
				
				if(StringUtil.isBlank(bandwidth)){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'bandwidth' is required");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				} 
				if(!(StringUtil.isInt(bandwidth))){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'bandwidth' must be int");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				}   
				if(bandwidth.length()>3){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'bandwidth' value beyond 1000");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
					
				}
				if(Integer.parseInt(bandwidth)<=0){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'bandwidth' must beyond 0");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
					
				}
				if("1".equals(region)&&Integer.parseInt(bandwidth)>1000){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'bandwidth' beyond 1000");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
					
				}
				if("4".equals(region)&&Integer.parseInt(bandwidth)>10){
					result.setStatus("failure");
					result.setErrorCode("1"); 
					result.setMessage("'bandwidth' beyond 10");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
					
				}
				parameter.put("bandwidth", bandwidth);
				parameter.put("loginInfo", loginInfo);
				
				PaymentService paymentService = CoreSpringContextManager.getPaymentService();
				MethodResult createResult = paymentService.getCloudHostFromAgent(parameter);
				if(createResult.status!="success"){
					result.setStatus("failure");
					result.setErrorCode("3"); 
					result.setMessage("creation failed");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
					
				}else{
					result.setStatus("success");
					result.setMessage("creation success");
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;					
				}

			}
			
			  
			 
			
		} 
	catch (Exception e)
	{
		logger.error("ClientServiceImpl.authenticate()",e);
		throw new AppException("失败");
	}

	}

	/*
	 * 代理商添加用户新用户
	 * */
	@Transactional(readOnly = false)
	public void agentAddUser(HttpServletRequest request,HttpServletResponse response)
	{
		try 
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			SysUserMapper sysUserMapper           = this.sqlSession.getMapper(SysUserMapper.class);
			UserMessageMapper userMessageMapper   = this.sqlSession.getMapper(UserMessageMapper.class);
			
			String  eCheck = "^([a-zA-Z0-9]*[-_.|\\/]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_.|\\/]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
//			String  eCheck="^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";
			String  pCheck = "^1[3|4|5|7|8][0-9]\\d{8,8}$";
			Pattern  ePattern = Pattern.compile(eCheck);    
			Pattern  pPattern = Pattern.compile(pCheck);
			
			String sessionId = StringUtil.trim(request.getParameter("session_id"));
			String name      = StringUtil.trim(request.getParameter("name"));
			String phone     = StringUtil.trim(request.getParameter("phone")); 
			String id        = StringUtil.generateUUID();
			if (StringUtil.isBlank(sessionId)) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("0"); 
	            result.setMessage("'session_id' is required");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			if (LoginHelper.getAgentLoginInfo(sessionId)==null) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("1"); 
	            result.setMessage("login out-of-date");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			if (StringUtil.isBlank(name)) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("2"); 
	            result.setMessage("'name' is required");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			if ((ePattern.matcher(name)).matches()==false)
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("3"); 
	            result.setMessage("'name' is not email");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			if (name.length()>30) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("4"); 
	            result.setMessage("'name' exceeds 30 characters");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			if (StringUtil.isBlank(phone)) 
			{
				Result result = new Result();
				result.setStatus("failure");
				result.setErrorCode("5"); 
				result.setMessage("'phone' is required");  
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			if (pPattern.matcher(phone).matches()==false) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("6"); 
	            result.setMessage("'phone' is incorrect");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			// 判断账号是否已经存在
			TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserByAccount(name);
			if( terminalUser != null )
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("7"); 
	            result.setMessage("'name' already exists");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserByPhone(phone);
			if (terminalUserVO != null) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("8"); 
	            result.setMessage("'phone' have binding account");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			//添加到系统表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       id);
			userData.put("account",  name);
			//随机密码
			String password = RandomPassword.getRandomNum(MailConstant.PASSWORD_LENGTH);
			userData.put("password", LoginHelper.toEncryptedPassword(password));
			userData.put("type",     AppConstant.SYS_USER_TYPE_TERMINAL_USER);
			int n = sysUserMapper.addSysUser(userData);
			//添加到终端用户表
			Map<String, Object> terminalUserData = new LinkedHashMap<String, Object>();
			terminalUserData.put("id",            id);
			terminalUserData.put("email",         name);
			terminalUserData.put("phone",         phone);
			terminalUserData.put("createTime",    StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			terminalUserData.put("emailVerified", AppConstant.TERMINAL_USER_EMAIL_VERIFIED_NOT);
			terminalUserData.put("phoneVerified", AppConstant.TERMINAL_USER_PHONE_VERIFIED_NOT);
			terminalUserData.put("status",        AppConstant.TERMINAL_USER_STATUS_NORMAL);
			terminalUserData.put("accountBalance",BigDecimal.ZERO);
			terminalUserData.put("belongingId",   LoginHelper.getAgentLoginInfo(sessionId).getUserId());
			//插入到相应的归属地
			terminalUserData.put("belongingType", AppConstant.TERMINAL_USER_BELONGING_TYPE_AGENT);
			terminalUserData.put("percentOff",    "0");
			int m = terminalUserMapper.addTerminalUser(terminalUserData);
			
			//消息表
			Map<String, Object> userMessage = new LinkedHashMap<String, Object>();
			userMessage.put("id", StringUtil.generateUUID());
			userMessage.put("userId", id);
			userMessage.put("content", "当前密码是系统初始密码，请及时修改");
			userMessage.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			userMessage.put("status", "1");
			int p=userMessageMapper.insertReg(userMessage);
			
			//发送邮件
			Map<String, Object> user = new LinkedHashMap<String, Object>();
			user.put("password", password);
			user.put("email",    name); 
			new SendMail().sendPasswordEmailForTerminalUser(user);

			if( n > 0 && m > 0 && p>0)
			{   
				Result result = new Result();
	            result.setStatus("success");
	            result.setMessage("Added successfully");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			else
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("9"); 
	            result.setMessage("Added fail");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}

		} catch (Exception e) 
		{
			logger.error("AgentAPIServiceImpl.agentAddUser()",e);
			throw new AppException("失败");
		}
	}
	/*
	 * 代理商查看用户
	 */
	public void agentViewUser(HttpServletRequest request,HttpServletResponse response) 
	{
		try 
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			
			String  eCheck = "^([a-zA-Z0-9]*[-_.|\\/]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_.|\\/]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
			
			Pattern  ePattern = Pattern.compile(eCheck);    
			
			String sessionId = StringUtil.trim(request.getParameter("session_id"));
			String name      = StringUtil.trim(request.getParameter("name"));
			if (StringUtil.isBlank(sessionId)) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("0"); 
	            result.setMessage("'session_id' is required");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			if (LoginHelper.getAgentLoginInfo(sessionId)==null) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("1"); 
	            result.setMessage("login out-of-date");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			if (StringUtil.isBlank(name)) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("2"); 
	            result.setMessage("'name' is required");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			if ((ePattern.matcher(name)).matches()==false)
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("3"); 
	            result.setMessage("'name' is not  email");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			if (name.length()>30) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("4"); 
	            result.setMessage("'name' exceeds 30 characters");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			Map<String, Object> terminalUserData = new LinkedHashMap<String, Object>();
			terminalUserData.put("account",     name);
			terminalUserData.put("belongingId", LoginHelper.getAgentLoginInfo(sessionId).getUserId());
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserByAccountToAgentApi(terminalUserData);
			if (terminalUserVO==null) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("5"); 
	            result.setMessage("not find any information");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			Result result = new Result();
			result.setStatus("success");
			result.setName(terminalUserVO.getAccount());
			result.setPhone(terminalUserVO.getPhone());
			result.setUserId(terminalUserVO.getId());
			result.setMessage("query success");
			response.getWriter().append(XMLUtil.javaToXml(result));
			
		} catch (Exception e) 
		{
			logger.error("AgentAPIServiceImpl.agentViewUser()",e);
			throw new AppException("失败");
		}
		
	}
	
	/*
	 * 代理商修改用户信息
	 * */
	@Transactional(readOnly = false)
	public void agentModifyUser(HttpServletRequest request,HttpServletResponse response) 
	{
		try 
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			SysUserMapper sysUserMapper           = this.sqlSession.getMapper(SysUserMapper.class);
			
			String  eCheck = "^([a-zA-Z0-9]*[-_.|\\/]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_.|\\/]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
			String  pCheck = "^1[3|4|5|7|8][0-9]\\d{8,8}$";
			Pattern  ePattern = Pattern.compile(eCheck);
			Pattern  pPattern = Pattern.compile(pCheck);
			
			String sessionId = StringUtil.trim(request.getParameter("session_id"));
			String userId    = StringUtil.trim(request.getParameter("user_id"));
			String name      = StringUtil.trim(request.getParameter("name"));
			String phone     = StringUtil.trim(request.getParameter("phone")); 
			if (StringUtil.isBlank(sessionId)) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("0"); 
	            result.setMessage("'session_id' is required");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			if (LoginHelper.getAgentLoginInfo(sessionId)==null) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("1"); 
	            result.setMessage("login out-of-date");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			if (StringUtil.isBlank(userId)) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("2"); 
	            result.setMessage("'user_id' is required");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			TerminalUserVO userVO =terminalUserMapper.getTerminalUserById(userId);
			if (userVO==null) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("3"); 
	            result.setMessage("'user_id' is invalid");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			if (StringUtil.isBlank(name)&&StringUtil.isBlank(phone)) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("4"); 
	            result.setMessage("'name' and 'phone' can not be null at the same time");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			
	//用户名不为空时判断是否邮箱格式以及长度,并且做错相应更改
			if (StringUtil.isBlank(phone)==true && StringUtil.isBlank(name)==false) {
				if ((ePattern.matcher(name)).matches()==false)
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("5"); 
					result.setMessage("'name' is not email");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				if (name.length()>30)
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("6"); 
					result.setMessage("'name' exceeds 30 characters");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				// 判断账号是否已经存在
				TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserByAccount(name);
				if( terminalUser!= null )
				{
					Result result = new Result();
		            result.setStatus("failure");
		            result.setErrorCode("7"); 
		            result.setMessage("'name' already exists");  
		            response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				// 更新系统用户表
				Map<String, Object> userData = new LinkedHashMap<String, Object>();
				userData.put("id",       userId);
				userData.put("account",  name);
				userData.put("password",  "");
				int n = sysUserMapper.updateSysUser(userData);
				// 更新终端用户表
				Map<String, Object> userData2 = new LinkedHashMap<String, Object>();
				userData2.put("id",     userId);
				userData2.put("email",  name);
				userData2.put("emailVerified", AppConstant.TERMINAL_USER_EMAIL_VERIFIED_YES);
				int m = terminalUserMapper.updateTerminalUserBaseInfoEmail(userData2);
				
				if (n>0 && m>0) 
				{
					Result result = new Result();
					result.setStatus("success");
					result.setMessage("change successfully");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				else 
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("10"); 
					result.setMessage("change fail");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
			}
			
	//电话号码不为空时判断电话格式时候存在，做错相应的更改
			if (StringUtil.isBlank(name)==true && StringUtil.isBlank(phone)==false) 
			{
				if (pPattern.matcher(phone).matches()==false) 
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("8"); 
					result.setMessage("'phone' is incorrect");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				//判断号码是否存在
				TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserByPhone(phone);
				if (terminalUserVO != null) 
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("9"); 
					result.setMessage("'phone' have binding account");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				// 更新终端用户表
				Map<String, Object> userData = new LinkedHashMap<String, Object>();
				userData.put("id",     userId);
				userData.put("phone",  phone);
				userData.put("phoneVerified", AppConstant.TERMINAL_USER_PHONE_VERIFIED_YES);
				int m = terminalUserMapper.updateTerminalUserBaseInfoPhone(userData);
				if (m>0) 
				{
					Result result = new Result();
					result.setStatus("success");
					result.setMessage("change successfully");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				else 
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("10"); 
					result.setMessage("change fail");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
			}
			
	//用户名和电话同时不为空，都更改的情况
			if (StringUtil.isBlank(phone)==false && StringUtil.isBlank(name)==false) 
			{
				// 判断账号是否已经存在
				TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserByAccount(name);
				if( terminalUser!=null )
				{
					Result result = new Result();
		            result.setStatus("failure");
		            result.setErrorCode("7"); 
		            result.setMessage("'name' already exists");  
		            response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				//判断号码是否存在
				TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserByPhone(phone);
				if (terminalUserVO!=null ) 
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("9"); 
					result.setMessage("'phone' have binding account");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				// 更新系统用户表
				Map<String, Object> userData = new LinkedHashMap<String, Object>();
				userData.put("id",       userId);
				userData.put("account",  name);
				userData.put("password",  "");
				int n = sysUserMapper.updateSysUser(userData);
				// 更新运营商表
				Map<String, Object> userData2 = new LinkedHashMap<String, Object>();
				userData2.put("id", userId);
				userData2.put("email", name);
				userData2.put("phone", phone);
				userData2.put("status", AppConstant.TERMINAL_USER_STATUS_NORMAL);
				int m = terminalUserMapper.updateTerminalUserBaseInfo(userData2);
				if (n>0 && m>0) 
				{
					Result result = new Result();
					result.setStatus("success");
					result.setMessage("change successfully");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				else 
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("10"); 
					result.setMessage("change fail");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
					
				}
			}
			
		} catch (Exception e) 
		{
			logger.error("AgentAPIServiceImpl.agentModifyUser()",e);
			throw new AppException("失败");
		}
		
	}
	
	/*
	 * 代理商查看自己信息
	 */
	public void agentViewAgent(HttpServletRequest request,HttpServletResponse response) 
	{
		try 
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
			AgentMapper agentMapper =this.sqlSession.getMapper(AgentMapper.class);
			
			String sessionId = StringUtil.trim(request.getParameter("session_id"));
			if (StringUtil.isBlank(sessionId)) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("0"); 
	            result.setMessage("'session_id' is required");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			if (LoginHelper.getAgentLoginInfo(sessionId)==null) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("1"); 
	            result.setMessage("login out-of-date");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			AgentVO agentVO = agentMapper.getAgentById(LoginHelper.getAgentLoginInfo(sessionId).getUserId());
			if (agentVO==null) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("1"); 
	            result.setMessage("not find any information");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}else 
			{
			Result result = new Result();
			result.setStatus("success");
//			result.setAgentId(agentVO.getId());
			result.setName(agentVO.getAccount());
			result.setPhone(agentVO.getPhone());
			result.setAccountBalance(agentVO.getAccountBalance().toString());
			result.setMessage("successfully");
			response.getWriter().append(XMLUtil.javaToXml(result));
			}
		} catch (Exception e) 
		{
			logger.error("AgentAPIServiceImpl.agentViewAgent()",e);
			throw new AppException("失败");
		}
		
	}
	
	/*
	 * 代理商修改自己信息
	 * */
	@Transactional(readOnly = false)
	public void agentModifyAgent(HttpServletRequest request,HttpServletResponse response) 
	{
		try 
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
			SysUserMapper sysUserMapper           = this.sqlSession.getMapper(SysUserMapper.class);
			
			String  eCheck = "^([a-zA-Z0-9]*[-_.|\\/]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_.|\\/]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
			String  pCheck = "^1[3|4|5|7|8][0-9]\\d{8,8}$";
			Pattern  ePattern = Pattern.compile(eCheck);    
			Pattern  pPattern = Pattern.compile(pCheck);
			
			String sessionId = StringUtil.trim(request.getParameter("session_id"));
//			String agentId    = StringUtil.trim(request.getParameter("agentId"));
			String name      = StringUtil.trim(request.getParameter("name"));
			String phone     = StringUtil.trim(request.getParameter("phone")); 
			if (StringUtil.isBlank(sessionId))
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("0"); 
	            result.setMessage("'session_id' is required");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			if (LoginHelper.getAgentLoginInfo(sessionId)==null) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("1"); 
	            result.setMessage("login out-of-date");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			//为后续使用
			AgentVO agent = agentMapper.getAgentById(LoginHelper.getAgentLoginInfo(sessionId).getUserId());
			
			if (StringUtil.isBlank(name)&&StringUtil.isBlank(phone)) 
			{
				Result result = new Result();
	            result.setStatus("failure");
	            result.setErrorCode("2"); 
	            result.setMessage("'name' and 'phone' can not be null at the same time");  
	            response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}
			
		//用户名不为空时判断，并且做相应的操作
			if (StringUtil.isBlank(name)==false && StringUtil.isBlank(phone)==true) {
				if ((ePattern.matcher(name)).matches()==false)
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("3"); 
					result.setMessage("'name' is not email");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				if (name.length()>30)
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("4"); 
					result.setMessage("'name' exceeds 30 characters");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				// 判断账号是否已经存在
				AgentVO agentVO = agentMapper.getAgentByAccount(name);
				if( agentVO!= null )
				{
					Result result = new Result();
		            result.setStatus("failure");
		            result.setErrorCode("5"); 
		            result.setMessage("'name' already exists");  
		            response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				// 更新系统用户表
				Map<String, Object> userData = new LinkedHashMap<String, Object>();
				userData.put("id",       agent.getId());
				userData.put("account",  name);
				userData.put("password",  "");
				userData.put("groupId", "");
				int n = sysUserMapper.updateSysUser(userData);
				// 更新代理商表
				Map<String, Object> userData2 = new LinkedHashMap<String, Object>();
				userData2.put("id",     agent.getId());
				userData2.put("email",  name);
				userData2.put("phone", 	agent.getPhone());
				userData2.put("status", AppConstant.AGENT_STATUS_NORMAL);
				userData2.put("operatorId", "");
				userData2.put("percentOff", "");
				int m = agentMapper.updateAgent(userData2);
				
				if (n>0 && m>0) 
				{
					Result result = new Result();
					result.setStatus("success");
					result.setMessage("change successfully");  
					response.getWriter().append(XMLUtil.javaToXml(result));	
					//释放
					synchronized (AppInconstant.agentLogInfo) 
					{
						AppInconstant.agentLogInfo.remove(sessionId);
					}
					return ;
				}
				else 
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("8"); 
					result.setMessage("change fail");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
			}
			//电话号码不为空时判断,并且做相应的操作
			if (StringUtil.isBlank(name)==true && StringUtil.isBlank(phone)==false) 
			{
				if (pPattern.matcher(phone).matches()==false) 
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("6"); 
					result.setMessage("'phone' is incorrect");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				//判断号码是否存在
				AgentVO agentVO2 = agentMapper.getAgentByPhone(phone);
				if (agentVO2!= null) 
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("7"); 
					result.setMessage("'phone' have binding account");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				// 更新终端用户表
				Map<String, Object> userData = new LinkedHashMap<String, Object>();
				userData.put("id",     agent.getId());
				userData.put("email",  agent.getEmail());
				userData.put("phone",  phone);
				userData.put("status", AppConstant.AGENT_STATUS_NORMAL);
				userData.put("operatorId", "");
				userData.put("percentOff", "");
				int m = agentMapper.updateAgent(userData);
				if (m>0) 
				{
					Result result = new Result();
					result.setStatus("success");
					result.setMessage("change successfully");  
					response.getWriter().append(XMLUtil.javaToXml(result));	
					//释放
					synchronized (AppInconstant.agentLogInfo) 
					{
						AppInconstant.agentLogInfo.remove(sessionId);
					}
					return ;
				}
				else 
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("8"); 
					result.setMessage("change fail");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
			}
			
			//用户名和电话同时不为空，都更改的情况
			if (StringUtil.isBlank(phone)==false && StringUtil.isBlank(name)==false) 
			{
				//判断格式
				if ((ePattern.matcher(name)).matches()==false)
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("3"); 
					result.setMessage("'name' is required");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				//判断长度
				if (name.length()>30)
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("4"); 
					result.setMessage("'name' exceeds 30 characters");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				// 判断账号是否已经存在
				AgentVO agentVO =agentMapper.getAgentByAccount(name);
				if( agentVO!=null )
				{
					Result result = new Result();
		            result.setStatus("failure");
		            result.setErrorCode("5"); 
		            result.setMessage("'name' already exists ");  
		            response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				//判断号码是否存在
				AgentVO agentVO2 = agentMapper.getAgentByPhone(phone);
				if (agentVO2!=null) 
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("7"); 
					result.setMessage("'phone' have binding account");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
				// 更新系统用户表
				Map<String, Object> userData = new LinkedHashMap<String, Object>();
				userData.put("id",       agent.getId());
				userData.put("account",  name);
				userData.put("password",  "");
				userData.put("groupId", "");
				int n = sysUserMapper.updateSysUser(userData);
				// 更新运营商表
				Map<String, Object> userData2 = new LinkedHashMap<String, Object>();
				userData2.put("id",     agent.getId());
				userData2.put("email",  name);
				userData2.put("phone",  phone);
				userData2.put("status", AppConstant.AGENT_STATUS_NORMAL);
				userData2.put("operatorId", "");
				userData2.put("percentOff", "");
				int m = agentMapper.updateAgent(userData2);
				
				if (n>0 && m>0) 
				{
					Result result = new Result();
					result.setStatus("success");
					result.setMessage("change successfully");  
					response.getWriter().append(XMLUtil.javaToXml(result));	
					//释放
					synchronized (AppInconstant.agentLogInfo) 
					{
						AppInconstant.agentLogInfo.remove(sessionId);
					}
					return ;
				}
				else 
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("8"); 
					result.setMessage("change fail");  
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}
			}
			
		} catch (Exception e) 
		{
			logger.error("AgentAPIServiceImpl.agentModifyAgent()",e);
			throw new AppException("失败");
		}
	}


	@Override
	public void agentGetImage(HttpServletRequest request,HttpServletResponse response) {
		try{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
 			String sessionId = StringUtil.trim(request.getParameter("session_id"));
			LoginInfo loginInfo = LoginHelper.getAgentLoginInfo(sessionId);
			SysDiskImageMapper sysDiskImageMapper = sqlSession.getMapper(SysDiskImageMapper.class);
			if(loginInfo == null){
				Result result = new Result();
				result.setStatus("failure");
				result.setErrorCode("1");
				result.setMessage("login out-of-date");
				response.getWriter().append(XMLUtil.javaToXml(result));
				return;
			}else{  
				List<SysDiskImageVO> sysDiskImageList = sysDiskImageMapper.getDiyImage(loginInfo.getUserId());
				List<SysDiskImageVO> CommonSysDiskImage = sysDiskImageMapper.getAllCommonImage();
				sysDiskImageList.addAll(CommonSysDiskImage);
				Result result = new Result();
				result.setStatus("success");
				result.setMessage("The total number of records:【"+sysDiskImageList.size()+"】");
				result.setSysDiskImageList(sysDiskImageList);
				response.getWriter().append(XMLUtil.javaToXml(result));
				return;
			}
			
		} 
		catch (Exception e)
		{
			logger.error("ClientServiceImpl.authenticate()",e);
			throw new AppException("失败");
		}
	}
	@Override
	@Transactional(readOnly = false)
	public void agentOperateUserHost(HttpServletRequest request,HttpServletResponse response) {
		try{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
 			String sessionId  = StringUtil.trim(request.getParameter("session_id"));
			String hostId     = StringUtil.trim(request.getParameter("host_id")); 
			String operType   = StringUtil.trim(request.getParameter("oper_type"));
			LoginInfo loginInfo = LoginHelper.getAgentLoginInfo(sessionId);
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			TerminalUserMapper terminalUserMapper = sqlSession.getMapper(TerminalUserMapper.class);
			CloudHostService cloudHostService = CoreSpringContextManager.getCloudHostService();
			if(loginInfo == null){
				Result result = new Result();
				result.setStatus("failure");
				result.setErrorCode("1");
				result.setMessage("login out-of-date");
				response.getWriter().append(XMLUtil.javaToXml(result));
				return;
			}else{
				if(hostId==null || StringUtil.isBlank(hostId)){
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("8");
					result.setMessage("'host_id' is required");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
				if(cloudHost==null){
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("2");
					result.setMessage("The cloudHost is not found");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserById(cloudHost.getUserId());
				if(!(terminalUser!=null && terminalUser.getBelongingId()!=null && terminalUser.getBelongingId().equals(loginInfo.getUserId()))){
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("9");
					result.setMessage("The cloudHost is not found");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				hostId = hostId + "AGENT";
				if(operType==null || StringUtil.isBlank(operType)){
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("3");
					result.setMessage("'oper_type' is required");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				if("1".equals(operType)){
					Result result = new Result();
					MethodResult methodResult =  cloudHostService.startCloudHost(hostId);
					if(methodResult.status.equals("success")){
						result.setStatus("success");
						result.setMessage(methodResult.message);
					}else if(methodResult.status.equals("fail")){
						result.setStatus("failure");
						result.setErrorCode("4");
						result.setMessage(methodResult.message);
					}
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}else if("2".equals(operType)){
					Result result = new Result();
					MethodResult methodResult =  cloudHostService.stopCloudHost(hostId);
					if(methodResult.status.equals("success")){
						result.setStatus("success");
						result.setMessage(methodResult.message);
					}else if(methodResult.status.equals("fail")){
						result.setStatus("failure");
						result.setErrorCode("5");
						result.setMessage(methodResult.message);
					}
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}else if("3".equals(operType)){
					Result result = new Result();
					MethodResult methodResult =  cloudHostService.restartCloudHost(hostId);
					if(methodResult.status.equals("success")){
						result.setStatus("success");
						result.setMessage(methodResult.message);
					}else if(methodResult.status.equals("fail")){
						result.setStatus("failure");
						result.setErrorCode("6");
						result.setMessage(methodResult.message);
					}
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}else if("4".equals(operType)){
					Result result = new Result();
					MethodResult methodResult =  cloudHostService.haltCloudHost(hostId);
					if(methodResult.status.equals("success")){
						result.setStatus("success");
						result.setMessage(methodResult.message);
					}else if(methodResult.status.equals("fail")){
						result.setStatus("failure");
						result.setErrorCode("7");
						result.setMessage(methodResult.message);
					}
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}else{
					Result result = new Result();
					result.setErrorCode("10");
					result.setMessage("The oper_type : "+operType+" is invalid");
					result.setStatus("failure");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
			}
			
		} 
		catch (Exception e)
		{
			logger.error("ClientServiceImpl.authenticate()",e);
			throw new AppException("失败");
		}
	}
	@Override
	@Transactional(readOnly = false)
	public void agentModifyUserHost(HttpServletRequest request,HttpServletResponse response) {
		try{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
 			String sessionId  = StringUtil.trim(request.getParameter("session_id"));
			String hostId     = StringUtil.trim(request.getParameter("host_id")); 
			String cpuCore    = StringUtil.trim(request.getParameter("cpu_core"));
			String memory     = StringUtil.trim(request.getParameter("memory"));
			String bandwidth  = StringUtil.trim(request.getParameter("bandwidth"));
			List<String> cpuCoreList = Arrays.asList("1","2","4","8","12","16");
			List<String> memoryList  = Arrays.asList("1","2","4","6","8","12","16","32","64");
			
			LoginInfo loginInfo = LoginHelper.getAgentLoginInfo(sessionId);
			TerminalUserMapper terminalUserMapper = sqlSession.getMapper(TerminalUserMapper.class);
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			if(hostId==null || StringUtil.isBlank(hostId)){
				Result result = new Result();
				result.setStatus("failure");
				result.setErrorCode("4");
				result.setMessage("'host_id' is required");
				response.getWriter().append(XMLUtil.javaToXml(result));
				return;
			}
			CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
			if(loginInfo == null){
				Result result = new Result();
				result.setStatus("failure");
				result.setErrorCode("1");
				result.setMessage("login out-of-date");
				response.getWriter().append(XMLUtil.javaToXml(result));
				return;
			}else{
				if(cloudHost == null){
					Result result = new Result();
					result.setStatus("failure");
					result.setMessage("The cloudHost is not found");
					result.setErrorCode("2");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserById(cloudHost.getUserId());
				if(!(terminalUser!=null && terminalUser.getBelongingId()!=null && terminalUser.getBelongingId().equals(loginInfo.getUserId()))){
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("5");
					result.setMessage("The cloudHost is not found");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				if(!"关机".equals(cloudHost.getSummarizedStatusText())){
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("3");
					result.setMessage("Please make sure that your cloud hosting is power off");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				if(StringUtil.isBlank(cpuCore) && StringUtil.isBlank(memory) && StringUtil.isBlank(bandwidth)){
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("4");
					result.setMessage(" No need to modify the options");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				if(cpuCore==null || StringUtil.isBlank(cpuCore)){
					cpuCore = cloudHost.getCpuCore().toString();
				}
				if(memory==null || StringUtil.isBlank(memory)){
					memory = CapacityUtil.toGBValue(cloudHost.getMemory(), 0).toString();
				}
				if(bandwidth==null || StringUtil.isBlank(bandwidth)){
					bandwidth = FlowUtil.toMbpsValue(cloudHost.getBandwidth(),0).toString();
				}
				if(!cpuCoreList.contains(cpuCore)){
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("4");
					result.setMessage("'cpu_core' options：1、2、4、8、12、16");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				if(!memoryList.contains(memory)){
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("4");
					result.setMessage("'memory' options：1、2、4、6、8、12、16、32、64,unit: G");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				
				try{
					Integer newBandwidth = Integer.parseInt(bandwidth);
					if(cloudHost.getRegion()==1){
						if(newBandwidth<1 || newBandwidth>1000){
							Result result = new Result();
							result.setStatus("failure");
							result.setErrorCode("4");
							result.setMessage("Value range:GuangZhou：1-1000，unit：Mbps");
							response.getWriter().append(XMLUtil.javaToXml(result));
							return;
						}
					}else if(cloudHost.getRegion()==4){
						if(newBandwidth<1 || newBandwidth>10){
							Result result = new Result();
							result.setStatus("failure");
							result.setErrorCode("4");
							result.setMessage("Value range:HongKong：1-10，unit：Mbps");
							response.getWriter().append(XMLUtil.javaToXml(result));
							return;
						}
					}
				}catch(NumberFormatException e){
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("4");
					result.setMessage("Is not a positive integer");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
				BigDecimal price        = CloudHostPrice.getMonthlyPrice(cloudHost.getRegion(),3,Integer.parseInt(cpuCore), CapacityUtil.fromCapacityLabel(memory+"GB"), cloudHost.getDataDisk(), FlowUtil.fromFlowLabel(bandwidth+"Mbps"));
				AgentVO agentVO = agentMapper.getAgentById(loginInfo.getUserId());
				BigDecimal percentOff = agentVO.getPercentOff();
				price = price.multiply(new BigDecimal(100).subtract(percentOff)).divide(new BigDecimal(100),2,BigDecimal.ROUND_HALF_UP);
				//计算余额能否支持一天，不足不创建主机
				if(agentVO!=null){
					List<CloudHostVO> cloudHostList = cloudHostMapper.getByAgentId(loginInfo.getUserId());
					BigDecimal totalPrice = new BigDecimal("0");
					if(cloudHostList!=null&&cloudHostList.size()>0){
						for(CloudHostVO vo:cloudHostList){
							if(vo.getType() == 2){
								if(!(vo.getSummarizedStatusText().equals("创建失败")||vo.getSummarizedStatusText().equals("停机"))&&vo.getMonthlyPrice()!=null&&(!vo.getId().equals(hostId))){						
									totalPrice = vo.getMonthlyPrice().add(totalPrice);
								}
							}
						} 
						
					}
					//计算出每天的费用
					totalPrice = totalPrice.add(price);
					totalPrice = totalPrice.divide(new BigDecimal("31"),0,BigDecimal.ROUND_HALF_UP);
					BigDecimal balance = agentVO.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
					if(balance.compareTo(totalPrice)<0){
						if(totalPrice.subtract(balance).compareTo(BigDecimal.TEN)<0){
							Result result = new Result();
							result.setStatus("failure");
							result.setMessage("Lack of balance");
							result.setErrorCode("13");
							response.getWriter().append(XMLUtil.javaToXml(result));
							return;
						}else{		
							Result result = new Result();
							result.setStatus("failure");
							result.setMessage("Lack of balance");
							result.setErrorCode("14");
							response.getWriter().append(XMLUtil.javaToXml(result));
							return;
 						}
						
					} 
					
				} 
				
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
				JSONObject hostModifyResult = channel.hostModify(cloudHost.getRealHostId(), 
						"", 
						Integer.parseInt(cpuCore), 
						CapacityUtil.fromCapacityLabel(memory+"GB"), 
						new Integer[]{0,1,1}, 
						new Integer[0], 
						"", 
						"", 
						"", 
						FlowUtil.fromFlowLabel(bandwidth+"Mbps"), 
						FlowUtil.fromFlowLabel(bandwidth+"Mbps"));
				if( HttpGatewayResponseHelper.isSuccess(hostModifyResult)==false )
				{
					Result result = new Result();
					result.setStatus("failure");
					result.setMessage("Modify fail");
					result.setErrorCode("15");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}else{
					// 结算，结算之后产生新的账单
					Date now = new Date();
					new CloudHostBillingHelper(sqlSession).settleAllUndoneCloudHostBills(hostId, now, true);
					Map<String, Object> newCloudHostData = new LinkedHashMap<String, Object>();
					newCloudHostData.put("id",         hostId);
					newCloudHostData.put("cpuCore",    Integer.valueOf(cpuCore));
					newCloudHostData.put("memory",     CapacityUtil.fromCapacityLabel(memory+"GB"));
					newCloudHostData.put("bandwidth",  FlowUtil.fromFlowLabel(bandwidth+"Mbps"));
					newCloudHostData.put("price",      price);
					int n = cloudHostMapper.updateClientCloudHostById(newCloudHostData);
					if( n > 0)
					{
						Result result = new Result();
						result.setStatus("success");
						response.getWriter().append(XMLUtil.javaToXml(result));
						return;
					}else{
						Result result = new Result();
						result.setStatus("success");
						result.setMessage("Database changes fail");
						result.setErrorCode("16");
						response.getWriter().append(XMLUtil.javaToXml(result));
						return;
					}
				}
			}
			
		} 
		catch (Exception e)
		{
			logger.error("ClientServiceImpl.authenticate()",e);
			throw new AppException("失败");
		}
	}
	
 
	
	 
	
	/**
	 * 代理商查询用户操作日志
	 */
	public void agentViewUserOper(HttpServletRequest request, HttpServletResponse response) { 
		logger.debug("AgentAPIServiceImpl.agentViewUserOper()");
		try
		{
			logger.info("AgentAPIServiceImpl.agentCreateUserHost()");
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
			String sessionId     = StringUtil.trim(request.getParameter("session_id"));
 			String userId     = StringUtil.trim(request.getParameter("user_id")); 
 			String page_str     = StringUtil.trim(request.getParameter("page")); 
 			String rows_str     = StringUtil.trim(request.getParameter("rows"));  
			Result result = new Result();
			
			
			if(StringUtil.isBlank(sessionId)){
				result.setStatus("failure"); 
				result.setErrorCode("1"); 
				result.setMessage("session_id is requried");   
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			}else{
				LoginInfo loginInfo = LoginHelper.getAgentLoginInfo(sessionId);
				if(loginInfo==null){  
					result.setErrorCode("2"); 
					result.setStatus("failure"); 
					result.setMessage("login out-of-date");
					
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
					
				}
				if(StringUtil.isBlank(userId)){
					result.setMessage("'user_id' is required");   
					result.setStatus("failure");
					result.setErrorCode("1");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
					
				}
				TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
				
				TerminalUserVO termialUser = terminalUserMapper.getTerminalUserById(userId); 
				if(termialUser==null){
					result.setStatus("failure");
					result.setErrorCode("1");   
					result.setMessage("not find account info by user_id");   
					response.getWriter().append(XMLUtil.javaToXml(result));				 
					return ;
				}  
			}
			
			if(StringUtil.isBlank(page_str)){
				result.setStatus("failure");
				result.setErrorCode("1");   
				result.setMessage("'page' is required");   
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;				
			}
			if(!(StringUtil.isInt(page_str))){
				result.setStatus("failure");
				result.setErrorCode("1");   
				result.setMessage("'page' must be int");   
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;								
			}
			if(StringUtil.isBlank(rows_str)){
				result.setStatus("failure");
				result.setErrorCode("1");   
				result.setMessage("'rows' is required");   
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;				
			}
			if(!(StringUtil.isInt(rows_str))){
				result.setStatus("failure");
				result.setErrorCode("1");   
				result.setMessage("'rows'  must be int");   
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;								
			}
			
			
			Integer page = StringUtil.parseInteger(page_str, 1) - 1;
			Integer rows = StringUtil.parseInteger(rows_str, 10);

			// 查询数据库
			OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("userId", userId);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = operLogMapper.queryPageCount(condition); // 总行数
			List<OperLogVO> operLogList = operLogMapper.queryPage(condition);// 分页结果
			List<OperLogForApiVO> list = new ArrayList<OperLogForApiVO>();// 分页结果
			for(OperLogVO logVO:operLogList){
				OperLogForApiVO vo = new OperLogForApiVO();
				vo.setContent(logVO.getContent());
				vo.setOperTime(StringUtil.formatDateString(logVO.getOperTime(), "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss.SSS"));
				list.add(vo);
			}
			if(list.size()>0){				
				result.setOperLogList(list);
			}
            result.setStatus("success");
            result.setCount(total+""); 
            response.getWriter().append(XMLUtil.javaToXml(result));
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}  
	}
	@Override
	public void agentConsumption(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("AgentAPIServiceImpl.agentConsumption()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");  
			String sessionId     = StringUtil.trim(request.getParameter("session_id")); 
 			String page_str     = StringUtil.trim(request.getParameter("page")); 
 			String rows_str     = StringUtil.trim(request.getParameter("rows"));  
			Result result = new Result();
			
			
			if(StringUtil.isBlank(sessionId)){
				result.setStatus("failure"); 
				result.setErrorCode("1"); 
				result.setMessage("'session_id' is required");   
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
			} 
			LoginInfo loginInfo = LoginHelper.getAgentLoginInfo(sessionId);
			if(loginInfo==null){  
				result.setErrorCode("2"); 
				result.setStatus("failure"); 
				result.setMessage("login out-of-date");
				
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;
				
			}
			
			if(StringUtil.isBlank(page_str)){
				result.setStatus("failure");
				result.setErrorCode("1");   
				result.setMessage("'page' is required");   
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;				
			}
			if(!(StringUtil.isInt(page_str))){
				result.setStatus("failure");
				result.setErrorCode("1");   
				result.setMessage("'page' must be int");   
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;								
			}
			if(StringUtil.isBlank(rows_str)){
				result.setStatus("failure");
				result.setErrorCode("1");   
				result.setMessage("'rows'  is required");   
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;				
			}
			if(!(StringUtil.isInt(rows_str))){
				result.setStatus("failure");
				result.setErrorCode("1");   
				result.setMessage("'rows' must be int");   
				response.getWriter().append(XMLUtil.javaToXml(result));				 
				return ;								
			}
			
			
			Integer page = StringUtil.parseInteger(page_str, 1) - 1;
			Integer rows = StringUtil.parseInteger(rows_str, 10);

			// 查询数据库
            AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);  
			
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("userId",  loginInfo.getUserId());
			condition.put("type", 2); 			 
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			String all = accountBalanceDetailMapper.getAllAccount(condition);
			int total = accountBalanceDetailMapper.getRechargeDetailByUserIdCount(condition);		// 总行数 
			List<AccountBalanceDetailVO> record = accountBalanceDetailMapper.getRechargeDetailByUserId(condition);	// 分页结果
			List<AgentConsumption> consumptionList = new ArrayList<AgentConsumption>();
			for(AccountBalanceDetailVO vo : record){
				AgentConsumption consumption = new AgentConsumption();
				consumption.setResourceName(vo.getResourceName());
				consumption.setCost(vo.getAmount());
				consumption.setPayTime(StringUtil.formatDateString(vo.getChangeTime(), "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss.SSS"));
				consumptionList.add(consumption);
			}
            result.setConsumptionList(consumptionList); 
            result.setStatus("success");
            result.setCount(total+""); 
            result.setTotalCost(all);
            response.getWriter().append(XMLUtil.javaToXml(result));
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}  
		
	}
	
 @Override
	public void agentViewUserPayExpenses(HttpServletRequest request,HttpServletResponse response) {
		try{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/xml; charset=utf-8");
 			String sessionId = StringUtil.trim(request.getParameter("session_id"));
			String userId    = StringUtil.trim(request.getParameter("user_id")); 
			String startTime = StringUtil.trim(request.getParameter("start_time")); 
			String endTime   = StringUtil.trim(request.getParameter("end_time")); 
			LoginInfo loginInfo = LoginHelper.getAgentLoginInfo(sessionId);
			CloudHostMapper cloudHostMapper = sqlSession.getMapper(CloudHostMapper.class);
			CloudHostBillDetailMapper cloudHostBillDetailMapper = sqlSession.getMapper(CloudHostBillDetailMapper.class);
			TerminalUserMapper terminalUserMapper = sqlSession.getMapper(TerminalUserMapper.class);
			if(loginInfo==null){
				Result result = new Result();
				result.setStatus("failure");
				result.setErrorCode("1");
				result.setMessage("login out-of-date");
				response.getWriter().append(XMLUtil.javaToXml(result));
				return;
			}else{
				if(userId==null || StringUtil.isBlank(userId)){
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("2");
					result.setMessage("'user_id' is required");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserById(userId);
				if(!(terminalUser!=null && terminalUser.getBelongingId()!=null && terminalUser.getBelongingId().equals(loginInfo.getUserId()))){
					Result result = new Result();
					result.setStatus("failure");
					result.setErrorCode("3");
					result.setMessage("The user is not found");
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				if(!StringUtil.isBlank(startTime)){
					try{
						StringUtil.stringToDate(startTime, "yyyyMMddHHmmssSSS");
					}catch(ParseException pe){
						Result result = new Result();
						result.setStatus("failure");
						result.setErrorCode("2");
						result.setMessage("'start_time' is error");
						response.getWriter().append(XMLUtil.javaToXml(result));
						return;
					}
				}
				if(!StringUtil.isBlank(endTime)){
					try{
						StringUtil.stringToDate(endTime, "yyyyMMddHHmmssSSS");
					}catch(ParseException pe){
						Result result = new Result();
						result.setStatus("failure");
						result.setErrorCode("2");
						result.setMessage("'end_time' is error");
						response.getWriter().append(XMLUtil.javaToXml(result));
						return;
					}
				}
				List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(userId);
				if(cloudHostList!=null && cloudHostList.size()>0){
					List<PayExpenses> payExpensesList = new ArrayList<PayExpenses>();
					for(CloudHostVO cloudHost:cloudHostList){
						BigDecimal totalFee = BigDecimal.ZERO;
						if(cloudHost.getType() == 2){
							Map<String, Object> condition = new LinkedHashMap<String, Object>();
 							condition.put("hostId", cloudHost.getId());
 							condition.put("startTime", startTime);
 							condition.put("endTime", endTime);
 							
							List<CloudHostBillDetailVO> cloudHostBillDetailList = cloudHostBillDetailMapper.getBillDetailByHostId(condition);
							for(CloudHostBillDetailVO cloudHostBillDetail:cloudHostBillDetailList){
								totalFee = totalFee.add(cloudHostBillDetail.getFee());
							}
							PayExpenses pe = new PayExpenses();
							pe.setEndTime(endTime);
							pe.setStartTime(startTime);
							pe.setHostName(cloudHost.getHostName());
							pe.setPayExpenses(totalFee);
							payExpensesList.add(pe);
						}
					}
					Result result = new Result();
					result.setStatus("success");
					result.setPayExpensesList(payExpensesList);
					response.getWriter().append(XMLUtil.javaToXml(result));
					return;
				}
				
			}
		} 
		catch (Exception e)
		{
			logger.error("ClientServiceImpl.authenticate()",e);
			throw new AppException("失败");
		}
	} 
	
	 
	
	 
	 

	
	
}
