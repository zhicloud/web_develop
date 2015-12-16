package com.zhicloud.ms.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.BlacklistVO;


public interface IBlacklistService {
    
    /**
     * 查询所有黑名单
     * @return
     */
    public List<BlacklistVO> getAll();
    
    /**
     * 按ID查询黑名单
     * @param ruleId
     * @return
     */
    public BlacklistVO getByRuleId(String ruleId);
    
    /**
     * 按名称查询黑名单
     * @param ruleName
     * @return
     */
    public BlacklistVO getByRuleName(String ruleName);
    
    /**
     * 添加黑名单
     * @param blacklist
     * @param request
     * @return
     */
    public MethodResult addBlacklist(BlacklistVO blacklist,HttpServletRequest request);
    
    /**
     * 更新黑名单
     * @param blacklist
     * @param request
     * @return
     */
    public MethodResult updateBlacklist(BlacklistVO blacklist,HttpServletRequest request);
    
    /**
     * 删除单个黑名单
     * @param ruleId
     * @return
     */
    public MethodResult deleteBlacklist(String ruleId);
    
    /**
     * 批量删除黑名单
     * @param ruleIds
     * @return
     */
    public MethodResult deleteBlacklists(List<String> ruleIds);

}
