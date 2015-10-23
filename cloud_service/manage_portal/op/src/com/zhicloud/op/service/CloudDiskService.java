package com.zhicloud.op.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.CloudDiskVO;

public interface CloudDiskService {

	public String managePage(HttpServletRequest request, HttpServletResponse response);
	
	public String addPage(HttpServletRequest request, HttpServletResponse response);
	
	public String addPageForAgent(HttpServletRequest request, HttpServletResponse response);
	
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
	
	/**
	 * 根绝地域获取云硬盘价格列表
	 * 
	 * @param request
	 * @param response
	 */
	public void queryDiskPrice(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 更新云硬盘每G的价格
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public MethodResult updateCloudDiskPriceOfOne(Map<String,String> parameter);
	
	/**
	 * 根据地域和硬盘大小计算价格
	 * 
	 * @param region
	 * @param sizeGB
	 * @return
	 */
	public BigDecimal getCloudDiskPrice(String region,String sizeGB);
	
	/**
	 * 查询所有所有云硬盘
	 * 
	 * @param request
	 * @param response
	 */
	public void getAllCloudDisk(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 批量逻辑删除云硬盘
	 * 
	 * @param ids
	 * @return
	 */
	public MethodResult deleteCloudDiskByIds(List<String> ids);
	
    /**
     * @Description:查询所有云硬盘数据。不分页
     * @param request
     * @param response
     * @return List<CloudDiskVO>
     */
    public List<CloudDiskVO> getAllCloudDiskForExport(HttpServletRequest request, HttpServletResponse response);

}
