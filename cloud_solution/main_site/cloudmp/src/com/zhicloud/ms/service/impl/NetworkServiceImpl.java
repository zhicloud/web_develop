package com.zhicloud.ms.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.ms.app.pool.network.NetworkCreateInfoPool;
import com.zhicloud.ms.app.pool.network.NetworkInfoExt;
import com.zhicloud.ms.app.pool.network.NetworkInfoExt.Host;
import com.zhicloud.ms.app.pool.network.NetworkInfoExt.Port;
import com.zhicloud.ms.app.pool.network.NetworkInfoPool;
import com.zhicloud.ms.app.pool.network.NetworkInfoSessionPool;
import com.zhicloud.ms.app.pool.network.NetworkPoolManager;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.service.INetworkService;

public class NetworkServiceImpl implements INetworkService {

	private final static Logger logger = Logger.getLogger(NetworkServiceImpl.class);

	// 异步创建vpc，返回参数：消息发送成功与否
	public boolean createAsync(Integer region, String name, int netmask, String description, String pool) throws MalformedURLException, IOException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
		JSONObject result = null;

		try {
			result = channel.networkCreate(name, netmask, description, pool);
		} catch (Exception e) {
			logger.error(String.format("fail to create network. exception[%s]", e));
			channel.release();
			throw e;
		}

		if (HttpGatewayResponseHelper.isSuccess(result) == true) {
			NetworkInfoExt network = new NetworkInfoExt();
			network.setRegionId(region);
			network.setName(name);
			network.setNetmask(netmask);
			network.setDescription(description);
			network.setIpPool(pool);
			network.setSessionId(channel.getSessionId());
			network.setMessage("");
			network.updateTime();

			network.initAsyncStatus();

			NetworkCreateInfoPool infoPool = NetworkPoolManager.singleton().getCreateInfoPool();
			infoPool.put(network);

			return true;
		} else {
			logger.warn(String.format("fail to create network. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
			channel.release();
			return false;
		}
	}

	// 获取异步创建vpc结果
	public NetworkInfoExt getCreateResult(Integer regionId, String name) {
		NetworkCreateInfoPool pool = NetworkPoolManager.singleton().getCreateInfoPool();

		return pool.getDuplication(regionId, name);
	}
	
	// 同步创建vpc，成功，则返回NetworkInfoExt对象；失败，则返回空对象
	public NetworkInfoExt createSync(Integer region, String name, int netmask, String description, String pool) throws MalformedURLException, IOException {
		boolean result = this.createAsync(region, name, netmask, description, pool);
		if (result) {
			NetworkCreateInfoPool infoPool = NetworkPoolManager.singleton().getCreateInfoPool();
			NetworkInfoExt network = infoPool.get(region, name);
			synchronized (network) {//wait for 5 second or notify by response message.
				try {
					network.wait(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("error occur when waiting create network response call back.", e);
				}
			}
			
			int status = network.getAsyncStatus();
			if (status == 1) {//成功
				return network.clone();
			} else {//失败或无应答
				return null;
			}
		} else {
			return null;
		}
	}

	// 获取所有vpc，只有uuid和name为有效。
	public NetworkInfoExt[] getAll() {
		NetworkInfoPool pool = NetworkPoolManager.singleton().getInfoPool();

		return pool.getALlDuplication();
	}

	// 异步修改vpc
	public boolean modifyAsync(Integer region, String uuid, String name, String description) throws MalformedURLException, IOException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
		JSONObject result = null;

		try {
			result = channel.networkModify(uuid, name, description);
		} catch (Exception e) {
			logger.error(String.format("fail to modify network. exception[%s]", e));
			channel.release();
			throw e;
		}

		if (HttpGatewayResponseHelper.isSuccess(result) == true) {
			NetworkInfoExt network = new NetworkInfoExt();
			network.setRegionId(region);
			network.setUuid(uuid);
			network.setName(name);
			network.setDescription(description);
			network.setSessionId(channel.getSessionId());
			network.setMessage("");
			network.updateTime();

			network.initAsyncStatus();

			NetworkInfoPool pool = NetworkPoolManager.singleton().getModifyPool();
			pool.put(network);

			return true;
		} else {
			logger.warn(String.format("fail to modify network. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
			channel.release();
			return false;
		}
	}

	// 获取异步修改vpc结果，值：0-失败， 1-成功， -1-正在修改
	public int getModifyResult(String uuid) {
		NetworkInfoPool pool = NetworkPoolManager.singleton().getModifyPool();
		NetworkInfoExt network = pool.get(uuid);
		
		return network.getAsyncStatus();
	}
	
	// 同步修改vpc
	public boolean modifySync(Integer region, String uuid, String name, String description) throws MalformedURLException, IOException {
		boolean result = this.modifyAsync(region, uuid, name, description);
		if (result) {
			NetworkInfoPool pool = NetworkPoolManager.singleton().getModifyPool();
			NetworkInfoExt network = pool.get(uuid);
			synchronized (network) {//wait for 5 second or notify by response message.
				try {
					network.wait(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("error occur when waiting modify network response call back.", e);
				}
			}
			
			int status = network.getAsyncStatus();
			if (status == 1) {//成功
				return true;
			} else {//失败或无应答
				return false;
			}
		} else {
			return false;
		}
	}

	// 异步查询vpc详情
	public boolean detailAsync(Integer region, String uuid) throws MalformedURLException, IOException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
		JSONObject result = null;

		try {
			result = channel.networkDetail(uuid);
		} catch (Exception e) {
			logger.error(String.format("fail to query network detail. exception[%s]", e));
			channel.release();
			throw e;
		}

		if (HttpGatewayResponseHelper.isSuccess(result) == true) {
			NetworkInfoPool pool = NetworkPoolManager.singleton().getInfoPool();
			NetworkInfoExt network = pool.get(uuid);
			if (network == null) {
				network = new NetworkInfoExt();
				network.setUuid(uuid);
				network.setRegionId(region);
				network.setSessionId(channel.getSessionId());
				network.updateTime();

				pool.put(network);
			}

			network.setMessage("");
			network.updateTime();
			network.setSessionId(channel.getSessionId());
			network.initAsyncStatus();

			return true;
		} else {
			logger.warn(String.format("fail to query network detail. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
			channel.release();
			return false;
		}
	}

	// 获取异步查询vpc详情
	public NetworkInfoExt getDetailResult(String uuid) {
		NetworkInfoPool pool = NetworkPoolManager.singleton().getInfoPool();
		
		return pool.getDuplication(uuid);
	}
	
	// 同步查询vpc详情.成功，则返回NetworkInfoExt对象；失败，则返回空对象
	public NetworkInfoExt detailSync(Integer region, String uuid) throws MalformedURLException, IOException {
		boolean result = this.detailAsync(region, uuid);
		if (result) {
			NetworkInfoPool pool = NetworkPoolManager.singleton().getInfoPool();
			NetworkInfoExt network = pool.get(uuid);
			synchronized (network) {//wait for 5 second or notify by response message.
				try {
					network.wait(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("error occur when waiting query network detail response call back.", e);
				}
			}
			
			int status = network.getAsyncStatus();
			if (status == 1) {//成功
				return network.clone();
			} else {//失败或无应答
				return null;
			}
		} else {
			return null;
		}
	}

	// 异步启动vpc
	public boolean startAsync(Integer region, String uuid) throws MalformedURLException, IOException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
		JSONObject result = null;

		try {
			result = channel.networkStart(uuid);
		} catch (Exception e) {
			logger.error(String.format("fail to start network. exception[%s]", e));
			channel.release();
			throw e;
		}

		if (HttpGatewayResponseHelper.isSuccess(result) == true) {
			NetworkInfoExt network = new NetworkInfoExt();
			network.setRegionId(region);
			network.setUuid(uuid);
			network.setSessionId(channel.getSessionId());
			network.updateTime();
			network.setMessage("");
			network.initAsyncStatus();

			NetworkInfoPool pool = NetworkPoolManager.singleton().getStartPool();
			pool.put(network);
			
			return true;
		} else {
			logger.warn(String.format("fail to start network. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
			channel.release();
			return false;
		}
	}

	// 获取异步启动vpc结果，值：0-失败， 1-成功， -1-正在修改
	public int getStartResult(String uuid) {
		NetworkInfoPool pool = NetworkPoolManager.singleton().getStartPool();
		NetworkInfoExt network = pool.get(uuid);
		
		return network.getAsyncStatus();
	}
	// 同步启动vpc
	public boolean startSync(Integer region, String uuid) throws MalformedURLException, IOException {
		boolean result = this.startAsync(region, uuid);
		if (result) {
			NetworkInfoPool pool = NetworkPoolManager.singleton().getStartPool();
			NetworkInfoExt network = pool.get(uuid);
			synchronized (network) {//wait for 5 second or notify by response message.
				try {
					network.wait(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("error occur when waiting start network response call back.", e);
				}
			}
			
			int status = network.getAsyncStatus();
			if (status == 1) {//成功
				return true;
			} else {//失败或无应答
				return false;
			}
		} else {
			return false;
		}
	}

	// 异步停止vpc
	public boolean stopAsync(Integer region, String uuid) throws MalformedURLException, IOException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
		JSONObject result = null;

		try {
			result = channel.networkStop(uuid);
		} catch (Exception e) {
			logger.error(String.format("fail to stop network. exception[%s]", e));
			channel.release();
			throw e;
		}

		if (HttpGatewayResponseHelper.isSuccess(result) == true) {
			NetworkInfoExt network = new NetworkInfoExt();
			network.setRegionId(region);
			network.setUuid(uuid);
			network.setSessionId(channel.getSessionId());
			network.updateTime();
			network.setMessage("");
			network.initAsyncStatus();

			NetworkInfoPool pool = NetworkPoolManager.singleton().getStopPool();
			pool.put(network);
			
			return true;
		} else {
			logger.warn(String.format("fail to stop network. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
			channel.release();
			return false;
		}
	}

	// 获取异步停止vpc结果，值：0-失败， 1-成功， -1-正在修改
	public int getStopResult(String uuid) {
		NetworkInfoPool pool = NetworkPoolManager.singleton().getStopPool();
		NetworkInfoExt network = pool.get(uuid);
		
		return network.getAsyncStatus();
	}
	
	// 同步停止vpc
	public boolean stopSync(Integer region, String uuid) throws MalformedURLException, IOException {
		boolean result = this.stopAsync(region, uuid);
		if (result) {
			NetworkInfoPool pool = NetworkPoolManager.singleton().getStopPool();
			NetworkInfoExt network = pool.get(uuid);
			synchronized (network) {//wait for 5 second or notify by response message.
				try {
					network.wait(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("error occur when waiting stop network response call back.", e);
				}
			}
			
			int status = network.getAsyncStatus();
			if (status == 1) {//成功
				return true;
			} else {//失败或无应答
				return false;
			}
		} else {
			return false;
		}
	}

	// 异步删除vpc
	public boolean deleteAsync(Integer region, String uuid) throws MalformedURLException, IOException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
		JSONObject result = null;

		try {
			result = channel.networkDelete(uuid);
		} catch (Exception e) {
			logger.error(String.format("fail to delete network. exception[%s]", e));
			channel.release();
			throw e;
		}

		if (HttpGatewayResponseHelper.isSuccess(result) == true) {
			NetworkInfoExt network = new NetworkInfoExt();
			network.setRegionId(region);
			network.setUuid(uuid);
			network.setSessionId(channel.getSessionId());
			network.updateTime();
			network.setMessage("");
			network.initAsyncStatus();

			NetworkInfoPool pool = NetworkPoolManager.singleton().getDelPool();
			pool.put(network);
			
			return true;
		} else {
			logger.warn(String.format("fail to delete network. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
			channel.release();
			return false;
		}
	}

	// 获取异步删除vpc结果，值：0-失败， 1-成功， -1-正在修改
	public int getDeleteResult(String uuid) {
		NetworkInfoPool pool = NetworkPoolManager.singleton().getDelPool();
		NetworkInfoExt network = pool.get(uuid);
		
		return network.getAsyncStatus();
	}
	
	// 同步删除vpc
	public boolean deleteSync(Integer region, String uuid) throws MalformedURLException, IOException {
		boolean result = this.deleteAsync(region, uuid);
		if (result) {
			NetworkInfoPool pool = NetworkPoolManager.singleton().getDelPool();
			NetworkInfoExt network = pool.get(uuid);
			synchronized (network) {//wait for 5 second or notify by response message.
				try {
					network.wait(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("error occur when waiting delete network response call back.", e);
				}
			}
			
			int status = network.getAsyncStatus();
			if (status == 1) {//成功
				return true;
			} else {//失败或无应答
				return false;
			}
		} else {
			return false;
		}
	}

	// 异步查询关联云主机
	public boolean queryHostAsync(Integer region, String uuid) throws MalformedURLException, IOException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
		JSONObject result = null;

		try {
			result = channel.networkQueryHost(uuid);
		} catch (Exception e) {
			logger.error(String.format("fail to query network host. exception[%s]", e));
			channel.release();
			throw e;
		}

		if (HttpGatewayResponseHelper.isSuccess(result) == true) {
			NetworkInfoExt network = new NetworkInfoExt();
			network.setRegionId(region);
			network.setUuid(uuid);
			network.setSessionId(channel.getSessionId());
			network.updateTime();
			network.setMessage("");
			network.initAsyncStatus();
			network.setHostList(new Host[0]);

			NetworkInfoPool pool = NetworkPoolManager.singleton().getQueryHostPool();
			pool.put(network);
			
			return true;
		} else {
			logger.warn(String.format("fail to query network host. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
			channel.release();
			return false;
		}
	}

	// 获取异步查询关联云主机
	public NetworkInfoExt getQueryHostResult(String uuid) {
		NetworkInfoPool pool = NetworkPoolManager.singleton().getQueryHostPool();
		
		return pool.getDuplication(uuid);
	}
	
	// 同步查询关联云主机.成功，则返回NetworkInfoExt对象；失败，则返回空对象
	public NetworkInfoExt queryHostSync(Integer region, String uuid) throws MalformedURLException, IOException {
		boolean result = this.queryHostAsync(region, uuid);
		if (result) {
			NetworkInfoPool pool = NetworkPoolManager.singleton().getQueryHostPool();
			NetworkInfoExt network = pool.get(uuid);
			synchronized (network) {//wait for 5 second or notify by response message.
				try {
					network.wait(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("error occur when waiting query network host response call back.", e);
				}
			}
			
			int status = network.getAsyncStatus();
			if (status == 1) {//成功
				return network.clone();
			} else {//失败或无应答
				return null;
			}
		} else {
			return null;
		}
	}

	// 异步关联云主机
	public String attachHostAsync(Integer region, String uuid, String hostUuid) throws MalformedURLException, IOException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
		JSONObject result = null;

		try {
			result = channel.networkAttachHost(uuid, hostUuid);
		} catch (Exception e) {
			logger.error(String.format("fail to attach host to network. exception[%s]", e));
			channel.release();
			throw e;
		}

		if (HttpGatewayResponseHelper.isSuccess(result) == true) {
			String sessionId = channel.getSessionId();
			
			NetworkInfoExt network = new NetworkInfoExt();
			network.setRegionId(region);
			network.setUuid(uuid);
			network.setSessionId(sessionId);
			network.updateTime();
			network.setMessage("");
			network.initAsyncStatus();
			network.setHostUuid(hostUuid);

			NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getAttachHostPool();
			pool.put(network);		
			
			return sessionId;
		} else {
			logger.warn(String.format("fail to attach host to network. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
			channel.release();
			return null;
		}
	}

	// 获取异步关联云主机结果
	public NetworkInfoExt getAttachHostResult(String sessionId, String uuid) {
		NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getAttachHostPool();
		
		return pool.getDuplication(sessionId, uuid);
	}
	
	// 同步关联云主机
	public NetworkInfoExt attachHostSync(Integer region, String uuid, String hostUuid) throws MalformedURLException, IOException {
		String sessionId = this.attachHostAsync(region, uuid, hostUuid);
		if (sessionId != null) {
			NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getAttachHostPool();
			NetworkInfoExt network = pool.get(sessionId, uuid);
			synchronized (network) {//wait for 5 second or notify by response message.
				try {
					network.wait(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("error occur when waiting attach host to network response call back.", e);
				}
			}
			
			int status = network.getAsyncStatus();
			if (status == 1) {//成功
				return network.clone();
			} else {//失败或无应答
				return null;
			}
		} else {
			return null;
		}
	}

	// 异步移除云主机
	public String detachHostAsync(Integer region, String uuid, String hostUuid) throws MalformedURLException, IOException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
		JSONObject result = null;

		try {
			result = channel.networkDetachHost(uuid, hostUuid);
		} catch (Exception e) {
			logger.error(String.format("fail to detach host to network. exception[%s]", e));
			channel.release();
			throw e;
		}

		if (HttpGatewayResponseHelper.isSuccess(result) == true) {
			String sessionId = channel.getSessionId();
			
			NetworkInfoExt network = new NetworkInfoExt();
			network.setRegionId(region);
			network.setUuid(uuid);
			network.setSessionId(sessionId);
			network.updateTime();
			network.setMessage("");
			network.initAsyncStatus();
			network.setHostUuid(hostUuid);
			
			NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getDetachHostPool();
			pool.put(network);
			
			return sessionId;
		} else {
			logger.warn(String.format("fail to detach host to network. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
			channel.release();
			return null;
		}
	}

	// 获取异步移除云主机结果
	public int getDetachHostResult(String sessionId, String uuid) {
		NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getDetachHostPool();
		NetworkInfoExt network = pool.get(sessionId, uuid);
		
		if (network != null) {
			return network.getAsyncStatus();
		}
		
		return 0;
	}
	
	// 同步移除云主机
	public boolean detachHostSync(Integer region, String uuid, String hostUuid) throws MalformedURLException, IOException {
		String sessionId = this.detachHostAsync(region, uuid, hostUuid);
		if (sessionId != null) {
			NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getDetachHostPool();
			NetworkInfoExt network = pool.get(sessionId, uuid);
			synchronized (network) {//wait for 5 second or notify by response message.
				try {
					network.wait(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("error occur when waiting detach host to network response call back.", e);
				}
			}
			
			int status = network.getAsyncStatus();
			if (status == 1) {//成功
				return true;
			} else {//失败或无应答
				return false;
			}
		} else {
			return false;
		}
	}

	// 异步申请公网ip
	public String attachAddressAsync(Integer region, String uuid, int count) throws MalformedURLException, IOException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
		JSONObject result = null;

		try {
			result = channel.networkAttachAddress(uuid, count);
		} catch (Exception e) {
			logger.error(String.format("fail to attach address to network. exception[%s]", e));
			channel.release();
			throw e;
		}

		if (HttpGatewayResponseHelper.isSuccess(result) == true) {
			String sessionId = channel.getSessionId();
			
			NetworkInfoExt network = new NetworkInfoExt();
			network.setRegionId(region);
			network.setUuid(uuid);
			network.setSessionId(sessionId);
			network.updateTime();
			network.setMessage("");
			network.initAsyncStatus();
			network.setCount(count);
			
			NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getAttachAddressPool();
			pool.put(network);
			
			return sessionId;
		} else {
			logger.warn(String.format("fail to attach address to network. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
			channel.release();
			return null;
		}
	}

	// 获取异步申请公网ip结果
	public NetworkInfoExt getAttachAddressResult(String sessionId, String uuid) {
		NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getAttachAddressPool();
		
		return pool.getDuplication(sessionId, uuid);
	}
	
	// 同步申请公网ip
	public NetworkInfoExt attachAddressSync(Integer region, String uuid, int count) throws MalformedURLException, IOException {
		String sessionId = this.attachAddressAsync(region, uuid, count);
		if (sessionId != null) {
			NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getAttachAddressPool();
			NetworkInfoExt network = pool.get(sessionId, uuid);
			synchronized (network) {//wait for 5 second or notify by response message.
				try {
					network.wait(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("error occur when waiting attach network address response call back.", e);
				}
			}
			
			int status = network.getAsyncStatus();
			if (status == 1) {//成功
				return network.clone();
			} else {//失败或无应答
				return null;
			}
		} else {
			return null;
		}
	}

	// 异步移除公网ip
	public String detachAddressAsync(Integer region, String uuid, String[] ip) throws MalformedURLException, IOException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
		JSONObject result = null;

		try {
			result = channel.networkDetachAddress(uuid, ip);
		} catch (Exception e) {
			logger.error(String.format("fail to detach network address. exception[%s]", e));
			channel.release();
			throw e;
		}

		if (HttpGatewayResponseHelper.isSuccess(result) == true) {
			String sessionId = channel.getSessionId();
			
			NetworkInfoExt network = new NetworkInfoExt();
			network.setRegionId(region);
			network.setUuid(uuid);
			network.setSessionId(sessionId);
			network.updateTime();
			network.setMessage("");
			network.initAsyncStatus();
			network.setIp(ip);
			
			NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getDetachAddressPool();
			pool.put(network);
			
			return sessionId;
		} else {
			logger.warn(String.format("fail to detach network address. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
			channel.release();
			return null;
		}
	}

	// 获取异步申请公网ip结果
	public NetworkInfoExt getDetachAddressResult(String sessionId, String uuid) {
		NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getDetachAddressPool();
		
		return pool.getDuplication(sessionId, uuid);
	}
	
	// 同步移除公网ip
	public NetworkInfoExt detachAddressSync(Integer region, String uuid, String[] ip) throws MalformedURLException, IOException {
		String sessionId = this.detachAddressAsync(region, uuid, ip);
		if (sessionId != null) {
			NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getDetachAddressPool();
			NetworkInfoExt network = pool.get(sessionId, uuid);
			synchronized (network) {//wait for 5 second or notify by response message.
				try {
					network.wait(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("error occur when waiting detach network address response call back.", e);
				}
			}
			
			int status = network.getAsyncStatus();
			if (status == 1) {//成功
				return network.clone();
			} else {//失败或无应答
				return null;
			}
		} else {
			return null;
		}
	}
	
	// 异步绑定端口
	public String bindPortAsync(Integer region, String uuid, String[] protocolList, String[] ipList, String[] portList, String[] hostList, String[] hostPortList) throws MalformedURLException, IOException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
		JSONObject result = null;

		try {
			result = channel.networkBindPort(uuid, protocolList, ipList, portList, hostList, hostPortList);
		} catch (Exception e) {
			logger.error(String.format("fail to bind port to network. exception[%s]", e));
			channel.release();
			throw e;
		}

		if (HttpGatewayResponseHelper.isSuccess(result) == true) {
			String sessionId = channel.getSessionId();
			
			NetworkInfoExt network = new NetworkInfoExt();
			network.setRegionId(region);
			network.setUuid(uuid);
			network.setSessionId(sessionId);
			network.updateTime();
			network.setMessage("");
			network.initAsyncStatus();
			Port[] requestPortList = new Port[protocolList.length];
			for (int i = 0; i < protocolList.length; i ++) {
				Port port = network.new Port();
				port.setProtocol(protocolList[i]);
				port.setPublicIp(ipList[i]);
				port.setPublicPort(portList[i]);
				port.setHostUuid(hostList[i]);
				port.setHostPort(hostPortList[i]);
				
				requestPortList[i] = port;
			}
			
			network.setRequestPortList(requestPortList);
			
			NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getBindPortPool();
			pool.put(network);
			
			return sessionId;
		} else {
			logger.warn(String.format("fail to bind port to network. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
			channel.release();
			return null;
		}
	}

	// 获取异步绑定端口
	public NetworkInfoExt getBindPortResult(String sessionId, String uuid) {
		NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getBindPortPool();
		
		return pool.getDuplication(sessionId, uuid);
	}
	
	// 同步绑定端口
	public NetworkInfoExt bindPortSync(Integer region, String uuid, String[] protocolList, String[] ipList, String[] portList, String[] hostList, String[] hostPortList) throws MalformedURLException, IOException {
		String sessionId = this.bindPortAsync(region, uuid, protocolList, ipList, portList, hostList, hostPortList);
		if (sessionId != null) {
			NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getBindPortPool();
			NetworkInfoExt network = pool.get(sessionId, uuid);
			synchronized (network) {//wait for 5 second or notify by response message.
				try {
					network.wait(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("error occur when waiting bind network port response call back.", e);
				}
			}
			
			int status = network.getAsyncStatus();
			if (status == 1) {//成功
				return network.clone();
			} else {//失败或无应答
				return null;
			}
		} else {
			return null;
		}
	}

	// 异步解除绑定端口
	public String unbindPortAsync(Integer region, String uuid, String[] protocolList, String[] ipList, String[] portList) throws MalformedURLException, IOException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
		JSONObject result = null;

		try {
			result = channel.networkUnbindPort(uuid, protocolList, ipList, portList);
		} catch (Exception e) {
			logger.error(String.format("fail to unbind port to network. exception[%s]", e));
			channel.release();
			throw e;
		}

		if (HttpGatewayResponseHelper.isSuccess(result) == true) {
			String sessionId = channel.getSessionId();
			
			NetworkInfoExt network = new NetworkInfoExt();
			network.setRegionId(region);
			network.setUuid(uuid);
			network.setSessionId(sessionId);
			network.updateTime();
			network.setMessage("");
			network.initAsyncStatus();
			Port[] requestPortList = new Port[protocolList.length];
			for (int i = 0; i < protocolList.length; i ++) {
				Port port = network.new Port();
				port.setProtocol(protocolList[i]);
				port.setPublicIp(ipList[i]);
				port.setPublicPort(portList[i]);
				
				requestPortList[i] = port;
			}
			
			network.setRequestPortList(requestPortList);
			
			NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getUnbindPortPool();
			pool.put(network);
			
			return sessionId;
		} else {
			logger.warn(String.format("fail to unbind port to network. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
			channel.release();
			return null;
		}
	}

	// 获取异步解除绑定端口
	public NetworkInfoExt getUnbindPortResult(String sessionId, String uuid) {
		NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getUnbindPortPool();
		
		return pool.getDuplication(sessionId, uuid);
	}
	
	// 同步解除绑定端口
	public NetworkInfoExt unbindPortSync(Integer region, String uuid, String[] protocolList, String[] ipList, String[] portList) throws MalformedURLException, IOException {
		String sessionId = this.unbindPortAsync(region, uuid, protocolList, ipList, portList);
		if (sessionId != null) {
			NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getUnbindPortPool();
			NetworkInfoExt network = pool.get(sessionId, uuid);
			synchronized (network) {//wait for 5 second or notify by response message.
				try {
					network.wait(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("error occur when waiting unbind network port response call back.", e);
				}
			}
			
			int status = network.getAsyncStatus();
			if (status == 1) {//成功
				return network.clone();
			} else {//失败或无应答
				return null;
			}
		} else {
			return null;
		}
	}

}
