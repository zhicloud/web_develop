package com.zhicloud.op.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo; 
import com.zhicloud.op.mybatis.mapper.AgentMapper;
import com.zhicloud.op.mybatis.mapper.SysUserMapper;
import com.zhicloud.op.mybatis.mapper.UserMessageMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.UserMessageService;
import com.zhicloud.op.service.constant.AppConstant; 
import com.zhicloud.op.vo.UserMessageVO;
@Transactional(readOnly=true)
public class UserMessageServiceImpl extends BeanDirectCallableDefaultImpl implements UserMessageService {
    public static final Logger logger = Logger.getLogger(OrderInfoServiceImpl.class);
	
	private SqlSession sqlSession;
	
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	@Callable
	public void queryUserMessage(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("UserMessageServiceImpl.queryUserMessage()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
			
			String userId = loginInfo.getUserId();
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);

			// 查询数据库
			UserMessageMapper userMessageMapper = this.sqlSession.getMapper(UserMessageMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("userId", userId);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = userMessageMapper.getUserMessageCount(condition); // 总行数
			List<UserMessageVO> userMessageList = userMessageMapper.getUserMessage(condition);// 分页结果
			if(userMessageList!=null&&userMessageList.size()>0){
				for(UserMessageVO message:userMessageList){
					if(message.getCreateTime()!=null){
						message.setContent("<span style='color:#999;font-size:10px;'>"+StringUtil.formatDateString(message.getCreateTime(), "yyyyMMddHHmmssSSS","yyyy-MM-dd HH:mm:ss")+"</span><br />"+message.getContent());
					}
				}
			}

			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, userMessageList);
		}
		catch( AppException e )
		{
			logger.error("查询失败", e);
			throw e;
		}
		catch( Exception e )
		{
			logger.error("查询失败", e);
			throw new AppException("查询失败");
		}

	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteUserMessage(String id)
	{
		logger.debug("AgentServiceImpl.deleteAgentById()");
		try
		{
			if( id == null )
			{
				throw new AppException("未选中消息");
			}

			UserMessageMapper userMessageMapper = this.sqlSession.getMapper(UserMessageMapper.class);
			int n = userMessageMapper.deleteUserMessage(id);

			 

			if( n > 0 )
			{
				return new MethodResult(MethodResult.SUCCESS, "删除成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "删除失败");
			}
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("删除失败");
		}
	}

}
