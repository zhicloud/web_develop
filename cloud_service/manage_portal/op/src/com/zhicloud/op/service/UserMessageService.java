package com.zhicloud.op.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;

public interface UserMessageService {
	
	public void queryUserMessage(HttpServletRequest request, HttpServletResponse response);
	public MethodResult deleteUserMessage(String id);
}
