package com.zhicloud.op.service.impl;

import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.mybatis.mapper.EmailConfigMapper;
import com.zhicloud.op.mybatis.mapper.MarkMapper;
import com.zhicloud.op.mybatis.mapper.MessageRecordMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.EmailConfigService;
import com.zhicloud.op.vo.EmailConfigVO;
import com.zhicloud.op.vo.MarkVO;
import com.zhicloud.op.vo.MessageRecordVO;

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
public class EmailConfigServiceImpl extends BeanDirectCallableDefaultImpl implements
    EmailConfigService {

    public static final Logger logger = Logger.getLogger(EmailConfigServiceImpl.class);

    private SqlSession sqlSession;

    public void setSqlSession(SqlSession sqlSession)
    {
        this.sqlSession = sqlSession;
    }

    @Callable
    public String managePage(HttpServletRequest request, HttpServletResponse response) {

        return "/security/operator/message/email/email_config_manage.jsp";
    }

    @Callable
    @Transactional(readOnly = false)
    public String addPage(HttpServletRequest request, HttpServletResponse response) {

        return "/security/operator/message/email/email_config_add.jsp";
    }

    @Callable
    public String modPage(HttpServletRequest request, HttpServletResponse response) {

        // 参数处理
        String id = StringUtil.trim(request.getParameter("id"));
        if( StringUtil.isBlank(id) )
        {
            throw new AppException("id不能为空");
        }

        EmailConfigMapper emailConfigMapper = this.sqlSession.getMapper(EmailConfigMapper.class);

        EmailConfigVO mailConfigVO = emailConfigMapper.queryConfigById(id);
        request.setAttribute("mailConfigVO", mailConfigVO);

        return "/security/operator/message/email/email_config_mod.jsp";
    }

    @Callable
    public void getAllConfig(HttpServletRequest request, HttpServletResponse response) {

        logger.debug("EmailConfigServiceImpl.getAllConfig()");
        try
        {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");

            // 获取参数
            String name = StringUtil.trim(request.getParameter("name"));
            Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
            Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);

            // 查询数据库
            EmailConfigMapper emailConfigMapper = this.sqlSession.getMapper(EmailConfigMapper.class);
            Map<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("name", "%" + name + "%");
            condition.put("start_row", page * rows);
            condition.put("row_count", rows);
            int total = emailConfigMapper.queryAllPageCount(condition); // 总行数
            List<EmailConfigVO> configList = emailConfigMapper.queryAllPage(condition);

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
    public EmailConfigVO getConfigById(String id) {
        try {

            // 查询数据库
            EmailConfigMapper emailConfigMapper = this.sqlSession.getMapper(EmailConfigMapper.class);

            return emailConfigMapper.queryConfigById(id);
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
            EmailConfigMapper emailConfigMapper = this.sqlSession.getMapper(EmailConfigMapper.class);
            EmailConfigVO mailConfigVO = emailConfigMapper.queryConfigByName(name);

            if(mailConfigVO == null) {
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
            String sender = StringUtil.trim(parameter.get("sender"));
            String senderAddress = StringUtil.trim(parameter.get("address"));
            String password = StringUtil.trim(parameter.get("password"));

            if(name==null || name==""){
                return new MethodResult(MethodResult.FAIL,"配置名不能为空");
            }
            if(sender==null || sender==""){
                return new MethodResult(MethodResult.FAIL,"发件人不能为空");
            }
            if(senderAddress==null || senderAddress==""){
                return new MethodResult(MethodResult.FAIL,"邮箱不能为空");
            }
            if(password==null || password==""){
                return new MethodResult(MethodResult.FAIL,"密码不能为空");
            }

            EmailConfigMapper emailConfigMapper = this.sqlSession.getMapper(EmailConfigMapper.class);

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", StringUtil.generateUUID());
            data.put("name", name);
            data.put("sender", sender);
            data.put("sender_address", senderAddress);
            data.put("password", password);
            data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

            int result = emailConfigMapper.insertConfig(data);

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
            String sender = StringUtil.trim(parameter.get("sender"));
            String senderAddress = StringUtil.trim(parameter.get("address"));
            String password = StringUtil.trim(parameter.get("password"));

            if(name==null || name==""){
                return new MethodResult(MethodResult.FAIL,"配置名不能为空");
            }
            if(sender==null || sender==""){
                return new MethodResult(MethodResult.FAIL,"发件人不能为空");
            }
            if(senderAddress==null || senderAddress==""){
                return new MethodResult(MethodResult.FAIL,"邮箱不能为空");
            }
            if(password==null || password==""){
                return new MethodResult(MethodResult.FAIL,"密码不能为空");
            }

            EmailConfigMapper emailConfigMapper = this.sqlSession.getMapper(EmailConfigMapper.class);

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", id);
            data.put("name", name);
            data.put("sender", sender);
            data.put("sender_address", senderAddress);
            data.put("password", password);
            data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

            int result = emailConfigMapper.updateConfig(data);

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

            EmailConfigMapper emailConfigMapper = this.sqlSession.getMapper(EmailConfigMapper.class);
            int n = emailConfigMapper.deleteConfigByIds(ids.toArray(new String[0]));

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
    
    @Callable
    public String detailPage(HttpServletRequest request, HttpServletResponse response) {
        try{
            // 参数处理
            String id = StringUtil.trim(request.getParameter("id"));
            if( StringUtil.isBlank(id) )
            {
                throw new AppException("id不能为空");
            }
    
            MessageRecordMapper messageRecordMapper = this.sqlSession.getMapper(MessageRecordMapper.class);
    
            MessageRecordVO messageRecordVO = messageRecordMapper.queryRecordById(id);
            request.setAttribute("messageRecordVO", messageRecordVO);
    
            return "/security/operator/message/email/email_manage_detail.jsp";
        }catch(Exception e){
            logger.error(e);
            throw new AppException("显示详细信息失败！");
        }
    }
}
