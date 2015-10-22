package com.zhicloud.op.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.CloudHostBillDetailVO;


public interface CloudHostBillDetailService
{

//	public CloudHostBillDetailVO getOneUndoneReocrd();
	
	public MethodResult startOneCloudHostBilling(Date endTime);
	
	
	
	
//	public void startCloudHostBilling();
	
}
