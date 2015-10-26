﻿<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.TerminalUserVO" %>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	TerminalUserVO terminalUser = (TerminalUserVO)request.getAttribute("terminalUser");
%>
<!DOCTYPE html>
<!-- terminal_user_change_password.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		<title>密码修改</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		/************/
		.panel-header {
			border-top: 0px;
			border-bottom: 1px solid #dddddd;
		}
		.panel-header, 
		.panel-body {
			border-left: 0px;
			border-right: 0px;
		}
		.panel-body {
			border-bottom: 0px;
		}
		</style>
	</head>
	<body>
	<div class="iframe">
		<form id="terminal_user_change_password" method="post">
		
			<div class="panel-header">
				<div class="panel-title">修改密码</div>
				<div class="panel-tool"></div>
			</div>
			
			<input type="hidden" id="terminalUser_id" name="terminalUser_id" value="<%=terminalUser.getId()%>" />
			
			<table style="margin: 10px 10px 10px 20px;">
				<tr>
					<td style="vertical-align:middle; text-align:right;">原密码：</td>
					<td style="padding:5px 0 5px 0">
						<input type="password" id="old_password" onpaste='return false' style="width:180px;height:23px" name="old_password" value="" onblur="checkOldPassword()"/>
					</td>
					<td class="inputtip" id="tip-oldPassword"></td>
				</tr>
				<tr>
					<td style="vertical-align:middle; text-align:right;">新密码：</td>
					<td style="padding:5px 0 5px 0">
						<input type="password" id="password" onpaste='return false' style="width:180px;height:23px" name="password" value="" onblur="checkPassword()"/>
					</td>
					<td><span id="tip-newPassword"></span></td>
				</tr>
				<tr>
					<td style="vertical-align:middle; text-align:right;">密码确认：</td>
					<td style="padding:5px 0 5px 0">
						<input type="password" id="confirm" onpaste='return false' style="width:180px;height:23px" name="confirm" value="" onblur="checkPasswordConf()"/>
					</td>
					<td><span id="tip-confirm"></span></td>
				</tr>
				<tr>
					<td></td>
					<td align="right" style="padding:5px 0 5px 0">
						<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-save"  id="save_btn">提交修改</a>
					</td>
				</tr>
			</table>
			
		</form>
		</div>
	</body>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript">
	
	function checkOldPassword(){
		var oldPassword = new String($("#old_password").val()).trim();
		if(oldPassword==null || oldPassword==""){
			$("#tip-oldPassword").html("<font color='red'>原密码不能为空</font>");
			return false;
		}
		else
		{	
			$("#tip-oldPassword").html("");
			return true;
		}

	}
	
	function checkPassword(){
		var myPassword = new String($("#password").val()).trim();
		var oldPassword = new String($("#old_password").val()).trim();
		if(myPassword==null || myPassword==""){
			$("#tip-newPassword").html("<font color='red'>密码不能为空</font>");
			return false;
		}
		if(myPassword==oldPassword)
		{
			$("#tip-newPassword").html("<font color='red'>新密码和原密码一样</font>");
			return false;
		}
		if(/^\w{8,20}$/.test(myPassword)){
			$("#tip-newPassword").html("");
			return true;
		}
		else
		{
			$("#tip-newPassword").html("<font color='red'>密码由字母、数字和下划线组成,长度在8-20之间</font>");
			return false;
		}
	}
	function checkPasswordConf(){
		var passwordOne = new String($("#password").val()).trim();
		var passwordTwo = new String($("#confirm").val()).trim();
		if(passwordTwo==null || passwordTwo=="")
		{
			$("#tip-confirm").html("<font color='red'>确认密码不能为空</font>");
			return false;
		}
		if(passwordOne!=passwordTwo)
		{
			$("#tip-confirm").html("<font color='red'>两次输入的密码不一致</font>");
			return false;
		}
		else{
			$("#tip-confirm").html("");
			return true;
		}
	}
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;

	var _terminal_user_change_password_scope_ = new function(){
		
		var self = this;
		
		// 保存
		self.save = function() { 
			var formData = $.formToBean(terminal_user_change_password);
			ajax.remoteCall("bean://terminalUserService:changePasswordById",
				[ formData ], 
				function(reply) {
					if (reply.status == "exception") {
						top.$.messager.alert('警告',reply.exceptionMessage,'warning');
					} else if (reply.result.status == "success") {
							window.parent.logout();
					}else {
						top.$.messager.alert('提示',reply.result.message,'info');
// 						top.$.messager.show({
// 							title:'提示',
// 							msg:reply.result.message,
// 							timeout:10000,
// 							showType:'slide'
// 						});
					}
				}
			);
		};

		//--------------------------
		$(document).ready(function(){
		    $("#save_btn").click(function() {
		    	if(checkOldPassword()&checkPassword() & checkPasswordConf()){
		    		top.$.messager.alert("提示","确定提交？","info",function(){
	    				self.save();
	    			});
		    	}
			});
		}) ;
	};
	</script>
</html>