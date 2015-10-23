package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.MyFileVO;

public interface CloudUserService
{
	
	public String baseInfoPage(HttpServletRequest request,HttpServletResponse response);
	
	public String myCloudHostPage(HttpServletRequest request, HttpServletResponse response);
	
	public String myCloudHostQueryResultPartPage(HttpServletRequest request, HttpServletResponse response);
	
	public String myHistoryOrderPage(HttpServletRequest request, HttpServletResponse response);
	
	public String userOrderViewDetailPage(HttpServletRequest request, HttpServletResponse response);
	
	public String myFileManagePage(HttpServletRequest request, HttpServletResponse response);
	
	public String uploadFilePage(HttpServletRequest request, HttpServletResponse response);
	
	public String uploadedFileDetailPage(HttpServletRequest request, HttpServletResponse response);
	
	//---------------------------------------------------------------------
	
	public void myHistoryOrderQuery(HttpServletRequest request, HttpServletResponse response);
	
	public void queryMyFile(HttpServletRequest request, HttpServletResponse response);
	
	public MyFileVO getMyFileById(String id);
	
	public MethodResult getMyFileByUserIdAndFileName( String fileName, String userId);
	
	public MethodResult getCurrentFileSize(String userId);

	
	
	//---------------------------------------------------------------------
	
	public MethodResult addMyFile(Map<String, Object> data);
	
	public MethodResult deleteUploadedFileByIds(List<String> fileIds);
	
}
