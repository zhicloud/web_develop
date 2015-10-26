package com.zhicloud.op.httpGateway;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.zhicloud.op.app.propeties.AppProperties;

public class HttpGatewayAsyncChannel extends HttpGatewayChannelExt {

	private final String callback;
	private long lastReceiveTime;

	public HttpGatewayAsyncChannel(int region) {
		super(region);

		String httpGatewayAddr = AppProperties.getValue("address_of_this_system");
		if (httpGatewayAddr.endsWith("/") == false) {
			httpGatewayAddr += "/";
		}
		this.callback = httpGatewayAddr + "hgMessage/push.do";
	}

	public String getSessionId() {
		if (this.getHelper() == null) {
			return null;
		}
		return this.getHelper().getSessionId();
	}

	public void release() {
		HttpGatewayManager.releseAsyncChannel(this);
	}

	public JSONObject decryptReceivedData(HttpServletRequest request) throws IOException {
		return getHelper().decryptReceivedData(request);
	}

	public byte[] encrypt(Map<String, String> data) {
		return this.getHelper().encrypt(data);
	}

	public synchronized void updateReceiveTime() {
		this.lastReceiveTime = new Date().getTime();
	}

	public synchronized long getLastReceiveTime() {
		return lastReceiveTime;
	}

	/**
	 * 更新接收异步消息时间为当前时间，并调用父类checkSessionRefresh()
	 * 
	 * @throws IOException
	 * @throws MalformedURLException
	 */
	@Override
	protected synchronized void checkSessionRefresh() throws MalformedURLException, IOException {
		super.checkSessionRefresh();
		this.updateReceiveTime();
	}

	public synchronized JSONObject hostStartMonitor(String[] target) throws MalformedURLException, IOException {
		return this.hostStartMonitor(target, this.callback);
	}

	public synchronized JSONObject diskImageCreateAsync(String name, String uuid, String description, String[] identity, String group, String user) throws MalformedURLException, IOException {
		return this.diskImageCreateAsync(name, uuid, description, identity, group, user, this.callback);
	}

	public synchronized JSONObject isoImageUpload(String name, String target, String description, String group, String user) throws MalformedURLException, IOException {
		return this.isoImageUpload(name, target, description, group, user, this.callback);
	}

	public synchronized JSONObject networkCreate(String name, int netmask, String description, String pool) throws MalformedURLException, IOException {
		return this.networkCreate(name, netmask, description, pool, this.callback);
	}

	public synchronized JSONObject networkQuery() throws MalformedURLException, IOException {
		return this.networkQuery(this.callback);
	}

	public synchronized JSONObject networkModify(String uuid, String name, String description) throws MalformedURLException, IOException {
		return this.networkModify(uuid, name, description, this.callback);
	}

	public synchronized JSONObject networkDetail(String uuid) throws MalformedURLException, IOException {
		return this.networkDetail(uuid, this.callback);
	}

	public synchronized JSONObject networkStart(String uuid) throws MalformedURLException, IOException {
		return this.networkStart(uuid, this.callback);
	}

	public synchronized JSONObject networkStop(String uuid) throws MalformedURLException, IOException {
		return this.networkStop(uuid, this.callback);
	}

	public synchronized JSONObject networkDelete(String uuid) throws MalformedURLException, IOException {
		return this.networkDelete(uuid, this.callback);
	}

	public synchronized JSONObject networkQueryHost(String uuid) throws MalformedURLException, IOException {
		return this.networkQueryHost(uuid, this.callback);
	}

	public synchronized JSONObject networkAttachHost(String uuid, String hostUuid) throws MalformedURLException, IOException {
		return this.networkAttachHost(uuid, hostUuid, this.callback);
	}

	public synchronized JSONObject networkDetachHost(String uuid, String hostUuid) throws MalformedURLException, IOException {
		return this.networkDetachHost(uuid, hostUuid, this.callback);
	}

	public synchronized JSONObject networkAttachAddress(String uuid, int count) throws MalformedURLException, IOException {
		return this.networkAttachAddress(uuid, count, this.callback);
	}

	public synchronized JSONObject networkDetachAddress(String uuid, String[] ip) throws MalformedURLException, IOException {
		return this.networkDetachAddress(uuid, ip, this.callback);
	}

	public synchronized JSONObject networkBindPort(String uuid, String[] protocolList, String[] ipList, String[] portList, String[] hostList, String[] hostPortList) throws MalformedURLException, IOException {
		return this.networkBindPort(uuid, protocolList, ipList, portList, hostList, hostPortList, this.callback);
	}

	public synchronized JSONObject networkUnbindPort(String uuid, String[] protocolList, String[] ipList, String[] portList) throws MalformedURLException, IOException {
		return this.networkUnbindPort(uuid, protocolList, ipList, portList, this.callback);
	}

	public synchronized JSONObject startSystemMonitor() throws MalformedURLException, IOException {
		return this.startSystemMonitor(this.callback);
	}

	public synchronized JSONObject snapshotPoolQuery() throws MalformedURLException, IOException {
		return this.snapshotPoolQuery(this.callback);
	}

	public synchronized JSONObject snapshotPoolCreate(String name) throws MalformedURLException, IOException {
		return this.snapshotPoolCreate(name, this.callback);
	}

	public synchronized JSONObject snapshotPoolModify(String uuid, String name) throws MalformedURLException, IOException {
		return this.snapshotPoolModify(uuid, name, this.callback);
	}

	public synchronized JSONObject snapshotPoolDelete(String uuid) throws MalformedURLException, IOException {
		return this.snapshotPoolDelete(uuid, this.callback);
	}

	public synchronized JSONObject snapshotPoolAddNode(String pool, String nodeName) throws MalformedURLException, IOException {
		return this.snapshotPoolAddNode(pool, nodeName, this.callback);
	}

	public synchronized JSONObject snapshotPoolRemoveNode(String pool, String nodeName) throws MalformedURLException, IOException {
		return this.snapshotPoolRemoveNode(pool, nodeName, this.callback);
	}

	public synchronized JSONObject snapshotPoolQueryNode(String pool) throws MalformedURLException, IOException {
		return this.snapshotPoolQueryNode(pool, this.callback);
	}

	public synchronized JSONObject snapshotQuery(String uuid) throws MalformedURLException, IOException {
		return this.snapshotQuery(uuid, this.callback);
	}

	public synchronized JSONObject snapshotCreate(String target, String name) throws MalformedURLException, IOException {
		return this.snapshotCreate(target, name, this.callback);
	}

	public synchronized JSONObject snapshotDelete(String uuid) throws MalformedURLException, IOException {
		return this.snapshotDelete(uuid, this.callback);
	}

	public synchronized JSONObject snapshotResume(String uuid) throws MalformedURLException, IOException {
		return this.snapshotResume(uuid, this.callback);
	}
}
