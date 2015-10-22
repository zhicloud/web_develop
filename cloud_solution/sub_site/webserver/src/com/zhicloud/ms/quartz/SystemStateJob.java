package com.zhicloud.ms.quartz;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhicloud.ms.service.StateService;
import com.zhicloud.ms.service.ResourceStatisticsService;
import com.zhicloud.ms.vo.ResUsageVO;

public class SystemStateJob implements Job  {
    private final static Logger logger = Logger.getLogger(SystemStateJob.class);
    BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml");
    StateService stateService = (StateService)factory.getBean("stateService");
    ResourceStatisticsService resourceStatisticsService = (ResourceStatisticsService)factory.getBean("resourceStatisticsService");
    @Resource
    private SqlSession sqlSession;
    
    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        logger.debug("SystemStateJob.execute()");

        ResUsageVO resUsageVO  = stateService.queryUsage();
        try{
            SimpleDateFormat sdf1 =   new SimpleDateFormat( "yyyyMMddHHmmss");
            SimpleDateFormat sdf2 =   new SimpleDateFormat( "yyyy-MM-dd");
            Calendar rightNow = Calendar.getInstance();
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("cpu_usage", new BigDecimal(resUsageVO.getCpuper()));
            map.put("memory_usage", new BigDecimal(resUsageVO.getMemper()));
            map.put("disk_usage", new BigDecimal(resUsageVO.getDiskper()));
            map.put("Throughput", new BigDecimal(resUsageVO.getData()));
            map.put("create_time", sdf1.format(rightNow.getTime()));
            map.put("data_date", sdf2.format(rightNow.getTime()));
            map.put("data_hours", rightNow.get(Calendar.HOUR_OF_DAY));
            resourceStatisticsService.addResourceStatistics(map);
        }catch(Exception ex){
            logger.error("SystemStateJob exception:" + ex.getMessage());
        }        
    }

}
