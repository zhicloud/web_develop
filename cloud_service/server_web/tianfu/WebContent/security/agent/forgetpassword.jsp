<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_AGENT;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>致云代理商管理平台</title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/agent.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/user/refreshprice.js"></script>
<%-- <script src="<%=request.getContextPath() %>/javascript/jquery.min.js"></script>  --%>
<script src="<%=request.getContextPath() %>/javascript/agent.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=<%=userType%>");
$(document).ready(function(){
	init(3);
	inituser();
	initstep(1);
	
	//手机获取
	$("#sendMessage1").click(function(){
		 $("#codetype2").hide();
	     $("#codetype1").show();
		 countDown(this,60);
	});
	//邮箱获取
	$("#sendMessage2").click(function(){
		 $("#codetype1").hide();
		 $("#codetype2").show();
 		 sendEmailMessage(this,60);
	});
});
function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	window.location.href="<%=request.getContextPath()%>/user.do";
}
$(function(){
	// 换一个验证码
	$("#verification_code_img,#verification_code").click(function(){
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
	ajax.remoteCall("bean://agentService:optionChangePassword",
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
 				$("#inputemail").val("");
				$("#inputcode").val("");
				$("#phone_get").html(reply.result.properties.phone);
				$("#email_get").html(reply.result.properties.email);
				$("#userId").val(reply.result.properties.userId); 
				nextstep();
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
var timeoutemail;
var timeoutephone;
function countDown(obj,second){
	
	if(timeoutemail){
		clearTimeout(timeoutemail);
	}
	var flag = 0;
	$("#sendcode").removeClass("greenbutton");
	$("#sendcode").removeClass("shadow");
	$("#sendcode").addClass("graylinebutton");
	$("#sendcode").html(second+"秒后可重新获取");
	$("#sendcode").unbind("click");
	$("#codetype1").show();
	$("#codetype2").hide();
    // 如果秒数还是大于0，则表示倒计时还没结束
	if (second==59){
		var formData = $.formToBean(user_forget_password);
		ajax.remoteCall("bean://agentService:resetPasswordSendPhoneCode", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
						top.$.messager.alert("警告","会话超时，请重新登录","warning");
					}
					else{
						top.$.messager.alert("警告",reply.exceptionMessage,"warning",function(){
							top.location.reload();
						});
					} 
				}
				else if(reply.result.status=="fail"){
					$("#sendcode").removeClass("graylinebutton");
					$("#sendcode").addClass("greenbutton");
					$("#sendcode").addClass("shadow");
					$("#sendcode").html("获取短信验证码");
					$("#sendcode").click(function(){
						countDown(this,60);
					});
					$("#tip-mobileMessage").html("<font color=red>"+reply.result.message+"</font>"); 
					flag = 1;
				}
				else if(reply.result.status == "success") { 
					$("#tip-mobileMessage").html("<font color=red>"+reply.result.message+"</font>"); 
				}
				else {
					$("#tip-mobileMessage").html("<font color=red>请输入正确短信验证码！</font>");  
				}
			}
		);
	}
    if(flag == 1){
    	return;
    }
    if(second>0){
        // 时间减一
        second--;
        // 一秒后重复执行
        timeoutephone = setTimeout(function(){countDown(obj,second);},1000);
    // 否则，按钮重置为初始状态
    }else{
    	$("#sendcode").removeClass("graylinebutton");
		$("#sendcode").addClass("greenbutton");
		$("#sendcode").addClass("shadow");
		$("#sendcode").html("获取短信验证码");
		$("#sendcode").click(function(){
			countDown(this,60);
		});
    } 
}
function sendEmailMessage(obj,second){
	if(timeoutephone){
		clearTimeout(timeoutephone);
	}
	var flag = 0;
	$("#sendEmailCode").removeClass("greenbutton");
	$("#sendEmailCode").removeClass("shadow");
	$("#sendEmailCode").addClass("graylinebutton");
	$("#sendEmailCode").html(second+"秒后可重新获取");
	$("#sendEmailCode").unbind("click");
	$("#codetype2").show();
	$("#codetype1").hide();
	if(second==55){
		var formData = $.formToBean(user_forget_password);
		ajax.remoteCall("bean://agentService:resetPasswordSendEmailCode",
			[formData],
			function(reply)
			{
				if( reply.status=="exception" )
				{
					if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
						top.$.messager.alert("警告","会话超时，请重新登录","warning");
					}
					else{
						top.$.messager.alert("警告",reply.exceptionMessage,"warning",function(){
							top.location.reload();
						});
					} 
				} 
				else if(reply.result.status=="fail"){
					$("#sendEmailCode").removeClass("graylinebutton");
					$("#sendEmailCode").addClass("greenbutton");
					$("#sendEmailCode").addClass("shadow");
					$("#sendEmailCode").html("获取邮箱验证码");
					$("#sendEmailCode").click(function(){
						sendEmailMessage(this,60);
					});
					$("#tip-emailMessage").html("<font color=red>"+reply.result.message+"</font>");  
					flag = 1;
				}
				else if( reply.result.status=="success" )
				{
					$("#tip-emailMessage").html("<font color=red>"+reply.result.message+"</font>");

				}
				else
				{ 
					top.$.messager.alert('提示',reply.result.message,'info'); 
				}
			}
		);
	}
	if(flag == 1){
    	return;
    }
    if(second>0){
        // 时间减一
        second--;
        // 一秒后重复执行
        timeoutemail = setTimeout(function(){sendEmailMessage(obj,second);},1000);
    // 否则，按钮重置为初始状态
    }else{
    	$("#sendEmailCode").removeClass("graylinebutton");
		$("#sendEmailCode").addClass("greenbutton");
		$("#sendEmailCode").addClass("shadow");
		$("#sendEmailCode").html("获取邮箱验证码");
		$("#sendEmailCode").click(function(){
			sendEmailMessage(this,60);
		});
    } 
}

function confirmationCode(){
	if(!(checkMobileMessage()||checkEmailMessage())){
		return;
	}
	var formData = $.formToBean(user_forget_password);
	ajax.remoteCall("bean://agentService:resetPasswordCheckEmailOrPhoneCode",
		[formData],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				top.$.messager.alert("警告","信息有误","warning",function(){
					top.location.reload();
				});
			} else if( reply.result.status=="success" )
			{
				nextstep();
				$("#tip-success").html("<font color=red>"+reply.result.message+"</font>");
				
			}else
			{	
				var inputmessage = new String($("#inputmessage").val()).trim();
				var inputmessage2 = new String($("#inputmessage2").val()).trim();
				if(!(inputmessage==null||inputmessage=="")){
					$("#tip-mobileMessage").html("<font color=red>"+reply.result.message+"</font>");
				}else if(!(inputmessage2==null||inputmessage2=="")){
					$("#tip-emailMessage2").html("<font color=red>"+reply.result.message+"</font>");
				}
			}
		}
	);
}

function finishReset(){
	top.location.href = "<%=request.getContextPath()%>/agent.do"; 
}

function checkMobileMessage(){
	var inputmessage = new String($("#inputmessage").val()).trim();
	if(inputmessage==null||inputmessage==""){
		$("#tip-mobileMessage").html("<font color=red>验证码不能为空</font>");
		return false;
	}else if(inputmessage.length>10){
		$("#tip-mobileMessage").html("<font color=red>验证码不正确</font>");
		return false;
	}else{
		$("#tip-mobileMessage").html("");
		return true;
	}
}
function checkEmailMessage(){
	var inputmessage2 = new String($("#inputmessage2").val()).trim();
	if(inputmessage2==null||inputmessage2==""){
		$("#tip-emailMessage2").html("<font color=red>验证码不能为空</font>");
		return false;
	}else if(inputmessage2.length>10){
		$("#tip-emailMessage2").html("<font color=red>验证码不正确</font>");
		return false;
	}else{
		$("#tip-emailMessage2").html("");
		return true;
	}
}
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
	   <a class="logo l" href="#"><img src="<%=request.getContextPath()%>/image/agent_logo.png" width="188" height="25" alt="致云代理商管理平台" /></a>
	   <div class="nav l">
	    <a href="#" id="business"><img id="agent_nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_1_i.png" width="21" height="21" />业务信息</a>
	    <a href="#" id="user_manage"><img id="agent_nav_2" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_2_i.png" width="21" height="21" />用户管理</a>
	    <a href="#" id="my_account"><img id="agent_nav_3" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_3_i.png" width="21" height="21" />我的账户</a>
	    <a href="#" id="oper_log"><img id="agent_nav_4" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_4_i.png" width="21" height="21" />操作日志</a>
	   </div>
	   <div class="user l">
	    <img class="reddot" src="<%=request.getContextPath()%>/image/reddot.png" width="6" height="6" alt=" " />
	    <a id="logoutlink" href="javascript:void(0);">注销</a>
	    <span>|</span>
	    <a id="userlink" href="javascript:void(0);"></a>
	   </div>
	   <div class="clear"></div>
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
      <input name="userId" id="userId" type="hidden" value=""/>
        <ul style="width:4000px;">
          <li>
            <div class="box" style="text-align:center;padding:0 0 15px 0">请输入您需要找回密码的账户名，即是您注册时输入的邮箱</div>
            <div class="box" style="width:280px;padding:30px; margin:0 auto;">
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
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a id="login_btn" class="bluebutton r" href="javascript:void(0);"  onclick="">下一步</a></div>
          </li>
          <li>
            <div class="box" style="text-align:center;padding:0 0 15px 0">为了保护您的账户安全，我们需要通过短信验证码或电子邮箱进行验证</div>
            <div id="accountselect"><br />
              <a id="sendMessage1" class="graybutton" href="javascript:void(0);" onclick="nextstep();">
              <div class="labeltitle">通过短信验证码验证</div>
              <div class="labeldesc">如果您的<b id="phone_get">139****6666</b>手机还在正常使用，请选择此方式验证</div>
              </a> <a id="sendMessage2" class="graybutton" href="javascript:void(0);" onclick="nextstep();">
              <div class="labeltitle">通过电子邮箱验证</div>
              <div class="labeldesc">如果您的<b id="email_get">zhi***@gmail.com</b>邮箱还在正常使用，请选择此方式验证</div>
              </a>
              <div class="clear"></div>
            </div>
          </li>
          <li>
            <div id="codetype1">
              <div class="box" style="text-align:center;padding:0 0 15px 0">我们已将短信验证码发送到您的手机，如未收到可在60秒后重新获取短信验证码</div>
              <div class="box" style="width:280px;padding:30px;margin:0 auto">
                <input id="inputmessage" name="myGetPhoneMessage" type="text" onfocus="inputfocus('message');" onblur="inputblur('message'),checkMobileMessage();" class="reginput l" style="width:140px; border-color:#b2b2b2; color:#626262;"/>
                <a id="sendcode" href="javascript:void(0);" class="greenbutton r">获取短信验证码</a>
                <div class="clear">&nbsp;</div>
                <div class="tip" style="color:#999">
                  <label id="messagelabel" for="inputmessage" class="reglabel" style="width:140px; color:#999">短信验证码</label>
        			<span class="tip" id="tip-mobileMessage">注意填写验证码</span> 
                  </div>
              </div>
            </div>
            <div id="codetype2">
              <div class="box" style="text-align:center;padding:0 0 15px 0">我们已将邮件验证码发送到您的邮箱，如未收到可在60秒后重邮箱新获取验证码</div>
              <div class="box" style="width:280px;padding:30px;margin:0 auto">
                <input id="inputmessage2" name="myGetEmailMessage" type="text" onfocus="inputfocus('message2');" onblur="inputblur('message2'),checkEmailMessage();" class="reginput l" style="width:140px; border-color:#b2b2b2; color:#626262;"/>
                <a  id="sendEmailCode" href="javascript:void(0);" class="greenbutton r">获取邮箱验证码</a>
                <div class="clear">&nbsp;</div>
                <div class="tip" style="color:#999">
                  <label id="message2label" for="inputmessage2" class="reglabel" style="width:140px; color:#999">邮箱验证码</label>
        			<span class="tip" id="tip-emailMessage2">注意填写验证码</span>
                  </div>
              </div>
            </div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);"  onclick="confirmationCode();">验证</a><a class="graybutton r" href="javascript:void(0);" onclick="prevstep();">上一步</a></div>
          </li>
          <li>
            <div id="restpassword1">
              <div class="infoicon">密码已重置</div>
              <div class="info" style="padding:0 0 30px 0">系统已为您生成新密码并发送到您的<span class="tip" id="tip-success"></span>，请注意查收</b></div>
              <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);" onclick="finishReset();">完成</a></div>
            </div>
          </li>
        </ul>
      </form>
    </div>
    <div class="clear"></div>
    <div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
  </div>
  </div> 
</div>
</body>
</html>
