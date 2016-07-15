package com.zhicloud.ms.app.listener;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
 


import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener; 

import com.zhicloud.ms.thread.LogFileManager;
 
/**
 * 时间监听器
 * 
 * @author xiaoqun.yi
 */
public class LogFileListener implements ServletContextListener {
    private Timer timer = new Timer();
    public static ServletContext servletContext;
 
    public void contextInitialized(ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();
        try {
 
            // 第一个参数是要运行的代码，第二个参数是从什么时候开始运行，第三个参数是每隔多久在运行一次。 
            sce.getServletContext().log("定时器已启动");
            // 设置在每晚24:0分执行任务
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 11); // 24 ,可以更改时间
            calendar.set(Calendar.MINUTE, 12); // 0可以更改分数
            calendar.set(Calendar.SECOND, 0);// 0 //默认为0,不以秒计
            Date date = calendar.getTime();
            // 监听器获取网站的根目录 
            
            String path = sce.getServletContext().getRealPath("/"); 
            //获取日志路径
             path = System.getProperty("catalina.home")+"/logs";
            // 第一个参数 是要运行的代码，第二个参数是指定的时间
            timer.schedule(new SystemTaskTest(path), date);
            sce.getServletContext().log("已经添加任务调度表");
 
        } catch (Exception e) {
        }
    }
 
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            timer.cancel();
        } catch (Exception e) {
        }
    }
}
 
/**
 * 时间任务器
 * 
 * @author xiaoqun.yi
 */
class SystemTaskTest extends TimerTask {
    private String path;
 
    public SystemTaskTest(String path) {
        this.path = path;
    }
 
    /**
     * 把要定时执行的任务就在run中
     */
    public void run() {
        try { 
            LogFileManager etf = new LogFileManager(path);
            etf.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
