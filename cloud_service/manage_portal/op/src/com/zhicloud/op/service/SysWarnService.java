package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.SysWarnRuleVO;
import com.zhicloud.op.vo.SysWarnValueVO;

/**
 * @ClassName: SysWarnService
 * @Description: 预警接口
 * @author 张本缘 于 2015年7月13日 下午2:29:47
 */
public interface SysWarnService {

    /**
     * @Description:预警规则页面跳转
     * @param request
     * @param response
     * @return String
     */
    public String RuleManagePage(HttpServletRequest request, HttpServletResponse response);

    /**
     * @Description:预警值设置页面跳转
     * @param request
     * @param response
     * @return String
     */
    public String ValueManagePage(HttpServletRequest request, HttpServletResponse response);

    /**
     * @Description:预警规则新增页面跳转
     * @param request
     * @param response
     * @return String
     */
    public String AddRulePage(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * @Description:预警规则修改页面跳转
     * @param request
     * @param response
     * @return String
     */    
    public String ModRulePage(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * @Description:保存规则信息
     * @param parameter 参数
     * @return MethodResult
     */
    public MethodResult SaveRuleInfo(Map<String, String> parameter);
    
    /**
     * @Description:规则查询方法
     * @param request
     * @param response
     */
    public void RuleQuery(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * @Description:批量删除规则
     * @param ruleids
     * @return MethodResult
     */
    public MethodResult deleteRuleByIDS(List<String> ruleids);
    
    /**
     * @Description:保存规则内容
     * @param array
     * @return MethodResult
     */
    public MethodResult SaveValueInfo(JSONArray array,String ruleid);
    
    /**
     * @Description:根据规则编码解析当前传入的json对象是否符合该规则
     * @param code 规则编码
     * @param json 对象
     * @return -1、该规则不存在 0、不符合 1、符合
     */
    public Integer AnalyzeRule(String code, JSONObject json);
    
    /**
     * @Description:根据编码取得规则对象，主要是为了方便定时器调用，因为定时器没有注入sqlsession
     * @param code
     * @return SysWarnRuleVO
     */
    public SysWarnRuleVO getRuleByCode(String code);
    
    /**
     * @Description:根据规则ID取得规则内容，主要是为了方便定时器调用，因为定时器没有注入sqlsession
     * @param ruleid
     * @return List<SysWarnValueVO>
     */
    public List<SysWarnValueVO> getValueByRule(String ruleid);
}
