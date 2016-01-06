<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.vo.TerminalUserVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
request.getSession().setAttribute("thisType","4");
TerminalUserVO terminalUser = (TerminalUserVO)request.getAttribute("terminalUserVO");
String resetPassword = (String)request.getAttribute("resetPassword");
%>
<!-- terminal_user_reset_password.jsp -->
<div id="terminal_user_reset_password_dlg_container">
	<div id="terminal_user_reset_password_dlg" class="easyui-dialog" title="重置密码" 
			style="
				width:280px; 
				height:160px; 
				padding:10px;
			"
			data-options="
				iconCls: 'icon-help',
				buttons: '#terminal_user_reset_password_dlg_buttons',
				modal: true,
				onMove:_terminal_user_reset_password_dlg_scope_.onMove,
				onClose: function(){
					$(this).dialog('destroy');
				},
				onDestroy: function(){
					delete _terminal_user_reset_password_dlg_scope_;
				}
			">
		<form id="terminal_user_reset_password_dlg_form" method="post">
			<input type="hidden" id="terminalUser_id" name="terminalUser_id"
				value="<%=terminalUser.getId()%>" />
			<input type="hidden" id="email"  name="email" 
			    value=<%=terminalUser.getEmail()%> />
			<table border="0" style="margin:20px auto 0 auto;">
				<tr>
					<td>请确认是否重置终端用户：[<%=terminalUser.getEmail()%>]的密码？</td>
					<td><input type="hidden" id="password" name="password" value=<%=resetPassword%> disabled="disabled"/></td>
				</tr>
			</table>
		</form>
	</div>
	
	<div id="terminal_user_reset_password_dlg_buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" id="terminal_user_reset_password_dlg_save_btn">
			&nbsp;是&nbsp;
		</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" id="terminal_user_reset_password_dlg_close_btn">
			&nbsp;否&nbsp;
		</a>
	</div>
</div>
	

<script type="text/javascript">

//=============================
var _terminal_user_reset_password_dlg_scope_ = {};		// 作用域
_terminal_user_reset_password_dlg_scope_.onMove = function(){
	var thisId = "#terminal_user_reset_password_dlg";
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
_terminal_user_reset_password_dlg_scope_.save = function()
{
	var formData = $.formToBean(terminal_user_reset_password_dlg_form);
	ajax.remoteCall("bean://terminalUserService:resetPasswordById",
		[formData],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				top.$.messager.alert('警告',reply.exceptionMessage,'warning');
			} 
			else if( reply.result.status=="success" )
			{
				var data = $("#terminal_user_reset_password_dlg_container").parent().prop("_data_");
				$("#terminal_user_reset_password_dlg").dialog("close");
				data.onClose(reply.result);
				top.$.messager.alert('提示','重置成功,邮件已发送','info');
			}
			else
			{
				top.$.messager.show({
					title:'提示',
					msg:reply.result.message,
					timeout:10000,
					showType:'slide'
				});
			}
		}
	);
};

// 关闭
_terminal_user_reset_password_dlg_scope_.close = function()
{
	$("#terminal_user_reset_password_dlg").dialog("close");
};

//--------------------------

// 保存
$("#terminal_user_reset_password_dlg_save_btn").click(function(){
		_terminal_user_reset_password_dlg_scope_.save();
});

// 关闭
$("#terminal_user_reset_password_dlg_close_btn").click(function(){
	_terminal_user_reset_password_dlg_scope_.close();
});
$(function(){
    $('#password').bind('keypress',function(event){
        if(event.keyCode == "13")    
        {
        	_terminal_user_reset_password_dlg_scope_.save();
        }
    });
}); 

</script>



