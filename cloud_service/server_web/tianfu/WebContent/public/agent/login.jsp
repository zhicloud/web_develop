<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_AGENT;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>致云代理商管理平台</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/agent.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script src="<%=request.getContextPath()%>/javascript/agent.js"></script>
<script type="text/javascript">
var a = '<%=request.getContextPath()%>' ;
$(document).ready(function(){
	init(0);
	inituser();
});
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
	var formData = $.formToBean(big_form);
	ajax.remoteCall("bean://sysUserService:login",
		[formData],
		function(reply)
		{ 
			if( reply.status=="exception" )
			{
				top.$.messager.alert("提示", reply.exceptionMessage, "warning");
			} 
			else if( reply.result.status=="success" )
			{ 
				// 跳转页面  
				 window.location.reload();
			}
			else
			{ 
//				top.$.messager.alert("提示",reply.result.message,"warning");
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
		parent.$("#reglink").click();
 	});
});

$(document).ready(function(){  
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
<div class="loginpage">
  <div class="loginheader"><img src="<%=request.getContextPath()%>/image/logo_agent.png" width="238" height="32" /></div>
  <div class="loginmain">
    <div class="loginbox">
      <div class="loginpanel">
        <div class="lp"></div>
        <div class="rp">
          <div class="title">致云账号登录</div>
          <form id="big_form" method="post">
            <input type="hidden" id="type" name="type" value="<%=userType%>"/>
 		      <input id="inputemail" name="account" type="text" onfocus="inputfocus('email');" onblur="inputblur('email');checkAccount();" class="itext" style="background:#eef0f1"/>
		      
		      <div class="itip" style="height:20px;">
		        <label id="emaillabel" for="inputemail" class="ilabel" style="background:#eef0f1">输入邮箱</label>
		        <span class="err" id="tip-account"></span>  
		      </div>
		      <input id="inputpassword" name="password" type="password" onfocus="inputfocus('password');" onblur="inputblur('password');checkPassword();" class="itext" style="background:#eef0f1"/>
 		      <div class="itip" style="height:20px;">
		        <label id="passwordlabel" for="inputpassword" class="ilabel" style="background:#eef0f1">输入密码</label>
		        <span class="err" id="tip-password"></span>
		      </div>
		       
		      <input id="inputcode" autocomplete="off" name="verification_code" type="text" onfocus="inputfocus('code');" onblur="inputblur('code'),checkVcode()" class="itext l"  style="width:100px;background:#eef0f1"/>
		      <a href="javascript:void(0);" name="verification_code_img" class="itext l" style="width:100px; height:40px; padding:0; margin-left:10px;"><img id="verification_code"  src="<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>"  width="100" height="40" alt="验证码" class="code"/></a>
		      <div class="r" style="height:12px; line-height:16px; padding:4px 0 4px 0;"><a id="verification_code_img" name='verification_code_img' href="javascript:void(0);">看不清<br />
		换一张</a></div>
		      
		       <div class="clear">&nbsp;</div>
		      <div class="itip" style="height:20px;">
		        <label id="codelabel" for="inputcode"    class="ilabel" style="width:80px;background:#eef0f1">验证码</label>
		        <span class="err" id="tip-verificationcode"></span>
		      </div>
            <a href="javascript:void(0);" class="ibluebtn" onclick="loadingbegin();" id="login_btn" style="width:280px;margin:28px 0;">立即登录</a>
            <div class="clear"></div>
            <div class="ilink"><a href="<%=request.getContextPath()%>/agentforgetpassword.do">忘记密码？</a></div>
          </form>
        </div>
      </div>
    </div>
  </div>
  <div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
  </div>
</div>
</body>
</html> 