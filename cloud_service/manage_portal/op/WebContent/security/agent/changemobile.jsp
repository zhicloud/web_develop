<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.vo.AgentVO"%>
<%@page import="com.zhicloud.op.vo.SysRoleVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	String userId  = loginInfo.getUserId();
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	AgentVO agentVO = (AgentVO)request.getAttribute("agentVO");
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
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<%-- <script src="<%=request.getContextPath() %>/javascript/jquery.min.js"></script>  --%>
<script src="<%=request.getContextPath() %>/javascript/agent.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=<%=userType%>");
$(document).ready(function(){
	init(3);
	inituser('<%=loginInfo.getAccount()%>',0);
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
	//再次邮箱
	$("#sendMessage3").click(function(){
 		 sendMobileMessageAgain(this,60);
	});
});
function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	window.location.reload();
}
function checkMobileMessage(){
	var inputmessage = new String($("#inputmessage").val()).trim();
	if(inputmessage==null||inputmessage==""){
		$("#tip-mobileMessage").html("<font color=red>验证码不能为空</font>");
		return false;
	}else if(inputmessage.length>10){
		$("#tip-mobileMessage").html("<font color=red>验证码长度不超过10位</font>");
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
		$("#tip-emailMessage2").html("<font color=red>验证码长度不超过10位</font>");
		return false;
	}else{
		$("#tip-emailMessage2").html("");
		return true;
	}
}

function checkEmailMessage3(){
	var inputmessage3 = new String($("#inputmessage3").val()).trim();
	if(inputmessage3==null||inputmessage3==""){
		$("#tip-emailMessage3").html("<font color=red>验证码不能为空</font>");
		return false;
	}else if(inputmessage3.length>10){
		$("#tip-emailMessage3").html("<font color=red>验证码长度不超过10位</font>");
		return false;
	}else{
		$("#tip-emailMessage3").html("");
		return true;
	}
}
function checkPhone(){
	var phone = new String($("#inputmobile").val()).trim();
	if(phone==null || phone==""){
		$("#tip-mobile").html("<font color=red>手机号码不能为空</font>");
		return false;
	}
	if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))){ 
		$("#tip-mobile").html("<font color=red>请输入正确的手机号码</font>");
		return false;
	}else{
		$("#tip-mobile").html("");
		return true;
	}
}
window.name = "selfWin";
ajax.async = false;
function confirmationCode(){
	if(!(checkMobileMessage()||checkEmailMessage())){
		return;
	}
	var formData = $.formToBean(change_Mobile_choice_option);
	ajax.remoteCall("bean://agentService:changeEmailCheckEmailOrPhone",
		[formData],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
					top.$.messager.alert("警告","会话超时，请重新登录","warning");
				}
				else{
					top.$.messager.alert("警告","验证码信息有误","warning",function(){
						top.location.reload();
					});
				} 
			} else if( reply.result.status=="success" )
			{
				nextstep();
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
function confirmationChange(){
	if(!(checkPhone()&checkEmailMessage3())){
		return;
	}
	var formData = $.formToBean(change_Mobile_choice_option);
	ajax.remoteCall("bean://agentService:updateBaseInfoPhone",
		[formData],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
					top.$.messager.alert("警告","会话超时，请重新登录","warning");
				}
				else{
					top.$.messager.alert("警告","修改失败","warning",function(){
						top.location.reload();
					});
				} 
			} 
			else if( reply.result.status=="success" )
			{
				nextstep();
				$("#tip-newEmail").html("<b>"+reply.result.message+"</b>"); 
			}
			else
			{
				$("#tip-emailMessage3").html("<font color=red>"+reply.result.message+"</font>");
// 				top.$.messager.alert('提示',reply.result.message,'info');
			}
		}
	);
}
function countDown(obj,second){
	var flag = 0;
	$("#sendcode").removeClass("greenbutton");
	$("#sendcode").removeClass("shadow");
	$("#sendcode").addClass("graylinebutton");
	$("#sendcode").html(second+"秒后可重新获取");
	$("#sendcode").unbind("click");
    // 如果秒数还是大于0，则表示倒计时还没结束
	if (second==59){
		var formData = $.formToBean(change_Mobile_choice_option);
		ajax.remoteCall("bean://agentService:changeEmailOrPhone", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
						top.$.messager.alert("警告","会话超时，请重新登录","warning");
					}
					else{
						top.$.messager.alert("警告","修改失败","warning",function(){
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
        setTimeout(function(){countDown(obj,second);},1000);
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
	var flag = 0;
	$("#sendEmailCode").removeClass("greenbutton");
	$("#sendEmailCode").removeClass("shadow");
	$("#sendEmailCode").addClass("graylinebutton");
	$("#sendEmailCode").html(second+"秒后可重新获取");
	$("#sendEmailCode").unbind("click");
	if(second==55){
		var formData = $.formToBean(change_Mobile_choice_option);
		ajax.remoteCall("bean://agentService:updateBaseInfoPhoneEmailCode",
			[formData],
			function(reply)
			{
				if( reply.status=="exception" )
				{
					if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
						top.$.messager.alert("警告","会话超时，请重新登录","warning");
					}
					else{
						top.$.messager.alert("警告","发送失败","warning",function(){
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
        setTimeout(function(){sendEmailMessage(obj,second);},1000);
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
function sendMobileMessageAgain(obj,second){
	if(!checkPhone()){
		return;
	}
	var flag = 0;
	$("#inputmobile").attr("disabled",true);
	$("#sendMessage3").removeClass("greenbutton");
	$("#sendMessage3").removeClass("shadow");
	$("#sendMessage3").addClass("graylinebutton");
	$("#sendMessage3").html(second+"秒后可重新获取");
	$("#sendMessage3").unbind("click");
	if(second==58){
		var formData = $.formToBean(change_Mobile_choice_option);
		ajax.remoteCall("bean://agentService:updateBaseInfoPhoneSendMessage",
			[formData],
			function(reply)
			{
				if( reply.status=="exception" )
				{
					if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
						top.$.messager.alert("警告","会话超时，请重新登录","warning");
					}
					else{
						top.$.messager.alert("警告","发送失败","warning",function(){
							top.location.reload();
						});
					} 
				} 
				else if(reply.result.status=="fail"){
					$("#inputmobile").attr("disabled",false);
					$("#sendMessage3").removeClass("graylinebutton");
					$("#sendMessage3").addClass("greenbutton");
					$("#sendMessage3").addClass("shadow");
					$("#sendMessage3").html("获取短信验证码");
					$("#sendMessage3").click(function(){
						sendMobileMessageAgain(this,60);
					});
					$("#tip-mobile").html("<font color=red>"+reply.result.message+"</font>");  
					flag = 1;
				}
				else if( reply.result.status=="success" )
				{
					$("#tip-emailMessage3").html("");
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
        setTimeout(function(){sendMobileMessageAgain(obj,second);},1000);
    // 否则，按钮重置为初始状态
    }else{
    	$("#inputmobile").attr("disabled",false);
    	$("#sendMessage3").removeClass("graylinebutton");
		$("#sendMessage3").addClass("greenbutton");
		$("#sendMessage3").addClass("shadow");
		$("#sendMessage3").html("获短信箱验证码");
		$("#sendMessage3").click(function(){
			sendMobileMessageAgain(this,60);
		});
    } 
}
function logout_1()
{ 
	ajax.remoteCall("bean://sysUserService:logout",
		[],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				top.$.messager.alert('提示','会话超时，请重新登录','info',function(){
					slideright();
				});
			} 
			else if( reply.result.status=="success" )
			{
				inituser();
				// 注销成功
				top.location.href=a+"/agent.do";
				// 跳转页面
				slideright();
			}
			else
			{
				top.$.messager.alert('警告',reply.result.message,'warning');
			}
		}
	);
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
	   		<a href="#" id="business">
				<img id="agent_nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_1_i.png" width="21" height="21" />
				<label>业务信息</label>
			</a>
			<a href="#" id="user_manage">
				<img id="agent_nav_2" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_2_i.png" width="21" height="21" />
				<label>用户管理</label>
			</a>
			<a href="#" id="my_account">
				<img id="agent_nav_3" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_3_i.png" width="21" height="21" />
				<label>我的账户</label>
			</a>
			<a href="#" id="oper_log">
				<img id="agent_nav_4" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_4_i.png" width="21" height="21" />
				<label>操作日志</label>
			</a>
			<a href="#" id="user_manual">
				<img id="agent_nav_5" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_5_i.png" width="21" height="21"/>
				<label>使用手册</label>
			</a>
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
        <div class="r">修改手机</div>
      </div>
      <ul class="steps" style="width:480px;">
        <li class="l">选择验证方式<span>1</span></li>
        <li class="l">验证<span>2</span></li>
        <li class="l">修改手机<span>3</span></li>
        <li class="l">修改完成<span>4</span></li>
      </ul>
      <form class="wizard" id="change_Mobile_choice_option" method="post">
        <ul style="width:4000px;">
          <li>
            <div class="box" style="text-align:center;padding:0 0 15px 0">为了保护您的账户安全，我们需要通过短信验证码或电子邮箱进行验证</div>
            <div id="accountselect"><br />
              <a id="sendMessage1" class="graybutton" href="javascript:void(0);" onclick="nextstep();">
              <div class="labeltitle">通过短信验证码验证</div>
              <div class="labeldesc">如果您的<b><%=agentVO.getPhone()%></b>手机还在正常使用，请选择此方式验证</div>
              </a> 
              <a id="sendMessage2"class="graybutton" href="javascript:void(0);" onclick="nextstep();">
              <div class="labeltitle">通过电子邮箱验证</div>
              <div class="labeldesc">如果您的<b><%=agentVO.getEmail()%></b>邮箱还在正常使用，请选择此方式验证</div>
              </a>
              <div class="clear"></div>
            </div>
          </li>
          <li>
            <div id="codetype1">
              <div class="box" style="text-align:center;padding:0 0 15px 0">我们已将短信验证码发送到您的手机，如未收到可在60秒后重新获取短信验证码</div>
              <div class="box"  style="width:280px;padding:30px;margin:0 auto">
              	<input id="" name="mobilePhone" type="hidden" value="<%=agentVO.getPhone()%>"/>
                <input id="inputmessage" name="myGetPhoneMessage" type="text" onfocus="inputfocus('message');" onblur="inputblur('message'),checkMobileMessage()" class="reginput l" style="width:140px; border-color:#b2b2b2; color:#626262;"/>
                <a id="sendcode" href="javascript:void(0);" class="greenbutton r">获取短信验证码</a>
                <div class="clear">&nbsp;</div>
                <div class="tip" style="color:#999">
                  <label id="messagelabel" for="inputmessage" class="reglabel" style="width:140px; color:#999">短信验证码</label>
               	  <span class="tip" id="tip-mobileMessage"></span>
               </div>
              </div>
            </div>
            <div id="codetype2">
              <div class="box" style="text-align:center;padding:0 0 15px 0">我们已将邮件验证码发送到您的邮箱，如未收到可在60秒后重邮箱新获取验证码</div>
              <div class="box"  style="width:280px;padding:30px;margin:0 auto">
              	<input id="" name="myEmailNumber" type="hidden" value="<%=agentVO.getEmail()%>"/>
                <input id="inputmessage2" name="myGetEmailMessage" type="text" onfocus="inputfocus('message2');" onblur="inputblur('message2'),checkEmailMessage()" class="reginput l" style="width:140px; border-color:#b2b2b2; color:#626262;"/>
                <a id="sendEmailCode" href="javascript:void(0);" class="greenbutton r">获取邮箱验证码</a>
                <div class="clear">&nbsp;</div>
                <div class="tip" style="color:#999">
                  <label id="message2label" for="inputmessage2" class="reglabel" style="width:140px; color:#999">邮箱验证码</label>
                  <span class="tip" id="tip-emailMessage2"></span>
                </div>
              </div>
            </div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);"s onclick="confirmationCode();">验证</a><a class="graybutton r" href="javascript:void(0);" onclick="prevstep();">上一步</a></div>
          </li>
          
          <li>
            <div>
              <div class="box" style="text-align:center;padding:0 0 15px 0">请输入您的新手机号码，并验证您的新手机</div>
              <div class="box"  style="width:280px;padding:30px;margin:0 auto">
                <input id="inputmobile" name="myNewMobile" type="text" onfocus="inputfocus('mobile');" onblur="inputblur('mobile'),checkPhone()" class="reginput" style=" border-color:#b2b2b2; color:#626262;"/>
                <div class="tip" style="color:#999">
                  <label id="mobilelabel" for="inputmobile" class="reglabel" style=" color:#999">输入新手机</label>
                  <span class="tip" id="tip-mobile"></span>
                </div>
                <input id="inputmessage3" name="changeMobileNewCode" type="text" onfocus="inputfocus('message3');" onblur="inputblur('message3'),checkEmailMessage3()" class="reginput l" style="width:140px; border-color:#b2b2b2; color:#626262;"/>
                <a id="sendMessage3" href="javascript:void(0);" class="greenbutton r">获取短信验证码</a>
                <div class="clear">&nbsp;</div>
                <div class="tip" style="color:#999">
                  <label id="message3label" for="inputmessage3" class="reglabel" style="width:140px; color:#999">短信验证码</label>
                  <span class="tip" id="tip-emailMessage3"></span>
                </div>
              </div>
            </div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);"s onclick="confirmationChange();">验证</a></div>
          </li>
          <li>
            <div class="infoicon">修改完成</div>
            <div class="info" style="padding:0 0 30px 0">您的手机号码已变更为新的号码<b><span class="tip" id="tip-newEmail"></span></b></div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);"s onclick="logout_1();">完成</a></div>
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