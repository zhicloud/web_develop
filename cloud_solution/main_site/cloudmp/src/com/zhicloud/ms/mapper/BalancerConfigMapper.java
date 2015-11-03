package com.zhicloud.ms.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.BalancerConfigVO;


public interface BalancerConfigMapper {
	
    /**
     * 添加负载均衡器配置
     * @param condition
     * @return
     */
    public int add(Map<String, Object> condition);
    
    /**
     * 删除负载均衡器配置
     * @param id
     * @return
     */
    public int deleteById(String id);
    
    /**
     * 查询所有负载均衡器配置
     * @return
     */
    public List<BalancerConfigVO> getAll();
    
    
    /**
     * 通过id查询负载均衡器配置
     * @param id
     * @return
     */
    public BalancerConfigVO getById(String id);
    
    /**
     * 通过主机id查询负载均衡器配置
     * @param hostId
     * @return
     */
    public BalancerConfigVO getByHostId(String hostId);
}

