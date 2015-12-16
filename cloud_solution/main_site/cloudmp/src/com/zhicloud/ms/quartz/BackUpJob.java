package com.zhicloud.ms.quartz;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException; 
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhicloud.ms.app.pool.host.back.HostBackupProgressData;
import com.zhicloud.ms.app.pool.host.back.HostBackupProgressPool;
import com.zhicloud.ms.app.pool.host.back.HostBackupProgressPoolManager;
import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.service.IBackUpDetailService;
import com.zhicloud.ms.service.ICloudHostService;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.CloudHostVO;
/**
 * 
* @ClassName: BackUpJob 
* @Description: 定时备份回调 
* @author sasa
* @date 2015年7月28日 下午3:19:13 
*
 */
public class BackUpJob implements Job {
    
    private static BackUpJob instance = null;

//    BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
    private static ICloudHostService cloudHostService = null;
    private static IBackUpDetailService backUpDetailService = null;
    private final static Logger logger = Logger.getLogger(BackUpJob.class);
    private final static int MAX_BACK_UP_COUNT = 10;
    
    public  static BackUpJob singleton() {
        if (BackUpJob.instance == null) {
            BackUpJob.instance = new BackUpJob();
            BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
             cloudHostService = (ICloudHostService)factory.getBean("cloudHostService");
             backUpDetailService = (IBackUpDetailService)factory.getBean("backUpDetailService");
        }
        return BackUpJob.instance;
    }
    
    @Override
    public void execute(JobExecutionContext context)throws JobExecutionException {
        String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
        Integer mode = context.getJobDetail().getJobDataMap().getIntValue("mode");
        Integer disk = context.getJobDetail().getJobDataMap().getIntValue("disk");
        String timerKey = context.getJobDetail().getJobDataMap().getString("timerKey");

        AppInconstant.isTimerForBackUp = true;
        try{
          //获取所有开机状态的加入定时任务的主机，并进行强制关机
            List<CloudHostVO> startList =  cloudHostService.getCloudHostInTimerBackUpStart(timerKey);          
            for(CloudHostVO host : startList){                
                cloudHostService.operatorCloudHost(host.getId(), "4");
            }
        }catch(Exception e){
            logger.error(e);
        }
        
        while(AppInconstant.isTimerForBackUp)
        {
            try
            {
                //获取正在备份的主机总数
                int backUpCount = AppInconstant.hostBackupProgress.size();
                //当前正在备份的主机数已经达到最大值，跳出循环
                if(backUpCount >= MAX_BACK_UP_COUNT){
                    continue;
                }else{
                    int limit = MAX_BACK_UP_COUNT - backUpCount;
                    List<CloudHostVO> hostList = cloudHostService.getCloudHostInTimerBackUpStop(limit, now,timerKey);
                    if(hostList == null || hostList.size() == 0){
                        AppInconstant.isTimerForBackUp = false;
                        continue;
                    }
                    for(CloudHostVO host : hostList){
                        //循环发送备份命令
                        String sessionId = null;
                        Map<String, Object> parameter = new LinkedHashMap<String, Object>();
                        
                        
                        parameter.put("command", "backup");
                        parameter.put("uuid", host.getRealHostId());
                        parameter.put("mode", mode);
                        parameter.put("disk", disk);
                        
                        try{
                            sessionId = this.hostBackup(parameter);
                            if( sessionId == null )
                            {
                                cloudHostService.updateHostBackUpTimeInTimer(host.getId(), StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                                backUpDetailService.insertDetail(host.getRealHostId(), AppConstant.BACK_UP_DETAIL_STATUS_FAIL, 0, mode, disk);

                                logger.warn("BackUpJob.excute() > ["+Thread.currentThread().getId()+"] query host backup failed,host["+host.getRealHostId()+"] name ["+host.getDisplayName()+"]");
                                continue;
                            }
                            
                            HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool(); 
                            HostBackupProgressData hostBackup = new HostBackupProgressData();
                                              
                            hostBackup.setSessionId(sessionId);
                            hostBackup.setUuid(host.getRealHostId());
                            hostBackup.updateTime();
                            hostBackup.setBackupStatus(9);
                            hostBackup.setType(1); //1表示是定时任务中的主机，用于后期判断是否应该加入历史记录中
                            pool.put(hostBackup);  
                            //更新最新的主机备份时间，避免重复备份
                            cloudHostService.updateHostBackUpTimeInTimer(host.getId(), StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                            //新增正在备份的记录
                            backUpDetailService.insertDetail(host.getRealHostId(), AppConstant.BACK_UP_DETAIL_STATUS_BACKINGUP, 0, mode, disk);
                            // 将当前可用版本设置成不可用
                            backUpDetailService.updateDetail(host.getRealHostId(), AppConstant.BACK_UP_DETAIL_STATUS_SUCCESS, AppConstant.BACK_UP_DETAIL_STATUS_ANOTHOR);
                         } catch (Exception e) {
                             cloudHostService.updateHostBackUpTimeInTimer(host.getId(), StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                             backUpDetailService.insertDetail(host.getRealHostId(), AppConstant.BACK_UP_DETAIL_STATUS_FAIL, 0, mode, disk);
                            System.err.println("fail to send backup host request.");
                            e.printStackTrace();
                         }
                    }
                }
              
            }
            finally
            {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }
    
    /**
     * 
    * @Title: hostBackup 
    * @Description: 发送备份云主机命令
    * @param @param parameter
    * @param @return
    * @param @throws Exception      
    * @return String     
    * @throws
     */
    //连接http gateway
    private String hostBackup(Map<String, Object> parameter) throws Exception{
        
        String uuid = (String) parameter.get("uuid");
        Integer region = 1;
        String command = (String) parameter.get("command");
        
        
        HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
        
        if ("backup".equalsIgnoreCase(command)) {
            this.operatorHostBeforeBackup(uuid, region);
            Integer mode = Integer.valueOf(String.valueOf(parameter.get("mode")));
            Integer disk = Integer.valueOf(String.valueOf(parameter.get("disk")));
            
            try {
                JSONObject result = channel.hostBackup(uuid, mode, disk);
                if (HttpGatewayResponseHelper.isSuccess(result) == true) {
                    System.err.println("success to send backup host request."); 
                    return channel.getSessionId();
                } else {
                    System.err.println("fail to send backup host request.");
                    channel.release();
                }
            } catch (Exception e) {
                System.err.println("fail to send backup host request.");
                channel.release();
                throw e;
            } 
        } else if ("resume".equalsIgnoreCase(command)) {
            Integer mode = Integer.valueOf(String.valueOf(parameter.get("mode")));
            Integer disk = Integer.valueOf(String.valueOf(parameter.get("disk")));
            try {
                JSONObject result = channel.hostResume(uuid, mode, disk);
                if (HttpGatewayResponseHelper.isSuccess(result) == true) {
                    System.err.println("success to send resume host request.");
                    return channel.getSessionId();
                } else {
                    System.err.println("fail to send resume host request.");
                    channel.release();
                }
            } catch (Exception e) {
                System.err.println("fail to send resume host request.");
                channel.release();
                throw e;
            }
        }else if ("query".equalsIgnoreCase(command)) {
                try {
                    JSONObject result = channel.hostQueryBackup(uuid);
                    if (HttpGatewayResponseHelper.isSuccess(result) == true) {
                        System.err.println("success to send query host backup request.");
                        return channel.getSessionId();
                    } else {
                        System.err.println("fail to send query host backup request.");
                        channel.release();
                    }
                } catch (Exception e) {
                    System.err.println("fail to send query host backup request.");
                    channel.release();
                    throw e;
                }
            }
            return null;
    }
    
    /**
     * 
    * @Title: operatorHostBeforeBackup 
    * @Description: 备份前查询主机是否有备份权限，没有添加上 
    * @param @param realHostId
    * @param @param region      
    * @return void     
    * @throws
     */
    private void operatorHostBeforeBackup(String realHostId,Integer region){
        // 从http gateway获取云主机的详细信息
        HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(region);
        try { 
            
            JSONObject hostResult = channel.hostQueryInfo(realHostId);
            JSONObject hostInfo = (JSONObject) hostResult.get("host");
            Integer[] option = JSONLibUtil.getIntegerArray(hostInfo, "option");
            if(option.length>3){
                if(option[2] == 0){
                    channel.hostModify(realHostId, "", 0, BigInteger.ZERO, new Integer[]{0, 1, 1}, new Integer[]{1,200}, "", "", "", BigInteger.ZERO, BigInteger.ZERO);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
