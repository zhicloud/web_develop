package com.zhicloud.ms.quartz;

import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudHostService;
import com.zhicloud.ms.service.ISetTimeOperationDetailService;
import com.zhicloud.ms.vo.CloudHostVO;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Iterator;
import java.util.List;

/**
 * Created by sean on 8/6/15.
 */
public class OperationJob implements Job {

    BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml");
    ICloudHostService cloudHostService = (ICloudHostService)factory.getBean("cloudHostService");
    ISetTimeOperationDetailService setTimeOperationDetailService =
        (ISetTimeOperationDetailService) factory.getBean("setTimeOperationDetailService");
    private final static Logger logger = Logger.getLogger(OperationJob.class);

    @Override public void execute(JobExecutionContext jobExecutionContext)
        throws JobExecutionException {
        try {

            //获取定时器类型
            String key1 = jobExecutionContext.getJobDetail().getJobDataMap().getString("key");
            String key2 = jobExecutionContext.getTrigger().getJobDataMap().getString("key");
            String key = key1 != null? key1:key2;


            List<CloudHostVO> cloudHostVOs = cloudHostService.getCloudHostInTimer(key);
            Iterator<CloudHostVO> it = cloudHostVOs.iterator();
            while (it.hasNext()) {
                CloudHostVO cloudHostVO = it.next();
                Integer runningStatus = cloudHostVO.getRunningStatus();
                String id = cloudHostVO.getId();
                MethodResult result = null;
                switch (key) {
                    //执行开机操作
                    case AppConstant.STARTUP_TIMER: {
                        //若关机则发送开机命令
                        if (AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN == runningStatus) {
                            result = cloudHostService.operatorCloudHost(id, "1");
                            if (MethodResult.SUCCESS.equals(result.status)) {
                                setTimeOperationDetailService
                                    .insertDetail(cloudHostVO.getRealHostId(), AppConstant.RESULT_SUCCESS,
                                        AppConstant.MESSAGE_TYPE_STARTUP);
                            } else {
                                setTimeOperationDetailService
                                    .insertDetail(cloudHostVO.getRealHostId(), AppConstant.RESULT_FAIL,
                                        AppConstant.MESSAGE_TYPE_STARTUP);
                            }
                        }
                        //若开机则直接写入操作记录
                        if (AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING == runningStatus) {
                            setTimeOperationDetailService
                                .insertDetail(cloudHostVO.getRealHostId(), AppConstant.RESULT_SUCCESS,
                                    AppConstant.MESSAGE_TYPE_STARTUP);
                        }
                        break;
                    }
                    //执行关机操作
                    case AppConstant.SHUTDOWN_TIMER: {
                        if (AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING == runningStatus) {
                            result = cloudHostService.operatorCloudHost(id, "2");
                            if (MethodResult.SUCCESS.equals(result.status)) {
                                setTimeOperationDetailService
                                    .insertDetail(cloudHostVO.getRealHostId(), AppConstant.RESULT_SUCCESS,
                                        AppConstant.MESSAGE_TYPE_SHUTDOWN);
                            } else {
                                setTimeOperationDetailService
                                    .insertDetail(cloudHostVO.getRealHostId(), AppConstant.RESULT_FAIL,
                                        AppConstant.MESSAGE_TYPE_SHUTDOWN);
                            }
                        }
                        //若开机则直接写入操作记录
                        if (AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN == runningStatus) {
                            setTimeOperationDetailService
                                .insertDetail(cloudHostVO.getRealHostId(), AppConstant.RESULT_SUCCESS,
                                    AppConstant.MESSAGE_TYPE_SHUTDOWN);
                        }
                        break;
                    }
                }

            }
        }catch (Exception e) {
            logger.error("定时操作异常");
            e.printStackTrace();
        }

    }
}
