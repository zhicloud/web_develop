
package com.zhicloud.op.app.listener;

import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.SendMail;
import com.zhicloud.op.common.util.SendSms;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.sql.*;
import java.util.Timer; 
import java.util.TimerTask;
 
/**
 * @ClassName: 定时检测数据库是否连通
 * @Description: Description of this class
 * @author 张本缘 于 2015年4月29日 上午10:51:23
 */
public class CheckDatabaseConnectListener implements ServletContextListener {
    // 调度控制器
    private Timer timer = new Timer();
    public static ServletContext servletContext;
    // 通知的间隔时间,默认为一小时
    private static final long PERIOD_HOUR = 60 * 60 * 1000;
    // 定时任务间隔执行时间
    private static final long PERIOD_MIN = 60 * 1000;
    // 数据库连接超时记录次数
    private static int count = 0;
    // 允许数据库连接不上或者超时的次数
    private static int allow_count = 3;
    // 是否已经发送过故障通知标示
    // private static boolean error_flag = false;
    // 是否已经发送过恢复通知
    private static boolean succ_flag = true;
    // 信息发送时间  
    private static long info_send_time = 0;
    // 数据库连接路径
    private static String url = "jdbc:mysql://127.0.0.1:3306/zcop?useUnicode=true&amp;characterEncoding=utf8&amp;autoReconnect=true&amp;autoReconnectForPools=true";

    private static String username = "zcop";
    private static String password = "zcop$250";
    private static String drivername = "com.mysql.jdbc.Driver";
    public static final Logger logger = Logger.getLogger(CheckDatabaseConnectListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // 记录任务开始时间
        // info_send_time = System.currentTimeMillis();
        sce.getServletContext().log("检测数据库连接定时器已启动");
        // 是否发送故障通知
        final String notification_on_off = AppProperties.getValue("notification_on_off", "yes");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // 检测
                checkConnect();
                if (count == 0 && !succ_flag) {
                    if ("yes".equals(notification_on_off)) {
                        sendInfomation(true);
                    }
                    // 数据库恢复连接以后，将成功标示置为true
                    succ_flag = true;
                }
                // 数据库多次连接不上或者连接超时
                logger.info("连接超时次数:"+count+"，允许超时次数:"+allow_count);
                logger.info("是否允许发送通知邮件和短信:"+notification_on_off);
                logger.info("是否已经发送过恢复通知："+succ_flag);
                if (count == allow_count) {
                    // 距离上次发送故障邮件的时间
                    long cost_time = System.currentTimeMillis() - info_send_time;
                    logger.info("距离上次发送故障邮件时间:"+cost_time+",间隔时间："+PERIOD_HOUR);
                    if (cost_time >= PERIOD_HOUR || succ_flag) {
                        if ("yes".equals(notification_on_off)) {
                            sendInfomation(false);
                        }
                        // 数据库连接不上或者超时以后，将成功标示置为false
                        succ_flag = false;
                    }
                    // 如果已经发送过故障邮件，则将count重置为0
                    count = 0;
                }
            }
        }, 0, PERIOD_MIN);

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            sce.getServletContext().log("取消检测数据库连接的定时任务");
            timer.cancel();
        } catch (Exception e) {
        }
    }

    /**
     * @Description:检测数据库是否可以连通
     * @return boolean
     * @throws SQLException
     */
    public void checkConnect() {
        Connection conn = null;
        Statement st = null;
        ResultSet rs = null;
        // 检测数据库是否连通
        try {
            logger.info("开始检测数据库是否连通,(URL:"+url+"),(username:"+username+"),(password:"+password+")");
            Class.forName(drivername);
            conn = DriverManager.getConnection(url, username, password);
            st = conn.createStatement();
            rs = st.executeQuery("select 1 from dual");
            // 如果没有获取到数据,记连接超时一次
            if (!rs.next()) {
                count++;
            } else {
                count = 0;
            }
        } catch (Exception e) {
            // 如果没有获取到数据库连接，也认为数据库连接不上，做相同处理
            if (conn == null) {
                count++;
            }
            logger.info("数据库连接超时");
        } finally {
            closeConn(rs, st, conn);
        }

    }

    /**
     * @Description:关闭数据库连接
     * @param rs
     * @param st
     * @param conn
     */
    @Transactional(readOnly = false)
    public void closeConn(ResultSet rs, Statement st, Connection conn) {
        try {
            if (rs != null) {// 如果返回的结果集对象不能为空,就关闭连接
                rs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (st != null) {
                st.close();// 关闭预编译对象
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                conn.close();// 关闭结果集对象
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        rs = null;
        st = null;
        conn = null;
    }

    /**
     * @Description:发送数据库连接信息
     * @param flag 如果为true则标示数据库正常，为false则故障
     */
    @Transactional(readOnly = false)
    public void sendInfomation(boolean flag) {
        logger.info("发送短信和邮件"+flag);
        info_send_time = System.currentTimeMillis();
        String info = "", status = "";
        if (!flag) {
            info = "数据库不能连接或者连接超时";
            status = "1";
        } else {
            info = "数据库连接";
            status = "2";
        }
        // 发送邮件和短信
        String notification_on_off = AppProperties.getValue("notification_on_off","yes");
        if("yes".equals(notification_on_off)){
            new SendMail().sendConnectInfo(flag);
            // 发送短信
            new SendSms().sendHttpGatewayDetail(info, status, AppProperties.getValue(("monitor_phone1")));
            new SendSms().sendHttpGatewayDetail(info, status, AppProperties.getValue(("monitor_phone2")));
            new SendSms().sendHttpGatewayDetail(info, status, AppProperties.getValue(("monitor_phone3")));
        }
        
       
    }

}
