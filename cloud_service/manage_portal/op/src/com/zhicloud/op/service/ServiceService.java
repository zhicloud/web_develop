/**
 * Project Name:op
 * File Name:VpnService.java
 * Package Name:com.zhicloud.op.service
 * Date:2015年4月1日下午2:12:43
 * 
 *
*/ 

package com.zhicloud.op.service;  	  

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface ServiceService {
	
	public String managePage(HttpServletRequest request,HttpServletResponse response); 
	
	public void queryService(HttpServletRequest request,HttpServletResponse response);
	
}

