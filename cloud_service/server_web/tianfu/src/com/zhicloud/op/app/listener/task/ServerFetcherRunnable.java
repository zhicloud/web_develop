package com.zhicloud.op.app.listener.task;

import java.math.BigInteger;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.op.app.helper.RegionHelper;
import com.zhicloud.op.app.helper.RegionHelper.RegionData;
import com.zhicloud.op.app.pool.serverInfoPool.ServerInfoExt;
import com.zhicloud.op.app.pool.serverInfoPool.ServerInfoPool;
import com.zhicloud.op.app.pool.serverInfoPool.ServerInfoPoolManager;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;

public class ServerFetcherRunnable implements Runnable {

	private final static Logger logger = Logger.getLogger(ServerFetcherRunnable.class);
	private int fetchServerIntervalTime = 10;// 单位：s
	private boolean running = true;

	public ServerFetcherRunnable(int fetchServerIntervalTime) {
		this.fetchServerIntervalTime = fetchServerIntervalTime;
	}

	public synchronized void stop() {
		running = false;
		this.notifyAll();
	}

	public synchronized boolean isRunning() {
		return running;
	}

	private synchronized void waitSelf() {
		try {
			this.wait(this.fetchServerIntervalTime * 1000);
		} catch (InterruptedException e) {
			logger.warn("the server fetcher thread is interrupted when waiting.");
		}
	}

	@Override
	public void run() {
		logger.info("start to fetch server information.");

		while (true) {
			// 是否在运行
			if (this.isRunning() == false) {
				break;
			}

			RegionData[] regions = RegionHelper.singleton.getAllResions();
			for (RegionData region : regions) {
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(region.getId());
				try {
					JSONObject rack = channel.getDefaultServerRackFromDefaultServerRoom();
					if (rack == null) {// 没有查询到default rack
						logger.warn(String.format("can not get the default server rack in the default server room from region[%s]", region.getName()));
						continue;
					}

					String rackUuid = rack.getString("uuid");
					if (rackUuid == null || rackUuid.trim().length() == 0) {
						logger.warn(String.format("the default rack from region[%s] has no uuid.", region.getName()));
						continue;
					}

					JSONObject result = channel.serverQuery(rackUuid);
					if (HttpGatewayResponseHelper.isSuccess(result) == false) {
						logger.warn(String.format("fail to fetch server list in the default rack from region[%s]", region.getName()));
						continue;
					}

					List<JSONObject> servers = (List<JSONObject>) result.get("servers");
					ServerInfoPool pool = ServerInfoPoolManager.singleton().getPool();

					for (JSONObject serv : servers) {
						String name = serv.getString("name");
						String uuid = serv.getString("uuid");
						int status = serv.getInt("status");
						int cpuCount = serv.getInt("cpu_count");
						double cpuUsage = serv.getDouble("cpu_usage");
						BigInteger[] memoryArray = JSONLibUtil.getBigIntegerArray(serv, "memory");
						double memoryUsage = serv.getDouble("memory_usage");
						BigInteger[] diskVolumeArray = JSONLibUtil.getBigIntegerArray(serv, "disk_volume");
						double diskUsage = serv.getDouble("disk_usage");
						String ip = serv.getString("ip");

						ServerInfoExt serverInfo = pool.getServer(uuid);
						if (serverInfo == null) {
							serverInfo = new ServerInfoExt();
						}
						synchronized (serverInfo) {// 同步处理,保证数据一致，防止数据覆盖
							serverInfo.setRegion(region.getId());
							serverInfo.setName(name);
							serverInfo.setUuid(uuid);
							serverInfo.setStatus(status);
							serverInfo.setCpuCount(cpuCount);
							serverInfo.setCpuUsage(cpuUsage);
							serverInfo.setMemory(memoryArray[1]);
							serverInfo.setMemoryUsage(memoryUsage);
							serverInfo.setDiskVolume(diskVolumeArray[1]);
							serverInfo.setDiskUsage(diskUsage);
							serverInfo.setIp(ip);
							serverInfo.updateTime();
						}

						pool.putServer(serverInfo);
					}
				} catch (Exception e) {
					logger.error(String.format("error occur when fetching server from region[%s]. exception[%s]", region.getName(), e.getMessage()));
				}
			}

			this.waitSelf();
		}

		logger.warn("finish to fetch server information.");
	}

}
