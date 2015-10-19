
package com.zhicloud.ms.transform.util;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @ClassName: TransFormCheckUserListener
 * @Description: 定时检测后台和前台的连通情况
 * @author 张本缘 于 2015年6月10日 上午9:15:55
 */
public class TransFormCheckUserListener implements ServletContextListener {
    // 10秒 换算成毫秒常量
    private static final long time = 1000 * 20;
    // 定时执行时间
    private static final long period_time = 1000 * 20;
    // 调度控制器
    private Timer timer = new Timer();

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        try {
            timer.cancel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        try {
            arg0.getServletContext().log("定时检测用户session是否处于活动状态已启动");
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    check();
                }
            }, 0, period_time);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @Description:定时检测用户是否处于活动状态
     */
    public void check() {
        // 服务器当前时间
        long currenttime = System.currentTimeMillis();
        for (Map.Entry<String, TransFormLoginInfo> map : TransFormLoginHelper.sessionMap.entrySet()) {
            TransFormLoginInfo info = (TransFormLoginInfo) map.getValue();
            if (info!=null&&currenttime != 0 &&info.getLastupdatetime() > 0 && (currenttime - info.getLastupdatetime()) > time) {
                System.out.println("当前秒数："+currenttime+" 用户最近时间:"+info.getLastupdatetime() +" 相差："+(currenttime - info.getLastupdatetime()));
                TransFormLoginHelper.removeSessionMap(map.getKey());
            }
        }
    }

}
