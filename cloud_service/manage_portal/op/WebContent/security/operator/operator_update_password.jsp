﻿<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.OperatorVO" %>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType    = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	
	OperatorVO operator = (OperatorVO)request.getAttribute("operator");
%>
<!DOCTYPE html>
<!-- operator_update_password.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>密码修改</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
</head>

<body>
<div class="oper-wrap">
	<form id="operator_update_password" method="post">
		<div class="panel-header">
			<div class="panel-title">修改密码</div>
			<div class="panel-tool"></div>
		</div>
		<input type="hidden" id="operator_id" name="operator_id" value="<%=operator.getId()%>" />
		<div style="background:#fff;width:100%;padding:10px 0;">
			<table style="margin-left:10px;">
				<tr>
					<td style="vertical-align:middle; text-align:right;">原密码：</td>
					<td style="padding:5px 0 5px 0">
						<input type="password" id="old_password"  name="old_password" class="messager-input" style="width:180px;" value="" onblur="checkOldPassword()"/>
					</td>
					<td class="inputtip" id="operator-tip-oldPassword"></td>
				</tr>
				<tr>
					<td style="vertical-align:middle; text-align:right;">新密码：</td>
					<td style="padding:5px 0 5px 0">
						<input type="password" id="password" name="password" value="" class="messager-input" style="width:180px;" onblur="checkPassword()"/>
					</td>
					<td><span id="operator-tip-password"></span></td>
				</tr>
				<tr>
					<td style="vertical-align:middle; text-align:right;">密码确认：</td>
					<td style="padding:5px 0 5px 0">
						<input type="password" id="confirm" name="confirm" value="" class="messager-input" style="width:180px;" onblur="checkPasswordConf()"/>
					</td>
					<td><span id="operator-tip-confirm"></span></td>
				</tr>
				<tr>
					<td></td>
					<td align="right" style="padding:5px 0 5px 0">
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-save'" id="save_btn">提交修改 </a>
					</td>
				</tr>
			</table>
		</div>
	</form>
</div>
</body>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript">
	//=========================
	function checkOldPassword(){
		var oldPassword = new String($("#old_password").val()).trim();
		if(oldPassword==null || oldPassword==""){
			$("#operator-tip-oldPassword").html("<font color='red'>原密码不能为空</font>");
			return false;
		}
		else
		{	
			$("#operator-tip-oldPassword").html("");
			return true;
		}

	}
	function checkPassword(){
		var myPassword = new String($("#password").val()).trim();
		var oldPassword = new String($("#old_password").val()).trim();
		if(myPassword==null || myPassword=="")
		{
			$("#operator-tip-password").html("<font color='red'>密码不能为空</font>");
			return false;
		}
		if(myPassword==oldPassword)
		{
			$("#operator-tip-password").html("<font color='red'>新密码和原密码一样</font>");
			return false;
		}
// 		if(/^\w{8,20}$/.test(myPassword))
// 		{
// 			$("#operator-tip-password").html("");
// 			return true;
// 		}
		if(myPassword.length>=6&&myPassword.length<=20)
		{
			$("#operator-tip-password").html("");
			return true;
		}
		$("#operator-tip-password").html("<font color='red'>密码长度为6-20个字符</font>");
			return false;
	}
	function checkPasswordConf(){
		var passwordOne = new String($("#password").val()).trim();
		var passwordTwo = new String($("#confirm").val()).trim();
		if(passwordTwo==null || passwordTwo==""){
			$("#operator-tip-confirm").html("<font color='red'>确认密码不能为空</font>");
			return false;			
		}
		if(passwordOne!=passwordTwo){
			$("#operator-tip-confirm").html("<font color='red'>两次输入的密码不一致</font>");
			return false;
		}
		$("#operator-tip-confirm").html("");
		return true;
	}
	function IsModifiedTwo()
   	{
   	    var result = false;      
   	    var colInput = document.getElementsByTagName("input");  
   	    for (var i=0; i<colInput.length; i++)            
   	    {
   	        if (colInput[i].value != colInput[i].defaultValue) 
   	        {
   	            return true;      
   	        }
   	    }
   	    return result;
   	}
	
	function onBeforeExit(target){
   		if(IsModifiedTwo()==true){
	   		top.$.messager.confirm("提示","信息未保存，是否离开？",function(r){
	   			if(r){
	   				target.ownerDocument.defaultView.onSwitch(target);
	   			}
	   		});
   		}else{
   			target.ownerDocument.defaultView.onSwitch(target);
   		}
   		return false;
   	}
	//=========================
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;

	var _operator_update_password_scope_ = new function(){
		
		var self = this;
		
		// 保存
		self.save = function() {
			var formData = $.formToBean(operator_update_password);
			ajax.remoteCall("bean://operatorService:updatePasswordById",
				[ formData ], 
				function(reply) {
					if (reply.status == "exception") {
						top.$.messager.alert('警告',reply.exceptionMessage,'warning');
					} else if (reply.result.status == "success") {
						top.$.messager.alert('信息', '修改成功，请重新登录!', 'info', function(){
							window.parent.logout();
						});
					}else {
						top.$.messager.alert('警告',reply.result.message,'warning');
					}
				}
			);
		};

		//--------------------------
		$(document).ready(function(){
		    $("#save_btn").click(function() {
		    	if(checkPassword() & checkPasswordConf()){
		    		top.$.messager.alert("提示","确定提交？","info",function(){
	    				self.save();
	    			});
		    	}
			});
		}) ;
	};
	</script>
</html>