/**
 * Project Name:op
 * File Name:PlatformResourceMonitorServiceImpl.java
 * Package Name:com.zhicloud.op.service.impl
 * Date:2015年6月9日上午10:22:09
 * 
 *
*/ 

package com.zhicloud.op.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.RegionHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.helper.RegionHelper.RegionData;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.OperLogMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.PlatformResourceMonitorService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.service.constant.AppInconstant;
import com.zhicloud.op.vo.OperLogVO;
import com.zhicloud.op.vo.PlatformResourceMonitorVO;

/** 
/**
 * ClassName: PlatformResourceMonitorServiceImpl 
 * Function:  平台资源监控
 * date: 2015年6月9日 上午10:22:09 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public class PlatformResourceMonitorServiceImpl extends BeanDirectCallableDefaultImpl implements PlatformResourceMonitorService {
	
	public static final Logger logger = Logger.getLogger(PlatformResourceMonitorServiceImpl.class);

	/**
	 * 获取数据
	 * @see com.zhicloud.op.service.PlatformResourceMonitorService#getData()
	 */
	@Callable
	public void getData(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("PlatformResourceMonitorServiceImpl.getData()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8"); 
			List<PlatformResourceMonitorVO> dataList = new ArrayList<PlatformResourceMonitorVO>();// 分页结果
			RegionData[] regionDatas = RegionHelper.singleton.getAllResions();
			for (RegionData regionData : regionDatas) {	
				if(AppInconstant.platformResourceMonitorData.get(regionData.getId()) != null){					
					dataList.add(AppInconstant.platformResourceMonitorData.get(regionData.getId()));
				}
			} 
			// 更新获取时间
			synchronized(AppInconstant.platformResourceMonitorDataLastGet){				
				AppInconstant.platformResourceMonitorDataLastGet = new Date();
			}

			// 输出json数据
			response.getWriter().write(JSONLibUtil.toJSONString(dataList));
//			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), dataList.size(), dataList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		} 

	}

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @see com.zhicloud.op.service.PlatformResourceMonitorService#startMonistor()
	 */
	@Callable
	public MethodResult startMonistor() throws MalformedURLException, IOException {
		if(AppInconstant.regionAndTaskRelation.size()<=0){			
			RegionData[] regionDatas = RegionHelper.singleton.getAllResions();
			for (RegionData regionData : regionDatas) {			
				HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(regionData.getId());
				try {
					JSONObject result = channel.startSystemMonitor();
					if (HttpGatewayResponseHelper.isSuccess(result) == true) {
						AppInconstant.regionAndTaskRelation.put(result.getInt("task"), regionData.getId());
						System.err.println(String.format("success to start system monitor. task[%s]", result.getInt("task")));
					} else {
						System.err.println("fail to start system monitor.");
						channel.release();
					}
				}catch(Exception e) {
					System.err.println("fail to start system monitor.");
					channel.release();
					throw e;
				}
			}
		}
		return new MethodResult(MethodResult.SUCCESS,"启动成功");

	}

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @see com.zhicloud.op.service.PlatformResourceMonitorService#stopMonitor()
	 */
	@Callable
	public MethodResult stopMonitor() throws MalformedURLException, IOException {
		RegionData[] regionDatas = RegionHelper.singleton.getAllResions();
		for (RegionData regionData : regionDatas) {			
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(regionData.getId());
			try {
				JSONObject result = channel.stopSystemMonitor(regionData.getId());
				if (HttpGatewayResponseHelper.isSuccess(result) == true) {
					System.err.println(String.format("success to start system monitor. task[%s]", result.getInt("task")));
				} else {
					System.err.println("fail to start system monitor.");
					channel.release();
				}
			}catch(Exception e) {
				System.err.println("fail to start system monitor.");
				channel.release();
				throw e;
			}
		}
		return new MethodResult(MethodResult.SUCCESS,"关闭成功");
	}

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.zhicloud.op.service.PlatformResourceMonitorService#toMonitorPage(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Callable
	public String toMonitorPage(HttpServletRequest request,
			HttpServletResponse response) {  
		return "/security/operator/platform_resource_monitor.jsp";
		
	}

}

