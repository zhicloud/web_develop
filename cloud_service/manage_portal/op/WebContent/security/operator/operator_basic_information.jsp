<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.OperatorVO" %>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_OPERATOR;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	OperatorVO operator = (OperatorVO)request.getAttribute("operator");
%>
<!DOCTYPE html>
<!-- operator_basic_information.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - 基本信息</title>
		
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
		<form id="operator_basic_information" method="post">
		
			<div class="panel-header">
				<div class="panel-title">基本信息</div>
				<div class="panel-tool"></div>
			</div>
			
			<input type="hidden" id="operator_id" name="operator_id" value="<%=operator.getId()%>" />
			<input type="hidden" id="group_id"    name="group_id"    value="<%=operator.getGroupId()%>" />
			
			<table style="margin: 10px 10px 10px 20px;">
<!-- 				<tr> -->
<!-- 					<td style="vertical-align:middle; text-align:right;">用户名：</td> -->
<!-- 					<td style="padding:5px;"> -->
<!-- 						<input type="text" id="account" name="account" style="width:300px;"  -->
<%-- 							value="<%=StringUtil.trim(operator.getAccount())%>" disabled="disabled" readonly="readonly"  /> --%>
<!-- 					</td> -->
<!-- 					<td><span id="operator-tip-account"><i>用户名不能修改</i></span></td> -->
<!-- 				</tr> -->
				<%-- <tr>
					<td style="vertical-align:middle; text-align:right;">姓名：</td>
					<td style="padding:5px;">
						<input type="text" id="name" name="name" style="width:300px;" 
							value="<%=StringUtil.trim(operator.getName())%>" disabled="disabled" onblur="checkName()"/>
					</td>
					<td><span id="operator-tip-name"></span></td>
				</tr> --%>
				<tr>
					<td style="vertical-align:middle; text-align:right;">邮箱：</td>
					<td style="padding:5px;">
						<input type="text" id="e_mail" name="e_mail" style="width:300px;" 
							value="<%=StringUtil.trim(operator.getAccount())%>" disabled="disabled" onblur="checkEmail()"/>
					</td>
					<td><span id="operator-tip-email"></span></td>
				</tr>
				<tr>
					<td style="vertical-align:middle; text-align:right;">手机：</td>
					<td style="padding:5px;">
						<input type="text" id="phone" name="phone" style="width:300px;" 
							value="<%=StringUtil.trim(operator.getPhone())%>" disabled="disabled" onblur="checkPhone()"/>
					</td>
					<td><span id="operator-tip-phone"></span></td>
				</tr>
				<tr>
					<td></td>
					<td align="right" style="padding:5px;">
						<a href="#" class="easyui-linkbutton" iconCls="icon-edit" id="edit-link"> 编 辑 </a>
						&nbsp;
						<a href="#" class="easyui-linkbutton" iconCls="icon-save" id="save_btn" disabled="disabled"> 保 存 </a>
					</td>
				</tr>
			</table>
			
		</form>
   	</body>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
   	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
   	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
   	<script type="text/javascript">
   	//=========================
   		
   	function checkAccount(){
   		var account = new String($("#account").val()).trim();
   		if(account==null || account==""){
   			$("#operator-tip-account").html("<font color='red'>帐号不能为空</font>");
   			return false;
   		}
   		if(account.length<2 || account.length>16){
   			$("#operator-tip-account").html("<font color='red'>帐号长度必须为2-16个字符</font>");
   			return false;
   		}
   		$("#operator-tip-account").html("");
   		return true;
   	}
   	
   	function checkName(){
   		var myName = new String($("#name").val()).trim();
   		if(myName==null || myName==""){
   			$("#operator-tip-name").html("<font color='red'>用户名不能为空</font>");
   			return false;
   		}
   		if(myName.length<2 || myName.length>16){
   			$("#operator-tip-name").html("<font color='red'>用户名长度必须为2-16个字符</font>");
   			return false;
   		}
   		$("#operator-tip-name").html("");
   		return true;
   	}
   	
   	function checkEmail(){
   		var email = new String($("#e_mail").val());
   		var reg = new RegExp("^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$");
   		if(email==null || email==""){
   			$("#operator-tip-email").html("<font color='red'>邮箱不能为空</font>");
   			return false;
   		}
   		if(!reg.test(email)){
   			$("#operator-tip-email").html("<font color='red'>请输入正确的邮箱</font>");
   			return false;
   		}
   		$("#operator-tip-email").html("");
   		return true;
   	}
   	
   	function checkPhone(){
   		var phone = new String($("#phone").val());
   		if(phone==null || phone==""){
   			$("#operator-tip-phone").html("<font color='red'>手机号码不能为空</font>");
   			return false;
   		}
   		if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))){ 
   			$("#operator-tip-phone").html("<font color='red'>请输入正确的手机号码</font>");
   			return false;
   		}
   		$("#operator-tip-phone").html("");
   		return true;
   	}
   	
   	function IsModified()
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
   	 	top.$.messager.alert('提示','信息未作改动','info');
   	    return result;
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
   	//============================= 
   	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	
	var _operator_basic_information_scope_ = new function(){
		
		var self = this;
		
		// 保存
		self.save = function() { 
			
			if(IsModified()){
				var formData = $.formToBean(operator_basic_information);
				ajax.remoteCall("bean://operatorService:updateBasicInformation",
					[ formData ], 
					function(reply) {
						if (reply.status == "exception") {
							top.$.messager.alert('警告', reply.exceptionMessage, 'warning');
						} else if (reply.result.status == "success") {
							top.$.messager.alert('信息', '保存成功，请重新登录!', 'info', function(){
								window.parent.logout();
							});
						}else {
// 							top.$.messager.show({
// 								title:'提示',
// 								msg:reply.result.message,
// 								timeout:10000,
// 								showType:'slide'
// 							});
							$("#operator-tip-email").html("<font color=red>"+reply.result.message+"</font>");
						}
					}
				);
				
			}
			else
			{
				$('#edit-link').click();
			}
		};

		//--------------------------
		$(document).ready(function(){
			var account = $("#account").val();
			var name    = $("#name").val();
			var email   = $("#e_mail").val();
			var phone   = $("#phone").val();
			// 编辑
		    $('#edit-link').click(function () {  
		    	var options = $(this).linkbutton("options");
		    	if( options.iconCls=="icon-edit" )	// 编辑
	    		{
		    		$("#account").attr("disabled", false);
					$("#name").attr("disabled", false);
					$("#e_mail").attr("disabled", false);
					$("#phone").attr("disabled", false);
					
					$("#save_btn").linkbutton("enable");
					
					$(this).linkbutton({
						iconCls : "icon-undo",
						text: " 取 消 "
					});
	    		}
		    	else
	    		{
		    		$("#account").val(account);
		    		$("#name").val(name);
		    		$("#e_mail").val(email);
		    		$("#phone").val(phone);
		    		$("#operator-tip-account").html("");
		    		$("#operator-tip-name").html("");
		    		$("#operator-tip-email").html("");
		    		$("#operator-tip-phone").html("");
		    		$("#account").attr("disabled", true);
					$("#name").attr("disabled", true);
					$("#e_mail").attr("disabled", true);
					$("#phone").attr("disabled", true);
					
					$("#save_btn").linkbutton("disable");
					
					$(this).linkbutton({
						iconCls : "icon-edit",
						text: " 编 辑 "
					});
	    		}
			});
			// 保存
		    $("#save_btn").linkbutton({
		    	onClick: function(){
		    		if( checkEmail()& checkPhone()& IsModified()){
		    			top.$.messager.alert("提示","确定保存？","info",function(){
		    				self.save();
		    			});
		    		}
		    	}
		    });
		}) ;
	};
	</script>
</html>