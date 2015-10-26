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
import com.zhicloud.op.mybatis.mapper.SysGroupMapper;
import com.zhicloud.op.mybatis.mapper.SysRoleGroupRelationMapper;
import com.zhicloud.op.mybatis.mapper.SysRoleMapper;
import com.zhicloud.op.mybatis.mapper.SysUserMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.SysGroupService;
import com.zhicloud.op.vo.SysGroupVO;
import com.zhicloud.op.vo.SysRoleVO;
import com.zhicloud.op.vo.SysUserVO;

@Transactional(readOnly=true)
public class SysGroupServiceImpl extends BeanDirectCallableDefaultImpl implements SysGroupService
{
	public static final Logger logger = Logger.getLogger(SysGroupServiceImpl.class);
	
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
		logger.debug("SysGroupServiceImpl.managePage()");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.sys_group_manage_page)==false )
		{
			return "/public/have_not_access.jsp";
		}
		
		return "/security/admin/sys_group_manage.jsp";
	}
	
	//-------------------
	
	
	

/**
	 * 
	 */
	@Callable
	public String addPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("SysGroupServiceImpl.addPage()");

		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.sys_group_manage_add)==false )
		{
			return "/public/have_not_access.jsp";
		}
		
		return "/security/admin/sys_group_add.jsp";
	}
	
	/**
	 * 
	 */
	@Callable
	public String modPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("SysGroupServiceImpl.modPage()");
		String groupId = (String)request.getParameter("groupId");
		
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		SysGroupVO sysGroup =  sysGroupMapper.getById(groupId);
		
		if( sysGroup!=null )
		{
			request.setAttribute("sysGroup", sysGroup);
			return "/security/admin/sys_group_mod.jsp";
		}
		else
		{
			request.setAttribute("message", "找不到权限组的记录");
			return "/public/warning_dialog.jsp";
		}
	}

	@Callable
	@Override
	public String setGroupMemberPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("SysGroupServiceImpl.setGroupMemberPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.sys_group_manage_set_member)==false )
		{
			return "/public/have_not_access.jsp";
		}
		
		// 参数处理
		String groupId = StringUtil.trim(request.getParameter("groupId"));
		if( StringUtil.isBlank(groupId) )
		{
			throw new AppException("groupId不能为空");
		}
		
		request.setAttribute("groupId", groupId);

		SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		
		// 查找组的用户
		List<SysUserVO> groupSysUserList = sysUserMapper.getByGroupId(groupId);
		request.setAttribute("groupSysUserList", groupSysUserList);
		
		SysGroupVO sysGroup = sysGroupMapper.getById(groupId);
		if( sysGroup != null )
		{
			request.setAttribute("sysGroup", sysGroup);
			return "/security/admin/sys_group_set_member.jsp";
		}
		else
		{
			request.setAttribute("message", "找不到权限组的记录");
			return "/public/warning_dialog.jsp";
		}
	}
	
	@Callable
	public String setRolePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("SysGroupServiceImpl.setRolePage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.sys_group_manage_set_role)==false )
		{
			return "/public/have_not_access.jsp";
		}
		
		// 参数处理
		String groupId = StringUtil.trim(request.getParameter("groupId"));
		if( StringUtil.isBlank(groupId) )
		{
			throw new AppException("groupId不能为空");
		}
		

		SysRoleMapper sysRoleMapper = this.sqlSession.getMapper(SysRoleMapper.class);
		
		// 查询角色列表
		List<SysRoleVO> notSelectedRoleList = sysRoleMapper.getRoleNotInGroupId(groupId);
		List<SysRoleVO> selectedRoleList    = sysRoleMapper.getRoleByGroupId(groupId);
		request.setAttribute("notSelectedRoleList", notSelectedRoleList);
		request.setAttribute("selectedRoleList",    selectedRoleList);
		request.setAttribute("groupId", groupId);
		
		return "/security/admin/sys_group_set_role.jsp";
	}

	@Callable
	public void querySysGroup(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("SysGroupServiceImpl.querySysGroup()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 参数处理
			String groupName = StringUtil.trim(request.getParameter("group_name"));
			Integer page     = StringUtil.parseInteger(request.getParameter("page"), 1) - 1;	// 前台过来的是从1开始，需要的是从0开始
			Integer rows     = StringUtil.parseInteger(request.getParameter("rows"), 10);
			
			SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
			
			// 查询数据库
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("groupName", "%"+groupName+"%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = sysGroupMapper.queryPageCount(condition);
			List<SysGroupVO> groupList = sysGroupMapper.queryPage(condition);
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, groupList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}

	}
	
	@Callable
	@Transactional(readOnly = false)
	public void queryUserFromGroupItem(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("SysGroupServiceImpl.queryUserFromGroupItem()");
		String groupId = request.getParameter("groupId");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 查询数据库
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			List<SysUserVO> userList = sysUserMapper.getUserFromGroupItem(groupId);
			// 输出json数据
			ServiceHelper.writeJsonTo(response.getOutputStream(), userList);
		}
		catch( AppException e )
		{ 
			logger.error(e);
			throw new AppException("查询失败");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}

	@Callable
	@Transactional(readOnly=false)
	public MethodResult addSysGroup(Map<String, String> parameter)
	{
		logger.debug("SysGroupServiceImpl.addSysGroup()");
		try
		{
			// 权限判断
			HttpServletRequest request = RequestContext.getHttpRequest();
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege(PrivilegeConstant.sys_group_manage_add)==false )
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限操作此功能!");
			}
			
			// 参数处理
			String groupName = StringUtil.trim(parameter.get("groupName"));
			if( StringUtil.isBlank(groupName) )
			{
				throw new AppException("groupName不能为空");
			}
			
			SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
			
			// 判断组名是否重复
			SysGroupVO sysGroup = sysGroupMapper.getByGroupName(groupName);
			if( sysGroup!=null )
			{
				return new MethodResult(MethodResult.FAIL, "组名["+groupName+"]已存在");
			}
			
			// 插入数据库
			Map<String, Object> groupData = new LinkedHashMap<String, Object>();
			groupData.put("id",        StringUtil.generateUUID());
			groupData.put("groupName", groupName);
			
			int n = sysGroupMapper.addSysGroup(groupData);
			if( n>0 )
			{
				return new MethodResult(MethodResult.SUCCESS, "添加成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "添加失败");
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
	public MethodResult updateSysGroupById(Map<String, String> parameter)
	{
		logger.debug("SysGroupServiceImpl.updateSysGroupById()");
		try
		{
			// 权限判断
			HttpServletRequest request = RequestContext.getHttpRequest();
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege(PrivilegeConstant.sys_group_manage_modify)==false )
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限操作此功能!");
			}
			
			// 参数处理
			String groupId   = parameter.get("id");
			String groupName = parameter.get("groupName");
			
			if( StringUtil.isBlank(groupId) )
			{
				throw new AppException("groupId不能为空");
			}
			if( StringUtil.isBlank(groupName) )
			{
				throw new AppException("groupName不能为空");
			}
			
			SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
			
			// 判断组名是否已经存在
			SysGroupVO sysGroup = sysGroupMapper.getByGroupName(groupName);
			if( sysGroup!=null && sysGroup.getId().equals(groupId)==false )
			{
				return new MethodResult(MethodResult.FAIL, "组名["+groupName+"]已存在");
			}
			
			// update数据库
			Map<String, Object> groupData = new LinkedHashMap<String, Object>();
			groupData.put("id",        groupId);
			groupData.put("groupName", groupName);
			int n = sysGroupMapper.updateSysGroupById(groupData);
			if( n>0 )
			{
				return new MethodResult(MethodResult.SUCCESS, "修改成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "修改失败");
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
	public MethodResult deleteSysGroupByIds(List<String> groupIds)
	{
		logger.debug("SysGroupServiceImpl.deleteByIds()");
		try
		{
			// 参数判断
			if( groupIds==null || groupIds.size()==0 )
			{
				throw new AppException("groupIds不能为空");
			}
			
			// 删除数据
			SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
			int n = sysGroupMapper.deleteSysGroupByIds(groupIds.toArray(new String[0]));
			if( n>0 )
			{
				return new MethodResult(MethodResult.SUCCESS, "删除成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "删除失败");
			}
		}
		catch (AppException e)
		{ 
			logger.error(e);
			throw new AppException("修改失败");
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
	public MethodResult deleteSysGroupById(String groupId)
	{
		logger.debug("SysGroupServiceImpl.deleteSysGroup()");
		try
		{
			if( groupId==null )
			{
				throw new AppException("groupId不能为空");
			}
			
			SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
			int n = sysGroupMapper.deleteSysGroupById(groupId);
			
			if( n>0 )
			{
				return new MethodResult(MethodResult.SUCCESS, "删除成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "删除失败");
			}
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("删除失败");
		}
	}

	@Callable
	@Transactional(readOnly=false)
	public MethodResult addGroupItem(Map<String,String> parameter) {
		logger.debug("SysGroupServiceImpl.addGroupItem()");
		try
		{
			String account = parameter.get("account");
			String groupId = parameter.get("groupId");
			if( account==null )
			{
				throw new AppException("account不能为空");
			}
			if( groupId==null )
			{
				throw new AppException("groupId不能为空");
			}
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			
			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			SysUserVO user = sysUserMapper.getByAccount(account);
			
			if( user!=null )
			{
				userData.put("groupId", groupId);
				userData.put("account", account);
				int n = sysUserMapper.updateGroupId(userData);
				if(n>0){
					return new MethodResult(MethodResult.SUCCESS, "添加成功");
				}else{
					return new MethodResult(MethodResult.FAIL, "添加失败");
				}
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "用户不存在");
			}
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("添加失败");
		}
	}

	@Callable
	@Transactional(readOnly=false)
	public MethodResult deleteUserFromGroupItem(String account) {
		logger.debug("SysGroupServiceImpl.deleteUserFromGroupItem()");
		try
		{
			if( account==null )
			{
				throw new AppException("account不能为空");
			}
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("groupId",null);
			userData.put("account", account);
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			int n = sysUserMapper.updateGroupId(userData);
			
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

	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateRoleFromGroupItem(String groupId, List<String> selectedRoleIds)
	{
		logger.debug("SysGroupServiceImpl.updateRoleFromGroupItem()");
		
		// 权限判断
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.sys_group_manage_set_role)==false )
		{
			return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
		}
		
		MethodResult result = new MethodResult();
		
		// 参数处理
		if( StringUtil.isBlank(groupId) )
		{
			throw new AppException("groupId不能为空");
		}
		if( selectedRoleIds.size()==0 )
		{
			throw new AppException("selectedRoleIds不能为空");
		}
		
		// 先删除与group关联的角色
		SysRoleGroupRelationMapper sysRoleGroupRelationMapper = this.sqlSession.getMapper(SysRoleGroupRelationMapper.class);
		int deleteCount = sysRoleGroupRelationMapper.deleteSysRoleGroupRelationByGroupId(groupId);
		result.put("deleteCount", deleteCount);
		
		// 添加进数据库
		int addCount = 0;
		for( String roleId : selectedRoleIds )
		{
			if( StringUtil.isBlank(roleId) )
			{
				continue;
			}
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("id",      StringUtil.generateUUID());
			data.put("roleId",  roleId);
			data.put("groupId", groupId);
			addCount += sysRoleGroupRelationMapper.addSysRoleGroupRelation(data);
		}
		result.put("addCount", addCount);
		
		result.status = MethodResult.SUCCESS;
		result.message = "操作成功";
		return result;
	}
	
	
	@Callable
	@Transactional(readOnly=false)
	public MethodResult setGroupMembers(String groupId, List<String> userIds)
	{
		HttpServletRequest request = RequestContext.getHttpRequest();

		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.sys_group_manage_set_member)==false )
		{
			return new MethodResult(MethodResult.FAIL, "您没有权限操作此功能!");
		}
		
		// 参数处理
		if( StringUtil.isBlank(groupId) )
		{
			throw new AppException("groupId不能为空");
		}
		
		SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
		
		// 移除组的原来用户
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("oldGroupId", groupId);
		condition.put("newGroupId", null);
		sysUserMapper.updateGroupIdByGroupId(condition);
		
		// 更新sys_user的group_id字段
		for( String userId : userIds )
		{
			Map<String, Object> sysUserData = new LinkedHashMap<String, Object>();
			sysUserData.put("id", userId);
			sysUserData.put("groupId", groupId);
			sysUserMapper.updateGroupIdById(sysUserData);
		}
		
		return new MethodResult(MethodResult.SUCCESS, "操作成功");
		
	}

	
}

















