package com.zhicloud.ms.quartz;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhicloud.ms.app.pool.CloudHostPoolManager;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudHostService;

public class CloudHostSynchronousJob implements Job{
    private static CloudHostSynchronousJob instance = null;
     private static ICloudHostService cloudHostService = null;
     private final static Logger logger = Logger.getLogger(BackUpJob.class);
     public synchronized static CloudHostSynchronousJob singleton() {
         if (instance == null) {
             instance = new CloudHostSynchronousJob();
             BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
             cloudHostService = (ICloudHostService)factory.getBean("cloudHostService");

         }
         return instance;
     }
     @Override
     public void execute(JobExecutionContext context)throws JobExecutionException { 
         try { 
             // 初始化云主机池
             if (CloudHostPoolManager.getSingleton().isInitialized() == false) {
                 CloudHostPoolManager.getSingleton().initCloudHostPool();
             }
             // 同步每个region的云主机列表  
             MethodResult result = cloudHostService.fetchNewestCloudHostFromHttpGateway();
             // 判断是否发送云主机列表更新事件
             boolean update = false;
             if ("fail".equals(result.status)) {
                 Object hostListUpdate = null;
             }
             Object hostListUpdate = result.get("host_list_update");
             if (hostListUpdate != null && ((Boolean) hostListUpdate) == Boolean.TRUE) {
                 update = true;
             }
             logger.info(String.format("fetch cloud host end.host list update[%s].", update));
//             if (update) {// 发送云主机列表更新事件
//                 handler.handleHostListUpdateEvent();
//             }
         } catch (Exception e) {
             logger.error("fail to fetch host list from http gateway", e);
         } 
         
      }

}
