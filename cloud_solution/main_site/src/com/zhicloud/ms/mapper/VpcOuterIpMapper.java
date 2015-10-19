package com.zhicloud.ms.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.VpcOuterIpVO;


public interface VpcOuterIpMapper {
	
    /**
     * 添加IP
     * @param condition
     * @return
     */
    public int add(Map<String, Object> condition);
    
    /**
     * 批量删除IP
     * @param ips
     * @return
     */
    public int deleteIps(List<String> ips);
    
    /**
     * 查询一个vpc的所有IP
     * @param condition
     * @return
     */
    public List<VpcOuterIpVO> getAllIpByVpcId(Map<String,Object> condition);
    
    
    /**
     * 通过vpcid查询ip
     * @param condition
     * @return
     */
    public int getCountByVpcId(Map<String,Object> condition);
}

