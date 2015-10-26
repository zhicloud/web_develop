<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<% 
	String URL = (String)request.getAttribute("url");
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>忘记用户名 - 云端在线</title>
		<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
        <link rel="stylesheet" type="text/css" href="css/style.css" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css"> 
		<style type="text/css">
		body input.input {
			width: 200px;
			height: 20px;
		}
		div.logo_text {
			color:#3E8433;
			text-align:center;
		}
		#login_table tr td {
			padding: 3px;
		}
		</style>
	</head>

	<body>
		  <div class="pagebox">
		    <div class="header">
		      <div class="headerbox">
		        <div class="headerlogo l"><a href="<%=request.getContextPath() %>/"><img src="<%=request.getContextPath()%>/images/logo_green_light_145_29.gif" width="145" height="29" alt="云端在线" /></a></div>
		        <div class="headerregister r"><a href="<%=request.getContextPath()%>/public/user/register.jsp">注册</a></div>
		        <div class="headerlogin r"><a href="<%=request.getContextPath()%>/user.do">登录</a></div>
		      </div>
		    </div>
		    <div class="nav">
		      <div class="navbox">
		        <div class="navbutton l"><a href="<%=request.getContextPath()%>">首页</a></div>
		        <div class="navsplitter l">&nbsp;</div>
		        <div class="navbutton l"><a href="<%=request.getContextPath()%>/user/buy.do">定制主机</a></div>
		        <div class="navsplitter l">&nbsp;</div>
		        <div class="navbutton l"><a href="<%=request.getContextPath()%>/public/downloadPage.do">相关下载</a></div>
		      </div>
		    </div>
		    <div class="main">
		      <div class="container l">
		        <div class="titlebar">
		          <div class="tab tabactive">忘记用户名</div>
		        </div>
		        <div class="content">
		          <form id="big_form" style="width:100%; height:100%; display:table-cell; vertical-align:middle;">
		          <input type="hidden" id="type" name="type" value="4" />
		            <span>身份证</span>
            		<input class="textfield"  id="idCard" name="idCard"  type="text" onblur="checkIdCard()"/>
            		<span class="tip" id="tip-idcard"></span>
		            <span>姓名</span>
            		<input class="textfield" id="name" name="name"   type="text" onblur="checkName()"/>
            		<span class="tip" id="tip-name"></span>
		           	<span>Email</span>
            		<input class="textfield" id="email" name="email" type="text" onblur="checkEmail()"/> 
            		<span class="tip" id="tip-email"></span> 
		            <span>验证码</span>
		            <input class="shortfield" id="verification_code" name="verification_code" type="text" />
		            <img id="verification_code_img" src="<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>" width="180" height="30" alt="验证码" class="code" /><span class="tip"><a id="change_verification_code_link" href="#"><img src="<%=request.getContextPath()%>/images/button_refresh_orange_24_24.gif" width="24" height="24" alt="换一个" />换一个</a></span>
		            <input type="button" id="login_btn" class="inputbutton" value="发送"/>
		            <div class="inputsplitter r">&nbsp;</div>
		            <div class="inputoption r"><a href="<%=request.getContextPath()%>/public/user/forget_password.jsp">忘记密码？</a></div>
		          </form>
		          <div class="clear">&nbsp;</div>
		        </div>
		      </div>
		      <div class="sidebar r">
		        <div class="sidebarimagelogin">&nbsp;</div>
		        <div class="sidebarcenter">还没有云端在线账户？<br />
		          <a href="<%=request.getContextPath()%>/register.do">立即注册<img class="pointer" src="<%=request.getContextPath()%>/images/pointer_blue_5_10.gif" width="5" height="9" alt="现在注册" /></a></div>
		      </div>
		      <div class="clear">&nbsp;</div>
		    </div>
		  <div class="pagefooter">
		    <div class="footer">
		      <div class="footerbox">
		        <div class="footercopyright">Copyright &copy; 2014 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1</div>
		      </div>
		    </div>
		  </div>
		</div>
	</body>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript">
	
	window.name = "selfWin";
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	
	function login()
	{
		var formData = $.formToBean(big_form);
		ajax.remoteCall("bean://terminalUserService:sendMailForForgetAccount",
			[formData],
			function(reply)
			{
				if( reply.status=="exception" )
				{
					top.$.messager.alert("提示", reply.exceptionMessage, "warning");
				} 
				else if( reply.result.status=="success" )
				{
					// 登录成功
					$("<div class=\"datagrid-mask\"></div>").css({
						display:"block",
						width:"100%",
						height:"100%"
					}).appendTo("body"); 
					$("<div class=\"datagrid-mask-msg\"></div>").html("发送邮箱成功，正在跳转页面。。。").appendTo("body").css({
						display:"block",
						left:($(document.body).outerWidth(true) - 190) / 2,
						top:($(window).height() - 45) / 2
					});
					// 跳转页面
					window.setTimeout(function(){
						if("<%=URL%>"!="null"){
							top.location.href ="<%=URL%>";							
						}else{
							top.location.href = "<%=request.getContextPath()%>/";							
						}
					}, 500);
				}
			else
			{
				alert(reply.result.message);
				$("#change_verification_code_link").click();
				$("#verification_code").val("");
			}
			}
		);
	}
	
	$(function(){
		// 换一个验证码
		$("#change_verification_code_link").click(function(){
			$("#verification_code_img").attr("src", "<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>&ts="+Math.random());
		});
		// 回车
		$("#name,#idCard,#email,#verification_code").keypress(function(evt){
			if( evt.keyCode==13 ){
				login();
			}
		});
		// 登录
		$("#login_btn").click(function(){
			login()
		});
		// 注册
		$("#register_btn").click(function(){ 
 			window.location.href = "<%=request.getContextPath()%>/public/user/register.jsp"; 
			 
		});
	});
	</script>
</html>
