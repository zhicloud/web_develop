package com.zhicloud.op.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class MainController
{
	
	
	public static final Logger logger = Logger.getLogger(MainController.class);
	

	@RequestMapping("/main.do")
	public String publicMain(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("AdminController.publicMain()");
		return "/public/index.jsp";
	}
	
	@RequestMapping("/downloadPage.do")
	public String publicDownloadPage(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("MainController.publicDownloadPage()");
		return "/public/downloadPage.jsp";
	}
	
//	@RequestMapping("/checkTimeOut.do")
//	public void checkTimeOut(HttpServletRequest request, HttpServletResponse response){
//		MethodResult mr = new MethodResult();
//		if(request.getSession().getAttribute("_LOGIN_INFO_1")==null){
//			mr.status = MethodResult.FAIL;
//		}else{
//			mr.status = MethodResult.SUCCESS;
//		}
//		try {
//			ServiceHelper.writeJsonTo(response.getOutputStream(),mr);
//		} catch (IOException e) {
//			logger.error("",e);
//		}
//	}
//	@RequestMapping("/checkTimeOutForAgent.do")
//	public void checkTimeOutForAgent(HttpServletRequest request, HttpServletResponse response){
//		MethodResult mr = new MethodResult();
//		if(request.getSession().getAttribute("_LOGIN_INFO_3")==null){
//			mr.status = MethodResult.FAIL;
//		}else{
//			mr.status = MethodResult.SUCCESS;
//		}
//		try {
//			ServiceHelper.writeJsonTo(response.getOutputStream(),mr);
//		} catch (IOException e) {
//			logger.error("",e);
//		}
//	}
//	@RequestMapping("/checkTimeOutForOperator.do")
//	public void checkTimeOutForOperator(HttpServletRequest request, HttpServletResponse response){
//		MethodResult mr = new MethodResult();
//		if(request.getSession().getAttribute("_LOGIN_INFO_2")==null){
//			mr.status = MethodResult.FAIL;
//		}else{
//			mr.status = MethodResult.SUCCESS;
//		}
//		try {
//			ServiceHelper.writeJsonTo(response.getOutputStream(),mr);
//		} catch (IOException e) {
//			logger.error("",e);
//		}
//	}
//	@RequestMapping("/checkTimeOutForUser.do")
//	public void checkTimeOutForUser(HttpServletRequest request, HttpServletResponse response){
//		MethodResult mr = new MethodResult();
//		if(request.getSession().getAttribute("_LOGIN_INFO_4")==null){
//			mr.status = MethodResult.FAIL;
//		}else{
//			mr.status = MethodResult.SUCCESS;
//		}
//		try {
//			ServiceHelper.writeJsonTo(response.getOutputStream(),mr);
//		} catch (IOException e) {
//			logger.error("",e);
//		}
//	}
}
