package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.SysRoleVO;

public interface SysRoleMapper
{
	
	public List<SysRoleVO> getAll();
	
	public int queryPageCount(Map<String, Object> condition);
	
	public List<SysRoleVO> queryPage(Map<String, Object> condition);
	
	public SysRoleVO getById(String roleId);
	
	public SysRoleVO getByName(String roleName);

	//-----------------
	
	public int addSysRole(Map<String, Object> roleData);
	
	public int updateSysRole(Map<String, Object> roleData);
	
	public int deleteSysRoleById(String roleId);
	
	public List<SysRoleVO> getRoleByGroupId(String groupId);
	
	public List<SysRoleVO> getRoleNotInGroupId(String groupId);
	
	public int addRoleGroupRelation(Map<String,Object> roleData);
	
	public int deleteRoloGroupRelation(Map<String,Object> roleData);
	
	
	public int deleteSysRoleByIds(String[] roleId);
	
	
}
