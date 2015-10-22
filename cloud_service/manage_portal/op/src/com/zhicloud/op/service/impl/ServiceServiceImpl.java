/**
 * Project Name:op
 * File Name:VpnServiceImpl.java
 * Package Name:com.zhicloud.op.service.impl
 * Date:2015年4月1日下午2:16:34
 * 
 *
*/ 

package com.zhicloud.op.service.impl;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoExt;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoPool;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoPoolManager;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.service.ServiceService;
import com.zhicloud.op.service.constant.MonitorConstant;
 
/**
 * ClassName: VpnServiceImpl  
 * date: 2015年4月1日 下午2:16:34 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public class ServiceServiceImpl extends BeanDirectCallableDefaultImpl implements ServiceService {

	@Callable
	public String managePage(HttpServletRequest request,HttpServletResponse response) {
		request.setAttribute("userType", LoginHelper.getLoginInfo(request).getUserType());
 		return "/security/operator/service_manage.jsp";
	}
    /**
     *  查询服务数据
    * @param request
    * @param response 
    * @see com.zhicloud.op.service.ServiceService#queryService(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
	@Callable
	public void queryService(HttpServletRequest request,HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			//参数处理
			String status = request.getParameter("query_by_status");
            // 初始化状态默认值
            if (status == null || "".equals(status)) {
                status = MonitorConstant.status_error;
            }
			String region = request.getParameter("region");
			String serviceName = StringUtil.trim(request.getParameter("service_name"));
			if("all".equals(region)){
				region = null;
			}
			if(StringUtil.isBlank(serviceName)){
				serviceName = null;
			}
			ServiceInfoPool pool = ServiceInfoPoolManager.singleton().getPool();
			ServiceInfoExt[] services = pool.getAll();
			List<ServiceInfoExt> serviceList = Arrays.asList(services);
			List<ServiceInfoExt> newList = new ArrayList<>();
	         //状态数量值
            int r_normal = 0, r_warning = 0, r_error = 0, r_stop = 0;
            if (status != null || region != null || serviceName != null) {
                for (ServiceInfoExt service : serviceList) {
                    String newstatus = service.getNewStatus();
                    if (MonitorConstant.status_stop.equals(newstatus)) {
                        r_stop++;
                    } else if (MonitorConstant.status_error.equals(newstatus)) {
                        r_error++;
                    } else if (MonitorConstant.status_warn.equals(newstatus)) {
                        r_warning++;
                    } else if (MonitorConstant.status_normal.equals(newstatus)) {
                        r_normal++;
                    } else {
                        r_normal++;
                    }
                    if (status != null && !(status.equals(newstatus)) && !"all".equals(status)) {
                        continue;
                    }
                    if (region != null && !(service.getRegion() == Integer.parseInt(region))) {
                        continue;
                    }
                    if (serviceName != null && !(service.getName().toLowerCase().contains(serviceName.toLowerCase()))) {
                        continue;
                    }
                    newList.add(service);
                }
            } else {
                newList = serviceList;
            }
			int total = newList.size();
			
            Map<String, String> map = new LinkedHashMap<String, String>();
            map.put("normal", r_normal + "");
            map.put("warning", r_warning + "");
            map.put("error", r_error + "");
            map.put("stop", r_stop + "");
			// 输出json数据
            ServiceHelper.writeMapResultAsJsonTo(response.getOutputStream(), total, newList, map);
		} catch (Exception e) {
			throw new AppException("失败");
		}
		
	}

}
 
