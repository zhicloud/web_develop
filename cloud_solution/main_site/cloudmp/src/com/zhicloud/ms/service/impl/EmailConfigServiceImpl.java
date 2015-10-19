package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.mapper.EmailConfigMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IEmailConfigService;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.EmailConfigVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */

@Transactional(readOnly = true)
public class EmailConfigServiceImpl implements IEmailConfigService {

    public static final Logger logger = Logger.getLogger(EmailConfigServiceImpl.class);

    @Resource
    private SqlSession sqlSession;

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    /**
     * @function 获取全部配置
     * @return
     */
    @Override
    public List<EmailConfigVO> getAllConfig() {
        return this.sqlSession.getMapper(EmailConfigMapper.class).queryAllConfig();
    }

    /**
     * @function 获取制定id的配置
     * @param id 配置id
     * @return
     */
    @Override
    public EmailConfigVO getConfigById(String id) {
        return this.sqlSession.getMapper(EmailConfigMapper.class).queryConfigById(id);

    }

    /**
     * @function 检测配置是否重名
     * @param name 配置名
     * @return
     */
    @Override
    public MethodResult checkConfigName(String name) {
        // 查询数据库
        EmailConfigVO mailConfigVO = this.sqlSession.getMapper(EmailConfigMapper.class).queryConfigByName(
            name);
        if(mailConfigVO == null) {
            return new MethodResult(MethodResult.SUCCESS, "该配置名有效");
        }
        return new MethodResult(MethodResult.FAIL, "该配置名已存在");

    }

    /**
     * @function 新增邮件配置
     * @param parameter
     * @return
     */
    @Override
    @Transactional(readOnly=false)
    public MethodResult addConfig(Map<String, Object> parameter) {

        try {
            // 参数处理
            String name = StringUtil.trim(parameter.get("name"));
            String protocol = StringUtil.trim(parameter.get("protocol"));
            String host = StringUtil.trim(parameter.get("host"));
            Integer port = Integer.valueOf(StringUtil.trim(parameter.get("port")));
            Integer isAuth = Integer.valueOf(StringUtil.trim(parameter.get("is_auth")));
            String sender = StringUtil.trim(parameter.get("sender"));
            String senderAddress = StringUtil.trim(parameter.get("sender_address"));
            String password = StringUtil.trim(parameter.get("password"));

            if(name==null || name==""){
                return new MethodResult(MethodResult.FAIL,"配置名不能为空");
            }
            if(protocol==null || protocol==""){
                return new MethodResult(MethodResult.FAIL,"协议不能为空");
            }
            if(host==null || host==""){
                return new MethodResult(MethodResult.FAIL,"邮件服务器不能为空");
            }
            if(port==null){
                return new MethodResult(MethodResult.FAIL,"端口不能为空");
            }
            if(isAuth==null){
                return new MethodResult(MethodResult.FAIL,"身份验证不能为空");
            }
            if(senderAddress==null || senderAddress==""){
                return new MethodResult(MethodResult.FAIL,"邮箱不能为空");
            }
            if(password==null || password==""){
                return new MethodResult(MethodResult.FAIL,"密码不能为空");
            }

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", StringUtil.generateUUID());
            data.put("name", name);
            data.put("protocol", protocol);
            data.put("host", host);
            data.put("port", port);
            data.put("is_auth", isAuth);
            data.put("sender", sender);
            data.put("sender_address", senderAddress);
            data.put("password", password);
            data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

            int result = this.sqlSession.getMapper(EmailConfigMapper.class).insertConfig(data);

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
     * @function 修改邮件配置
     * @param parameter
     * @return
     */
    @Override
    @Transactional(readOnly=false)
    public MethodResult modifyConfigById(Map<String, Object> parameter) {

        try {
            // 参数处理
            String id = StringUtil.trim(parameter.get("id"));
            String name = StringUtil.trim(parameter.get("name"));
            String protocol = StringUtil.trim(parameter.get("protocol"));
            String host = StringUtil.trim(parameter.get("host"));
            Integer port = Integer.valueOf(StringUtil.trim(parameter.get("port")));
            Integer isAuth = Integer.valueOf(StringUtil.trim(parameter.get("is_auth")));
            String sender = StringUtil.trim(parameter.get("sender"));
            String senderAddress = StringUtil.trim(parameter.get("sender_address"));
            String password = StringUtil.trim(parameter.get("password"));

            if(id==null || id==""){
                return new MethodResult(MethodResult.FAIL,"ID不能为空");
            }
            if(name==null || name==""){
                return new MethodResult(MethodResult.FAIL,"配置名不能为空");
            }
            if(protocol==null || protocol==""){
                return new MethodResult(MethodResult.FAIL,"协议不能为空");
            }
            if(host==null || host==""){
                return new MethodResult(MethodResult.FAIL,"邮件服务器不能为空");
            }
            if(port==null){
                return new MethodResult(MethodResult.FAIL,"端口不能为空");
            }
            if(isAuth==null){
                return new MethodResult(MethodResult.FAIL,"身份验证不能为空");
            }
            if(senderAddress==null || senderAddress==""){
                return new MethodResult(MethodResult.FAIL,"邮箱不能为空");
            }
            if(password==null || password==""){
                return new MethodResult(MethodResult.FAIL,"密码不能为空");
            }

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", id);
            data.put("name", name);
            data.put("protocol", protocol);
            data.put("host", host);
            data.put("port", port);
            data.put("is_auth", isAuth);
            data.put("sender", sender);
            data.put("sender_address", senderAddress);
            data.put("password", password);
            data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

            int result = this.sqlSession.getMapper(EmailConfigMapper.class).updateConfig(data);

            if (result > 0) {
                return new MethodResult(MethodResult.SUCCESS, "修改成功");
            }

            return new MethodResult(MethodResult.FAIL, "修改失败");
        } catch( Exception e ) {
            logger.error(e);
            throw new AppException("修改失败");
        }

    }

    /**
     * @function 删除邮件配置
     * @param ids
     * @return
     */
    @Override
    @Transactional(readOnly=false)
    public MethodResult removeConfigByIds(List<?> ids) {
        try {
            if( ids == null || ids.size() == 0 )
            {
                return new MethodResult(MethodResult.FAIL, "ids不能为空");
            }

            int n = this.sqlSession.getMapper(EmailConfigMapper.class).deleteConfigByIds(
                ids.toArray(new String[0]));

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
