package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.SysGroupVO;

public interface SysGroupMapper
{

	public List<SysGroupVO> getAll();

	public int queryPageCount(Map<String, Object> condition);

	public List<SysGroupVO> queryPage(Map<String, Object> condition);

	public SysGroupVO getById(String groupId);

	public SysGroupVO getByGroupName(String groupName);

	//------------------

	public int addSysGroup(Map<String, Object> groupData);

	//------------------

	public int updateSysGroupById(Map<String, Object> groupData);

	//------------------

	public int deleteSysGroupById(String groupId);

	public int deleteSysGroupByIds(String[] groupIds);

}
