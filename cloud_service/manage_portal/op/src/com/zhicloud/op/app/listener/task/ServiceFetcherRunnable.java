package com.zhicloud.op.app.listener.task;

import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.op.app.helper.RegionHelper;
import com.zhicloud.op.app.helper.RegionHelper.RegionData;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoExt;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoPool;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoPoolManager;
import com.zhicloud.op.common.util.constant.NodeTypeDefine;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;

public class ServiceFetcherRunnable implements Runnable {

	private final static Logger logger = Logger.getLogger(ServiceFetcherRunnable.class);
	private int node = NodeTypeDefine.INVALID;
	private String group = "default";
	private int intervalTime = 10;// 单位：s
	private boolean running = true;

	public ServiceFetcherRunnable(int node, String group, int intervalTime) {
		this.node = node;
		this.group = group;
		this.intervalTime = intervalTime;
	}

	public synchronized void stop() {
		this.running = false;
		this.notifyAll();
	}

	public synchronized boolean isRunning() {
		return running;
	}

	private synchronized void waitSelf() {
		try {
			this.wait(this.intervalTime * 1000);
		} catch (InterruptedException e) {
			logger.warn("the service fetcher thread is interrupted when waiting.");
		}
	}

	@Override
	public void run() {
		logger.info(String.format("start to fetch service information. node_type[%s], group[%s], interval_time[%s]", this.node, this.group, this.intervalTime));

		while (this.isRunning()) {
			RegionData[] regions = RegionHelper.singleton.getAllResions();

			for (RegionData region : regions) {// 遍历所有区域的http_gateway
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(region.getId());
				try {
					JSONObject result = channel.serviceQuery(node, group);
					if (HttpGatewayResponseHelper.isSuccess(result) == false) {
						logger.warn(String.format("fail to fetch service information. node_type[%s], group[%s], region[%s]", this.node, this.group, region.getId()));
						continue;
					}

					// 返回数据处理
					List<JSONObject> services = (List<JSONObject>) result.get("services");
					ServiceInfoPool pool = ServiceInfoPoolManager.singleton().getPool();

					for (JSONObject serv : services) {
						String name = serv.getString("name");
						String ip = serv.getString("ip");
						int port = serv.getInt("port");
						int status = serv.getInt("status");
						String version = serv.getString("version");

						ServiceInfoExt serviceInfo = pool.get(name);
						if (serviceInfo == null) {
							serviceInfo = new ServiceInfoExt();
						}
						synchronized (serviceInfo) {// 同步处理,保证数据一致，防止数据覆盖
							serviceInfo.setRegion(region.getId());
							serviceInfo.setType(this.node);
							serviceInfo.setGroup(this.group);
							serviceInfo.setName(name);
							serviceInfo.setIp(ip);
							serviceInfo.setPort(port);
							serviceInfo.setStatus(status);
							serviceInfo.setVersion(version);
							serviceInfo.updateTime();
						}

						pool.put(serviceInfo);
					}
				} catch (Exception e) {
					logger.error(String.format("error occur when fetching service from region[%s]. node_type[%s], group[%s], exception[%s]", region.getId(), this.node, this.group, e.getMessage()));
				}
			}

			this.waitSelf();
		}

		logger.info(String.format("finish fetch service information. node_type[%s], group[%s], interval_time[%s]", this.node, this.group, this.intervalTime));
	}

}
