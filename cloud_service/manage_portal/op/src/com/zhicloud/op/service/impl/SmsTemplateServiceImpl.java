package com.zhicloud.op.service.impl;

import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.mybatis.mapper.SmsConfigMapper;
import com.zhicloud.op.mybatis.mapper.SmsTemplateMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.SmsTemplateService;
import com.zhicloud.op.vo.SmsConfigVO;
import com.zhicloud.op.vo.SmsTemplateVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

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
public class SmsTemplateServiceImpl extends BeanDirectCallableDefaultImpl implements
    SmsTemplateService {

    public static final Logger logger = Logger.getLogger(SmsTemplateServiceImpl.class);

    private SqlSession sqlSession;

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Callable
    public String managePage(HttpServletRequest request, HttpServletResponse response) {

        return "/security/operator/message/sms/sms_template_manage.jsp";
    }

    @Callable
    public String addPage(HttpServletRequest request, HttpServletResponse response) {

        SmsConfigMapper smsConfigMapper = this.sqlSession.getMapper(SmsConfigMapper.class);
        List<SmsConfigVO> smsConfigList = smsConfigMapper.queryAllConfig();
        request.setAttribute("smsConfigList", smsConfigList);
        return "/security/operator/message/sms/sms_template_add.jsp";
    }

    @Callable
    public String modPage(HttpServletRequest request, HttpServletResponse response) {

        // 参数处理
        String id = StringUtil.trim(request.getParameter("id"));
        if (StringUtil.isBlank(id)) {
            throw new AppException("id不能为空");
        }

        SmsTemplateMapper smsTemplateMapper = this.sqlSession.getMapper(SmsTemplateMapper.class);

        SmsTemplateVO smsTemplateVO = smsTemplateMapper.queryTemplateById(id);

        SmsConfigMapper smsConfigMapper = this.sqlSession.getMapper(SmsConfigMapper.class);
        List<SmsConfigVO> smsConfigList = smsConfigMapper.queryAllConfig();

        request.setAttribute("smsTemplateVO", smsTemplateVO);
        request.setAttribute("smsConfigList", smsConfigList);


        return "/security/operator/message/sms/sms_template_mod.jsp";
    }

    @Callable
    public void getAllTemplate(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");

            // 获取参数
            String name = StringUtil.trim(request.getParameter("name"));
            Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
            Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);

            // 查询数据库
            SmsTemplateMapper smsTemplateMapper = this.sqlSession.getMapper(SmsTemplateMapper.class);
            Map<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("name", "%" + name + "%");
            condition.put("start_row", page * rows);
            condition.put("row_count", rows);
            int total = smsTemplateMapper.queryAllPageCount(condition); // 总行数
            List<SmsTemplateVO> configList = smsTemplateMapper.queryAllPage(condition);

            // 输出json数据
            ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, configList);
        } catch (Exception e) {
            e.printStackTrace();
//            logger.error(e);
//            throw new AppException("查询失败");
        }
    }

    @Callable
    public SmsTemplateVO getTemplateById(String id) {
        try {

            // 查询数据库
            SmsTemplateMapper smsTemplateMapper = this.sqlSession.getMapper(SmsTemplateMapper.class);

            return smsTemplateMapper.queryTemplateById(id);
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("查询失败");
        }
    }

    @Callable
    public SmsTemplateVO getTemplateByCode(String code) {
        try {

            // 查询数据库
            SmsTemplateMapper smsTemplateMapper = this.sqlSession.getMapper(SmsTemplateMapper.class);

            return smsTemplateMapper.queryTemplateByCode(code);
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("查询失败");
        }
    }

    @Callable
    public MethodResult checkTemplateName(String name) {
        try {

            // 查询数据库
            SmsTemplateMapper smsTemplateMapper = this.sqlSession.getMapper(SmsTemplateMapper.class);
            SmsTemplateVO smsTemplateVO = smsTemplateMapper.queryTemplateByName(name);

            if (smsTemplateVO == null) {
                return new MethodResult(MethodResult.SUCCESS, "该配置名有效");
            }

            return new MethodResult(MethodResult.FAIL, "该配置名已存在");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("查询失败");
        }
    }

    @Callable
    public MethodResult checkTemplateCode(String code) {
        try {

            // 查询数据库
            SmsTemplateMapper smsTemplateMapper = this.sqlSession.getMapper(SmsTemplateMapper.class);
            SmsTemplateVO smsTemplateVO = smsTemplateMapper.queryTemplateByCode(code);

            if (smsTemplateVO == null) {
                return new MethodResult(MethodResult.SUCCESS, "该标识码有效");
            }

            return new MethodResult(MethodResult.FAIL, "该标识码已存在");
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("查询失败");
        }
    }

    @Callable
    @Transactional(readOnly = false)
    public MethodResult addTemplate(Map<String, String> parameter) {
        try {
            // 参数处理
            String name = StringUtil.trim(parameter.get("name"));
            String configId = StringUtil.trim(parameter.get("config_id"));
            String code = StringUtil.trim(parameter.get("code"));
            String recipient = StringUtil.trim(parameter.get("recipient"));
            String content = StringUtil.trim(parameter.get("content"));

            if(configId==null || configId==""){
                return new MethodResult(MethodResult.FAIL,"短信配置不能为空");
            }
            if(name==null || name==""){
                return new MethodResult(MethodResult.FAIL,"模版名不能为空");
            }
            if(code==null || code==""){
                return new MethodResult(MethodResult.FAIL,"标识码不能为空");
            }
            if(content==null || content==""){
                return new MethodResult(MethodResult.FAIL,"短信内容不能为空");
            }

            String id = StringUtil.generateUUID();

            SmsTemplateMapper smsTemplateMapper = this.sqlSession.getMapper(SmsTemplateMapper.class);

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", id);
            data.put("config_id", configId);
            data.put("name", name);
            data.put("code", code);
            data.put("recipient", recipient);
            data.put("content", content);
            data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

            int result = smsTemplateMapper.insertTemplate(data);

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
    public MethodResult updateTemplateById(Map<String, Object> parameter) {
        try {
            // 参数处理
            String id = StringUtil.trim(parameter.get("id"));
            String name = StringUtil.trim(parameter.get("name"));
            String configId = StringUtil.trim(parameter.get("config_id"));
            String code = StringUtil.trim(parameter.get("code"));
            String recipient = StringUtil.trim(parameter.get("recipient"));
            String content = StringUtil.trim(parameter.get("content"));

            if(configId==null || configId==""){
                return new MethodResult(MethodResult.FAIL,"短信配置不能为空");
            }
            if(name==null || name==""){
                return new MethodResult(MethodResult.FAIL,"模版名不能为空");
            }
            if(content==null || content==""){
                return new MethodResult(MethodResult.FAIL,"短信内容不能为空");
            }
            if(code==null || code==""){
                return new MethodResult(MethodResult.FAIL,"标识码不能为空");
            }

            SmsTemplateMapper smsTemplateMapper = this.sqlSession.getMapper(SmsTemplateMapper.class);

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", id);
            data.put("config_id", configId);
            data.put("name", name);
            data.put("code", code);
            data.put("recipient", recipient);
            data.put("content", content);
            data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

            int result = smsTemplateMapper.updateTemplate(data);

            if (result > 0) {
                return new MethodResult(MethodResult.SUCCESS, "修改成功");
            }

            return new MethodResult(MethodResult.FAIL, "修改失败");
        } catch( Exception e ) {
            logger.error(e);
            throw new AppException("修改失败");
        }
    }

    @Callable
    @Transactional(readOnly = false)
    public MethodResult deleteTemplateByIds(List<?> ids) {
        try {
            if( ids == null || ids.size() == 0 )
            {
                return new MethodResult(MethodResult.FAIL, "ids不能为空");
            }

            SmsTemplateMapper smsTemplateMapper = this.sqlSession.getMapper(SmsTemplateMapper.class);
            int n = smsTemplateMapper.deleteTemplateByIds(ids.toArray(new String[0]));

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
