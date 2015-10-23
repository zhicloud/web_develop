//package com.zhicloud.op.remote.ro;
//
//import java.util.Map;
//
//import org.apache.log4j.Logger;
//
//import com.zhicloud.op.core.CoreSpringContextManager;
//import com.zhicloud.op.remote.CallWithoutLogin;
//import com.zhicloud.op.remote.Callable;
//import com.zhicloud.op.remote.MethodResult;
//import com.zhicloud.op.service.SysUserService;
//
//public class LoginRO
//{
//	
//	private static final Logger logger = Logger.getLogger(LoginRO.class);
//	
//	@Callable
//	@CallWithoutLogin
//	public MethodResult login(Map<String, String> parameter)
//	{
//		logger.debug("LoginRO.login()");
//		SysUserService sysUserService = CoreSpringContextManager.getSysUserService();
//		return sysUserService.login(parameter);
//	}
//	
//	@Callable
//	@CallWithoutLogin
//	public MethodResult logout()
//	{
//		logger.debug("LoginRO.logout()");
//		SysUserService sysUserService = CoreSpringContextManager.getSysUserService();
//		return sysUserService.logout();
//	}
//	
//}
