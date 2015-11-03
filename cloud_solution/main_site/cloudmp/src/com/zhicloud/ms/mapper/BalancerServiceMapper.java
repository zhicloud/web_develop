package com.zhicloud.ms.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.BalancerServiceVO;


public interface BalancerServiceMapper {
	
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
    public List<BalancerServiceVO> getAll();
    
    
    /**
     * 通过id查询负载均衡服务
     * @param id
     * @return
     */
    public BalancerServiceVO getById(String id);
    
    /**
     * 通过主机id查询负载均衡服务
     * @param hostId
     * @return
     */
    public List<BalancerServiceVO> getByHostId(String hostId);
}

