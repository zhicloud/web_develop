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
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.op.httpGateway.HttpGatewayChannel;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;
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
	
//	@RequestMapping("/storage_pool")
//	public void storagePoolQuery(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		HttpGatewayChannel channel = HttpGatewayManager.getChannel(4);
//		Writer out = new PrintWriter(response.getOutputStream());
//
//		if ("query".equalsIgnoreCase(command)) {
//			JSONObject result = channel.storagePoolQuery();
//			out.write(result.toString());
//
//		} else if ("create".equalsIgnoreCase(command)) {
//			String name = request.getParameter("name");
//			if (name == null || name.trim().length() == 0) {
//				out.write("invalid parameter \"name\"");
//			} else {
//				JSONObject result = channel.storagePoolCreate(name);
//				out.write(result.toString());
//			}
//
//		} else if ("modify".equalsIgnoreCase(command)) {
//			String name = request.getParameter("name");
//			String uuid = request.getParameter("uuid");
//			if (name == null || name.trim().length() == 0) {
//				out.write("invalid parameter \"name\"");
//			} else if (uuid == null || uuid.trim().length() == 0) {
//				out.write("invalid parameter \"uuid\"");
//			} else {
//				JSONObject result = channel.storagePoolModify(uuid, name);
//				out.write(result.toString());
//			}
//
//		} else if ("delete".equalsIgnoreCase(command)) {
//			String uuid = request.getParameter("uuid");
//			if (uuid == null || uuid.trim().length() == 0) {
//				out.write("invalid parameter \"uuid\"");
//			} else {
//				JSONObject result = channel.storagePoolDelete(uuid);
//				out.write(result.toString());
//			}
//		}
//
//		out.flush();
//	}

//	@RequestMapping("/storage_resource")
//	public void storageResource(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		HttpGatewayChannel channel = HttpGatewayManager.getChannel(4);
//		Writer out = new PrintWriter(response.getOutputStream());
//
//		if ("query".equalsIgnoreCase(command)) {
//			String pool = request.getParameter("pool");
//			if (pool == null || pool.trim().length() == 0) {
//				pool = "";
//			}
//			JSONObject result = channel.storagePoolQueryResource(pool);
//			out.write(result.toString());
//
//		} else if ("add".equalsIgnoreCase(command)) {
//			String pool = request.getParameter("pool");
//			String name = request.getParameter("name");
//			if (pool == null || pool.trim().length() == 0) {
//				out.write("invalid parameter \"pool\"");
//			} else if (name == null || name.trim().length() == 0) {
//				out.write("invalid parameter \"name\"");
//			} else {
//				JSONObject result = channel.storagePoolAddResource(pool, name);
//				out.write(result.toString());
//			}
//
//		} else if ("remove".equalsIgnoreCase(command)) {
//			String pool = request.getParameter("pool");
//			String name = request.getParameter("name");
//			if (pool == null || pool.trim().length() == 0) {
//				out.write("invalid parameter \"pool\"");
//			} else if (name == null || name.trim().length() == 0) {
//				out.write("invalid parameter \"name\"");
//			} else {
//				JSONObject result = channel.storagePoolRemoveResource(pool, name);
//				out.write(result.toString());
//			}
//		}
//
//		out.flush();
//	}
//
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
//
//	@RequestMapping("/forwarder")
//	public void forwarder(@RequestParam("command") String command, HttpServletRequest request, HttpServletResponse response) throws Exception {
//		HttpGatewayChannel channel = HttpGatewayManager.getChannel(4);
//		Writer out = new PrintWriter(response.getOutputStream());
//
//		if ("add".equalsIgnoreCase(command)) {
//			String target = request.getParameter("target");
//			String typeStr = request.getParameter("type");
//			String networkTypeStr = request.getParameter("network_type");
//			String networkSource = request.getParameter("network_source");
//
//			if (target == null || target.trim().length() == 0) {
//				out.write("invalid parameter \"target\"");
//			} else if (typeStr == null || typeStr.trim().length() != 1) {
//				out.write("invalid parameter \"type\"");
//			} else if (networkTypeStr == null || networkTypeStr.trim().length() != 1) {
//				out.write("invalid parameter \"network_type\"");
//			} else if (networkSource == null || networkSource.trim().length() == 0) {
//				out.write("invalid parameter \"network_source\"");
//			} else {
//				int type = 0;
//				int networkType = 0;
//
//				try {
//					type = Integer.parseInt(typeStr.trim());
//				} catch (Exception e) {
//					out.write("invalid parameter \"type\"");
//					out.flush();
//					return;
//				}
//
//				try {
//					networkType = Integer.parseInt(networkTypeStr.trim());
//				} catch (Exception e) {
//					out.write("invalid parameter \"network_type\"");
//					out.flush();
//					return;
//				}
//				JSONObject result = channel.forwarderAdd(target, type, networkType, networkSource);
//				out.write(result.toString());
//			}
//
//		} else if ("remove".equalsIgnoreCase(command)) {
//			String target = request.getParameter("target");
//			String typeStr = request.getParameter("type");
//			String uuid = request.getParameter("uuid");
//			if (target == null || target.trim().length() == 0) {
//				out.write("invalid parameter \"target\"");
//			} else if (typeStr == null || typeStr.trim().length() != 1) {
//				out.write("invalid parameter \"type\"");
//			} else if (uuid == null || uuid.trim().length() == 0) {
//				out.write("invalid parameter \"uuid\"");
//			} else {
//				int type = 0;
//
//				try {
//					type = Integer.parseInt(typeStr.trim());
//				} catch (Exception e) {
//					out.write("invalid parameter \"type\"");
//					out.flush();
//					return;
//				}
//				JSONObject result = channel.forwarderRemove(target, type, uuid);
//				out.write(result.toString());
//			}
//		}
//
//		out.flush();
//	}
	
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
		if ("query".equalsIgnoreCase(command)) {
			HttpGatewayChannel channel = HttpGatewayManager.getChannel(Integer.valueOf(1));
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
	
}
