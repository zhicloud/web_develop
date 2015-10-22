package com.zhicloud.op.mybatis.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.CashCouponVO;


public interface CashCouponMapper {
	
	public int addCashCoupon(Map<String, Object> data);
	
	public int updateSendTimeAndStatus(Map<String, Object> data);
	
	public int updateStatusOther(Map<String, Object> data);
	
	public int deleteCashCouponByIds(List<String> cashCouponIds);
	
	public int deleteCashCouponByCreaterId(List<String> createrIds);
	
	//---------------
	
	public int                 queryPageCount(Map<String, Object> condition);
	public List<CashCouponVO>  queryPage(Map<String, Object> condition);
	
	//----------------
	
	public CashCouponVO getCashCouponById(String id);
	
	public CashCouponVO getCashCouponByCode(String cashCode);
	
	public BigDecimal getTotalMoney(Map<String, Object> condition);
}
