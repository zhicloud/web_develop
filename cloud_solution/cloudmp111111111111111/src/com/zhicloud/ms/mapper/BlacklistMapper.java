package com.zhicloud.ms.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.BlacklistVO;


public interface BlacklistMapper {
	
    /**
     * 查询所以黑名单
     * @return
     */
    public List<BlacklistVO> getAll();
    
    /**
     * 添加黑名单
     * @param condition
     * @return
     */
    public int add(Map<String,Object> condition);
    
    /**
     * 修改黑名单
     * @param condition
     * @return
     */
    public int update(Map<String,Object> condition);
    
    /**
     * 删除黑名单
     * @param ruleId
     * @return
     */
    public int deleteById(String ruleId);
    
    /**
     * 按ID查询黑名单
     * @param ruleId
     * @return
     */
    public BlacklistVO getByRuleId(String ruleId);
    
    /**
     * 按规则名查询黑名单
     * @param name
     * @return
     */
    public BlacklistVO getByRuleName(String name);
    
    /**
     * 批量删除黑名单
     * @param ruleIds
     * @return
     */
    public int deleteByRuleIds(List<String> ruleIds);
}

