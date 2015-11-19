package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.httpGateway.HttpGatewayChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.mapper.QosMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IQosService;
import com.zhicloud.ms.vo.QosVO;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;

/**
 * @description Qos service接口实现
 * @author 张翔
 */
@Service("qosService")
@Transactional(readOnly=true)
public class QosServiceImpl implements IQosService{


    private static final Logger logger = Logger.getLogger(ComputePoolServiceImpl.class);
    @Resource private SqlSession sqlSession;

    /**
     * @function 获取全部Qos规则
     * @return 全部Qos规则
     */
    @Override public List<QosVO> getAll(int type){
        return this.sqlSession.getMapper(QosMapper.class).queryAll(type);
    }

    /**
     * @function 检测Qos规则是否重名
     * @param condition
     * @return 全部是否存在该Qos规则
     */
    @Override public MethodResult checkName(Map<String, Object> condition) {
        QosVO qosVO = this.sqlSession.getMapper(QosMapper.class).queryByName(condition);
        if (qosVO != null) {
            return new MethodResult(MethodResult.FAIL, "该QoS规则已存在");
        }
        return new MethodResult(MethodResult.SUCCESS, "该QoS规则可用");
    }

    /**
     * @function 新增Qos规则
     * @param parameter Qos规则
     * @return 该方法执行结果（成功或者失败）
     */
    @Override public MethodResult addQos(Map<String, Object> parameter) {

        parameter.put("id", StringUtil.generateUUID());
        parameter.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));

        String uuid = StringUtil.trim(parameter.get("uuid"));
        BigInteger inboundBandwidth = new BigInteger(StringUtil.trim(parameter.get("inbound_bandwidth")));
        BigInteger outboundBandwidth = new BigInteger(StringUtil.trim(parameter.get("outbound_bandwidth")));
        Integer maxIops = Integer.valueOf(StringUtil.trim(parameter.get("max_iops")));
        Integer priority = Integer.valueOf(StringUtil.trim(parameter.get("priority")));

        String ip = null;
        String serverIp = null;

        HttpGatewayChannel channel = HttpGatewayManager.getChannel(1);
        try {
            //获取ip和宿主机ip
            JSONObject data = channel.hostQueryInfo(uuid);
            if (!HttpGatewayResponseHelper.isSuccess(data)){
                return new MethodResult(MethodResult.FAIL, "找不到该主机");
            }
            serverIp = JSONLibUtil.getStringArray(data.getJSONObject("host"), "ip")[0];
            ip = JSONLibUtil.getStringArray(data.getJSONObject("host"), "ip")[1];
            parameter.put("server_ip", serverIp);
            parameter.put("ip", ip);
        } catch (IOException e) {
            e.printStackTrace();
            return new MethodResult(MethodResult.FAIL, "创建失败");

        }
        try {
            JSONObject result = channel.hostModify(uuid, "", 0, BigInteger.ZERO, new Integer[0],
                new Integer[0], "", "", "", inboundBandwidth, outboundBandwidth, maxIops, priority);
            if ("fail".equals(result.getString("status"))) {
                return new MethodResult(MethodResult.FAIL, "创建失败");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new MethodResult(MethodResult.FAIL, "创建失败");
        }

        Integer result = this.sqlSession.getMapper(QosMapper.class).addQos(parameter);

        if(result > 0) {
            return new MethodResult(MethodResult.SUCCESS, "创建成功");
        }

        return new MethodResult(MethodResult.FAIL, "创建失败");
    }

    /**
     * @function 移除Qos规则
     * @param ids 关联云主机uuid
     * @return 该方法执行结果（成功或者失败）
     */
    @Override public MethodResult removeQos(List<String> ids) {
        HttpGatewayChannel channel = HttpGatewayManager.getChannel(1);
        int count = 0;
        JSONObject result = null;
        Iterator<String> it = ids.iterator();
        while (it.hasNext()) {
            String uuid = it.next();

            try {
                result = channel.hostModify(uuid, "", 0, BigInteger.ZERO, new Integer[0], new Integer[0], "", "", "", BigInteger.ZERO, BigInteger.ZERO, 0, 0);
                if ("success".equals(result.getString("status"))) {
                    count++;
                }
            } catch (IOException e) {
                logger.error(e);
                return new MethodResult(MethodResult.FAIL, "删除失败");
            }
            if (count != ids.size()) {
                return new MethodResult(MethodResult.FAIL, "删除失败");
            }
        }

        Integer n = this.sqlSession.getMapper(QosMapper.class).deleteQosByHostUuids(ids.toArray(new String[ids.size()]));

        if(n > 0) {
            return new MethodResult(MethodResult.SUCCESS, "删除成功");
        }

        return new MethodResult(MethodResult.FAIL, "删除失败");
    }
}
