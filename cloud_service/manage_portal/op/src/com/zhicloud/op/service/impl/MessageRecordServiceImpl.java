package com.zhicloud.op.service.impl;

import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.message.sms.SmsInterfaceQuery;
import com.zhicloud.op.mybatis.mapper.MessageRecordMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.MessageRecordService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.MessageRecordVO;

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
public class MessageRecordServiceImpl extends BeanDirectCallableDefaultImpl implements
    MessageRecordService {

    public static final Logger logger = Logger.getLogger(MessageRecordServiceImpl.class);

    private SqlSession sqlSession;

    public void setSqlSession(SqlSession sqlSession)
    {
        this.sqlSession = sqlSession;
    }

    @Callable
    public String managePage(HttpServletRequest request, HttpServletResponse response) {

        Integer type = Integer.valueOf(request.getParameter("type"));
        if (AppConstant.MESSAGE_TYPE_EMAIL.equals(type)) {
            return "/security/operator/message/email/email_record_manage.jsp";
        }
        // 用于页面显示回调结果
        Map<String, Object> balanceMap = SmsInterfaceQuery.getBalance();
        request.setAttribute("balance_totaled", balanceMap.get("Totaled"));
        request.setAttribute("balance_sended", balanceMap.get("Sended"));
        request.setAttribute("balance_balance", balanceMap.get("balance"));
        return "/security/operator/message/sms/sms_record_manage.jsp";

    }

    @Callable
    public void getAllRecord(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("EmailConfigServiceImpl.getAllConfig()");
        try
        {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");

            // 获取参数
            String type = StringUtil.trim(request.getParameter("type"));
            String recipient_address = StringUtil.trim(request.getParameter("recipient_address"));
            Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
            Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);

            // 查询数据库
            MessageRecordMapper mailRecordMapper = this.sqlSession.getMapper(MessageRecordMapper.class);
            Map<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("recipient_address", "%" + recipient_address + "%");
            condition.put("type", type);
            condition.put("start_row", page * rows);
            condition.put("row_count", rows);
            int total = mailRecordMapper.queryAllPageCount(condition); // 总行数
            List<MessageRecordVO> configList = mailRecordMapper.queryAllPage(condition);
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

            MessageRecordMapper mailRecordMapper = this.sqlSession.getMapper(MessageRecordMapper.class);

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("id", StringUtil.generateUUID());
            data.put("type", type);
            data.put("sender_address", senderAddress);
            data.put("recipient_address", recipientAddress);
            data.put("content", content);
            // 增加短信发送状态信息
            data.put("sms_state", StringUtil.trim(parameter.get("sms_state")));
            data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

            int result = mailRecordMapper.insertRecord(data);

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
    public MethodResult deleteRecordByIds(List<?> ids) {
        try {
            if( ids == null || ids.size() == 0 )
            {
                return new MethodResult(MethodResult.FAIL, "ids不能为空");
            }

            MessageRecordMapper mailRecordMapper = this.sqlSession.getMapper(MessageRecordMapper.class);
            int n = mailRecordMapper.deleteRecord(ids.toArray(new String[0]));

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
