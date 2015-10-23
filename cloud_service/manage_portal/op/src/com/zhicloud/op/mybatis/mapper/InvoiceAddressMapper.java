package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.InvoiceAddressVO;

public interface InvoiceAddressMapper {

	public int queryPageCount(Map<String, Object> condition);

	public List<InvoiceAddressVO> queryPage(Map<String, Object> condition);

	public List<InvoiceAddressVO> getInvoiceAddress(String userId);

	public InvoiceAddressVO getInvoiceAddressById(String id);

	public int addInvoiceAddress(Map<String, Object> data);
	
	public int updateInvoiceAddress(Map<String, Object> data);
	
	public int deleteAddressByIds(String[] ids);

}
