package com.zhicloud.op.mybatis.mapper;

import java.math.BigDecimal;
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
	
	/**
	 * 根据代理商和时间查询所有收入
	 * 
	 * @param condition
	 * @return
	 */
	public BigDecimal getIncomeByTime(Map<String,Object> condition);
	
	/**
	 * 根据时间和地域查询消费额
	 * 
	 * @param condition
	 * @return
	 */
	public BigDecimal getIncomeByTimeAndRegion(Map<String,Object> condition);
	
	/**
	 * 按月查询消费额
	 * 
	 * @param condition
	 * @return
	 */
	public List<BillVO> getDetailByMonthly(Map<String,Object> condition);
	
	/**
	 * 查询月份总数
	 * 
	 * @param condition
	 * @return
	 */
	public int getDetailByMonthlyCount(Map<String,Object> condition);
	
	/**
	 * 查询某月的业务额
	 * 
	 * @param condition
	 * @return
	 */
	public BigDecimal countMonthlyBill(Map<String,Object> condition);
	
	/**
	 * 查询每月消费详情的总记录数
	 * 
	 * @param condition
	 * @return
	 */
	public int getDetailByHostMonthlyCount(Map<String,Object> condition);
	
	/**
	 * 查询每月消费的详细记录
	 * 
	 * @param condition
	 * @return
	 */
	public List<BillVO> getDetailByHostMonthly(Map<String,Object> condition);
	
}
