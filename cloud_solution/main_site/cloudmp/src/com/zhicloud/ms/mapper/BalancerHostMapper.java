package com.zhicloud.ms.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.BalancerHostVO;


public interface BalancerHostMapper {
	
    /**
     * 添加一条关联信息
     * @param condition
     * @return
     */
    public int add(Map<String, Object> condition);
    
    /**
     * 删除关联信息
     * @param id
     * @return
     */
    public int deleteById(String id);
    
    /**
     * 查询所有关联信息
     * @return
     */
    public List<BalancerHostVO> getAll();
    
    
    /**
     * 通过id查询关联信息
     * @param id
     * @return
     */
    public BalancerHostVO getById(String id);
    
    /**
     * 通过主机id查询关联信息
     * @param hostId
     * @return
     */
    public List<BalancerHostVO> getByHostId(String hostId);
}

