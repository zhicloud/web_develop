package com.zhicloud.op.service;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;



public interface StatementService {

	// --------------------

	public String managePage(HttpServletRequest request,HttpServletResponse response);

	public String businessGraphicsPage(HttpServletRequest request,HttpServletResponse response);
	
	public String databaseMonitorPage(HttpServletRequest request,HttpServletResponse response);
	
	//---------
	public MethodResult businessGraphicsByTime(Map<String, Object> parameter);
	
	public MethodResult businessGraphicsByTimeAndOthers(Map<String, Object> parameter);
	
	public MethodResult getCpuAndMemData();
	
	public MethodResult getDiskData();
}
