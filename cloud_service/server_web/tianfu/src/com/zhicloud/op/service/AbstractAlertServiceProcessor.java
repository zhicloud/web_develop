package com.zhicloud.op.service;

import java.util.List;

import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoExt;

/**
 * 服务异常处理器-抽象类
 * 
 * 在服务异常情况下，最快能0s检测到；最多10s就能检测到服务异常情况（网络畅通情况下）。
 * 如检测到服务异常持续10s（一个服务信息更新周期）以上，则每隔1s调用该类所有子类的对应方法，直到服务恢复正常或被移除。
 *
 */
public class AbstractAlertServiceProcessor {
	
	/**
	 * 异常状态事件:当检测到服务异常状态持续10s以上，每隔1s调用一遍。
	 * @param serverList
	 *            异常服务列表
	 */
	public void exceptionStatusEvent(List<ServiceInfoExt> serviceList) {
	}

	/**
	 * 恢复正常状态事件：当服务从异常状态（持续10s以上）恢复时调用，仅在恢复时调用一次。
	 * @param serverList
	 *            由异常状态恢复的服务列表
	 */
	public void recoverEvent(List<ServiceInfoExt> serviceList) {
	}
}
