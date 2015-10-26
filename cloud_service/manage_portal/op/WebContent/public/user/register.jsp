<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	String URL = (String)request.getAttribute("url");
	String code = (String)request.getParameter("code");
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
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
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
var a = '<%=request.getContextPath()%>' ;
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
$(document).ready(function(){ 
});
function initlayout(){
	var s = $(window).width();
		if( s > 480){
			$(".right").addClass("rightreg");
		}else{
			$(".right").removeClass("rightreg");
	};
}
function checkPassword(){
	var myPassword = new String($("#inputpassword").val()).trim();
	if(myPassword==null || myPassword==""){
		$("#tip-password").html("密码不能为空");
		return false;
	}
	if(myPassword.length>=6&&myPassword.length<=20){
		$("#tip-password").html("");
		return true;
	}else{
		$("#tip-password").html("密码长度为6-20个字符");
		return false;
	}
}
function checkPasswordConf(){
	var passwordOne = new String($("#inputpassword").val()).trim();
	var passwordTwo = new String($("#inputpassword2").val()).trim();
	if(passwordTwo.length<6||passwordTwo.length>20){ 
		$("#tip-passwordconf").html("请输入6-20个字符");
		return false;
	} 
	if(passwordTwo==null || passwordTwo==""){
		$("#tip-passwordconf").html("确认密码不能为空");
		return false;
	}
	if(passwordOne!=passwordTwo){
		$("#tip-passwordconf").html("两次输入的密码不一致");
		return false;
	}else{
		$("#tip-passwordconf").html("");
		return true;
	}
}
function checkPhone(){
	var phone = new String($("#inputmobile").val()).trim();
	if(phone==null || phone==""){
		$("#tip-phone").html("手机号码不能为空");
		return false;
	}
	if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))){ 
		$("#tip-phone").html("请输入正确的手机号码");
		return false;
	}else{
		$("#tip-phone").html("");
		return true;
	}
}

function checkVcode(){
	var verificationCode = new String($("#inputcode").val()).trim();
	if(verificationCode==null || verificationCode==""){
		$("#tip-verificationCode").html("验证码不能为空");
		return false;
	}if(verificationCode.length>2){
		$("#tip-verificationCode").html("验证码不正确");
		return false;
	}
	else{
		$("#tip-verificationCode").html("");
		return true;
	}
}

function checkMessage(){
	var messageCode = new String($("#inputmessage").val()).trim();
	if(messageCode==null||messageCode==""){
		$("#tip-messageCode").html("短息验证码不能为空");
		return false;
	}if(messageCode.length>10){
		$("#tip-messageCode").html("短息验证码长度不能超过10位");
		return false;
	}
	else{
		$("#tip-messageCode").html("");
		return true;
	}
	
}

function checkInvite(){
	var inviteCode = new String($("#inputinvite").val()).trim();
	if(inviteCode==null||inviteCode==""){
		$("#tip-inviteCode").html("邀请码不能为空");
		return false;
	}else if(inviteCode.length>10){
		$("#tip-inviteCode").html("输入不超过10个字符");
		return false;
	}else{
		$("#tip-inviteCode").html("");
		return true;
	}
}
function checkRule(){ 
	if(document.getElementById("rule").checked){		
		$("#tip-rule").html("");
		return true;
	}else{
		$("#tip-rule").html("您未同意相关服务条款");
		return false;
		
	} 
}
var action;
function countDown(obj,second){
	if(!(checkAccount()&checkPassword()&checkPhone()&checkVcode()&checkPasswordConf()))
	{
		return;
	}
	var flag = 0;
	$("#sendcode").hide(); 
	$("#secendback").show(); 
	$("#inputmobile").attr("disabled",true);
	
	$("#secendback").html(second+"秒后可重新获取"); 
    // 如果秒数还是大于0，则表示倒计时还没结束
	if (second==60){
		var formData = $.formToBean(terminal_user_add_dlg_form);
		ajax.remoteCall("bean://terminalUserService:checkSmsRegister", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					alert(reply.exceptionMessage);
				}
				else if(reply.result.status=="fail"){ 
					$("#sendcode").show();
					$("#secendback").hide();  
					$("#inputmobile").attr("disabled",false);
// 					$("#tip-phone").html("手机号码有误！");  
					top.$.messager.alert("提示", reply.result.message, "warning",function(){
// 					window.location.reload();
					$("#inputcode").val("");
					$("#verification_code_img").click();
					});
					flag = 1;
				}
				else if(reply.result.status == "success") { 
					$("#tip-messageCode").html(""); 
			        $("#isTrue").val("true");
				}
				else {
					$("#tip-messageCode").html("请输入正确短信验证码！");  
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
        action = setTimeout(function(){countDown(obj,second);},1000);
    // 否则，按钮重置为初始状态
    }else{
    	$("#sendcode").show();
		$("#secendback").hide(); 
		$("#inputmobile").attr("disabled",false);
    } 
}

window.name = "selfWin";
var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;
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
	else
		{
			var flag = false;
	var formData = $.formToBean(terminal_user_add_dlg_form);
	ajax.remoteCall("bean://terminalUserService:checkAccount", 
		[ formData ],
		function(reply) {
			if (reply.status == "exception") 
			{
				alert(reply.exceptionMessage);
			}else if(reply.result.status=="fail")
			{
				$("#tip-account").html("邮箱已经注册，请更换邮箱");
			}else
			{
				$("#tip-account").html("");
				flag = true;
			}
		}
	);
	return flag;
   }
}
var _terminal_user_add_dlg_scope_ = new function(){
	
	var self = this;
	// 保存
	self.save = function() {
		var formData = $.formToBean(terminal_user_add_dlg_form);
		ajax.remoteCall("bean://terminalUserService:addTerminalUserWithoutLogin", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					$("#terminal_user_add_dlg_save_btn").attr("value","注  册");
					$("#terminal_user_add_dlg_save_btn").removeAttr("disabled");
					top.$.messager.alert('警告','请重新注册','warning');
					window.location.reload();
				} else if (reply.result.status == "success") {
					// 注册成功
					clearTimeout(action);
					$("<div class=\"datagrid-mask\"></div>").css({
						display:"block",
						width:"100%",
						height:"100%"
					}).appendTo("body"); 
					$("<div class=\"datagrid-mask-msg\"></div>").html("注册成功，正在跳转页面。。。").appendTo("body").css({
						display:"block",
						left:($(document.body).outerWidth(true) - 190) / 2,
						top:($(window).height() - 45) / 2
					});
					window.setTimeout(function(){  
					    parent.window.location.href="<%=request.getContextPath()%>/user.do";
					}, 1000);
					// 跳转页面
				}
				else {  
					$("#tip-messageCode").html(""+reply.result.message+""); 
					$("#verification_code_img").click();
					$("#verification_code").val("");
					$("#terminal_user_add_dlg_save_btn").removeAttr("disabled");
					$("#terminal_user_add_dlg_save_btn").attr("value","注  册");
				} 
			}
		);
	};

	// 关闭
	self.close = function() {
		$("#terminal_user_add_dlg").dialog("close");
	};
	// 初始化
	$(document).ready(function(){
		// 换一个验证码
		$("a[name=verification_code_img]").click(function(){
			$("#verification_code").attr("src", "<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>&ts="+Math.random());
		});
		// 回车
		$("#inputemail, #inputpassword,#inputpassword2,#inputmobile,#inputmessage,#inputcode,#inputinvite").keypress(function(evt){
			if( evt.keyCode==13 ){
				$("#terminal_user_add_dlg_save_btn").click();
			}
		});
		// 保存
		$("#terminal_user_add_dlg_save_btn").click(function() {
			$("#terminal_user_add_dlg_save_btn").attr("value","正在注册");
			$("#terminal_user_add_dlg_save_btn").attr("disabled","disabled");
			if(checkAccount()&checkPassword()&checkPhone()&checkMessage()&checkVcode()&checkPasswordConf()&checkRule()){
				self.save(); 
			}else{
				$("#terminal_user_add_dlg_save_btn").attr("value","注  册");
				$("#terminal_user_add_dlg_save_btn").removeAttr("disabled");
// 				top.$.messager.show({
// 					title:'提示',
// 					msg:'输入信息有误或不完整,无法提交',
// 					timeout:10000,
// 					showType:'slide'
// 				});
			}
		});
		// 关闭
		$("#terminal_user_add_dlg_close_btn").click(function() {
			self.close();
		});
		//倒计时
		$("#sendcode").click(function(){
			countDown(this,60);
		});
		
		$("#secendback").hide();  
	});
};

 
// function reshVerification()
// { 
// 	$("#verification_code_img").click(); 	 
// }
// var t2 = window.setInterval(reshVerification,900000);
</script>
<!--[if IE 6]>
<script src="javascript/DD_belatedPNG.js"></script>
<script type="text/javascript">
	DD_belatedPNG.fix("*");
</script>
<![endif]-->
</head>

<body style="overflow:hidden;">
<div class="right">
  <div class="box">
    <div class="title">账号注册</div>
      <input name="isTrue" id="isTrue" type="hidden" value="false"/>
      <form id="terminal_user_add_dlg_form" method="post"> 
      <input id="inputemail" autocomplete="off" name="account" type="text" onfocus="inputfocus('email');" onblur="inputblur('email'),checkAccount()" class="itext"  />
      <div class="itip">
        <label id="emaillabel" for="inputemail" class="ilabel">输入邮箱</label>
        <span class="err" id="tip-account"></span> 
      </div>
        
      <input id="inputpassword" autocomplete="off" name="password" type="password" onpaste='return false' onfocus="inputfocus('password');" onblur="inputblur('password'),checkPassword()" class="itext"/>
      <div class="itip" >
        <label id="passwordlabel" for="inputpassword" class="ilabel">输入密码</label>
        <span class="err" id="tip-password"></span> 
      </div>
      
      <input id="inputpassword2" autocomplete="off" name="confpassword" type="password" onpaste='return false' onfocus="inputfocus('password2');" onblur="inputblur('password2'),checkPasswordConf()" class="itext"/>
      <div class="itip">
        <label id="password2label" for="inputpassword2" class="ilabel">确认密码</label>
        <span class="err" id="tip-passwordconf"></span> 
      </div>
        
      <input id="inputmobile" autocomplete="off" name="phone" type="text" onfocus="inputfocus('mobile');" onblur="inputblur('mobile'),checkPhone()" class="itext"/>
      <div class="itip" >
        <label id="mobilelabel" for="inputmobile" class="ilabel">手机号码</label>
        <span class="err" id="tip-phone">绑定手机接收系统重要通知</span>
      </div>
      
      <input id="inputcode" autocomplete="off" name="verificationCode" type="text" onfocus="inputfocus('code');" onblur="inputblur('code'),checkVcode()"  class="itext l" style="width:100px"/>
      <a href="javascript:void(0);" name="verification_code_img" class="itext l" style="width:100px; height:40px; padding:0; margin-left:10px;"><img id="verification_code"  src="<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>"  width="100" height="40" alt="验证码" class="code"/></a>
       <div class="r" style="height:12px; line-height:16px; padding:4px 0 4px 0;"><a  id="verification_code_img" name="verification_code_img" href="javascript:void(0);">看不清<br />
              换一张</a></div>
      <div class="clear">&nbsp;</div>
      <div class="itip">
        <label id="codelabel" for="inputcode" class="ilabel" style="width:80px">验证码</label>
        <span class="err" id="tip-verificationCode"></span> 
      </div>
        
      <input id="inputmessage" autocomplete="off" name="messageCode" type="text" onfocus="inputfocus('message');" onblur="inputblur('message'),checkMessage()" class="itext l" style="width:100px"/>
       
      <div class="r" style="width:150px;height:40px;">
      <a id="sendcode" href="javascript:void(0);" class="iorangebtn" style="width:150px; height:40px; font-size:14px;">获取短信验证码</a>
      <div  id="secendback" class="igraybtn" style="width:150px; height:40px; font-size:14px; color:#999;">60秒后重新获取</div>
      </div>
      <div class="clear">&nbsp;</div>
      
      <div class="itip">
        <label id="messagelabel" for="inputmessage" class="ilabel" style="width:80px">短信验证码</label>
        <span class="err" id="tip-messageCode"></span>
     </div> 
     
     
     <div class="ilink2"><input id="rule" name="rule" type="checkbox" value="1" />我已阅读并同意相关<a href="<%=request.getContextPath() %>/public/eula.jsp" target="_blank">服务条款</a></div>
      <div class="itip"> 
        <span class="err" id="tip-rule"></span>
     </div> 
     
          
      <a href="javascript:void(0);" class="igraybtn r" onclick="parent.slideright();">返回</a><a href="javascript:void(0);" class="ibluebtn l"  id="terminal_user_add_dlg_save_btn" >注册</a>
      <div class="clear"></div>

    </form>
  </div>
</div>
</body>
</html>
