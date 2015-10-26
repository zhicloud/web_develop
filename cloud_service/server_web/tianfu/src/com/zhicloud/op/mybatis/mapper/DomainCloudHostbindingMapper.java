package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.DomainCloudHostBindingVO;


public interface DomainCloudHostbindingMapper {

	public int queryPageCount(Map<String, Object> condition);

	public List<DomainCloudHostBindingVO> queryPage(Map<String, Object> condition);
	
	public DomainCloudHostBindingVO queryDomainByDomain(String name);
	
	public int addDomain(Map<String, Object> data);

	public int deleteDomainByIds(String[] id);
	
}
