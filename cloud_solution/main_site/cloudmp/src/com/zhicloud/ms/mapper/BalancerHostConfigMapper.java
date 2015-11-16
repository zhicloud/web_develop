package com.zhicloud.ms.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.BalancerHostConfigVO;


public interface BalancerHostConfigMapper {
	
    /**
     * 添加负载均衡服务
     * @param condition
     * @return
     */
    public int add(Map<String, Object> condition);
    
    /**
     * 删除负载均衡服务
     * @param id
     * @return
     */
    public int deleteById(String id);
    
    /**
     * 查询所有负载均衡服务
     * @return
     */
    public List<BalancerHostConfigVO> getAll();
    
    
    /**
     * 通过id查询负载均衡服务
     * @param id
     * @return
     */
    public BalancerHostConfigVO getById(String id);
    
    /**
     * 通过主机id查询负载均衡服务
     * @param hostId
     * @return
     */
    public List<BalancerHostConfigVO> getByHostId(String hostId);
}

