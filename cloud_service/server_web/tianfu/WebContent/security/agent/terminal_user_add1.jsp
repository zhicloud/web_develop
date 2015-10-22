<%@page import="com.zhicloud.op.vo.SysGroupVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
List<SysGroupVO> sysGroupList = (List<SysGroupVO>)request.getAttribute("sysGroupList");
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/metro/easyui.css" />
<div id="terminal_user_add_dlg_container">

	<div id="terminal_user_add_dlg" class="easyui-dialog" title="添加终端用户"
		style="width:530px; height:300px; padding:15px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#terminal_user_add_dlg_buttons',
			modal: true,
			onMove:_terminal_user_add_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _terminal_user_add_dlg_scope_;
			}
		">
		<form id="terminal_user_add_dlg_form" method="post">
		<table border="0" cellpadding="0" cellspacing="0">
		    <tr >
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />邮箱：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text"  style="width:180px;height:24px" id="account" name="account" onblur="checkAccount()"/></td>
		      <td class="inputtip" id="agent-tip-account"><i>请输入常用邮箱</i></td>
		    </tr>
		    <tr style="height:56px;">
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />手机：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text"  style="width:180px;height:24px" id="phone" name="phone" onblur="checkPhone()"/></td>
		      <td class="inputtip" id="agent-tip-phone"><i>请输入11位手机号码</i></td>
		    </tr>
		  </table>
		</form>
	</div>

	<div id="terminal_user_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="terminal_user_add_dlg_save_btn"> &nbsp;保&nbsp;存&nbsp; </a>
		<a href="javascript:" class="easyui-linkbutton" id="terminal_user_add_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
</div>


<script type="text/javascript">
//==================check begin==================
function checkAccount(){
	var account = new String($("#account").val()).trim();
	var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
	if(account==null || account==""){
		$("#agent-tip-account").html("<b>邮箱不能为空</b>");
		return false;
	}
	if(!myreg.test(account)){
		$("#agent-tip-account").html("<b>邮箱格式不正确</b>");
		return false;
	}
	if(account.length>30){
		$("#agent-tip-account").html("<b>输入不能超过30个字符</b>");
		return false;
	}
	$("#agent-tip-account").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}

function checkPassword(){
	var myPassword = new String($("#password").val()).trim();
	if(myPassword==null || myPassword==""){
		$("#agent-tip-password").html("<b>密码不能为空</b>");
		return false;
	}
	if(/^\w{8,20}$/.test(myPassword)){
		$("#agent-tip-password").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
		return true;
	}
	$("#agent-tip-password").html("<b>密码由字母、数字和下划线<br/>组成，长度为8-20个字符</b>");
	return false;
}
function checkPasswordConf(){
	var passwordOne = new String($("#password").val()).trim();
	var passwordTwo = new String($("#confirm").val()).trim();
	if(passwordTwo==null || passwordTwo==""){
		$("#agent-tip-confirm").html("<b>确认密码不能为空</b>");
		return false;
	}
	if(passwordOne!=passwordTwo){
		$("#agent-tip-confirm").html("<b>两次输入的密码不一致</b>");
		return false;
	}
	$("#agent-tip-confirm").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}
function checkEmail(){
	var email = new String($("#email").val());
	if(email==null || email==""){
		$("#agent-tip-email").html("<b>邮箱不能为空</b>");
		return false;
	}
	var reg = new RegExp("^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$");
	if(!reg.test(email)){
		$("#agent-tip-email").html("<b>请输入正确的邮箱</b>");
		return false;
	}
	$("#agent-tip-email").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}
function checkPhone(){
	var phone = new String($("#phone").val());
	if(phone==null || phone==""){
		$("#agent-tip-phone").html("<b>手机号码不能为空</b>");
		return false;
	}
	if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))){ 
		$("#agent-tip-phone").html("<b>请输入正确的手机号码</b>");
		return false;
	}
	$("#agent-tip-phone").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}


//====================check end================
var _terminal_user_add_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#terminal_user_add_dlg";
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
		var result = null;
		var formData = $.formToBean(terminal_user_add_dlg_form);
		ajax.remoteCall("bean://terminalUserService:addTerminalUser", 
			[ formData ],
			function(reply) {
				result = new String(reply.result.message);
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					var data = $("#terminal_user_add_dlg_container").parent().prop("_data_");
					$("#terminal_user_add_dlg").dialog("close");
					data.onClose(reply.result);
				} else {
					if(result.endsWith("账号")){
						$("#agent-tip-phone").html("<b>"+reply.result.message+"</b>");
					}else{
						$("#agent-tip-account").html("<b>"+reply.result.message+"</b>");
					}
// 					top.$.messager.alert('警告',reply.result.message,'warning');
// 					$("#agent-tip-account").html("<b>"+reply.result.message+"</b>");
				}
			}
		);
	};

	// 关闭
	self.close = function() {
		$("#terminal_user_add_dlg").dialog("close");
	};

	//--------------------------
	
	// 初始化
	$(document).ready(function(){
		// 保存
		$("#terminal_user_add_dlg_save_btn").click(function() {
			if(checkAccount() & checkPhone()){
				self.save();
			}
		});
		// 关闭
		$("#terminal_user_add_dlg_close_btn").click(function() {
			self.close();
		});
	});
};
</script>



