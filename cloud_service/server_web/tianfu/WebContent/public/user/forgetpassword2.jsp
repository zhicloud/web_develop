<%@page import="com.zhicloud.op.vo.TerminalUserVO"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
	                Integer userType =AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	TerminalUserVO  myTerminalUserVO =(TerminalUserVO)request.getSession().getAttribute("myterminalUserVO");
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
	initstep(2);
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
function finishReset(){
	top.location.href = "<%=request.getContextPath()%>/user.do"; 
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
window.name = "selfWin";
ajax.async = false;
function confirmationCode(){
	if(!(checkMobileMessage()||checkEmailMessage())){
		return;
	}
	var formData = $.formToBean(user_forget_password);
	ajax.remoteCall("bean://terminalUserService:resetPasswordCheckEmailOrPhoneCode",
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
function countDown(obj,second){
	var flag = 0;
	$("#sendcode").removeClass("greenbutton");
	$("#sendcode").removeClass("shadow");
	$("#sendcode").addClass("graylinebutton");
	$("#sendcode").html(second+"秒后可重新获取");
	$("#sendcode").unbind("click");
    // 如果秒数还是大于0，则表示倒计时还没结束
	if (second==59){
		var formData = $.formToBean(user_forget_password);
		ajax.remoteCall("bean://terminalUserService:resetPasswordSendPhoneCode", 
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
					$("#sendcode").remove("graylinebutton");
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
    	$("#sendcode").remove("graylinebutton");
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
		var formData = $.formToBean(user_forget_password);
		ajax.remoteCall("bean://terminalUserService:resetPasswordSendEmailCode",
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
					$("#sendEmailCode").remove("graylinebutton");
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
    	$("#sendEmailCode").remove("graylinebutton");
		$("#sendEmailCode").addClass("greenbutton");
		$("#sendEmailCode").addClass("shadow");
		$("#sendEmailCode").html("获取邮箱验证码");
		$("#sendEmailCode").click(function(){
			sendEmailMessage(this,60);
		});
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
      	<input name="terminalUser_id" id="terminalUser_id" type="hidden" value="<%=myTerminalUserVO.getId()%>"/>
        <ul style="width:4000px;">
          <li>
            <div class="box" style="text-align:center;padding:0 0 15px 0">请输入您需要找回密码的账户名，即是您注册时输入的邮箱</div>
            <div class="box" style="width:280px;padding:30px;">
              <input id="inputemail" name="" type="text" onfocus="inputfocus('email');" onblur="inputblur('email');" class="reginput" style="border-color:#b2b2b2; color:#626262;"/>
              <div class="tip" style="color:#999">
                <label id="emaillabel" for="inputemail" class="reglabel" style="color:#999">输入邮箱</label>
                出错提示</div>
              <input id="inputcode" name="" type="text" onfocus="inputfocus('code');" onblur="inputblur('code');" class="reginput l" style="width:140px;border-color:#b2b2b2; color:#626262;"/>
              <a href="javascript:void(0);" class="greenbutton l" style="width:80px; margin-left:10px;"><img src="image/code.gif" width="80" height="30" alt="验证码" /></a> <a href="javascript:void(0);" class="greenbutton r" style="width:30px; background: #4cda64 url(image/input_refresh.png) no-repeat center center">&nbsp;</a>
              <div class="clear">&nbsp;</div>
              <div class="tip" style="color:#999">
                <label id="codelabel" for="inputcode" class="reglabel" style="width:140px;color:#999">验证码</label>
                出错提示</div>
            </div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);"s onclick="nextstep();">下一步</a></div>
          </li>
          <li>
            <div class="box" style="text-align:center;padding:0 0 15px 0">为了保护您的账户安全，我们需要通过短信验证码或电子邮箱进行验证</div>
            <div id="accountselect"><br />
              <a id="sendMessage1" class="graybutton" href="javascript:void(0);" onclick="nextstep();">
              <div class="labeltitle">通过短信验证码验证</div>
              <div class="labeldesc">如果您的<b><%=myTerminalUserVO.getPhone()%></b>手机还在正常使用，请选择此方式验证</div>
              </a> 
              <a id="sendMessage2" class="graybutton" href="javascript:void(0);" onclick="nextstep();">
              <div class="labeltitle">通过电子邮箱验证</div>
              <div class="labeldesc">如果您的<b><%=myTerminalUserVO.getEmail()%></b>邮箱还在正常使用，请选择此方式验证</div>
              </a>
              <div class="clear"></div>
            </div>
          </li>
          <li>
            <div id="codetype1">
              <div class="box" style="text-align:center;padding:0 0 15px 0">我们已将短信验证码发送到您的手机，如未收到可在60秒后重新获取短信验证码</div>
              <div class="box" style="width:280px;padding:30px;">
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
              <div class="box" style="width:280px;padding:30px;">
                <input id="inputmessage2" name="myGetEmailMessage" type="text" onfocus="inputfocus('message2');" onblur="inputblur('message2'),checkEmailMessage();" class="reginput l" style="width:140px; border-color:#b2b2b2; color:#626262;"/>
                <a  id="sendEmailCode" href="javascript:void(0);" class="greenbutton r">获取邮箱验证码</a>
                <div class="clear">&nbsp;</div>
                <div class="tip" style="color:#999">
                  <label id="message2label" for="inputmessage2" class="reglabel" style="width:140px; color:#999">邮箱验证码</label>
        			<span class="tip" id="tip-emailMessage2">注意填写验证码</span>
                  </div>
              </div>
            </div>
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);"s onclick="confirmationCode();">验证</a><a class="graybutton r" href="javascript:void(0);" onclick="prevstep();">上一步</a></div>
          </li>
          <li>
            <div id="restpassword1">
              <div class="infoicon">密码已重置</div>
              <div class="info" style="padding:0 0 30px 0">系统已为您生成新密码并发送到您的<span class="tip" id="tip-success"></span>，请注意查收</b></div>
              <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);"s onclick="finishReset();">完成</a></div>
            </div>
<!--             <div id="restpassword2"> -->
<!--               <div class="infoicon">密码已重置</div> -->
<!--               <div class="info" style="padding:0 0 30px 0">系统已为您生成新密码并短信发送到您的手机，请查看短信获取新密码</b></div> -->
<!--               <div class="buttonbar" style="border-top:solid 1px #b2b2b2"><a class="bluebutton r" href="javascript:void(0);"s onclick="finishReset();">完成</a></div> -->
<!--             </div> -->
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
