package com.zhicloud.op.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.op.app.pool.device.DeviceExt;
import com.zhicloud.op.app.pool.network.NetworkInfoExt;
import com.zhicloud.op.app.pool.network.NetworkInfoExt.Host;
import com.zhicloud.op.app.pool.network.NetworkInfoExt.Port;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfo;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoExt;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoPool;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoPoolManager;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.op.httpGateway.HttpGatewayChannel;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.op.httpGateway.MonitorLevel;
import com.zhicloud.op.httpGateway.NodeTypeDefine;
import com.zhicloud.op.service.DeviceService;
import com.zhicloud.op.service.NetworkService;

@Controller
public class TestController {
	
	@RequestMapping("/public/test/queryTermianlUser.do")
	public void publicQueryTermianlUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
	}

	@RequestMapping("/security/test2.do")
	public String test2(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("TestController.test2()");
		return "/security/test2.jsp";
	}

	@RequestMapping("**/test3.do")
	public void test3(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("TestController.test3()");
		response.getWriter().print(request.getRequestURI());
	}

	@RequestMapping("/**/test4.do")
	public void test4(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("TestController.test4()");
		response.getWriter().print(request.getRequestURI());
	}

	@RequestMapping("/test6")
	@ResponseBody
	public void test6(HttpServletRequest request, HttpServletResponse response) throws Exception {
		System.out.println("TestController.test6()");
	}
	
	@RequestMapping("/storage_pool")
	public void storagePoolQuery(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpGatewayChannel channel = HttpGatewayManager.getChannel(4);
		Writer out = new PrintWriter(response.getOutputStream());

		if ("query".equalsIgnoreCase(command)) {
			JSONObject result = channel.storagePoolQuery();
			out.write(result.toString());

		} else if ("create".equalsIgnoreCase(command)) {
			String name = request.getParameter("name");
			if (name == null || name.trim().length() == 0) {
				out.write("invalid parameter \"name\"");
			} else {
				JSONObject result = channel.storagePoolCreate(name);
				out.write(result.toString());
			}

		} else if ("modify".equalsIgnoreCase(command)) {
			String name = request.getParameter("name");
			String uuid = request.getParameter("uuid");
			if (name == null || name.trim().length() == 0) {
				out.write("invalid parameter \"name\"");
			} else if (uuid == null || uuid.trim().length() == 0) {
				out.write("invalid parameter \"uuid\"");
			} else {
				JSONObject result = channel.storagePoolModify(uuid, name);
				out.write(result.toString());
			}

		} else if ("delete".equalsIgnoreCase(command)) {
			String uuid = request.getParameter("uuid");
			if (uuid == null || uuid.trim().length() == 0) {
				out.write("invalid parameter \"uuid\"");
			} else {
				JSONObject result = channel.storagePoolDelete(uuid);
				out.write(result.toString());
			}
		}

		out.flush();
	}

	@RequestMapping("/storage_resource")
	public void storageResource(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpGatewayChannel channel = HttpGatewayManager.getChannel(4);
		Writer out = new PrintWriter(response.getOutputStream());

		if ("query".equalsIgnoreCase(command)) {
			String pool = request.getParameter("pool");
			if (pool == null || pool.trim().length() == 0) {
				pool = "";
			}
			JSONObject result = channel.storagePoolQueryResource(pool);
			out.write(result.toString());

		} else if ("add".equalsIgnoreCase(command)) {
			String pool = request.getParameter("pool");
			String name = request.getParameter("name");
			if (pool == null || pool.trim().length() == 0) {
				out.write("invalid parameter \"pool\"");
			} else if (name == null || name.trim().length() == 0) {
				out.write("invalid parameter \"name\"");
			} else {
				JSONObject result = channel.storagePoolAddResource(pool, name);
				out.write(result.toString());
			}

		} else if ("remove".equalsIgnoreCase(command)) {
			String pool = request.getParameter("pool");
			String name = request.getParameter("name");
			if (pool == null || pool.trim().length() == 0) {
				out.write("invalid parameter \"pool\"");
			} else if (name == null || name.trim().length() == 0) {
				out.write("invalid parameter \"name\"");
			} else {
				JSONObject result = channel.storagePoolRemoveResource(pool, name);
				out.write(result.toString());
			}
		}

		out.flush();
	}

	@RequestMapping("/device")
	public void device(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpGatewayChannel channel = HttpGatewayManager.getChannel(1);
		Writer out = new PrintWriter(response.getOutputStream());

		if ("query".equalsIgnoreCase(command)) {
			String target = request.getParameter("target");
			if (target == null || target.trim().length() == 0) {
				out.write("invalid parameter \"target\"");
			} else {
				JSONObject result = channel.deviceQuery(target);
				out.write(result.toString());
			}

		} else if ("create".equalsIgnoreCase(command)) {
			String pool = request.getParameter("pool");
			String name = request.getParameter("name");
			String diskVolumeStr = request.getParameter("disk_volume");
			BigInteger diskVolume = null;

			if (pool == null || pool.trim().length() == 0) {
				out.write("invalid parameter \"pool\"");
			} else if (name == null || name.trim().length() == 0) {
				out.write("invalid parameter \"name\"");
			} else if (diskVolumeStr == null || diskVolumeStr.trim().length() == 0) {
				out.write("invalid parameter \"disk_volume\"");
			} else {
				try {
					diskVolume = BigInteger.valueOf(Long.valueOf(diskVolumeStr.trim()));
					JSONObject result = channel.deviceCreate(name, pool, diskVolume, 4096, 0, new Integer[] { 0, 0, 0, 0, 0, 0 }, 0, "", "", "", "");
					out.write(result.toString());
				} catch (Exception e) {
					out.write("invalid parameter \"disk_volume\"");
				}
			}

		} else if ("delete".equalsIgnoreCase(command)) {
			String uuid = request.getParameter("uuid");
			if (uuid == null || uuid.trim().length() == 0) {
				out.write("invalid parameter \"uuid\"");
			} else {
				JSONObject result = channel.deviceDelete(uuid);
				out.write(result.toString());
			}
		} else if ("modify".equalsIgnoreCase(command)) {
			String uuid = request.getParameter("uuid");
			String name = request.getParameter("name");
			if (uuid == null || uuid.trim().length() == 0) {
				out.write("invalid parameter \"uuid\"");
			} else if (name == null || name.trim().length() == 0) {
				out.write("invalid parameter \"name\"");
			} else {
				JSONObject result = channel.deviceModify(uuid, name, new Integer[] { 0, 0, 0 }, 0, "", "", "");
				out.write(result.toString());
			}
		} else if ("query_all".equalsIgnoreCase(command)) {
			DeviceService service = CoreSpringContextManager.getDeviceService();
			DeviceExt[] deviceList = service.getAll();
			for (DeviceExt device : deviceList) {
				out.write(device.getName() + " : " + device.getUuid() + "\n");
			}
		}

		out.flush();
	}

	@RequestMapping("/forwarder")
	public void forwarder(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws Exception {
		HttpGatewayChannel channel = HttpGatewayManager.getChannel(4);
		Writer out = new PrintWriter(response.getOutputStream());

		if ("add".equalsIgnoreCase(command)) {
			String target = request.getParameter("target");
			String typeStr = request.getParameter("type");
			String networkTypeStr = request.getParameter("network_type");
			String networkSource = request.getParameter("network_source");

			if (target == null || target.trim().length() == 0) {
				out.write("invalid parameter \"target\"");
			} else if (typeStr == null || typeStr.trim().length() != 1) {
				out.write("invalid parameter \"type\"");
			} else if (networkTypeStr == null || networkTypeStr.trim().length() != 1) {
				out.write("invalid parameter \"network_type\"");
			} else if (networkSource == null || networkSource.trim().length() == 0) {
				out.write("invalid parameter \"network_source\"");
			} else {
				int type = 0;
				int networkType = 0;

				try {
					type = Integer.parseInt(typeStr.trim());
				} catch (Exception e) {
					out.write("invalid parameter \"type\"");
					out.flush();
					return;
				}

				try {
					networkType = Integer.parseInt(networkTypeStr.trim());
				} catch (Exception e) {
					out.write("invalid parameter \"network_type\"");
					out.flush();
					return;
				}
				JSONObject result = channel.forwarderAdd(target, type, networkType, networkSource);
				out.write(result.toString());
			}

		} else if ("remove".equalsIgnoreCase(command)) {
			String target = request.getParameter("target");
			String typeStr = request.getParameter("type");
			String uuid = request.getParameter("uuid");
			if (target == null || target.trim().length() == 0) {
				out.write("invalid parameter \"target\"");
			} else if (typeStr == null || typeStr.trim().length() != 1) {
				out.write("invalid parameter \"type\"");
			} else if (uuid == null || uuid.trim().length() == 0) {
				out.write("invalid parameter \"uuid\"");
			} else {
				int type = 0;

				try {
					type = Integer.parseInt(typeStr.trim());
				} catch (Exception e) {
					out.write("invalid parameter \"type\"");
					out.flush();
					return;
				}
				JSONObject result = channel.forwarderRemove(target, type, uuid);
				out.write(result.toString());
			}
		}

		out.flush();
	}
	
	@RequestMapping("/network_async")
	public void networkAsync(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
		if ("create".equalsIgnoreCase(command)) {// 创建vpc
			Integer region = Integer.valueOf(1);
			String name = "test" + new Random().nextInt(100);
			NetworkService service = CoreSpringContextManager.getNetworkService();
			boolean result = service.createAsync(region, name, 27, "test", "5db7a60d80c042cc9c7ebc120fadcabf");
			// get result
			int i = 0;
			while (true) {
				i++;
				synchronized (this) {
					this.wait(10);// 10ms
				}
				NetworkInfoExt network = service.getCreateResult(region, name);
				int status = network.getAsyncStatus();
				System.err.println(String.format("create network. name[%s], async_status[%s]", name, status));
				if (status >= 0) {
					System.err.println(String.format("uuid[%s], network_address[%s]", network.getUuid(), network.getNetworkAddress()));
					break;
				}
				if (i >= 300) {
					break;
				}
			}

		} else if ("query".equalsIgnoreCase(command)) { // 查询vpc
			NetworkService service = CoreSpringContextManager.getNetworkService();
			NetworkInfoExt[] list = service.getAll();
			for (NetworkInfoExt network : list) {
				System.err.println(String.format("uuid[%s], name[%s]", network.getUuid(), network.getName()));
			}
		} else if ("modify".equalsIgnoreCase(command)) { // 修改vpc
			String uuid = "e99251e482194b138112496ea1474cca";
			NetworkService service = CoreSpringContextManager.getNetworkService();
			boolean result = service.modifyAsync(Integer.valueOf(1), uuid, "test_", "description");
			// get result
			int i = 0;
			while (true) {
				i++;
				synchronized (this) {
					this.wait(10);// 10ms
				}
				int status = service.getModifyResult(uuid);
				System.err.println(String.format("modify network. uuid[%s], async_status[%s]", uuid, status));
				if (status >= 0 || i >= 300) {
					break;
				}
			}
			
		} else if ("detail".equalsIgnoreCase(command)) { // 查询vpc详情
			String uuid = "e99251e482194b138112496ea1474cca";
			NetworkService service = CoreSpringContextManager.getNetworkService();
			boolean result = service.detailAsync(Integer.valueOf(1), uuid);
			// get result
			int i = 0;
			while (true) {
				i++;
				synchronized (this) {
					this.wait(10);// 10ms
				}
				NetworkInfoExt network = service.getDetailResult(uuid);
				int status = network.getAsyncStatus();
				System.err.println(String.format("query network detail. uuid[%s], async_status[%s]", uuid, status));
				if (status >= 0 || i >= 300) {
					System.err.println(String.format("name[%s]", network.getName()));
					break;
				}
			}
			
		} else if ("start".equalsIgnoreCase(command)) { // 启动vpc
			String uuid = "e99251e482194b138112496ea1474cca";
			NetworkService service = CoreSpringContextManager.getNetworkService();
			boolean result = service.startAsync(Integer.valueOf(1), uuid);
			// get result
			int i = 0;
			while (true) {
				i++;
				synchronized (this) {
					this.wait(10);// 10ms
				}
				int status = service.getStartResult(uuid);
				System.err.println(String.format("start network. uuid[%s], async_status[%s]", uuid, status));
				if (status >= 0 || i >= 300) {
					break;
				}
			}
			
		} else if ("stop".equalsIgnoreCase(command)) { // 停止vpc
			String uuid = "e99251e482194b138112496ea1474cca";
			NetworkService service = CoreSpringContextManager.getNetworkService();
			boolean result = service.stopAsync(Integer.valueOf(1), uuid);
			// get result
			int i = 0;
			while (true) {
				i++;
				synchronized (this) {
					this.wait(10);// 10ms
				}
				int status = service.getStopResult(uuid);
				System.err.println(String.format("stop network. uuid[%s], async_status[%s]", uuid, status));
				if (status >= 0 || i >= 300) {
					break;
				}
			}
			
		} else if ("delete".equalsIgnoreCase(command)) { // 删除vpc
			String uuid = "e99251e482194b138112496ea1474cca";
			NetworkService service = CoreSpringContextManager.getNetworkService();
			boolean result = service.deleteAsync(Integer.valueOf(1), uuid);
			// get result
			int i = 0;
			while (true) {
				i++;
				synchronized (this) {
					this.wait(10);// 10ms
				}
				int status = service.getDeleteResult(uuid);
				System.err.println(String.format("delete network. uuid[%s], async_status[%s]", uuid, status));
				if (status >= 0 || i >= 300) {
					break;
				}
			}

		} else if ("query_host".equalsIgnoreCase(command)) { // 查询vpc关联云主机
			String uuid = "b0890a6752344aaeb49b1525b4061a11";
			NetworkService service = CoreSpringContextManager.getNetworkService();
			boolean result = service.queryHostAsync(Integer.valueOf(1), uuid);
			// get result
			int i = 0;
			while (true) {
				i++;
				synchronized (this) {
					this.wait(10);// 10ms
				}
				NetworkInfoExt network = service.getQueryHostResult(uuid);
				int status = network.getAsyncStatus();
				System.err.println(String.format("query network host. uuid[%s], async_status[%s]", uuid, status));
				if (status >= 0 || i >= 300) {
					Host[] hosts = network.getHostList();
					for (Host host : hosts) {
						System.err.println(String.format("host_uuid[%s], host_name[%s], host_network_address[%s]", host.getUuid(), host.getName(), host.getNetworkAddress()));
					}
					break;
				}
			}
			
		} else if ("attach_host".equalsIgnoreCase(command)) { // 关联云主机
			String uuid = "b0890a6752344aaeb49b1525b4061a11";
			String hostUuid = "649c45d6-2d4c-4ccb-9c20-ad7757365969";

			NetworkService service = CoreSpringContextManager.getNetworkService();
			String sessionId = service.attachHostAsync(Integer.valueOf(1), uuid, hostUuid);
			int i = 0;
			while(true) {
				i ++;
				synchronized (this) {
					this.wait(10);//10ms
				}
				NetworkInfoExt network = service.getAttachHostResult(sessionId, uuid);
				int status = network.getAsyncStatus();
				System.err.println(String.format("attach host to network. uuid[%s], async_status[%s]", uuid, status));	
				if (status >= 0) {
					System.err.println(String.format("network_address[%s]", network.getHostNetworkAddress()));
					break;
				}
				if (i >= 300) {
					break;
				}
			}

		} else if ("detach_host".equalsIgnoreCase(command)) { // 移除云主机
			String uuid = "13daf473678d42e584eb47fcb94815c3";
			String hostUuid = "649c45d6-2d4c-4ccb-9c20-ad7757365969";

			NetworkService service = CoreSpringContextManager.getNetworkService();
			String sessionId = service.detachHostAsync(Integer.valueOf(1), uuid, hostUuid);
			int i = 0;
			while (true) {
				i++;
				synchronized (this) {
					this.wait(10);// 10ms
				}
				int status = service.getDetachHostResult(sessionId, uuid);
				System.err.println(String.format("detach host to network. uuid[%s], async_status[%s]", uuid, status));
				if (status >= 0 || i >= 300) {
					break;
				}
			}
		} else if ("attach_address".equalsIgnoreCase(command)) { // 申请公网ip
			String uuid = "13daf473678d42e584eb47fcb94815c3";
			int count = 1;

			NetworkService service = CoreSpringContextManager.getNetworkService();
			String sessionId = service.attachAddressAsync(Integer.valueOf(1), uuid, count);
			if (sessionId != null) {
				int i = 0;
				while (true) {
					i++;
					synchronized (this) {
						this.wait(10);// 10ms
					}
					NetworkInfoExt network = service.getAttachAddressResult(sessionId, uuid);
					int status = network.getAsyncStatus();
					System.err.println(String.format("attach address to network. uuid[%s], async_status[%s]", uuid, status));
					if (status >= 0 || i >= 300) {
						System.err.println(String.format("ip list : [%s]", StringUtil.joinWrap(network.getIp())));
						break;
					}
				}
				
			} else {
				System.err.println(String.format("fail to send attach network address message. uuid[%s], count[%s]", uuid, count));
			}
		} else if ("detach_address".equalsIgnoreCase(command)) { // 移除公网ip
			String uuid = "13daf473678d42e584eb47fcb94815c3";
			String[] ip = new String[] { "111.111.11.11" };

			NetworkService service = CoreSpringContextManager.getNetworkService();
			String sessionId = service.detachAddressAsync(Integer.valueOf(1), uuid, ip);
			
			if (sessionId != null) {
				int i = 0;
				while (true) {
					i++;
					synchronized (this) {
						this.wait(10);// 10ms
					}
					NetworkInfoExt network = service.getDetachAddressResult(sessionId, uuid);
					int status = network.getAsyncStatus();
					System.err.println(String.format("detach address to network. uuid[%s], async_status[%s]", uuid, status));
					if (status >= 0) {
						System.err.println(String.format("success ip list : [%s]", StringUtil.joinWrap(network.getSuccessIpList())));
						break;
					}
					if (i >= 300) {
						break;
					}
				}
				
			} else {
				System.err.println(String.format("fail to send detach network address message. uuid[%s], ip[%s]", uuid, StringUtil.joinWrap(ip)));
			}
		} else if ("bind_port".equalsIgnoreCase(command)) { // 绑定端口
			String uuid = "13daf473678d42e584eb47fcb94815c3";
			String[] protocolList = new String[] {"0"};
			String[] ipList = new String[] { "111.111.11.11" };
			String[] portList = new String[] { "10000" };
			String[] hostList = new String[] { "649c45d6-2d4c-4ccb-9c20-ad7757365969" };
			String[] hostPortList = new String[] { "80" };

			NetworkService service = CoreSpringContextManager.getNetworkService();
			String sessionId = service.bindPortAsync(Integer.valueOf(1), uuid, protocolList, ipList, portList, hostList, hostPortList);
			
			if (sessionId != null) {
				int i = 0;
				while (true) {
					i++;
					synchronized (this) {
						this.wait(10);// 10ms
					}
					NetworkInfoExt network = service.getBindPortResult(sessionId, uuid);
					int status = network.getAsyncStatus();
					System.err.println(String.format("bind port to network. uuid[%s], async_status[%s]", uuid, status));
					if (status >= 0) {
						Port[] successPortList = network.getSuccessPortList();
						for (Port port : successPortList) {
							System.err.println(String.format("protocol[%s], public_ip[%s], public_port[%s], host_uuid[%s], host_port[%s]", port.getProtocol(), port.getPublicIp(), port.getPublicPort(), port.getHostUuid(), port.getHostPort()));
						}
						break;
					}
					if (i >= 300) {
						break;
					}
				}
				
			} else {
				System.err.println(String.format("fail to send bind port message. uuid[%s]", uuid));
			}
			
		} else if ("unbind_port".equalsIgnoreCase(command)) { // 解除绑定端口
			String uuid = "13daf473678d42e584eb47fcb94815c3";
			String[] protocolList = new String[] { "0" };
			String[] ipList = new String[] { "111.111.11.11" };
			String[] portList = new String[] { "10000" };

			NetworkService service = CoreSpringContextManager.getNetworkService();
			String sessionId = service.unbindPortAsync(Integer.valueOf(1), uuid, protocolList, ipList, portList);
			
			if (sessionId != null) {
				int i = 0;
				while (true) {
					i++;
					synchronized (this) {
						this.wait(10);// 10ms
					}
					NetworkInfoExt network = service.getUnbindPortResult(sessionId, uuid);
					int status = network.getAsyncStatus();
					System.err.println(String.format("unbind port to network. uuid[%s], async_status[%s]", uuid, status));
					if (status >= 0) {
						Port[] successPortList = network.getSuccessPortList();
						for (Port port : successPortList) {
							System.err.println(String.format("protocol[%s], public_ip[%s], public_port[%s], host_uuid[%s], host_port[%s]", port.getProtocol(), port.getPublicIp(), port.getPublicPort(), port.getHostUuid(), port.getHostPort()));
						}
						break;
					}
					if (i >= 300) {
						break;
					}
				}
				
			} else {
				System.err.println(String.format("fail to send unbind port message. uuid[%s]", uuid));
			}
		}
	}
	
	@RequestMapping("/network_sync")
	public void networkSync(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
		if ("create".equalsIgnoreCase(command)) {// 创建vpc
			Integer region = Integer.valueOf(1);
			String name = "test" + new Random().nextInt(100);
			NetworkService service = CoreSpringContextManager.getNetworkService();
			NetworkInfoExt network = service.createSync(region, name, 24, "test", "5db7a60d80c042cc9c7ebc120fadcabf");
			if (network == null) {
				System.err.println("fail to create network.");
			} else {
				System.err.println(String.format("success to create network. uuid[%s], network_address[%s]", network.getUuid(), network.getNetworkAddress()));
			}
			
		} else if ("modify".equalsIgnoreCase(command)) { // 修改vpc
			String uuid = "f4c4882041f74efe9e57ffdc19378eec";
			NetworkService service = CoreSpringContextManager.getNetworkService();
			boolean result = service.modifySync(Integer.valueOf(1), uuid, "test_", "description");
			System.err.println(String.format("modify network. uuid[%s], result[%s]", uuid, result));
			
		} else if ("detail".equalsIgnoreCase(command)) { // 查询vpc详情
			String uuid = "f4c4882041f74efe9e57ffdc19378eec";
			NetworkService service = CoreSpringContextManager.getNetworkService();
			NetworkInfoExt network = service.detailSync(Integer.valueOf(1), uuid);
			if (network != null) {
				System.err.println(String.format("success to query network detail. uuid[%s], name[%s]", uuid, network.getName()));
			} else {
				System.err.println(String.format("fail to query network detail. uuid[%s]", uuid));
			}
			
		} else if ("start".equalsIgnoreCase(command)) { // 启动vpc
			String uuid = "f4c4882041f74efe9e57ffdc19378eec";
			NetworkService service = CoreSpringContextManager.getNetworkService();
			boolean result = service.startSync(Integer.valueOf(1), uuid);
			System.err.println(String.format("start network. uuid[%s], result[%s]", uuid, result));
			
		} else if ("stop".equalsIgnoreCase(command)) { // 停止vpc
			String uuid = "f4c4882041f74efe9e57ffdc19378eec";
			NetworkService service = CoreSpringContextManager.getNetworkService();
			boolean result = service.stopSync(Integer.valueOf(1), uuid);
			System.err.println(String.format("stop network. uuid[%s], result[%s]", uuid, result));
			
		} else if ("delete".equalsIgnoreCase(command)) { // 删除vpc
			String uuid = "f4c4882041f74efe9e57ffdc19378eec";
			NetworkService service = CoreSpringContextManager.getNetworkService();
			boolean result = service.deleteSync(Integer.valueOf(1), uuid);
			System.err.println(String.format("delete network. uuid[%s], result[%s]", uuid, result));

		} else if ("query_host".equalsIgnoreCase(command)) { // 查询vpc关联云主机
			String uuid = "b0890a6752344aaeb49b1525b4061a11";
			NetworkService service = CoreSpringContextManager.getNetworkService();
			NetworkInfoExt network = service.queryHostSync(Integer.valueOf(1), uuid);
			if (network == null) {
				System.err.println("fail to query network host.");
			} else {
				System.err.println(String.format("success to query network host. uuid[%s]", network.getUuid()));
				Host[] hosts = network.getHostList();
				for (Host host : hosts) {
					System.err.println(String.format("host_uuid[%s], host_name[%s], host_network_address[%s]", host.getUuid(), host.getName(), host.getNetworkAddress()));
				}
			}
			
		} else if ("attach_host".equalsIgnoreCase(command)) { // 关联云主机
			String uuid = "b0890a6752344aaeb49b1525b4061a11";
			String hostUuid = "536ca1a1-3c9f-49b0-a09c-53f2f9932a0b";

			NetworkService service = CoreSpringContextManager.getNetworkService();
			NetworkInfoExt network = service.attachHostSync(Integer.valueOf(1), uuid, hostUuid);
			if (network != null) {
				System.err.println(String.format("success to attach host to network. uuid[%s], host_uuid[%s], network_address[%s]", uuid, network.getHostUuid(), network.getHostNetworkAddress()));	
			} else {
				System.err.println(String.format("fail to attach host to network. uuid[%s], host_uuid[%s]", uuid, hostUuid));				
			}

		} else if ("detach_host".equalsIgnoreCase(command)) { // 移除云主机
			String uuid = "13daf473678d42e584eb47fcb94815c3";
			String hostUuid = "649c45d6-2d4c-4ccb-9c20-ad7757365969";

			NetworkService service = CoreSpringContextManager.getNetworkService();
			boolean result = service.detachHostSync(Integer.valueOf(1), uuid, hostUuid);
			System.err.println(String.format("detach host to network. uuid[%s], result[%s]", uuid, result));

		} else if ("attach_address".equalsIgnoreCase(command)) { // 申请公网ip
			String uuid = "13daf473678d42e584eb47fcb94815c3";
			int count = 1;

			NetworkService service = CoreSpringContextManager.getNetworkService();
			NetworkInfoExt network = service.attachAddressSync(Integer.valueOf(1), uuid, count);
			System.err.println(String.format("attach address to network. uuid[%s], async_status[%s]", uuid, network.getAsyncStatus()));

		} else if ("detach_address".equalsIgnoreCase(command)) { // 移除公网ip
			String uuid = "13daf473678d42e584eb47fcb94815c3";
			String[] ip = new String[] { "111.111.11.11" };

			NetworkService service = CoreSpringContextManager.getNetworkService();
			NetworkInfoExt network = service.detachAddressSync(Integer.valueOf(1), uuid, ip);
			System.err.println(String.format("detach address to network. uuid[%s], async_status[%s]，success ip list : [%s]", uuid, network.getAsyncStatus(), StringUtil.joinWrap(network.getSuccessIpList())));

		} else if ("bind_port".equalsIgnoreCase(command)) { // 绑定端口
			String uuid = "13daf473678d42e584eb47fcb94815c3";
			String[] protocolList = new String[] {"0"};
			String[] ipList = new String[] { "111.111.11.11" };
			String[] portList = new String[] { "10000" };
			String[] hostList = new String[] { "649c45d6-2d4c-4ccb-9c20-ad7757365969" };
			String[] hostPortList = new String[] { "80" };

			NetworkService service = CoreSpringContextManager.getNetworkService();
			NetworkInfoExt network = service.bindPortSync(Integer.valueOf(1), uuid, protocolList, ipList, portList, hostList, hostPortList);
			System.err.println(String.format("bind port to network. uuid[%s], async_status[%s]", uuid, network.getAsyncStatus()));

			Port[] successPortList = network.getSuccessPortList();
			for (Port port : successPortList) {
				System.err.println(String.format("protocol[%s], public_ip[%s], public_port[%s], host_uuid[%s], host_port[%s]", port.getProtocol(), port.getPublicIp(), port.getPublicPort(), port.getHostUuid(), port.getHostPort()));
			}
			
		} else if ("unbind_port".equalsIgnoreCase(command)) { // 解除绑定端口
			String uuid = "13daf473678d42e584eb47fcb94815c3";
			String[] protocolList = new String[] { "0" };
			String[] ipList = new String[] { "111.111.11.11" };
			String[] portList = new String[] { "10000" };

			NetworkService service = CoreSpringContextManager.getNetworkService();
			NetworkInfoExt network = service.unbindPortSync(Integer.valueOf(1), uuid, protocolList, ipList, portList);
			
			System.err.println(String.format("unbind port to network. uuid[%s], async_status[%s]", uuid, network.getAsyncStatus()));

			Port[] successPortList = network.getSuccessPortList();
			for (Port port : successPortList) {
				System.err.println(String.format("protocol[%s], public_ip[%s], public_port[%s], host_uuid[%s], host_port[%s]", port.getProtocol(), port.getPublicIp(), port.getPublicPort(), port.getHostUuid(), port.getHostPort()));
			}
		}
	}
	
	@RequestMapping("/address_pool")
	public void addressPool(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
		HttpGatewayChannel channel = HttpGatewayManager.getChannel(Integer.valueOf(1));
		if ("query".equalsIgnoreCase(command)) {
			JSONObject result = channel.addressPoolQuery();
			if (HttpGatewayResponseHelper.isSuccess(result)) {
				JSONArray poolList = result.getJSONArray("addressPools");
				for (int i = 0; i < poolList.size(); i ++) {
					JSONObject pool = poolList.getJSONObject(i);
					String name = pool.getString("name");
					String uuid = pool.getString("uuid");
					
					System.err.println(String.format("pool: name[%s], uuid[%s]", name, uuid));
				}
			}
		} else if ("add_resource".equalsIgnoreCase(command)) {
			String pool = "5801fa989a004b71b4feadbd5acf41e3";
			String[] ip = new String[]{"1.0.0.2", "1.0.0.3"};
			Integer[] range = new Integer[]{1, 1};
			JSONObject result = channel.addressPoolAddResource(pool, ip, range);
			if (HttpGatewayResponseHelper.isSuccess(result)) {
				System.err.println(String.format("add resource to address pool success, pool '%s', ip '%s', range '%s'", pool, StringUtil.joinWrap(ip), StringUtil.joinWrap(range)));
			} else {
				System.err.println(String.format("add resource to address pool fail, pool '%s', ip '%s', range '%s'", pool, StringUtil.joinWrap(ip), StringUtil.joinWrap(range)));
			}
		} else if ("remove_resource".equalsIgnoreCase(command)) {
			String pool = "5801fa989a004b71b4feadbd5acf41e3";
			String[] ip = new String[]{"2.0.0.1"};
			JSONObject result = channel.addressPoolRemoveResource(pool, ip);
			if (HttpGatewayResponseHelper.isSuccess(result)) {
				System.err.println(String.format("remove resource to address pool success, pool '%s', ip '%s'", pool, ip));
			} else {
				System.err.println(String.format("remove resource to address pool fail, pool '%s', ip '%s'", pool, ip));
			}
		} else if ("query_resource".equalsIgnoreCase(command)) {
			String pool = "5801fa989a004b71b4feadbd5acf41e3";
			JSONObject result = channel.addressPoolQueryResource(pool);
			if (HttpGatewayResponseHelper.isSuccess(result)) {
				System.err.println(String.format("query resource to address pool success, pool '%s', result '%s'", pool, result));
			} else {
				System.err.println(String.format("query resource to address pool fail, pool '%s'", pool));
			}
		}
	}
	
	private int systemMonitorTask = 0;
	@RequestMapping("/system_monitor")
	public void systemMonitor(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
		if ("start".equalsIgnoreCase(command)) {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			try {
				JSONObject result = channel.startSystemMonitor();
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					this.systemMonitorTask = result.getInt("task");
					System.err.println(String.format("success to start system monitor. task[%s]", this.systemMonitorTask));
				} else {
					System.err.println("fail to start system monitor.");
					channel.release();
				}
			}catch(Exception e) {
				System.err.println("fail to start system monitor.");
				channel.release();
				throw e;
			}
			
		} else if ("stop".equalsIgnoreCase(command)) {
			HttpGatewayChannel channel = HttpGatewayManager.getChannel(Integer.valueOf(1));
			JSONObject result = channel.stopSystemMonitor(this.systemMonitorTask);
			if (HttpGatewayResponseHelper.isSuccess(result) == true) {
				System.err.println(String.format("success to stop system monitor. task[%s]", this.systemMonitorTask));
				this.systemMonitorTask = 0;
			} else {
				System.err.println(String.format("fail to stop system monitor. task[%s]", this.systemMonitorTask));
			}
		}
	}
	
	@RequestMapping("/snapshot_pool")
	public void snapshotPool(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
		if ("query".equalsIgnoreCase(command)) {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			try {
				JSONObject result = channel.snapshotPoolQuery();
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send query snapshot pool request.");
				} else {
					System.err.println("fail to send query snapshot pool request.");
					channel.release();
				}
			}catch(Exception e) {
				System.err.println("fail to send query snapshot pool request.");
				channel.release();
				throw e;
			}
			
		} else if ("create".equalsIgnoreCase(command)) {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			try {
				JSONObject result = channel.snapshotPoolCreate("test");
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send create snapshot pool request.");
				} else {
					System.err.println("fail to create snapshot pool request.");
					channel.release();
				}
			}catch(Exception e) {
				System.err.println("fail to create snapshot pool request.");
				channel.release();
				throw e;
			}
			
		} else if ("modify".equalsIgnoreCase(command)) {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			try {
				JSONObject result = channel.snapshotPoolModify("123456789", "test");
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send modify snapshot pool request.");
				} else {
					System.err.println("fail to modify snapshot pool request.");
					channel.release();
				}
			}catch(Exception e) {
				System.err.println("fail to modify snapshot pool request.");
				channel.release();
				throw e;
			}
			
		} else if ("delete".equalsIgnoreCase(command)) {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			try {
				JSONObject result = channel.snapshotPoolDelete("123456789");
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send delete snapshot pool request.");
				} else {
					System.err.println("fail to delete snapshot pool request.");
					channel.release();
				}
			}catch(Exception e) {
				System.err.println("fail to delete snapshot pool request.");
				channel.release();
				throw e;
			}
			
		} else if ("add_node".equalsIgnoreCase(command)) {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			try {
				JSONObject result = channel.snapshotPoolAddNode("123456789", "data_node_dev");
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send add snapshot node request.");
				} else {
					System.err.println("fail to add snapshot node request.");
					channel.release();
				}
			}catch(Exception e) {
				System.err.println("fail to add snapshot node request.");
				channel.release();
				throw e;
			}
			
		} else if ("remove_node".equalsIgnoreCase(command)) {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			try {
				JSONObject result = channel.snapshotPoolRemoveNode("123456789", "data_node_dev");
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send remove snapshot node request.");
				} else {
					System.err.println("fail to remove snapshot node request.");
					channel.release();
				}
			}catch(Exception e) {
				System.err.println("fail to remove snapshot node request.");
				channel.release();
				throw e;
			}
			
		} else if ("query_node".equalsIgnoreCase(command)) {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			try {
				JSONObject result = channel.snapshotPoolQueryNode("123456789");
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send query snapshot node request.");
				} else {
					System.err.println("fail to query snapshot node request.");
					channel.release();
				}
			}catch(Exception e) {
				System.err.println("fail to query snapshot node request.");
				channel.release();
				throw e;
			}
			
		}
	}
	
	@RequestMapping("/snapshot")
	public void snapshot(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
		if ("query".equalsIgnoreCase(command)) {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			try {
				JSONObject result = channel.snapshotQuery("123456789");
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send query snapshot request.");
				} else {
					System.err.println("fail to send query snapshot request.");
					channel.release();
				}
			}catch(Exception e) {
				System.err.println("fail to send query snapshot request.");
				channel.release();
				throw e;
			}
			
		} else if ("create".equalsIgnoreCase(command)) {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			try {
				JSONObject result = channel.snapshotCreate("123456789", "test");
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send create snapshot request.");
				} else {
					System.err.println("fail to send create snapshot request.");
					channel.release();
				}
			}catch(Exception e) {
				System.err.println("fail to send create snapshot request.");
				channel.release();
				throw e;
			}
			
		} else if ("delete".equalsIgnoreCase(command)) {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			try {
				JSONObject result = channel.snapshotDelete("123456789");
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send delete snapshot request.");
				} else {
					System.err.println("fail to send delete snapshot request.");
					channel.release();
				}
			}catch(Exception e) {
				System.err.println("fail to send delete snapshot request.");
				channel.release();
				throw e;
			}
			
		} else if ("resume".equalsIgnoreCase(command)) {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			try {
				JSONObject result = channel.snapshotResume("123456789");
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send resume snapshot request.");
				} else {
					System.err.println("fail to send resume snapshot request.");
					channel.release();
				}
			}catch(Exception e) {
				System.err.println("fail to send resume snapshot request.");
				channel.release();
				throw e;
			}
			
		}
	}
	
	@RequestMapping("/flush_disk_image")
	public void flushDiskImage(HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
		try {
			String uuid = "6b4cbcaf-ebdb-4401-a80b-0a5286cc071c";
			int disk = 0;
			int mode = 0;
			String image = "0fce83def4ec4fe49ac4a08417c55b69";
					
			JSONObject result = channel.hostFlushDiskImage(uuid, disk, mode, image);
			if (HttpGatewayResponseHelper.isSuccess(result) == true) {
				System.err.println("success to send flush disk image request.");
			} else {
				System.err.println("fail to send flush disk image request.");
				channel.release();
			}
		}catch(Exception e) {
			System.err.println("fail to send flush disk image request.");
			channel.release();
			throw e;
		}
	}
	
	@RequestMapping("/backup_host")
	public void flushDiskImage(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
		String uuid = "b8540391-b2cb-4c5e-8ba2-368c247936f1";
		int mode = 1;
		int disk = 0;

		if ("backup".equalsIgnoreCase(command)) {
			try {
				JSONObject result = channel.hostBackup(uuid, mode, disk);
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send backup host request.");
				} else {
					System.err.println("fail to send backup host request.");
					channel.release();
				}
			} catch (Exception e) {
				System.err.println("fail to send backup host request.");
				channel.release();
				throw e;
			}
		} else if ("resume".equalsIgnoreCase(command)) {
			try {
				JSONObject result = channel.hostResume(uuid, mode, disk);
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send resume host request.");
				} else {
					System.err.println("fail to send resume host request.");
					channel.release();
				}
			} catch (Exception e) {
				System.err.println("fail to send resume host request.");
				channel.release();
				throw e;
			}
		} else if ("query".equalsIgnoreCase(command)) {
			try {
				JSONObject result = channel.hostQueryBackup(uuid);
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send query host backup request.");
				} else {
					System.err.println("fail to send query host backup request.");
					channel.release();
				}
			} catch (Exception e) {
				System.err.println("fail to send query host backup request.");
				channel.release();
				throw e;
			}
		}
	}
	
	@RequestMapping("/rule")
	public void rule(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
		String target = "";
		int mode = 0;
		String[] ip = new String[]{"", ""};
		Integer[] port = new Integer[]{Integer.valueOf(0), Integer.valueOf(0)};

		if ("add".equalsIgnoreCase(command)) {
			try {
				JSONObject result = channel.ruleAdd(target, mode, ip, port);
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send add rule request.");
				} else {
					System.err.println("fail to send add rule request.");
					channel.release();
				}
			} catch (Exception e) {
				System.err.println("fail to send add rule request.");
				channel.release();
				throw e;
			}
		} else if ("remove".equalsIgnoreCase(command)) {
			try {
				JSONObject result = channel.ruleRemove(target, mode, ip, port);
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send remove rule request.");
				} else {
					System.err.println("fail to send remove rule request.");
					channel.release();
				}
			} catch (Exception e) {
				System.err.println("fail to send remove rule request.");
				channel.release();
				throw e;
			}
		} else if ("query".equalsIgnoreCase(command)) {
			target = "intelligent_router_000c29484546";
			try {
				JSONObject result = channel.ruleQuery(target);
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println("success to send query rule request.");
				} else {
					System.err.println("fail to send query rule request.");
					channel.release();
				}
			} catch (Exception e) {
				System.err.println("fail to send query rule request.");
				channel.release();
				throw e;
			}
		}
	}
	
	@RequestMapping("/host")
	public void host(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
		HttpGatewayChannel channel = HttpGatewayManager.getChannel(Integer.valueOf(1));
		if ("create".equalsIgnoreCase(command)) {
			String name = "test_option";
			String pool = "8f98d372139741dcab41ed17195bba40";
			Integer cpu_count = 1;
			BigInteger memory = BigInteger.ONE;
			Integer[] option = new Integer[]{0, 0, 0};
			String image = "";
			BigInteger[] disk_volume = new BigInteger[]{BigInteger.valueOf(1024*1024*1024)};
			Integer[] port = new Integer[]{};
			String user = "";
			String group = "";
			String display = "";
			String authentication = "";
			String network = "";
			BigInteger inbound_bandwidth = BigInteger.ZERO;
			BigInteger outbound_bandwidth = BigInteger.ZERO;
			String path = "aaa";
			String crypt = "bbb";
			int maxIops = 1;
			int cpuPriority = 1;
			
			JSONObject result = channel.hostCreate(name, pool, cpu_count, memory, option, image, disk_volume, port, user, group, display, authentication, network, inbound_bandwidth, outbound_bandwidth
					, path, crypt, maxIops, cpuPriority
					);
		} else if ("modify".equalsIgnoreCase(command)) {
			String uuid = "57ac941e-0c89-412a-92c0-3576229ead22";
			String name = "";
			Integer cpu_count = 1;
			BigInteger memory = BigInteger.valueOf(102555);
			Integer[] option = new Integer[]{1};
			Integer[] port = new Integer[]{};
			String display = "";
			String authentication = "";
			String network = "";
			BigInteger inbound_bandwidth = BigInteger.ZERO;
			BigInteger outbound_bandwidth = BigInteger.ZERO;
			int maxIops = 0;
			int cpuPriority = 0;
			
			JSONObject result = channel.hostModify(uuid, name, cpu_count, memory, option, port, display, authentication, network, inbound_bandwidth, outbound_bandwidth, maxIops, cpuPriority);
		} else if ("info".equalsIgnoreCase(command)) {
			String uuid = "57ac941e-0c89-412a-92c0-3576229ead22";
			JSONObject hostResult = channel.hostQueryInfo(uuid);
			System.err.println("query host info result: " + hostResult);
		} else if ("reset".equalsIgnoreCase(command)) {
			String uuid = "1cf341e9-0f49-4d0e-ba81-1b267c5038c8";
			HttpGatewayAsyncChannel asyncChannel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			JSONObject hostResult = asyncChannel.hostReset(uuid);
			System.err.println("send reset host request: " + hostResult);
		} else if ("query".equalsIgnoreCase(command)) {
			String pool = "";
			int range = 1;
			String target = "f6d78e4c2f5a443282902afdf74bd40a";
			JSONObject result = channel.hostQuery(range, target);
			System.err.println("send query host request: " + result);
		}
	}
	
	@RequestMapping("/service")
	public void service(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
		if ("query_cache".equalsIgnoreCase(command)) {
			ServiceInfoPool pool = ServiceInfoPoolManager.singleton().getPool();
			ServiceInfoExt[] services = pool.getAll();
			for (ServiceInfo service : services) {
				System.err.println(String.format("service : name[%s], ip[%s], port[%d], status[%d], version[%s]", service.getName(), service.getIp(), service.getPort(), service.getStatus(), service.getVersion()));
			}
		} else if ("query".equalsIgnoreCase(command)) {
			int type = NodeTypeDefine.node_client;
			String group = "default";
			HttpGatewayChannel channel = HttpGatewayManager.getChannel(Integer.valueOf(1));
			JSONObject result = channel.serviceQuery(type, group);
			System.err.println("query service result : " + result);
		}
	}
	
	@RequestMapping("/monitor")
	public void monitor(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
		HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
		if ("start".equalsIgnoreCase(command)) {
			channel.startMonitor(MonitorLevel.SYSTEM, new String[]{" sfdsdf"});
//			int level = Integer.parseInt(request.getParameter("level"));
//			channel.startMonitor(level, new String[]{"657f4057-c9ad-437d-ae0c-15fa4ecf76b9"});
		} else if ("stop".equalsIgnoreCase(command)) {
			int task = Integer.parseInt(request.getParameter("task"));
			channel.stopMonitor(task);
		}
	}
	
	@RequestMapping("/compute_pool")
	public void computePool(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
		if ("query_resource".equalsIgnoreCase(command)) {
			HttpGatewayChannel channel = HttpGatewayManager.getChannel(Integer.valueOf(1));
			JSONObject result = channel.computePoolQueryResource("");
			System.err.println("query resource of compute pool : " + result);
		} else if ("modify".equalsIgnoreCase(command)) {
			String uuid = "52ef03480b5946cc966ff9e81f8801f8";
			String name = "test";
			int networkType = 0;
			String network = "";
			int diskType = 0;
			String diskSource = "";
			Integer[] mode = new Integer[]{Integer.valueOf(0), Integer.valueOf(0)};
			int option = 0;
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			JSONObject result = channel.computePoolModify(uuid, name, networkType, network, diskType, diskSource, mode, option);
			System.err.println("modify compute pool, result : " + result);
		} else if ("create".equalsIgnoreCase(command)) {
			String name = "test1";
			int networkType = 0;
			String network = "";
			int diskType = 0;
			String diskSource = "";
			Integer[] mode = new Integer[]{Integer.valueOf(1), Integer.valueOf(1)};
			HttpGatewayChannel channel = HttpGatewayManager.getChannel(Integer.valueOf(1));
			JSONObject result = channel.computePoolCreate(name, networkType, network, diskType, diskSource, mode);
			System.err.println("create compute pool, result : " + result);
		} else if ("detail".equalsIgnoreCase(command)) {
			String uuid = "02d84911afda483d8c2d64f9e1544258";
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
			JSONObject result = channel.computePoolDetail(uuid);
			System.err.println("query compute pool detail, result : " + result);
		}
	}
	
	@RequestMapping("/port_pool")
	public void portPool(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
		HttpGatewayChannel channel = HttpGatewayManager.getChannel(Integer.valueOf(1));
		String pool = "f124759a568a4fd49623e57815fd0ba6";
		if ("query_resource".equalsIgnoreCase(command)) {
			JSONObject result = channel.portPoolQueryResource(pool);
			System.err.println("query resource of port pool : " + result);
		} else if ("add_resource".equalsIgnoreCase(command)) {
			String[] ip = new String[]{"1.0.0.254"};
			Integer[] range = new Integer[]{1, 1};
			JSONObject result = channel.portPoolAddResource(pool, ip, range);
			System.err.println("add resource into port pool, result : " + result);
		} else if ("remove_resource".equalsIgnoreCase(command)) {
			String[] ip = new String[]{"1.0.0.254"};
			JSONObject result = channel.portPoolRemoveResource(pool, ip);
			System.err.println("remove resource into port pool, result : " + result);
		}
	}
	
    @RequestMapping("/monitor_test")
    public void monitor_test(@RequestParam("command") String command,@RequestParam("level") String level,@RequestParam("uuid") String uuid, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
        HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
        if ("start".equalsIgnoreCase(command)) {
            System.out.println("comman:"+command+",level:"+level+",uuid:"+uuid);
            channel.startMonitor(Integer.parseInt(level), new String[]{uuid});
        } else if ("stop".equalsIgnoreCase(command)) {
            int task = Integer.parseInt(request.getParameter("task"));
            channel.stopMonitor(task);
        }
    }
	
    @RequestMapping("/server")
    public void server(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws MalformedURLException, IOException, InterruptedException {
        HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(Integer.valueOf(1));
        int level = 0;
        String target = "node_client_000c29d74563";
        int diskType = 0;
        if ("query_storage_device".equalsIgnoreCase(command)) {
            JSONObject result = channel.serverQueryStorageDevice(level, target, diskType);
            System.err.println("send query storage device request, result:" + result);
        } else if ("add_storage_device".equalsIgnoreCase(command)) {
            JSONObject result = channel.serverAddStorageDevice(level, target, diskType, new Integer[]{Integer.valueOf(0)}, "", "", "");
            System.err.println("send add storage device request, result:" + result);
        } else if ("remove_storage_device".equalsIgnoreCase(command)) {
        	int index = 0;
            JSONObject result = channel.serverRemoveStorageDevice(level, target, diskType, index);
            System.err.println("send remove storage device request, result:" + result);
        } else if ("enable_storage_device".equalsIgnoreCase(command)) {
        	int index = 0;
            JSONObject result = channel.serverEnableStorageDevice(level, target, diskType, index);
            System.err.println("send enable storage device request, result:" + result);
        } else if ("disable_storage_device".equalsIgnoreCase(command)) {
        	int index = 0;
            JSONObject result = channel.serverDisableStorageDevice(level, target, diskType, index);
            System.err.println("send disable storage device request, result:" + result);
        }
    }
}
