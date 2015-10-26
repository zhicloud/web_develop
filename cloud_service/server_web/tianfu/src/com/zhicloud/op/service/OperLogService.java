package com.zhicloud.op.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface OperLogService {
	public String managePage(HttpServletRequest request, HttpServletResponse response);
	public void queryOperLog(HttpServletRequest request, HttpServletResponse response);
	public String managePageForAgent(HttpServletRequest request, HttpServletResponse response);
	public void queryOperLogForAgent(HttpServletRequest request, HttpServletResponse response);
	/**
	 * 
	 * addNewOperLog: 添加操作日志 
	 *
	 * @author sasa
	 * @param userId
	 * @param status
	 * @param content
	 * @param resourceName
	 * @param operDuration void
	 * @since JDK 1.7
	 */
	public void addNewOperLog(String userId,Integer status,String content,String resourceName,String operDuration);
}
