
package com.zhicloud.ms.constant;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.vo.SysWarnRuleVO;
import com.zhicloud.ms.vo.SysWarnValueVO;

import net.sf.json.JSONObject;
import net.sf.json.JSONArray;

/**
 * @ClassName: MonitorConstant
 * @Description: 资源监控常量
 * @author 张本缘 于 2015年6月24日 下午5:15:37
 */
public class MonitorConstant {
    // 区域存放常量
    public static Set<JSONObject> areaSet = Collections.synchronizedSet(new TreeSet<JSONObject>());
    // 机房存放常量,String是区域ID
    public static Map<String, JSONArray> roomMap = Collections.synchronizedMap(new LinkedHashMap<String, JSONArray>());
    // 机架存放常量,String是机房ID
    public static Map<String, JSONArray> rackMap = Collections.synchronizedMap(new LinkedHashMap<String, JSONArray>());
    // 服务器存放常量,String是机架ID
    public static Map<String, JSONArray> serverMap = Collections
            .synchronizedMap(new LinkedHashMap<String, JSONArray>());
    // 云主机存放常量,String是资源池ID
    public static Map<String, JSONArray> hostsMap = Collections.synchronizedMap(new LinkedHashMap<String, JSONArray>());
    // 名称存放常量
    public static Map<String, String> nameMap = Collections.synchronizedMap(new LinkedHashMap<String, String>());
    // 阀值存放常量
    public static Map<String, String> thresholdMap = Collections.synchronizedMap(new HashMap<String, String>());
    // ip和服务器ID对应关系存放
    public static Map<String, String> serversIP = Collections.synchronizedMap(new HashMap<String, String>());
    // 状态
    public static final String status_normal = "normal";
    public static final String status_warn = "warning";
    public static final String status_error = "error";
    public static final String status_stop = "stop";
    // 服务器资源利用率超过警告阀值的次数
    public static Map<String, Integer> server_map_warn_count = Collections
            .synchronizedMap(new LinkedHashMap<String, Integer>());
    // 云主机资源利用率超过警告阀值的次数
    public static Map<String, Integer> host_map_warn_count = Collections
            .synchronizedMap(new LinkedHashMap<String, Integer>());
    // 服务器资源利用率超过故障阀值的次数
    public static Map<String, Integer> server_map_error_count = Collections
            .synchronizedMap(new LinkedHashMap<String, Integer>());
    // 云主机资源利用率超过故障阀值的次数
    public static Map<String, Integer> host_map_error_count = Collections
            .synchronizedMap(new LinkedHashMap<String, Integer>());
    // 服务器或云主机故障上次发送邮件的时间
    public static Map<String, Long> map_sendmail_time = Collections.synchronizedMap(new LinkedHashMap<String, Long>());
    // 故障恢复的队列
    public static List<String> server_error_list_recover = Collections.synchronizedList(new LinkedList<String>());
    public static List<String> host_error_list_recover = Collections.synchronizedList(new LinkedList<String>());
    // 告警恢复的队列
    public static List<String> server_warn_list_recover = Collections.synchronizedList(new LinkedList<String>());
    public static List<String> host_warn_list_recover = Collections.synchronizedList(new LinkedList<String>());
    // 使用告警规则代码
    public static final String monitor_rule_warn = "MONITOR_RULE_WARN";
    public static final String monitor_rule_error = "MONITOR_RULE_ERROR";
    public static final String monitor_rule_fixtime = "MONITOR_RULE_FIXTIME";
    public static final String monitor_rule_recover = "MONITOR_RULE_RECOVER";
    // 短信发送模板标示符
    public static final String monitor_sm_notify = "MONITOR_NOTIFY";
    // gw返回状态代码
    public static final String gw_status_normal = "0";
    public static final String gw_status_warn = "1";
    public static final String gw_status_error = "2";
    public static final String gw_status_stop = "3";
    // 告警规则配置存放常量
    public static Map<String, SysWarnRuleVO> rulevo = Collections
            .synchronizedMap(new LinkedHashMap<String, SysWarnRuleVO>());
    // 故障规则集合
    public static Map<String, List<SysWarnValueVO>> rule_values = Collections
            .synchronizedMap(new LinkedHashMap<String, List<SysWarnValueVO>>());
    // 区分服务器和云主机标示常量
    public static final String server_flag = "server";
    public static final String host_flag = "host";
    // 常量 是否已被屏蔽(0 取消屏蔽 1 屏蔽)
    public static final String shield = "shield";
    public static final String shield_on = "0";
    public static final String shield_off = "1";
    // 存放uuid和shield的对应关系
    public static Map<String, String> shieldmap = Collections.synchronizedMap(new LinkedHashMap<String, String>());
    // 存放监控对象的uuid和taskid对应关系
    public static Map<String, Integer> monitorData = Collections.synchronizedMap(new LinkedHashMap<String, Integer>());
    // 异步监控数据回调集合
    public static Map<String,JSONObject> receiveData = Collections.synchronizedMap(new LinkedHashMap<String, JSONObject>());
    //资源池信息存放
    public static Map<String, JSONObject> resourceData = Collections
            .synchronizedMap(new LinkedHashMap<String, JSONObject>());
    
    /**
     * @Description:根据ID查找父级ID
     * @param id
     * @param map 当前实体对象集合
     * @return 父级ID
     */
    public static String getParentID(String id, Map<String, JSONArray> map) {
        if (id == null || "".equals(id) || map.isEmpty()) {
            return "";
        }
        Object[] keySets = map.keySet().toArray();
        String returnid = "";
        boolean flag = false;
        for (int i = 0; i < keySets.length; i++) {
            String superid = (String) keySets[i];
            JSONArray Objects = map.get(superid);
            if (flag) {
                break;
            }
            for (int j = 0; j < Objects.size(); j++) {
                JSONObject object = (JSONObject) Objects.get(j);
                if (id.equals(JSONLibUtil.getString(object, "uuid"))) {
                    returnid = superid;
                    flag = true;
                    break;
                }
            }
        }
        return returnid;
    }

    /**
     * @Description:判断数组里面是否存在传入的ID值
     * @param id
     * @param lists
     * @return boolean
     */
    public static Integer isContainUUID(String id, Map<String, Integer> map) {
        if (map == null || map.isEmpty()) {
            return 0;
        }
        Integer returnnum = 0;
        if (map.containsKey(id)) {
            returnnum = map.get(id);
        }
        return returnnum;
    }

    /**
     * @Description:判断数组里面是否存在传入的ID值
     * @param id
     * @param lists
     * @return boolean
     */
    public static Long isContainUUID2(String id, Map<String, Long> map) {
        if (map == null || map.isEmpty()) {
            return -1L;
        }
        Long returnnum = 0L;
        if (map.containsKey(id)) {
            returnnum = map.get(id);
        }
        return returnnum;
    }

    /**
     * @Description:移除集合的某个元素
     * @param id
     * @param lists
     */
    public static void removeElement(String id, Map<String, Integer> maps) {
        if (maps != null && !maps.isEmpty()) {
            Iterator<String> it = maps.keySet().iterator();
            while (it.hasNext()) {
                String uuid = it.next();
                if (id.equals(uuid)) {
                    it.remove();
                    break;
                }
            }
        }
    }

    /**
     * @Description:根据阀值判断当前对象的状态
     * @param json
     * @return String
     */
    public static String judgeThreshold(JSONObject json) {
        if (json == null) {
            return MonitorConstant.status_normal;
        }
        // 如果gw接口返回的对象状态是故障，则直接返回
        if ("2".equals(JSONLibUtil.getString(json, "status"))) {
            return MonitorConstant.status_error;
        }
        // 如果gw接口返回的对象状态是停止，则直接返回
        if ("3".equals(JSONLibUtil.getString(json, "status"))) {
            return MonitorConstant.status_error;
        }
        String cpu_usage = MonitorConstant.thresholdMap.get("cpu_usage");
        String memory_usage = MonitorConstant.thresholdMap.get("memory_usage");
        String disk_usage = MonitorConstant.thresholdMap.get("disk_usage");
        if (cpu_usage == null || "".equals(cpu_usage)) {
            return MonitorConstant.status_normal;
        }
        if (memory_usage == null || "".equals(memory_usage)) {
            return MonitorConstant.status_normal;
        }
        if (disk_usage == null || "".equals(disk_usage)) {
            return MonitorConstant.status_normal;
        }
        // cpu利用率阀值
        float cpu_warn = Float.parseFloat(cpu_usage.split("-")[0]);
        float cpu_error = Float.parseFloat(cpu_usage.split("-")[1]);
        // 内存利用率阀值
        float memory_warn = Float.parseFloat(memory_usage.split("-")[0]);
        float memory_error = Float.parseFloat(memory_usage.split("-")[1]);
        // 磁盘利用率阀值
        float disk_warn = Float.parseFloat(memory_usage.split("-")[0]);
        float disk_error = Float.parseFloat(memory_usage.split("-")[1]);
        // gw返回的利用率
        double jsoncpu = convertStringToDouble(JSONLibUtil.getString(json, "cpu_usage"));
        double jsonmemory = convertStringToDouble(JSONLibUtil.getString(json, "memory_usage"));
        double jsondisk = convertStringToDouble(JSONLibUtil.getString(json, "disk_usage"));
        // 只要有一个利用率达到阀值,都返回故障状态
        if (jsoncpu >= cpu_error || jsonmemory >= memory_error || jsondisk >= disk_error) {
            return MonitorConstant.status_error;
        }
        // 只要有一个利用率在警告阀值返回，都返回警告状态
        if ((jsoncpu >= cpu_warn && jsoncpu < cpu_error) || (jsonmemory >= memory_warn && jsonmemory < memory_error)
                || (jsondisk >= disk_warn && jsondisk < disk_error)) {
            return MonitorConstant.status_warn;
        }
        return MonitorConstant.status_normal;
    }

    /**
     * @Description:将字符串转换成double类型,四舍五入
     * @param value
     * @return
     * @throws
     */
    public static double convertStringToDouble(String value) {
        // 科学计数法转换
        if (value.indexOf("E-") > -1) {
            BigDecimal bg = new BigDecimal(value);
            value = bg.toPlainString();
        }
        if (value == null || "".equals(value) || "null".equals(value)) {
            return 0;
        }
        double f = Double.parseDouble(value) * 100;
        BigDecimal bg = new BigDecimal(f);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * @Description:根据阀值判断当前对象的状态
     * @param json
     * @param flag server表示服务器 host表示云主机
     * @return String
     */
    public static String judgeThresholdUseRule(JSONObject json, String flag) {
        if (json == null) {
            return MonitorConstant.status_normal;
        }
        // 如果该对象已被屏蔽则返回stop状态
        if (json.get(MonitorConstant.shield) != null
                && MonitorConstant.shield_off.equals(json.getString(MonitorConstant.shield))) {
            return MonitorConstant.status_stop;
        }
        // 解析告警规则
        Integer warn = AnalyzeStaticRule(monitor_rule_warn, json);

        Integer error = AnalyzeStaticRule(monitor_rule_error, json);
        // 当同时符合故障和告警规则时,显示故障
        if (error > 0) {
            return MonitorConstant.status_error;
        }
        if (warn > 0) {
            return MonitorConstant.status_warn;
        }
        // 如果gw接口返回的对象状态是告警，则直接返回
        if (gw_status_warn.equals(JSONLibUtil.getString(json, "status"))) {
            return MonitorConstant.status_warn;
        }
        // 如果gw接口返回的对象状态是故障，则直接返回
        if (gw_status_error.equals(JSONLibUtil.getString(json, "status"))) {
            return MonitorConstant.status_error;
        }
        // 如果gw接口返回的对象状态是停止，则直接返回
        if (gw_status_stop.equals(JSONLibUtil.getString(json, "status"))) {
            if (server_flag.equals(flag)) {// 服务器关机默认是故障
                return MonitorConstant.status_error;
            } else if (host_flag.equals(flag)) {// 云主机关机默认是正常
                return MonitorConstant.status_normal;
            } else {// 其他暂时默认故障
                return MonitorConstant.status_error;
            }

        }
        return MonitorConstant.status_normal;
    }

    /**
     * @Description:根据规则编码解析当前传入的json对象是否符合该规则
     * @param code 规则编码
     * @param json 对象
     * @return -1、该规则不存在 0、不符合 1、符合
     * @throws
     */
    public static Integer AnalyzeStaticRule(String code, JSONObject json) {
        Integer returnnum = 0;
        SysWarnRuleVO rule = rulevo.get(code);
        if (rule == null) {
            // 没有定义规则
            return -1;
        }
        List<SysWarnValueVO> values = rule_values.get(code);
        if (values == null || values.size() == 0) {
            // 规则没有设置内容
            return -1;
        }
        boolean[] judge = new boolean[values.size()];
        boolean temp = true;
        for (int i = 0; i < values.size(); i++) {
            SysWarnValueVO value = values.get(i);
            String json_value = json.getString(value.getCode());
            // 没有取到值
            if (json_value == null || "".equals(json_value)) {
                continue;
            }
            // 解析操作符operator
            if ("equal".equals(value.getOperator())) {
                judge[i] = json_value.equals(value.getOperator());
            } else if ("high".equals(value.getOperator())) {
                judge[i] = Float.parseFloat(json_value) > Float.parseFloat(value.getValue());
            } else if ("low".equals(value.getOperator())) {
                judge[i] = Float.parseFloat(json_value) < Float.parseFloat(value.getValue());
            } else if ("between".equals(value.getOperator())) {
                String lower = value.getValue().split("-")[0];
                String higher = value.getValue().split("-")[1];
                if (Float.parseFloat(json_value) >= Float.parseFloat(lower)
                        && Float.parseFloat(json_value) <= Float.parseFloat(higher)) {
                    judge[i] = true;
                } else {
                    judge[i] = false;
                }
            }
            // 解析连接符
            if (i == 0 && i == values.size() - 1) {// 只有一条规则内容
                if (judge[i]) {
                    temp = true;
                    returnnum = 1;
                } else {
                    returnnum = 0;
                    temp = false;
                }
            }
            if (i > 0 && i < values.size()) {// 多条规则内容
                if ("and".equals(values.get(i - 1).getContact())) {
                    temp = temp && judge[i - 1] && judge[i];
                }
                if ("or".equals(values.get(i - 1).getContact())) {
                    if (temp) {
                        temp = temp && judge[i - 1] || judge[i];
                    } else {
                        temp = temp || judge[i - 1] || judge[i];
                    }

                }
            }
        }
        if (temp) {
            returnnum = 1;
        } else {
            returnnum = 0;
        }
        return returnnum;
    }

    /**
     * @Description:取得所有机房uuids
     * @return Object[]
     */
    public static String[] getRoomObjects() {
        Set<String> rooms = MonitorConstant.rackMap.keySet();
        String[] room_ids = new String[rooms.size()];
        for (int i = 0; i < rooms.size(); i++) {
            String uuid = rooms.iterator().next();
            room_ids[i] = uuid;
        }
        return room_ids;
    }

    /**
     * @Description:取得所有的机架uuids
     * @return Object[]
     */
    public static String[] getRackObjects() {
        Set<String> racks = MonitorConstant.serverMap.keySet();
        String[] rack_ids = new String[racks.size()];
        for (int i = 0; i < racks.size(); i++) {
            String uuid = racks.iterator().next();
            rack_ids[i] = uuid;
        }
        return rack_ids;
    }

    /**
     * @Description:取得所有正常状态服务器uuid列表
     * @return
     */
    public static String[] getServerObjects() {
        Set<String> racks = MonitorConstant.serverMap.keySet();
        JSONArray array = new JSONArray();
        for (int i = 0; i < racks.size(); i++) {
            String uuid = racks.iterator().next();
            JSONArray servers = MonitorConstant.serverMap.get(uuid);
            for (int j = 0; j < servers.size(); j++) {
                JSONObject server = servers.getJSONObject(j);
                if ("0".equals(server.getString("status"))) {
                    array.add(server.getString("uuid"));
                }
            }
        }
        String[] rack_ids = new String[array.size()];
        for (int k = 0; k < array.size(); k++) {
            rack_ids[k] = array.getString(k);
        }
        return rack_ids;
    }

    /**
     * @Description:根据uuid从常量里面寻找匹配的对象
     * @param uuid
     * @param map
     * @return JSONObject
     */
    public static JSONObject getObjectByUUID(String uuid, Map<String, JSONArray> map) {
        if (uuid == null || "".equals(uuid)) {
            return null;
        }
        Iterator<String> its = map.keySet().iterator();
        String key = "";
        JSONArray array = new JSONArray();
        JSONObject json = new JSONObject();
        JSONObject data = new JSONObject();
        boolean flag = false;
        while (its.hasNext()) {
            key = its.next();
            array = map.get(key);
            if (flag) {
                break;
            }
            for (int i = 0; i < array.size(); i++) {
                json = array.getJSONObject(i);
                if (uuid.equals(json.get("uuid") + "")) {
                    data = json;
                    flag = true;
                    break;
                }
            }
        }
        return data;
    }
    
    /**
     * @Description:计算数组里面对象不同状态的数量
     * @param list
     * @return JSONObject
     */
    public static JSONObject getStatusNum(List<JSONObject> list) {
        JSONObject json = new JSONObject();
        Integer normal = 0, warning = 0, error = 0, stop = 0;
        if (list != null && list.size() > 0) {
            String status = "";
            for (JSONObject data : list) {
                status = data.get("status") + "";
                if (MonitorConstant.status_normal.equals(status)) {
                    normal++;
                } else if (MonitorConstant.status_warn.equals(status)) {
                    warning++;
                } else if (MonitorConstant.status_error.equals(status)) {
                    error++;
                } else {
                    stop++;
                }
            }
        }
        json.put("normal", normal);
        json.put("warning", warning);
        json.put("error", error);
        json.put("stop", stop);
        return json;
    }
    
    /**
     * @Description:计算数组里面对象不同状态的数量
     * @param list
     * @return JSONObject
     */
    public static JSONObject getStatusNumByArray(JSONArray list) {
        JSONObject json = new JSONObject();
        Integer normal = 0, warning = 0, error = 0, stop = 0;
        if (list != null && list.size() > 0) {
            String status = "";
            for (int i = 0; i < list.size(); i++) {
                JSONObject obj = list.getJSONObject(i);
                status = MonitorConstant.judgeThresholdUseRule(obj, MonitorConstant.host_flag);
                if (MonitorConstant.status_normal.equals(status)) {
                    normal++;
                } else if (MonitorConstant.status_warn.equals(status)) {
                    warning++;
                } else if (MonitorConstant.status_error.equals(status)) {
                    error++;
                } else {
                    stop++;
                }
            }
        }
        json.put("normal", normal);
        json.put("warning", warning);
        json.put("error", error);
        json.put("stop", stop);
        return json;
    }
    
    /**
     * @Description:保存uuid和taskid关联关系
     * @param json
     */
    public static void saveUUIDAndTaskID(JSONObject json) {
        //System.out.println("回调执行了");
        JSONArray uuids = json.getJSONArray("uuid");
        int task = json.getInt("task");
        if (uuids != null && uuids.size() > 0) {
            for (int i = 0; i < uuids.size(); i++) {
                JSONObject data = new JSONObject();
                // 磁盘IO
                JSONArray disk_io = json.getJSONArray("disk_io").getJSONArray(i);
                // 网络IO
                JSONArray network_io = json.getJSONArray("network_io").getJSONArray(i);
                // 速度 单位字节/s
                JSONArray speed = json.getJSONArray("speed").getJSONArray(i);
                data.put("disk_io", disk_io);
                data.put("network_io", network_io);
                data.put("speed", speed);
                receiveData.put(uuids.getString(i), data);
                //System.out.println("i===="+i+",uuid:"+uuids.getString(i));
            }
            monitorData.put(json.getString("uuid"), task);
        }

    }
    
    /**
     * @Description:取得所有正常状态云主机uuid列表
     * @return
     */
    public static String[] getHostObjects(Integer areaid) {
        Iterator<String> pools = MonitorConstant.hostsMap.keySet().iterator();
        JSONArray array = new JSONArray();
        while (pools.hasNext()) {
            String poolid = pools.next();
            JSONArray hosts = hostsMap.get(poolid);
            if (!hosts.isEmpty() && hosts.size() > 0) {
                for (int i = 0; i < hosts.size(); i++) {
                    JSONObject host = hosts.getJSONObject(i);
                    if (gw_status_normal.equals(host.getString("status")) && host.getInt("areaid") == areaid) {
                        array.add(host.getString("uuid"));
                        //System.out.println("内存主机状态:"+host.getString("status")+",主机name:"+host.getString("name"));
                    }
                }
            }
        }
        String[] host_ids = new String[array.size()];
        for (int k = 0; k < array.size(); k++) {
            host_ids[k] = array.getString(k);
        }
        return host_ids;
    }
    
    /**
     * @Description:取得所有正常状态服务器uuid列表
     * @return
     */
    public static String[] getServerObjects(Integer areaid) {
        Iterator<String> racks = MonitorConstant.serverMap.keySet().iterator();
        JSONArray array = new JSONArray();
        while (racks.hasNext()) {
            String rack = racks.next();
            JSONArray servers = serverMap.get(rack);
            if (!servers.isEmpty() && servers.size() > 0) {
                for (int i = 0; i < servers.size(); i++) {
                    JSONObject server = servers.getJSONObject(i);
                    if (gw_status_normal.equals(server.getString("status")) && server.getInt("areaid") == areaid) {
                        array.add(server.getString("uuid"));
                    }
                }
            }
        }
        String[] rack_ids = new String[array.size()];
        for (int k = 0; k < array.size(); k++) {
            rack_ids[k] = array.getString(k);
        }
        return rack_ids;
    }
    
    /**
     * @Description:将Double转化成百分比
     * @param d
     * @return String
     */
    public static String convertDoubleToString(Double d) {
        if (d.isNaN()) {
            return "0.0%";
        }
        return d + "%";
    }
}
