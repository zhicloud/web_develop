package com.zhicloud.op.service.impl;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.cache.annotation.Cacheable;

import com.zhicloud.op.app.helper.RegionHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.helper.RegionHelper.RegionData;
import com.zhicloud.op.app.pool.network.NetworkInfoExt;
import com.zhicloud.op.app.pool.network.NetworkInfoSessionPool;
import com.zhicloud.op.app.pool.network.NetworkPoolManager;
import com.zhicloud.op.app.pool.rule.RuleInfo;
import com.zhicloud.op.app.pool.rule.RuleInfoPool;
import com.zhicloud.op.app.pool.rule.RulePoolManager;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoExt;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoPool;
import com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoPoolManager;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.constant.NodeTypeDefine;
import com.zhicloud.op.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.IntelligentRouterService;
import com.zhicloud.op.vo.IntelligentRouterVO;

public class IntelligentRouterServiceImpl extends BeanDirectCallableDefaultImpl implements IntelligentRouterService {
	
    public static final Logger logger = Logger.getLogger(IntelligentRouterServiceImpl.class);


	@Callable
	public String queryRulePage(HttpServletRequest request,HttpServletResponse response) {
		return "/security/operator/intelligent_router_rule.jsp";
	}

	@Callable
	public MethodResult deleteRule(String target,JSONArray ip,JSONArray port,Integer region,Integer mode) { 
		try {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
			String[] ipStr = new String[]{"",""};
			Integer[] portInt = new Integer[]{0,0};
			for(int i=0;i<ip.size();i++){
				ipStr[i] = StringUtil.trim(ip.get(i));
			}
			for(int i=0;i<port.size();i++){
				portInt[i] = (Integer)port.get(i);
			}		
 			RuleInfo info = this.deleteRuleAsync(target, mode, ipStr, portInt, channel);
			if(info==null){
				return new MethodResult(MethodResult.FAIL,"删除失败");
			}
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new MethodResult(MethodResult.SUCCESS,"删除成功");
	}

	@Callable
	public MethodResult addRule(Map<String, Object> parameter) { 
		
		String target   = StringUtil.trim(parameter.get("markId"));
		Integer region  = Integer.valueOf((String) parameter.get("region"));
		Integer mode  = Integer.valueOf((String) parameter.get("mode"));
 		String[] ip = new String[]{};
		Integer[] port = new Integer[]{0,0};
		if(mode==0){
			String ip1 = StringUtil.trim(parameter.get("ip1"));
			String ip2 = StringUtil.trim(parameter.get("ip2"));
			ip = new String[]{ip1,ip2};
			port = new Integer[]{Integer.valueOf((String) parameter.get("port1")),0};
		}else if(mode==1){
			String ip1 = StringUtil.trim(parameter.get("ip1"));
			String ip2 = StringUtil.trim(parameter.get("ip2"));
			ip = new String[]{ip1,ip2};
		}else if(mode == 2){
			String ip1 = StringUtil.trim(parameter.get("ip1"));
			String ip2 = StringUtil.trim(parameter.get("ip2"));
			ip = new String[]{ip1,ip2};
			port = new Integer[]{Integer.valueOf((String) parameter.get("port1")),Integer.valueOf((String) parameter.get("port2"))};
		}
 
		try {
			HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
 			RuleInfo info = this.addRuleAsync(target, mode, ip, port, channel);
			if(info==null){
				return new MethodResult(MethodResult.FAIL,"新增失败");
			} 
 			channel.release();
 		} catch (Exception e) {
			System.err.println("fail to send add rule request.");  
		}
		return new MethodResult(MethodResult.SUCCESS,"新增成功");
	}

	@Callable
	public void queryRule(HttpServletRequest request, HttpServletResponse response) { 
		try {
			String region = StringUtil.trim(request.getParameter("region"));
             if(StringUtil.isBlank(region)){
            	region = "1";
            }
             HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(Integer.parseInt(region)); 
             ServiceInfoExt[] serviceInfoArray = new ServiceInfoExt[0];
			JSONObject resultInfo = channel.serviceQuery( NodeTypeDefine.INTELLIGENT_REOUTER, "default");
			if (HttpGatewayResponseHelper.isSuccess(resultInfo) == false) {
				logger.warn(String.format("fail to fetch service information. node_type[%s], group[%s], region[%s]", NodeTypeDefine.INVALID, "default", region));
 			}else{
 			// 返回数据处理
 				List<JSONObject> services = (List<JSONObject>) resultInfo.get("services");
 				serviceInfoArray = new ServiceInfoExt[services.size()];
 				int count = 0 ;
 				for (JSONObject serv : services) {
 					String name = serv.getString("name");
 					String ip = serv.getString("ip");
 					int port = serv.getInt("port");
 					int status = serv.getInt("status");
 					String version = serv.getString("version");

 					ServiceInfoExt serviceInfo = new ServiceInfoExt(); 
 					serviceInfo.setRegion(Integer.parseInt(region));
 					serviceInfo.setType(NodeTypeDefine.INVALID);
 					serviceInfo.setGroup("default");
 					serviceInfo.setName(name);
 					serviceInfo.setIp(ip);
 					serviceInfo.setPort(port);
 					serviceInfo.setStatus(status);
 					serviceInfo.setVersion(version);
 					serviceInfo.updateTime();
 	 
 					serviceInfoArray[count]=serviceInfo;
 					count++;
 				}
 			}

			
 		HttpGatewayAsyncChannel channel1 = HttpGatewayManager.getAsyncChannel(Integer.parseInt(region));
		List<IntelligentRouterVO> ruleList = new ArrayList<IntelligentRouterVO>(); 
 		for(ServiceInfoExt vo : serviceInfoArray){
			if(vo.getName().contains("intelligent")&&vo.getRegion() == Integer.parseInt(region)){
				JSONObject result = channel1.ruleQuery(vo.getName());
				List<RuleInfo> infoList = this.queryRuleSync(vo.getName(), channel1);
				String target = vo.getName();
				if(infoList != null){
 					for(int i = 1;i<infoList.size();i++){
						IntelligentRouterVO rule = new IntelligentRouterVO();
						RuleInfo info = infoList.get(i);
						rule.setTarget(target);
						rule.setMode(Integer.parseInt(info.getMode()[0]));
						if(info.getIpList()==null||info.getIpList().length<=0){
							continue;
						}
						rule.setIp(info.getIpList());
						rule.setPort(info.getPortList());
						rule.setRegion(Integer.parseInt(region));
						ruleList.add(rule);
					}
				}
			}
		}
		
		List<ServiceInfoExt> javaList = Arrays.asList(serviceInfoArray);   
		
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), ruleList.size(), ruleList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@Callable
	public String addRulePage(HttpServletRequest request, HttpServletResponse response) {
		try {
			RegionData[] regions = RegionHelper.singleton.getAllResions();
			 List<ServiceInfoExt> serviceInfoArray = new ArrayList<ServiceInfoExt>();
 			for (RegionData region : regions) {// 遍历所有区域的http_gateway
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(region.getId());
		        
				JSONObject resultInfo;
				
					resultInfo = channel.serviceQuery( NodeTypeDefine.INTELLIGENT_REOUTER, "default");
				
				if (HttpGatewayResponseHelper.isSuccess(resultInfo) == false) {
					logger.warn(String.format("fail to fetch service information. node_type[%s], group[%s], region[%s]", NodeTypeDefine.INVALID, "default", region));
				}else{
				// 返回数据处理
					List<JSONObject> services = (List<JSONObject>) resultInfo.get("services");
 					
					for (JSONObject serv : services) {
						String name = serv.getString("name");
						String ip = serv.getString("ip");
						int port = serv.getInt("port");
						int status = serv.getInt("status");
						String version = serv.getString("version");
		
						ServiceInfoExt serviceInfo = new ServiceInfoExt(); 
						serviceInfo.setRegion(region.getId());
						serviceInfo.setType(NodeTypeDefine.INVALID);
						serviceInfo.setGroup("default");
						serviceInfo.setName(name);
						serviceInfo.setIp(ip);
						serviceInfo.setPort(port);
						serviceInfo.setStatus(status);
						serviceInfo.setVersion(version);
						serviceInfo.updateTime();
		 
						serviceInfoArray.add(serviceInfo);
 					}
				}
			}
			request.setAttribute("targetarray", serviceInfoArray);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		
		
		return "/security/operator/intelligent_router_rule_add.jsp";
	}
	
	// 同步查询规则
	public List<RuleInfo> queryRuleSync(String target, HttpGatewayAsyncChannel channel) throws MalformedURLException, IOException {
		String sessionId = this.queryRuleAsync(target, channel);
		if (sessionId != null) {
			RuleInfoPool pool = RulePoolManager.singleton().getInfoPool();
			List<RuleInfo> info = pool.get(sessionId);
			synchronized (info) {//wait for 5 second or notify by response message.
				try {
					info.wait(5 * 1000);
				} catch (InterruptedException e) {
					logger.error("error occur when waiting attach network address response call back.", e);
				}
			}
			
			int status = info.get(0).getAsyncStatus();
			if (status == 1) {//成功
				return  pool.getDuplication(sessionId);
			} else {//失败或无应答
				return null;
			}
		} else {
			return null;
		}
	}
	// 异步查询规则
	public String queryRuleAsync(String target, HttpGatewayAsyncChannel channel) throws MalformedURLException, IOException {
 		JSONObject result = null;

		try {
			result = channel.ruleQuery(target);
		} catch (Exception e) {
			logger.error(String.format("fail to attach address to network. exception[%s]", e));
			channel.release();
			throw e;
		}

		if (HttpGatewayResponseHelper.isSuccess(result) == true) {
			String sessionId = channel.getSessionId();
			List<RuleInfo> ruleList = new ArrayList<RuleInfo>();
			RuleInfo rule = new RuleInfo();
 			rule.setSessionId(sessionId);
 			rule.setMessage("");
			rule.initAsyncStatus();
			ruleList.add(rule);
 			
			RuleInfoPool pool = RulePoolManager.singleton().getInfoPool();
			pool.put(ruleList);
			
			return sessionId;
		} else {
			logger.warn(String.format("fail to attach address to network. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
			channel.release();
			return null;
		}
	}
	
	// 异步删除规则
	public RuleInfo deleteRuleAsync(String target, int mode, String[] ip, Integer[] port, HttpGatewayAsyncChannel channel) throws MalformedURLException, IOException {
 		JSONObject result = null;

		try {
			result = channel.ruleRemove(target, mode, ip, port);
		} catch (Exception e) {
			logger.error(String.format("fail to attach address to network. exception[%s]", e));
			channel.release();
			throw e;
		}

		if (HttpGatewayResponseHelper.isSuccess(result) == true) {
			String sessionId = channel.getSessionId();
			List<RuleInfo> ruleList = new ArrayList<RuleInfo>();
			RuleInfo rule = new RuleInfo();
 			rule.setSessionId(sessionId);
 			rule.setMessage("");
			rule.initAsyncStatus();
			ruleList.add(rule);
 			
			RuleInfoPool pool = RulePoolManager.singleton().getInfoPool();
			pool.put(ruleList);
			
			if (sessionId != null) {
 				List<RuleInfo> info = pool.get(sessionId);
				synchronized (info) {//wait for 5 second or notify by response message.
					try {
						info.wait(8 * 1000);
					} catch (InterruptedException e) {
						logger.error("error occur when waiting attach network address response call back.", e);
					}
				}
				
				int status = info.get(0).getAsyncStatus();
				if (status == 1) {//成功
					return  pool.getDuplication(sessionId).get(0);
				} else {//失败或无应答
					return null;
				}
			} else {
				return null;
			} 
		} else {
			logger.warn(String.format("fail to attach address to network. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
			channel.release();
			return null;
		}
	}
	
	// 异步删除规则
		public RuleInfo addRuleAsync(String target, int mode, String[] ip, Integer[] port, HttpGatewayAsyncChannel channel) throws MalformedURLException, IOException {
	 		JSONObject result = null;

			try {
				result = channel.ruleAdd(target, mode, ip, port);
			} catch (Exception e) {
				logger.error(String.format("fail to add rule. exception[%s]", e));
				channel.release();
				throw e;
			}

			if (HttpGatewayResponseHelper.isSuccess(result) == true) {
				String sessionId = channel.getSessionId();
				List<RuleInfo> ruleList = new ArrayList<RuleInfo>();
				RuleInfo rule = new RuleInfo();
	 			rule.setSessionId(sessionId);
	 			rule.setMessage("");
				rule.initAsyncStatus();
				ruleList.add(rule);
	 			
				RuleInfoPool pool = RulePoolManager.singleton().getInfoPool();
				pool.put(ruleList);
				
				if (sessionId != null) {
	 				List<RuleInfo> info = pool.get(sessionId);
					synchronized (info) {//wait for 5 second or notify by response message.
						try {
							info.wait(8 * 1000);
						} catch (InterruptedException e) {
							logger.error("error occur when waiting attach network address response call back.", e);
						}
					}
					
					int status = info.get(0).getAsyncStatus();
					if (status == 1) {//成功
						return  pool.getDuplication(sessionId).get(0);
					} else {//失败或无应答
						return null;
					}
				} else {
					return null;
				} 
			} else {
				logger.warn(String.format("fail to attach address to network. message[%s]", HttpGatewayResponseHelper.getMessage(result)));
				channel.release();
				return null;
			}
		}

}
