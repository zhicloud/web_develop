package com.zhicloud.ms.httpGateway;


import com.zhicloud.ms.app.propeties.AppProperties;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.Map;

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

	public synchronized JSONObject isoImageUpload(String name, String target, String description, String group, String user,int diskType,String uuid) throws MalformedURLException, IOException {
		return this.isoImageUpload(name, target, description, group, user,diskType,uuid, this.callback);
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

    public synchronized JSONObject hostFlushDiskImage(String uuid, int disk, int mode, String image) throws MalformedURLException, IOException {
        return this.hostFlushDiskImage(uuid, disk, mode, image, this.callback);
    }

	public synchronized JSONObject hostBackup(String uuid, int mode, int disk) throws MalformedURLException, IOException {
		return this.hostBackup(uuid, mode, disk, this.callback);
	}

	public synchronized JSONObject hostResume(String uuid, int mode, int disk) throws MalformedURLException, IOException {
		return this.hostResume(uuid, mode, disk, this.callback);
	}

	public synchronized JSONObject hostQueryBackup(String uuid) throws MalformedURLException, IOException {
		return this.hostQueryBackup(uuid, this.callback);
	}

	public synchronized JSONObject snapshotQuery(int type,String uuid, int mode, int index) throws MalformedURLException, IOException {
		return this.snapshotQuery(type,uuid,mode,index,this.callback);
	}	

	public synchronized JSONObject snapshotDel(int type, String uuid, int mode, String snapshotId) throws MalformedURLException, IOException {
		return this.snapshotDel(type,uuid,mode,snapshotId,this.callback);
	}	
	
	public synchronized JSONObject snapshotCreate(int type, String uuid, int mode, String index) throws MalformedURLException, IOException {
		return this.snapshotCreate(type,uuid,mode,index,this.callback);
	}	
	
	public synchronized JSONObject snapshotResume(int type, String uuid, int mode, String snapshotId) throws MalformedURLException, IOException {
		return this.snapshotResume(type,uuid,mode,snapshotId,this.callback);
	}	
	
	public synchronized JSONObject ruleAdd(String target, int mode, String[] ip, Integer[] port) throws MalformedURLException, IOException {
		return this.ruleAdd(target, mode, ip, port, this.callback);
	}

	public synchronized JSONObject ruleRemove(String target, int mode, String[] ip, Integer[] port) throws MalformedURLException, IOException {
		return this.ruleRemove(target, mode, ip, port, this.callback);
	}

	public synchronized JSONObject ruleQuery(String target) throws MalformedURLException, IOException {
		return this.ruleQuery(target, this.callback);
	}

    public synchronized JSONObject computePoolCreate(String name, Integer networkType, String network, Integer diskType, String diskSource, Integer[] mode, String path, String crypt) throws MalformedURLException, IOException {
        return this.computePoolCreate(name, networkType, network, diskType, diskSource, mode, path, crypt, this.callback);
    }

    public synchronized JSONObject computePoolModify(String uuid, String name, int networkType, String network, int diskType, String diskSource, Integer[] mode, int option,String path, String crypt) throws MalformedURLException, IOException {
        return this.computePoolModify(uuid, name, networkType, network, diskType, diskSource, mode, option, path,crypt, this.callback);
    }

    public synchronized JSONObject computePoolDetail(String uuid) throws MalformedURLException, IOException {
        return this.computePoolDetail(uuid, this.callback);
    }
	
   public synchronized JSONObject startMonitor(int level, String[] target) throws MalformedURLException, IOException {
        return this.startMonitor(level, target, this.callback);
    }
   
   public synchronized JSONObject stopMonitor(int task) throws MalformedURLException, IOException {
       return this.stopMonitor(task, this.callback);
   }
   
   public synchronized JSONObject hostReset(String uuid) throws MalformedURLException, IOException {
       return this.hostReset(uuid, this.callback);
   }
	public synchronized JSONObject serverQueryStorageDevice(int level, String target, int diskType) throws MalformedURLException, IOException {
		return this.serverQueryStorageDevice(level, target, diskType, this.callback);
	}

	public synchronized JSONObject serverAddStorageDevice(int level, String target, int diskType, Integer[] index, String name, String path, String crypt) throws MalformedURLException, IOException {
		return this.serverAddStorageDevice(level, target, diskType, index, name, path, crypt, this.callback);
	}

	public synchronized JSONObject serverRemoveStorageDevice(int level, String target, int diskType, int index) throws MalformedURLException, IOException {
		return this.serverRemoveStorageDevice(level, target, diskType, index, this.callback);
	}

	public synchronized JSONObject serverEnableStorageDevice(int level, String target, int diskType, int index) throws MalformedURLException, IOException {
		return this.serverEnableStorageDevice(level, target, diskType, index, this.callback);
	}

	public synchronized JSONObject serverDisableStorageDevice(int level, String target, int diskType, int index) throws MalformedURLException, IOException {
		return this.serverDisableStorageDevice(level, target, diskType, index, this.callback);
	}
	
	
	/**
	 * 
	* @Title: hostMigrate 
	* @Description: 主机迁移 
	* @param @param uuid
	* @param @param target
	* @param @param type
	* @param @return
	* @param @throws MalformedURLException
	* @param @throws IOException      
	* @return JSONObject     
	* @throws
	 */
	public synchronized JSONObject hostMigrate(String uuid, String target, int type) throws MalformedURLException, IOException {
        return this.hostMigrate(uuid, target, type, this.callback);
    }

    public synchronized JSONObject serviceModify(int type, String target, int diskType, String diskSource, String crypt) throws MalformedURLException, IOException {
        return this.serviceModify(type, target, diskType, diskSource, crypt, this.callback);
    }
    
    public synchronized JSONObject query_service_detail(String target, int level, String begin, String end)
            throws MalformedURLException, IOException {
        return this.query_service_detail(target, level, begin, end, this.callback);
    }

    public synchronized JSONObject query_service_summary(String[] target, String begin, String end)
            throws MalformedURLException, IOException {
        return this.query_service_summary(target, begin, end, this.callback);
    }

    public synchronized JSONObject query_operate_detail(String target, int level, String begin, String end)
            throws MalformedURLException, IOException {
        return this.query_operate_detail(target, level, begin, end, this.callback);
    }

    public synchronized JSONObject query_operate_summary(String[] target, String begin, String end)
            throws MalformedURLException, IOException {
        return this.query_operate_summary(target, begin, end, this.callback);
    }
	
}
