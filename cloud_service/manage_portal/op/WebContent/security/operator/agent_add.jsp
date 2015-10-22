﻿﻿<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.vo.SysGroupVO"%>
<%@page import="com.zhicloud.op.vo.MarkVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%

List<SysGroupVO> sysGroupList = (List<SysGroupVO>)request.getAttribute("sysGroupList");
List<MarkVO> markList = (List<MarkVO>)request.getAttribute("markList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>我的云端 - 云端在线</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/metro/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css" /> 

</head>
<body>
<div id="agent_add_dlg_container">
	<div id="agent_add_dlg" class="easyui-dialog" title="添加代理商"
		style="width:500px; height:370px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#agent_add_dlg_buttons',
			modal: true,
			onMove:_agent_add_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _agent_add_dlg_scope_;
			}
		">
		<form id="agent_add_dlg_form" method="post">
  <table border="0" cellpadding="0" cellspacing="0">
  	<tr style="height:56px;">
		      <td class="inputtitle">标签：</td>
		      <td class="inputcont"><select class="inputselect"  id="mark_id" name="markId" onblur="checkMarkId()">
		          <option value="">请选择</option>
		          <%
						for( MarkVO mark : markList ) 
						{ 
				  %> 
		          <option value="<%=mark.getId()%>"><%=mark.getName()%></option>
		          <%
						} 
				  %> 
		        </select></td>
		        <td class="inputtip" id="operator-tip-mark"><i>请选择一个标签</i></td>
		    </tr>
    <tr>
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />权限组：</td>
      <td class="inputcont"><select class="inputselect"  id="group_id" name="group_id" onblur="checkGroupId()">
          <option value="">请选择</option>
          <%
								for( SysGroupVO sysGroup : sysGroupList )
								{
							%>
          <option value="<%=StringUtil.trim(sysGroup.getId())%>"><%=StringUtil.trim(sysGroup.getGroupName())%></option>
          <%
								}
							%>
        </select></td>
        <td class="inputtip" id="operator-tip-group"><i>请选择一个群组</i></td>
    </tr>
<!--     <tr> -->
<%--       <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />用户名：</td> --%>
<!--       <td class="inputcont"><input class="textbox inputtext" type="text" id="account" name="account" onblur="checkAccount()"/></td> -->
<!--       <td class="inputtip" id="operator-tip-account"><i>请输入2-16个字符的用户名</i></td> -->
<!--     </tr> -->
    <%-- <tr>
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />姓名：</td>
      <td class="inputcont"><input class="textbox inputtext" type="text" id="name" name="name" onblur="checkName()"/></td>
      <td class="inputtip" id="operator-tip-name"> <i>请输入2-16个字符的姓名</i></td>
    </tr> --%>
   <%--  <tr>
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />密码：</td>
      <td class="inputcont"><input class="textbox inputtext" type="password" id="password" name="password" onblur="checkPassword()"/></td>
      <td class="inputtip" id="operator-tip-password"><i>密码由字母、数字和下划线组成，长度为8-20个字符</i></td>
    </tr>
    <tr>
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />密码确认：</td>
      <td class="inputcont"><input class="textbox inputtext" type="password" id="confirm" name="confirm" onblur="checkPasswordConf()"/></td>
      <td class="inputtip" id="operator-tip-confirm"><i>请再次输入密码</i></td>
    </tr> --%>
	<tr>
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />昵称：</td>
      <td class="inputcont"><input class="textbox inputtext" type="text" id="name" name="name" onblur="checkName()"/></td>
      <td class="inputtip" id="operator-tip-name"> <i>请输入2-16个字符的昵称</i></td>
    </tr>    
    <tr>
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />邮箱：</td>
      <td class="inputcont"><input class="textbox inputtext" type="text" id="email" name="email" onblur="checkEmail()"/></td>
      <td class="inputtip" id="operator-tip-email"><i>请输入有效邮箱</i></td>
    </tr>
    <tr>
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />手机：</td>
      <td class="inputcont"><input class="textbox inputtext" type="text" id="phone" name="phone" onblur="checkPhone()"/></td>
      <td class="inputtip" id="operator-tip-phone"><i>请输入11位手机号码</i></td>
    </tr>
    <tr>
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />折扣：</td>
      <td class="inputcont"><input class="textbox inputtext" type="text" id="percentOff" name="percentOff" onblur="checkPercentOff()"/></td>
      <td class="inputtip" id="operator-tip-off"><i>请输入0-99的折扣</i></td>
    </tr>
  </table>
</form>
	</div>

	<div id="agent_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton"
			id="agent_add_dlg_save_btn"> &nbsp;保&nbsp;存&nbsp; </a> <a
			href="javascript:" class="easyui-linkbutton"
			id="agent_add_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
</div>

</body>
<script type="text/javascript">
//==================check begin==================
function checkMarkId(){ 
	$("#operator-tip-mark").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}
function checkGroupId(){ 
	var groupId = new String($("#group_id").val()).trim();;
	if(groupId == null || groupId == ""){
		$("#operator-tip-group").html("<b>请选择一个群组</b>");
		
		return false;
	}
	$("#operator-tip-group").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}
function checkAccount(){
	var account = new String($("#account").val()).trim(); 
	if(account.length<2 || account.length>16){ 
		$("#operator-tip-account").html("<b>用户名长度必须为2-16个字符</b>");
		return false;
	}
	$("#operator-tip-account").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}
function checkName(){
	var myName = new String($("#name").val()).trim();
	if(myName.length<2 || myName.length>16){
		$("#operator-tip-name").html("<b>昵称长度必须为2-16个字符</b>");
		return false;  
	}
	$("#operator-tip-name").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}
function checkPassword(){
	var myPassword = new String($("#password").val()).trim();
	if(myPassword==null || myPassword==""){
		 $("#operator-tip-password").html("<b>密码不能为空</b>");
		 return false;
	}
	if(/^\w{8,20}$/.test(myPassword)){
	    $("#operator-tip-password").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
		return true;
	}
	$("#operator-tip-password").html("<b>密码由字母、数字和下划线组成，长度为8-20个字符</b>");
	 
	return false;
}
function checkPasswordConf(){
	var passwordOne = new String($("#password").val()).trim();
	var passwordTwo = new String($("#confirm").val()).trim();
	if(passwordTwo==null||passwordTwo==""){
	    $("#operator-tip-confirm").html("<b>确认密码不能为空</b>");		
		return false;
	}
	if(passwordOne!=passwordTwo){
	    $("#operator-tip-confirm").html("<b>两次密码不一致</b>");		
		return false;
	}
    $("#operator-tip-confirm").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}
function checkEmail(){
	var email = new String($("#email").val());
	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if(email==null || email==""){
		$("#operator-tip-email").html("<b>邮箱不能为空</b>");
		return false;
	}
	if(!myreg.test(email)){
	    $("#operator-tip-email").html("<b>请输入正确的邮箱</b>");		
		return false;
	}
	if(email.length>30){
		$("#operator-tip-email").html("<b>输入不能超过30个字符</b>");
		return false;
	}
    $("#operator-tip-email").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");		
	return true;
}
function checkPhone(){
	var phone = new String($("#phone").val());
	if(phone==null || phone==""){
	    $("#operator-tip-phone").html("<b>手机号码不能为空</b>");		
	    return false;
	}
	if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))){ 
	    $("#operator-tip-phone").html("<b>请输入正确的手机号码</b>");		
		return false;
	}
    $("#operator-tip-phone").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");		
	return true;
}
function checkPercentOff(){
	var off = new String($("#percentOff").val());
	if(off==null || off==""){
	    $("#operator-tip-off").html("<b>折扣不能为空</b>");		
	    return false;
	}
	if(!(/^[0-9][0-9]?$/.test(off))){ 
	    $("#operator-tip-off").html("<b>请输入0-99的整数</b>");		
		return false;
	}
	if(off<0||off>99){ 
	    $("#operator-tip-off").html("<b>请输入0-99的折扣</b>");		
		return false;
	}
    $("#operator-tip-off").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");		
	return true;
}

//====================check end================
var _agent_add_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#agent_add_dlg";
		var topValue = $(thisId).offset().top;
		var leftValue = $(thisId).offset().left;
		if(topValue==0){
			topValue = 30;
		}
		if(topValue<30){
			$(thisId).dialog('move',{
				left:leftValue,
				top:30
			});
			return;
		}
		if(leftValue>1315){
			$(thisId).dialog('move',{
				left:1300,
				top:topValue
			});
			return;
		}
		if(topValue>600){
			$(thisId).dialog('move',{
				left:leftValue,
				top:570
			});
			return;
		}
	};
	// 保存
	self.save = function() {
		if(checkMarkId()&&checkGroupId()&&checkAccount()&&checkName()&&checkPhone()&&checkPercentOff()){
		var formData = $.formToBean(agent_add_dlg_form);
		ajax.remoteCall("bean://agentService:addAgent", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
					{
						top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
							window.location.reload();
						});
					}else{
						
						top.$.messager.alert('警告',reply.exceptionMessage,'warning');
					}
				} else if (reply.result.status == "success") {
					var data = $("#agent_add_dlg_container").parent().prop("_data_");
					$("#agent_add_dlg").dialog("close");
					data.onClose(reply.result);
				} else {
					if(reply.result.message.indexOf("email")>-1){
						$("#operator-tip-email").html("<b>"+reply.result.message.split("-")[1]+"</b>");
					}
					if(reply.result.message.indexOf("name")>-1){
						$("#operator-tip-name").html("<b>"+reply.result.message.split("-")[1]+"</b>");
					}
					if(reply.result.message.indexOf("phone")>-1){
						$("#operator-tip-phone").html("<b>"+reply.result.message.split("-")[1]+"</b>");
					}	
				}
			}
		);
		}
	};
	
	// 关闭
	self.close = function() {
		$("#agent_add_dlg").dialog("close");
	};
	
	//--------------------------
	
	// 初始化
	$(document).ready(function(){
		// 保存
		$("#agent_add_dlg_save_btn").click(function() {
			if(checkGroupId() & checkEmail() &checkPhone()&checkPercentOff()){
				self.save();
			}
		});
		// 关闭
		$("#agent_add_dlg_close_btn").click(function() {
			self.close();
		});
	});
};

</script>
</html>



