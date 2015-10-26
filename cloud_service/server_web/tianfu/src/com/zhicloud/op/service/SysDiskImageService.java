package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;

public interface SysDiskImageService
{
	

	// --------------------
	
	public String managePage(HttpServletRequest request,  HttpServletResponse response);

	public String addPage(HttpServletRequest request,  HttpServletResponse response);
	
	public String modPage(HttpServletRequest request, HttpServletResponse response);
	
	// --------------
	
	public void querySysDiskImage(HttpServletRequest request,  HttpServletResponse response);

	// ---------------
	
	public MethodResult addSysDiskImage(Map<String, Object> parameter);

	public MethodResult updateSysDiskImageById(Map<String, Object> parameter);
	
	public MethodResult deleteSysDiskImageByIds(List<String> sysDiskImageIds);
	
	public MethodResult publishSysDiskImageByIds(List<String> sysDiskImageIds);

	public MethodResult deleteSysDiskImageId(String sysDiskImageId);
	/**
	 * 根据镜像ID获取镜像创建信息
	 * @param id
	 * @return
	 */
	public MethodResult getSysDiskImageCreationResult(String id);
	//---------------
	
	public boolean initSysDiskImageFromHttpGateway();
	/**
	 * 
	 * selectDiskImageByRegion: 根据地域筛选镜像
	 *
	 * @author sasa
	 * @param region
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult selectDiskImageByRegion(String region);
	 
	
	
}
