/**
 * Project Name:CloudDeskTopMS
 * File Name:MainController.java
 * Package Name:com.zhicloud.ms.controller
 * Date:2015年3月20日上午10:56:32
 * 
 *
*/ 

package com.zhicloud.ms.controller; 

 

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.ms.app.helper.LoginHelper;
import com.zhicloud.ms.login.LoginInfo; 
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.transform.util.TransFormLoginHelper;
import com.zhicloud.ms.transform.util.TransFormLoginInfo; 

/**
 * ClassName: MainController 
 * Function: 用于登录跳转.  
 * date: 2015年3月20日 上午10:56:32 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/main")
public class MainController {
	@RequestMapping(value="/menu",method=RequestMethod.GET)
	public String toIamgeList(Model model,HttpServletRequest request){	
		//查询loginInfo 
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		//超时返回首页
		if(loginInfo == null){
			return "index";
		}
		model.addAttribute("loginInfo", loginInfo);
 		return "main";
	} 
	/**
	 * 
	 * checkLogin:超时判断 
	 *
	 * @author sasa
	 * @param request
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/checklogin",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult checkLogin(HttpServletRequest request){ 
 		
 		//查询loginInfo 
		TransFormLoginInfo loginInfo = TransFormLoginHelper.getLoginInfo(request);
		//超时返回首页
		if(loginInfo == null){ 
			return new MethodResult(MethodResult.FAIL,""); 
 		}
		return   new MethodResult(MethodResult.SUCCESS,"");
	}

}

