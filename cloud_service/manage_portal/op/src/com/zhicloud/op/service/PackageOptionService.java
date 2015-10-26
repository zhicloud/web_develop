package com.zhicloud.op.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.PackagePriceVO;
import com.zhicloud.op.vo.VpcPriceVO;

public interface PackageOptionService
{

	public String managePage(HttpServletRequest request, HttpServletResponse response);
	
	public String packagePriceManagePage(HttpServletRequest request, HttpServletResponse response);
	//............
	public MethodResult addCpuPackageOption(Map<String, String> parameter);
	public MethodResult deleteCpuPackageOption(String coreId);
	
	//............
	public MethodResult addMemoryPackageOption(Map<String, String> parameter);
	public MethodResult deleteMemoryPackageOption(String memoryId);
	
	//............
	public MethodResult updateDiskPackageOption(Map<String, Object> parameter);
	
	//............
	public MethodResult updateBandwidthPackageOption(Map<String, Object> parameter);
	
	//............
	public MethodResult updateTrialPeriodParam(Map<String, Object> parameter);
	
	public void queryPackagePrice(HttpServletRequest request,HttpServletResponse response);
	
	public List<PackagePriceVO> getPackagePrice(String type);
	
	public String addPackagePricePage(HttpServletRequest request, HttpServletResponse response);

	public String modPackagePricePage(HttpServletRequest request, HttpServletResponse response);
	
	public MethodResult addPackagePrice(Map<String, String> parameter);
	
	public MethodResult modPackagePrice(Map<String, String> parameter);

	public MethodResult deletePackagePrice(String id);
	
	public MethodResult updateDiskPriceOfOne(Map<String, String> parameter);
	
	public MethodResult updateBandwidthPriceOfOne(Map<String, String> parameter);
	
	public void getOption(HttpServletRequest request,HttpServletResponse response);
	
	public MethodResult getCurrentPrice(String region,String priceStatus,String cpuCore,String memory,String dataDisk,String bandwidth);
	
	public void queryVPCPrice(HttpServletRequest request,HttpServletResponse response);
	
	public String addVPCPricePage(HttpServletRequest request, HttpServletResponse response);
	
	public MethodResult addVPCPrice(Map<String,String> parameter);
	
	public String modVPCPricePage(HttpServletRequest request, HttpServletResponse response);
	
	public MethodResult modVPCPrice(Map<String,String> parameter);
	
	public MethodResult deleteVPCPrice(String id);
	
	public MethodResult updateIpPrice(Map<String, String> parameter);
	
	public BigDecimal getVpcPrice(String region,Integer amount);
}
