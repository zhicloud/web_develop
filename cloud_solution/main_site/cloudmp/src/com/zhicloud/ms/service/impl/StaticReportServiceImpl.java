
package com.zhicloud.ms.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.constant.StaticReportHandle;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.service.StaticReportService;


/**
 * @ClassName: StaticReportServiceImpl
 * @Description: 统计报表实现
 * @author 张本缘 于 2015年10月12日 上午11:18:46
 */
@Service("staticReportService")
@Transactional(readOnly=false)
public class StaticReportServiceImpl implements StaticReportService {
    /* 日志 */
    public static final Logger logger = Logger.getLogger(StaticReportServiceImpl.class);
    
    @Resource
    private SqlSession sqlSession;

    /**
     * @Description:发送获取明细异步请求
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONObject
     * @throws IOException
     * @throws MalformedURLException
     */
    public boolean sendAsyRequest(String target, Integer level, String begin, String end, Integer type) {
        HttpGatewayAsyncChannel channel_asy = null;
        JSONObject result = null;
        boolean returnflag = false;
        // 发送异步请求前，先判断是否为相同条件的查询
        if (target.equals(StaticReportHandle.target) && begin.equals(StaticReportHandle.begin)
                && end.equals(StaticReportHandle.end)) {
            return true;
        }
        try {
            channel_asy = HttpGatewayManager.getAsyncChannel(1);
            if (type == 1) {
                result = channel_asy.query_service_detail(target, level, begin, end);
            } else if (type == 2) {
                result = channel_asy.query_operate_detail(target, level, begin, end);
            }
            if (HttpGatewayResponseHelper.isSuccess(result) == true) {
                StaticReportHandle.target = target;
                StaticReportHandle.begin = begin;
                StaticReportHandle.end = end;
                // 等待五秒，以便异步接口处理回调数据
                synchronized (this) {
                    try {
                        this.wait(5 * 1000);
                        returnflag = true;
                    } catch (InterruptedException e) {
                        logger.error("等待统计接口查询回调时出错", e);
                    }
                }
            }
        } catch (Exception e) {
            channel_asy.release();
            e.printStackTrace();
        }

        return returnflag;
    }

    /**
     * @Description:获取cpu在线时长数据
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONArray
     */
    public JSONArray getCpuOnlineData(String target, Integer level, String begin, String end, Integer type) {
        boolean result = this.sendAsyRequest(target, level, begin, end, type);
        if (result) {
            return StaticReportHandle.getCpuOnLine(type);
        }
        return null;
    }
    
    /**
     * @Description:获取复杂柱形图数据
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONArray
     */
    public JSONArray getMultyData(String target, Integer level, String begin, String end, Integer type) {
        boolean result = this.sendAsyRequest(target, level, begin, end, type);
        if (result) {
            return StaticReportHandle.getMultyBarData(type);
        }
        return null;
    }
    
    /**
     * @Description:获取服务状态数据
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONArray
     */
    public JSONArray getServiceStatus(String target, Integer level, String begin, String end, Integer type) {
        boolean result = this.sendAsyRequest(target, level, begin, end, type);
        if (result) {
            return StaticReportHandle.getServerStatus(type);
        }
        return null;
    }
    
    /**
     * @Description:获取内存和磁盘数据
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONArray
     */
    public JSONArray getMemoryAndDisk(String target, Integer level, String begin, String end, Integer type) {
        boolean result = this.sendAsyRequest(target, level, begin, end, type);
        if (result) {
            return StaticReportHandle.getMemoryAndDisk(type);
        }
        return null;
    }
    
    /**
     * @Description:获取读/写数据
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONArray
     */
    public JSONArray getReadAndWrite(String target, Integer level, String begin, String end, Integer type) {
        boolean result = this.sendAsyRequest(target, level, begin, end, type);
        if (result) {
            return StaticReportHandle.getReadAndWrite(type);
        }
        return null;
    }
    
    /**
     * @Description:获取接收数据
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONArray
     */
    public JSONArray getReceiveData(String target, Integer level, String begin, String end, Integer type) {
        boolean result = this.sendAsyRequest(target, level, begin, end, type);
        if (result) {
            return StaticReportHandle.getReceiveData(type);
        }
        return null;
    }
    
    /**
     * @Description:获取发送数据
     * @param target 目标ID
     * @param level 级别(0=month;1=day;2=hour;3=minute)
     * @param begin 开始日期
     * @param end 结束日期
     * @param type 类型(1、服务明细 2、操作明细)
     * @return JSONArray
     */
    public JSONArray getSentData(String target, Integer level, String begin, String end, Integer type) {
        boolean result = this.sendAsyRequest(target, level, begin, end, type);
        if (result) {
            return StaticReportHandle.getSentData(type);
        }
        return null;
    } 
    
}
