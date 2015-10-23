<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.vo.InviteCodeVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
request.getSession().setAttribute("thisType","4");
InviteCodeVO inviteCode = (InviteCodeVO)request.getAttribute("inviteCode");
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/metro/easyui.css" />
<div id="send_invite_code_phone_dlg_container">
	<div id="send_invite_code_phone_dlg" class="easyui-dialog" title="发送邀请码" 
			style="
				width:380px; 
				height:220px; 
				padding:15px;
			"
			data-options="
				iconCls: 'icon-help', 
				buttons: '#send_invite_code_phone_dlg_buttons',
				modal: true,
				onMove:_send_invite_code_phone_dlg_scope_.onMove,
				onClose: function(){
					$(this).dialog('destroy');
				},
				onDestroy: function(){
					delete _send_invite_code_phone_dlg_scope_;
				}
			">
		<form id="send_invite_code_phone_dlg_form" method="post">
			<input type="hidden" id="inviteCode_id" name="inviteCode_id"
				value="<%=inviteCode.getId()%>" />
			<input type="hidden" id="inviteCode"  name="inviteCode" 
			    value=<%=inviteCode.getCode()%> />
			<table border="0" style="margin:20px auto 0 auto;">
				<tr> 
			     	 <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />手机号码：</td>
			     	 <td class="textfield"><input  type="text"  style="width:180px;height:23px" id="terminal_phone" name="terminal_phone" onblur="checkPhone()"/></td>
			    </tr>
			    <tr>
			    	<td></td>
			    	<td class="inputtip" id="tip-phone"><i>请输入手机号码</i></td>
			    </tr>
			</table>
		</form>
	</div>
	
	<div id="send_invite_code_phone_dlg_buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" id="send_invite_code_phone_dlg_save_btn">
			&nbsp;发&nbsp;&nbsp;送&nbsp;
		</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" id="send_invite_code_phone_dlg_close_btn">
			&nbsp;关&nbsp;&nbsp;闭&nbsp;
		</a>
	</div>
</div>
	

<script type="text/javascript">

function checkPhone(){
	var phone = new String($("#terminal_phone").val());
	if(phone==null||phone==""){
		$("#tip-phone").html("<b>请注意填写手机号码</b>");
		return false;
	}
	if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))){ 
		$("#tip-phone").html("<b>请输入正确的手机号码</b>");
		return false;
	}else{
		$("#tip-phone").html("");
		return true;
	}
}


//=============================
var _send_invite_code_phone_dlg_scope_ = {};		// 作用域
_send_invite_code_phone_dlg_scope_.onMove = function(){
	var thisId = "#send_invite_code_phone_dlg";
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
_send_invite_code_phone_dlg_scope_.save = function()
{
	var formData = $.formToBean(send_invite_code_phone_dlg_form);
	ajax.remoteCall("bean://inviteCodeService:sendInviteCodeByPhone",
		[formData],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
					top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
						top.location.reload();
					});
				}
				else{
					top.$.messager.alert("警告",reply.exceptionMessage,"warning",function(){
						top.location.reload();
					});
				} 
			} 
			else if( reply.result.status=="success" )
			{
				var data = $("#send_invite_code_phone_dlg_container").parent().prop("_data_");
				$("#send_invite_code_phone_dlg").dialog("close");
				data.onClose(reply.result);
				top.$.messager.alert('提示','邀请码已成功发送','info')
			}
			else
			{
				$("#tip-phone").html("<b>"+reply.result.message+"</b>");
// 				top.$.messager.alert("提示",reply.result.message,"warning");
			}
		}
	);
};

// 关闭
_send_invite_code_phone_dlg_scope_.close = function()
{
	$("#send_invite_code_phone_dlg").dialog("close");
};

//--------------------------

// 保存
$("#send_invite_code_phone_dlg_save_btn").click(function(){
	if(checkPhone()){
		_send_invite_code_phone_dlg_scope_.save();
	}
});

// 关闭
$("#send_invite_code_phone_dlg_close_btn").click(function(){
	_send_invite_code_phone_dlg_scope_.close();
});
$(function(){
    $('#terminal_phone').bind('keypress',function(event){
        if(event.keyCode == "13")    
        {
        	_send_invite_code_phone_dlg_scope_.save();
        }
    });
}); 

</script>