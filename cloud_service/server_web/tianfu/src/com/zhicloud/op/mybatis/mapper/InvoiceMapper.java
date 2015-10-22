package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.InvoiceVO;

public interface InvoiceMapper {

	public int queryPageCount(Map<String, Object> condition);
	
	public List<InvoiceVO> queryPage(Map<String, Object> condition);
	
	public int queryAllPageCount(Map<String, Object> condition);
	
	public List<InvoiceVO> queryAllPage(Map<String, Object> condition);
	
	public InvoiceVO getInvoiceById(String id);
	
	public int addInvoice(Map<String, Object> data);
	
	public int updateInvoiceStatus(Map<String, Object> data);
	
}
