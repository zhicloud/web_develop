package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.AccountBalanceDetailVO;


public interface AccountBalanceDetailMapper
{

	/**
	 * 新增余额变动明细记录
	 * @param condition
	 * @return
	 */
	public int addAccountBalanceDetail(Map<String, Object> condition);
	
	/**
	 * 充值之后更新表数据
	 * @param condition
	 * @return
	 */
	public int updateAfterRecharge(Map<String, Object> condition);
	
	/**
	 * 开发票后更新invoice_id
	 * @param condition
	 * @return
	 */
	public int updateInvoiceId(Map<String, Object> condition);
	
	/**
	 * 根据ID获取余额变动记录
	 * @param id
	 * @return
	 */
	public AccountBalanceDetailVO getBalanceDetailById(String id);
	
	/**
	 * 根据用户ID获取充值记录
	 * @param userId
	 * @return
	 */
	public List<AccountBalanceDetailVO> getRechargeDetailByUserId(Map<String, Object> condition);
	
	/**
	 * 根据ID获取充值记录
	 * @param userId
	 * @return
	 */
	public  List<AccountBalanceDetailVO> getRechargeDetailById(Map<String, Object> condition);
	
	/**
	 * 根据用户ID获取充值记录总数
	 * @param userId
	 * @return
	 */
	public int getRechargeDetailByUserIdCount(Map<String, Object> condition);
	/**
	 * 获取对账单
	 * @param userId
	 * @return
	 */
	public List<AccountBalanceDetailVO> getDetail(Map<String, Object> condition);
	/**
	 * 获取对账单总数
	 * @param userId
	 * @return
	 */
	public int getDetailCount(Map<String, Object> condition);
	
	/**
	 * 根据用户ID获取未开发票的充值记录
	 * @param userId
	 * @return
	 */
	public List<AccountBalanceDetailVO> getBalanceDetailByUserIdForInvoice(Map<String, Object> condition);

	/**
	 * 根据代理商ID获取未开发票的充值记录
	 * @param userId
	 * @return
	 */
	public List<AccountBalanceDetailVO> getBalanceDetailByUserIdForInvoiceForAgent(Map<String, Object> condition);
	
	/**
	 * 根据用户ID获取未开发票的充值记录总数
	 * @param userId
	 * @return
	 */
	public int getBalanceDetailByUserIdCountForInvoice(Map<String, Object> condition);
	
	public String getAllAccount(Map<String, Object> condition);
	
}
