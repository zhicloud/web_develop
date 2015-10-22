package com.zhicloud.op.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.zhicloud.op.app.pool.CloudHostData;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.CloudHostVO;

public interface CloudHostService
{
	
	// --------------------
	
	public String selfUseCloudUsePage(HttpServletRequest request, HttpServletResponse response);
	
	public String cloudHostQueryResultPartPage(HttpServletRequest request, HttpServletResponse response);
	
	public String addPortPage(HttpServletRequest request, HttpServletResponse response);
	
	public String modifyAllocationPage(HttpServletRequest request, HttpServletResponse response);
	
	public String cloudTerminalBindingPage(HttpServletRequest request, HttpServletResponse response);
	
	public String updatePasswordPage(HttpServletRequest request, HttpServletResponse response);

	public String createCloudHostPage(HttpServletRequest request, HttpServletResponse response);
	
	public String cloudHostViewDetailPage(HttpServletRequest request, HttpServletResponse response);

	public String startCloudHostPage(HttpServletRequest request, HttpServletResponse response);

	public String restartCloudHostPage(HttpServletRequest request, HttpServletResponse response);
	
	public String insertIsoImagePage(HttpServletRequest request, HttpServletResponse response);
	
	public String managePage(HttpServletRequest request, HttpServletResponse response);
	
	public String cloudHostPage(HttpServletRequest request, HttpServletResponse response);
	
	public String modifyPortPage(HttpServletRequest request, HttpServletResponse response);
	
	public String manageDomainPage(HttpServletRequest request, HttpServletResponse response);
	
	public String addDomainPage(HttpServletRequest request, HttpServletResponse response);
	
	
	// --------------
	
	
	
	public void queryCloudHost(HttpServletRequest request, HttpServletResponse response);
	
	public void getDomain(HttpServletRequest request, HttpServletResponse response);
	
	
	
	//---------------
	
	public List<CloudHostVO> getAllCloudHost();
	
	public int updateRunningStatusByRealHostId(Map<String, Object> data);
	
	// ---------------
	
	public MethodResult addCloudHost(Map<String, Object> parameter);
	
	public MethodResult addPort(Map<String, Object> parameter);
	
	public MethodResult cloudTerminalBinding(Map<String, Object> parameter);
	
	public MethodResult deleteCloudHostById(String hostId);
	
	public MethodResult deleteCloudHostByIds(List<?> hostIds);
	
	public MethodResult operatorDeleteCloudHostByIds(List<?> hostIds);
	
	public MethodResult startCloudHost(String hostId);
	
	public MethodResult startCloudHostFromIsoImage(String hostId, String realIsoImageId);
	
	public MethodResult haltCloudHost(String hostId);
	
	public MethodResult stopCloudHost(String hostId);
	
	public MethodResult restartCloudHost(String hostId);
	
	public MethodResult restartCloudHostFromIsoImage(String hostId, String realIsoImageId);
	
	public MethodResult insertIsoImage(String hostId, String realIsoImageId);
	
	public MethodResult updatePassword(Map<String, Object> parameter);
	
	public MethodResult updateHostNameById(String hostId,String hostName);
	
	public MethodResult popupIsoImage(String hostId);
	
	public MethodResult getCloudHostCreationResult(String hostId);
	
	public MethodResult inactivateCloudHost(String hostId);

	public MethodResult reactivateCloudHost(String hostId);
	
	public MethodResult checkPortIsOpened(String hostId, Integer port);
	
	public MethodResult checkDomainAvaliable(String domain);
	
	public MethodResult addDomain(Map<String, Object> parameter);
	
	public MethodResult deleteDomainByIds(List<String> ids,  List<String> domains);
	
	
	//----------------
	
	public void handleNewlyCreatedCloudHost(int region, String realHostId, String hostName, JSONObject hostQueryInfoResult);
	
	public void handleCreatedFailedCloudHost(String hostName);
	
	public MethodResult fetchNewestCloudHostFromHttpGateway();
	
	public CloudHostData refreshData(String cloudHostId);
	
	public MethodResult modifyAllocation(Map<String, Object> parameter);
	
	public void getTotalPrice(HttpServletRequest request, HttpServletResponse response);
	
	public MethodResult getCreateInfo(String userId,String region);
	
	
	public int getAllCloudHostCount(String userId);
	
	public String getNewCloudHostNameByUserId(String userId,String region);

	public MethodResult getCloudHostStatus(String hostId);
	
	public MethodResult getCloudHostStartStatus(String hostId);
	
	public void checkStopHost();
	
	public MethodResult sendPort(String port);
	
	public void keepAlive(String port);

	public boolean startMonitor(int regionId, String realHostId) throws MalformedURLException, IOException;
	
	public boolean stopMonitor(String realHostId) throws MalformedURLException, IOException;
	
	public CloudHostData getMonitorData(String realHostId);
	
	public MethodResult startMonitoring(String region,String realHostId);
	
	public MethodResult stopMyMonitor(String realHostId);
	
	public MethodResult getHostNameForAgentCreate(String userId, String region) ;
	
	/**
	 * 通过vpcId查询所有的云主机
	 * 
	 * @param request
	 * @param response
	 */
	public void getAllHostByVpcId(HttpServletRequest request, HttpServletResponse response);
}
