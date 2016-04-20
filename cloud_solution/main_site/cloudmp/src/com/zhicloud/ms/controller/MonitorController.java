
package com.zhicloud.ms.controller;

import java.math.BigInteger;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.ms.app.pool.serviceInfoPool.ServiceInfoExt;
import com.zhicloud.ms.app.pool.serviceInfoPool.ServiceInfoPool;
import com.zhicloud.ms.app.pool.serviceInfoPool.ServiceInfoPoolManager;
import com.zhicloud.ms.common.util.CapacityUtil;
import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.constant.MonitorConstant;
import com.zhicloud.ms.mapper.CloudHostMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudHostService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.MonitorService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.constant.TransformConstant;
import com.zhicloud.ms.transform.controller.TransFormBaseAction;
import com.zhicloud.ms.vo.CloudHostVO;

/**
 * @ClassName: MonitorController
 * @Description: 监控控制类
 * @author 张本缘 于 2015年8月17日 下午2:59:26
 */
@Controller
@RequestMapping("/monitor")
public class MonitorController extends TransFormBaseAction {
    /* 日志 */
    public static final Logger logger = Logger.getLogger(MonitorController.class);

    private SqlSession sqlSession;
    
    @Resource
    private MonitorService monitorService;
    
    @Resource
    private IOperLogService operLogService;
    
    @Resource
	ICloudHostService cloudHostService;

    public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	/**
     * @Description:取得区域监控数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/areaquery", method = RequestMethod.GET)
    public String areaQuery(HttpServletRequest request) {
        logger.debug("MonitorController.areaQuery()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.monitor_area_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        List<JSONObject> list = monitorService.areaQuery();
        JSONObject json = MonitorConstant.getStatusNum(list);
        request.setAttribute("areaList", list);
        request.setAttribute("statusdata", json);
        return "/monitor/monitor_area_manage";
    }

    /**
     * @Description:取得机房监控数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/roomquery", method = RequestMethod.GET)
    public String roomQuery(@RequestParam(value = "data", defaultValue = "") String data, HttpServletRequest request) {
        logger.debug("MonitorController.roomQuery()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.monitor_room_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        // 参数
        JSONObject params = new JSONObject();
        if ("".equals(data)) {
            params = new JSONObject();
        } else {
            params = JSONObject.fromObject(data);
            params.put("areaid", params.getString("areaid"));
            params.put("menuflag", params.getString("menuflag"));
            params.put("areaname", MonitorConstant.nameMap.get(params.getString("areaid")));
        }
        List<JSONObject> list = monitorService.roomQuery(params);
        JSONObject json = MonitorConstant.getStatusNum(list);
        request.setAttribute("totalnum", list.size());
        // 页面点击警告或故障按钮过滤数据
        if (!params.isEmpty() && list.size() > 0) {
            String status = params.get("status") == null ? null : params.getString("status");
            if (status != null && !status.isEmpty()) {
                Iterator<JSONObject> its = list.iterator();
                while (its.hasNext()) {
                    if (!status.equals(its.next().getString("status"))) {
                        its.remove();
                    }
                }
            }
        }
        request.setAttribute("roomList", list);
        request.setAttribute("params", params);
        request.setAttribute("statusdata", json);
        return "/monitor/monitor_room_manage";
    }

    /**
     * @Description:取得机架监控数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/rackquery", method = RequestMethod.GET)
    public String rackQuery(@RequestParam(value = "data", defaultValue = "") String data, HttpServletRequest request) {
        logger.debug("MonitorController.rackQuery()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.monitor_rack_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        // 参数
        JSONObject params = null;
        if ("".equals(data)) {
            params = new JSONObject();
        } else {
            params = JSONObject.fromObject(data);
            params.put("areaname", MonitorConstant.nameMap.get(params.getString("areaid")));
            params.put("roomname", MonitorConstant.nameMap.get(params.getString("roomid")));
        }
        List<JSONObject> list = monitorService.rackQuery(params);
        JSONObject json = MonitorConstant.getStatusNum(list);
        request.setAttribute("totalnum", list.size());
        // 页面点击警告或故障按钮过滤数据
        if (!params.isEmpty() && list.size() > 0) {
            String status = params.get("status") == null ? null : params.getString("status");
            if (status != null && !status.isEmpty()) {
                Iterator<JSONObject> its = list.iterator();
                while (its.hasNext()) {
                    if (!status.equals(its.next().getString("status"))) {
                        its.remove();
                    }
                }
            }
        }
        request.setAttribute("rackList", list);
        request.setAttribute("params", params);
        request.setAttribute("statusdata", json);
        return "/monitor/monitor_rack_manage";
    }

    /**
     * @Description:取得服务器监控数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/serverquery", method = RequestMethod.GET)
    public String serverQuery(@RequestParam(value = "data", defaultValue = "") String data, HttpServletRequest request) {
        logger.debug("MonitorController.serverQuery()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.monitor_server_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        // 参数
        JSONObject params = null;
        if ("".equals(data)) {
            params = new JSONObject();
        } else {
            params = JSONObject.fromObject(data);
            params.put("areaname",
                    MonitorConstant.nameMap.get(params.get("areaid") == null ? "" : params.getString("areaid")));
            params.put("roomname",
                    MonitorConstant.nameMap.get(params.get("roomid") == null ? "" : params.getString("roomid")));
            params.put("rackname",
                    MonitorConstant.nameMap.get(params.get("rackid") == null ? "" : params.getString("rackid")));
        }
        List<JSONObject> list = monitorService.serverQuery(params);
        JSONObject json = MonitorConstant.getStatusNum(list);
        request.setAttribute("totalnum", list.size());
        // 页面点击警告或故障按钮过滤数据
        if (!params.isEmpty() && list.size() > 0) {
            String status = params.get("status") == null ? null : params.getString("status");
            if (status != null && !status.isEmpty()) {
                Iterator<JSONObject> its = list.iterator();
                while (its.hasNext()) {
                    if (!status.equals(its.next().getString("status"))) {
                        its.remove();
                    }
                }
            }
        }
        request.setAttribute("serverList", list);
        request.setAttribute("params", params);
        request.setAttribute("statusdata", json);
        return "/monitor/monitor_server_manage";
    }

    /**
     * @Description:取得云主机监控数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/hostquery", method = RequestMethod.GET)
    public String hostQuery(@RequestParam(value = "data", defaultValue = "") String data, HttpServletRequest request) {
        logger.debug("MonitorController.hostQuery()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.monitor_host_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        // 参数
        JSONObject params = null;
        if ("".equals(data)) {
            params = new JSONObject();
        } else {
            params = JSONObject.fromObject(data);
            params.put("areaname", MonitorConstant.nameMap.get(params.getString("areaid")));
            params.put("roomname", MonitorConstant.nameMap.get(params.getString("roomid")));
            params.put("rackname", MonitorConstant.nameMap.get(params.getString("rackid")));
            params.put("servername", MonitorConstant.nameMap.get(params.getString("serverid")));
        }
        List<JSONObject> list = monitorService.hostQuery(params);
        JSONObject json = MonitorConstant.getStatusNum(list);
        request.setAttribute("totalnum", list.size());
        // 页面点击警告或故障按钮过滤数据
        if (!params.isEmpty() && list.size() > 0) {
            String status = params.get("status") == null ? null : params.getString("status");
            if (status != null && !status.isEmpty()) {
                Iterator<JSONObject> its = list.iterator();
                while (its.hasNext()) {
                    if (!status.equals(its.next().getString("status"))) {
                        its.remove();
                    }
                }
            }
        }
        request.setAttribute("hostList", list);
        request.setAttribute("params", params);
        request.setAttribute("statusdata", json);
        return "/monitor/monitor_host_manage";
    }
    
    /**
     * @Description:取得资源池监控数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/resourcequery", method = RequestMethod.GET)
    public String resourceQuery(@RequestParam(value = "data", defaultValue = "") String data, HttpServletRequest request) {
        logger.debug("MonitorController.resourceQuery()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.monitor_resource_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        Iterator<String> its = MonitorConstant.hostsMap.keySet().iterator();
        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();
        while (its.hasNext()) {
            String key = its.next();
            json = MonitorConstant.getStatusNumByArray(MonitorConstant.hostsMap.get(key));
            json.put("resourceid", key.split("∮")[0]);
            json.put("resourcename", key.split("∮")[1]);
            json.put("areaname", key.split("∮")[2]);
            JSONObject pool = MonitorConstant.resourceData.get(key.split("∮")[0]);
            json.put("cpu_usage", MonitorConstant.convertStringToDouble(pool.get("cpu_usage") + ""));
            json.put("memory_usage", MonitorConstant.convertStringToDouble(pool.get("memory_usage") + ""));
            json.put("disk_usage", MonitorConstant.convertStringToDouble(pool.get("disk_usage") + ""));
            array.add(json);
        }
        request.setAttribute("resourcedata", array);
        return "/monitor/monitor_resource_manage";
    }
    
    /**
     * @Description:取得资源池监控数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/shieldobject", method = RequestMethod.POST)
    @ResponseBody
    public MethodResult shieldobject(@RequestParam(value = "data", defaultValue = "") String data,
            HttpServletRequest request) {
        logger.debug("MonitorController.shieldobject()");
        JSONObject json = JSONObject.fromObject(data);
        if (json != null && !json.isEmpty()) {
            boolean flag = false;
            String type = json.getString("type");
            if ("server".equals(type)) {
                flag = isHasPrivilege(request, TransFormPrivilegeConstant.monitor_system_shield_server);
            } else {
                flag = isHasPrivilege(request, TransFormPrivilegeConstant.monitor_system_shield_host);

            }
            if (!flag) {
                return new MethodResult(MethodResult.FAIL, "没有操作权限");
            }
            String shield = json.getString("shield");
            String uuid = json.getString("uuid");
            Iterator<String> its = null;
            String key = "";
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("id", uuid);
            map.put("shield", shield);
            if ("server".equals(type)) {
                try {
                    synchronized (MonitorConstant.serverMap) {
                        its = MonitorConstant.serverMap.keySet().iterator();
                        JSONArray array = new JSONArray();
                        while (its.hasNext()) {
                            key = its.next();
                            array = MonitorConstant.serverMap.get(key);
                            for (int i = 0; i < array.size(); i++) {
                                JSONObject server = array.getJSONObject(i);
                                if (uuid.equals(server.getString("uuid"))) {
                                    server.put(MonitorConstant.shield, shield);
                                    MonitorConstant.shieldmap.put(server.getString("uuid"), shield);
                                }
                            }
                        }
                    }
                    monitorService.saveShieldData(map);
                    // 增加操作日志
                    if (MonitorConstant.shield_on.equals(shield)) {
                        operLogService.addLog("服务器信息监控", "服务器:" + uuid + ",取消屏蔽成功", "1", "1", request);
                    } else {
                        operLogService.addLog("服务器信息监控", "服务器:" + uuid + ",屏蔽成功", "1", "1", request);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (MonitorConstant.shield_on.equals(shield)) {
                        operLogService.addLog("服务器信息监控", "服务器:" + uuid + ",取消屏蔽失败", "1", "2", request);
                    } else {
                        operLogService.addLog("服务器信息监控", "服务器:" + uuid + ",屏蔽失败", "1", "2", request);
                    }           
               }
                
            } else if ("host".equals(type)) {
                try {
                    synchronized (MonitorConstant.hostsMap) {
                        its = MonitorConstant.hostsMap.keySet().iterator();
                        JSONArray array = new JSONArray();
                        while (its.hasNext()) {
                            key = its.next();
                            array = MonitorConstant.hostsMap.get(key);
                            for (int i = 0; i < array.size(); i++) {
                                JSONObject host = array.getJSONObject(i);
                                if (uuid.equals(host.getString("uuid"))) {
                                    host.put(MonitorConstant.shield, shield);
                                    MonitorConstant.shieldmap.put(host.getString("uuid"), shield);
                                }
                            }
                        }
                    }
                    monitorService.saveShieldData(map);
                    // 增加操作日志
                    if (MonitorConstant.shield_on.equals(shield)) {
                        operLogService.addLog("云主机信息监控", "云主机:" + uuid + ",取消屏蔽成功", "1", "1", request);
                    } else {
                        operLogService.addLog("云主机信息监控", "云主机:" + uuid + ",屏蔽成功", "1", "1", request);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // 增加操作日志
                    if (MonitorConstant.shield_on.equals(shield)) {
                        operLogService.addLog("云主机信息监控", "云主机:" + uuid + ",取消屏蔽失败", "1", "2", request);
                    } else {
                        operLogService.addLog("云主机信息监控", "云主机:" + uuid + ",屏蔽失败", "1", "2", request);
                    }
                }
            } else if ("service".equals(type)) {
                // 服务操作同时更新服务的存放变量值
                ServiceInfoPool pool = ServiceInfoPoolManager.singleton().getPool();
                ServiceInfoExt service = pool.get(uuid);
                try {
                    if (MonitorConstant.shield_off.equals(shield)) {
                        service.setNewStatus(MonitorConstant.status_stop);
                    } else {
                        // 取消屏蔽 更新回原来的初始状态
                        if ((service.getStatus() + "").equals(MonitorConstant.gw_status_normal)) {
                            service.setNewStatus(MonitorConstant.status_normal);
                        }
                        if ((service.getStatus() + "").equals(MonitorConstant.gw_status_warn)) {
                            service.setNewStatus(MonitorConstant.status_warn);
                        }
                        if ((service.getStatus() + "").equals(MonitorConstant.gw_status_error)
                                || (service.getStatus() + "").equals(MonitorConstant.gw_status_stop)) {
                            service.setNewStatus(MonitorConstant.status_error);
                        }
                    }
                    MonitorConstant.shieldmap.put(uuid, shield);
                    pool.put(service);
                    monitorService.saveShieldData(map);
                    // 增加操作日志
                    if (MonitorConstant.shield_on.equals(shield)) {
                        operLogService.addLog("服务管理", "服务:" + service.getName() + ",取消屏蔽成功", "1", "1", request);
                    } else {
                        operLogService.addLog("服务管理", "服务:" + service.getName() + ",屏蔽成功", "1", "1", request);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // 增加操作日志
                    if (MonitorConstant.shield_on.equals(shield)) {
                        operLogService.addLog("服务管理", "服务:" + service.getName() + ",取消屏蔽失败", "1", "2", request);
                    } else {
                        operLogService.addLog("服务管理", "服务:" + service.getName() + ",屏蔽失败", "1", "2", request);
                    }
                }
            }
            return new MethodResult(MethodResult.SUCCESS, "操作成功");
        }
        return new MethodResult(MethodResult.FAIL, "操作失败,请联系管理员");
    }
    
    /**
     * @Description:取得资源池监控数据
     * @param request
     * @return
     */
    @RequestMapping(value = "/viewdetail", method = RequestMethod.GET)
    public String viewDetail(@RequestParam(value = "data", defaultValue = "") String data, HttpServletRequest request) {
        logger.debug("MonitorController.viewDetail()");
        /*
         * boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.monitor_resource_query); if (!flag) {
         * return TransformConstant.transform_jsp_noaccsess; }
         */
        JSONObject json = JSONObject.fromObject(data);
//        JSONObject objectdata = new JSONObject();
        if (json != null && !json.isEmpty()) {
            String type = json.getString("type");
            String uuid = json.getString("uuid");
        	// 查找云主机
            CloudHostVO cloudHost = cloudHostService.queryCloudHostByHostName(uuid);
            JSONObject objectdata = objectdata(cloudHost);
            objectdata.put("uuid", uuid);// 主机uuid
            
            if (MonitorConstant.server_flag.equals(type)) {
                JSONObject server = MonitorConstant.getObjectByUUID(uuid, MonitorConstant.serverMap);
                String status = MonitorConstant.judgeThresholdUseRule(server, MonitorConstant.server_flag);
                objectdata.put("type", type);
                objectdata.put("name", JSONLibUtil.getString(server, "name"));
                if (MonitorConstant.status_normal.equals(status)) {
                    objectdata.put("status", "正常");
                } else if (MonitorConstant.status_error.equals(status)) {
                    objectdata.put("status", "故障");
                } else if (MonitorConstant.status_warn.equals(status)) {
                    objectdata.put("status", "告警");
                } else if (MonitorConstant.status_stop.equals(status)) {
                    objectdata.put("status", "屏蔽");
                }
                objectdata.put("cpu_count", JSONLibUtil.getString(server, "cpu_count") + "核");
                objectdata.put("cpu_usage",
                        MonitorConstant.convertStringToDouble(JSONLibUtil.getString(server, "cpu_usage")) + "%");
                objectdata.put("memory_usage",
                        MonitorConstant.convertStringToDouble(JSONLibUtil.getString(server, "memory_usage")) + "%");
                objectdata.put("disk_usage",
                        MonitorConstant.convertStringToDouble(JSONLibUtil.getString(server, "disk_usage")) + "%");
                objectdata.put("memory",
                        CapacityUtil.toGBValue(new BigInteger(server.getJSONArray("memory").get(1) + ""), 0) + "G");
                objectdata
                        .put("disk",
                                CapacityUtil.toGBValue(new BigInteger(server.getJSONArray("disk_volume").get(1) + ""),
                                        0) + "G");
                request.setAttribute("objectdata", objectdata);
            } else if (MonitorConstant.host_flag.equals(type)) {
                synchronized (MonitorConstant.hostsMap) {
                    JSONObject host = MonitorConstant.getObjectByUUID(uuid, MonitorConstant.hostsMap);
                    String status = MonitorConstant.judgeThresholdUseRule(host, MonitorConstant.host_flag);
                    objectdata.put("type", type);
                    objectdata.put("name", JSONLibUtil.getString(host, "name"));
                    if (MonitorConstant.status_normal.equals(status)) {
                        objectdata.put("status", "正常");
                    } else if (MonitorConstant.status_error.equals(status)) {
                        objectdata.put("status", "故障");
                    } else if (MonitorConstant.status_warn.equals(status)) {
                        objectdata.put("status", "告警");
                    } else if (MonitorConstant.status_stop.equals(status)) {
                        objectdata.put("status", "屏蔽");
                    }
                    objectdata.put("cpu_count", JSONLibUtil.getString(host, "cpu_count") + "核");
                    objectdata.put("cpu_usage",
                            MonitorConstant.convertStringToDouble(JSONLibUtil.getString(host, "cpu_usage")) + "%");
                    objectdata.put("memory_usage",
                            MonitorConstant.convertStringToDouble(JSONLibUtil.getString(host, "memory_usage")) + "%");
                    objectdata.put("disk_usage",
                            MonitorConstant.convertStringToDouble(JSONLibUtil.getString(host, "disk_usage")) + "%");
                    objectdata.put("memory",
                            CapacityUtil.toGBValue(new BigInteger(host.getJSONArray("memory").get(1) + ""), 0) + "G");
                    objectdata.put("disk",
                            CapacityUtil.toGBValue(new BigInteger(host.getJSONArray("disk_volume").get(1) + ""), 0)
                                    + "G");
                    objectdata.put("port", host.get("port"));// 主机端口映射
                    objectdata.put("user", host.get("user"));// 所属用户标签
                    objectdata.put("group", host.get("group"));// 所属用户组标签
                    objectdata.put("display", host.get("display"));// 监控验证用户名
                    objectdata.put("authentication", host.get("authentication"));// 监控验证密码
/*                    objectdata.put("inbound_bandwidth",
                            CapacityUtil.toMBValue(new BigInteger(host.get("inbound_bandwidth") + ""), 0));// 入口带宽
                    objectdata.put("outbound_bandwidth",
                            CapacityUtil.toMBValue(new BigInteger(host.get("outbound_bandwidth") + ""), 0));// 入口带宽
                    objectdata.put("network_type", host.get("network_type"));// 网络地址类型
                    objectdata.put("disk_type", host.get("disk_type"));// 磁盘模式
*/
                    request.setAttribute("objectdata", objectdata);
                }
            }
        }
        return "/monitor/monitor_view_detail";
    }
    
    /**
     * 云主机信息转json格式
     * @param cloudHost 云主机bean
     * @return objectdata  json字符串
     */
    private static JSONObject objectdata(CloudHostVO cloudHost){
    	JSONObject objectdata = new JSONObject();
    	if(null != cloudHost){
    		objectdata.put("hostName", cloudHost.getHostName());//主机真实名称
            objectdata.put("realHostId", cloudHost.getRealHostId());//显示名称
            objectdata.put("account", cloudHost.getAccount());//spice用户名
            objectdata.put("password", cloudHost.getPassword());//密码
            objectdata.put("sysImageName", cloudHost.getSysImageName());//镜像名称
            objectdata.put("realHostId", cloudHost.getRealHostId());//真实主机id
            objectdata.put("innerIp", cloudHost.getInnerIp());//内网ip
            objectdata.put("innerPort", cloudHost.getInnerPort());//内网端口
            objectdata.put("outerIp", cloudHost.getOuterIp());//外网ip
            objectdata.put("outerPort", cloudHost.getOuterPort());//外网端口
    	}else{
    		objectdata.put("hostName", "");//主机真实名称
            objectdata.put("realHostId", "");//显示名称
            objectdata.put("account", "");//spice用户名
            objectdata.put("password", "");//密码
            objectdata.put("sysImageName", "");//镜像名称
            objectdata.put("realHostId", "");//真实主机id
            objectdata.put("innerIp", "");//内网ip
            objectdata.put("innerPort", "");//内网端口
            objectdata.put("outerIp", "");//外网ip
            objectdata.put("outerPort", "");//外网端口
    	}
        return objectdata;
    }
    
    /**
     * @Description:根据资源池取得云主机信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/resourcetohost", method = RequestMethod.GET)
    public String resourcetohostQuery(@RequestParam(value = "data", defaultValue = "") String data,
            HttpServletRequest request) {
        logger.debug("MonitorController.resourcetohostQuery()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.monitor_resource_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        // 参数
        JSONObject params = JSONObject.fromObject(data);
        if (!params.isNullObject() && !params.isEmpty()) {
            params.put("menuflag", "resourcetohost");
        }
        List<JSONObject> list = monitorService.hostQuery(params);
        JSONObject json = MonitorConstant.getStatusNum(list);
        request.setAttribute("totalnum", list.size());
        // 页面点击警告或故障按钮过滤数据
        if (!params.isEmpty() && list.size() > 0) {
            String status = params.get("status") == null ? null : params.getString("status");
            if (status != null && !status.isEmpty()) {
                Iterator<JSONObject> its = list.iterator();
                while (its.hasNext()) {
                    if (!status.equals(its.next().getString("status"))) {
                        its.remove();
                    }
                }
            }
        }
        request.setAttribute("hostList", list);
        request.setAttribute("params", params);
        request.setAttribute("statusdata", json);
        return "/monitor/monitor_resourcetohost";
    }
}
