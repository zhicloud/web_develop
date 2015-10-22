package com.zhicloud.op.service.impl;

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
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.MarkBindUserMapper;
import com.zhicloud.op.mybatis.mapper.MarkMapper;
import com.zhicloud.op.mybatis.mapper.OperLogMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.MarkService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.MarkVO;
import com.zhicloud.op.vo.OperLogVO;
@Transactional(readOnly = true)
public class MarkServiceImpl extends BeanDirectCallableDefaultImpl implements MarkService {
	
	public static final Logger logger = Logger.getLogger(OperatorServiceImpl.class);

	private SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	@Callable
	public String managePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("MarkServiceImpl.managePage()"); 
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.mark_manage_page) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		request.setAttribute("userType", loginInfo.getUserType());
		return "/security/operator/mark_manage.jsp";
	}

	@Callable
	public void queryOperLog(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("OperLogServiceImpl.queryOperator()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
			Integer userType = Integer.valueOf(request.getParameter("userType"));
			String userId = "";
			if(userType==AppConstant.SYS_USER_TYPE_TERMINAL_USER){		 
				LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
				userId = loginInfo.getUserId();
			}else if(userType==AppConstant.SYS_USER_TYPE_AGENT){
				userId = request.getParameter("terminalUserId"); 	
				
			}else if(userType==AppConstant.SYS_USER_TYPE_OPERATOR){
				userId = request.getParameter("id");
			}
			
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
			String startTime = StringUtil.trim(request.getParameter("startTime"));
			String endTime = StringUtil.trim(request.getParameter("endTime"));

			// 查询数据库
			OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("userId", userId);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			condition.put("startTime", startTime);
			condition.put("endTime", endTime);
			int total = operLogMapper.queryPageCount(condition); // 总行数
			List<OperLogVO> operLogList = operLogMapper.queryPage(condition);// 分页结果

			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, operLogList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}

	}
	@Callable
	public String managePageForAgent(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("OperLogServiceImpl.managePageForAgent()"); 
		Integer userType = Integer.valueOf(request.getParameter("userType"));
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if(userType==AppConstant.SYS_USER_TYPE_AGENT){
			request.setAttribute("terminalUserId", loginInfo.getUserId());  
			return "/security/agent/oper_log_agent_manage.jsp";			
		}else{
			return "/public/page_not_exists.jsp";
		}
	}
	@Callable
	public void queryOperLogForAgent(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("OperLogServiceImpl.queryOperaLogForAgent()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
			String userId = request.getParameter("terminalUserId"); 	
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
			String startTime = StringUtil.trim(request.getParameter("startTime"));
			String endTime = StringUtil.trim(request.getParameter("endTime")); 
			// 查询数据库
			OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("userId", userId);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			condition.put("startTime", startTime);
			condition.put("endTime", endTime);
			int total = operLogMapper.queryPageCount(condition); // 总行数
			List<OperLogVO> operLogList = operLogMapper.queryPage(condition);// 分页结果

			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, operLogList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}

	}
	@Callable
	@Override
	public void queryAllMarks(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("MarkServiceImpl.queryAllMarks()");
		try{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request,  AppConstant.SYS_USER_TYPE_OPERATOR);

			// 获取参数
			String markName = StringUtil.trim(request.getParameter("mark_name"));
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
			// 查询数据库
			MarkMapper markMapper = this.sqlSession.getMapper(MarkMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("markName", "%" + markName + "%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = markMapper.queryPageCount(condition); // 总行数
			List<MarkVO> markList = markMapper.queryPage(condition);// 分页结果
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, markList);
		}catch( Exception e )
		{
			logger.error("CloudDiskServiceImpl.queryCloudDisk()",e); 
			throw new AppException("查询失败");
		}
	}

	@Callable
	public String addPage(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("MarkServiceImpl.addPage()");
		return "/security/operator/mark_add.jsp";
	}

	@Callable
	@Transactional(readOnly=false)
	public MethodResult addMark(Map<String, String> parameter) {
		try
		{
			String name           = StringUtil.trim(parameter.get("name"));
			String description    = StringUtil.trim(parameter.get("description"));

			if( StringUtil.isBlank(name) )
			{
				throw new AppException("用户名不能为空");
			}

			MarkMapper markMapper = this.sqlSession.getMapper(MarkMapper.class);
			
			// 判断账号是否已经存在
			MarkVO markVO = markMapper.getMarkByName(name);
			if( markVO != null )
			{
				return new MethodResult(MethodResult.FAIL, "标签名[" + name + "]已经存在");
			}
			
			Map<String, Object> markData = new LinkedHashMap<String, Object>();
			markData.put("id",       StringUtil.generateUUID());
			markData.put("name",  name);
			markData.put("description",  description);
			int n = markMapper.addMark(markData);
			
			if( n > 0)
			{   
				return new MethodResult(MethodResult.SUCCESS, "添加成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "添加失败");
			}

		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("添加失败");
		}
	}
	
	@Callable
	public String modPage(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("MarkServiceImpl.addPage()");
		MarkMapper markMapper = this.sqlSession.getMapper(MarkMapper.class);
		String markId = request.getParameter("markId");
		MarkVO markVO = markMapper.getMarkById(markId);
		request.setAttribute("markVO", markVO);
		return "/security/operator/mark_mod.jsp";
	}

	@Callable
	@Transactional(readOnly=false)
	public MethodResult modMark(Map<String, String> parameter) {
		logger.debug("MarkServiceImpl.modMark()");
		try
		{
			String id             = StringUtil.trim(parameter.get("id"));
			String name           = StringUtil.trim(parameter.get("name"));
			String description    = StringUtil.trim(parameter.get("description"));

			if( StringUtil.isBlank(name) )
			{
				throw new AppException("用户名不能为空");
			}

			MarkMapper markMapper = this.sqlSession.getMapper(MarkMapper.class);
			
			// 判断账号是否已经存在
			MarkVO markVO = markMapper.getMarkByName(name);
			if( markVO != null && (!id.equals(markVO.getId())))
			{
				return new MethodResult(MethodResult.FAIL, "标签名[" + name + "]已经存在");
			}
			
			Map<String, Object> markData = new LinkedHashMap<String, Object>();
			markData.put("id",    id);
			markData.put("name",  name);
			markData.put("description",  description);
			int n = markMapper.updateMarkById(markData);
			
			if( n > 0)
			{   
				return new MethodResult(MethodResult.SUCCESS, "修改成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "修改失败");
			}

		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}
	}

	@Callable
	@Transactional(readOnly=false)
	public MethodResult deleteMarkByIds(List<String> ids) {
		logger.debug("MarkServiceImpl.modMark()");
		try
		{
			if(ids==null || ids.size()<1){
				return new MethodResult(MethodResult.FAIL,"请选择要删除的项");
			}
			MarkMapper markMapper = this.sqlSession.getMapper(MarkMapper.class);
			MarkBindUserMapper markBindUserMapper = this.sqlSession.getMapper(MarkBindUserMapper.class);
			
			/**
			 * 删除mark表
			 */
			int n = markMapper.deleteByMarkIds(ids);
			/**
			 * 删除关联表
			 */
			int m = markBindUserMapper.deleteByMarkIds(ids);
			if(n>0){
				return new MethodResult(MethodResult.SUCCESS,"删除成功");
			}
			return new MethodResult(MethodResult.FAIL,"删除失败");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("删除失败");
		}
	}
	
    /**
     * @Description:取得特殊的标签对象
     * @param name 标签名称
     * @return MarkVO
     */
    public MarkVO getSpecialMark(String name) {
        MarkMapper markMapper = this.sqlSession.getMapper(MarkMapper.class);
        // 判断账号是否已经存在
        MarkVO markVO = markMapper.getMarkByName(name);
        return markVO;
    }
}
