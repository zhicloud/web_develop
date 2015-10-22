package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.mapper.SmsConfigMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ISmsConfigService;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.SmsConfigVO;
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
public class SmsConfigServiceImpl implements ISmsConfigService {

    public static final Logger logger = Logger.getLogger(SmsConfigServiceImpl.class);

    private SqlSession sqlSession;

    public void setSqlSession(SqlSession sqlSession)
    {
        this.sqlSession = sqlSession;
    }


    @Override
    public List<SmsConfigVO> getAllConfig() {

        logger.debug("SmsConfigServiceImpl.getAllConfig()");
        return this.sqlSession.getMapper(SmsConfigMapper.class).queryAllConfig();
    }

    @Override
    public SmsConfigVO getConfigById(String id) {
        return this.sqlSession.getMapper(SmsConfigMapper.class).queryConfigById(id);
    }

    @Override
    public MethodResult checkConfigName(String name) {

        // 查询数据库
        SmsConfigVO smsConfigVO = this.sqlSession.getMapper(SmsConfigMapper.class).queryConfigByName(
            name);

        if(smsConfigVO == null) {
            return new MethodResult(MethodResult.SUCCESS, "该配置名有效");
        }

        return new MethodResult(MethodResult.FAIL, "该配置名已存在");
    }

    @Override
    @Transactional(readOnly = false)
    public MethodResult addConfig(Map<String, Object> parameter) {

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

    @Override
    @Transactional(readOnly = false)
    public MethodResult modifyConfigById(Map<String, Object> parameter) {

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
            e.printStackTrace();
            logger.error(e);
            throw new AppException("修改失败");
        }

    }

    @Override
    @Transactional(readOnly = false)
    public MethodResult removeConfigByIds(List<?> ids) {
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
