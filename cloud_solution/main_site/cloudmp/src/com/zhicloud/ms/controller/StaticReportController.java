
package com.zhicloud.ms.controller;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.ms.common.util.CapacityUtil;
import com.zhicloud.ms.constant.MonitorConstant;
import com.zhicloud.ms.constant.StaticReportConstant;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.StaticReportService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.constant.TransformConstant;
import com.zhicloud.ms.transform.controller.TransFormBaseAction;
import com.zhicloud.ms.transform.util.ExportExcelUtils;

/**
 * @ClassName: StaticReportController
 * @Description: 统计报表控制类
 * @author 张本缘 于 2015年10月12日 上午11:16:57
 */
@Controller
@RequestMapping("/staticreport")
public class StaticReportController extends TransFormBaseAction {
    /* 日志 */
    public static final Logger logger = Logger.getLogger(StaticReportController.class);

    @Resource
    private StaticReportService staticReportService;

    @Resource
    private IOperLogService operLogService;

    /**
     * @Description:取得云主机统计数据
     * @param request
     * @return String
     */
    @RequestMapping(value = "/cloudindex", method = RequestMethod.GET)
    public String cloudindex(HttpServletRequest request) {
        logger.debug("StaticReportController.cloudindex()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.static_report_cloud_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        // 取得内存中所有云主机信息
        List<JSONObject> hostsArray = new ArrayList<JSONObject>();
        Iterator<String> its = MonitorConstant.hostsMap.keySet().iterator();
        // CPU总核数
        Integer cpu_total = 0;

        BigInteger memory_total = new BigInteger("0");// 内存总量
        BigInteger memory_canuse = new BigInteger("0");// 内存可用
        BigInteger disk_total = new BigInteger("0");// 磁盘总量
        BigInteger disk_canuse = new BigInteger("0");// 磁盘可用
        // 从内存中读取存在的数据一次放入array中
        while (its.hasNext()) {
            String key = its.next();
            JSONArray array = MonitorConstant.hostsMap.get(key);
            if (array != null && array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject host = array.getJSONObject(i);
                    JSONObject json = new JSONObject();
                    json.put("id", host.getString("uuid"));
                    json.put("name", host.getString("name"));
                    json.put("status", MonitorConstant.judgeThresholdUseRule(host, MonitorConstant.host_flag));
                    cpu_total += host.getInt("cpu_count");
                    memory_canuse = memory_canuse.add(new BigInteger(host.getJSONArray("memory").getString(0)));
                    memory_total = memory_total.add(new BigInteger(host.getJSONArray("memory").getString(1)));
                    disk_canuse = disk_canuse.add(new BigInteger(host.getJSONArray("disk_volume").getString(0)));
                    disk_total = disk_total.add(new BigInteger(host.getJSONArray("disk_volume").getString(1)));
                    hostsArray.add(json);
                }
            }
        }
        request.setAttribute("cpu_total", cpu_total);
        request.setAttribute("memory_canuse", CapacityUtil.toGBValue(memory_canuse, 0));
        request.setAttribute("memory_used", CapacityUtil.toGBValue(memory_total.subtract(memory_canuse), 0));
        request.setAttribute("disk_canuse", CapacityUtil.toGBValue(disk_canuse, 0));
        request.setAttribute("disk_used", CapacityUtil.toGBValue(disk_total.subtract(disk_canuse), 0));
        request.setAttribute("cloudlists", hostsArray);
        request.setAttribute("statusdata", MonitorConstant.getStatusNum(hostsArray));
        request.setAttribute("total_num", hostsArray.size());
        return "/staticreport/static_report_cloud_total";
    }
    
    /**
     * @Description:取得服务器统计数据
     * @param request
     * @return String
     */
    @RequestMapping(value = "/serverindex", method = RequestMethod.GET)
    public String serverindex(HttpServletRequest request) {
        logger.debug("StaticReportController.serverindex()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.static_report_server_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        // 查询所有机房信息
        JSONArray rooms = new JSONArray();
        Iterator<String> its = MonitorConstant.roomMap.keySet().iterator();
        // 将所有的机房
        while (its.hasNext()) {
            JSONArray array = MonitorConstant.roomMap.get(its.next());
            if (array != null && array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    rooms.add(array.get(i));
                }
            }
        }
        request.setAttribute("rooms", rooms);
        return "/staticreport/static_report_server_total";
    }

    /**
     * @Description:根据云主机ID取得相关数据
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/getservertotaldata", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getServerTotalData(@RequestParam(value = "roomid", defaultValue = "") String roomid,
            HttpServletRequest request) {
        logger.debug("StaticReportController.getServerTotalData()");
        JSONObject result = new JSONObject();
        try {
            JSONArray serverobjs = new JSONArray();
            List<String> serverids = new ArrayList<String>();
            JSONArray racks = new JSONArray();
            // 查询所有机房
            if ("all".equals(roomid)) {
                Iterator<String> its = MonitorConstant.rackMap.keySet().iterator();
                while (its.hasNext()) {
                    JSONArray temparray = MonitorConstant.rackMap.get(its.next());
                    if (temparray != null && temparray.size() > 0) {
                        for (int i = 0; i < temparray.size(); i++) {
                            racks.add(temparray.get(i));
                        }
                    }
                }
            } else {
                racks = MonitorConstant.rackMap.get(roomid);
            }

            // 获取机架上的所有服务器
            if (racks != null && racks.size() > 0) {
                for (int i = 0; i < racks.size(); i++) {
                    JSONObject rack = racks.getJSONObject(i);
                    JSONArray servers = MonitorConstant.serverMap.get(rack.getString("uuid"));
                    if (servers != null && servers.size() > 0) {
                        for (int j = 0; j < servers.size(); j++) {
                            JSONObject server = servers.getJSONObject(j);
                            serverids.add(server.getString("uuid"));
                            serverobjs.add(server);
                        }
                    }
                }
            }

            JSONObject data = new JSONObject();
            // 取得内存中所有服务器信息
            List<JSONObject> hostsArray = new ArrayList<JSONObject>();
            Iterator<String> its = MonitorConstant.serverMap.keySet().iterator();
            // CPU总核数
            Integer cpu_total = 0;

            BigInteger memory_total = new BigInteger("0");// 内存总量
            BigInteger memory_canuse = new BigInteger("0");// 内存可用
            BigInteger disk_total = new BigInteger("0");// 磁盘总量
            BigInteger disk_canuse = new BigInteger("0");// 磁盘可用
            // 从内存中读取存在的数据一次放入array中
            while (its.hasNext()) {
                String key = its.next();
                JSONArray array = MonitorConstant.serverMap.get(key);
                if (array != null && array.size() > 0) {
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject server = array.getJSONObject(i);
                        if (!serverids.contains(server.getString("uuid"))) {
                            continue;
                        }
                        JSONObject json = new JSONObject();
                        json.put("id", server.getString("uuid"));
                        json.put("name", server.getString("name"));
                        json.put("status", MonitorConstant.judgeThresholdUseRule(server, MonitorConstant.server_flag));
                        cpu_total += server.getInt("cpu_count");
                        memory_canuse = memory_canuse.add(new BigInteger(server.getJSONArray("memory").getString(0)));
                        memory_total = memory_total.add(new BigInteger(server.getJSONArray("memory").getString(1)));
                        disk_canuse = disk_canuse.add(new BigInteger(server.getJSONArray("disk_volume").getString(0)));
                        disk_total = disk_total.add(new BigInteger(server.getJSONArray("disk_volume").getString(1)));
                        hostsArray.add(json);
                    }
                }
            }
            data.put("cpu_total", cpu_total);
            data.put("memory_canuse", CapacityUtil.toGBValue(memory_canuse, 0));
            data.put("memory_used", CapacityUtil.toGBValue(memory_total.subtract(memory_canuse), 0));
            data.put("disk_canuse", CapacityUtil.toGBValue(disk_canuse, 0));
            data.put("disk_used", CapacityUtil.toGBValue(disk_total.subtract(disk_canuse), 0));
            data.put("serverlists", hostsArray);
            data.put("statusdata", MonitorConstant.getStatusNum(hostsArray));
            data.put("total_num", hostsArray.size());
            data.put("servers", serverobjs);
            result.put("status", "success");
            result.put("data", data);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("status", "fali");
            result.put("message", e.getMessage());
        }
        return result;
    }
    
    /**
     * @Description:取得云主机概览数据
     * @param request
     * @return String
     */
    @RequestMapping(value = "/cloudhost/{id}", method = RequestMethod.GET)
    public String cloudSummary(@PathVariable String id, HttpServletRequest request) {
        logger.debug("StaticReportController.cloudSummary()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.static_report_cloud_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        // 取得内存中所有云主机信息
        List<JSONObject> hostsArray = new ArrayList<JSONObject>();
        Iterator<String> its = MonitorConstant.hostsMap.keySet().iterator();
        // 从内存中读取存在的数据一次放入array中
        while (its.hasNext()) {
            String key = its.next();
            JSONArray array = MonitorConstant.hostsMap.get(key);
            if (array != null && array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject host = array.getJSONObject(i);
                    JSONObject json = new JSONObject();
                    json.put("id", host.getString("uuid"));
                    json.put("name", host.getString("name"));
                    hostsArray.add(json);
                }
            }
        }
        request.setAttribute("cloudlists", hostsArray);
        request.setAttribute("cloudid", id);
        return "/staticreport/static_report_cloud_summary";
    }
    
    /**
     * @Description:取得服务器概览数据
     * @param request
     * @return String
     */
    @RequestMapping(value = "/server/{id}", method = RequestMethod.GET)
    public String serverSummary(@PathVariable String id, HttpServletRequest request) {
        logger.debug("StaticReportController.serverSummary()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.static_report_server_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        // 取得内存中所有服务器信息
        List<JSONObject> hostsArray = new ArrayList<JSONObject>();
        Iterator<String> its = MonitorConstant.serverMap.keySet().iterator();
        // 从内存中读取存在的数据一次放入array中
        while (its.hasNext()) {
            String key = its.next();
            JSONArray array = MonitorConstant.serverMap.get(key);
            if (array != null && array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject server = array.getJSONObject(i);
                    JSONObject json = new JSONObject();
                    json.put("id", server.getString("uuid"));
                    json.put("name", server.getString("name"));
                    hostsArray.add(json);
                }
            }
        }
        request.setAttribute("serverlists", hostsArray);
        request.setAttribute("serverid", id);
        return "/staticreport/static_report_server_summary";
    }

    /**
     * @Description:取得云主机明细数据
     * @param request
     * @return String
     */
    @RequestMapping(value = "/cloudhost/detail/{id}", method = RequestMethod.GET)
    public String cloudDetail(@PathVariable String id, HttpServletRequest request) {
        logger.debug("StaticReportController.cloudDetail()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.static_report_cloud_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        // 取得内存中所有云主机信息
        List<JSONObject> hostsArray = new ArrayList<JSONObject>();
        Iterator<String> its = MonitorConstant.hostsMap.keySet().iterator();
        // 从内存中读取存在的数据一次放入array中
        while (its.hasNext()) {
            String key = its.next();
            JSONArray array = MonitorConstant.hostsMap.get(key);
            if (array != null && array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject host = array.getJSONObject(i);
                    JSONObject json = new JSONObject();
                    json.put("id", host.getString("uuid"));
                    json.put("name", host.getString("name"));
                    hostsArray.add(json);
                }
            }
        }
        request.setAttribute("cloudlists", hostsArray);
        request.setAttribute("cloudid", id);
        return "/staticreport/static_report_cloud_detail";
    }
    
    /**
     * @Description:取得服务器明细数据
     * @param request
     * @return String
     */
    @RequestMapping(value = "/server/detail/{id}", method = RequestMethod.GET)
    public String serverDetail(@PathVariable String id, HttpServletRequest request) {
        logger.debug("StaticReportController.serverDetail()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.static_report_server_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        // 取得内存中所有云主机信息
        List<JSONObject> hostsArray = new ArrayList<JSONObject>();
        Iterator<String> its = MonitorConstant.serverMap.keySet().iterator();
        // 从内存中读取存在的数据一次放入array中
        while (its.hasNext()) {
            String key = its.next();
            JSONArray array = MonitorConstant.serverMap.get(key);
            if (array != null && array.size() > 0) {
                for (int i = 0; i < array.size(); i++) {
                    JSONObject server = array.getJSONObject(i);
                    JSONObject json = new JSONObject();
                    json.put("id", server.getString("uuid"));
                    json.put("name", server.getString("name"));
                    hostsArray.add(json);
                }
            }
        }
        request.setAttribute("serverlists", hostsArray);
        request.setAttribute("serverid", id);
        return "/staticreport/static_report_server_detail";
    }

    /**
     * @Description:根据云主机ID取得相关数据
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/gethostdata", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getHostData(@RequestParam(value = "id", defaultValue = "") String id,
            @RequestParam(value = "startdate", defaultValue = "") String startdate,
            @RequestParam(value = "enddate", defaultValue = "") String enddate,
            @RequestParam(value = "type", defaultValue = "") String type, HttpServletRequest request) {
        logger.debug("StaticReportController.getHostData()");
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject host = MonitorConstant.getObjectByUUID(id, MonitorConstant.hostsMap);
        // false直接短路是因为目前全是虚拟数据,host查询出来肯定没有值,为了页面调试效果,暂时屏蔽
        if (false && (host == null || host.isEmpty())) {
            result.put("status", "fail");
            result.put("message", "没有查询到主机");
        } else {
            /*
             * data.put("name", host.getString("name")); data.put("cpu_count", host.getString("cpu_count")); // 磁盘总量
             * BigInteger disk_total = new BigInteger(host.getJSONArray("disk_volume").getString(1)); BigInteger
             * disk_canuse = new BigInteger(host.getJSONArray("disk_volume").getString(0)); BigInteger disk_used =
             * disk_total.subtract(disk_canuse); data.put("diskdata", CapacityUtil.toGB(disk_total, 0) + "/" +
             * CapacityUtil.toGB(disk_used, 0)); // 内存 BigInteger memory_total = new
             * BigInteger(host.getJSONArray("memory").getString(1)); data.put("memorydata",
             * CapacityUtil.toGB(memory_total, 0)); // 状态 data.put("status", MonitorConstant.judgeThresholdUseRule(host,
             * MonitorConstant.host_flag)); result.put("data", data);
             */
            if ("all".equals(type)) {
                // cpu在线时长数据
                JSONArray online_times = new JSONArray();
                Integer len = 5;
                // 测试虚拟数据
                for (int i = 0; i < len; i++) {
                    JSONObject json = new JSONObject();
                    json.put("name", "2015101" + i);
                    json.put("value", new Random().nextInt(200));
                    online_times.add(json);
                }
                result.put("singlebar", online_times);
                // 多个柱形图数据
                JSONArray multybar = new JSONArray();
                // 测试虚拟数据
                for (int i = 0; i < len; i++) {
                    JSONObject json = new JSONObject();
                    json.put("y", "2015101" + i);
                    json.put("read", new Random().nextInt(200));
                    json.put("write", new Random().nextInt(200));
                    json.put("send", new Random().nextInt(200));
                    json.put("receive", new Random().nextInt(200));
                    multybar.add(json);
                }
                result.put("multybar", multybar);

                StaticReportConstant.cloudhost_summary_cpu_temp = online_times;
                StaticReportConstant.cloudhost_summary_readwrite_temp = multybar;
            } else if ("cpu".equals(type)) {
                // cpu在线时长数据
                JSONArray online_times = new JSONArray();
                Integer len = 5;
                // 自定义日期
                if (!startdate.isEmpty() && !enddate.isEmpty()) {
                    len = 7;
                }
                // 测试虚拟数据
                for (int i = 0; i < len; i++) {
                    JSONObject json = new JSONObject();
                    json.put("name", "2015101" + i);
                    json.put("value", new Random().nextInt(200));
                    online_times.add(json);
                }
                result.put("singlebar", online_times);
                StaticReportConstant.cloudhost_summary_cpu_temp = online_times;
            } else if ("multy".equals(type)) {
                // 多个柱形图数据
                JSONArray multybar = new JSONArray();
                Integer len = 5;
                // 自定义日期
                if (!startdate.isEmpty() && !enddate.isEmpty()) {
                    len = 7;
                }
                // 测试虚拟数据
                for (int i = 0; i < len; i++) {
                    JSONObject json = new JSONObject();
                    json.put("y", "2015101" + i);
                    json.put("read", new Random().nextInt(200));
                    json.put("write", new Random().nextInt(200));
                    json.put("send", new Random().nextInt(200));
                    multybar.add(json);
                }
                result.put("multybar", multybar);
                StaticReportConstant.cloudhost_summary_readwrite_temp = multybar;
            }

            result.put("status", "success");
        }

        return result;
    }
    
    /**
     * @Description:根据服务器ID取得相关数据
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/getserverdata", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getServerData(@RequestParam(value = "id", defaultValue = "") String id,
            @RequestParam(value = "startdate", defaultValue = "") String startdate,
            @RequestParam(value = "enddate", defaultValue = "") String enddate,
            @RequestParam(value = "type", defaultValue = "") String type, HttpServletRequest request) {
        logger.debug("StaticReportController.getServerData()");
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject server = MonitorConstant.getObjectByUUID(id, MonitorConstant.serverMap);
        if (false && (server == null || server.isEmpty())) {
            result.put("status", "fail");
            result.put("message", "没有查询到服务器");
        } else {
            /*
             * data.put("name", host.getString("name")); data.put("cpu_count", host.getString("cpu_count")); // 磁盘总量
             * BigInteger disk_total = new BigInteger(host.getJSONArray("disk_volume").getString(1)); BigInteger
             * disk_canuse = new BigInteger(host.getJSONArray("disk_volume").getString(0)); BigInteger disk_used =
             * disk_total.subtract(disk_canuse); data.put("diskdata", CapacityUtil.toGB(disk_total, 0) + "/" +
             * CapacityUtil.toGB(disk_used, 0)); // 内存 BigInteger memory_total = new
             * BigInteger(host.getJSONArray("memory").getString(1)); data.put("memorydata",
             * CapacityUtil.toGB(memory_total, 0)); // 状态 data.put("status", MonitorConstant.judgeThresholdUseRule(host,
             * MonitorConstant.host_flag)); result.put("data", data);
             */
            if ("all".equals(type)) {
                // cpu在线时长数据
                JSONArray online_times = new JSONArray();
                Integer len = 5;
                // 测试虚拟数据
                for (int i = 0; i < len; i++) {
                    JSONObject json = new JSONObject();
                    json.put("name", "2015101" + i);
                    json.put("value", new Random().nextInt(200));
                    online_times.add(json);
                }
                result.put("singlebar", online_times);
                // 多个柱形图数据
                JSONArray multybar = new JSONArray();
                // 测试虚拟数据
                for (int i = 0; i < len; i++) {
                    JSONObject json = new JSONObject();
                    json.put("y", "2015101" + i);
                    json.put("read", new Random().nextInt(200));
                    json.put("write", new Random().nextInt(200));
                    json.put("send", new Random().nextInt(200));
                    json.put("receive", new Random().nextInt(200));
                    multybar.add(json);
                }
                result.put("multybar", multybar);

                StaticReportConstant.server_summary_cpu_temp = online_times;
                StaticReportConstant.server_summary_readwrite_temp = multybar;
            } else if ("cpu".equals(type)) {
                // cpu在线时长数据
                JSONArray online_times = new JSONArray();
                Integer len = 5;
                // 自定义日期
                if (!startdate.isEmpty() && !enddate.isEmpty()) {
                    len = 7;
                }
                // 测试虚拟数据
                for (int i = 0; i < len; i++) {
                    JSONObject json = new JSONObject();
                    json.put("name", "2015101" + i);
                    json.put("value", new Random().nextInt(200));
                    online_times.add(json);
                }
                result.put("singlebar", online_times);
                StaticReportConstant.server_summary_cpu_temp = online_times;
            } else if ("multy".equals(type)) {
                // 多个柱形图数据
                JSONArray multybar = new JSONArray();
                Integer len = 5;
                // 自定义日期
                if (!startdate.isEmpty() && !enddate.isEmpty()) {
                    len = 7;
                }
                // 测试虚拟数据
                for (int i = 0; i < len; i++) {
                    JSONObject json = new JSONObject();
                    json.put("y", "2015101" + i);
                    json.put("read", new Random().nextInt(200));
                    json.put("write", new Random().nextInt(200));
                    json.put("send", new Random().nextInt(200));
                    multybar.add(json);
                }
                result.put("multybar", multybar);
                StaticReportConstant.server_summary_readwrite_temp = multybar;
            }

            result.put("status", "success");
        }

        return result;
    }

    /**
     * @Description:根据云主机ID取得明细数据
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/gethostdetaildata", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getHostDetailData(@RequestParam(value = "id", defaultValue = "") String id,
            @RequestParam(value = "startdate", defaultValue = "") String startdate,
            @RequestParam(value = "enddate", defaultValue = "") String enddate, HttpServletRequest request) {
        logger.debug("StaticReportController.getHostDetailData()");
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject host = MonitorConstant.getObjectByUUID(id, MonitorConstant.hostsMap);
        if (false && (host == null || host.isEmpty())) {
            result.put("status", "fail");
            result.put("message", "没有查询到主机");
        } else {
            /*
             * data.put("name", host.getString("name")); data.put("cpu_count", host.getString("cpu_count")); // 磁盘总量
             * BigInteger disk_total = new BigInteger(host.getJSONArray("disk_volume").getString(1)); BigInteger
             * disk_canuse = new BigInteger(host.getJSONArray("disk_volume").getString(0)); BigInteger disk_used =
             * disk_total.subtract(disk_canuse); data.put("diskdata", CapacityUtil.toGB(disk_total, 0) + "/" +
             * CapacityUtil.toGB(disk_used, 0)); // 内存 BigInteger memory_total = new
             * BigInteger(host.getJSONArray("memory").getString(1)); data.put("memorydata",
             * CapacityUtil.toGB(memory_total, 0)); // 状态 data.put("status", MonitorConstant.judgeThresholdUseRule(host,
             * MonitorConstant.host_flag)); result.put("data", data);
             */

            // 服务状态数据
            JSONArray server_status = new JSONArray();
            Integer len = 5;
            // 自定义日期
            if (!startdate.isEmpty() && !enddate.isEmpty()) {
                len = 7;
            }
            // 测试虚拟数据
            for (int i = 0; i < len; i++) {
                JSONObject json = new JSONObject();
                json.put("name", "2015101" + i);
                json.put("status", "测试");
                server_status.add(json);
            }
            result.put("server_status", server_status);

            // cpu在线时长数据
            JSONArray cpu_online_time = new JSONArray();
            // 测试虚拟数据
            for (int i = 0; i < len; i++) {
                JSONObject json = new JSONObject();
                json.put("name", "2015101" + i);
                json.put("value", new Random().nextInt(200));
                cpu_online_time.add(json);
            }
            result.put("cpu_online_time", cpu_online_time);

            // 内存和磁盘虚拟数据
            JSONArray memory_disk = new JSONArray();
            for (int i = 0; i < len; i++) {
                JSONObject json = new JSONObject();
                json.put("y", "2015-10-1" + i);
                json.put("memory_use", new Random().nextInt(100));
                json.put("disk_use", new Random().nextInt(100));
                memory_disk.add(json);
            }
            result.put("memory_disk", memory_disk);

            // 读/写模拟数据
            JSONArray read_write = new JSONArray();
            for (int i = 0; i < len; i++) {
                JSONObject json = new JSONObject();
                json.put("timestamp", "2015101" + i);
                json.put("read_byte", new Random().nextInt(100) + "M");
                json.put("write_byte", new Random().nextInt(100) + "M");
                json.put("read_request", new Random().nextInt(10000));
                json.put("write_request", new Random().nextInt(10000));
                json.put("read_speed", new Random().nextInt(2000));
                json.put("write_speed", new Random().nextInt(2000));
                read_write.add(json);
            }
            result.put("read_write", read_write);

            // 接收模拟数据
            JSONArray receive_data = new JSONArray();
            for (int i = 0; i < len; i++) {
                JSONObject json = new JSONObject();
                json.put("timestamp", "2015101" + i);
                json.put("received_bytes", new Random().nextInt(100) + "M");
                json.put("received_packets", new Random().nextInt(200));
                json.put("received_errors", new Random().nextInt(100));
                json.put("received_drop", new Random().nextInt(50));
                json.put("received_speed", new Random().nextInt(2000));
                receive_data.add(json);
            }
            result.put("receive_data", receive_data);

            // 发送模拟数据
            JSONArray sent_data = new JSONArray();
            for (int i = 0; i < len; i++) {
                JSONObject json = new JSONObject();
                json.put("timestamp", "2015101" + i);
                json.put("sent_bytes", new Random().nextInt(100) + "M");
                json.put("sent_packets", new Random().nextInt(200));
                json.put("sent_errors", new Random().nextInt(100));
                json.put("sent_drop", new Random().nextInt(50));
                json.put("sent_speed", new Random().nextInt(2000));
                sent_data.add(json);
            }
            result.put("sent_data", sent_data);

            // 更新数据到临时常量中,方便导出
            StaticReportConstant.cloudhost_detail_server_temp = server_status;
            StaticReportConstant.cloudhost_detail_onlinetime_temp = cpu_online_time;
            StaticReportConstant.cloudhost_detail_memorydisk_temp = memory_disk;
            StaticReportConstant.cloudhost_detail_readwrite_temp = read_write;
            StaticReportConstant.cloudhost_detail_receive_temp = receive_data;
            StaticReportConstant.cloudhost_detail_sent_temp = sent_data;

            result.put("status", "success");
        }

        return result;
    }
    
    
    /**
     * @Description:根据服务器ID取得明细数据
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/getserverdetaildata", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getServerDetailData(@RequestParam(value = "id", defaultValue = "") String id,
            @RequestParam(value = "startdate", defaultValue = "") String startdate,
            @RequestParam(value = "enddate", defaultValue = "") String enddate, HttpServletRequest request) {
        logger.debug("StaticReportController.getServerDetailData()");
        JSONObject result = new JSONObject();
        JSONObject data = new JSONObject();
        JSONObject server = MonitorConstant.getObjectByUUID(id, MonitorConstant.serverMap);
        if (false && (server == null || server.isEmpty())) {
            result.put("status", "fail");
            result.put("message", "没有查询到服务器");
        } else {
            /*
             * data.put("name", host.getString("name")); data.put("cpu_count", host.getString("cpu_count")); // 磁盘总量
             * BigInteger disk_total = new BigInteger(host.getJSONArray("disk_volume").getString(1)); BigInteger
             * disk_canuse = new BigInteger(host.getJSONArray("disk_volume").getString(0)); BigInteger disk_used =
             * disk_total.subtract(disk_canuse); data.put("diskdata", CapacityUtil.toGB(disk_total, 0) + "/" +
             * CapacityUtil.toGB(disk_used, 0)); // 内存 BigInteger memory_total = new
             * BigInteger(host.getJSONArray("memory").getString(1)); data.put("memorydata",
             * CapacityUtil.toGB(memory_total, 0)); // 状态 data.put("status", MonitorConstant.judgeThresholdUseRule(host,
             * MonitorConstant.host_flag)); result.put("data", data);
             */

            // 服务状态数据
            JSONArray server_status = new JSONArray();
            Integer len = 5;
            // 自定义日期
            if (!startdate.isEmpty() && !enddate.isEmpty()) {
                len = 7;
            }
            // 测试虚拟数据
            for (int i = 0; i < len; i++) {
                JSONObject json = new JSONObject();
                json.put("name", "2015101" + i);
                json.put("status", "测试");
                server_status.add(json);
            }
            result.put("server_status", server_status);

            // cpu在线时长数据
            JSONArray cpu_online_time = new JSONArray();
            // 测试虚拟数据
            for (int i = 0; i < len; i++) {
                JSONObject json = new JSONObject();
                json.put("name", "2015101" + i);
                json.put("value", new Random().nextInt(200));
                cpu_online_time.add(json);
            }
            result.put("cpu_online_time", cpu_online_time);

            // 内存和磁盘虚拟数据
            JSONArray memory_disk = new JSONArray();
            for (int i = 0; i < len; i++) {
                JSONObject json = new JSONObject();
                json.put("y", "2015-10-1" + i);
                json.put("memory_use", new Random().nextInt(100));
                json.put("disk_use", new Random().nextInt(100));
                memory_disk.add(json);
            }
            result.put("memory_disk", memory_disk);

            // 读/写模拟数据
            JSONArray read_write = new JSONArray();
            for (int i = 0; i < len; i++) {
                JSONObject json = new JSONObject();
                json.put("timestamp", "2015101" + i);
                json.put("read_byte", new Random().nextInt(100) + "M");
                json.put("write_byte", new Random().nextInt(100) + "M");
                json.put("read_request", new Random().nextInt(10000));
                json.put("write_request", new Random().nextInt(10000));
                json.put("read_speed", new Random().nextInt(2000));
                json.put("write_speed", new Random().nextInt(2000));
                read_write.add(json);
            }
            result.put("read_write", read_write);

            // 接收模拟数据
            JSONArray receive_data = new JSONArray();
            for (int i = 0; i < len; i++) {
                JSONObject json = new JSONObject();
                json.put("timestamp", "2015101" + i);
                json.put("received_bytes", new Random().nextInt(100) + "M");
                json.put("received_packets", new Random().nextInt(200));
                json.put("received_errors", new Random().nextInt(100));
                json.put("received_drop", new Random().nextInt(50));
                json.put("received_speed", new Random().nextInt(2000));
                receive_data.add(json);
            }
            result.put("receive_data", receive_data);

            // 发送模拟数据
            JSONArray sent_data = new JSONArray();
            for (int i = 0; i < len; i++) {
                JSONObject json = new JSONObject();
                json.put("timestamp", "2015101" + i);
                json.put("sent_bytes", new Random().nextInt(100) + "M");
                json.put("sent_packets", new Random().nextInt(200));
                json.put("sent_errors", new Random().nextInt(100));
                json.put("sent_drop", new Random().nextInt(50));
                json.put("sent_speed", new Random().nextInt(2000));
                sent_data.add(json);
            }
            result.put("sent_data", sent_data);

            // 更新数据到临时常量中,方便导出
            StaticReportConstant.server_detail_server_temp = server_status;
            StaticReportConstant.server_detail_onlinetime_temp = cpu_online_time;
            StaticReportConstant.server_detail_memorydisk_temp = memory_disk;
            StaticReportConstant.server_detail_readwrite_temp = read_write;
            StaticReportConstant.server_detail_receive_temp = receive_data;
            StaticReportConstant.server_detail_sent_temp = sent_data;

            result.put("status", "success");
        }

        return result;
    }

    /**
     * @Description:统计报表导出数据
     * @param request
     * @param response
     */
    @RequestMapping(value = "/exportdata/{name}/{type}", method = RequestMethod.GET)
    public void exportData(@PathVariable String name, @PathVariable String type, HttpServletRequest request,
            HttpServletResponse response) {
        logger.debug("StaticReportController.exportData()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            JSONArray data = null;
            String[][] columns = null;
            String title = "";
            if (StaticReportConstant.cscpu.equals(type)) {
                data = StaticReportConstant.cloudhost_summary_cpu_temp;
                columns = StaticReportConstant.summary_cpu_columns;
                title = name + "概览_CPU在线时长";
            } else if (StaticReportConstant.cswrite.equals(type)) {
                data = StaticReportConstant.cloudhost_summary_readwrite_temp;
                columns = StaticReportConstant.summary_readwrite_columns;
                title = name + "概览_读写和发送";
            } else if (StaticReportConstant.cdserver.equals(type)) {
                data = StaticReportConstant.cloudhost_detail_server_temp;
                columns = StaticReportConstant.summary_detail_server_columns;
                title = name + "明细_服务状态";
            } else if (StaticReportConstant.cdonline.equals(type)) {
                data = StaticReportConstant.cloudhost_detail_onlinetime_temp;
                columns = StaticReportConstant.summary_cpu_columns;
                title = name + "明细_CPU在线时长";
            } else if (StaticReportConstant.cdmemory.equals(type)) {
                data = StaticReportConstant.cloudhost_detail_memorydisk_temp;
                columns = StaticReportConstant.summary_detail_memorydisk_columns;
                title = name + "明细_内存磁盘利用率";
            } else if (StaticReportConstant.cdwrite.equals(type)) {
                data = StaticReportConstant.cloudhost_detail_readwrite_temp;
                columns = StaticReportConstant.summary_detail_readwrite_columns;
                title = name + "明细_读写";
            } else if (StaticReportConstant.cdreceive.equals(type)) {
                data = StaticReportConstant.cloudhost_detail_receive_temp;
                columns = StaticReportConstant.summary_detail_receive_columns;
                title = name + "明细_接收";
            } else if (StaticReportConstant.cdsent.equals(type)) {
                data = StaticReportConstant.cloudhost_detail_sent_temp;
                columns = StaticReportConstant.summary_detail_sent_columns;
                title = name + "明细_发送";
            } else if (StaticReportConstant.sscpu.equals(type)) {
                data = StaticReportConstant.server_summary_cpu_temp;
                columns = StaticReportConstant.summary_cpu_columns;
                title = name + "概览_CPU在线时长";
            } else if (StaticReportConstant.sswrite.equals(type)) {
                data = StaticReportConstant.server_summary_readwrite_temp;
                columns = StaticReportConstant.summary_readwrite_columns;
                title = name + "概览_读写和发送";
            } else if (StaticReportConstant.sdserver.equals(type)) {
                data = StaticReportConstant.server_detail_server_temp;
                columns = StaticReportConstant.summary_detail_server_columns;
                title = name + "明细_服务状态";
            } else if (StaticReportConstant.sdonline.equals(type)) {
                data = StaticReportConstant.server_detail_onlinetime_temp;
                columns = StaticReportConstant.summary_cpu_columns;
                title = name + "明细_CPU在线时长";
            } else if (StaticReportConstant.sdmemory.equals(type)) {
                data = StaticReportConstant.server_detail_memorydisk_temp;
                columns = StaticReportConstant.summary_detail_memorydisk_columns;
                title = name + "明细_内存磁盘利用率";
            } else if (StaticReportConstant.sdwrite.equals(type)) {
                data = StaticReportConstant.server_detail_readwrite_temp;
                columns = StaticReportConstant.summary_detail_readwrite_columns;
                title = name + "明细_读写";
            } else if (StaticReportConstant.sdreceive.equals(type)) {
                data = StaticReportConstant.server_detail_receive_temp;
                columns = StaticReportConstant.summary_detail_receive_columns;
                title = name + "明细_接收";
            } else if (StaticReportConstant.sdsent.equals(type)) {
                data = StaticReportConstant.server_detail_sent_temp;
                columns = StaticReportConstant.summary_detail_sent_columns;
                title = name + "明细_发送";
            }
            
            if (columns != null) {
                ExportExcelUtils.exportStaticData(request, response, data, title, columns);
                operLogService.addLog("统计报表", "导出[" + name + "]数据成功", "1", "1", request);
            }
        } catch (Exception e) {
            logger.error("StaticReportController.exportData()", e);
            operLogService.addLog("统计报表", "导出数据失败", "1", "2", request);
            throw new AppException("导出失败");
        }
    }
}
