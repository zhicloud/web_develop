package com.zhicloud.op.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.SendMail;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.OperLogMapper;
import com.zhicloud.op.mybatis.mapper.SuggestionMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.SuggestionService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.SuggestionVO;
@Transactional(readOnly = false)
public class SuggestionServiceImpl extends BeanDirectCallableDefaultImpl implements SuggestionService {
	public static final Logger logger = Logger.getLogger(SuggestionServiceImpl.class);
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	@Callable
	public String managePage(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("SuggestionServiceImpl.managePage()");
		
		// 权限判断
		String type = request.getParameter("userType"); 
		if(type!=null&&type.equals("4")){
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
			SuggestionMapper suggestionMapper = this.sqlSession.getMapper(SuggestionMapper.class);
			
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			
			condition.put("userId",  loginInfo.getUserId());	  		 
			List<SuggestionVO> suggestionList = suggestionMapper.queryPageAll(condition);	// 分页结果
			request.setAttribute("suggestionList", suggestionList);
			return "/security/user/suggestion_manage.jsp";			
		}else{
			 LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_OPERATOR);
			 if(loginInfo!=null&&loginInfo.hasPrivilege(PrivilegeConstant.suggestion_manage_page_operator)==true){
				return  "/security/operator/suggestion_manage.jsp";
			 }
			 return  "/public/have_not_access.jsp";
			
		}
		
	}

	@Callable
	public String addSuggestionPage(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("SuggestionServiceImpl.managePage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
 		
		
		return "/security/user/suggestion_manage_add.jsp"; 
	}

	@Callable
	public String suggestionDetailPage(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("SuggestionServiceImpl.addSuggestion()"); 
		String suggestionId = request.getParameter("suggestionId"); 
		String type = request.getParameter("type"); 
		
		SuggestionMapper suggestionMapper = this.sqlSession.getMapper(SuggestionMapper.class);
		
		Map<String, Object> suggestionData = new LinkedHashMap<String, Object>();
		suggestionData.put("suggestionId", suggestionId); 
		SuggestionVO suggestion =  suggestionMapper.getSuggestionById(suggestionData);
		if(type!=null&&type.equals("reply")){
			suggestion.setStatus(2);
			if(suggestion.getResult()==null){
				suggestion.setResult("");
				
			}
			request.setAttribute("suggestion", suggestion);
			return "/security/operator/suggestion_manage_reply.jsp" ; 
			
		}
		request.setAttribute("suggestion", suggestion);
	    return "/security/user/suggestion_manage_mod.jsp" ; 
	}

	@Callable
	public MethodResult addSuggestion(Map<String, Object> parameter) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		logger.debug("SuggestionServiceImpl.addSuggestion()");
		try
		{
 			String type = StringUtil.trim(parameter.get("type"));
 			type = "1";
			String talk = StringUtil.trim(parameter.get("talk"));
			if( type.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL , "类别不能为空");
			}
			
			SuggestionMapper suggestionMapper = this.sqlSession.getMapper(SuggestionMapper.class);
			
			Map<String, Object> suggestionData = new LinkedHashMap<String, Object>();
			suggestionData.put("id",       StringUtil.generateUUID());
			suggestionData.put("type", type);
			suggestionData.put("content", talk);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");
			String now  = sdf.format(new Date());
			suggestionData.put("submitTime",now );
			suggestionData.put("status", "1");  // 1：未答复 2： 已答复
			suggestionData.put("userId", loginInfo.getUserId());  // 1：未答复 2： 已答复
			int n = suggestionMapper.addSuggestion(suggestionData);
			
			if( n>0 )
			{
				new SendMail().sendSuggestion(loginInfo.getAccount(), "终端用户", talk);
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				return new MethodResult(MethodResult.SUCCESS, now);
			}
			else
			{
				return new MethodResult(MethodResult.SUCCESS, "添加失败");
			}
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("添加失败");
		}finally{
			try{
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "添加意见反馈");
				operLogData.put("operTime", StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "用户名:"+loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis()-begin);
				operLogMapper.addOperLog(operLogData);
			}catch(Exception e){
				logger.error(e);
			}
			
		}
	}
	@Callable
	public MethodResult updateSuggestion(Map<String, Object> parameter) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		logger.debug("SuggestionServiceImpl.updateSuggestion()");
		try
		{ 
			String status = StringUtil.trim(parameter.get("status"));
			String result = StringUtil.trim(parameter.get("result"));
			String id = StringUtil.trim(parameter.get("id"));
			
			if( result.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL , "答复内容不能为空");
			}
			
			SuggestionMapper suggestionMapper = this.sqlSession.getMapper(SuggestionMapper.class);
			
			Map<String, Object> suggestionData = new LinkedHashMap<String, Object>();
			suggestionData.put("id",       id);  
			suggestionData.put("status", status);  // 1：未答复 2： 已答复
			suggestionData.put("result", result);  // 1：未答复 2： 已答复
			suggestionData.put("replyTime", StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));  //时间
			int n = suggestionMapper.updateSuggestion(suggestionData);
			
			if( n>0 )
			{
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				return new MethodResult(MethodResult.SUCCESS, "添加成功");
			}
			else
			{
				return new MethodResult(MethodResult.SUCCESS, "添加失败");
			}
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("添加失败");
		}finally{
			try{
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "修改意见反馈");
				operLogData.put("operTime", StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "用户名:"+loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis()-begin);
				operLogMapper.addOperLog(operLogData);
			}catch(Exception e){
				logger.error(e);
			}
			
		}
	}

	@Callable
	public void querySuggestion(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("SuggestionServiceImpl.querySuggestion()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 参数处理
			String userId="";
			String type = StringUtil.trim(request.getParameter("type"));
			String status = StringUtil.trim(request.getParameter("status"));
			Integer page    = StringUtil.parseInteger(request.getParameter("page"), 1) - 1;	// 前台过来的是从1开始，需要的是从0开始
			Integer rows    = StringUtil.parseInteger(request.getParameter("rows"), 10);
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if(loginInfo.getUserType()==AppConstant.SYS_USER_TYPE_TERMINAL_USER){
				userId = loginInfo.getUserId();
			}
			// 查询数据库
			SuggestionMapper suggestionMapper = this.sqlSession.getMapper(SuggestionMapper.class);
			
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("type",  type);
			condition.put("status",  status);
			if(userId!=null&&userId.length()>0){
				condition.put("userId",  userId);				
			}
			condition.put("user",  status);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = suggestionMapper.queryPageCount(condition);			// 总行数
			List<SuggestionVO> suggestionList = suggestionMapper.queryPage(condition);	// 分页结果
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, suggestionList);
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
		
	}

}
