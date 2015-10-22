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
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.SysRoleMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.SysRoleService;
import com.zhicloud.op.vo.SysRoleVO;

@Transactional(readOnly=true)
public class SysRoleServiceImpl extends BeanDirectCallableDefaultImpl implements SysRoleService
{
	public static final Logger logger = Logger.getLogger(SysRoleServiceImpl.class);
	
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	//---------------
	
	/**
	 * 
	 */
	@Callable
	public String managePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("SysRoleServiceImpl.managePage()");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.sys_role_manage_page)==false )
		{
			return "/public/have_not_access.jsp";
		}
		
		return "/security/admin/sys_role_manage.jsp";
	}
	
	/**
	 * 
	 */
	@Callable
	public String addPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("SysRoleServiceImpl.addPage()");
		return "/security/admin/sys_role_add.jsp";
	}
	
	/**
	 * 
	 */
	@Callable
	public String modPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("SysRoleServiceImpl.modPage()");
		String roleId = (String)request.getParameter("roleId");
		
		SysRoleMapper sysRoleMapper = this.sqlSession.getMapper(SysRoleMapper.class);
		SysRoleVO sysRole =  sysRoleMapper.getById(roleId);
		
		if( sysRole!=null )
		{
			request.setAttribute("sysRole", sysRole);
			return "/security/admin/sys_role_mod.jsp";
		}
		else
		{
			request.setAttribute("message", "找不到系统角色的记录");
			return "/public/warning_dialog.jsp";
		}
	}
	
	
	/**
	 * 
	 */
	@Callable
	public String setPrivilegePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("SysRoleServiceImpl.setPrivilegePage()");
		return "/security/admin/sys_role_set_privilege.jsp";
	}
	
	//--------------------
	
	/**
	 * 
	 */
	@Callable
	public void querySysRole(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("SysRoleServiceImpl.querySysRole()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 参数处理
			String roleName = StringUtil.trim(request.getParameter("role_name"));
			Integer page    = StringUtil.parseInteger(request.getParameter("page"), 1) - 1;	// 前台过来的是从1开始，需要的是从0开始
			Integer rows    = StringUtil.parseInteger(request.getParameter("rows"), 10);
			
			// 查询数据库
			SysRoleMapper sysRoleMapper = this.sqlSession.getMapper(SysRoleMapper.class);
			
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("roleName",  "%"+roleName+"%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = sysRoleMapper.queryPageCount(condition);			// 总行数
			List<SysRoleVO> roleList = sysRoleMapper.queryPage(condition);	// 分页结果
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, roleList);
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}
	
	/**
	 * 
	 */
	@Callable
	@Transactional(readOnly=false)
	public MethodResult addSysRole(Map<String, String> parameter)
	{
		logger.debug("SysRoleServiceImpl.addSysRole()");
		try
		{
			String roleName = StringUtil.trim(parameter.get("role_name"));
			
			if( roleName.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL , "角色名不能为空");
			}
			
			SysRoleMapper sysRoleMapper = this.sqlSession.getMapper(SysRoleMapper.class);
			SysRoleVO sysRole = sysRoleMapper.getByName(roleName);
			if( sysRole!=null )
			{
				return new MethodResult(MethodResult.FAIL, "角色名["+roleName+"]已存在");
			}
			
			Map<String, Object> roleData = new LinkedHashMap<String, Object>();
			roleData.put("id",       StringUtil.generateUUID());
			roleData.put("roleName", roleName);
			SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmssSSS");     
			roleData.put("createTime", sdf.format(new Date()));
			int n = sysRoleMapper.addSysRole(roleData);
			
			if( n>0 )
			{
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
		}
	}
	
	/**
	 * 
	 */
	@Callable
	@Transactional(readOnly=false)
	public MethodResult updateSysRoleById(Map<String, String> parameter)
	{
		logger.debug("SysRoleServiceImpl.updateSysRole()");
		try
		{
			String roleId   = parameter.get("role_id");
			String roleName = parameter.get("role_name");
			
			if( roleId==null )
			{
				throw new AppException("roleId不能为空");
			}
			if( roleName==null )
			{
				throw new AppException("roleName不能为空");
			}
			
			Map<String, Object> roleData = new LinkedHashMap<String, Object>();
			roleData.put("id", roleId);
			roleData.put("roleName", roleName);
			
			SysRoleMapper sysRoleMapper = this.sqlSession.getMapper(SysRoleMapper.class);
			SysRoleVO sysRoleVo = sysRoleMapper.getByName(roleName);
			if(sysRoleVo!=null && sysRoleVo.getId().equals(roleId)==false){
				return new MethodResult(MethodResult.FAIL, "角色名["+roleName+"]已存在");
			}
			int n = sysRoleMapper.updateSysRole(roleData);
			
			if( n>0 )
			{
				return new MethodResult(MethodResult.SUCCESS, "修改成功");
			}
			else
			{
				return new MethodResult(MethodResult.SUCCESS, "修改失败");
			}
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("修改失败");
		}
	}
	
	/**
	 * 
	 */
	@Callable
	@Transactional(readOnly=false)
	public MethodResult deleteSysRoleById(String roleId)
	{
		logger.debug("SysRoleServiceImpl.deleteSysRole()");
		try
		{
			if( roleId==null )
			{
				throw new AppException("roleId不能为空");
			}
			
			SysRoleMapper sysRoleMapper = this.sqlSession.getMapper(SysRoleMapper.class);
			int n = sysRoleMapper.deleteSysRoleById(roleId);
			
			if( n>0 )
			{
				return new MethodResult(MethodResult.SUCCESS, "删除成功");
			}
			else
			{
				return new MethodResult(MethodResult.SUCCESS, "删除失败");
			}
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("删除失败");
		}
	}
	
	/**
	 * 
	 */
	@Callable
	@Transactional(readOnly=false)
	public MethodResult deleteSysRoleByIds(List<?> roleIds)
	{
		logger.debug("SysRoleServiceImpl.deleteSysRoleByIds()");
		try
		{
			if( roleIds==null || roleIds.size()==0 )
			{
				throw new AppException("roleIds不能为空");
			}
			
			SysRoleMapper sysRoleMapper = this.sqlSession.getMapper(SysRoleMapper.class);
			int n = sysRoleMapper.deleteSysRoleByIds(roleIds.toArray(new String[0]));
			
			if( n>0 )
			{
				return new MethodResult(MethodResult.SUCCESS, "删除成功");
			}
			else
			{
				return new MethodResult(MethodResult.SUCCESS, "删除失败");
			}
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("删除失败");
		}
	}
	
	//-------------------
	
	
	

}

















