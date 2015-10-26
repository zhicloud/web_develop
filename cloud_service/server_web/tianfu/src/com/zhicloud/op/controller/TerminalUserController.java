package com.zhicloud.op.controller;

 

import com.zhicloud.op.app.helper.AppHelper;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.common.util.ObjectUtil;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.pay.unionpay.QuickPayConf;
import com.zhicloud.op.pay.unionpay.QuickPayUtils;
import com.zhicloud.op.service.*;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.MyFileVO;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class TerminalUserController
{
	
	public static final Logger logger = Logger.getLogger(TerminalUserController.class);
	

	@RequestMapping("/user.do")
	public String publicAgent(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.publicAgent()");
		
		boolean isLogin = LoginHelper.isLogin(request,AppConstant.SYS_USER_TYPE_TERMINAL_USER);
		String url = request.getParameter("url");
		if(url!=null){			
			url = new String(url.getBytes("ISO8859-1"), "utf-8"); 
		}
		request.setAttribute("url", url);
		if( isLogin==false )
		{ 
			String flag= request.getParameter("flag");
			if(flag!=null&&flag.equals("login")){
				return "/public/user/big_login.jsp";
				
			}
			request.setAttribute("outtime", "yes"); 
			return "/public/user/big_login.jsp";
		}
		else
		{
			LoginInfo user = LoginHelper.getLoginInfo(request,AppConstant.SYS_USER_TYPE_TERMINAL_USER);
			if(user.getUserType()!=4){	
				request.setAttribute("message", "您不是终端用户，不能访问本页面！");
				return "/public/warning.jsp";
			}
			//获取余额信息
			AccountBalanceService accountBalanceService = CoreSpringContextManager.getAccountBalanceService();
			accountBalanceService.getBalance(request, response);
			CloudHostService cloudHostService = CoreSpringContextManager.getCloudHostService();
			request.setAttribute("cloudHostCount", cloudHostService.getAllCloudHostCount(user.getUserId()));
			 
//			return "/security/user/main.jsp";
			return "/security/user/overview.jsp";
		}
	}
	@RequestMapping("/login.do")
	public String publicLogin(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.publicLogin()"); 
		 return "/public/user/login.jsp";
		 
	} 
	@RequestMapping("/register.do")
	public String publicUserRegister(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.publicUserRegister()"); 
		return "/public/user/big_register.jsp";
	}
	@RequestMapping("user/register.do")
	public String publicRegister(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.publicUserRegister()"); 
		return "/public/user/big_register.jsp";
	}
	@RequestMapping("/icp.do")
	public String toIcp(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toIcp()"); 
		return "/public/help_3_10.jsp";
	}
	@RequestMapping("/forgetpassword.do")
	public String publicUserForgetPassword(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.publicUserForgetpassword()"); 
		return "/public/user/forgetpassword.jsp";
	}
	@RequestMapping("/forgetPassword.do")
	public String publicUserForgetPassword2(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.publicUserForgetpassword2()"); 
		return "/public/user/forgetpassword2.jsp";
	}
	@RequestMapping("/user/successtrail.do")
	public String publicSuccessTrail(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.publicSuccessTrail()"); 
		return "/security/user/trail_success.jsp";
	}
	@RequestMapping("/cloudsever.do")
	public String toCloudServer(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toCloudServer()"); 
		return "/public/cloudsever.jsp";
	}
	@RequestMapping("/solution.do")
	public String toSolution(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toSolution()"); 
		return "/public/solution.jsp";
	}
	@RequestMapping("/cloudstorage.do")
	public String toCloudStorage(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toCloudStorage()"); 
		return "/public/cloudstorage.jsp";
	}
	@RequestMapping("/vpcserver.do")
	public String toVpcserver(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toVpcserver()"); 
		return "/public/vpcserver.jsp";
	}
	@RequestMapping("/help.do")
	public String toHelp(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toHelp()"); 
		return "/public/help.jsp";
	}
	@RequestMapping("/user/banancing.do")
	public String toBanancing(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toBanancing()"); 
		return "/public/banancing.jsp";
	}
	@RequestMapping("/user/database.do")
	public String toDataBase(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toDataBase()"); 
		return "/public/database.jsp";
	}
	@RequestMapping("/user/sdn.do")
	public String toSdn(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toSdn()"); 
		return "/public/sdn.jsp";
	}
	@RequestMapping("/user/recovery.do")
	public String toRecovery(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toRecovery()"); 
		return "/public/recovery.jsp";
	}
	@RequestMapping("/aboutus.do")
	public String toAboutUs(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toAboutUs()"); 
		return "/public/aboutus.jsp";
	}
	@RequestMapping("/contactus.do")
	public String toContactUs(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toContactUs()"); 
		return "/public/contactus.jsp";
	}
	@RequestMapping("/serverhelp.do")
	public String toServerhelp(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toServerhelp()"); 
		return "/public/serverhelp.jsp";
	}
	@RequestMapping("/hotline.do")
	public String toHotline(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toHotline()"); 
		return "/public/hotline.jsp";
	}
	@RequestMapping("/storagehelp.do")
	public String toStoragehelp(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toStoragehelp()"); 
		return "/public/storagehelp.jsp";
	}
	@RequestMapping("/job.do")
	public String toJob(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toJob()"); 
		return "/public/job.jsp";
	}
	
	@RequestMapping("/buy.do")
	public String publicUserBuy(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.publicUserBuy()"); 
		
		PaymentService paymentService = CoreSpringContextManager.getPaymentService();
		paymentService.getParameter(request, response);
		AccountBalanceService accountBalanceService = CoreSpringContextManager.getAccountBalanceService();
		accountBalanceService.getBalance(request, response); 
		 
		return "/security/user/cloud_host_buy_page.jsp";
		 
	}
	
	@RequestMapping("/user/shoppingcart.do")
	public String toShoppingCart(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.toShoppingCart()"); 
//		boolean isLogin = LoginHelper.isLogin(request);
//		if( isLogin==true )
//		{
		Object beanObj = CoreSpringContextManager.getBean("paymentService");
		Class<?>[] parameterTypes = new Class<?>[2];
		parameterTypes[0] = request.getClass();
		parameterTypes[1] = response.getClass();
		Method method = ObjectUtil.getCompatibleMethod(beanObj.getClass(), "getParameter", parameterTypes);
		method.invoke(beanObj, request, response);
//		}  
		return  "/security/user/shoppingcart.jsp";
	}
	
	@RequestMapping("/user/account.do")
	public String publicUserAccount(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.publicUserAccount()"); 
//		boolean isLogin = LoginHelper.isLogin(request); 
		Object beanObj = CoreSpringContextManager.getBean("paymentService");
		Class<?>[] parameterTypes = new Class<?>[2];
		parameterTypes[0] = request.getClass();
		parameterTypes[1] = response.getClass();
		Method method = ObjectUtil.getCompatibleMethod(beanObj.getClass(), "toAccountPage", parameterTypes);
		return  (String)method.invoke(beanObj, request, response); 
	}
	
	@RequestMapping("/response.do")
	public void service(HttpServletRequest request, HttpServletResponse response) throws Exception{
		try {
			request.setCharacterEncoding(QuickPayConf.charset);
		} catch (UnsupportedEncodingException e) {
		}
		
		String[] resArr = new String[QuickPayConf.notifyVo.length]; 
		String orderId = "";
		for(int i=0;i<QuickPayConf.notifyVo.length;i++){
			if(QuickPayConf.notifyVo[i].equals("orderNumber")){
				orderId  = request.getParameter(QuickPayConf.notifyVo[i]);
			}
			resArr[i] = request.getParameter(QuickPayConf.notifyVo[i]);
		}
		String signature = request.getParameter(QuickPayConf.signature);
		String signMethod = request.getParameter(QuickPayConf.signMethod);
		
		response.setContentType("text/html;charset="+QuickPayConf.charset);    
		response.setCharacterEncoding(QuickPayConf.charset);
 		try {
			Boolean signatureCheck = new QuickPayUtils().checkSign(resArr, signMethod, signature);
			if(signatureCheck && "00".equals(resArr[10])){
				/*
				 * 执行数据库操作
				 */
				AccountBalanceService pay = CoreSpringContextManager.getAccountBalanceService();
				String a = pay.updateAfterPay(orderId);
			}
			response.getWriter()
			.append("建议前台通知和后台通知用两个类实现，后台通知进行商户的数据库等处理,前台通知实现客户浏览器跳转<br>")
			.append("签名是否正确："+ signatureCheck)
			.append("<br>交易是否成功："+"00".equals(resArr[10]));
			if(!"00".equals(resArr[10])){
				response.getWriter().append("<br>失败原因:"+resArr[11]);
			} 
		} catch (IOException e) {
			
		}
	
		response.setStatus(HttpServletResponse.SC_OK);
	}
	
	@RequestMapping(value = "/user/sendmail.do")
	public String sendMail(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.sendMail()"); 
		TerminalUserService terminalUserService = CoreSpringContextManager.getTerminalUserService();
		return terminalUserService.activated(request, response);
	}

	@RequestMapping(value = "/user/changePasswordbysendmail.do")
	public String sendMailChangePassword(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		logger.debug("TerminalUserController.changePasswordbysendmail()"); 
		TerminalUserService terminalUserService = CoreSpringContextManager.getTerminalUserService();
		return terminalUserService.setStrToResetPasswordPage(request, response);
	}
	
	@RequestMapping(value ="/user/uploadFile.do")
	public void uploadFile(HttpServletRequest request,HttpServletResponse response) throws Exception{
		logger.debug("TerminalUserController.uploadFile()"); 
		try {
			//获取前台参数
			
			String userId=request.getParameter("userId");
			
			MultipartHttpServletRequest  multipartRequest = (MultipartHttpServletRequest) request;
			
			MultipartFile attach = multipartRequest.getFile("attach");
			
			//获取文件名
			String fileName = new String(attach.getOriginalFilename().getBytes("ISO-8859-1"), "UTF-8");
			//TODO:iso image upload demo
//			String name = "test#" + new Random().nextInt();
//			String description = "test";
//			String user = userId;
//			String group = "users";
//			IsoImageService isoImageService = CoreSpringContextManager.getIsoImageService();
//			String sessionId = isoImageService.upload(1, fileName, attach.getInputStream(), name, description, user, group);
//			System.out.println("upload iso image result : " + sessionId);
//			//获取上传进度及结果
//			IsoImageProgressData isoImageProgressData = isoImageService.getProgressData(sessionId, name);
//			System.out.println(isoImageProgressData.getProgress());
			//注入Service
			CloudUserService cloudUserService = CoreSpringContextManager.getCloudUserService();
			
			
			//定义上传路径
			String filePath = AppHelper.getServerHome()+"/projects/"+AppHelper.APP_NAME+"/user_upload/"+userId;
			
			//若无该文件夹自动创建
			File fp = new File(filePath);
			
			if(!fp.exists()){
				fp.mkdirs();
			}
			
			File file = new File(filePath+"/"+fileName);
			
			// 上传文件
			FileUtils.copyInputStreamToFile(attach.getInputStream(), file);
			
			BigInteger size =BigInteger.valueOf(file.length());
			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("id", StringUtil.generateUUID());
			data.put("userId", userId);
			data.put("fileName",fileName);
			data.put("filePath",  filePath+"/"+fileName);
			data.put("size",  size);
			data.put("uploadTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			
			cloudUserService.addMyFile(data);
			
		} catch (Exception e) {
			logger.error("TerminalUserController.uploadFile() > ["+Thread.currentThread().getId()+"] ",e);
			throw new AppException("上传失败");
		}
		
	}

	@RequestMapping(value = "/fileDownload.do")
	public void download(HttpServletResponse response,HttpServletRequest request) throws IOException {
		try {
			String id = (String) request.getParameter("fileId");
			
			CloudUserService cloudUserService = CoreSpringContextManager.getCloudUserService();
			
			MyFileVO myFile = cloudUserService.getMyFileById(id);
			String path = myFile.getFilePath();
			File file = new File(path);
			
			InputStream in = new FileInputStream(file);
			OutputStream os = response.getOutputStream();
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(file.getName().getBytes("utf-8"), "iso8859-1"));
			response.addHeader("Content-Length", file.length() + "");
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
			int data = 0;
			while ((data = in.read()) != -1) {
				os.write(data);
			}
			os.close();
			in.close();
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request,AppConstant.SYS_USER_TYPE_TERMINAL_USER);
			OperLogService operLogService = CoreSpringContextManager.getOperLogService();
			operLogService.addNewOperLog(loginInfo.getUserId(), AppConstant.OPER_LOG_SUCCESS, "下载文件"+myFile.getFileName(), "", "1");
		} catch (Exception e) {
			logger.error("TerminalUserController.download() > ["+Thread.currentThread().getId()+"] ",e);
			throw new AppException("下载失败");
		}
		
	}
	@RequestMapping(value = "/loadWord.do")
	public void loadWord(HttpServletResponse response,HttpServletRequest request) throws IOException {
		try { 
			 
			String path = System.getProperty("catalina.home")+"/webapps/download/致云代理商管理平台操作指南.pdf";;
			File file = new File(path);
			
			InputStream in = new FileInputStream(file);
			OutputStream os = response.getOutputStream();
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(file.getName().getBytes("utf-8"), "iso8859-1"));
			response.addHeader("Content-Length", file.length() + "");
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/octet-stream");
			int data = 0;
			while ((data = in.read()) != -1) {
				os.write(data);
			}
			os.close();
			in.close();
		} catch (Exception e) {
			logger.error("TerminalUserController.download() > ["+Thread.currentThread().getId()+"] ",e);
			throw new AppException("下载失败");
		}
		
	}
	
	@RequestMapping(value = "/user/checkExisting.do")
	public String checkExisting(HttpServletResponse response,HttpServletRequest request) throws IOException {

			try {
				//获取前台参数
				LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 4);

				String userId = loginInfo.getUserId();
				
//				MultipartResolver resolver = new CommonsMultipartResolver(request.getSession().getServletContext());
//				MultipartHttpServletRequest multipartRequest = resolver.resolveMultipart(request);
				
//			    MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
				
//				MultipartFile attach = multipartRequest.getFile("attach");
				
				//获取文件名
//				String fileName = new String(attach.getOriginalFilename().getBytes("ISO-8859-1"), "UTF-8");
//				String fileName = "云端在线珠海节点管控表.xlsx";
//				
//				CloudUserService cloudUserService = CoreSpringContextManager.getCloudUserService();
//				
//				Map<String, Object> data = new LinkedHashMap<String, Object>();
//				data.put("userId", userId);
//				data.put("fileName",fileName);
//				MyFileVO myFile = cloudUserService.getMyFileByUserIdAndFileName(data);
//				
//				if(myFile != null) {
//					return 1;
//				}
				return "true";
				
		} catch (Exception e) {
			logger.error("TerminalUserController.checkExisting() > ["+Thread.currentThread().getId()+"] ",e);
			throw new AppException("失败");
		}
	}	
}
