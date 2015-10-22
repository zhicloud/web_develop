package com.zhicloud.op.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

@Controller
public class InterfaceController extends MultiActionController
{

	@RequestMapping("/interface/test.do")
	public void test(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		System.out.println("EasyInterfaceController.test()");
		response.getWriter().print(request.getRequestURI());
	}
	
}
