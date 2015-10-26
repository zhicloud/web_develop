package com.zhicloud.op.mybatis.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.CloudHostBillDetailVO;

public interface CloudHostBillDetailMapper
{
	
	public CloudHostBillDetailVO getOneUndoneReocrdBeforeTime(String beforeTime);
	
	public List<CloudHostBillDetailVO> getAllUndoneByHostIdBeforeTime(Map<String, Object> condition);
	
	public List<CloudHostBillDetailVO> queryCloudHostByDetailByBillId(String billId);
	
	public List<CloudHostBillDetailVO> getAllUndoneByUserId(Map<String, Object> condition);
	
	public CloudHostBillDetailVO queryCloudHostBillDetailByBillDetailId(String billDetailId);
	
	
	public List<CloudHostBillDetailVO> getDetailByMonthly(Map<String, Object> condition);
	public int getDetailByMonthlyCount(Map<String, Object> condition);	
	public List<CloudHostBillDetailVO> getDetailByHostMonthly(Map<String, Object> condition);
	public List<CloudHostBillDetailVO> getAllDetailByHostMonthly(Map<String, Object> condition);
	public int getDetailByHostMonthlyCount(Map<String, Object> condition);
	
	//--------------------
	
	public BigDecimal incomeCountForAgent(String agentId);
	public BigDecimal incomeCountPaymentForMonthly(String agentId);
	public BigDecimal incomeCountPaymentForDosage(String agentId); 
	public BigDecimal getIncomeCountByTime(Map<String, Object> condition);
	
	/**
	 * 业务总额
	 * @param agentId
	 * @return
	 */
	public BigDecimal countAllBill(String agentId); 
	/**
	 * 获取月度账单业务总额
	 * @param data
	 * @return
	 */
	public BigDecimal countMonthlyBill(Map<String, Object> data);
	/**
	 * 根据地域、时间和代理商id查询业务额
	 * @param data
	 * @return
	 */
	public BigDecimal countAllBillByRegion(Map<String, Object> data);
	
	//------------------
	
	public int addCloudHostBillDetail(Map<String, Object> data);
	
	public int updateFeeById(Map<String, Object> data);

	public int deleteById(String id);
	
	public int deleteAllNotPaid(Map<String, Object> data);
	
	
	public List<CloudHostBillDetailVO> getBillDetailByHostId(Map<String, Object> data);
	
}
