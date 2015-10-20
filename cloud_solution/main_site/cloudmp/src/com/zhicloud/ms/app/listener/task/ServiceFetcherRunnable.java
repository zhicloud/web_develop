package com.zhicloud.ms.app.listener.task;

import com.zhicloud.ms.app.pool.serviceInfoPool.ServiceInfoExt;
import com.zhicloud.ms.app.pool.serviceInfoPool.ServiceInfoPool;
import com.zhicloud.ms.app.pool.serviceInfoPool.ServiceInfoPoolManager;
import com.zhicloud.ms.common.util.constant.NodeTypeDefine;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.util.List;

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
//			RegionData[] regions = RegionHelper.singleton.getAllResions();

//			for (RegionData region : regions) {// 遍历所有区域的http_gateway
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
				try {
					JSONObject result = channel.serviceQuery(node, group);
					if (HttpGatewayResponseHelper.isSuccess(result) == false) {
						logger.warn(String.format("fail to fetch service information. node_type[%s], group[%s], region[%s]", this.node, this.group, 1));
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
            int diskType = serv.getInt("disk_type");

						ServiceInfoExt serviceInfo = pool.get(name);
						if (serviceInfo == null) {
							serviceInfo = new ServiceInfoExt();
						}
						synchronized (serviceInfo) {// 同步处理,保证数据一致，防止数据覆盖
							serviceInfo.setRegion(1);
							serviceInfo.setType(this.node);
							serviceInfo.setGroup(this.group);
							serviceInfo.setName(name);
							serviceInfo.setIp(ip);
							serviceInfo.setPort(port);
							serviceInfo.setStatus(status);
							serviceInfo.setVersion(version);
              serviceInfo.setDiskType(diskType);
							serviceInfo.updateTime();
							
						}

						pool.put(serviceInfo);
					}
				} catch (Exception e) {
					logger.error(String.format("error occur when fetching service from region[%s]. node_type[%s], group[%s], exception[%s]", 1, this.node, this.group, e.getMessage()));
				}
//			}

			this.waitSelf();
		}

		logger.info(String.format("finish fetch service information. node_type[%s], group[%s], interval_time[%s]", this.node, this.group, this.intervalTime));
	}

}
