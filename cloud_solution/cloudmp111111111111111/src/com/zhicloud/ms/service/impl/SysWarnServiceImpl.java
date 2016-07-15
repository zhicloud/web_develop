
package com.zhicloud.ms.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger; 
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.app.listener.CheckServerRoomsListener;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.constant.MonitorConstant;
import com.zhicloud.ms.mapper.SysWarnRuleMapper;
import com.zhicloud.ms.mapper.SysWarnValueMapper;
import com.zhicloud.ms.service.SysWarnService;
import com.zhicloud.ms.vo.SysWarnRuleVO;
import com.zhicloud.ms.vo.SysWarnValueVO;

/**
 * @ClassName: SysWarnServiceImpl
 * @Description: 预警规则接口实现
 * @author 张本缘 于 2015年7月14日 上午9:31:34
 */
//@Service("sysWarnService")
@Transactional(readOnly=false)
public class SysWarnServiceImpl implements SysWarnService {

    public static final Logger logger = Logger.getLogger(SysWarnServiceImpl.class);

    @Resource
    private SqlSession sqlSession;

    /**
     * @Description:保存规则信息
     * @param parameter
     * @return
     */
    @Transactional(readOnly = false)
    public int SaveRuleInfo(SysWarnRuleVO rule, String userid) {
        logger.debug("SysWarnServiceImpl.SaveRuleInfo()");
        SysWarnRuleMapper ruleMapper = this.sqlSession.getMapper(SysWarnRuleMapper.class);
        Map<String, Object> ruleData = new LinkedHashMap<String, Object>();
        String type = rule.getEdittype();
        // 参数处理
        if ("modify".equals(type)) {
            ruleData.put("id", rule.getId());
        } else {
            ruleData.put("id", StringUtil.generateUUID());
        }
        // 参数处理
        ruleData.put("name", rule.getName());
        ruleData.put("code", rule.getCode());
        ruleData.put("ruletype", rule.getRuletype());
        ruleData.put("isnotify", rule.getIsnotify());
        ruleData.put("realtime", rule.getRealtime());
        ruleData.put("frequency", rule.getFrequency());
        ruleData.put("sampletime", rule.getSampletime());
        // 如果是及时通知则将定时时间置为null
        if (rule.getRealtime() == 1) {
            ruleData.put("sendtime", rule.getSendtime());
        } else {
            ruleData.put("sendtime", null);
        }
/*        ruleData.put("notify_phone", rule.getNotify_phone());
        ruleData.put("notify_email", rule.getNotify_email());*/
        int n = 0;
        if ("add".equals(type)) {
            ruleData.put("insert_user", userid);
            ruleData.put("insert_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            n = ruleMapper.addSysWarnRule(ruleData);
        }
        if ("modify".equals(type)) {
            n = ruleMapper.updateSysWarnRule(ruleData);
        }
        // 更新内存数据
        if (rule.getRuletype() == 0) {// 告警
            CheckServerRoomsListener.warn_on_off = rule.getIsnotify();
        } else {// 故障
            CheckServerRoomsListener.error_on_off = rule.getIsnotify();
        }
        return n;
    }

    /**
     * @Description:规则查询方法
     * @param request
     * @param response
     * @throws
     */
    public List<SysWarnRuleVO> RuleQuery() {
        // 查询数据库
        SysWarnRuleMapper ruleMapper = this.sqlSession.getMapper(SysWarnRuleMapper.class);
        SysWarnValueMapper valueMapper = this.sqlSession.getMapper(SysWarnValueMapper.class);
        List<SysWarnRuleVO> ruleList = ruleMapper.getAll();
        // 从子表拼接规则内容
        for (SysWarnRuleVO rule : ruleList) {
            List<SysWarnValueVO> values = valueMapper.getByRuleID(rule.getId());
            if (values != null && values.size() > 0) {
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < values.size(); i++) {
                    SysWarnValueVO value = values.get(i);
                    sb.append("【");
                    sb.append(value.getName());
                    if ("between".equals(value.getOperator())) {
                        sb.append(" 在 ");
                        sb.append(value.getValue());
                        sb.append(" 之间");
                    } else {
                        if ("equal".equals(value.getOperator())) {
                            sb.append(" 等于 ");
                        }
                        if ("high".equals(value.getOperator())) {
                            sb.append(" 大于 ");
                        }
                        if ("low".equals(value.getOperator())) {
                            sb.append(" 小于 ");
                        }
                        sb.append(value.getValue());
                    }
                    sb.append("】");
                    if (sb != null && !"".equals(sb) && i != values.size() - 1) {
                        if ("and".equals(value.getContact())) {
                            sb.append("并且");
                        }
                        if ("or".equals(value.getContact())) {
                            sb.append("或者");
                        }
                        if ("".equals(value.getContact())) {
                            sb.append("");
                        }
                    }
                }
                rule.setContent(sb.toString());
            }
        }
        return ruleList;
    }

    /**
     * @Description:根据主键id取得规则实体对象VO
     * @param id
     * @return SysWarnRuleVO
     */
    public SysWarnRuleVO getWarnRuleById(String id) {
        SysWarnRuleMapper ruleMapper = this.sqlSession.getMapper(SysWarnRuleMapper.class);
        return ruleMapper.getById(id);
    }

    /**
     * @Description:根据code取得规则实体对象VO
     * @param code
     * @return SysWarnRuleVO
     */
    public SysWarnRuleVO getWarnRuleByCode(String code) {
        SysWarnRuleMapper ruleMapper = this.sqlSession.getMapper(SysWarnRuleMapper.class);
        return ruleMapper.getByCode(code);
    }
    
    /**
     * @Description:根据name取得规则实体对象VO
     * @param code
     * @return SysWarnRuleVO
     */
    public SysWarnRuleVO getWarnRuleByName(String name) {
        SysWarnRuleMapper ruleMapper = this.sqlSession.getMapper(SysWarnRuleMapper.class);
        return ruleMapper.getByName(name);
    }
    
    /**
     * @Description:根据规则编码解析当前传入的json对象是否符合该规则
     * @param code 规则编码
     * @param json 对象
     * @return -1、该规则不存在 0、不符合 1、符合
     * @throws
     */
    public Integer AnalyzeRule(String code, JSONObject json) {
        Integer returnnum = 0;
        SysWarnRuleMapper ruleMapper = this.sqlSession.getMapper(SysWarnRuleMapper.class);
        SysWarnValueMapper valueMapper = this.sqlSession.getMapper(SysWarnValueMapper.class);
        SysWarnRuleVO rule = ruleMapper.getByCode(code);
        if (rule == null) {
            // 没有定义规则
            return -1;
        }
        List<SysWarnValueVO> values = valueMapper.getByRuleID(rule.getId());
        if (values == null || values.size() == 0) {
            // 规则没有设置内容
            return -1;
        }
        boolean[] judge = new boolean[values.size()];
        boolean temp = true;
        for (int i = 0; i < values.size(); i++) {
            SysWarnValueVO value = values.get(i);
            String json_value = json.getString(value.getCode());
            // 没有取到值
            if (json_value == null || "".equals(json_value)) {
                continue;
            }
            // 解析操作符operator
            if ("equal".equals(value.getOperator())) {
                judge[i] = json_value.equals(value.getOperator());
            } else if ("high".equals(value.getOperator())) {
                judge[i] = Float.parseFloat(json_value) > Float.parseFloat(value.getValue());
            } else if ("low".equals(value.getOperator())) {
                judge[i] = Float.parseFloat(json_value) < Float.parseFloat(value.getValue());
            } else if ("between".equals(value.getOperator())) {
                String lower = value.getValue().split("-")[0];
                String higher = value.getValue().split("-")[1];
                if (Float.parseFloat(json_value) >= Float.parseFloat(lower)
                        && Float.parseFloat(json_value) <= Float.parseFloat(higher)) {
                    judge[i] = true;
                } else {
                    judge[i] = false;
                }
            }
            // 解析连接符
            if (i == 0 && i == values.size() - 1) {// 只有一条规则内容
                if (judge[i]) {
                    temp = true;
                    returnnum = 1;
                } else {
                    returnnum = 0;
                    temp = false;
                }
            }
            if (i > 0 && i < values.size()) {// 多条规则内容
                if ("and".equals(values.get(i - 1).getContact())) {
                    temp = temp && judge[i - 1] && judge[i];
                }
                if ("or".equals(values.get(i - 1).getContact())) {
                    if (temp) {
                        temp = temp && judge[i - 1] || judge[i];
                    } else {
                        temp = temp || judge[i - 1] || judge[i];
                    }

                }
            }
        }
        if (temp) {
            returnnum = 1;
        } else {
            returnnum = 0;
        }
        return returnnum;
    }
    
    /**
     * @Description:根据编码取得规则对象，主要是为了方便定时器调用，因为定时器没有注入sqlsession
     * @param code
     * @return SysWarnRuleVO
     */
    public SysWarnRuleVO getRuleByCode(String code) {
        SysWarnRuleMapper ruleMapper = this.sqlSession.getMapper(SysWarnRuleMapper.class);
        return ruleMapper.getByCode(code);
    }
    
    /**
     * @Description:根据规则ID取得规则内容，主要是为了方便定时器调用，因为定时器没有注入sqlsession
     * @param ruleid
     * @return List<SysWarnValueVO>
     */
    public List<SysWarnValueVO> getValueByRule(String ruleid) {
        SysWarnValueMapper valueMapper = this.sqlSession.getMapper(SysWarnValueMapper.class);
        return valueMapper.getByRuleID(ruleid);
    }

    /**
     * Description: 删除规则
     * @param ids
     * @return int
     */
    public int deleterule(String[] ids) {
        SysWarnRuleMapper ruleMapper = this.sqlSession.getMapper(SysWarnRuleMapper.class);
        SysWarnValueMapper valueMapper = this.sqlSession.getMapper(SysWarnValueMapper.class);
        int n = 0;
        if (ids.length > 0) {
            for (String id : ids) {
                valueMapper.deleteByRule(id);
            }
            n = ruleMapper.deleteSysWarnRule(ids);
        }
        return n;
    }
    
    /**
     * @Description:根据规则ID查询规则对应类容
     * @param ruleid
     * @return List<SysWarnValueVO>
     */
    public List<SysWarnValueVO> getValueByRuleID(String ruleid) {
        SysWarnValueMapper valueMapper = this.sqlSession.getMapper(SysWarnValueMapper.class);
        return valueMapper.getByRuleID(ruleid);
    }
    
    /**
     * @Description:保存规则内容信息
     * @param array JSONArray
     * @param ruleid String
     * @return int
     */
    public int saveValueInfo(JSONArray array, String ruleid) {
        SysWarnValueMapper valueMapper = this.sqlSession.getMapper(SysWarnValueMapper.class);
        // 保存前，先删除原来的数据
        valueMapper.deleteByRule(ruleid);
        int n = 0;
        // 插入新数据
        if (array != null && array.size() > 0) {
            for (Object value : array) {
                Map<String, Object> data = new LinkedHashMap<String, Object>();
                data = (Map<String, Object>) value;
                data.put("id", StringUtil.generateUUID());
                data.put("ruleid", ruleid);
                n = valueMapper.addSysWarnValue(data);
            }
            
            // 更新规则内容到内存
            SysWarnRuleVO warnrule = this.getRuleByCode(MonitorConstant.monitor_rule_warn);
            SysWarnRuleVO errorrule = this.getRuleByCode(MonitorConstant.monitor_rule_error);
            MonitorConstant.rulevo.put(MonitorConstant.monitor_rule_warn, warnrule);
            MonitorConstant.rulevo.put(MonitorConstant.monitor_rule_error, errorrule);
            MonitorConstant.rule_values.put(MonitorConstant.monitor_rule_warn, this.getValueByRule(warnrule.getId()));
            MonitorConstant.rule_values.put(MonitorConstant.monitor_rule_error, this.getValueByRule(errorrule.getId()));
        }
        return n;
    }
}
