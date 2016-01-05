<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.vo.CashCouponVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
CashCouponVO cashCouponVO = (CashCouponVO)request.getAttribute("cashCouponVO");
%>
<!-- operator_cash_coupon_send_email.jsp -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/metro/easyui.css" />
<div id="send_invite_code_email_dlg_container">
	<div id="send_invite_code_email_dlg" class="easyui-dialog" title="发送现金券" 
			style="
				width:380px; 
				height:220px; 
				padding:15px;
			"
			data-options="
				iconCls: 'icon-help',
				buttons: '#send_invite_code_email_dlg_buttons',
				modal: true,
				onMove:_send_invite_code_email_dlg_scope_.onMove,
				onClose: function(){
					$(this).dialog('destroy');
				},
				onDestroy: function(){
					delete _send_invite_code_email_dlg_scope_;
				}
			">
		<form id="send_invite_code_email_dlg_form" method="post">
			<input type="hidden" id="cashCouponId" name="cashCouponId"
				value="<%=cashCouponVO.getId()%>" />
			<input type="hidden" id="cashCouponMoney"  name="cashCouponMoney" 
			    value=<%=cashCouponVO.getMoney()%> />
			<input type="hidden" id="cashCouponCode"  name="cashCouponCode" 
			    value=<%=cashCouponVO.getCashCode()%> />
			<table border="0" style="margin:20px auto 0 auto;">
				<tr> 
			     	 <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />电子邮箱：</td>
			     	 <td class="textfield"><input  type="text"  style="width:180px;height:23px" id="terminal_email" name="terminal_email" onblur="checkEmail()"/></td>
			     	  
			    </tr>
			    <tr>
			    	<td></td>
			    	<td class="inputtip" id="tip-email"><i>请输入常用邮箱</i></td>
			    </tr>
			</table>
		</form>
	</div>
	
	<div id="send_invite_code_email_dlg_buttons">
		<a href="javascript:void(0)" class="easyui-linkbutton" id="send_invite_code_email_dlg_save_btn">
			&nbsp;发&nbsp;&nbsp;送&nbsp;
		</a>
		<a href="javascript:void(0)" class="easyui-linkbutton" id="send_invite_code_email_dlg_close_btn">
			&nbsp;关&nbsp;&nbsp;闭&nbsp;
		</a>
	</div>
</div>
	

<script type="text/javascript">

function checkEmail(){
	var email = new String($("#terminal_email").val()).trim();
	var reg = new RegExp("^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$");
	if(email==null||email==""){
		$("#tip-email").html("<b>请注意填写邮箱</b>");
		return false;
	}
	if(!reg.test(email)){
		$("#tip-email").html("<b>邮箱格式不正确，请注意检查</b>");
		return false;
	}else{
		$("#tip-email").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>邮箱格式正确");
		return true;
	}
}


//=============================
var _send_invite_code_email_dlg_scope_ = {};		// 作用域
_send_invite_code_email_dlg_scope_.onMove = function(){
	var thisId = "#send_invite_code_email_dlg";
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
_send_invite_code_email_dlg_scope_.save = function()
{
	var formData = $.formToBean(send_invite_code_email_dlg_form);
	ajax.remoteCall("bean://cashCouponService:sendCashCouponByEmail",
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
				var data = $("#send_invite_code_email_dlg_container").parent().prop("_data_");
				$("#send_invite_code_email_dlg").dialog("close");
				data.onClose(reply.result);
				top.$.messager.alert('提示','现金券已成功发送','info');
			}
			else
			{
				$("#tip-email").html("<b>"+reply.result.message+"</b>");
// 				top.$.messager.show({
// 					title:'提示',
// 					msg:reply.result.message,
// 					timeout:10000,
// 					showType:'slide'
// 				});
			}
		}
	);
};

// 关闭
_send_invite_code_email_dlg_scope_.close = function()
{
	$("#send_invite_code_email_dlg").dialog("close");
};

//--------------------------

// 保存
$("#send_invite_code_email_dlg_save_btn").click(function(){
	if(checkEmail()){
		_send_invite_code_email_dlg_scope_.save();
	}
});

// 关闭
$("#send_invite_code_email_dlg_close_btn").click(function(){
	_send_invite_code_email_dlg_scope_.close();
});
$(function(){
    $('#terminal_email').bind('keypress',function(event){
        if(event.keyCode == "13")    
        {
        	_send_invite_code_email_dlg_scope_.save();
        }
    });
}); 

</script>



