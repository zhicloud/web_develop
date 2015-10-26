package com.zhicloud.op.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;

public interface CloudDiskService {

	public String managePage(HttpServletRequest request, HttpServletResponse response);
	
	public String addPage(HttpServletRequest request, HttpServletResponse response);
	
	public String modPage(HttpServletRequest request, HttpServletResponse response);
	
	public String cloudDiskDetailPage(HttpServletRequest request, HttpServletResponse response);
	
	public void queryCloudDisk(HttpServletRequest request, HttpServletResponse response);
	
	public MethodResult addCloudDisk(Map<String, String> parameter);
	
	public MethodResult updateCloudDiskById(Map<String, String> parameter);
	
	public MethodResult deleteCloudDiskById(String id);
	
	public MethodResult checkInvitationCode(String diskInvitationCode);
	
	public MethodResult inactivateCloudDisk(String diskId);
	
	public MethodResult reactivateCloudDisk(String diskId);
	
	public String getNewCloudDiskNameByUserId(String userId,String region);
	
	public MethodResult getCreateInfo(String region);
}
