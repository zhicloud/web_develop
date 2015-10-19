
package com.zhicloud.ms.service.impl;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.app.listener.CheckServerRoomsListener;
import com.zhicloud.ms.common.util.CapacityUtil;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.constant.MonitorConstant;
import com.zhicloud.ms.mapper.MonitorServerMapper;
import com.zhicloud.ms.service.MonitorService;
import com.zhicloud.ms.vo.MonitorServerVO;


/**
 * @ClassName: MonitorServiceImpl
 * @Description: 监控接口实现
 * @author 张本缘 于 2015年6月24日 下午4:09:03
 */
@Service("monitorService")
@Transactional(readOnly=false)
public class MonitorServiceImpl implements MonitorService {
    /* 日志 */
    public static final Logger logger = Logger.getLogger(MonitorServiceImpl.class);
    
    @Resource
    private SqlSession sqlSession;

    /**
     * @Description:查询区域监控信息
     * @param request
     * @param response
     * @throws
     */
    public List<JSONObject> areaQuery() {
        List<JSONObject> responseList = new ArrayList<JSONObject>();
        JSONArray rooms = (JSONArray) MonitorConstant.roomMap.get(CheckServerRoomsListener.regionDataID + "");
        int normal = 0, warning = 0, error = 0, stop = 0;
        int r_normal = 0, r_warning = 0, r_error = 0, r_stop = 0;
        if (rooms == null || rooms.size() == 0) {
            return null;
        }
        // 循环这些机房
        for (int i = 0; i < rooms.size(); i++) {
            JSONObject room = (JSONObject) rooms.get(i);
            JSONArray racks = MonitorConstant.rackMap.get(room.get("uuid"));
            if (racks == null)
                continue;
            // 循环机房机架
            for (int k = 0; k < racks.size(); k++) {
                JSONObject rack = (JSONObject) racks.get(i);
                JSONArray servers = MonitorConstant.serverMap.get(rack.get("uuid"));
                if (servers == null)
                    continue;
                // 循环服务器
                for (int n = 0; n < servers.size(); n++) {
                    JSONObject server = (JSONObject) servers.get(n);
                    if (MonitorConstant.status_normal.equals(MonitorConstant.judgeThresholdUseRule(server,
                            MonitorConstant.server_flag))) {
                        normal++;
                    } else if (MonitorConstant.status_warn.equals(MonitorConstant.judgeThresholdUseRule(server,
                            MonitorConstant.server_flag))) {
                        warning++;
                    } else if (MonitorConstant.status_error.equals(MonitorConstant.judgeThresholdUseRule(server,
                            MonitorConstant.server_flag))) {
                        error++;
                    } else if (MonitorConstant.status_stop.equals(MonitorConstant.judgeThresholdUseRule(server,
                            MonitorConstant.server_flag))) {
                        stop++;
                    }
                }
            }
            if (error > 0) {
                r_error++;
            } else if (warning > 0) {
                r_warning++;
            } else if (normal > 0) {
                r_normal++;
            } else if (stop > 0) {
                r_stop++;
            }
        }
        // 组装数据放入集合
        JSONObject json = new JSONObject();
        json.put("id", CheckServerRoomsListener.regionDataID);
        json.put("name", CheckServerRoomsListener.regionDataName);
        json.put("normal", r_normal);
        json.put("warning", r_warning);
        json.put("error", r_error);
        json.put("stop", r_stop);
        if (r_error > 0) {
            json.put("status", "error");
        } else if (r_warning > 0) {
            json.put("status", "warning");
        } else if (r_normal > 0) {
            json.put("status", "normal");
        } else if (r_stop > 0) {
            json.put("status", "error");
        } else {
            json.put("status", "normal");
        }
        responseList.add(json);
        return responseList;
    }
    
    /**
     * @Description:查询机房监控信息
     * @param request
     * @param response
     * @throws
     */
    public List<JSONObject> roomQuery(JSONObject params) {
        String areaid = "";
        String menuflag = "";
        if (!params.isEmpty()) {
            areaid = params.getString("areaid");
            menuflag = params.getString("menuflag");
        }
        // 机房信息
        JSONArray roomsArray = new JSONArray();
        if (!"room".equals(menuflag)) {
            roomsArray = contactArray(MonitorConstant.roomMap);
        } else {
            roomsArray = makeNewArray(MonitorConstant.roomMap.get(areaid));
        }
        // 参数
        List<JSONObject> responseList = new ArrayList<JSONObject>();
        if (roomsArray != null && roomsArray.size() > 0) {
            for (int j = 0; j < roomsArray.size(); j++) {
                JSONObject room = (JSONObject) roomsArray.get(j);
                JSONObject json = new JSONObject();
                json.put("areaid", JSONLibUtil.getString(room, "areaid"));
                json.put("areaname", MonitorConstant.nameMap.get(JSONLibUtil.getString(room, "areaid")));
                json.put("name", JSONLibUtil.getString(room, "name"));
                json.put("uuid", JSONLibUtil.getString(room, "uuid"));
                json.put("cpu_count", JSONLibUtil.getString(room, "cpu_count"));
                json.put("cpu_usage", MonitorConstant.convertStringToDouble(JSONLibUtil.getString(room, "cpu_usage"))
                        + "%");
                json.put("memory_usage",
                        MonitorConstant.convertStringToDouble(JSONLibUtil.getString(room, "memory_usage")) + "%");
                json.put("disk_usage", MonitorConstant.convertStringToDouble(JSONLibUtil.getString(room, "disk_usage"))
                        + "%");
                json.put("server_stop", JSONLibUtil.getLongArray(room, "server")[0]);
                json.put("server_warning", JSONLibUtil.getLongArray(room, "server")[1]);
                json.put("server_error", JSONLibUtil.getLongArray(room, "server")[2]);
                json.put("server_normal", JSONLibUtil.getLongArray(room, "server")[3]);
                // 获取机架
                JSONArray racks = MonitorConstant.rackMap.get(JSONLibUtil.getString(room, "uuid"));
                int normal = 0, warning = 0, error = 0, stop = 0;
                int r_normal = 0, r_warning = 0, r_error = 0, r_stop = 0;
                if (racks == null || racks.size() == 0)
                    continue;
                // 循环机房机架
                for (int k = 0; k < racks.size(); k++) {
                    JSONObject rack = (JSONObject) racks.get(k);
                    JSONArray servers = MonitorConstant.serverMap.get(rack.get("uuid"));
                    if (servers == null)
                        continue;
                    // 循环服务器
                    for (int n = 0; n < servers.size(); n++) {
                        JSONObject server = (JSONObject) servers.get(n);
                        if (MonitorConstant.status_normal.equals(MonitorConstant.judgeThresholdUseRule(server,
                                MonitorConstant.server_flag))) {
                            normal++;
                        } else if (MonitorConstant.status_warn.equals(MonitorConstant.judgeThresholdUseRule(server,
                                MonitorConstant.server_flag))) {
                            warning++;
                        } else if (MonitorConstant.status_error.equals(MonitorConstant.judgeThresholdUseRule(server,
                                MonitorConstant.server_flag))) {
                            error++;
                        } else if (MonitorConstant.status_stop.equals(MonitorConstant.judgeThresholdUseRule(server,
                                MonitorConstant.server_flag))) {
                            stop++;
                        }
                    }

                    if (error > 0) {
                        r_error++;
                    } else if (warning > 0) {
                        r_warning++;
                    } else if (normal > 0) {
                        r_normal++;
                    } else if (stop > 0) {
                        r_stop++;
                    }
                }
                json.put("racks", racks.size());
                if (r_error > 0) {
                    json.put("status", "error");
                } else if (r_warning > 0) {
                    json.put("status", "warning");
                } else if (r_normal > 0) {
                    json.put("status", "normal");
                } else if (r_stop > 0) {
                    json.put("status", "error");
                } else {
                    json.put("status", "normal");
                }
                responseList.add(json);
            }
        }
        return responseList;
    }
    
    /**
     * @Description:查询机架监控信息
     * @param request
     * @param response
     * @throws
     */
    public List<JSONObject> rackQuery(JSONObject params) {
        String roomid = "";
        String menuflag = "";
        if (!params.isEmpty()) {
            roomid = params.getString("roomid");
            menuflag = params.getString("menuflag");
        }
        // 机架信息
        JSONArray racksArray = new JSONArray();
        if (!"rack".equals(menuflag)) {
            racksArray = contactArray(MonitorConstant.rackMap);
        } else {
            racksArray = makeNewArray(MonitorConstant.rackMap.get(roomid));
        }
        List<JSONObject> responseList = new ArrayList<JSONObject>();
        if (racksArray != null && racksArray.size() > 0) {
            for (int j = 0; j < racksArray.size(); j++) {
                JSONObject rack = (JSONObject) racksArray.get(j);
                JSONObject json = new JSONObject();
                json.put("areaid", JSONLibUtil.getString(rack, "areaid"));
                json.put("roomid", JSONLibUtil.getString(rack, "roomid"));
                json.put("areaname", MonitorConstant.nameMap.get(JSONLibUtil.getString(rack, "areaid")));
                json.put("roomname", MonitorConstant.nameMap.get(JSONLibUtil.getString(rack, "roomid")));
                json.put("name", JSONLibUtil.getString(rack, "name"));
                json.put("uuid", JSONLibUtil.getString(rack, "uuid"));
                json.put("cpu_count", JSONLibUtil.getString(rack, "cpu_count"));
                json.put("cpu_usage", MonitorConstant.convertStringToDouble(JSONLibUtil.getString(rack, "cpu_usage"))
                        + "%");
                json.put("memory_usage",
                        MonitorConstant.convertStringToDouble(JSONLibUtil.getString(rack, "memory_usage")) + "%");
                json.put("disk_usage", MonitorConstant.convertStringToDouble(JSONLibUtil.getString(rack, "disk_usage"))
                        + "%");
                json.put("server_stop", JSONLibUtil.getLongArray(rack, "server")[0]);
                json.put("server_warning", JSONLibUtil.getLongArray(rack, "server")[1]);
                json.put("server_error", JSONLibUtil.getLongArray(rack, "server")[2]);
                json.put("server_normal", JSONLibUtil.getLongArray(rack, "server")[3]);
                // 获取服务器
                JSONArray servers = MonitorConstant.serverMap.get(JSONLibUtil.getString(rack, "uuid"));
                int normal = 0, warning = 0, error = 0, stop = 0;
                if (servers == null || servers.size() == 0)
                    continue;
                // 循环服务器
                for (int n = 0; n < servers.size(); n++) {
                    JSONObject server = (JSONObject) servers.get(n);
                    if (MonitorConstant.status_normal.equals(MonitorConstant.judgeThresholdUseRule(server,
                            MonitorConstant.server_flag))) {
                        normal++;
                    } else if (MonitorConstant.status_warn.equals(MonitorConstant.judgeThresholdUseRule(server,
                            MonitorConstant.server_flag))) {
                        warning++;
                    } else if (MonitorConstant.status_error.equals(MonitorConstant.judgeThresholdUseRule(server,
                            MonitorConstant.server_flag))) {
                        error++;
                    } else if (MonitorConstant.status_stop.equals(MonitorConstant.judgeThresholdUseRule(server,
                            MonitorConstant.server_flag))) {
                        stop++;
                    }
                }
                json.put("servers", servers.size());
                if (error > 0) {
                    json.put("status", "error");
                } else if (warning > 0) {
                    json.put("status", "warning");
                } else if (normal > 0) {
                    json.put("status", "normal");
                } else if (stop > 0) {
                    json.put("status", "error");
                } else {
                    json.put("status", "normal");
                }
                responseList.add(json);
            }
        }
        return responseList;
    }
    
    /**
     * @Description:查询服务器监控信息
     * @param request
     * @param response
     * @throws
     */
    public List<JSONObject> serverQuery(JSONObject params) {
        String rackid = "";
        String menuflag = "";
        if (!params.isEmpty()) {
            rackid = params.getString("rackid");
            menuflag = params.getString("menuflag");
        }
        // 服务器信息
        JSONArray serversArray = new JSONArray();
        if (!"server".equals(menuflag)) {
            serversArray = contactArray(MonitorConstant.serverMap);
        } else {
            serversArray = makeNewArray(MonitorConstant.serverMap.get(rackid));
        }
        List<JSONObject> responseList = new ArrayList<JSONObject>();
        if (serversArray != null && serversArray.size() > 0) {
            for (int j = 0; j < serversArray.size(); j++) {
                JSONObject server = (JSONObject) serversArray.get(j);
                // 启动服务器监控
                JSONObject json = new JSONObject();
                json.put("areaid", JSONLibUtil.getString(server, "areaid"));
                json.put("roomid", JSONLibUtil.getString(server, "roomid"));
                json.put("rackid", JSONLibUtil.getString(server, "rackid"));
                json.put("areaname", MonitorConstant.nameMap.get(JSONLibUtil.getString(server, "areaid")));
                json.put("roomname", MonitorConstant.nameMap.get(JSONLibUtil.getString(server, "roomid")));
                json.put("rackname", MonitorConstant.nameMap.get(JSONLibUtil.getString(server, "rackid")));
                json.put("name", JSONLibUtil.getString(server, "name"));
                json.put("uuid", JSONLibUtil.getString(server, "uuid"));
                json.put("cpu_count", JSONLibUtil.getString(server, "cpu_count"));
                json.put("cpu_usage", MonitorConstant.convertStringToDouble(JSONLibUtil.getString(server, "cpu_usage"))
                        + "%");
                json.put("memory_usage",
                        MonitorConstant.convertStringToDouble(JSONLibUtil.getString(server, "memory_usage")) + "%");
                json.put("disk_usage",
                        MonitorConstant.convertStringToDouble(JSONLibUtil.getString(server, "disk_usage")) + "%");
                json.put("ip", JSONLibUtil.getString(server, "ip"));
                json.put("status", MonitorConstant.judgeThresholdUseRule(server, MonitorConstant.server_flag));
                responseList.add(json);
            }
        }
        return responseList;
    }
    
    /**
     * @Description:查询云主机监控信息
     * @param request
     * @param response
     * @throws
     */
    public List<JSONObject> hostQuery(JSONObject params) {
        // 参数处理
        String menuflag = "";
        String serverid = "";
        if (!params.isEmpty()) {
            menuflag = params.getString("menuflag");
            serverid = params.getString("serverid");
        }
        // 云主机信息
        JSONArray hostsArray = new JSONArray();
        if ("resourcetohost".equals(menuflag)) {
            // 资源池ID
            String poolid = params.getString("poolid");
            Iterator<String> its = MonitorConstant.hostsMap.keySet().iterator();
            // 由于放入数据的时候key键是存的uuid_name_areaname_areaid的格式，
            // 所以传入的poolid只能进行模糊匹配
            while (its.hasNext()) {
                String key = its.next();
                if (key.indexOf(poolid) > -1) {
                    hostsArray = MonitorConstant.hostsMap.get(key);
                }
            }

        } else {
            Iterator<String> its = MonitorConstant.hostsMap.keySet().iterator();
            // 从内存中读取存在的数据一次放入array中
            while (its.hasNext()) {
                String key = its.next();
                JSONArray array = MonitorConstant.hostsMap.get(key);
                if (array != null && array.size() > 0) {
                    for (int i = 0; i < array.size(); i++) {
                        hostsArray.add(array.get(i));
                    }
                }
            }
        }
        if (hostsArray != null && !hostsArray.isEmpty()) {
            for (int i = hostsArray.size() - 1; i >= 0; i--) {
                JSONObject host = hostsArray.getJSONObject(i);
                String serverIP = JSONLibUtil.getStringArray(host, "ip")[0];
                // 服务器
                if (serverid != null && !"".equals(serverid) && !"null".equals(serverid)
                        && !serverid.equals(MonitorConstant.serversIP.get(serverIP))) {
                    hostsArray.remove(i);
                }
            }
        }

        List<JSONObject> responseList = new ArrayList<JSONObject>();
        if (hostsArray != null && hostsArray.size() > 0) {
            for (int j = 0; j < hostsArray.size(); j++) {
                JSONObject host = (JSONObject) hostsArray.get(j);
                JSONObject json = new JSONObject();
                json.put("commonip", JSONLibUtil.getStringArray(host, "ip")[1]);
                json.put("serverip", JSONLibUtil.getStringArray(host, "ip")[0]);
                json.put("region", JSONLibUtil.getString(host, "areaid"));
                json.put("regionname", JSONLibUtil.getString(host, "area_name"));
                json.put("hostName", JSONLibUtil.getString(host, "name"));
                json.put("realHostId", JSONLibUtil.getString(host, "uuid"));
                json.put("cpuCore", JSONLibUtil.getString(host, "cpu_count") + "核");
                json.put("cpu_usage", MonitorConstant.convertStringToDouble(JSONLibUtil.getString(host, "cpu_usage"))
                        + "%");
                json.put("memory", CapacityUtil.toGBValue(new BigInteger(host.getJSONArray("memory").get(1) + ""), 0)
                        + "G");
                json.put("memory_usage",
                        MonitorConstant.convertStringToDouble(JSONLibUtil.getString(host, "memory_usage")) + "%");
                json.put("dataDisk",
                        CapacityUtil.toGBValue(new BigInteger(host.getJSONArray("disk_volume").get(1) + ""), 0) + "G");
                json.put("disk_usage", MonitorConstant.convertStringToDouble(JSONLibUtil.getString(host, "disk_usage"))
                        + "%");
                json.put("status", MonitorConstant.judgeThresholdUseRule(host, MonitorConstant.host_flag));
                responseList.add(json);
            }
        }
        return responseList;
    }

    
    /**
     * @Description:机房监控页面跳转
     * @param request
     * @param response
     * @return String
     * @throws UnsupportedEncodingException
     */
    public String roomPage(HttpServletRequest request, HttpServletResponse response) {
        /*logger.debug("MonitorServiceImpl.roomPage()");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_room) == false) {
            return "/public/have_not_access.jsp";
        }
        request.setAttribute("menuflag", request.getParameter("menuflag"));
        request.setAttribute("areaid", request.getParameter("areaid"));
        request.setAttribute("areaname", MonitorConstant.nameMap.get(request.getParameter("areaid")));
        return "/security/operator/monitor_room_manage.jsp";*/
        return "";
    }

    /**
     * @Description:机架监控页面跳转
     * @param request
     * @param response
     * @return String
     * @throws UnsupportedEncodingException
     */
    public String rackPage(HttpServletRequest request, HttpServletResponse response) {
        /*logger.debug("MonitorServiceImpl.rackPage()");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_rack) == false) {
            return "/public/have_not_access.jsp";
        }
        request.setAttribute("menuflag", request.getParameter("menuflag"));
        request.setAttribute("areaid", request.getParameter("areaid"));
        request.setAttribute("areaname", MonitorConstant.nameMap.get(request.getParameter("areaid")));
        request.setAttribute("roomid", request.getParameter("roomid"));
        request.setAttribute("roomname", MonitorConstant.nameMap.get(request.getParameter("roomid")));
        return "/security/operator/monitor_rack_manage.jsp";*/
        return "";
    }

    /**
     * @Description:服务器监控页面跳转
     * @param request
     * @param response
     * @return String
     * @throws UnsupportedEncodingException
     */
    public String serverPage(HttpServletRequest request, HttpServletResponse response) {
        /*logger.debug("MonitorServiceImpl.serverPage()");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_server) == false) {
            return "/public/have_not_access.jsp";
        }
        request.setAttribute("menuflag", request.getParameter("menuflag"));
        request.setAttribute("areaid", request.getParameter("areaid"));
        request.setAttribute("areaname", MonitorConstant.nameMap.get(request.getParameter("areaid")));
        request.setAttribute("roomid", request.getParameter("roomid"));
        request.setAttribute("roomname", MonitorConstant.nameMap.get(request.getParameter("roomid")));
        request.setAttribute("rackid", request.getParameter("rackid"));
        request.setAttribute("rackname", MonitorConstant.nameMap.get(request.getParameter("rackid")));
        return "/security/operator/monitor_server_manage.jsp";*/
        return "";
    }

    /**
     * @Description:云主机监控页面跳转
     * @param request
     * @param response
     * @return String
     */
    public String cloudHostPage(HttpServletRequest request, HttpServletResponse response) {
        /*logger.debug("MonitorServiceImpl.cloudHostPage()");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_cloudhost) == false) {
            return "/public/have_not_access.jsp";
        }
        request.setAttribute("menuflag", request.getParameter("menuflag"));
        request.setAttribute("areaid", request.getParameter("areaid"));
        request.setAttribute("areaname", MonitorConstant.nameMap.get(request.getParameter("areaid")));
        request.setAttribute("roomid", request.getParameter("roomid"));
        request.setAttribute("roomname", MonitorConstant.nameMap.get(request.getParameter("roomid")));
        request.setAttribute("rackid", request.getParameter("rackid"));
        request.setAttribute("rackname", MonitorConstant.nameMap.get(request.getParameter("rackid")));
        request.setAttribute("serverid", request.getParameter("serverid"));
        request.setAttribute("servername", MonitorConstant.nameMap.get(request.getParameter("serverid")));
                
        return "/security/operator/monitor_cloudhost_manage.jsp";*/
        return "";
    }

    /**
     * @Description:资源管理监控页面跳转
     * @param request
     * @param response
     * @return String
     */
    public String hostResourcePage(HttpServletRequest request, HttpServletResponse response) {
        /*logger.debug("MonitorServiceImpl.hostResourcePage()");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_resource) == false) {
            return "/public/have_not_access.jsp";
        }
        return "/security/operator/monitor_resource_manage.jsp";*/
        return "";
    }

    /**
     * @Description:资源管理系统监控页面转
     * @param request
     * @param response
     * @return String
     */
    public String systemMonitorPage(HttpServletRequest request, HttpServletResponse response) {
        /*logger.debug("MonitorServiceImpl.hostResourcePage()");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_resource) == false) {
            return "/public/have_not_access.jsp";
        }
        return "/security/operator/monitor_total_manage.jsp";*/
        return "";
    }



    /**
     * @Description:插入服务器临时数据到临时表
     * @param map
     */
    public void saveMonitorTempData(Map<String, Object> map) {
        MonitorServerMapper servermonitor = this.sqlSession.getMapper(MonitorServerMapper.class);
        map.put("id", StringUtil.generateUUID());
        try {
            servermonitor.addMonitorTemp(map);
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("插入临时数据到临时表报错:" + e.getMessage());
        }
    }

    /**
     * @Description:保存服务器监控数据，清空临时表
     * @throws
     */
    public void saveMonitorData(String monitor_type, String server_type) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        MonitorServerMapper servermonitor = this.sqlSession.getMapper(MonitorServerMapper.class);
        try {
            map.put("monitor_type", monitor_type);
            map.put("server_type", server_type);
            List<MonitorServerVO> lists = servermonitor.queryAverage(map);
            if (lists.size() > 0) {
                String currenttime = StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
                for (MonitorServerVO monitor : lists) {
                    map.put("id", StringUtil.generateUUID());
                    String serverid = monitor.getServerid();
                    // 如果是云主机类型,serverid为hostid
                    if (MonitorConstant.host_flag.equals(server_type)) {
                        String hostid = monitor.getServerid();
                        // 根据host的uuid,从内存中读取host对象
                        JSONObject host = MonitorConstant.getObjectByUUID(hostid, MonitorConstant.hostsMap);
                        // 获取服务器IP
                        String serverip = JSONLibUtil.getStringArray(host, "ip")[0];
                        serverid = MonitorConstant.serversIP.get(serverip);
                        map.put("serverid", hostid);
                    } else {
                        map.put("serverid", serverid);
                    }
                    String rackid = MonitorConstant.getParentID(serverid, MonitorConstant.serverMap);
                    String roomid = MonitorConstant.getParentID(rackid, MonitorConstant.rackMap);
                    String areaid = MonitorConstant.getParentID(roomid, MonitorConstant.roomMap);
                    map.put("areaid", areaid);
                    map.put("roomid", roomid);
                    map.put("rackid", rackid);
                    map.put("servername", MonitorConstant.nameMap.get(serverid));
                    map.put("cpu_usage", monitor.getCpu_usage());
                    map.put("memory_usage", monitor.getMemory_usage());
                    map.put("disk_usage", monitor.getDisk_usage());
                    map.put("insert_date", currenttime);
                    servermonitor.addMonitor(map);
                }
                // 保存完正式数据以后，删除临时数据
                servermonitor.deleteMonitorTemp(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("插入监控正式数据报错:" + e.getMessage());
        }
    }

    /**
     * @Description:根据日期返回监控数据
     * @param insert_date
     * @return List<MonitorServerVO>
     */
    public List<MonitorServerVO> getDataByDay(Map<String, Object> map) {
        MonitorServerMapper servermonitor = this.sqlSession.getMapper(MonitorServerMapper.class);
        return servermonitor.getDataByDay(map);
    }

    /**
     * @Description:将map集合里面的多个JSONArray合并成一个
     * @param map
     * @return JSONArray
     */
    public JSONArray contactArray(Map<String, JSONArray> map) {
        JSONArray returnArray = new JSONArray();
        if (map != null && !map.isEmpty()) {
            Iterator<String> its = map.keySet().iterator();
            while (its.hasNext()) {
                String keyid = its.next();
                JSONArray array = map.get(keyid);
                for (Object object : array) {
                    returnArray.add(object);
                }
            }
        }
        return returnArray;
    }

    /**
     * @Description:根据内存数据构造新的Array
     * @param array
     * @return JSONArray
     */
    public JSONArray makeNewArray(JSONArray array) {
        JSONArray returnArray = new JSONArray();
        if (array != null && !array.isEmpty()) {
            for (int i = 0; i < array.size(); i++) {
                returnArray.add(array.get(i));
            }
        }
        return returnArray;
    }

    /**
     * @Description:资源管理查询资源池
     * @param request
     * @param response
     * @throws
     */
    public JSONArray resourcePoolQuery(String areaid) {
        /*try {
            logger.debug("MonitorServiceImpl.resourcePoolQuery()");
            Iterator<String> its = MonitorConstant.hostsMap.keySet().iterator();
            String poolid = "";
            JSONArray returnarray = new JSONArray();
             ==========测试用代码start========== 
            
             * JSONObject test = new JSONObject(); test.put("poolname", "资源池名称"); test.put("areaname", "区域名称");
             * test.put("normal", 10); test.put("warn", 5); test.put("error", 8); returnarray.add(test);
             
             ==========测试用代码end========== 
            while (its.hasNext()) {
                poolid = its.next();
                JSONArray hosts = MonitorConstant.hostsMap.get(poolid);
                JSONObject json = new JSONObject();
                Integer normal = 0, warn = 0, error = 0;
                // 循环主机，根据主机自身信息和阀值计算当前状态
                if (hosts != null && hosts.size() > 0) {
                    for (int i = 0; i < hosts.size(); i++) {
                        JSONObject host = hosts.getJSONObject(i);
                        String status = MonitorConstant.judgeThresholdUseRule(host, "host");
                        if (MonitorConstant.status_normal.equals(status)) {
                            normal++;
                        } else if (MonitorConstant.status_warn.equals(status)) {
                            warn++;
                        } else {
                            error++;
                        }
                    }
                }
                json.put("poolid", poolid.split("-")[0]);
                json.put("poolname", poolid.split("-")[1]);
                json.put("areaname", poolid.split("-")[2]);
                json.put("normal", normal);
                json.put("warn", warn);
                json.put("error", error);
                // 全部
                if ("all".equals(areaid)) {
                    returnarray.add(json);
                }
                // 广州
                if (areaid_gz.equals(areaid) && areaid_gz.equals(poolid.split("-")[3])) {
                    returnarray.add(json);
                }
                // 成都
                if (areaid_cd.equals(areaid) && areaid_cd.equals(poolid.split("-")[3])) {
                    returnarray.add(json);
                }
                // 香港
                if (areaid_xg.equals(areaid) && areaid_xg.equals(poolid.split("-")[3])) {
                    returnarray.add(json);
                }
            }
            return returnarray;
        } catch (Exception e) {
            logger.error("MonitorServiceImpl.resourcePoolQuery()", e);
            throw new AppException("查询失败");
        }*/
        return null;
    }

    /**
     * @Description:资源管理图表层跳转列表层
     * @param request
     * @param response
     * @return String
     */
    public String resourceToHostPage(HttpServletRequest request, HttpServletResponse response) {
/*        logger.debug("MonitorServiceImpl.resourceToHostPage()");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_resource) == false) {
            return "/public/have_not_access.jsp";
        }
        request.setAttribute("poolid", request.getParameter("poolid"));
        request.setAttribute("menuflag", request.getParameter("menuflag"));
        request.setAttribute("poolname", request.getParameter("poolname"));*/
        return "/security/operator/monitor_resourcetohost.jsp";
    }
    
    /**
     * @Description:保存
     * @param map
     * @throws
     */
    public void saveShieldData(Map<String, Object> map) {
        MonitorServerMapper servermonitor = this.sqlSession.getMapper(MonitorServerMapper.class);
        MonitorServerVO monitor = servermonitor.queryShieldByID(map.get("id") + "");
        if (monitor == null) {
            servermonitor.addShield(map);
        } else {
            servermonitor.updateShield(map);
        }
        MonitorConstant.shieldmap.put(map.get("id") + "", map.get("shield") + "");
    }
    
    /**
     * @Description:初始化所有屏蔽的数据到内存常量
     * @throws
     */
    public void initMonitorShield() {
        MonitorServerMapper servermonitor = this.sqlSession.getMapper(MonitorServerMapper.class);
        List<MonitorServerVO> list = servermonitor.queryShield();
        if (list != null && list.size() > 0) {
            for (MonitorServerVO monitor : list) {
                MonitorConstant.shieldmap.put(monitor.getId(), monitor.getShield());
            }
        }
    }
}
