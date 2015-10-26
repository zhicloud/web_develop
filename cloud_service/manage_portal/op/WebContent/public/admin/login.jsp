<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_ADMIN;
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		<script  language="JavaScript" type="text/javascript"> 
			 var $buoop = {vs:{i:8,f:20,o:15,s:5.1}}; 
			 $buoop.ol = window.onload; 
			 window.onload=function(){ 
			 try {if ($buoop.ol) $buoop.ol();}catch (e) {} 
			 var e = document.createElement("script"); 
			 e.setAttribute("type", "text/javascript"); 
			 e.setAttribute("src", "//browser-update.org/update.js"); 
			 document.body.appendChild(e); 
		} 
		</script>
		<title>超级管理员 - 登录</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
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
		<form id="big_form" style="width:100%; height:100%; display:table-cell; vertical-align:middle;">
		
			<%-- 表明是超级管理员 --%>
			<input type="hidden" id="type" name="type" value="1" />
			
			<div style="width:600px; height:320px; border:1px solid #dddddd; margin:0 auto 0 auto; position:relative;
				background:#F4F7FF;">
			
				<img src="<%=request.getContextPath()%>/images/logo2.png" style="position:absolute; width:150px; height:120px; top:20px; left:20px;" />
				
				<div style="position:absolute; top:44px; left:190px;">
					<div style="font-size:34px;" class="logo_text">致 云 科 技</div>
					<div style="font-size:28px;" class="logo_text">ZHICLOUD</div>
				</div>
				
				<div style="position:absolute; right:20px; bottom:20px; background:#F4F7FF; padding:10px;">
					<table id="login_table" style="border:0px solid black; border-collapse:collapse; ">
						<tr>
							<td align="right">用户名：</td>
							<td><input type="text" class="input" id="account" name="account" value="" /></td>
						</tr>
						<tr>
							<td align="right">密码：</td>
							<td><input type="password" class="input" id="password" name="password" value="" /></td>
						</tr>
						<tr>
							<td align="right">验证码：</td>
							<td valign="bottom">
								<input type="text" class="input" id="verification_code" name="verification_code" style="width:80px;" maxlength="4" />
								&nbsp;
								<img id="verification_code_img" src="<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>"
									 style="border:1px solid #777777; width:60px; height:20px; margin-bottom:-6px; padding:0px; display:inline;" />
								&nbsp;
								<a id="change_verification_code_link" href="javascript:">换一个</a>
							</td>
						</tr>
						<tr>
							<td></td>
							<td><input type="button" id="login_btn" style="padding:2px;" value="   登 录   " /></td>
						</tr>
					</table>
				</div>
			</div>
		</form>
	</body>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript">
	
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
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} 
				else if( reply.result.status=="success" )
				{
					// 登录成功
					$("<div class=\"datagrid-mask\"></div>").css({
						display:"block",
						width:"100%",
						height:"100%"
					}).appendTo("body"); 
					$("<div class=\"datagrid-mask-msg\"></div>").html("登录成功，正在跳转页面。。。").appendTo("body").css({
						display:"block",
						left:($(document.body).outerWidth(true) - 190) / 2,
						top:($(window).height() - 45) / 2
					});
					// 跳转页面
					window.setTimeout(function(){
						top.location.href = "<%=request.getContextPath()%>/admin.do";
					}, 500);
				}
				else
				{
					top.$.messager.alert('警告',reply.result.message,'warning');
					$("#password").val("");
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
		$("#account, #password, #verification_code").keypress(function(evt){
			if( evt.keyCode==13 )
			{
				login();
			}
		});
		// 登录
		$("#login_btn").click(function(){
			login();
		});
	});
	function reshVerification()
	{ 
		$("#change_verification_code_link").click(); 	 
	}
	var t2 = window.setInterval(reshVerification,1740000);
	</script>
</html>