package com.zhicloud.op.remote.ro;

import java.util.Map;

import org.apache.log4j.Logger;

import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.remote.CallWithoutLogin;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.TestService;

public class TestRO
{
	
	final static Logger logger = Logger.getLogger(TestRO.class);
	
	
	@Callable
	@CallWithoutLogin
	public MethodResult addUser(Map<Object, Object> parameter)
	{
		logger.debug("TestRO.addUser()");
		MethodResult result = new MethodResult();
		
		TestService testService = CoreSpringContextManager.getTestService();
		result.put("add_result", testService.addTermianlUser(parameter)) ;
		
		result.message = "添加用户成功";
		return result;
	}
	
	
	@Callable
	@CallWithoutLogin
	public MethodResult deleteUser(String userId)
	{
		logger.debug("TestRO.deleteUser()");
		MethodResult result = new MethodResult();
		
		TestService testService = CoreSpringContextManager.getTestService();
		result.put("delete_result", testService.deleteTermianlUser(userId)) ;
		
		result.message = "删除用户成功";
		return result;
	}
	
	
	
	
	
	
	
	
	
	

}













