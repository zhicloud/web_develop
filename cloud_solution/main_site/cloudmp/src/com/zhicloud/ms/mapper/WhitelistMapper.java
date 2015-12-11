package com.zhicloud.ms.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.WhitelistVO;


public interface WhitelistMapper {
	
    /**
     * 查询所以白名单
     * @return
     */
    public List<WhitelistVO> getAll();
    
    /**
     * 添加白名单
     * @param condition
     * @return
     */
    public int add(Map<String,Object> condition);
    
    /**
     * 修改白名单
     * @param condition
     * @return
     */
    public int update(Map<String,Object> condition);
    
    /**
     * 删除白名单
     * @param ruleId
     * @return
     */
    public int delete(String ruleId);
    
    /**
     * 按ID查询白名单
     * @param ruleId
     * @return
     */
    public WhitelistVO getByRuleId(String ruleId);
    
    /**
     * 按规则名查询白名单
     * @param name
     * @return
     */
    public WhitelistVO getByRuleName(String name);
    
    /**
     * 批量删除白名单
     * @param ruleIds
     * @return
     */
    public int deleteByRuleIds(List<String> ruleIds);
}

