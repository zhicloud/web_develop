package com.zhicloud.ms.quartz;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException; 
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhicloud.ms.app.listener.WarehouseCheckTimeListener;
 import com.zhicloud.ms.service.ICloudHostWarehouseService;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.CloudHostWarehouse;

public class WarehouseJob implements Job{
    private static  WarehouseJob instance = null;
	BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
	ICloudHostWarehouseService cloudHostWarehouseService = (ICloudHostWarehouseService)factory.getBean("cloudHostWarehouseService");
	private final static Logger logger = Logger.getLogger(WarehouseCheckTimeListener.class);
	public synchronized static WarehouseJob singleton() {
        if (WarehouseJob.instance == null) {
            WarehouseJob.instance = new WarehouseJob();
        }
        return WarehouseJob.instance;
	 }
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		String id = context.getJobDetail().getJobDataMap().getString("id");
		CloudHostWarehouse chw = cloudHostWarehouseService.getById(id);
		if(chw==null){
			return;
		}
		Integer minimumI = chw.getMinimum();
		Integer remainI = chw.getRemainAmount();
		String poolId = chw.getPoolId();
		//如果资源池ID为null或空值则不做任何处理
		if(poolId==null || StringUtil.isBlank(poolId)){
			return;
		}
		if(minimumI - remainI > 0){
			logger.info("check warehouse remain");
			cloudHostWarehouseService.addAmount(id, String.valueOf((minimumI-remainI)), poolId,"yes");
		}
	}

}
