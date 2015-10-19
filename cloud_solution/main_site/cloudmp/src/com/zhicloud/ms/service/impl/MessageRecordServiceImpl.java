package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.mapper.MessageRecordMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IMessageRecordService;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.MessageRecordVO;
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
public class MessageRecordServiceImpl implements IMessageRecordService {

    public static final Logger logger = Logger.getLogger(MessageRecordServiceImpl.class);

    private SqlSession sqlSession;

    public void setSqlSession(SqlSession sqlSession)
    {
        this.sqlSession = sqlSession;
    }


    @Override
    public List<MessageRecordVO> getAllRecord(int type) {
        logger.debug("EmailConfigServiceImpl.getAllConfig()");
        return this.sqlSession.getMapper(MessageRecordMapper.class).queryAllRecord(type);
    }

    @Override
    public MessageRecordVO getRecordById(String id) {
        logger.debug("EmailConfigServiceImpl.getAllConfig()");
        return this.sqlSession.getMapper(MessageRecordMapper.class).queryRecordById(id);
    }

    @Override
    @Transactional(readOnly = false)
    public MethodResult addRecord(Map<String, Object> parameter) {
        try {
            // 参数处理
            String senderAddress = StringUtil.trim(parameter.get("sender_address"));
            String recipientAddress = StringUtil.trim(parameter.get("recipient_address"));
            String content = StringUtil.trim(parameter.get("content"));
            Integer type = Integer.valueOf(StringUtil.trim(parameter.get("type")));

            if(senderAddress==null || senderAddress==""){
                return new MethodResult(MethodResult.FAIL,"发送邮箱不能为空");
            }
            if(recipientAddress ==null || recipientAddress==""){
                return new MethodResult(MethodResult.FAIL,"邮件邮箱不能为空");
            }
            if(content==null || content==""){
                return new MethodResult(MethodResult.FAIL,"邮件内容不能为空");
            }

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", StringUtil.generateUUID());
            data.put("type", type);
            data.put("sender_address", senderAddress);
            data.put("recipient_address", recipientAddress);
            data.put("content", content);
            // 增加短信发送状态信息
            data.put("sms_state", StringUtil.trim(parameter.get("sms_state")));
            data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

            int result = this.sqlSession.getMapper(MessageRecordMapper.class).insertRecord(data);

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
    public MethodResult removeRecordByIds(List<?> ids) {
        try {
            if( ids == null || ids.size() == 0 )
            {
                return new MethodResult(MethodResult.FAIL, "ids不能为空");
            }

            int n = this.sqlSession.getMapper(MessageRecordMapper.class).deleteRecordByIds(
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
            e.printStackTrace();
            logger.error(e);
            throw new AppException("删除失败");
        }
    }
}
