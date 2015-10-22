<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.vo.TerminalUserVO"%>
<%@page import="com.zhicloud.op.vo.SysRoleVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	TerminalUserVO terminalUser = (TerminalUserVO)request.getAttribute("terminalUser");
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/metro/easyui.css" />
<div id="terminal_account_mod_dlg_container">
	<div id="terminal_account_mod_dlg" class="easyui-dialog" title="编辑用户名" 
			style="
				width:380px; 
				height:180px; 
				padding:15px;
			"
			data-options="
				iconCls: 'icon-edit',
				buttons: '#terminal_account_mod_dlg_buttons',
				modal: true,
				onMove:_terminal_account_mod_dlg_scope_.onMove, 
				onClose: function(){
					$(this).dialog('destroy');
				},
				onDestroy: function(){
					delete _terminal_account_mod_dlg_scope_;
				}
			">
		<form id="terminal_account_mod_dlg_form" method="post">
			
			<input type="hidden" id="terminalUser_id" name="terminalUser_id" value="<%=terminalUser.getId()%>" />
			<table border="0" cellpadding="0" cellspacing="0">
			    <tr>
			      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />用户名：</td>
			      <td class="inputcont"><input class="textbox inputtext" type="text" id="terminal_account" name="terminal_account" onblur="checkAccount()" value="<%=terminalUser.getAccount()%>"/></td>
			    </tr>
			    <tr>
			      <td></td>
			      <td class="inputtip" id="tip-account"><i>请输入新用户名</i></td>
			    </tr>
			</table>
		</form>
	</div>
	
	<div id="terminal_account_mod_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="terminal_account_mod_dlg_save_btn">
			&nbsp;保&nbsp;存&nbsp;
		</a>
		<a href="javascript:" class="easyui-linkbutton" id="terminal_account_mod_dlg_close_btn">
			&nbsp;关&nbsp;闭&nbsp;
		</a>
	</div>
</div>

<script type="text/javascript">

function checkAccount(){
	var Account = new String($("#terminal_account").val()).trim();
	if(Account == null || Account == ""){
		$("#tip-account").html("<b>用户名不能为空</b>");
		return false;
	}
	if(Account.length<3 || Account.length>16){
		$("#tip-account").html("<b>用户名长度为3-16个字符</b>");
		return false;
	}
<%-- 	$("#tip-account").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>"); --%>
	return true;
}


//=============================
var _terminal_account_mod_dlg_scope_ = new function(){	// 作用域
var self = this;
self.onMove = function(){
	var thisId = "#terminal_account_mod_dlg";
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
self.save = function()
{
	var formData = $.formToBean(terminal_account_mod_dlg_form);
	ajax.remoteCall("bean://terminalUserService:updateBaseInfoAccount",
		[formData],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				top.$.messager.alert('警告',reply.exceptionMessage,'warning');
			} 
			else if(reply.status=="fail")
			{
				top.$.messager.alert('提示',reply.result.message,'info');
			}
			else if( reply.result.status=="success" )
			{
				$("#terminal_account_mod_dlg").dialog("close");
				top.$.messager.alert('提示','保存成功，请重新登录','info',function(){
					window.parent.logout(); 
				});
			}
			else
			{
				$("#tip-account").html("<b>该用户名已经存在</b>");
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


//--------------------------
// 关闭
self.close = function()
{
	$("#terminal_account_mod_dlg").dialog("close");
};


// 保存
$("#terminal_account_mod_dlg_save_btn").click(function(){
	if(checkAccount()){
		self.save();
	}
});

// 关闭
$("#terminal_account_mod_dlg_close_btn").click(function(){
	self.close();
});
};
</script>



