package com.zhicloud.ms.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.WhitelistVO;


public interface IWhitelistService {
    
    /**
     * 查询所有黑名单
     * @return
     */
    public List<WhitelistVO> getAll();
    
    /**
     * 按ID查询黑名单
     * @param ruleId
     * @return
     */
    public WhitelistVO getByRuleId(String ruleId);
    
    /**
     * 按名称查询黑名单
     * @param ruleName
     * @return
     */
    public WhitelistVO getByRuleName(String ruleName);
    
    /**
     * 添加黑名单
     * @param Whitelist
     * @param request
     * @return
     */
    public MethodResult addWhitelist(WhitelistVO whitelist,HttpServletRequest request);
    
    /**
     * 更新黑名单
     * @param Whitelist
     * @param request
     * @return
     */
    public MethodResult updateWhitelist(WhitelistVO whitelist,HttpServletRequest request);
    
    /**
     * 删除单个黑名单
     * @param ruleId
     * @return
     */
    public MethodResult deleteWhitelist(String ruleId);
    
    /**
     * 批量删除黑名单
     * @param ruleIds
     * @return
     */
    public MethodResult deleteWhitelists(List<String> ruleIds);

}
