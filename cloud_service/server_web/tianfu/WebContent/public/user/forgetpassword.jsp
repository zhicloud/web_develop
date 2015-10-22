<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/user/refreshprice.js"></script>
<%-- <script src="<%=request.getContextPath() %>/javascript/jquery.min.js"></script>  --%>
<script src="<%=request.getContextPath() %>/javascript/common.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
$(document).ready(function(){
	init();
	inituser();
	initstep(1);
});
function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	window.location.href="<%=request.getContextPath()%>/user.do";
}
$(function(){
	// 换一个验证码
	$("#verification_code_img").click(function(){
		$("#verification_code").attr("src", "<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>&ts="+Math.random());
	});
	// 回车
	$("#inputemail,#inputcode").keypress(function(evt){
		if( evt.keyCode==13 ){
			login();
		}
	});
	// 登录
	$("#login_btn").click(function(){ 
		if(checkAccount()&checkCode()){
		login();
		}
	});
});
function checkAccount() {
	var account = new String($("#inputemail").val()).trim(); 
	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if(account==null || account==""){
		$("#tip-account").html("<font color=red>邮箱不能为空</font>");
		return false;
	}
	if(!myreg.test(account)){
		$("#tip-account").html("<font color=red>邮箱格式不正确</font>");
		return false;
	}
	if(account.length>30){
		$("#tip-account").html("<font color=red>输入不能超过30个字符</font>");
		return false;
	}
	else{
		$("#tip-account").html("");
		return true;
		}
	}
function checkCode(){
	var verificationCode = new String($("#inputcode").val()).trim();
	if(verificationCode==null || verificationCode==""){
		$("#tip-verificationcode").html("<font color=red>验证码不能为空</font>");
		return false;
	}if(verificationCode.length>10){
		$("#tip-verificationcode").html("<font color=red>验证码不正确</font>");
		return false;
	}
	else{
		$("#tip-verificationcode").html("");
		return true;
	}
}
window.name = "selfWin";
var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;

function login()
{
	var formData = $.formToBean(user_forget_password);
	ajax.remoteCall("bean://terminalUserService:optionChangePassword",
		[formData],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				top.$.messager.alert("警告","请重新填写信息","warning",function(){
					top.location.reload();
					});
			} 
			else if( reply.result.status=="success" )
			{
				top.location.href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=terminalUserService&method=uerForgetPassword"
				$("#inputemail").val("");
				$("#inputcode").val("");
			}
			else
			{
	// 			top.$.messager.alert("提示",reply.result.message , "warning");
				$("#tip-verificationcode").html("<font color=red>"+reply.result.message+"</font>");
				$("#verification_code_img").click();
				$("#inputemail").val("");
				$("#inputcode").val("");
			}
		}
	);
}
function reshVerification()
{
	$("#verification_code_img").click(); 	 
}
var t1 = window.setTimeout(reshVerification,1000);
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
  <div class="pageleft">
    <div class="header"> 
	   <div class="top"> 
	    <a class="logo l" href="<%=request.getContextPath()%>/"><img src="<%=request.getContextPath()%>/image/logo_tf.png" width="184" height="34" alt="天府软件园创业场" /></a> 
	    <div id="beforelogin" class="user r"> 
	     <a id="loginlink" href="javascript:void(0);" class="graylink">登录</a>
	     <span>|</span> 
	     <a id="reglink" href="javascript:void(0);">注册</a> 
	    </div> 
	    <div id="afterlogin" class="user r" style="display:none;">
	     <img class="reddot" src="<%=request.getContextPath()%>/image/reddot.png" width="6" height="6" alt=" " />
	     <a id="logoutlink" href="javascript:void(0);">注销</a>
	     <span>|</span>
	     <a href="<%=request.getContextPath()%>/user.do" class="bluelink">我的云端</a>
	    </div>
	    <div class="nav r">
	     <a href="<%=request.getContextPath()%>/" style="background:transparent;"><img id="nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/nav_1_i.png" width="20" height="20" alt="首页" style="padding:8px 0" /> </a>
	     <a href="<%=request.getContextPath()%>/cloudsever.do">云主机</a>
	     <a href="<%=request.getContextPath()%>/cloudstorage.do">云硬盘</a>
	     <a href="<%=request.getContextPath()%>/solution.do">解决方案</a>
	     <a href="<%=request.getContextPath()%>/help.do">帮助中心</a>
	     <a href="<%=request.getContextPath()%>/aboutus.do">关于我们</a>
	     <a href="#" style="display:none"></a>
	     <a href="<%=request.getContextPath()%>/user.do?flag=login" style="display:none"></a>
	     <a href="#" style="display:none"></a>
	     <a href="#" style="display:none">我的云端</a>
	    </div>
	   </div>
	   <div class="subnav">
	    <div class="box">
	     1
	    </div>
	    <div class="box">
	     2
	    </div>
	    <div class="box">
	     3
	    </div>
	    <div class="box">
	     4
	    </div>
	    <div class="box">
	     5
	    </div>
	    <div class="box">
	     6
	    </div>
	    <div class="box">
	     7
	    </div>
	    <div class="box">
	     8
	    </div>
	    <div class="box">
	     9
	    </div>
	    <div class="box">
	     <a id="overview" onclick="onSwitch(this);" href="#"><img id="nav_10_1" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_1_i.png" width="24" height="24" alt="概览" /><br />概览</a>
	     <a id="my_cloud_host_link" onclick="onSwitch(this);" href="#"><img id="nav_10_2" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_2_i.png" width="24" height="24" alt="我的云主机" /><br />我的云主机</a>
	     <a href="#" id="my_cloud_disk_link" onclick="onSwitch(this);"><img id="nav_10_3" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_3_i.png" width="24" height="24" alt="我的云硬盘" /><br />我的云硬盘</a>
	     <a href="#" id="recharge_record" onclick="onSwitch(this);"><img id="nav_10_4" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_4_i.png" width="24" height="24" alt="我的账户" /><br />我的账户</a>
	     <a href="#" id="oper_log" onclick="onSwitch(this);"><img id="nav_10_5" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_5_i.png" width="24" height="24" alt="操作日志" /><br />操作日志</a>
	     <a href="#" id="suggestion" onclick="onSwitch(this);"><img id="nav_10_6" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_6_i.png" width="24" height="24" alt="意见反馈" /><br />意见反馈</a>
	     <a href="#" id="my_uploaded_file_link" onclick="onSwitch(this);"><img id="nav_10_7" class="swapimage" src="<%=request.getContextPath()%>/image/nav_10_7_i.png" width="24" height="24" alt="文件夹" /><br />文件夹</a>
	    </div>
	   </div> 
	</div>
    <div class="main">
      <div class="titlebar"><a href="javascript:void(0);" onclick="window.history.back()"><img src="<%=request.getContextPath()%>/image/button_back.png" width="22" height="30" alt="返回" /></a>
        <div class="r">忘记密码</div>
      </div>
      <ul class="steps" style="width:480px;">
        <li class="l">输入账户<span>1</span></li>
        <li class="l">选择验证方式<span>2</span></li>
        <li class="l">验证<span>3</span></li>
        <li class="l">重置密码<span>4</span></li>
      </ul>
      <form class="wizard" id="user_forget_password" method="post">
        <ul style="width:4000px;">
          <li>
            <div class="box" style="text-align:center;padding:0 0 15px 0">请输入您需要找回密码的账户名，即是您注册时输入的邮箱</div>
            <div class="box" style="width:280px;padding:30px;">
              <input id="inputemail" name="inputemail" type="text" onfocus="inputfocus('email');" onblur="inputblur('email'),checkAccount();" class="reginput" style="border-color:#b2b2b2; color:#626262;"/>
              <div class="tip" style="color:#999">
                <label id="emaillabel" for="inputemail" class="reglabel" style="color:#999">输入邮箱</label>
                <span class="tip" id="tip-account"></span>
              </div>
              <input id="inputcode" name="inputcode" type="text" onfocus="inputfocus('code');" onblur="inputblur('code'),checkCode();" class="reginput l" style="width:140px;border-color:#b2b2b2; color:#626262;"/>
              <a href="javascript:void(0);" class="greenbutton l" style="width:80px; margin-left:10px;"><img id="verification_code" src="<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>" width="80" height="30" alt="验证码" /></a> 
              <a id="verification_code_img" href="javascript:void(0);" class="greenbutton r" style="width:30px; background: #4cda64 url(<%=request.getContextPath()%>/image/input_refresh.png) no-repeat center center">&nbsp;</a>
              <div class="clear">&nbsp;</div>
              <div class="tip" style="color:#999">
                <label id="codelabel" for="inputcode" class="reglabel" style="width:140px;color:#999">验证码</label>
                <span class="tip" id="tip-verificationcode"></span>
              </div>
            </div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a id="login_btn" class="bluebutton r" href="javascript:void(0);"s onclick="">下一步</a></div>
          </li>
          <li>
            <div class="box" style="text-align:center;padding:0 0 15px 0">为了保护您的账户安全，我们需要通过短信验证码或电子邮箱进行验证</div>
            <div id="accountselect"><br />
              <a class="graybutton" href="javascript:void(0);" onclick="nextstep();">
              <div class="labeltitle">通过短信验证码验证</div>
              <div class="labeldesc">如果您的<b>139****6666</b>手机还在正常使用，请选择此方式验证</div>
              </a> <a class="graybutton" href="javascript:void(0);" onclick="nextstep();">
              <div class="labeltitle">通过电子邮箱验证</div>
              <div class="labeldesc">如果您的<b>zhi***@gmail.com</b>邮箱还在正常使用，请选择此方式验证</div>
              </a>
              <div class="clear"></div>
            </div>
          </li>
          <li>
            <div id="codetype1">
              <div class="box" style="text-align:center;padding:0 0 15px 0">我们已将短信验证码发送到您的手机，如未收到可在60秒后重新获取短信验证码</div>
              <div class="box" style="width:280px;padding:30px;">
                <input id="inputmessage" name="" type="text" onfocus="inputfocus('message');" onblur="inputblur('message');" class="reginput l" style="width:140px; border-color:#b2b2b2; color:#626262;"/>
                <a href="javascript:void(0);" class="greenbutton r">获取短信验证码</a>
                <div class="clear">&nbsp;</div>
                <div class="tip" style="color:#999">
                  <label id="messagelabel" for="inputmessage" class="reglabel" style="width:140px; color:#999">短信验证码</label>
                  出错提示</div>
              </div>
            </div>
            <div id="codetype1">
              <div class="box" style="text-align:center;padding:0 0 15px 0">我们已将邮件验证码发送到您的邮箱，如未收到可在60秒后重邮箱新获取验证码</div>
              <div class="box" style="width:280px;padding:30px;">
                <input id="inputmessage2" name="" type="text" onfocus="inputfocus('message2');" onblur="inputblur('message2');" class="reginput l" style="width:140px; border-color:#b2b2b2; color:#626262;"/>
                <a href="javascript:void(0);" class="greenbutton r">获取邮箱验证码</a>
                <div class="clear">&nbsp;</div>
                <div class="tip" style="color:#999">
                  <label id="message2label" for="inputmessage2" class="reglabel" style="width:140px; color:#999">邮箱验证码</label>
                  出错提示</div>
              </div>
            </div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);"s onclick="nextstep();">验证</a><a class="graybutton r" href="javascript:void(0);" onclick="prevstep();">上一步</a></div>
          </li>
          <li>
            <div id="restpassword1">
              <div class="infoicon">密码已重置</div>
              <div class="info" style="padding:0 0 30px 0">系统已为您生成新密码并发送到您的邮箱，请前往邮箱获取新密码</b></div>
              <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);">完成</a></div>
            </div>
            <div id="restpassword2">
              <div class="infoicon">密码已重置</div>
              <div class="info" style="padding:0 0 30px 0">系统已为您生成新密码并短信发送到您的手机，请查看短信获取新密码</b></div>
              <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);">完成</a></div>
            </div>
          </li>
        </ul>
      </form>
    </div>
    <div class="clear"></div>
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
  <div class="pageright">
    <iframe id="loginiframe" src="<%=request.getContextPath()%>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    <iframe id="regiframe" src="<%=request.getContextPath()%>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  </div>
</div>
</body>
</html>
