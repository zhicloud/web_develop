package com.zhicloud.ms.app.listener.task;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.ms.balancer.BalancerHelper;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;

public class LoadBalanceRunnable implements Runnable {

	private final static Logger logger = Logger.getLogger(LoadBalanceRunnable.class);
	//主机真实ID
	private String realHostId;
	//内网IP
	private String innerIp;
	private int timeOutTimes = 0;
	public LoadBalanceRunnable(String realHostId,String innerIp) {
		this.realHostId = realHostId;
		this.innerIp = innerIp;
	}
	@Override
	public void run() {
		logger.info("Start deal with load balancer info");
		while (true) {
			HttpGatewayAsyncChannel channel = null;
			try {
				channel = HttpGatewayManager.getAsyncChannel(new Integer(1));
				JSONObject result = channel.networkQuery();
				if (HttpGatewayResponseHelper.isSuccess(result) == false) {
					channel.release();
				}
				JSONObject jObj = new JSONObject();
				jObj.put("ip", innerIp);
				jObj.put("port", "50000");
				jObj.put("session_id", "");
				//先不处理，直接退出
				return;
				//修改配置信息
				//TO DO SOMETHING
				//查询服务是否启动
				//BalancerHelper.queryBalancerSummary(jObj);
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
