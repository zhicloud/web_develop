package com.zhicloud.op.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.mybatis.mapper.SysUserMapper;
import com.zhicloud.op.service.TestService;
import com.zhicloud.op.vo.SysUserVO;

@Transactional(readOnly=true)
public class TestServiceImpl implements TestService
{
	
	private static final Logger logger = Logger.getLogger(TestServiceImpl.class);
	
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}
	
	//---------------

	public void queryTermianlUser(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TestServiceImpl.queryTermianlUser()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			String key = StringUtil.trim(request.getParameter("key"));
			logger.debug("key="+key);
			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			List<SysUserVO> userList = sysUserMapper.getAll();
			
			ServiceHelper.writeJsonTo(response.getOutputStream(), userList);
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}
	
	//----------

	@Transactional(readOnly=false)
	public int addTermianlUser(Map<Object, Object> parameter)
	{
		logger.debug("TestServiceImpl.addTermianlUser()");
		try
		{
			String account = StringUtil.trim(parameter.get("account"));
			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id", StringUtil.generateUUID());
			userData.put("account", account);
			userData.put("type", 1);
			
			return sysUserMapper.addSysUser(userData);
			
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}
	

	//----------

	public int deleteTermianlUser(String userId)
	{
		logger.debug("TestServiceImpl.addTermianlUser()");
		try
		{
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			return sysUserMapper.deleteSysUser(userId);
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("删除失败");
		}
	}
	
}
