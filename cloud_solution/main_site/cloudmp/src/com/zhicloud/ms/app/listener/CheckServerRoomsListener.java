
package com.zhicloud.ms.app.listener;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.constant.MonitorConstant;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.httpGateway.MonitorLevel;
import com.zhicloud.ms.message.MessageServiceManager;
import com.zhicloud.ms.message.email.EmailSendService;
import com.zhicloud.ms.message.sms.SmsSendService;
import com.zhicloud.ms.service.MonitorService;
import com.zhicloud.ms.service.SysWarnService;
import com.zhicloud.ms.vo.MonitorServerVO;
import com.zhicloud.ms.vo.SysWarnRuleVO;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;

/**
 * @ClassName: 定时检测区域监控信息,并更新到内存
 * @Description: Description of this class
 * @author 张本缘 于 2015年4月29日 上午10:51:23
 */
public class CheckServerRoomsListener implements ServletContextListener {
    public static final Logger logger = Logger.getLogger(CheckServerRoomsListener.class);
    // 调度控制器
    private Timer timer = new Timer();
    public static ServletContext servletContext;
    // 故障间隔通知时间
    private static final long ERROR_TIME = 60 * 60 * 1000;
    // 定时更新内存数据间隔时间 默认五秒
    private static final long INTERVAL_TIME = 10 * 1000;
    // 一天的毫秒数
    private static final long PERIOD_DAY = 24 * 60 * 60 * 1000;
    /** =================规则配置信息======================= **/
    // 默认连续故障次数(采样次数)
    private static Integer ERROR_COUNT = 3;
    // 默认连续告警次数(采样次数)
    private static Integer WARN_COUNT = 3;
    // 是否发送通知的开关
    private static final Integer yes = 1;
    // 定时还是实时发送通知
    private static final Integer timing = 2;// 实时
    private static final Integer fixtime = 1;// 定时
    // 规则配置的通知类型
    private static Integer warn_notify_type = 0;
    private static Integer error_notify_type = 0;
    // 是否发送故障通知
    private static Integer error_on_off = 0;
    private static Integer warn_on_off = 0;
    // 定时任务间隔执行时间(采样频率)
    private static long ERROR_PERIOD_MIN = 0;
    private static long WARN_PERIOD_MIN = 0;
    // 定时时间
    private static Calendar error_cal = Calendar.getInstance();
    private static Calendar warn_cal = Calendar.getInstance();
    /** ============================================= **/
    BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
    // 监控服务接口
    private MonitorService monitorService = (MonitorService)factory.getBean("monitorService"); 
    
    // 规则服务接口
    private SysWarnService sysWarnService =(SysWarnService)factory.getBean("sysWarnService");

    // 区域ID
    public static final Integer regionDataID = 1;
    public static final String regionDataName = "成都";
    
    private boolean monitorFlag = true;
    /**
     * Description:启动
     * 
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 记录任务开始时间
        sce.getServletContext().log("定时更新gw监控数据任务已启动");
        try {
            initThresholdData(sce);
            monitorService.initMonitorShield();
        } catch (Exception e1) {
            e1.printStackTrace();
            logger.error("初始化配置信息出错:" + e1.getMessage());
        }
        /** ================定时任务更新内存数据========================= **/
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    // 硬件信息
                    updateMemoryData();
                    // 云主机信息
                    putHostsIntoMemory();
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("定时更新内存数据出错:" + e.getMessage());
                }
            }
        }, 0, INTERVAL_TIME);
        /** =======================故障规则类型(必须要设置了采样频率才执行任务)=================== **/
        if (ERROR_PERIOD_MIN > 0) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // 采集故障信息
                    try {
                        putServerErrorInfo();// 服务器
                        putHostErrorInfo();// 云主机
                        if(monitorFlag){
                            startServerMonitor();
                            startHostsMonitor();
                            monitorFlag = false;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("采集故障信息出错:" + e.getMessage());
                    }
                }
            }, 0, ERROR_PERIOD_MIN);
            // 定时任务
            if (error_notify_type == fixtime && error_on_off == yes) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            // 每天定时汇总平均数据
                            monitorService.saveMonitorData(MonitorConstant.monitor_rule_error,
                                    MonitorConstant.server_flag);
                            monitorService.saveMonitorData(MonitorConstant.monitor_rule_error,
                                    MonitorConstant.host_flag);
                            sendTotalEmail(MonitorConstant.monitor_rule_fixtime, MonitorConstant.monitor_rule_error,
                                    MonitorConstant.server_flag);
                            sendTotalEmail(MonitorConstant.monitor_rule_fixtime, MonitorConstant.monitor_rule_error,
                                    MonitorConstant.host_flag);
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("故障定时任务出错:" + e.getMessage());
                        }
                    }
                }, error_cal.getTime(), PERIOD_DAY);
            }
            // 实时任务
            if (error_notify_type == timing && error_on_off == yes) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            // 发送错误邮件
                            sendServerErrorEmail(MonitorConstant.monitor_rule_error);// 服务器
                            sendHostErrorEmail(MonitorConstant.monitor_rule_error);// 云主机
                            // 发送恢复邮件
                            sendRecoverEmail(MonitorConstant.monitor_rule_recover, MonitorConstant.server_flag);
                            sendRecoverEmail(MonitorConstant.monitor_rule_recover, MonitorConstant.host_flag);
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("故障实时任务出错:" + e.getMessage());
                        }
                    }
                }, 0, ERROR_PERIOD_MIN);
            }
        }
        /** =======================告警规则类型(必须要设置了采样频率才执行任务)=================== **/
        if (WARN_PERIOD_MIN > 0) {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    // 采集告警信息
                    try {
                        putServerWarnInfo();// 服务器
                        putHostWarnInfo();// 云主机
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("采集告警信息出错:" + e.getMessage());
                    }

                }
            }, 0, WARN_PERIOD_MIN);
            // 定时任务
            if (warn_notify_type == fixtime && warn_on_off == yes) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            // 每天汇总平均数据
                            monitorService.saveMonitorData(MonitorConstant.monitor_rule_warn,
                                    MonitorConstant.server_flag);
                            monitorService.saveMonitorData(MonitorConstant.monitor_rule_warn,
                                    MonitorConstant.host_flag);
                            sendTotalEmail(MonitorConstant.monitor_rule_fixtime, MonitorConstant.monitor_rule_warn,
                                    MonitorConstant.server_flag);
                            sendTotalEmail(MonitorConstant.monitor_rule_fixtime, MonitorConstant.monitor_rule_warn,
                                    MonitorConstant.host_flag);
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("告警信息定时任务出错:" + e.getMessage());
                        }
                    }
                }, warn_cal.getTime(), PERIOD_DAY);
            }
            // 实时任务
            if (warn_notify_type == timing && warn_on_off == yes) {
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            // 发送错误邮件
                            sendServerErrorEmail(MonitorConstant.monitor_rule_warn);// 服务器
                            sendHostErrorEmail(MonitorConstant.monitor_rule_warn);// 云主机
                            // 发送恢复邮件
                            sendRecoverEmail(MonitorConstant.monitor_rule_warn, MonitorConstant.server_flag);
                            sendRecoverEmail(MonitorConstant.monitor_rule_warn, MonitorConstant.host_flag);
                        } catch (Exception e) {
                            e.printStackTrace();
                            logger.error("告警信息实时任务出错:" + e.getMessage());
                        }
                    }
                }, 0, WARN_PERIOD_MIN);
            }
        }

    }

    /**
     * Description:销毁
     * 
     * @param sce
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            sce.getServletContext().log("取消定时更新gw监控数据任务");
            timer.cancel();
        } catch (Exception e) {
        }
    }

    /**
     * @throws IOException
     * @throws MalformedURLException
     * @Description:更新内存数据
     */
    public void updateMemoryData() throws Exception {
        // 区域信息
        MonitorConstant.nameMap.put(regionDataID + "", regionDataName);
        HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(regionDataID);
        HttpGatewayAsyncChannel channel_asy = HttpGatewayManager.getAsyncChannel(regionDataID);
        /*
         * // 获取默认计算资源池 JSONObject defaultComputePool = null; try { defaultComputePool =
         * channel.getDefaultComputePool(); } catch (Exception e) { logger.error(e.getMessage()); continue; } if
         * (defaultComputePool == null) {// 不能获取，则跳过该region continue; }
         */
        // 从http gateway获取所有的机房信息
        JSONObject roomQueryResult = channel.serverRoomQuery();
        if (HttpGatewayResponseHelper.isSuccess(roomQueryResult) == false) {// 失败则跳过该region
            return;
        }
        JSONArray rooms = (JSONArray) roomQueryResult.get("server_rooms");
        // 获取机架信息
        for (int i = 0; i < rooms.size(); i++) {
            JSONObject room = (JSONObject) rooms.get(i);
            // 将上级ID放入JSON，用于页面查询
            room.put("areaid", regionDataID);
            // 更新屏蔽信息
            MonitorConstant.nameMap.put(JSONLibUtil.getString(room, "uuid"), JSONLibUtil.getString(room, "name"));
            if (MonitorConstant.shieldmap.get(room.get("uuid")) == null) {
                room.put(MonitorConstant.shield, MonitorConstant.shield_on);
                MonitorConstant.shieldmap.put(JSONLibUtil.getString(room, "uuid"), MonitorConstant.shield_on);
            } else {
                room.put(MonitorConstant.shield, MonitorConstant.shieldmap.get(room.get("uuid")));
            }

            JSONObject rackQueryResult = channel.serverRackQuery(JSONLibUtil.getString(room, "uuid"));
            if (HttpGatewayResponseHelper.isSuccess(rackQueryResult) == false) {// 失败则跳过该room
                return;
            }
            JSONArray racks = (JSONArray) rackQueryResult.get("server_racks");
            // 获取服务器信息
            for (int j = 0; j < racks.size(); j++) {
                JSONObject rack = (JSONObject) racks.get(j);
                // 将上级ID放入JSON，用于页面查询
                rack.put("areaid", regionDataID);
                rack.put("roomid", JSONLibUtil.getString(room, "uuid"));
                // 更新屏蔽信息
                MonitorConstant.nameMap.put(JSONLibUtil.getString(rack, "uuid"), JSONLibUtil.getString(rack, "name"));
                if (MonitorConstant.shieldmap.get(rack.get("uuid")) == null) {
                    rack.put(MonitorConstant.shield, MonitorConstant.shield_on);
                    MonitorConstant.shieldmap.put(JSONLibUtil.getString(rack, "uuid"), MonitorConstant.shield_on);
                } else {
                    rack.put(MonitorConstant.shield, MonitorConstant.shieldmap.get(rack.get("uuid")));
                }
                JSONObject serverQueryResult = channel.serverQuery(JSONLibUtil.getString(rack, "uuid"));
                if (HttpGatewayResponseHelper.isSuccess(serverQueryResult) == false) {// 失败则跳过该rack
                    continue;
                }
                JSONArray servers = (JSONArray) serverQueryResult.get("servers");
                // 循环服务器
                for (int k = 0; k < servers.size(); k++) {
                    JSONObject server = (JSONObject) servers.get(k);
                    
                    // 启动服务器监控
                    if (!monitorFlag && MonitorConstant.gw_status_normal.equals(server.get("status") + "")
                            && !MonitorConstant.receiveData.containsKey(server.get("uuid"))) {
                        channel_asy.startMonitor(MonitorLevel.SERVER, new String[] {server.get("uuid") + "" });
                    }
                    // 将上级ID放入JSON，用于页面查询
                    server.put("areaid", regionDataID);
                    server.put("roomid", JSONLibUtil.getString(room, "uuid"));
                    server.put("rackid", JSONLibUtil.getString(rack, "uuid"));
                    // 更新屏蔽信息
                    MonitorConstant.nameMap.put(JSONLibUtil.getString(server, "uuid"),
                            JSONLibUtil.getString(server, "name"));
                    if (MonitorConstant.shieldmap.get(server.get("uuid")) == null) {
                        server.put(MonitorConstant.shield, MonitorConstant.shield_on);
                        MonitorConstant.shieldmap.put(JSONLibUtil.getString(server, "uuid"), MonitorConstant.shield_on);
                    } else {
                        server.put(MonitorConstant.shield, MonitorConstant.shieldmap.get(server.get("uuid")));
                    }
                    // 将服务器的ip和id对应关系关联起来，用于云主机查询
                    if (!"".equals(JSONLibUtil.getString(server, "ip"))
                            && !"".equals(JSONLibUtil.getString(server, "uuid"))) {
                        synchronized (MonitorConstant.serversIP) {
                            MonitorConstant.serversIP.put(JSONLibUtil.getString(server, "ip"),
                                    JSONLibUtil.getString(server, "uuid"));
                        }
                    }
                }
                // 将服务器数据存入常量 (由于其他地方有可能同时调用该变量，加入一个同步锁)
                synchronized (MonitorConstant.serverMap) {
                    MonitorConstant.serverMap.put(JSONLibUtil.getString(rack, "uuid"), servers);
                }
                servers = null;
            }
            // 将机架数据存入常量
            synchronized (MonitorConstant.rackMap) {
                MonitorConstant.rackMap.put(JSONLibUtil.getString(room, "uuid"), racks);
            }
            racks = null;
        }
        // 将机房数据存入常量
        synchronized (MonitorConstant.roomMap) {
            MonitorConstant.roomMap.put(regionDataID + "", rooms);
        }
        rooms = null;
    }

    /**
     * @throws Exception
     * @Description:初始化规则配置信息
     * @throws
     */
    public void initThresholdData(ServletContextEvent sce) throws Exception {
        try {
            /*
             * Document doc = new SAXReader().read(sce.getServletContext().getResource("/META-INF/thresholds.xml"));
             * Element root = doc.getRootElement(); Iterator<Element> thresholdIter = root.elementIterator("threshold");
             * while (thresholdIter != null && thresholdIter.hasNext()) { Element paramElement = thresholdIter.next();
             * String warnvalue = paramElement.attributeValue("warnvalue"); String errorvalue =
             * paramElement.attributeValue("errorvalue");
             * MonitorConstant.thresholdMap.put(paramElement.attributeValue("id"), warnvalue + "-" + errorvalue); }
             */
            // 初始化告警规则
            SysWarnRuleVO warnrule = sysWarnService.getRuleByCode(MonitorConstant.monitor_rule_warn);
            SysWarnRuleVO errorrule = sysWarnService.getRuleByCode(MonitorConstant.monitor_rule_error);
            MonitorConstant.rulevo.put(MonitorConstant.monitor_rule_warn, warnrule);
            MonitorConstant.rulevo.put(MonitorConstant.monitor_rule_error, errorrule);
            // 规则内容
            MonitorConstant.rule_values.put(MonitorConstant.monitor_rule_warn,
                    sysWarnService.getValueByRule(warnrule.getId()));
            MonitorConstant.rule_values.put(MonitorConstant.monitor_rule_error,
                    sysWarnService.getValueByRule(errorrule.getId()));
            // 通知类型(实时还是定时)
            warn_notify_type = warnrule.getRealtime();
            // 设置定时时间
            if (warn_notify_type == fixtime) {
                warn_cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(warnrule.getSendtime().split(":")[0]));
                warn_cal.set(Calendar.MINUTE, Integer.parseInt(warnrule.getSendtime().split(":")[1]));
                warn_cal.set(Calendar.SECOND, Integer.parseInt(warnrule.getSendtime().split(":")[2]));
            }
            error_notify_type = errorrule.getRealtime();
            // 设置定时时间
            if (error_notify_type == fixtime) {
                error_cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(errorrule.getSendtime().split(":")[0]));
                error_cal.set(Calendar.MINUTE, Integer.parseInt(errorrule.getSendtime().split(":")[1]));
                error_cal.set(Calendar.SECOND, Integer.parseInt(errorrule.getSendtime().split(":")[2]));
            }
            // 是否发送告警通知
            error_on_off = errorrule.getIsnotify();
            warn_on_off = warnrule.getIsnotify();
            // 定时任务间隔执行时间(采样频率)
            ERROR_PERIOD_MIN = Long.parseLong(errorrule.getFrequency()) * 1000;
            WARN_PERIOD_MIN = Long.parseLong(warnrule.getFrequency()) * 1000;
            // 初始化采样次数
            ERROR_COUNT = errorrule.getSampletime();
            WARN_COUNT = warnrule.getSampletime();

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    /**
     * @Description:取得需要发送的故障数组集合
     * @param code 标示
     * @return String[]
     */
    public List<String> getSendErrorArray(String code) {
        if (MonitorConstant.server_flag.equals(code)) {
            // 如果存在故障节点(服务器)
            if (MonitorConstant.server_map_error_count.size() > 0) {
                // 当前时间
                long time = System.currentTimeMillis();
                List<String> uuids = new ArrayList<String>();
                // 循环比较故障节点
                Iterator<String> it = MonitorConstant.server_map_error_count.keySet().iterator();
                while (it.hasNext()) {
                    String uuid = it.next();
                    // 如果没有达到设定的连续故障次数，则跳过
                    if (MonitorConstant.server_map_error_count.get(uuid) < ERROR_COUNT) {
                        continue;
                    }
                    Long lastTime = MonitorConstant.isContainUUID2(uuid, MonitorConstant.map_sendmail_time);
                    if (lastTime == 0L || lastTime == -1L) {
                        uuids.add(uuid);
                    } else {
                        // 与上次间隔一个小时，才加入发送列表
                        if (time - lastTime >= ERROR_TIME) {
                            uuids.add(uuid);
                        }
                    }
                }
                return uuids;
            }
        }
        if (MonitorConstant.host_flag.equals(code)) {
            // 如果存在故障节点(云主机)
            if (MonitorConstant.host_map_error_count.size() > 0) {
                // 当前时间
                long time = System.currentTimeMillis();
                List<String> uuids = new ArrayList<String>();
                // 循环比较故障节点
                Iterator<String> it = MonitorConstant.host_map_error_count.keySet().iterator();
                while (it.hasNext()) {
                    String uuid = it.next();
                    // 如果没有达到设定的连续故障次数，则跳过
                    if (MonitorConstant.host_map_error_count.get(uuid) < ERROR_COUNT) {
                        continue;
                    }
                    Long lastTime = MonitorConstant.isContainUUID2(uuid, MonitorConstant.map_sendmail_time);
                    if (lastTime == 0L || lastTime == -1L) {
                        uuids.add(uuid);
                    } else {
                        // 与上次间隔一个小时，才加入发送列表
                        if (time - lastTime >= ERROR_TIME) {
                            uuids.add(uuid);
                        }
                    }
                }
                return uuids;
            }
        }
        return null;
    }

    /**
     * @Description:取得需要发送的告警数组集合
     * @param code 标示
     * @return String[]
     */
    public List<String> getSendWarnArray(String code) {
        if (MonitorConstant.server_flag.equals(code)) {
            // 如果存在告警节点(服务器)
            if (MonitorConstant.server_map_warn_count.size() > 0) {
                // 当前时间
                long time = System.currentTimeMillis();
                List<String> uuids = new ArrayList<String>();
                // 循环比较告警节点
                Iterator<String> it = MonitorConstant.server_map_warn_count.keySet().iterator();
                while (it.hasNext()) {
                    String uuid = it.next();
                    // 如果没有达到设定的连续告警次数，则跳过
                    if (MonitorConstant.server_map_warn_count.get(uuid) < WARN_COUNT) {
                        continue;
                    }
                    Long lastTime = MonitorConstant.isContainUUID2(uuid, MonitorConstant.map_sendmail_time);
                    if (lastTime == 0L || lastTime == -1L) {
                        uuids.add(uuid);
                    } else {
                        // 与上次间隔一个小时，才加入发送列表
                        if (time - lastTime >= ERROR_TIME) {
                            uuids.add(uuid);
                        }
                    }
                }
                return uuids;
            }
        }
        if (MonitorConstant.host_flag.equals(code)) {
            // 如果存在告警节点(云主机)
            if (MonitorConstant.host_map_warn_count.size() > 0) {
                // 当前时间
                long time = System.currentTimeMillis();
                List<String> uuids = new ArrayList<String>();
                // 循环比较告警节点
                Iterator<String> it = MonitorConstant.host_map_warn_count.keySet().iterator();
                while (it.hasNext()) {
                    String uuid = it.next();
                    // 如果没有达到设定的连续告警次数，则跳过
                    if (MonitorConstant.host_map_warn_count.get(uuid) < WARN_COUNT) {
                        continue;
                    }
                    Long lastTime = MonitorConstant.isContainUUID2(uuid, MonitorConstant.map_sendmail_time);
                    if (lastTime == 0L || lastTime == -1L) {
                        uuids.add(uuid);
                    } else {
                        // 与上次间隔一个小时，才加入发送列表
                        if (time - lastTime >= ERROR_TIME) {
                            uuids.add(uuid);
                        }
                    }
                }
                return uuids;
            }
        }
        return null;
    }

    /**
     * @Description:发送服务器故障邮件
     * @throws
     */
    public void sendServerErrorEmail(String code) {
        List<String> array = null;
        if (MonitorConstant.monitor_rule_warn.equals(code)) {
            array = getSendWarnArray(MonitorConstant.server_flag);
        }
        if (MonitorConstant.monitor_rule_error.equals(code)) {
            array = getSendErrorArray(MonitorConstant.server_flag);
        }
        if (array != null && array.size() > 0) {
            // 使用邮件模板发送邮件
            try {
                EmailSendService mailSendService = MessageServiceManager.singleton().getMailService();
                Map<String, Object> parameter = new LinkedHashMap<>();
                parameter.put("content", createTableHtml(array));
                parameter.put("type", "服务器");
                mailSendService.sendMail(code, parameter);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("发送服务器故障邮件出错:" + e.getMessage());
            }
            try {
                SmsSendService smaSendService = MessageServiceManager.singleton().getSmsService();
                Map<String, Object> parameter = new LinkedHashMap<>();
                parameter.put("content", "【致云科技】管理员您好,云端在线 " + array.size() + "台服务器发生故障,请及时登录监控平台处理");
                smaSendService.sendSms(MonitorConstant.monitor_sm_notify, parameter);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("发送服务器故障短信出错:" + e.getMessage());
            }

            // 邮件发送以后更新发送时间
            long time = System.currentTimeMillis();
            for (int i = 0; i < array.size(); i++) {
                MonitorConstant.map_sendmail_time.put(array.get(i), time);
            }
        }
    }

    /**
     * @Description:发送云主机故障邮件
     * @throws
     */
    public void sendHostErrorEmail(String code) {
        List<String> array = null;
        if (MonitorConstant.monitor_rule_warn.equals(code)) {
            array = getSendWarnArray(MonitorConstant.host_flag);
        }
        if (MonitorConstant.monitor_rule_error.equals(code)) {
            array = getSendErrorArray(MonitorConstant.host_flag);
        }
        if (array != null && array.size() > 0) {
            // 使用邮件模板发送邮件
            try {
                EmailSendService mailSendService = MessageServiceManager.singleton().getMailService();
                Map<String, Object> parameter = new LinkedHashMap<>();
                parameter.put("content", createHostHtml(array));
                parameter.put("type", "云主机");
                mailSendService.sendMail(code, parameter);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("发送云主机故障邮件出错:" + e.getMessage());
            }
            
            try {
                SmsSendService smaSendService = MessageServiceManager.singleton().getSmsService();
                Map<String, Object> parameter = new LinkedHashMap<>();
                parameter.put("content", "【致云科技】管理员您好,云端在线 " + array.size() + "台云主机发生故障,请及时登录监控平台处理");
                smaSendService.sendSms(MonitorConstant.monitor_sm_notify, parameter);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("发送云主机故障短信出错:" + e.getMessage());
            }

            // 邮件发送以后更新发送时间
            long time = System.currentTimeMillis();
            for (int i = 0; i < array.size(); i++) {
                MonitorConstant.map_sendmail_time.put(array.get(i), time);
            }
        }
    }

    /**
     * @Description:发送恢复邮件
     * @throws
     */
    public void sendRecoverEmail(String code,String type) {
        List<String> list_recover = new LinkedList<String>();
        if (MonitorConstant.monitor_rule_warn.equals(code)) {
            if (MonitorConstant.server_flag.equals(type)) {
                list_recover = MonitorConstant.server_warn_list_recover;
            } else {
                list_recover = MonitorConstant.host_warn_list_recover;
            }

        }
        if (MonitorConstant.monitor_rule_error.equals(code)) {
            if (MonitorConstant.server_flag.equals(type)) {
                list_recover = MonitorConstant.server_error_list_recover;
            } else {
                list_recover = MonitorConstant.host_error_list_recover;
            }
        }
        if (list_recover.size() > 0) {
            // 使用邮件模板发送邮件
            try {
                EmailSendService mailSendService = MessageServiceManager.singleton().getMailService();
                Map<String, Object> parameter = new LinkedHashMap<>();
                if (MonitorConstant.server_flag.equals(type)) {
                    parameter.put("content", createTableHtml(list_recover));
                } else {
                    parameter.put("content", createHostHtml(list_recover));
                }
                mailSendService.sendMail(code, parameter);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("发送恢复邮件出错:" + e.getMessage());
            }
            // new SendMail().sendServerErrorInfo(0, createTableHtml(list_recover));
            try {
                SmsSendService smaSendService = MessageServiceManager.singleton().getSmsService();
                Map<String, Object> parameter = new LinkedHashMap<>();
                parameter.put("content", "【致云科技】管理员您好,，云端在线 " + list_recover.size() + "台设备已恢复正常");
                smaSendService.sendSms(MonitorConstant.monitor_sm_notify, parameter);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("发送设备恢复短信出错:" + e.getMessage());
            }

            // 发送恢复邮件以后，清空恢复列表
            if (MonitorConstant.monitor_rule_warn.equals(code)) {
                if (MonitorConstant.server_flag.equals(type)) {
                    MonitorConstant.server_warn_list_recover.removeAll(list_recover);
                } else {
                    MonitorConstant.host_warn_list_recover.removeAll(list_recover);
                }

            }
            if (MonitorConstant.monitor_rule_error.equals(code)) {
                if (MonitorConstant.server_flag.equals(type)) {
                    MonitorConstant.server_error_list_recover.removeAll(list_recover);
                } else {
                    MonitorConstant.host_error_list_recover.removeAll(list_recover);
                }
            }
        }
    }

    /**
     * @Description:发送当天监控数据汇总邮件
     * @param code 邮件模板编码
     * @param monitor_type 监控类型(故障OR告警)
     * @param server_type 服务类型(服务器OR云主机)
     */
    public void sendTotalEmail(String templatecode, String monitor_type, String server_type) {
        String currenttime = StringUtil.dateToString(new Date(), "yyyy-MM-dd");
        Map<String, Object> condition = new LinkedHashMap<String, Object>();
        condition.put("insert_date", currenttime);
        condition.put("monitor_type", monitor_type);
        condition.put("server_type", server_type);
        List<MonitorServerVO> lists = monitorService.getDataByDay(condition);
        if (lists != null && lists.size() > 0) {
            // 拼html
            StringBuffer sb = new StringBuffer();
            if(MonitorConstant.host_flag.equals(server_type)){
                sb.append("<table border=\"0\" cellpadding=\"1\"><tr><td style=\"width=200px;font-weight:bold;\">区域</td><td style=\"width=200px;font-weight:bold;\">机房</td><td style=\"width=200px;font-weight:bold;\">机架</td><td style=\"width=200px;font-weight:bold;\">服务器</td>"
                        + "<td style=\"width=200px;font-weight:bold;\">云主机</td>"
                        + "<td style=\"width=150px;font-weight:bold;\">CPU利用率</td>"
                        + "<td style=\"width=150px;font-weight:bold;\">内存利用率</td>"
                        + "<td style=\"width=150px;font-weight:bold;\">磁盘利用率</td></tr>");
            }else{
                sb.append("<table border=\"0\" cellpadding=\"1\"><tr><td style=\"width=200px;font-weight:bold;\">区域</td><td style=\"width=200px;font-weight:bold;\">机房</td><td style=\"width=200px;font-weight:bold;\">机架</td><td style=\"width=200px;font-weight:bold;\">服务器</td>"
                        + "<td style=\"width=150px;font-weight:bold;\">CPU利用率</td>"
                        + "<td style=\"width=150px;font-weight:bold;\">内存利用率</td>"
                        + "<td style=\"width=150px;font-weight:bold;\">磁盘利用率</td></tr>");
            }
            for (int i = 0; i < lists.size(); i++) {
                MonitorServerVO monitor = lists.get(i);
                if (lists.get(i) == null || "null".equals(lists.get(i)))
                    continue;
                sb.append("<tr><td>");
                // 区域名
                sb.append(MonitorConstant.nameMap.get(monitor.getAreaid()));
                sb.append("</td><td>");
                // 机房名
                sb.append(MonitorConstant.nameMap.get(monitor.getRoomid()));
                sb.append("</td><td>");
                // 机架名
                sb.append(MonitorConstant.nameMap.get(monitor.getRackid()));
                sb.append("</td><td>");
                // 如果对象是云主机,那么serverid是hostid,需要重新计算
                if (MonitorConstant.host_flag.equals(server_type)) {
                    // 根据host的uuid,从内存中读取host对象
                    JSONObject host = MonitorConstant.getObjectByUUID(monitor.getServerid(), MonitorConstant.hostsMap);
                    // 获取服务器IP
                    String serverip = JSONLibUtil.getStringArray(host, "ip")[0];
                    String serverid = MonitorConstant.serversIP.get(serverip);
                    // 服务器名
                    sb.append(MonitorConstant.nameMap.get(serverid));
                    sb.append("</td><td>");
                    // 云主机名
                    sb.append(MonitorConstant.nameMap.get(host.getString("uuid")));
                    sb.append("</td><td>");
                } else {
                    // 服务器名
                    sb.append(MonitorConstant.nameMap.get(monitor.getServerid()));
                    sb.append("</td><td>");
                }
                // CPU利用率
                sb.append(MonitorConstant.convertDoubleToString(monitor.getCpu_usage()));
                sb.append("</td><td>");
                // 内存利用率
                sb.append(MonitorConstant.convertDoubleToString(monitor.getMemory_usage()));
                sb.append("</td><td>");
                // 磁盘利用率
                sb.append(MonitorConstant.convertDoubleToString(monitor.getDisk_usage()));
                sb.append("</td></tr>");
            }
            sb.append("</table>");
            // 使用邮件模板发送邮件
            try {
                EmailSendService mailSendService = MessageServiceManager.singleton().getMailService();
                Map<String, Object> parameter = new LinkedHashMap<>();
                if (MonitorConstant.server_flag.equals(server_type)) {
                    parameter.put("type", "服务器");
                } else {
                    parameter.put("type", "云主机");
                }
                parameter.put("content", sb.toString());
                mailSendService.sendMail(templatecode, parameter);
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("发送汇总邮件出错:" + e.getMessage());
            }
            // new SendMail().sendServerErrorInfo(2, sb.toString());
        }
    }

    /**
     * @Description:根据集合构造table的html
     * @param array 集合
     * @return html
     */
    public String createTableHtml(List<String> array) {
        if (array == null || array.size() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        sb.append("<table border=\"0\" cellpadding=\"1\"><tr><td style=\"width=200px;font-weight:bold;\">区域</td><td style=\"width=200px;font-weight:bold;\">机房</td><td style=\"width=200px;font-weight:bold;\">机架</td><td style=\"width=200px;font-weight:bold;\">服务器</td><td style=\"width=200px;font-weight:bold;\">服务器IP</td></tr>");
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == null || "null".equals(array.get(i)))
                continue;
            sb.append("<tr><td>");
            // 根据服务器获取机架
            String rackid = MonitorConstant.getParentID(array.get(i), MonitorConstant.serverMap);
            // 根据机架获取机房
            String roomid = MonitorConstant.getParentID(rackid, MonitorConstant.rackMap);
            // 根据机房获取区域
            String areaid = MonitorConstant.getParentID(roomid, MonitorConstant.roomMap);
            // 区域名
            sb.append(MonitorConstant.nameMap.get(areaid));
            sb.append("</td><td>");
            // 机房名
            sb.append(MonitorConstant.nameMap.get(roomid));
            sb.append("</td><td>");
            // 机架名
            sb.append(MonitorConstant.nameMap.get(rackid));
            sb.append("</td><td>");
            // 服务器名
            sb.append(MonitorConstant.nameMap.get(array.get(i)));
            sb.append("</td><td>");
            JSONObject server = MonitorConstant.getObjectByUUID(array.get(i), MonitorConstant.serverMap);
            // 服务器IP
            sb.append(server.get("ip"));
            sb.append("</td></tr>");           
            
        }
        sb.append("</table>");
        return sb.toString();
    }

    /**
     * @Description:根据集合构造table的html
     * @param array 集合
     * @return html
     */
    public String createHostHtml(List<String> array) {
        if (array == null || array.size() == 0)
            return "";
        String serverip = "";
        String serverid = "";
        StringBuffer sb = new StringBuffer();
        sb.append("<table border=\"0\" cellpadding=\"1\"><tr><td style=\"width=200px;font-weight:bold;\">区域</td><td style=\"width=200px;font-weight:bold;\">机房</td><td style=\"width=200px;font-weight:bold;\">机架</td><td style=\"width=200px;font-weight:bold;\">服务器</td><td style=\"width=200px;font-weight:bold;\">云主机</td><td style=\"width=200px;font-weight:bold;\">云主机IP</td></tr>");
        for (int i = 0; i < array.size(); i++) {
            if (array.get(i) == null || "null".equals(array.get(i)))
                continue;
            sb.append("<tr><td>");
            JSONObject host = MonitorConstant.getObjectByUUID(array.get(i), MonitorConstant.hostsMap);
            if (host == null || host.isEmpty()) {
                continue;
            }
            // 获取服务器ID
            serverip = JSONLibUtil.getStringArray(host, "ip")[0];
            serverid = MonitorConstant.serversIP.get(serverip);
            // 根据服务器获取机架
            String rackid = MonitorConstant.getParentID(serverid, MonitorConstant.serverMap);
            // 根据机架获取机房
            String roomid = MonitorConstant.getParentID(rackid, MonitorConstant.rackMap);
            // 根据机房获取区域
            String areaid = MonitorConstant.getParentID(roomid, MonitorConstant.roomMap);
            // 区域名
            sb.append(MonitorConstant.nameMap.get(areaid));
            sb.append("</td><td>");
            // 机房名
            sb.append(MonitorConstant.nameMap.get(roomid));
            sb.append("</td><td>");
            // 机架名
            sb.append(MonitorConstant.nameMap.get(rackid));
            sb.append("</td><td>");
            // 服务器名
            sb.append(MonitorConstant.nameMap.get(serverid));
            sb.append("</td><td>");
            // 云主机名
            sb.append(MonitorConstant.nameMap.get(array.get(i)));
            sb.append("</td><td>");
            // 云主机IP
            sb.append(JSONLibUtil.getStringArray(host, "ip")[1]);
            sb.append("</td></tr>");
        }
        sb.append("</table>");
        return sb.toString();
    }
    
    /**
     * @Description:保存监控数据到临时表
     * @param server_type 服务器类型
     * @param monitor_type 监控类型 故障还是告警
     * @param server
     */
    public void saveTempData(JSONObject server, String server_type, String monitor_type) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        if (server != null) {
            map.put("serverid", JSONLibUtil.getString(server, "uuid"));
            map.put("cpu_usage", MonitorConstant.convertStringToDouble(JSONLibUtil.getString(server, "cpu_usage")));
            map.put("memory_usage",
                    MonitorConstant.convertStringToDouble(JSONLibUtil.getString(server, "memory_usage")));
            map.put("disk_usage", MonitorConstant.convertStringToDouble(JSONLibUtil.getString(server, "disk_usage")));
            map.put("server_type", server_type);
            map.put("monitor_type", monitor_type);
            monitorService.saveMonitorTempData(map);
        }
    }

    /**
     * @Description:采样服务器告警数据
     * @throws
     */
    public void putServerWarnInfo() {
        // 存在写操作，加入同步锁
        synchronized (MonitorConstant.serverMap) {
            Map<String, JSONArray> map = MonitorConstant.serverMap;
            if (map != null && !map.isEmpty()) {
                Iterator<String> its = map.keySet().iterator();
                while (its.hasNext()) {
                    String keyid = its.next();
                    JSONArray array = map.get(keyid);
                    for (Object object : array) {
                        JSONObject server = (JSONObject) object;
                        if (MonitorConstant.status_warn.equals(MonitorConstant.judgeThresholdUseRule(server,
                                MonitorConstant.server_flag))) {
                            Integer num = MonitorConstant.isContainUUID(JSONLibUtil.getString(server, "uuid"),
                                    MonitorConstant.server_map_warn_count);
                            // 保存server临时数据
                            if (warn_notify_type == fixtime && error_on_off == yes) {
                                saveTempData(server, MonitorConstant.server_flag, MonitorConstant.monitor_rule_warn);
                            }
                            // 如果服务器处于警告状态,则加入警告的集合里面
                            if (num >= 0) {
                                num++;
                            }
                            // 防止溢出
                            if (num > Integer.MAX_VALUE / 2) {
                                num = WARN_COUNT;
                            }
                            MonitorConstant.server_map_warn_count.put(JSONLibUtil.getString(server, "uuid"), num);
                        }
                        if (MonitorConstant.status_normal.equals(MonitorConstant.judgeThresholdUseRule(server,
                                MonitorConstant.server_flag))) {
                            Integer num = MonitorConstant.isContainUUID(JSONLibUtil.getString(server, "uuid"),
                                    MonitorConstant.server_map_warn_count);
                            // 如果服务器正常了，则从警告集合中移除
                            if (num > 0) {
                                MonitorConstant.server_warn_list_recover.add(JSONLibUtil.getString(server, "uuid"));
                                MonitorConstant.removeElement(JSONLibUtil.getString(server, "uuid"),
                                        MonitorConstant.server_map_warn_count);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @Description:采样云主机告警数据
     * @throws
     */
    public void putHostWarnInfo() {
        // 存在写操作，加入同步锁
        synchronized (MonitorConstant.hostsMap) {
            Map<String, JSONArray> map = MonitorConstant.hostsMap;
            if (map != null && !map.isEmpty()) {
                Iterator<String> its = map.keySet().iterator();
                while (its.hasNext()) {
                    String keyid = its.next();
                    JSONArray array = map.get(keyid);
                    for (Object object : array) {
                        JSONObject host = (JSONObject) object;
                        if (MonitorConstant.status_warn.equals(MonitorConstant.judgeThresholdUseRule(host,
                                MonitorConstant.host_flag))) {
                            Integer num = MonitorConstant.isContainUUID(JSONLibUtil.getString(host, "uuid"),
                                    MonitorConstant.host_map_warn_count);
                            // 保存host临时数据
                            if (warn_notify_type == fixtime && error_on_off == yes) {
                                saveTempData(host, MonitorConstant.host_flag, MonitorConstant.monitor_rule_warn);
                            }
                            // 如果服务器处于警告状态,则加入警告的集合里面
                            if (num >= 0) {
                                num++;
                            }
                            // 防止溢出
                            if (num > Integer.MAX_VALUE / 2) {
                                num = WARN_COUNT;
                            }
                            MonitorConstant.host_map_warn_count.put(JSONLibUtil.getString(host, "uuid"), num);
                        }
                        if (MonitorConstant.status_normal.equals(MonitorConstant.judgeThresholdUseRule(host,
                                MonitorConstant.host_flag))) {
                            Integer num = MonitorConstant.isContainUUID(JSONLibUtil.getString(host, "uuid"),
                                    MonitorConstant.host_map_warn_count);
                            // 如果服务器正常了，则从警告集合中移除
                            if (num > 0) {
                                MonitorConstant.host_warn_list_recover.add(JSONLibUtil.getString(host, "uuid"));
                                MonitorConstant.removeElement(JSONLibUtil.getString(host, "uuid"),
                                        MonitorConstant.host_map_warn_count);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @Description:采集服务器故障数据
     * @throws
     */
    public void putServerErrorInfo() {
        // 存在写操作，加入同步锁
        synchronized (MonitorConstant.serverMap) {
            Map<String, JSONArray> map = MonitorConstant.serverMap;
            if (map != null && !map.isEmpty()) {
                Iterator<String> its = map.keySet().iterator();
                while (its.hasNext()) {
                    String keyid = its.next();
                    JSONArray array = map.get(keyid);
                    for (Object object : array) {
                        JSONObject server = (JSONObject) object;
                        if (MonitorConstant.status_error.equals(MonitorConstant.judgeThresholdUseRule(server,
                                MonitorConstant.server_flag))) {
                            Integer num = MonitorConstant.isContainUUID(JSONLibUtil.getString(server, "uuid"),
                                    MonitorConstant.server_map_error_count);
                            // 保存server临时数据
                            if (error_notify_type == fixtime && error_on_off == yes) {
                                saveTempData(server, MonitorConstant.server_flag, MonitorConstant.monitor_rule_error);
                            }
                            // 如果服务器处于故障状态,则加入故障的集合里面
                            if (num >= 0) {
                                num++;
                            }
                            // 防止溢出
                            if (num > Integer.MAX_VALUE / 2) {
                                num = ERROR_COUNT;
                            }
                            MonitorConstant.server_map_error_count.put(JSONLibUtil.getString(server, "uuid"), num);
                        }
                        if (MonitorConstant.status_normal.equals(MonitorConstant.judgeThresholdUseRule(server,
                                MonitorConstant.server_flag))) {
                            Integer num = MonitorConstant.isContainUUID(JSONLibUtil.getString(server, "uuid"),
                                    MonitorConstant.server_map_error_count);
                            // 如果服务器正常了，则从故障集合中移除
                            if (num > 0) {
                                MonitorConstant.server_error_list_recover.add(JSONLibUtil.getString(server, "uuid"));
                                MonitorConstant.removeElement(JSONLibUtil.getString(server, "uuid"),
                                        MonitorConstant.server_map_error_count);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @Description:采集云主机故障数据
     * @throws
     */
    public void putHostErrorInfo() {
        // 存在写操作，加入同步锁
        synchronized (MonitorConstant.hostsMap) {
            Map<String, JSONArray> map = MonitorConstant.hostsMap;
            if (map != null && !map.isEmpty()) {
                Iterator<String> its = map.keySet().iterator();
                while (its.hasNext()) {
                    String keyid = its.next();
                    JSONArray array = map.get(keyid);
                    for (Object object : array) {
                        JSONObject host = (JSONObject) object;
                        if (MonitorConstant.status_error.equals(MonitorConstant.judgeThresholdUseRule(host,
                                MonitorConstant.host_flag))) {
                            Integer num = MonitorConstant.isContainUUID(JSONLibUtil.getString(host, "uuid"),
                                    MonitorConstant.host_map_error_count);
                            // 保存host临时数据
                            if (error_notify_type == fixtime && error_on_off == yes) {
                                saveTempData(host, MonitorConstant.host_flag, MonitorConstant.monitor_rule_error);
                            }
                            // 如果云主机处于故障状态,则加入故障的集合里面
                            if (num >= 0) {
                                num++;
                            }
                            // 防止溢出
                            if (num > Integer.MAX_VALUE / 2) {
                                num = ERROR_COUNT;
                            }
                            MonitorConstant.host_map_error_count.put(JSONLibUtil.getString(host, "uuid"), num);
                        }
                        if (MonitorConstant.status_normal.equals(MonitorConstant.judgeThresholdUseRule(host,
                                MonitorConstant.host_flag))) {
                            Integer num = MonitorConstant.isContainUUID(JSONLibUtil.getString(host, "uuid"),
                                    MonitorConstant.host_map_error_count);
                            // 如果云主机正常了，则从故障集合中移除
                            if (num > 0) {
                                MonitorConstant.host_error_list_recover.add(JSONLibUtil.getString(host, "uuid"));
                                MonitorConstant.removeElement(JSONLibUtil.getString(host, "uuid"),
                                        MonitorConstant.host_map_error_count);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @throws IOException
     * @throws MalformedURLException
     * @Description:将云主机信息放入内存
     * @throws
     */
    public void putHostsIntoMemory() throws MalformedURLException, IOException {
        // 区域信息
        // 广州暂时跳过不处理
        HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(regionDataID);
        HttpGatewayAsyncChannel channel_asy = HttpGatewayManager.getAsyncChannel(regionDataID);
        try {
            // 获取该区域所有的资源池
            JSONObject object = channel.computePoolQuery();
            //add by chenjinhua
        	if ("fail".equals(object.getString("status"))) {
        		  logger.error("从资源池获取主机列表出错:" + object.getString("message"));
        		  return;
        	}
            
            JSONArray pools = object.getJSONArray("compute_pools");
            if (pools != null && pools.size() > 0) {
                for (int i = 0; i < pools.size(); i++) {
                    // 单个资源池
                    JSONObject pool = pools.getJSONObject(i); 
                    JSONObject object_host = channel.hostQuery(pool.getString("uuid"));
                    // 将主机信息+资源池ID存入内存常量
                    JSONArray hosts;
                    try {
                        hosts = object_host.getJSONArray("hosts");
                    } catch (Exception e) {
                        logger.error("从资源池获取主机列表出错:" + e.getMessage());
                        continue;
                    }
                    if (hosts != null && hosts.size() > 0) {
                        // 循环云主机信息，放入区域和资源池信息
                        for (int j = 0; j < hosts.size(); j++) {
                            JSONObject host = hosts.getJSONObject(j);
                            // 启动云主机监控
                            if (!monitorFlag && MonitorConstant.gw_status_normal.equals(host.get("status") + "")
                                    && !MonitorConstant.receiveData.containsKey(host.get("uuid"))) {
                                channel_asy.startMonitor(MonitorLevel.HOST, new String[] {host.get("uuid") + "" });
                            }
                            JSONObject host_info = channel.hostQueryInfo(host.get("uuid") + "").getJSONObject("host");
                            host.put("poolid", pool.getString("uuid"));
                            host.put("areaid", regionDataID);
                            host.put("area_name", regionDataName);
                            if (host_info != null && !host_info.isEmpty()) {
                                host.put("port", host_info.get("port"));// 主机端口映射
                                host.put("user", host_info.get("user"));// 所属用户标签
                                host.put("group", host_info.get("group"));// 所属用户组标签
                                host.put("display", host_info.get("display"));// 监控验证用户名
                                host.put("authentication", host_info.get("authentication"));// 监控验证密码
                                host.put("inbound_bandwidth", host_info.get("inbound_bandwidth"));// 入口带宽
                                host.put("outbound_bandwidth", host_info.get("outbound_bandwidth"));// 入口带宽
                                host.put("network_type", host_info.get("network_type"));// 网络地址类型
                                host.put("disk_type", host_info.get("disk_type"));// 磁盘模式
                            }

                            MonitorConstant.nameMap.put(JSONLibUtil.getString(host, "uuid"),
                                    JSONLibUtil.getString(host, "name"));
                            if (MonitorConstant.shieldmap.get(host.get("uuid")) == null) {
                                host.put(MonitorConstant.shield, MonitorConstant.shield_on);
                                MonitorConstant.shieldmap.put(JSONLibUtil.getString(host, "uuid"),
                                        MonitorConstant.shield_on);
                            } else {
                                host.put(MonitorConstant.shield, MonitorConstant.shieldmap.get(host.get("uuid")));
                            }
                        }
                    }
                    synchronized (MonitorConstant.hostsMap) {
                        MonitorConstant.hostsMap.put(pool.getString("uuid") + "∮" + pool.getString("name") + "∮"
                                + regionDataName + "∮" + regionDataID, hosts);
                    }
                    pool = null;
                    object_host = null;
                    hosts = null;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("更新云主机信息到内存出错：" + e.getMessage());
            return;
        }
    }
    
    /**
     * @Description:停止所有监控
     * @throws
     */
    public void stopAllMonitor() {
        // 区域信息
        try {
                HttpGatewayAsyncChannel channel_asy = HttpGatewayManager.getAsyncChannel(regionDataID);
                Iterator<String> its = MonitorConstant.monitorData.keySet().iterator();
                while (its.hasNext()) {
                    channel_asy.stopMonitor(MonitorConstant.monitorData.get(its.next()));
            }
        } catch (Exception e) {
            logger.error("停止监控出错:" + e.getMessage());
        }
    }
    
    /**
     * @Description:启动所有服务器监控(状态正常的才启动)
     * @throws
     */
    public void startServerMonitor() {
        try {
                HttpGatewayAsyncChannel channel_asy = HttpGatewayManager.getAsyncChannel(regionDataID);
                String[] array = MonitorConstant.getServerObjects(regionDataID);
                channel_asy.startMonitor(MonitorLevel.SERVER, array);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("启动服务器监控出错:" + e.getMessage());
        }
    }
    
    /**
     * @Description:启动所有云主机监控(状态正常的才启动)
     * @throws
     */
    public void startHostsMonitor() {
        try {
                HttpGatewayAsyncChannel channel_asy = HttpGatewayManager.getAsyncChannel(regionDataID);
                String[] array = MonitorConstant.getHostObjects(regionDataID);
                channel_asy.startMonitor(MonitorLevel.HOST, array);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("启动云主机监控出错:" + e.getMessage());
        }
    }
}
