package com.zhicloud.ms.app.listener;

import java.io.IOException;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.quartz.QuartzManage;
import com.zhicloud.ms.quartz.WarehouseJob;
import com.zhicloud.ms.service.ICloudHostWarehouseService;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.CloudHostWarehouse;

public class WarehouseCheckTimeListener implements Servlet {

	private final static Logger logger = Logger.getLogger(WarehouseCheckTimeListener.class);
	BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
	ICloudHostWarehouseService cloudHostWarehouseService = (ICloudHostWarehouseService)factory.getBean("cloudHostWarehouseService");
	@Override
	public void init(ServletConfig config) throws ServletException {
		logger.info("start scheduler");
		try {
			QuartzManage.getQuartzManage().getScheduler().start();
			//查询所有仓库
			List<CloudHostWarehouse> warehouseList = cloudHostWarehouseService.getAll();
			if(warehouseList!=null && warehouseList.size()>0){
				for(CloudHostWarehouse chw : warehouseList){
					if(chw.getPoolId()==null || StringUtil.isBlank(chw.getPoolId())){
						continue;
					}
					if(chw.getMinimum()==0){
						continue;
					}
					if(chw.getCheckTime()==null || chw.getCheckTime().length()!=6){
						continue;
					}
					String hour = chw.getCheckTime().substring(0, 2);
					String min = chw.getCheckTime().substring(2, 4);
					String sec = chw.getCheckTime().substring(4);
					String checkTiem = sec+" "+min+" "+hour+" * * ? *";
					//定义任务
    				JobDetail jd = JobBuilder.newJob(WarehouseJob.singleton().getClass())
    						.withIdentity(new JobKey(chw.getId(),"groupJob"))
    						.usingJobData("id", chw.getId())
    						.requestRecovery(true)
    						.build();
    				//定义触发器
    				CronTrigger ct = (CronTrigger)TriggerBuilder.newTrigger()
    						.withIdentity(new TriggerKey(chw.getId(),"groupTrigger"))
    						.withSchedule(CronScheduleBuilder.cronSchedule(checkTiem).withMisfireHandlingInstructionDoNothing())
    						.startNow()
    						.build();
    				//添加任务
    				QuartzManage.getQuartzManage().addTrigger(jd, ct);
				}
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void destroy() {
		logger.info("destory all the service fetcher listers");
	}

	@Override
	public ServletConfig getServletConfig() {
		throw new AppException("unsupported method");
	}

	@Override
	public String getServletInfo() {
		throw new AppException("unsupported method");
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		throw new AppException("unsupported method");
	}

}
