package com.zhicloud.ms.service;

import java.io.IOException;
import java.net.MalformedURLException;

import com.zhicloud.ms.app.pool.network.NetworkInfoExt;

public interface INetworkService {

	public boolean createAsync(Integer region, String name, int netmask, String description, String pool) throws MalformedURLException, IOException;// 异步创建vpc，返回参数：消息发送成功与否

	public NetworkInfoExt createSync(Integer region, String name, int netmask, String description, String pool) throws MalformedURLException, IOException;// 同步创建vpc，成功，则返回NetworkInfoExt对象；失败，则返回空对象

	public NetworkInfoExt getCreateResult(Integer regionId, String name);// 获取异步创建vpc结果

	public NetworkInfoExt[] getAll(); // 获取所有vpc，只有uuid和name为有效。

	public boolean modifyAsync(Integer region, String uuid, String name, String description) throws MalformedURLException, IOException;// 异步修改vpc

	public boolean modifySync(Integer region, String uuid, String name, String description) throws MalformedURLException, IOException;// 同步修改vpc

	public int getModifyResult(String uuid);// 获取异步修改vpc结果，值：0-失败， 1-成功， -1-正在修改

	public boolean detailAsync(Integer region, String uuid) throws MalformedURLException, IOException;// 异步查询vpc详情

	public NetworkInfoExt detailSync(Integer region, String uuid) throws MalformedURLException, IOException;// 同步查询vpc详情.成功，则返回NetworkInfoExt对象；失败，则返回空对象

	public NetworkInfoExt getDetailResult(String uuid);// 获取异步查询vpc详情

	public boolean startAsync(Integer region, String uuid) throws MalformedURLException, IOException;// 异步启动vpc

	public boolean startSync(Integer region, String uuid) throws MalformedURLException, IOException;// 同步启动vpc

	public int getStartResult(String uuid);// 获取异步启动vpc结果，值：0-失败， 1-成功， -1-正在修改

	public boolean stopAsync(Integer region, String uuid) throws MalformedURLException, IOException;// 异步停止vpc

	public boolean stopSync(Integer region, String uuid) throws MalformedURLException, IOException;// 同步停止vpc

	public int getStopResult(String uuid);// 获取异步停止vpc结果，值：0-失败， 1-成功， -1-正在修改

	public boolean deleteAsync(Integer region, String uuid) throws MalformedURLException, IOException;// 异步删除vpc

	public boolean deleteSync(Integer region, String uuid) throws MalformedURLException, IOException;// 同步删除vpc

	public int getDeleteResult(String uuid);// 获取异步删除vpc结果，值：0-失败， 1-成功， -1-正在修改

	public boolean queryHostAsync(Integer region, String uuid) throws MalformedURLException, IOException;// 异步查询关联云主机

	public NetworkInfoExt queryHostSync(Integer region, String uuid) throws MalformedURLException, IOException;// 同步查询关联云主机.成功，则返回NetworkInfoExt对象；失败，则返回空对象

	public NetworkInfoExt getQueryHostResult(String uuid);// 获取异步查询关联云主机

	public String attachHostAsync(Integer region, String uuid, String hostUuid) throws MalformedURLException, IOException;// 异步关联云主机

	public NetworkInfoExt attachHostSync(Integer region, String uuid, String hostUuid) throws MalformedURLException, IOException;// 同步关联云主机

	public NetworkInfoExt getAttachHostResult(String sessionId, String uuid);// 获取异步关联云主机结果

	public String detachHostAsync(Integer region, String uuid, String hostUuid) throws MalformedURLException, IOException;// 异步移除云主机

	public boolean detachHostSync(Integer region, String uuid, String hostUuid) throws MalformedURLException, IOException;// 同步移除云主机

	public int getDetachHostResult(String sessionId, String uuid);// 获取异步移除云主机结果

	public String attachAddressAsync(Integer region, String uuid, int count) throws MalformedURLException, IOException;// 异步申请公网ip

	public NetworkInfoExt attachAddressSync(Integer region, String uuid, int count) throws MalformedURLException, IOException;// 同步申请公网ip

	public NetworkInfoExt getAttachAddressResult(String sessionId, String uuid);// 获取异步申请公网ip结果

	public String detachAddressAsync(Integer region, String uuid, String[] ip) throws MalformedURLException, IOException;// 异步移除公网ip

	public NetworkInfoExt detachAddressSync(Integer region, String uuid, String[] ip) throws MalformedURLException, IOException;// 同步移除公网ip

	public NetworkInfoExt getDetachAddressResult(String sessionId, String uuid);// 获取异步申请公网ip结果

	public String bindPortAsync(Integer region, String uuid, String[] protocolList, String[] ipList, String[] portList, String[] hostList, String[] hostPortList) throws MalformedURLException, IOException;// 异步绑定端口

	public NetworkInfoExt bindPortSync(Integer region, String uuid, String[] protocolList, String[] ipList, String[] portList, String[] hostList, String[] hostPortList) throws MalformedURLException, IOException;// 同步绑定端口

	public NetworkInfoExt getBindPortResult(String sessionId, String uuid);// 获取异步绑定端口

	public String unbindPortAsync(Integer region, String uuid, String[] protocolList, String[] ipList, String[] portList) throws MalformedURLException, IOException;// 异步解除绑定端口

	public NetworkInfoExt unbindPortSync(Integer region, String uuid, String[] protocolList, String[] ipList, String[] portList) throws MalformedURLException, IOException;// 同步解除绑定端口

	public NetworkInfoExt getUnbindPortResult(String sessionId, String uuid);// 获取异步解除绑定端口
}
