package com.zhicloud.op.service;

import java.util.List;

import com.zhicloud.op.app.pool.serverInfoPool.ServerInfoExt;

/**
 * 服务器异常处理器-抽象类
 * 
 * 在服务器异常情况下，最快能0s检测到；最多10s就能检测到服务器异常情况（网络畅通情况下）。
 * 如检测到服务器异常情况，则每隔1s调用该类所有子类的对应方法，直到服务器恢复正常或被移除。
 *
 */
public abstract class AbstractAlertServerProcessor {
	
	/**
	 * 异常状态事件:当检测到服务器异常状态，每隔1s调用一遍。
	 * @param serverList 异常服务器列表
	 */
	public void serverExceptionStatusEvent(List<ServerInfoExt> serverList) {}
	
	/**
	 * 恢复正常状态事件：当服务器从异常状态恢复时调用，仅在恢复时调用一次。
	 * @param serverList 由异常状态恢复的服务器列表
	 */
	public void serverRecoverEvent(List<ServerInfoExt> serverList) {}

}
