
package com.zhicloud.op.controller;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.service.constant.MonitorConstant;

/**
 * @ClassName: MonitorController
 * @Description: 系统监控控制器
 * @author 张本缘 于 2015年7月27日 上午11:02:41
 */
@Controller
public class MonitorController {

    // 日志
    public static final Logger logger = Logger.getLogger(MonitorController.class);
    // 当前controller返回的状态常量值
    public static final String success = "success";
    public static final String fail = "fail";

    /**
     * @Description:查询系统相关监控信息(根据不同参数区分)
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/monitor/querymonitordata.do")
    public void queryMonitorData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("MonitorController.queryMonitorData()");
        String type = StringUtil.trim(request.getParameter("type"));
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        // gw对应的几种状态
        Integer normal = 0, warn = 0, error = 0, stop = 0;
        // 返回状态初始化
        String status = "";
        // 查询服务器监控信息
        if ("server".equals(type)) {
            Iterator<String> its = MonitorConstant.serverMap.keySet().iterator();
            // cup和内存利用率初始化
            float cpu_usage = 0, memory_usage = 0;
            int divisor = 0;
            // 循环所有服务器信息
            while (its.hasNext()) {
                String rackid = its.next();
                JSONArray servers = MonitorConstant.serverMap.get(rackid);
                if (servers != null && servers.size() > 0) {
                    for (int i = 0; i < servers.size(); i++) {
                        // 除数+1
                        divisor++;
                        JSONObject server = (JSONObject) servers.get(i);
                        cpu_usage += Float.parseFloat(JSONLibUtil.getString(server, "cpu_usage"));
                        memory_usage += Float.parseFloat(JSONLibUtil.getString(server,
                                "memory_usage"));
                        status = MonitorConstant.judgeThresholdUseRule(server, MonitorConstant.server_flag);
                        if (MonitorConstant.status_normal.equals(status)) {
                            normal++;
                        } else if (MonitorConstant.status_warn.equals(status)) {
                            warn++;
                        } else if (MonitorConstant.status_error.equals(status)) {
                            error++;
                        } else {
                            stop++;
                        }
                    }
                }
            }
            // 计算利用率平均值
            if (divisor > 0) {
                map.put("cpu_usage", MonitorConstant.convertStringToDouble(cpu_usage / divisor + ""));
                map.put("memory_usage", MonitorConstant.convertStringToDouble(memory_usage / divisor + ""));
            }
            map.put("normal", normal);
            map.put("warn", warn);
            map.put("error", error);
            map.put("stop", stop);
            map.put("result", success);
        }
        // 查询云主机相关信息
        if ("host".equals(type)) {
            int divisor = 0;
            // 初始化读次数、写次数、收包数、发包数 用于计算
            BigInteger readtimes = BigInteger.ZERO, writetimes = BigInteger.ZERO, reseivenum = BigInteger.ZERO, sendnum = BigInteger.ZERO;
            Iterator<String> its = MonitorConstant.hostsMap.keySet().iterator();
            // 循环所有云主机信息
            while (its.hasNext()) {
                String poolid = its.next();
                JSONArray hosts = MonitorConstant.hostsMap.get(poolid);
                if (hosts != null && hosts.size() > 0) {
                    for (int i = 0; i < hosts.size(); i++) {
                        // 除数+1
                        divisor++;
                        JSONObject host = (JSONObject) hosts.get(i);
                        JSONObject cloudHostData = null;
                        // 根据host的uuid从其他地方取得当前主机的监控信息
                        if (host != null) {
                            cloudHostData = MonitorConstant.receiveData.get(JSONLibUtil.getString(host, "uuid"));
                            if (cloudHostData != null) {
                               // 大整数累加求和
                                readtimes = readtimes.add(BigInteger.valueOf(cloudHostData.getJSONArray("disk_io")
                                        .getLong(0)));
                                writetimes = writetimes.add(BigInteger.valueOf(cloudHostData.getJSONArray("disk_io")
                                        .getLong(2)));
                                reseivenum = reseivenum.add(BigInteger.valueOf(cloudHostData.getJSONArray("network_io")
                                        .getLong(1)));
                                sendnum = sendnum.add(BigInteger.valueOf(cloudHostData.getJSONArray("network_io")
                                        .getLong(5)));
                            }
                        }
                        status = MonitorConstant.judgeThresholdUseRule(host, MonitorConstant.host_flag);
                        if (MonitorConstant.status_normal.equals(status)) {
                            normal++;
                        } else if (MonitorConstant.status_warn.equals(status)) {
                            warn++;
                        } else if (MonitorConstant.status_error.equals(status)) {
                            error++;
                        } else {
                            stop++;
                        }
                    }
                    // 计算平均值
                    map.put("readtimes", readtimes.divide(new BigInteger(divisor + "")));
                    map.put("writetimes", writetimes.divide(new BigInteger(divisor + "")));
                    map.put("reseivenum", reseivenum.divide(new BigInteger(divisor + "")));
                    map.put("sendnum", sendnum.divide(new BigInteger(divisor + "")));
                    // 云主机状态
                    map.put("normal", normal);
                    map.put("warn", warn);
                    map.put("error", error);
                    map.put("stop", stop);
                    map.put("result", success);
                }
            }
        }
        ServiceHelper.writeJsonTo(response.getOutputStream(), map);
    }
}
