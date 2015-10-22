/**
 * Project Name:op
 * File Name:PlatformResourceMonitorService.java
 * Package Name:com.zhicloud.op.service
 * Date:2015年6月9日上午9:39:03
 * 
 *
*/ 

package com.zhicloud.op.service; 

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;

/**
 * ClassName: PlatformResourceMonitorService 
 * Function:  平台资源监控service
 * date: 2015年6月9日 上午9:39:03 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface PlatformResourceMonitorService {
	/**
	 * 
	 * getData: 获取数据
	 *
	 * @author sasa
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public void getData(HttpServletRequest request,HttpServletResponse response);
	/**
	 * 
	 * startMonistor: 开启监控
	 *
	 * @author sasa
	 * @return MethodResult
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @since JDK 1.7
	 */
	public MethodResult startMonistor() throws MalformedURLException, IOException;
	/**
	 * 
	 * stopMonistor: 结束监控 
	 *
	 * @author sasa
	 * @return MethodResult
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @since JDK 1.7
	 */
	public MethodResult stopMonitor() throws MalformedURLException, IOException;
	/**
	 * 
	 * toMonitorPage: 跳转到监控页面
	 *
	 * @author sasa
	 * @param request
	 * @param response
	 * @return String
	 * @since JDK 1.7
	 */
	public String toMonitorPage(HttpServletRequest request, HttpServletResponse response);

}

