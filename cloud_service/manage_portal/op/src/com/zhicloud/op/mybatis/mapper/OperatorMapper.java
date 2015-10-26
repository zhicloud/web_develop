package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.OperatorVO;

public interface OperatorMapper
{

	public List<OperatorVO> getAll();

	public int queryPageCount(Map<String, Object> condition);

	public List<OperatorVO> queryPage(Map<String, Object> condition);

	public OperatorVO getOperatorById(String id);
	
	public OperatorVO getBasicInformationById(String id);

	public OperatorVO getOperatorByAccount(String account);

	public int addOperator(Map<String, Object> data);

	public int updateOperator(Map<String, Object> data);

	public int deleteOperatorById(String userId);
	
	public int deleteOperatorByIds(String[] roleId);
	
	public List<OperatorVO> getByName(String name);

}
