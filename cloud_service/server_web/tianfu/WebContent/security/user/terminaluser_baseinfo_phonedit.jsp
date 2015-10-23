<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.vo.TerminalUserVO"%>
<%@page import="com.zhicloud.op.vo.SysRoleVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	TerminalUserVO terminalUser = (TerminalUserVO)request.getAttribute("terminalUser");
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/metro/easyui.css" />
<div id="terminal_phone_mod_dlg_container">
	<div id="terminal_phone_mod_dlg" class="easyui-dialog" title="编辑手机号码" 
			style="
				width:520px; 
				height:380px; 
				padding:15px;
			"
			data-options="
				iconCls: 'icon-edit',
				buttons: '#terminal_phone_mod_dlg_buttons',
				modal: true,
				onMove:_terminal_phone_mod_dlg_scope_.onMove,
				onClose: function(){
					$(this).dialog('destroy');
				},
				onDestroy: function(){
					delete _terminal_phone_mod_dlg_scope_;
				}
			">
		<form id="terminal_phone_mod_dlg_form" method="post">
			
			<input type="hidden" id="terminalUser_id" name="terminalUser_id" value="<%=terminalUser.getId()%>" />
			<table border="0" cellpadding="0" cellspacing="0">
				<tr>
					<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />原手机号码：</td>
					<td class="inputcont"><input class="textbox inputtext" type="text" style="width:180px;height:23px" id="oldPhone" name="oldPhone" onblur="checkOldPhone()"/></td>
				</tr>
				<tr style="height:50px;">
					<td></td>
					<td class="inputtip" id="tip-oldphone"><i>请输入原手机号码</i></td>
				</tr>
			    <tr >
			     	 <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />新手机号码：</td>
			     	 <td class="inputcont"><input class="textbox inputtext" type="text" style="width:180px;height:23px" id="newPhone" name="newPhone" onblur="checkNewPhone()"/></td>
			     	 <td class="sendcode"><a onclick="countDown(this,60);" class="easyui-linkbutton" style="cursor:pointer">获取短信验证码</a></td>
			    </tr>
			    <tr style="height:50px;">
			    	<td></td>
			    	 <td class="inputtip" id="tip-newphone"><i>请输入需要绑定的手机号码</i></td>
			    </tr>
			    <tr>
			    	<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />手机验证码：</td>
			     	<td class="inputcont"><input  class="textbox inputtext" type="text" style="width:180px;height:23px" id="messageCode" name="messageCode" onblur="checkMessage()"/></td>
			    </tr>
			    <tr style="height:50px;">
			    	<td></td>
			    	<td class="inputtip" id="tip-messageCode"><i>请输入短信验证码</i></td>
			    </tr>
			</table>
		</form>
	</div>
	
	<div id="terminal_phone_mod_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="terminal_phone_mod_dlg_save_btn">
			&nbsp;保&nbsp;存&nbsp;
		</a>
		<a href="javascript:" class="easyui-linkbutton" id="terminal_phone_mod_dlg_close_btn">
			&nbsp;关&nbsp;闭&nbsp;
		</a>
	</div>
</div>

<script type="text/javascript">

function checkMessage(){
	var messageCode = new String($("#messageCode").val()).trim();
	if(messageCode==null||messageCode==""){
		$("#tip-messageCode").html("<b>短信验证码不能为空</b>");
		return false;
	}
	if(messageCode.length>6){
		$("#tip-messageCode").html("<b>短信验证码有误</b>");
		return false;
	}
	else
	{
		$("#tip-messageCode").html("");
		return true;
	}
	
}

function checkNewPhone() {
	var newPhone = new String($("#newPhone").val()).trim();
	var oldPhone = new String($("#oldPhone").val()).trim();
	if(newPhone==null||newPhone==""){
		$("#tip-newphone").html("<b>新手机号码不能为空<b>");
		return false;
	}
	if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(newPhone))){
		$("#tip-newphone").html("<b>手机号码有误<b>");
		return false;
	}
	if(newPhone==oldPhone)
	{
		$("#tip-newphone").html("<b>手机号码未做更改<b>");
		return false;
	}
	else{
		$("#tip-newphone").html("");
		return true;
	}
}



function countDown(obj,second){
	if(!checkOldPhone()){
		return;
	}
	var flag = 0;
	$(".sendcode").html(second+"秒后可重新获取");
    // 如果秒数还是大于0，则表示倒计时还没结束
	if (second==60){
		var formData = $.formToBean(terminal_phone_mod_dlg_form);
		ajax.remoteCall("bean://terminalUserService:updateBaseInfoPhoneSendMessage", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
// 					alert(reply.exceptionMessage);
					top.$.messager.alert("提示", reply.exceptionMessage, "warning");
				}
				else if(reply.result.status=="fail"){
					$(".sendcode").html('<a onclick="countDown(this,60);" style="cursor:pointer">获取短信验证码</a>');
					$("#tip-messageCode").html("<b>手机号码有误<b>");
// 					alert(reply.result.message);
					top.$.messager.alert("提示", reply.result.message, "warning");
					flag = 1;
				}
				else if(reply.result.status == "success") { 
					$("#tip-newphone").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>验证码已成功发送");
				}
				else {
					$("#tip-messageCode").html("请输入正确短信验证码！");  
				}
			}
		);
	}
    if(flag == 1){
    	return;
    }
    if(second>0){
        // 时间减一
        second--;
        // 一秒后重复执行
        setTimeout(function(){countDown(obj,second);},1000);
    // 否则，按钮重置为初始状态
    }else{
    	$(".sendcode").html('<a onclick="countDown(this,60);" style="cursor:pointer">获取短信验证码</a>');
    } 
}


//=============================

window.name = "selfWin";
var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;

  function checkOldPhone() {
	var oldPhone = new String($("#oldPhone").val()).trim();
	if(oldPhone==null||oldPhone==""){
		$("#tip-oldphone").html("<b>原手机号码不能为空<b>");
		return false;
	}
	if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(oldPhone))){
		$("#tip-oldphone").html("<b>手机号码有误<b>");
			return false;
		}
	else
		{
			var flag = false;
	var formData = $.formToBean(terminal_phone_mod_dlg_form);
	ajax.remoteCall("bean://terminalUserService:updateBaseInfoPhoneCheckOldPhone", 
		[ formData ],
		function(reply) {
			if (reply.status == "exception") 
			{
				alert(reply.exceptionMessage);
			}else if(reply.result.status=="fail")
			{
				$("#tip-oldphone").html("<b>原手机号码不正确<b>");
			}else
			{
				$("#tip-oldphone").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>原手机号码正确");
				flag = true;
			}
		}
	);
	return flag;
   }
}	
	
//=========================================
var _terminal_phone_mod_dlg_scope_ = new function(){	// 作用域
var self = this;
self.onMove = function(){
	var thisId = "#terminal_phone_mod_dlg";
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
//保存
self.save = function()
{
	var formData = $.formToBean(terminal_phone_mod_dlg_form);
	ajax.remoteCall("bean://terminalUserService:updateBaseInfoPhone",
		[formData],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				top.$.messager.alert('警告',reply.exceptionMessage,'warning');
			} 
			else if( reply.result.status=="success" )
			{
				$("#terminal_phone_mod_dlg").dialog("close");
				top.$.messager.alert('提示','修改成功','info',function(){
					window.parent.location.reload();
				});
				
			}
			else
			{
				top.$.messager.alert("提示",reply.result.message,"warning");
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
	self.close = function()
	{
		$("#terminal_phone_mod_dlg").dialog("close");
	};

	// 保存
	$("#terminal_phone_mod_dlg_save_btn").click(function(){
		if(checkOldPhone()&&checkNewPhone()&&checkMessage()){
			self.save();
		}
	});

	// 关闭
	$("#terminal_phone_mod_dlg_close_btn").click(function(){
		self.close();
	});


};
</script>



