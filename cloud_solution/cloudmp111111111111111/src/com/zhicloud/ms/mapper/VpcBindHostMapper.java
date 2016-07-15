package com.zhicloud.ms.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.VpcBindHostVO;


public interface VpcBindHostMapper {
	
	/**
	 * 增加一条绑定信息
	 * @param condition
	 * @return
	 */
	public int add(Map<String, Object> condition);
	
	/**
	 * 
	 * 逻辑删除绑定记录
	 * @param condition
	 * @return
	 */
	public int deleteLogical(Map<String, Object> condition);
	
	
	/**
	 * 根据vpcid查询主机信息
	 * @param vpcId
	 * @return
	 */
	public  List<VpcBindHostVO> getByVpcId(String vpcId);

}

