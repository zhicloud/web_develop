﻿<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<% 
	String URL = (String)request.getAttribute("url");
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	String outtime = (String)request.getAttribute("outtime");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta name="viewport" content="height=600, initial-scale=1, minimum-scale=1, maximum-scale=2, user-scalable=yes" />
<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" /> 
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/page.js"></script>
<script type="text/javascript">
var a = '<%=request.getContextPath()%>' ;
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
function checkAccount() {
	var account = new String($("#inputemail").val()).trim(); 
	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if(account==null || account==""){
		$("#tip-account").html("邮箱不能为空");
		return false;
	}
	if(!myreg.test(account)){
		$("#tip-account").html("邮箱格式不正确");
		return false;
	}
	if(account.length>30){
		$("#tip-account").html("输入不能超过30个字符");
		return false;
		
	}
	else{
		$("#tip-account").html("");
		return true;
		}
	}

function checkPassword(){
	var myPassword = new String($("#inputpassword").val()).trim();
	if(myPassword==null || myPassword==""){
		$("#tip-password").html("密码不能为空");
		return false;
	}else if(myPassword.length<6||myPassword.length>20){ 
		$("#tip-password").html("密码长度为6-20个字符");
		return false;
	} else{
		$("#tip-password").html("");
		return true;
		}
	}
function checkVcode(){
	var verificationCode = new String($("#inputcode").val()).trim();
	if(verificationCode==null || verificationCode==""){
		$("#tip-verificationcode").html("验证码不能为空");
		return false;
	}else{
		$("#tip-verificationcode").html("");
		return true;
	}
}

var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;

function login()
{
	loadingbegin();
	var formData = $.formToBean(big_form);
	ajax.remoteCall("bean://sysUserService:login",
		[formData],
		function(reply)
		{  
			if( reply.status=="exception" )
			{
				loadingend();
				top.$.messager.alert("提示", reply.exceptionMessage, "warning");
			} 
			else if( reply.result.status=="success" )
			{ 
				// 跳转页面  
				window.location.reload(); 
			}
			else if( reply.result.status=="fail" )
			{
			//	top.$.messager.alert("提示",reply.result.message,"warning");
				loadingend();
				$("#tip-verificationcode").html(""+reply.result.message+"");
				$("#password").val("");
				$("#verification_code_img").click();
				$("#verification_code").val("");
			}
		}
	);
}

$(function(){
	// 换一个验证码
	$("#verification_code_img").click(function(){
		$("#verification_code").attr("src", "<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>&ts="+Math.random());
	});
	$("a[name=verification_code_img]").click(function(){
		$("#verification_code").attr("src", "<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>&ts="+Math.random());
	});
	// 回车
	$("#inputemail, #inputpassword,#inputcode").keypress(function(evt){
		if( evt.keyCode==13 ){
			login();
		}
	});
	// 登录
	$("#login_btn").click(function(){ 
		if(checkAccount()&checkPassword()&checkVcode()){
		login();
		}
	});
	// 注册
	$("#register_btn").click(function(){ 
			window.location.href = "<%=request.getContextPath()%>/public/user/register.jsp"; 
	}); 
});
$(document).ready(function(){
	init(0);
	inituser();
	$("#beforelogin").css("display","none");
	$("#afterlogin").css("display","none");
}); 
function reshVerification()
{ 
	$("#verification_code_img").click(); 	 
}
var t2 = window.setInterval(reshVerification,1740000);
</script>
<!--[if IE 6]>
<script src="javascript/DD_belatedPNG.js"></script>
<script type="text/javascript">
	DD_belatedPNG.fix("*");
</script>
<![endif]-->
</head>
<body>
<div class="page">
  <div class="pagewhite" style="background:#f6f6f6;"> 
    <div class="header" style="background:#fff">
        <div class="top">
			<a class="logo l" href="<%=request.getContextPath()%>/"> 
			<img src="<%=request.getContextPath()%>/image/logo_tf.png" width="184" height="34" alt="天府软件园创业场" /></a>
			<div id="beforelogin" class="user r">
				<a id="loginlink" href="javascript:void(0);" class="graylink">登录</a><span>|</span>
				<a id="reglink" href="javascript:void(0);">注册</a>
			</div>
			<div id="afterlogin" class="user r" style="display: none;">
				<img class="reddot" src="<%=request.getContextPath()%>/image/reddot.png" width="6" height="6" alt=" " />
				<a id="logoutlink" href="javascript:void(0);">注销</a><span>|</span>
				<a href="<%=request.getContextPath()%>/user.do" class="bluelink">我的云端</a>
			</div>
			<div class="nav r">
				<a href="<%=request.getContextPath()%>/" style="background: transparent;"><img id="nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/nav_1_i.png" width="20" height="20" alt="首页" style="padding: 8px 0" /> </a>
				<a href="<%=request.getContextPath()%>/cloudsever.do">云主机</a>
				<a href="<%=request.getContextPath()%>/cloudstorage.do">云硬盘</a>
				<a href="<%=request.getContextPath()%>/solution.do">解决方案</a>
				<a href="<%=request.getContextPath()%>/help.do">帮助中心</a>
				<a href="<%=request.getContextPath()%>/aboutus.do">关于我们</a>
				<a href="#" style="display: none"></a>
				<a href="<%=request.getContextPath()%>/user.do?flag=login" style="display: none"></a>
				<a href="#" style="display: none"></a>
				<a href="#" style="display: none">我的云端</a>
			</div>
		</div>
		<div class="subnav">
			<div class="box">1</div>
			<div class="box">2</div>
			<div class="box">3</div>
			<div class="box">4</div>
			<div class="box">5</div>
			<div class="box">6</div>
			<div class="box">7</div>
			<div class="box">8</div>
			<div class="box">9</div>
			<div class="box">
				<a href="#"><img id="nav_10_1" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_1_i.png" width="24" height="24" alt="概览" /><br />概览</a><a
					href="#"><img id="nav_10_2" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_2_i.png" width="24" height="24" alt="我的云主机" /><br />我的云主机</a><a
					href="#"><img id="nav_10_3" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_3_i.png" width="24" height="24" alt="我的云硬盘" /><br />我的云硬盘</a><a
					href="#"><img id="nav_10_4" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_4_i.png" width="24" height="24" alt="我的账户" /><br />我的账户</a><a
					href="#"><img id="nav_10_5" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_5_i.png" width="24" height="24" alt="操作日志" /><br />操作日志</a><a
					href="#"><img id="nav_10_6" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_6_i.png" width="24" height="24" alt="意见反馈" /><br />意见反馈</a><a
					href="#"><img id="nav_10_7" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_7_i.png" width="24" height="24" alt="文件夹" /><br />文件夹</a>
			</div>
		</div>
	</div>
    <div class="pagemain">
      <div class="loginbox">
        <div class="loginpanel">
          <div class="lp"></div>
          <div class="right" style="float:right;height:380px; margin-top:34px;background:#fff; border-left:solid 1px #e6e6e6;">
            <div class="box">
              <div class="title">账号登录</div>
              <form id="big_form" method="post">
                <input type="hidden" id="type" name="type" value="4"/>
              
                <input id="inputemail" autocomplete="off" name="account" type="text" onfocus="inputfocus('email');" onblur="inputblur('email'),checkAccount()" class="itext" style="background:#eef0f1"/>
                <div class="itip" style="height:20px;">
                  <label id="emaillabel" for="inputemail" class="ilabel" style="background:#eef0f1">输入邮箱</label>
                  <span class="err" id="tip-account"></span> </div>
                <input id="inputpassword" autocomplete="off" name="password" type="password" onfocus="inputfocus('password');" onblur="inputblur('password');checkPassword();" class="itext" style="background:#eef0f1"/>
                <div class="itip" style="height:20px;">
                  <label id="passwordlabel" for="inputpassword" class="ilabel" style="background:#eef0f1">输入密码</label>
                  <span class="err" id="tip-password"></span></div>
                 
                <input id="inputcode" autocomplete="off" name="verification_code" type="text" onfocus="inputfocus('code');" onblur="inputblur('code');checkVcode()" class="itext l" style="width:100px;background:#eef0f1"/>
                <a href="javascript:void(0);" name="verification_code_img" class="itext l" style="width:100px; height:40px; padding:0; margin-left:10px;"><img id="verification_code"  src="<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>" width="100" height="40" alt="验证码" class="code"/></a>
                <div class="r" style="height:12px; line-height:16px; padding:4px 0 4px 0;"><a id="verification_code_img" name="verification_code_img" href="javascript:void(0);">看不清<br />
                  换一张</a></div>
                <div class="clear">&nbsp;</div>
                <div class="itip" style="height:20px;">
                  <label id="codelabel" for="inputcode" class="ilabel" style="width:80px;background:#eef0f1">验证码</label>
                  <span class="err" id="tip-verificationcode"></span> </div>
                <a href="javascript:void(0);" id="login_btn" class="ibluebtn" style="width:280px;margin:28px 0;">立即登录</a>
                <div class="clear"></div>
                <div class="ilink"><a href="<%=request.getContextPath()%>/register.do">注册账户</a>　|　<a href="<%=request.getContextPath()%>/forgetpassword.do" target="_parent">忘记密码？</a></div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="footer">
		<div class="box">
			<div class="sitemap">
				产品<br />
				<a href="<%=request.getContextPath()%>/cloudsever.do">云主机</a><br />
				<a href="<%=request.getContextPath()%>/cloudstorage.do">云硬盘</a>
			</div>
			<div class="sitemap">
				解决方案<br />
				<a href="<%=request.getContextPath()%>/solution.do">云管理平台</a><br />
				<a href="<%=request.getContextPath()%>/solution.do">云存储</a><br />
				<a href="<%=request.getContextPath()%>/solution.do">云桌面</a>
			</div>
			<div class="sitemap">
				帮助中心<br />
				<a href="<%=request.getContextPath()%>/help.do">常见问题</a><br />
				<a href="<%=request.getContextPath()%>/help.do">账户相关指南</a><br />
				<a href="<%=request.getContextPath()%>/help.do">云主机指南</a>
			</div>
			<div class="sitemap">
				关于我们<br />
				<a href="<%=request.getContextPath()%>/aboutus.do">关于我们</a><br /> 
			</div>
			<div class="sitemap" style="width: 100px;">
				关注我们<br />
				<a href="javascript:void(0);">微信公众号</a><br />
				<img src="<%=request.getContextPath()%>/image/weixin.gif" width="70" height="70" />
			</div>
			<div class="sitemap">
				&nbsp;<br />
				<a href="http://weibo.com/zhicloud" target="_blank">新浪微博</a><br />
				<img src="<%=request.getContextPath()%>/image/weibo.gif" width="70" height="70" />
			</div>
			<div class="hotline">
				<img src="<%=request.getContextPath()%>/image/tel.png" width="30" height="30"
					style="vertical-align: middle" /> 客服热线<br />
				<span style="font-size: 22px; color: #595959;">4000-212-999</span><br />
				<span>客服服务时间：7X24小时</span>
			</div>
			<div class="clear"></div>
			<div class="copyright">
				Copyright &copy; 2014 <a href="http://www.tianfusoftwarepark.com" target="_blank">成都天府软件园有限公司</a>, All rights reserved.
				蜀ICP备11001370号-3
			</div>
		</div> 
	</div>
  </div> 
</div>
</body>
</html>
