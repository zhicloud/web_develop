
package com.zhicloud.op.service.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.SysWarnRuleMapper;
import com.zhicloud.op.mybatis.mapper.SysWarnValueMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.SysWarnService;
import com.zhicloud.op.vo.SysWarnRuleVO;
import com.zhicloud.op.vo.SysWarnValueVO;

/**
 * @ClassName: SysWarnServiceImpl
 * @Description: 预警规则接口实现
 * @author 张本缘 于 2015年7月14日 上午9:31:34
 */
@Transactional(readOnly = false)
public class SysWarnServiceImpl extends BeanDirectCallableDefaultImpl implements SysWarnService {

    public static final Logger logger = Logger.getLogger(SysWarnServiceImpl.class);

    private SqlSession sqlSession;

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    /**
     * Description:预警规则页面跳转
     * 
     * @param request
     * @param response
     * @return String
     */
    @Callable
    public String RuleManagePage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("SysWarnServiceImpl.RuleManagePage");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.agent_manage_page) == false) {
            return "/public/have_not_access.jsp";
        }
        return "/security/operator/sys_warnrule_manage.jsp";
    }

    /**
     * Description:预警规则内容页面跳转
     * 
     * @param request
     * @param response
     * @return String
     */
    @Callable
    public String ValueManagePage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("SysWarnServiceImpl.ValueManagePage");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.agent_manage_page) == false) {
            return "/public/have_not_access_dialog.jsp";
        }
        String ruleid = request.getParameter("ruleid");
        SysWarnValueMapper valueMapper = this.sqlSession.getMapper(SysWarnValueMapper.class);
        List<SysWarnValueVO> values = valueMapper.getByRuleID(ruleid);
        request.setAttribute("warnvalues", values);
        request.setAttribute("ruleid", ruleid);
        return "/security/operator/sys_warnvalue_edit.jsp";
    }

    /**
     * @Description:新增规则页面跳转
     * @param request
     * @param response
     * @return String
     */
    @Callable
    public String AddRulePage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("SysWarnServiceImpl.addRulePage()");

        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.agent_manage_page) == false) {
            return "/public/have_not_access_dialog.jsp";
        }
        return "/security/operator/sys_warnrule_add.jsp";
    }

    /**
     * @Description:修改规则页面跳转
     * @param request
     * @param response
     * @return String
     */
    @Callable
    public String ModRulePage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("SysWarnServiceImpl.ModRulePage()");

        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.agent_manage_page) == false) {
            return "/public/have_not_access_dialog.jsp";
        }
        String ruleid = request.getParameter("ruleid");
        SysWarnRuleMapper ruleMapper = this.sqlSession.getMapper(SysWarnRuleMapper.class);
        SysWarnRuleVO rule = ruleMapper.getById(ruleid);
        request.setAttribute("rulevo", rule);
        return "/security/operator/sys_warnrule_edit.jsp";
    }

    /**
     * @Description:保存规则信息
     * @param parameter
     * @return
     */
    @Callable
    @Transactional(readOnly = false)
    public MethodResult SaveRuleInfo(Map<String, String> parameter) {
        logger.debug("SysWarnServiceImpl.SaveRuleInfo()");
        HttpServletRequest request = RequestContext.getHttpRequest();
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        // 新增还是修改
        String type = StringUtil.trim(parameter.get("type"));
        if ("add".equals(type) && loginInfo.hasPrivilege(PrivilegeConstant.agent_manage_add) == false) {
            return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
        }
        if ("mod".equals(type) && loginInfo.hasPrivilege(PrivilegeConstant.agent_manage_modify) == false) {
            return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
        }
        SysWarnRuleMapper ruleMapper = this.sqlSession.getMapper(SysWarnRuleMapper.class);
        Map<String, Object> ruleData = new LinkedHashMap<String, Object>();
        // 参数处理
        if ("mod".equals(type)) {
            ruleData.put("id", StringUtil.trim(parameter.get("ruleid")));
        } else {
            ruleData.put("id", StringUtil.generateUUID());
        }
        //判断名称是否重复
        SysWarnRuleVO rule = ruleMapper.getByName(StringUtil.trim(parameter.get("name")));
        if (rule != null && "add".equals(type)) {
            return new MethodResult(MethodResult.FAIL, "规则名称重复");
        }
        if (rule != null && "mod".equals(type) && !rule.getId().equals(StringUtil.trim(parameter.get("ruleid")))) {
            return new MethodResult(MethodResult.FAIL, "规则名称重复");
        }
        //判断编码是否重复
        rule = ruleMapper.getByCode(StringUtil.trim(parameter.get("code")));
        if (rule != null && "add".equals(type)) {
            return new MethodResult(MethodResult.FAIL, "规则编码重复");
        }
        if (rule != null && "mod".equals(type) && !rule.getId().equals(StringUtil.trim(parameter.get("ruleid")))) {
            return new MethodResult(MethodResult.FAIL, "规则编码重复");
        }
        // 参数处理
        ruleData.put("name", StringUtil.trim(parameter.get("name")));
        ruleData.put("code", StringUtil.trim(parameter.get("code")));
        ruleData.put("ruletype", StringUtil.trim(parameter.get("ruletype")));
        ruleData.put("isnotify", StringUtil.trim(parameter.get("isnotify")));
        ruleData.put("realtime", StringUtil.trim(parameter.get("realtime")));
        ruleData.put("frequency", StringUtil.trim(parameter.get("frequency")));
        ruleData.put("sampletime", StringUtil.trim(parameter.get("sampletime")));
        // 如果是及时通知则将定时时间置为null
        if ("1".equals(StringUtil.trim(parameter.get("realtime")))) {
            ruleData.put("sendtime", StringUtil.trim(parameter.get("sendtime")));
        } else {
            ruleData.put("sendtime", null);
        }
        ruleData.put("notify_phone", StringUtil.trim(parameter.get("notify_phone")));
        ruleData.put("notify_email", StringUtil.trim(parameter.get("notify_email")));
        ruleData.put("insert_user", loginInfo.getUserId());
        ruleData.put("insert_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
        int n = 0;
        if ("add".equals(type)) {
            n = ruleMapper.addSysWarnRule(ruleData);
        }
        if ("mod".equals(type)) {
            n = ruleMapper.updateSysWarnRule(ruleData);
        }
        if (n > 0) {
            return new MethodResult(MethodResult.SUCCESS, "保存成功");
        } else {
            return new MethodResult(MethodResult.FAIL, "保存失败");
        }
    }

    /**
     * @Description:规则查询方法
     * @param request
     * @param response
     * @throws
     */
    @Callable
    public void RuleQuery(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("SysWarnServiceImpl.RuleQuery()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");

            // 权限判断
            LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
            if (loginInfo.hasPrivilege(PrivilegeConstant.agent_manage_page) == false) {
                throw new AppException("您没有权限进行此操作");
            }
            // 参数
            String ruletype = StringUtil.trim(request.getParameter("ruletype"));
            String realtime = StringUtil.trim(request.getParameter("realtime"));
            String notify_phone = StringUtil.trim(request.getParameter("notify_phone"));
            String notify_email = StringUtil.trim(request.getParameter("notify_email"));
            Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
            Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);

            // 查询数据库
            SysWarnRuleMapper ruleMapper = this.sqlSession.getMapper(SysWarnRuleMapper.class);
            SysWarnValueMapper valueMapper = this.sqlSession.getMapper(SysWarnValueMapper.class);
            Map<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("ruletype", ruletype);
            condition.put("realtime", realtime);
            condition.put("notify_phone", "%" + notify_phone + "%");
            condition.put("notify_email", "%" + notify_email + "%");
            condition.put("start_row", page * rows);
            condition.put("row_count", rows);
            int total = ruleMapper.queryPageCount(condition); // 总行数
            List<SysWarnRuleVO> ruleList = ruleMapper.queryPage(condition);// 分页结果
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

            // 输出json数据
            ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, ruleList);
        } catch (Exception e) {
            logger.error("SysWarnServiceImpl.RuleQuery()", e);
            throw new AppException("查询失败");
        }
    }

    /**
     * @Description:批量删除规则
     * @param ruleids
     * @return MethodResult
     */
    @Callable
    @Transactional(readOnly = false)
    public MethodResult deleteRuleByIDS(List<String> ruleids) {
        logger.debug("SysWarnServiceImpl.deleteRuleByIDS()");
        try {
            if (ruleids == null || ruleids.size() == 0) {
                throw new AppException("规则ID不能为空");
            }

            SysWarnRuleMapper ruleMapper = this.sqlSession.getMapper(SysWarnRuleMapper.class);
            SysWarnValueMapper valueMapper = this.sqlSession.getMapper(SysWarnValueMapper.class);
            // 删除规则内容
            for (String ruleid : ruleids) {
                valueMapper.deleteByRule(ruleid);
            }
            int n = ruleMapper.deleteSysWarnRule(ruleids.toArray(new String[0]));

            if (n > 0) {
                return new MethodResult(MethodResult.SUCCESS, "删除成功");
            } else {
                return new MethodResult(MethodResult.FAIL, "删除失败");
            }
        } catch (Exception e) {
            logger.error("SysWarnServiceImpl.deleteRuleByIDS()", e);
            throw new AppException("删除失败");
        }
    }

    /**
     * @Description:保存规则内容
     * @param array 参数
     * @return MethodResult
     */
    @SuppressWarnings("unchecked")
    @Callable
    @Transactional(readOnly = false)
    public MethodResult SaveValueInfo(JSONArray array, String ruleid) {
        logger.debug("SysWarnServiceImpl.SaveRuleInfo()");
        SysWarnValueMapper valueMapper = this.sqlSession.getMapper(SysWarnValueMapper.class);
        try {
            // 保存前，先删除原来的数据
            valueMapper.deleteByRule(ruleid);
            // 插入新数据
            if (array != null && array.size() > 0) {
                for (Object value : array) {
                    Map<String, Object> data = new LinkedHashMap<String, Object>();
                    data = (Map<String, Object>) value;
                    data.put("id", StringUtil.generateUUID());
                    data.put("ruleid", ruleid);
                    valueMapper.addSysWarnValue(data);
                }
            }
            return new MethodResult(MethodResult.SUCCESS, "保存成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MethodResult(MethodResult.FAIL, "保存失败");
        }
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
}
