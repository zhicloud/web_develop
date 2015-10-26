package com.zhicloud.op.service.impl;

import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.mybatis.mapper.SmsConfigMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.SmsConfigService;
import com.zhicloud.op.vo.SmsConfigVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */

@Transactional(readOnly = true)
public class SmsConfigServiceImpl extends BeanDirectCallableDefaultImpl implements
    SmsConfigService {

    public static final Logger logger = Logger.getLogger(SmsConfigServiceImpl.class);

    private SqlSession sqlSession;

    public void setSqlSession(SqlSession sqlSession)
    {
        this.sqlSession = sqlSession;
    }

    @Callable
    public String managePage(HttpServletRequest request, HttpServletResponse response) {
        System.err.println("managePage is running");
        return "/security/operator/message/sms/sms_config_manage.jsp";
    }

    @Callable
    public String addPage(HttpServletRequest request, HttpServletResponse response) {

        return "/security/operator/message/sms/sms_config_add.jsp";
    }

    @Callable
    public String modPage(HttpServletRequest request, HttpServletResponse response) {

        // 参数处理
        String id = StringUtil.trim(request.getParameter("id"));
        if( StringUtil.isBlank(id) )
        {
            throw new AppException("id不能为空");
        }

        SmsConfigMapper smsConfigMapper = this.sqlSession.getMapper(SmsConfigMapper.class);

        SmsConfigVO smsConfigVO = smsConfigMapper.queryConfigById(id);
        request.setAttribute("smsConfigVO", smsConfigVO);

        return "/security/operator/message/sms/sms_config_mod.jsp";
    }

    @Callable
    public void getAllConfig(HttpServletRequest request, HttpServletResponse response) {

        logger.debug("SmsConfigServiceImpl.getAllConfig()");
        try
        {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");

            // 获取参数
            String config_name = StringUtil.trim(request.getParameter("config_name"));
            Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
            Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);

            // 查询数据库
            SmsConfigMapper smsConfigMapper = this.sqlSession.getMapper(SmsConfigMapper.class);
            Map<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("config_name", "%" + config_name + "%");
            condition.put("start_row", page * rows);
            condition.put("row_count", rows);
            int total = smsConfigMapper.queryAllPageCount(condition); // 总行数
            List<SmsConfigVO> configList = smsConfigMapper.queryAllPage(condition);

            // 输出json数据
            ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, configList);
        }
        catch( Exception e )
        {
            logger.error(e);
            throw new AppException("查询失败");
        }
    }

    @Callable
    public SmsConfigVO getConfigById(String id) {
        try {

            // 查询数据库
            SmsConfigMapper smsConfigMapper = this.sqlSession.getMapper(SmsConfigMapper.class);

            return smsConfigMapper.queryConfigById(id);
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("查询失败");
        }
    }

    @Callable
    @ResponseBody
    public MethodResult checkConfigName(String name) {

        try {

            // 查询数据库
            SmsConfigMapper smsConfigMapper = this.sqlSession.getMapper(SmsConfigMapper.class);
            SmsConfigVO smsConfigVO = smsConfigMapper.queryConfigByName(name);

            if(smsConfigVO == null) {
                return new MethodResult(MethodResult.SUCCESS, "该配置名有效");
            }

            return new MethodResult(MethodResult.FAIL, "该配置名已存在");
        } catch( Exception e ) {
            logger.error(e);
            throw new AppException("查询失败");
        }

    }

    @Callable
    @Transactional(readOnly = false)
    public MethodResult addConfig(Map<String, String> parameter) {

        try {
            // 参数处理
            String name = StringUtil.trim(parameter.get("name"));
            String smsId = StringUtil.trim(parameter.get("sms_id"));
            String serviceUrl = StringUtil.trim(parameter.get("service_url"));
            String configName = StringUtil.trim(parameter.get("config_name"));
            String password = StringUtil.trim(parameter.get("password"));

            if(name==null || name==""){
                return new MethodResult(MethodResult.FAIL,"账户名不能为空");
            }
            if(smsId==null || smsId==""){
                return new MethodResult(MethodResult.FAIL,"ID不能为空");
            }
            if(serviceUrl==null || serviceUrl==""){
                return new MethodResult(MethodResult.FAIL,"服务链接不能为空");
            }
            if(configName==null || configName==""){
                return new MethodResult(MethodResult.FAIL,"配置名不能为空");
            }
            if(password==null || password==""){
                return new MethodResult(MethodResult.FAIL,"密码不能为空");
            }

            SmsConfigMapper smsConfigMapper = this.sqlSession.getMapper(SmsConfigMapper.class);

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", StringUtil.generateUUID());
            data.put("sms_id", smsId);
            data.put("name", name);
            data.put("service_url", serviceUrl);
            data.put("config_name", configName);
            data.put("password", password);
            data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

            int result = smsConfigMapper.insertConfig(data);

            if (result > 0) {
                return new MethodResult(MethodResult.SUCCESS, "添加成功");
            }

            return new MethodResult(MethodResult.FAIL, "添加失败");
        } catch( Exception e ) {
            logger.error(e);
            throw new AppException("添加失败");
        }

    }

    @Callable
    @Transactional(readOnly = false)
    public MethodResult updateConfigById(Map<String, Object> parameter) {

        try {
            // 参数处理
            String id = StringUtil.trim(parameter.get("id"));
            String name = StringUtil.trim(parameter.get("name"));
            String smsId = StringUtil.trim(parameter.get("sms_id"));
            String serviceUrl = StringUtil.trim(parameter.get("service_url"));
            String configName = StringUtil.trim(parameter.get("config_name"));
            String password = StringUtil.trim(parameter.get("password"));

            if(name==null || name==""){
                return new MethodResult(MethodResult.FAIL,"账户名不能为空");
            }
            if(smsId==null || smsId==""){
                return new MethodResult(MethodResult.FAIL,"ID不能为空");
            }
            if(serviceUrl==null || serviceUrl==""){
                return new MethodResult(MethodResult.FAIL,"服务链接不能为空");
            }
            if(configName==null || configName==""){
                return new MethodResult(MethodResult.FAIL,"配置名不能为空");
            }
            if(password==null || password==""){
                return new MethodResult(MethodResult.FAIL,"密码不能为空");
            }

            SmsConfigMapper smsConfigMapper = this.sqlSession.getMapper(SmsConfigMapper.class);

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", id);
            data.put("sms_id", smsId);
            data.put("name", name);
            data.put("service_url", serviceUrl);
            data.put("config_name", configName);
            data.put("password", password);
            data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

            int result = smsConfigMapper.updateConfig(data);

            if (result > 0) {
                return new MethodResult(MethodResult.SUCCESS, "修改成功");
            }

            return new MethodResult(MethodResult.FAIL, "修改失败");
        } catch( Exception e ) {
//            e.printStackTrace();
            logger.error(e);
            throw new AppException("修改失败");
        }

    }

    @Callable
    @Transactional(readOnly = false)
    public MethodResult deleteConfigByIds(List<?> ids) {
        try {
            if( ids == null || ids.size() == 0 )
            {
                return new MethodResult(MethodResult.FAIL, "ids不能为空");
            }

            SmsConfigMapper smsConfigMapper = this.sqlSession.getMapper(SmsConfigMapper.class);
            int n = smsConfigMapper.deleteConfigByIds(ids.toArray(new String[0]));

            if( n > 0 )
            {
                return new MethodResult(MethodResult.SUCCESS, "删除成功");
            }
            else
            {
                return new MethodResult(MethodResult.FAIL, "删除失败");
            }
        } catch( Exception e ) {
            logger.error(e);
            throw new AppException("删除失败");
        }

    }
}
