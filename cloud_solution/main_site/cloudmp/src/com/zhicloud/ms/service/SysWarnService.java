package com.zhicloud.ms.service;

import java.util.List;

import com.zhicloud.ms.vo.SysWarnRuleVO;
import com.zhicloud.ms.vo.SysWarnValueVO;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * @ClassName: SysWarnService
 * @Description: 预警接口
 * @author 张本缘 于 2015年7月13日 下午2:29:47
 */
public interface SysWarnService {

    /**
     * @Description:保存规则信息
     * @param parameter 参数
     * @return MethodResult
     */
    public int SaveRuleInfo(SysWarnRuleVO rule,String userid);
    
    /**
     * @Description:规则查询方法
     * @param request
     * @param response
     */
    public List<SysWarnRuleVO> RuleQuery();
    
    /**
     * @Description:根据主键id取得规则实体对象VO
     * @param id
     * @return SysWarnRuleVO
     */
    public SysWarnRuleVO getWarnRuleById(String id);

    /**
     * @Description:根据code取得规则实体对象VO
     * @param code
     * @return SysWarnRuleVO
     */
    public SysWarnRuleVO getWarnRuleByCode(String code);

    /**
     * @Description:根据name取得规则实体对象VO
     * @param code
     * @return SysWarnRuleVO
     */
    public SysWarnRuleVO getWarnRuleByName(String name);
    
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
    
    /**
     * @Description:根据id删除告警规则
     * @param ids
     * @return int
     */
    public int deleterule(String[] ids);
    
    /**
     * @Description:根据规则ID查询规则对应类容
     * @param ruleid
     * @return List<SysWarnValueVO>
     */
    public List<SysWarnValueVO> getValueByRuleID(String ruleid);
    
    /**
     * @Description:保存规则内容信息
     * @param array JSONArray
     * @param ruleid String
     * @return int
     */
    public int saveValueInfo(JSONArray array, String ruleid);
}
