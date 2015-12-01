package com.zhicloud.ms.app.listener.task;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
  
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhicloud.ms.balancer.BalancerHelper;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.service.ILoadBalanceService;
import com.zhicloud.ms.vo.BalancerForwardVO;

public class LoadBalanceRunnable implements Runnable {

	private final static Logger logger = Logger.getLogger(LoadBalanceRunnable.class);
	//主机真实ID
	private String realHostId;
	//内网IP
	private String innerIp;
	private ILoadBalanceService loadBalanceService;
	
	private int timeOutTimes = 0;
	public LoadBalanceRunnable(String realHostId,String innerIp) {
		this.realHostId = realHostId;
		this.innerIp = innerIp;
		BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml");  
		loadBalanceService = (ILoadBalanceService)factory.getBean("loadBalance"); 
	}
	@Override
	public void run() {
		logger.info("Start deal with load balancer info");
		while (true) {
			try {
				JSONObject jObj = new JSONObject();
				jObj.put("ip", innerIp);
				jObj.put("port", "50000");
				jObj.put("session_id", "");
				//查询服务是否启动
				JSONObject result = BalancerHelper.queryBalancerSummary(jObj);
				JSONArray listenPort = new JSONArray();
				
				if("success".equals(result.get("status"))){
					BalancerForwardVO balancer = loadBalanceService.getBalanceForwardByHostId(realHostId);
				}
			} catch (Exception e) {
				//判断连接超时次数，每次超时后间隔一分钟再发请求，连续5次超时则放弃操作。
				if(timeOutTimes<=4){
					timeOutTimes++;
					logger.warn(String.format("Connect load balancer timeout times[%s]", timeOutTimes));
					try {
						Thread.sleep(1000*60);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					return;
				}else{
					logger.warn("Fail to connect load balancer,innerIp["+innerIp+"]");
					return;
				}
			}
		}
	}
}
