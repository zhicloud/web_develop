package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.mapper.SmsTemplateMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ISmsTemplateService;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.SmsTemplateVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */

@Transactional(readOnly = true)
public class SmsTemplateServiceImpl implements ISmsTemplateService {

    public static final Logger logger = Logger.getLogger(SmsTemplateServiceImpl.class);

    private SqlSession sqlSession;

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    @Override
    public List<SmsTemplateVO> getAllTemplate() {
        return this.sqlSession.getMapper(SmsTemplateMapper.class).queryAllTemplate();
    }

    @Override
    public SmsTemplateVO getTemplateById(String id) {
        return this.sqlSession.getMapper(SmsTemplateMapper.class).queryTemplateById(id);
    }

    @Override
    public SmsTemplateVO getTemplateByCode(String code) {
        return this.sqlSession.getMapper(SmsTemplateMapper.class).queryTemplateByCode(code);
    }

    @Override
    public MethodResult checkTemplateName(String name) {
        // 查询数据库
        SmsTemplateVO smsTemplateVO = this.sqlSession.getMapper(SmsTemplateMapper.class).queryTemplateByName(
            name);

        if (smsTemplateVO == null) {
            return new MethodResult(MethodResult.SUCCESS, "该配置名有效");
        }

        return new MethodResult(MethodResult.FAIL, "该配置名已存在");

    }

    @Override
    public MethodResult checkTemplateCode(String code) {

        // 查询数据库
        SmsTemplateVO smsTemplateVO = this.sqlSession.getMapper(SmsTemplateMapper.class).queryTemplateByCode(
            code);

        if (smsTemplateVO == null) {
            return new MethodResult(MethodResult.SUCCESS, "该标识码有效");
        }

        return new MethodResult(MethodResult.FAIL, "该标识码已存在");
    }

    @Override
    @Transactional(readOnly = false)
    public MethodResult addTemplate(Map<String, Object> parameter) {
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

    @Override
    @Transactional(readOnly = false)
    public MethodResult modifyTemplateById(Map<String, Object> parameter) {
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

    @Override
    @Transactional(readOnly = false)
    public MethodResult removeTemplateByIds(List<?> ids) {
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
