<%@page import="com.zhicloud.op.vo.TerminalUserVO"%>
<%@page import="com.zhicloud.op.vo.SysRoleVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	TerminalUserVO terminalUser = (TerminalUserVO)request.getAttribute("terminalUser");
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/metro/easyui.css" />
<div id="terminal_email_mod_dlg_container">
	<div id="terminal_email_mod_dlg" class="easyui-dialog" title="编辑邮箱" 
			style="
				width:530px; 
				height:380px; 
				padding:15px;
			"
			data-options="
				iconCls: 'icon-edit',
				buttons: '#terminal_email_mod_dlg_buttons',
				modal: true,
				onMove:_terminal_email_mod_dlg_scope_.onMove,
				onClose: function(){
					$(this).dialog('destroy');
				},
				onDestroy: function(){
					delete _terminal_email_mod_dlg_scope_;
				}
			">
		<form id="terminal_email_mod_dlg_form" method="post">
			
			<input type="hidden" id="terminalUser_id" name="terminalUser_id" value="<%=terminalUser.getId()%>" />
			<table border="0" cellpadding="0" cellspacing="0">
			    	<%if(!(terminalUser.getEmail()==null||terminalUser.getEmail().isEmpty())){%>
			    <tr>
			     	 <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />原邮箱：</td>
			     	 <td class="inputcont"><input class="textbox inputtext" type="text" style="width:180px;height:23px" id="terminal_email_old" name="terminal_email_old" onblur="checkOldEmail()"  /></td>
			     	 
			     	 
			    </tr>
			    <tr>
			    	<td></td>
			    	<td class="inputtip" id="tip-email_old"><i>请输入原邮箱</i></td>
			    </tr>
			     	 <%}%>
			    <tr> 
			     	 <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />常用邮箱：</td>
			     	 <td class="textfield"><input  type="text"  style="width:180px;height:23px" id="terminal_email" name="terminal_email" onblur="checkEmail()"/></td>
			     	  
			    </tr>
			    <tr>
			    	<td></td>
			    	<td class="inputtip" id="tip-email"><i>请输入常用邮箱</i></td>
			    </tr>
			    <tr></tr>
			    <tr style="height:50px;">
			    	<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />邮箱验证码：</td>
			     	<td class="inputcont"><input  type="text"  style="width:180px;height:23px" id="emailMessage" name="emailMessage" onblur="checkMessage()"/></td>
			    	<td><a href="javascript:void(0);" class="easyui-linkbutton" id="get-code-link">获取邮箱验证码</a></td>
			    </tr>
			    <tr>
			    	<td></td>
			    	<td class="inputtip" id="tip-message"><i>请输入邮箱验证码</i></td>
			    </tr>
			</table>
		</form>
	</div>
	
	<div id="terminal_email_mod_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="terminal_email_mod_dlg_save_btn">
			&nbsp;保&nbsp;存&nbsp;
		</a>
		<a href="javascript:" class="easyui-linkbutton" id="terminal_email_mod_dlg_close_btn">
			&nbsp;关&nbsp;闭&nbsp;
		</a>
	</div>
</div>

<script type="text/javascript">
function checkOldEmail(){
	var email = new String($("#terminal_email_old").val()).trim();
	var reg = new RegExp("^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$");
	if(email==null||email==""){
		$("#tip-email_old").html("<b>请注意填写邮箱<b>");
		return false;
	}
	if(!reg.test(email)){
		$("#tip-email_old").html("<b>邮箱格式不正确，请注意检查<b>");
		return false;
	} 
	else {
			var flag = false;
	var formData = $.formToBean(terminal_email_mod_dlg_form);
	ajax.remoteCall("bean://terminalUserService:updateBaseInfoPhoneCheckOldMail", 
		[ formData ],
		function(reply) {
			if (reply.status == "exception") 
			{
				alert(reply.exceptionMessage);
			}else if(reply.result.status=="fail")
			{
				$("#tip-email_old").html("<b>原邮箱不正确<b>");
			}else
			{
				$("#tip-email_old").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>原邮箱正确");
				flag = true;
			}
		}
	);
	return flag;
   }
}
function checkEmail(){
	var email = new String($("#terminal_email").val()).trim();
	var reg = new RegExp("^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$");
	if(email==null||email==""){
		$("#tip-email").html("<b>请注意填写邮箱<b>");
		return false;
	}
	if(!reg.test(email)){
		$("#tip-email").html("<b>邮箱格式不正确，请注意检查<b>");
		return false;
	}else{
		$("#tip-email").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>邮箱格式正确");
		return true;
	}
}

function checkMessage(){
	var messageCode = new String($("#emailMessage").val()).trim();
	if(messageCode==null||messageCode==""){
		$("#tip-message").html("<b>邮箱验证码不能为空</b>");
		return false;
	}else{
		$("#tip-message").html("");
		return true;
	}
	
}



//=============================
var _terminal_email_mod_dlg_scope_ = new function(){	// 作用域
var self = this;
self.onMove = function(){
	var thisId = "#terminal_email_mod_dlg";
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
//发送
self.send = function()
{
	var formData = $.formToBean(terminal_email_mod_dlg_form);
	ajax.remoteCall("bean://terminalUserService:updateBaseInfoEmailSendCode",
		[formData],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				top.$.messager.alert('警告',reply.exceptionMessage,'warning');
			} 
			else if( reply.result.status=="success" )
			{
				top.$.messager.alert('提示','邮件已发送，请注意查收','info');
// 				$("#terminal_email_mod_dlg").dialog("close");
// 				top.$.messager.alert('提示','邮件已发送，请注意查收','info',function(){
// 					window.parent.logout(); 
// 				});
// 				var data = $("#terminal_email_mod_dlg_container").parent().prop("_data_");
// 				data.onClose(reply.result);
			}
			else
			{ 
				top.$.messager.alert('提示',reply.result.message,'info'); 
			}
		}
	);
};

//保存
self.save = function()
{
	var formData = $.formToBean(terminal_email_mod_dlg_form);
	ajax.remoteCall("bean://terminalUserService:updateBaseInfoEmail",
		[formData],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				top.$.messager.alert('警告',reply.exceptionMessage,'warning');
			} 
			else if( reply.result.status=="success" )
			{
				$("#terminal_email_mod_dlg").dialog("close");
				top.$.messager.alert('提示','修改成功','info',function(){
					window.parent.location.reload();
				});
				
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

	//发送邮箱验证码
	$("#get-code-link").click(function(){
			self.send();
	});


	// 关闭
	self.close = function()
	{
		$("#terminal_email_mod_dlg").dialog("close");
	};

//--------------------------

	// 保存
	$("#terminal_email_mod_dlg_save_btn").click(function(){
		if(checkOldEmail()&&checkEmail()&&checkMessage()){
			self.save();
		}
	});

	// 关闭
	$("#terminal_email_mod_dlg_close_btn").click(function(){
		self.close();
	});


};
</script>



