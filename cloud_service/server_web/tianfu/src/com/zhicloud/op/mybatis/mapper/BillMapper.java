package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.BillVO;

public interface BillMapper
{
	
	public int queryPageCount(Map<String, Object> condition);
	
	
	
	public List<BillVO> queryPage(Map<String, Object> condition);
	public List<BillVO> queryPageForAgent(Map<String, Object> condition);
	public List<BillVO> queryPageForTerminal(Map<String, Object> condition);
	
	public BillVO getById(String id);
	
	public List<BillVO> getByUserId(String userName);
	
	//---------------------
	
	public int addBill(Map<String, Object> data);
	
	public int deleteByIds(String[] billIds);
	
}
