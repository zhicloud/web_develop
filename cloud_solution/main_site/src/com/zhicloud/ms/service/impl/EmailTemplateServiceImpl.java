package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.mapper.EmailTemplateMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IEmailTemplateService;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.EmailTemplateVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张翔
 * @function 邮件模板 Service
 * @date
 */

@Transactional(readOnly = true)
public class EmailTemplateServiceImpl implements IEmailTemplateService {

    public static final Logger logger = Logger.getLogger(EmailTemplateServiceImpl.class);

    private SqlSession sqlSession;

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    /**
     * @function 获取所有模板
     * @return
     */
    @Override
    public List<EmailTemplateVO> getAllTemplate() {
       return this.sqlSession.getMapper(EmailTemplateMapper.class).queryAllTemplate();
    }

    /**
     * @function 按照 id 获取模板
     * @param id
     * @return
     */
    @Override
    public EmailTemplateVO getTemplateById(String id) {
       return this.sqlSession.getMapper(EmailTemplateMapper.class).queryTemplateById(id);
    }

    /**
     * @function 按照 code 获取模板
     * @param code
     * @return
     */
    @Override
    public EmailTemplateVO getTemplateByCode(String code) {
        try {
            EmailTemplateVO emailTemplateVO = this.sqlSession.getMapper(EmailTemplateMapper.class).queryTemplateByCode(code);
            return emailTemplateVO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @function 检测模板名是否重名
     * @param name
     * @return
     */
    @Override
    public MethodResult checkTemplateName(String name) {

        // 查询数据库
        EmailTemplateVO emailTemplateVO = this.sqlSession.getMapper(EmailTemplateMapper.class).queryTemplateByName(name);

        if (emailTemplateVO == null) {
            return new MethodResult(MethodResult.SUCCESS, "该配置名有效");
        }

        return new MethodResult(MethodResult.FAIL, "该配置名已存在");
    }

    /**
     * @function 检测 code 是否重名
     * @param code
     * @return
     */
    @Override
    public MethodResult checkTemplateCode(String code) {

        EmailTemplateVO emailTemplateVO = this.sqlSession.getMapper(EmailTemplateMapper.class).queryTemplateByCode(
            code);

        if (emailTemplateVO == null) {
            return new MethodResult(MethodResult.SUCCESS, "该标识码有效");
        }

        return new MethodResult(MethodResult.FAIL, "该标识码已存在");

    }

    /**
     * @function 添加模板
     * @param parameter
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public MethodResult addTemplate(Map<String, Object> parameter) {
        try {
            // 参数处理
            String name = StringUtil.trim(parameter.get("name"));
            String configId = StringUtil.trim(parameter.get("config_id"));
            String code = StringUtil.trim(parameter.get("code"));
            String recipient = StringUtil.trim(parameter.get("recipient"));
            String subject = StringUtil.trim(parameter.get("subject"));
            String content = StringUtil.trim(parameter.get("content"));

            if(configId==null || configId==""){
                return new MethodResult(MethodResult.FAIL,"邮件配置不能为空");
            }
            if(name==null || name==""){
                return new MethodResult(MethodResult.FAIL,"模版名不能为空");
            }
            if(subject==null || subject==""){
                return new MethodResult(MethodResult.FAIL,"邮件主题不能为空");
            }
            if(code==null || code==""){
                return new MethodResult(MethodResult.FAIL,"标识码不能为空");
            }
            if(content==null || content==""){
                return new MethodResult(MethodResult.FAIL,"邮件内容不能为空");
            }

            String id = StringUtil.generateUUID();

            EmailTemplateMapper emailTemplateMapper = this.sqlSession.getMapper(EmailTemplateMapper.class);

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", id);
            data.put("config_id", configId);
            data.put("name", name);
            data.put("code", code);
            data.put("recipient", recipient);
            data.put("subject", subject);
            data.put("content", content);
            data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

            int result = emailTemplateMapper.insertTemplate(data);

            if (result > 0) {
                return new MethodResult(MethodResult.SUCCESS, "添加成功");
            }

            return new MethodResult(MethodResult.FAIL, "添加失败");
        } catch( Exception e ) {
            logger.error(e);
            throw new AppException("添加失败");
        }

    }

    /**
     * 修改模板
     * @param parameter
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public MethodResult modifyTemplateById(Map<String, Object> parameter) {
        try {
            // 参数处理
            String id = StringUtil.trim(parameter.get("id"));
            String name = StringUtil.trim(parameter.get("name"));
            String configId = StringUtil.trim(parameter.get("config_id"));
            String subject = StringUtil.trim(parameter.get("subject"));
            String content = StringUtil.trim(parameter.get("content"));
            String code = StringUtil.trim(parameter.get("code"));
            String recipient = StringUtil.trim(parameter.get("recipient"));

            if(configId==null || configId==""){
                return new MethodResult(MethodResult.FAIL,"邮件配置不能为空");
            }
            if(name==null || name==""){
                return new MethodResult(MethodResult.FAIL,"模版名不能为空");
            }
            if(subject==null || subject==""){
                return new MethodResult(MethodResult.FAIL,"邮件主题不能为空");
            }
            if(content==null || content==""){
                return new MethodResult(MethodResult.FAIL,"邮件内容不能为空");
            }
            if(code==null || code==""){
                return new MethodResult(MethodResult.FAIL,"标识码不能为空");
            }

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", id);
            data.put("config_id", configId);
            data.put("name", name);
            data.put("code", code);
            data.put("recipient", recipient);
            data.put("subject", subject);
            data.put("content", content);
            data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

            int result = this.sqlSession.getMapper(EmailTemplateMapper.class).updateTemplate(data);

            if (result > 0) {
                return new MethodResult(MethodResult.SUCCESS, "修改成功");
            }


            return new MethodResult(MethodResult.FAIL, "修改失败");
        } catch( Exception e ) {
            e.printStackTrace();
            logger.error(e);
            throw new AppException("修改失败");
        }
    }

    /**
     * 删除模板
     * @param ids
     * @return
     */
    @Override
    @Transactional(readOnly = false)
    public MethodResult removeTemplateByIds(List<?> ids) {
        try {
            if( ids == null || ids.size() == 0 )
            {
                return new MethodResult(MethodResult.FAIL, "ids不能为空");
            }

            EmailTemplateMapper emailTemplateMapper = this.sqlSession.getMapper(EmailTemplateMapper.class);
            int n = emailTemplateMapper.deleteTemplateByIds(ids.toArray(new String[0]));

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
