package com.zhicloud.op.controller;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhicloud.op.common.util.StringUtil;


@Controller
public class MainController
{
	
	
	public static final Logger logger = Logger.getLogger(MainController.class);
	

	@RequestMapping("/main.do")
	public String publicMain(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("AdminController.publicMain()");
		String target = (String) request.getParameter("target");
		if(StringUtil.isBlank(target)){
			return "/src/user/u_home.jsp";
		}else if(target.equals("cloudser")){
			return "/src/user/u_cloudser.jsp";
		}else if(target.equals("cshighpowered")){
			return "/src/user/u_cshighpowered.jsp";
		}else if(target.equals("cloudcdn")){
			return "/src/user/u_cloudcdn.jsp";
		}else if(target.equals("loadbalance")){
			return "/src/user/u_loadbalance.jsp";
		}else if(target.equals("cloudmonitor")){
			return "/src/user/u_cloudmonitor.jsp";
		}else if(target.equals("exclucloud")){
			return "/src/user/u_exclucloud.jsp";
		}else if(target.equals("clouddrive")){
			return "/src/user/u_clouddrive.jsp";
		}else if(target.equals("clouddatabase")){
			return "/src/user/u_clouddatabase.jsp";
		}else if(target.equals("mirrorhost")){
			return "/src/user/u_mirrorhost.jsp";
		}else if(target.equals("eggplan")){
			return "/src/user/u_eggplan.jsp";
		}else if(target.equals("eggplanapplication")){
			return "/src/user/u_epapplication.jsp";
		}else if(target.equals("solution")){
			return "/src/user/u_solution.jsp";
		}else if(target.equals("serconcept")){
			return "/src/user/u_serconcept.jsp";
		}else if(target.equals("serconsult")){
			return "/src/user/u_serconsult.jsp";
		}else if(target.equals("helpcenter")){
			return "/src/user/u_helpcenter.jsp";
		}else if(target.equals("arcser")){
			return "/src/user/u_arcser.jsp";
		}else if(target.equals("comprofile")){
			return "/src/user/u_comprofile.jsp";
		}else if(target.equals("jionus")){
			return "/src/user/u_jionus.jsp";
		}else if(target.equals("newsdynamics")){
			return "/src/user/u_newsdynamics.jsp";
		}else if(target.equals("contactus")){
			return "/src/user/u_contactus.jsp";
		}else if(target.equals("application")){ 
			return "/src/user/u_application.jsp";
		}else if(target.equals("mhapplication")){ 
			return "/src/user/u_mhapplication.jsp";
		}else{
			return "/src/user/u_home.jsp";
		}
 		
		
		
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
