package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.vo.SysUserVO;

public interface SysUserMapper
{
	
	public List<SysUserVO> getAll();
	
	public int queryPageCount(Object condition);
	
	public List<SysUserVO> queryPage(Object condition);
	
	public int queryPageNotInGroupIdCount(Object condition);
	
	public List<SysUserVO> queryPageNotInGroupId(Object condition);
	
	public int queryPageNotInImageIdCount(Object condition);
	
	public List<SysUserVO> queryPageNotInImageId(Object condition);
	
	public List<SysUserVO> getUserFromGroupItem(String groupId);
	
	public SysUserVO getById(String id);
	
	public SysUserVO getByTypeAndAccount(Object condition);
	
	public SysUserVO getByTypeAndAccountAndPassword(Object condition);
	
	public SysUserVO getByAccount(String account);
	
	public SysUserVO getUserPassword(String id);
	
	public List<SysUserVO> getByGroupId(String groudId);
	
	
	public List<SysUserVO> queryUserInImageId(String sysImageId);
	
	//---------------------
	
	public int addSysUser(Map<String, Object> data);

	//---------------------
	
	public int updateSysUser(Map<String, Object> data);
	
	public int updateGroupId(Map<String, Object> data);
	
	public int updateGroupIdById(Map<String, Object> data);

	public int updateGroupIdByGroupId(Map<String, Object> data);
	
	public int updateSysUserPassword(Map<String, Object> data);
	
	public int updateSysUserAccount(Map<String, Object> data);
	
	//---------------------
	
	public int deleteSysUser(String userId);
	
	public int deleteSysUsers(String[] userId);
	
	public List<SysUserVO> getAllOperator();
	
	
}
