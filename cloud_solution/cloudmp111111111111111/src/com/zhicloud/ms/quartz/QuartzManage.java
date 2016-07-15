package com.zhicloud.ms.quartz;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;

public class QuartzManage {
	private static Scheduler sche;
	private static QuartzManage qm;
	private QuartzManage(){
		
	}
	
	public synchronized static QuartzManage getQuartzManage(){
		if(qm == null){
			qm = new QuartzManage();
		}
		return qm;
	}	
	
	public Scheduler getScheduler(){
		if(sche==null){
			try {
				sche = new StdSchedulerFactory().getScheduler();
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		}
		return sche;
	}
	
	/**
	 * 判断触发器是否存在
	 * @param name
	 * @param group
	 * @return
	 */
	public Trigger queryTrigger(String name,String group){
		TriggerKey tk = new TriggerKey(name,group);
		try {
			return getScheduler().getTrigger(tk);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 添加任务
	 * @param trigger
	 */
	public void addTrigger(JobDetail jd,Trigger trigger){
		try {
			getScheduler().scheduleJob(jd,trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 更新任务
	 * @param name
	 * @param group
	 * @param trigger
	 */
	public void updateTrigger(TriggerKey oldTriggerKey,Trigger newTrigger){
		try {
			getScheduler().rescheduleJob(oldTriggerKey, newTrigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * 删除任务
	 * @param jobKey
	 * @param triggerKey
	 */
	public void deleteTrigger(JobKey jobKey,TriggerKey triggerKey){
		try {
			getScheduler().pauseTrigger(triggerKey);
			getScheduler().unscheduleJob(triggerKey);
			getScheduler().deleteJob(jobKey);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
}
