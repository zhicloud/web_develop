<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
    LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
    String userId = loginInfo.getUserId();
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/metro/easyui.css" />
<!-- cloud_update_password.jsp -->

<div id="cloud_host_update_password_dlg_container">
	<div id="cloud_host_update_password_dlg" class="easyui-dialog" title="修改监控密码"
		style=" width:440px; height:150px;  padding:10px; "
		data-options="
			iconCls: 'icon-add',
			buttons: '#cloud_host_update_password_dlg_buttons',
			modal: true,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _cloud_host_update_password_dlg_scope_;
			}
		">
		<form id="cloud_host_update_password_dlg_form" method="post">
			
			<input type="hidden" id="host_id" name="host_id" value="<%=request.getAttribute("host_id")%>" />
			<input type="hidden" id="user_id" name="user_id" value="<%=request.getAttribute("user_id")%>" />
		
			<table border="0" style="height: auto; margin: auto;">
				 
				<tr>
			      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />监控密码：</td>
			      <td class="inputcont"><input class="textbox inputtext" type="text" id="password" name="password" onblur="checkPassword()"/></td>
			      <td class="inputtip" id="user-tip-password"><i>请输入监控密码</i></td>
			    </tr>
			</table>
		</form>
	</div>
 
	<div id="cloud_host_update_password_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="cloud_host_update_password_dlg_save_btn">
			&nbsp;保&nbsp;存&nbsp;
		</a>
		<a href="javascript:" class="easyui-linkbutton" id="cloud_host_update_password_dlg_close_btn">
			&nbsp;关&nbsp;闭&nbsp;
		</a>
	</div>
</div>


<script type="text/javascript">
function checkPassword(){
	var myPassword = new String($("#password").val()).trim();
	if(myPassword==null || myPassword==""){
		 $("#user-tip-password").html("<b>密码不能为空</b>");
		 return false;
	}
	if(/^\w{8,20}$/.test(myPassword)){
	    $("#user-tip-password").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
		return true;
	}
	$("#user-tip-password").html("<b>密码由字母、数字和下划线组成，长度为8-20个字符</b>");
	 
	return false;
}

var _cloud_host_update_password_dlg_scope_ = new function(){
	
	var self = this;
	
	 
	
	// 保存
	self.save = function() {
		var formData = $.formToBean(cloud_host_update_password_dlg_form);
 		ajax.remoteCall("bean://cloudHostService:updatePassword", 
			[ formData ],
			function(reply) {
					if (reply.status=="exception")
					{
						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
						{
							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
								window.location.reload();
							});
						}
						else 
						{
							top.$.messager.alert("警告", reply.exceptionMessage, "warning");
						}
					}
					else
					{ 
						top.$.messager.alert("警告", reply.result.message, "info", function(){
							$("#cloud_host_update_password_dlg_close_btn").click();
						});
					}
				}
		);
	};
	
	
	// 关闭
	self.close = function() {
		$("#cloud_host_update_password_dlg").dialog("close");
	};

	//--------------------------
	
	$(document).ready(function() {
			
		  
		// 保存
		$("#cloud_host_update_password_dlg_save_btn").click(function() {
			if(checkPassword()){
				
				self.save();
			}
		});
		// 关闭
		$("#cloud_host_update_password_dlg_close_btn").click(function() {
			self.close();
		});
	});
	
};
	
	
</script>



