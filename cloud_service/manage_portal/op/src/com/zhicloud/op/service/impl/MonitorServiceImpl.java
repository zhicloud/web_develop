
package com.zhicloud.op.service.impl;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.RegionHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.helper.RegionHelper.RegionData;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoExt;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoPool;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoPoolManager;
import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.MonitorServerMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.MonitorService;
import com.zhicloud.op.service.constant.MonitorConstant;
import com.zhicloud.op.vo.MonitorServerVO;

/**
 * @ClassName: MonitorServiceImpl
 * @Description: 监控接口实现
 * @author 张本缘 于 2015年6月24日 下午4:09:03
 */
@Transactional(readOnly = false)
public class MonitorServiceImpl extends BeanDirectCallableDefaultImpl implements MonitorService {
    /* 日志 */
    public static final Logger logger = Logger.getLogger(MonitorServiceImpl.class);
    /* 区域标示码 */
    private static final String areaid_gz = "1"; // 广州
    private static final String areaid_cd = "2"; // 成都
    private static final String areaid_xg = "4"; // 香港

    private SqlSession sqlSession;

    /**
     * @Description:sqlSession注入
     * @param sqlSession
     */
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    /**
     * Description:区域监控页面跳转
     * @param request
     * @param response
     * @return String
     */
    @Callable
    public String areaPage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("MonitorServiceImpl.areaPage()");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_area) == false) {
            return "/public/have_not_access.jsp";
        }
        request.setAttribute("menuflag", request.getParameter("menuflag"));
        return "/security/operator/monitor_area_manage.jsp";
    }

    /**
     * @Description:机房监控页面跳转
     * @param request
     * @param response
     * @return String
     * @throws UnsupportedEncodingException
     */
    @Callable
    public String roomPage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("MonitorServiceImpl.roomPage()");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_room) == false) {
            return "/public/have_not_access.jsp";
        }
        request.setAttribute("menuflag", request.getParameter("menuflag"));
        request.setAttribute("areaid", request.getParameter("areaid"));
        request.setAttribute("areaname", MonitorConstant.nameMap.get(request.getParameter("areaid")));
        return "/security/operator/monitor_room_manage.jsp";
    }

    /**
     * @Description:机架监控页面跳转
     * @param request
     * @param response
     * @return String
     * @throws UnsupportedEncodingException
     */
    @Callable
    public String rackPage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("MonitorServiceImpl.rackPage()");
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
        return "/security/operator/monitor_rack_manage.jsp";
    }

    /**
     * @Description:服务器监控页面跳转
     * @param request
     * @param response
     * @return String
     * @throws UnsupportedEncodingException
     */
    @Callable
    public String serverPage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("MonitorServiceImpl.serverPage()");
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
        return "/security/operator/monitor_server_manage.jsp";
    }

    /**
     * @Description:云主机监控页面跳转
     * @param request
     * @param response
     * @return String
     */
    @Callable
    public String cloudHostPage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("MonitorServiceImpl.cloudHostPage()");
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
                
        return "/security/operator/monitor_cloudhost_manage.jsp";
    }

    /**
     * @Description:资源管理监控页面跳转
     * @param request
     * @param response
     * @return String
     */
    @Callable
    public String hostResourcePage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("MonitorServiceImpl.hostResourcePage()");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_resource) == false) {
            return "/public/have_not_access.jsp";
        }
        return "/security/operator/monitor_resource_manage.jsp";
    }

    /**
     * @Description:资源管理系统监控页面转
     * @param request
     * @param response
     * @return String
     */
    @Callable
    public String systemMonitorPage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("MonitorServiceImpl.hostResourcePage()");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_resource) == false) {
            return "/public/have_not_access.jsp";
        }
        return "/security/operator/monitor_total_manage.jsp";
    }

    /**
     * @Description:查询区域监控信息
     * @param request
     * @param response
     * @throws
     */
    @Callable
    public void areaQuery(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("MonitorServiceImpl.areaQuery()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            // 权限判断
            LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
            if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_area) == false) {
                throw new AppException("您没有权限进行此操作");
            }
            Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
            Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
            // 区域信息
            RegionData[] regionDatas = RegionHelper.singleton.getAllResions();
            List<JSONObject> responseList = new ArrayList<JSONObject>();
            int r_normal = 0, r_warning = 0, r_error = 0, r_stop = 0;
            for (int j = page * rows; j < ((page + 1) * rows > regionDatas.length ? regionDatas.length : (page + 1)
                    * rows); j++) {
                RegionData regionData = regionDatas[j];
                JSONArray rooms = (JSONArray) MonitorConstant.roomMap.get(regionData.getId() + "");
                int normal = 0, warning = 0, error = 0, stop = 0;
                if (rooms == null || rooms.size() == 0)
                    continue;
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
                            } else if (MonitorConstant.status_error.equals(MonitorConstant.judgeThresholdUseRule(
                                    server, MonitorConstant.server_flag))) {
                                error++;
                            } else if (MonitorConstant.status_stop.equals(MonitorConstant.judgeThresholdUseRule(server,
                                    MonitorConstant.server_flag))) {
                                stop++;
                            }
                        }
                    }
                }
                // 组装数据放入集合
                JSONObject json = new JSONObject();
                json.put("id", regionData.getId());
                json.put("name", regionData.getName());
                if (error > 0) {
                    json.put("status", "error");
                    r_error++;
                } else if (warning > 0) {
                    json.put("status", "warning");
                    r_warning++;
                } else if (normal > 0) {
                    json.put("status", "normal");
                    r_normal++;
                }/* else if (stop > 0) {
                    json.put("status", "stop");
                    r_stop++;
                }*/ else {
                    json.put("status", "normal");
                    r_normal++;
                }
                responseList.add(json);
            }
            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("normal", r_normal + "");
            map.put("warning", r_warning + "");
            map.put("error", r_error + "");
            map.put("stop", r_stop + "");
            // 输出json数据
            ServiceHelper.writeMapResultAsJsonTo(response.getOutputStream(), responseList.size(), responseList, map);
        } catch (Exception e) {
            logger.error("MonitorServiceImpl.areaQuery()", e);
            throw new AppException("查询失败");
        }
    }

    /**
     * @Description:查询机房监控信息
     * @param request
     * @param response
     * @throws
     */
    @Callable
    public void roomQuery(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("MonitorServiceImpl.roomQuery()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            // 权限判断
            LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
            if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_room) == false) {
                throw new AppException("您没有权限进行此操作");
            }
            String areaid = StringUtil.trim(request.getParameter("areaid"));
            String menuflag = StringUtil.trim(request.getParameter("menuflag"));
            Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
            Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
            // 机房信息
            JSONArray roomsArray = new JSONArray();
            if ("room".equals(menuflag)) {
                roomsArray = contactArray(MonitorConstant.roomMap);
            } else {
                roomsArray = makeNewArray(MonitorConstant.roomMap.get(areaid));
            }
            // 参数
            String region = StringUtil.trim(request.getParameter("region"));
            String room_name = StringUtil.trim(request.getParameter("room_name"));
            if (!roomsArray.isEmpty()) {
                for (int i = roomsArray.size() - 1; i >= 0; i--) {
                    JSONObject room = roomsArray.getJSONObject(i);
                    String regionid = MonitorConstant.getParentID(JSONLibUtil.getString(room, "uuid"),
                            MonitorConstant.roomMap);
                    String roomname = MonitorConstant.nameMap.get(JSONLibUtil.getString(room, "uuid"));
                    // 区域
                    if (region != null && !"".equals(region) && !region.equals(regionid)) {
                        roomsArray.remove(i);
                        continue;
                    }
                    // 机房名
                    if (room_name != null && !"".equals(room_name) && roomname.indexOf(room_name) == -1) {
                        roomsArray.remove(i);
                        continue;
                    }
                }
            }
            List<JSONObject> responseList = new ArrayList<JSONObject>();
            int r_normal = 0, r_warning = 0, r_error = 0, r_stop = 0;
            if (roomsArray != null && roomsArray.size() > 0) {
                for (int j = page * rows; j < ((page + 1) * rows > roomsArray.size() ? roomsArray.size() : (page + 1)
                        * rows); j++) {
                    JSONObject room = (JSONObject) roomsArray.get(j);
                    JSONObject json = new JSONObject();
                    json.put("areaid", JSONLibUtil.getString(room, "areaid"));
                    json.put("areaname", MonitorConstant.nameMap.get(JSONLibUtil.getString(room, "areaid")));
                    json.put("name", JSONLibUtil.getString(room, "name"));
                    json.put("uuid", JSONLibUtil.getString(room, "uuid"));
                    json.put("cpu_count", JSONLibUtil.getString(room, "cpu_count"));
                    json.put("cpu_usage",
                            MonitorConstant.convertStringToDouble(JSONLibUtil.getString(room, "cpu_usage")) + "%");
                    json.put("memory_usage",
                            MonitorConstant.convertStringToDouble(JSONLibUtil.getString(room, "memory_usage")) + "%");
                    json.put("disk_usage",
                            MonitorConstant.convertStringToDouble(JSONLibUtil.getString(room, "disk_usage")) + "%");
                    // 获取机架
                    JSONArray racks = MonitorConstant.rackMap.get(JSONLibUtil.getString(room, "uuid"));
                    int normal = 0, warning = 0, error = 0, stop = 0;
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
                            } else if (MonitorConstant.status_error.equals(MonitorConstant.judgeThresholdUseRule(
                                    server, MonitorConstant.server_flag))) {
                                error++;
                            } else if (MonitorConstant.status_stop.equals(MonitorConstant.judgeThresholdUseRule(server,
                                    MonitorConstant.server_flag))) {
                                stop++;
                            }
                        }
                    }
                    json.put("racks", racks.size());
                    if (error > 0) {
                        json.put("status", "error");
                        r_error++;
                    } else if (warning > 0) {
                        json.put("status", "warning");
                        r_warning++;
                    } else if (normal > 0) {
                        json.put("status", "normal");
                        r_normal++;
                    }/* else if (stop > 0) {
                        json.put("status", "error");
                        r_stop++;
                    }*/ else {
                        r_normal++;
                        json.put("status", "normal");
                    }
                    responseList.add(json);
                }
            }
            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("normal", r_normal + "");
            map.put("warning", r_warning + "");
            map.put("error", r_error + "");
            map.put("stop", r_stop + "");
            // 输出json数据
            ServiceHelper.writeMapResultAsJsonTo(response.getOutputStream(), responseList.size(), responseList, map);
        } catch (Exception e) {
            logger.error("MonitorServiceImpl.roomQuery()", e);
            throw new AppException("查询失败");
        }
    }

    /**
     * @Description:查询机架监控信息
     * @param request
     * @param response
     * @throws
     */
    @Callable
    public void rackQuery(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("MonitorServiceImpl.rackQuery()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            // 权限判断
            LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
            if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_rack) == false) {
                throw new AppException("您没有权限进行此操作");
            }
            String roomid = StringUtil.trim(request.getParameter("roomid"));
            String menuflag = StringUtil.trim(request.getParameter("menuflag"));
            Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
            Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);

            // 机房信息
            JSONArray racksArray = new JSONArray();
            if ("rack".equals(menuflag)) {
                racksArray = contactArray(MonitorConstant.rackMap);
            } else {
                racksArray = makeNewArray(MonitorConstant.rackMap.get(roomid));
            }
            // 参数
            String region = StringUtil.trim(request.getParameter("region"));
            String room_name = StringUtil.trim(request.getParameter("room_name"));
            String rack_name = StringUtil.trim(request.getParameter("rack_name"));
            if (!racksArray.isEmpty()) {
                for (int i = racksArray.size() - 1; i >= 0; i--) {
                    JSONObject rack = racksArray.getJSONObject(i);
                    String regionid = JSONLibUtil.getString(rack, "areaid");
                    String roomname = MonitorConstant.nameMap.get(JSONLibUtil.getString(rack, "roomid"));
                    String rackname = MonitorConstant.nameMap.get(JSONLibUtil.getString(rack, "uuid"));
                    // 区域
                    if (region != null && !"".equals(region) && !region.equals(regionid)) {
                        racksArray.remove(i);
                        continue;
                    }
                    // 机房名
                    if (room_name != null && !"".equals(room_name) && roomname.indexOf(room_name) == -1) {
                        racksArray.remove(i);
                        continue;
                    }
                    // 机架名
                    if (rack_name != null && !"".equals(rack_name) && rackname.indexOf(rack_name) == -1) {
                        racksArray.remove(i);
                        continue;
                    }
                }
            }
            List<JSONObject> responseList = new ArrayList<JSONObject>();
            int r_normal = 0, r_warning = 0, r_error = 0, r_stop = 0;
            if (racksArray != null && racksArray.size() > 0) {
                for (int j = page * rows; j < ((page + 1) * rows > racksArray.size() ? racksArray.size() : (page + 1)
                        * rows); j++) {
                    JSONObject rack = (JSONObject) racksArray.get(j);
                    JSONObject json = new JSONObject();
                    json.put("areaid", JSONLibUtil.getString(rack, "areaid"));
                    json.put("roomid", JSONLibUtil.getString(rack, "roomid"));
                    json.put("areaname", MonitorConstant.nameMap.get(JSONLibUtil.getString(rack, "areaid")));
                    json.put("roomname", MonitorConstant.nameMap.get(JSONLibUtil.getString(rack, "roomid")));
                    json.put("name", JSONLibUtil.getString(rack, "name"));
                    json.put("uuid", JSONLibUtil.getString(rack, "uuid"));
                    json.put("cpu_count", JSONLibUtil.getString(rack, "cpu_count"));
                    json.put("cpu_usage",
                            MonitorConstant.convertStringToDouble(JSONLibUtil.getString(rack, "cpu_usage")) + "%");
                    json.put("memory_usage",
                            MonitorConstant.convertStringToDouble(JSONLibUtil.getString(rack, "memory_usage")) + "%");
                    json.put("disk_usage",
                            MonitorConstant.convertStringToDouble(JSONLibUtil.getString(rack, "disk_usage")) + "%");
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
                        r_error++;
                    } else if (warning > 0) {
                        json.put("status", "warning");
                        r_warning++;
                    } else if (normal > 0) {
                        json.put("status", "normal");
                        r_normal++;
                    } else if (stop > 0) {
                        json.put("status", "error");
                        r_stop++;
                    } else {
                        json.put("status", "normal");
                        r_normal++;
                    }
                    responseList.add(json);
                }
            }
            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("normal", r_normal + "");
            map.put("warning", r_warning + "");
            map.put("error", r_error + "");
            map.put("stop", r_stop + "");
            // 输出json数据
            ServiceHelper.writeMapResultAsJsonTo(response.getOutputStream(), responseList.size(), responseList, map);
        } catch (Exception e) {
            logger.error("MonitorServiceImpl.rackQuery()", e);
            throw new AppException("查询失败");
        }
    }

    /**
     * @Description:查询服务器监控信息
     * @param request
     * @param response
     * @throws
     */
    @Callable
    public void serverQuery(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("MonitorServiceImpl.serverQuery()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            // 权限判断
            LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
            if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_server) == false) {
                throw new AppException("您没有权限进行此操作");
            }
            String rackid = StringUtil.trim(request.getParameter("rackid"));
            String menuflag = StringUtil.trim(request.getParameter("menuflag"));
            Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
            Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
            String status_param = StringUtil.trim(request.getParameter("status"));

            // 服务器信息
            JSONArray serversArray = new JSONArray();
            if ("server".equals(menuflag)) {
                serversArray = contactArray(MonitorConstant.serverMap);
            } else {
                serversArray = makeNewArray(MonitorConstant.serverMap.get(rackid));
                status_param = "all";
            }
            // 参数
            String region = StringUtil.trim(request.getParameter("region"));
            String room_name = StringUtil.trim(request.getParameter("room_name"));
            String rack_name = StringUtil.trim(request.getParameter("rack_name"));
            String server_name = StringUtil.trim(request.getParameter("server_name"));
            int r_normal = 0, r_warning = 0, r_error = 0, r_stop = 0;
            String status = "";
            if (!serversArray.isEmpty()) {
                String temp_status = "";
                for (int i = serversArray.size() - 1; i >= 0; i--) {
                    JSONObject server = serversArray.getJSONObject(i);
                    // 计算状态数量值
                    status = MonitorConstant.judgeThresholdUseRule(server, MonitorConstant.server_flag);
                    if (MonitorConstant.status_normal.equals(status)) {
                        r_normal++;
                    } else if (MonitorConstant.status_warn.equals(status)) {
                        r_warning++;
                    } else if (MonitorConstant.status_error.equals(status)) {
                        r_error++;
                    } else if (MonitorConstant.status_stop.equals(status)) {
                        r_stop++;
                    }
                    String regionid = JSONLibUtil.getString(server, "areaid");
                    String roomname = MonitorConstant.nameMap.get(JSONLibUtil.getString(server, "roomid"));
                    String rackname = MonitorConstant.nameMap.get(JSONLibUtil.getString(server, "rackid"));
                    String servername = MonitorConstant.nameMap.get(JSONLibUtil.getString(server, "uuid"));
                    // 区域
                    if (region != null && !"".equals(region) && !region.equals(regionid)) {
                        serversArray.remove(i);
                        continue;
                    }
                    // 机房名
                    if (room_name != null && !"".equals(room_name) && roomname.indexOf(room_name) == -1) {
                        serversArray.remove(i);
                        continue;
                    }
                    // 机架名
                    if (rack_name != null && !"".equals(rack_name) && rackname.indexOf(rack_name) == -1) {
                        serversArray.remove(i);
                        continue;
                    }
                    // 服务器名
                    if (server_name != null && !"".equals(server_name) && servername.indexOf(server_name) == -1) {
                        serversArray.remove(i);
                        continue;
                    }
                    if (!"all".equals(status_param)) {
                        // 默认值
                        if ("".equals(status_param)) {
                            status_param = "error";
                        }
                        temp_status = MonitorConstant.judgeThresholdUseRule(server, MonitorConstant.server_flag);
                        if (!temp_status.equals(status_param)) {
                            serversArray.remove(i);
                            continue;
                        }
                    }
                }
            }

            List<JSONObject> responseList = new ArrayList<JSONObject>();
            if (serversArray != null && serversArray.size() > 0) {
                for (int j = page * rows; j < ((page + 1) * rows > serversArray.size() ? serversArray.size()
                        : (page + 1) * rows); j++) {
                    JSONObject server = (JSONObject) serversArray.get(j);
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
                    json.put("cpu_usage",
                            MonitorConstant.convertStringToDouble(JSONLibUtil.getString(server, "cpu_usage")) + "%");
                    json.put("memory_usage",
                            MonitorConstant.convertStringToDouble(JSONLibUtil.getString(server, "memory_usage")) + "%");
                    json.put("disk_usage",
                            MonitorConstant.convertStringToDouble(JSONLibUtil.getString(server, "disk_usage")) + "%");
                    json.put("ip", JSONLibUtil.getString(server, "ip"));
                    status = MonitorConstant.judgeThresholdUseRule(server, MonitorConstant.server_flag);
                    json.put("status", status);
                    responseList.add(json);
                }
            }
            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("normal", r_normal + "");
            map.put("warning", r_warning + "");
            map.put("error", r_error + "");
            map.put("stop", r_stop + "");
            // 输出json数据
            ServiceHelper.writeMapResultAsJsonTo(response.getOutputStream(), serversArray.size(), responseList, map);
        } catch (Exception e) {
            logger.error("MonitorServiceImpl.serverQuery()", e);
            throw new AppException("查询失败");
        }
    }

    /**
     * @Description:查询云主机监控信息
     * @param request
     * @param response
     * @throws
     */
    @Callable
    public void hostQuery(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("MonitorServiceImpl.serverQuery()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            // 权限判断
            LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
            // 参数处理
            String menuflag = StringUtil.trim(request.getParameter("menuflag"));
            Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
            Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
            String status_param = StringUtil.trim(request.getParameter("status"));
            String status = "";
            // 资源池ID
            String poolid = StringUtil.trim(request.getParameter("poolid"));
            // 云主机信息
            JSONArray hostsArray = new JSONArray();
            if ("resourcetohost".equals(menuflag)) {
                if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_resource) == false) {
                    throw new AppException("您没有权限进行此操作");
                }
                status_param = "all";
                Iterator<String> its = MonitorConstant.hostsMap.keySet().iterator();
                // 由于放入数据的时候key键是存的uuid_name_areaname_areaid的格式，
                // 所以传入的poolid只能进行模糊匹配
                while (its.hasNext()) {
                    String key = its.next();
                    if (key.indexOf(poolid) > -1) {
                        hostsArray = MonitorConstant.hostsMap.get(key);
                    }
                }
                /*
                 * List<CloudHostData> cloudhosts = CloudHostPoolManager.getCloudHostPool().getAll();
                 * //将list转换成JSONArray if(cloudhosts.size()>0){ for(CloudHostData host:cloudhosts){ JSONObject json =
                 * new JSONObject(); json.put("region", host.getRegion()); json.put("hostName", host.getHostName());
                 * json.put("realHostId", host.getRealHostId()); json.put("cpuCore", host.getCpuCore() + "核");
                 * json.put("memory", CapacityUtil.toCapacityLabel(host.getMemory(),0)); json.put("dataDisk",
                 * CapacityUtil.toCapacityLabel(host.getDataDisk(),0)); json.put("cpu_usage", host.getCpuUsage());
                 * json.put("memory_usage", host.getMemoryUsage()); json.put("disk_usage", host.getDataDiskUsage());
                 * json.put("readSpeed", host.getReadSpeed()); json.put("writeSpeed", host.getWriteSpeed());
                 * json.put("receiveSpeed", host.getReceiveSpeed()); json.put("sendSpeed", host.getSendSpeed());
                 * json.put("status", host.getRunningStatus()); hostsArray.add(json); } }
                 */
            } else {
                if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_cloudhost) == false) {
                    throw new AppException("您没有权限进行此操作");
                }
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
            // 参数
            String region = StringUtil.trim(request.getParameter("region"));
            String host_name = StringUtil.trim(request.getParameter("host_name"));
            String serverid = StringUtil.trim(request.getParameter("serverid"));
            int r_normal = 0, r_warning = 0, r_error = 0, r_stop = 0;
            if (hostsArray != null && !hostsArray.isEmpty()) {
                String temp_status = "";
                for (int i = hostsArray.size() - 1; i >= 0; i--) {
                    JSONObject host = hostsArray.getJSONObject(i);
                    // 计算状态数量值
                    status = MonitorConstant.judgeThresholdUseRule(host, MonitorConstant.host_flag);
                    if (MonitorConstant.status_normal.equals(status)) {
                        r_normal++;
                    } else if (MonitorConstant.status_warn.equals(status)) {
                        r_warning++;
                    } else if (MonitorConstant.status_error.equals(status)) {
                        r_error++;
                    } else if (MonitorConstant.status_stop.equals(status)) {
                        r_stop++;
                    }
/*                    System.out.println("计算数量,云主机:" + JSONLibUtil.getString(host, "name") + ",status:" + status + ",json:"
                            + host);*/
                    String regionid = JSONLibUtil.getString(host, "areaid");
                    String hostname = JSONLibUtil.getString(host, "name");
                    String serverIP = JSONLibUtil.getStringArray(host, "ip")[0];
                    // 区域
                    if (region != null && !"".equals(region) && !region.equals(regionid)) {
                        hostsArray.remove(i);
                        continue;
                    }
                    // 主机名
                    if (host_name != null && !"".equals(host_name) && hostname.indexOf(host_name) == -1) {
                        hostsArray.remove(i);
                        continue;
                    }
                    // 服务器
                    if (serverid != null && !"".equals(serverid) && !"null".equals(serverid)
                            && !serverid.equals(MonitorConstant.serversIP.get(serverIP))) {
                        status_param = "all";
                        hostsArray.remove(i);
                        continue;
                    }
                    if (!"all".equals(status_param)) {
                        // 默认值
                        if ("".equals(status_param)) {
                            status_param = "error";
                        }
                        temp_status = MonitorConstant.judgeThresholdUseRule(host, MonitorConstant.host_flag);
                        if (!temp_status.equals(status_param)) {
                            hostsArray.remove(i);
                            continue;
                        }
                    }
                }
            }

            List<JSONObject> responseList = new ArrayList<JSONObject>();
            if (hostsArray != null && hostsArray.size() > 0) {
                for (int j = page * rows; j < ((page + 1) * rows > hostsArray.size() ? hostsArray.size() : (page + 1)
                        * rows); j++) {
                    JSONObject host = (JSONObject) hostsArray.get(j);
                    JSONObject json = new JSONObject();
                    json.put("commonip", JSONLibUtil.getStringArray(host, "ip")[1]);
                    json.put("serverip", JSONLibUtil.getStringArray(host, "ip")[0]);
                    json.put("region", JSONLibUtil.getString(host, "areaid"));
                    json.put("regionname", JSONLibUtil.getString(host, "area_name"));
                    json.put("hostName", JSONLibUtil.getString(host, "name"));
                    json.put("realHostId", JSONLibUtil.getString(host, "uuid"));
                    json.put("uuid", JSONLibUtil.getString(host, "uuid"));
                    json.put("cpuCore", JSONLibUtil.getString(host, "cpu_count") + "核");
                    json.put("cpu_usage",
                            MonitorConstant.convertStringToDouble(JSONLibUtil.getString(host, "cpu_usage")) + "%");
                    json.put("memory",
                            CapacityUtil.toGBValue(new BigInteger(host.getJSONArray("memory").get(1) + ""), 0) + "G");
                    json.put("memory_usage",
                            MonitorConstant.convertStringToDouble(JSONLibUtil.getString(host, "memory_usage")) + "%");
                    json.put("dataDisk",
                            CapacityUtil.toGBValue(new BigInteger(host.getJSONArray("disk_volume").get(1) + ""), 0)
                                    + "G");
                    json.put("disk_usage",
                            MonitorConstant.convertStringToDouble(JSONLibUtil.getString(host, "disk_usage")) + "%");
                    status = MonitorConstant.judgeThresholdUseRule(host, MonitorConstant.host_flag);
/*                    System.out.println("云主机:" + JSONLibUtil.getString(host, "name") + ",status:" + status + ",json:"
                            + host);*/
                    json.put("status", status);
                    
/*                    json.put("readSpeed", JSONLibUtil.getString(host, "readSpeed"));
                    json.put("writeSpeed", JSONLibUtil.getString(host, "writeSpeed"));
                    json.put("receiveSpeed", JSONLibUtil.getString(host, "receiveSpeed"));
                    json.put("sendSpeed", JSONLibUtil.getString(host, "sendSpeed"));
*/
                    responseList.add(json);
                }
            }
            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("normal", r_normal + "");
            map.put("warning", r_warning + "");
            map.put("error", r_error + "");
            map.put("stop", r_stop + "");
            // 输出json数据
            ServiceHelper.writeMapResultAsJsonTo(response.getOutputStream(), hostsArray.size(), responseList, map);
        } catch (Exception e) {
            logger.error("MonitorServiceImpl.serverQuery()", e);
            throw new AppException("查询失败");
        }
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
                    String rackid = MonitorConstant.getParentID(serverid, MonitorConstant.serverMap);
                    String roomid = MonitorConstant.getParentID(rackid, MonitorConstant.rackMap);
                    String areaid = MonitorConstant.getParentID(roomid, MonitorConstant.roomMap);
                    map.put("areaid", areaid);
                    map.put("roomid", roomid);
                    map.put("rackid", rackid);
                    map.put("serverid", serverid);
                    map.put("servername", MonitorConstant.nameMap.get(serverid));
                    map.put("cpu_useage", monitor.getCpu_usage());
                    map.put("memory_useage", monitor.getMemory_usage());
                    map.put("disk_useage", monitor.getDisk_usage());
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
    @Callable
    public JSONArray resourcePoolQuery(String areaid) {
        try {
            logger.debug("MonitorServiceImpl.resourcePoolQuery()");
            Iterator<String> its = MonitorConstant.hostsMap.keySet().iterator();
            String poolid = "";
            JSONArray returnarray = new JSONArray();
            /* ==========测试用代码start========== */
            /*
             * JSONObject test = new JSONObject(); test.put("poolname", "资源池名称"); test.put("areaname", "区域名称");
             * test.put("normal", 10); test.put("warn", 5); test.put("error", 8); returnarray.add(test);
             */
            /* ==========测试用代码end========== */
            while (its.hasNext()) {
                poolid = its.next();
                JSONArray hosts = MonitorConstant.hostsMap.get(poolid);
                JSONObject json = new JSONObject();
                Integer normal = 0, warn = 0, error = 0, stop = 0;
                // 循环主机，根据主机自身信息和阀值计算当前状态
                if (hosts != null && hosts.size() > 0) {
                    for (int i = 0; i < hosts.size(); i++) {
                        JSONObject host = hosts.getJSONObject(i);
                        String status = MonitorConstant.judgeThresholdUseRule(host, "host");
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
                json.put("poolid", poolid.split("-")[0]);
                json.put("poolname", poolid.split("-")[1]);
                json.put("areaname", poolid.split("-")[2]);
                json.put("normal", normal);
                json.put("warn", warn);
                json.put("error", error);
                json.put("stop", stop);
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
        }
    }

    /**
     * @Description:资源管理图表层跳转列表层
     * @param request
     * @param response
     * @return String
     */
    @Callable
    public String resourceToHostPage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("MonitorServiceImpl.resourceToHostPage()");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_resource) == false) {
            return "/public/have_not_access.jsp";
        }
        request.setAttribute("poolid", request.getParameter("poolid"));
        request.setAttribute("menuflag", request.getParameter("menuflag"));
        request.setAttribute("poolname", request.getParameter("poolname"));
        return "/security/operator/monitor_resourcetohost.jsp";
    }
    
    /**
     * @Description:屏蔽和取消屏蔽
     * @param request
     * @param response
     * @throws IOException
     */
    @Callable
    public MethodResult updateShield(String type,String shield,String uuid) {
        Iterator<String> its = null;
        String key = "";
        try {
            if ("server".equals(type)) {
                synchronized (MonitorConstant.serverMap) {
                    its = MonitorConstant.serverMap.keySet().iterator();
                    JSONArray array = new JSONArray();
                    while (its.hasNext()) {
                        key = its.next();
                        array = MonitorConstant.serverMap.get(key);
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            if (uuid.equals(json.getString("uuid"))) {
                                json.put(MonitorConstant.shield, shield);
                                MonitorConstant.shieldmap.put(json.getString("uuid"), shield);
                            }
                        }
                    }
                }
            } else if ("host".equals(type)) {
                synchronized (MonitorConstant.hostsMap) {
                    its = MonitorConstant.hostsMap.keySet().iterator();
                    JSONArray array = new JSONArray();
                    while (its.hasNext()) {
                        key = its.next();
                        array = MonitorConstant.hostsMap.get(key);
                        for (int i = 0; i < array.size(); i++) {
                            JSONObject json = array.getJSONObject(i);
                            if (uuid.equals(json.getString("uuid"))) {
                                json.put(MonitorConstant.shield, shield);
                                MonitorConstant.shieldmap.put(json.getString("uuid"), shield);
                            }
                        }
                    }
                }
            } else if ("service".equals(type)){
                // 服务操作同时更新服务的存放变量值
                ServiceInfoPool pool = ServiceInfoPoolManager.singleton().getPool();
                ServiceInfoExt service = pool.get(uuid);
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
                pool.put(service);
                MonitorConstant.shieldmap.put(uuid, shield);
            }
            
            // 更新数据库
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("id", uuid);
            map.put("shield", shield);
            this.saveShieldData(map);
            return new MethodResult(MethodResult.SUCCESS, "操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new MethodResult(MethodResult.SUCCESS, "操作失败,请联系管理员");
        }
    }
    
    /**
     * Description:查看详情页面跳转
     * @param request
     * @param response
     * @return String
     */
    @Callable
    public String viewDetailPage(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("MonitorServiceImpl.viewDetailPage()");
        // 权限判断
        LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
        if (loginInfo.hasPrivilege(PrivilegeConstant.monitor_area) == false) {
            return "/public/have_not_access.jsp";
        }
        String type = request.getParameter("type");
        String uuid = request.getParameter("uuid");
        Iterator<String> its = null;
        String key = "";
        String status = "";
        JSONObject objectdata = new JSONObject();
        if ("server".equals(type)) {
            synchronized (MonitorConstant.serverMap) {
                its = MonitorConstant.serverMap.keySet().iterator();
                JSONArray array = new JSONArray();
                while (its.hasNext()) {
                    key = its.next();
                    array = MonitorConstant.serverMap.get(key);
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        if (uuid.equals(json.getString("uuid"))) {
                            status = MonitorConstant.judgeThresholdUseRule(json, MonitorConstant.server_flag);
                            objectdata.put("type", type);
                            objectdata.put("name", JSONLibUtil.getString(json, "name"));
                            objectdata.put("ip", JSONLibUtil.getString(json, "ip"));
                            if (MonitorConstant.status_normal.equals(status)) {
                                objectdata.put("status", "正常");
                            } else if (MonitorConstant.status_error.equals(status)) {
                                objectdata.put("status", "故障");
                            } else if (MonitorConstant.status_warn.equals(status)) {
                                objectdata.put("status", "告警");
                            } else if (MonitorConstant.status_stop.equals(status)) {
                                objectdata.put("status", "屏蔽");
                            }
                            objectdata.put("cpu_count", JSONLibUtil.getString(json, "cpu_count") + "核");
                            objectdata.put("cpu_usage",
                                    MonitorConstant.convertStringToDouble(JSONLibUtil.getString(json, "cpu_usage"))
                                            + "%");
                            objectdata.put("memory_usage",
                                    MonitorConstant.convertStringToDouble(JSONLibUtil.getString(json, "memory_usage"))
                                            + "%");
                            objectdata.put("disk_usage",
                                    MonitorConstant.convertStringToDouble(JSONLibUtil.getString(json, "disk_usage"))
                                            + "%");
                            objectdata.put("memory",
                                    CapacityUtil.toGBValue(new BigInteger(json.getJSONArray("memory").get(1) + ""), 0)
                                            + "G");
                            objectdata.put(
                                    "disk",
                                    CapacityUtil.toGBValue(
                                            new BigInteger(json.getJSONArray("disk_volume").get(1) + ""), 0) + "G");
                            request.setAttribute("objectdata", objectdata);
                        }
                    }
                }
            }
        } else if ("host".equals(type)) {
            synchronized (MonitorConstant.hostsMap) {
                its = MonitorConstant.hostsMap.keySet().iterator();
                JSONArray array = new JSONArray();
                while (its.hasNext()) {
                    key = its.next();
                    array = MonitorConstant.hostsMap.get(key);
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject json = array.getJSONObject(i);
                        if (uuid.equals(json.getString("uuid"))) {
                            status = MonitorConstant.judgeThresholdUseRule(json, MonitorConstant.host_flag);
                            objectdata.put("type", type);
                            objectdata.put("name", JSONLibUtil.getString(json, "name"));
                            //objectdata.put("ip", JSONLibUtil.getStringArray(json, "ip")[1]);
                            if (MonitorConstant.status_normal.equals(status)) {
                                objectdata.put("status", "正常");
                            } else if (MonitorConstant.status_error.equals(status)) {
                                objectdata.put("status", "故障");
                            } else if (MonitorConstant.status_warn.equals(status)) {
                                objectdata.put("status", "告警");
                            } else if (MonitorConstant.status_stop.equals(status)) {
                                objectdata.put("status", "屏蔽");
                            }
                            objectdata.put("cpu_count", JSONLibUtil.getString(json, "cpu_count") + "核");
                            objectdata.put("cpu_usage",
                                    MonitorConstant.convertStringToDouble(JSONLibUtil.getString(json, "cpu_usage"))
                                            + "%");
                            objectdata.put("memory_usage",
                                    MonitorConstant.convertStringToDouble(JSONLibUtil.getString(json, "memory_usage"))
                                            + "%");
                            objectdata.put("disk_usage",
                                    MonitorConstant.convertStringToDouble(JSONLibUtil.getString(json, "disk_usage"))
                                            + "%");
                            objectdata.put("memory",
                                    CapacityUtil.toGBValue(new BigInteger(json.getJSONArray("memory").get(1) + ""), 0)
                                            + "G");
                            objectdata.put(
                                    "disk",
                                    CapacityUtil.toGBValue(
                                            new BigInteger(json.getJSONArray("disk_volume").get(1) + ""), 0) + "G");
                            objectdata.put("port", json.get("port"));// 主机端口映射
                            objectdata.put("user", json.get("user"));// 所属用户标签
                            objectdata.put("group", json.get("group"));// 所属用户组标签
                            objectdata.put("display", json.get("display"));// 监控验证用户名
                            objectdata.put("authentication", json.get("authentication"));// 监控验证密码
                            objectdata.put("inbound_bandwidth",
                                    CapacityUtil.toMBValue(new BigInteger(json.get("inbound_bandwidth") + ""), 0));// 入口带宽
                            objectdata.put("outbound_bandwidth",
                                    CapacityUtil.toMBValue(new BigInteger(json.get("outbound_bandwidth") + ""), 0));// 入口带宽
                            objectdata.put("network_type", json.get("network_type"));// 网络地址类型
                            objectdata.put("disk_type", json.get("disk_type"));// 磁盘模式
                            
                            request.setAttribute("objectdata", objectdata);
                        }
                    }
                }
            }
        }
        return "/security/operator/monitor_view_detail.jsp";
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
