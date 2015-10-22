<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/metro/easyui.css" />
<div id="mail_config_add_dlg_container">
	<div id="mail_config_add_dlg" class="easyui-dialog" title="新增配置" 
			style="
				width:470px;
				height:250px;
				padding:10px;
			"
			data-options="
				iconCls: 'icon-add',
				buttons: '#mail_config_add_dlg_buttons',
				modal: true,
				onMove:_mail_config_add_dlg_scope_.onMove,
				onClose: function(){
					$(this).dialog('destroy');
				},
				onDestroy: function(){
					delete _mail_config_add_dlg_scope_;
				}
			">
		<form id="mail_config_add_dlg_form" method="post">
		<table border="0" cellpadding="0" cellspacing="0">
		    <tr>
		        <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />配置名：</td>
		        <td class="inputcont"><input class="textbox inputtext" type="text" id="name" name="name" onblur="checkName()"/></td>
		        <td class="inputtip" id="config-tip-name"><i>请输入配置名</i></td>
			</tr>
			<tr>
				<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />发件人：</td>
				<td class="inputcont"><input class="textbox inputtext" type="text" id="sender" name="sender" onblur="checkSender()"/></td>
				<td class="inputtip" id="config-tip-sender"><i>请输入发件人名称(仅英文)</i></td>
		    </tr>
			<tr>
				<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />发件邮箱：</td>
				<td class="inputcont"><input class="textbox inputtext" type="text" id="address" name="address" onblur="checkAddress()"/></td>
				<td class="inputtip" id="config-tip-address"><i>请输入发件人邮箱</i></td>
			</tr>
			<tr>
				<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />邮箱密码：</td>
				<td class="inputcont"><input class="textbox inputtext" type="password" id="password" name="password" onblur="checkPassword()"/></td>
				<td class="inputtip" id="config-tip-password"><i>请输入邮箱密码</i></td>
			</tr>
		</table>
		</form>
	</div>
	
	<div id="mail_config_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="mail_config_add_dlg_save_btn">
			&nbsp;保&nbsp;存&nbsp;
		</a>
		<a href="javascript:" class="easyui-linkbutton" id="mail_config_add_dlg_close_btn">
			&nbsp;关&nbsp;闭&nbsp;
		</a>
	</div>
</div>
	

<script type="text/javascript">

	var path = "<%=request.getContextPath()%>";

function checkName(){
	var name = new String($("#name").val()).trim();
	var flag = true;
	if(name == null || name == ""){
		$("#config-tip-name").html("<b>配置名不能为空</b>");
		return false;
	}
	if(name.length<2 || name.length>16){
		$("#config-tip-name").html("<b>配置名长度为2-16个字符</b>");
		return false;
	}

	ajax.remoteCall("bean://emailConfigService:checkConfigName",
			[ name ],
			function(reply) {
				if( reply.result.status == "fail" ) {
					flag = false;
				}
			}
	);

	if(flag == false) {
		$("#config-tip-name").html("<b>该配置名已存在</b>");
		return false;
	}

	$("#config-tip-name").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}

function checkSender(){
	var sender = new String($("#sender").val()).trim();
	if(sender == null || sender == ""){
		$("#config-tip-sender").html("<b>发件人不能为空</b>");
		return false;
	}
	if(sender.length<2 || sender.length>16){
		$("#config-tip-sender").html("<b>发件人长度为2-16个字符</b>");
		return false;
	}
	$("#config-tip-sender").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}

function checkAddress(){
	var address = new String($("#address").val()).trim();
	var reg = new RegExp("^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$");
	if(address==null || address==""){
		$("#config-tip-address").html("<b>邮箱不能为空");
		return false;
	}
	if(!reg.test(address)){
		$("#config-tip-address").html("<b>请输入正确的邮箱</b>");

		return false;
	}
	$("#config-tip-address").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}

function checkPassword(){
	var password = new String($("#password").val()).trim();
	if(password == null || password == ""){
		$("#config-tip-password").html("<b>密码不能为空</b>");
		return false;
	}
	if(password.length<2 || password.length>16){
		$("#config-tip-password").html("<b>密码长度为2-16个字符</b>");
		return false;
	}
	$("#config-tip-password").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}

//================================
var _mail_config_add_dlg_scope_ = {};		// 作用域
_mail_config_add_dlg_scope_.onMove = function(){
	var thisId = "#mail_config_add_dlg";
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
_mail_config_add_dlg_scope_.save = function()
{
	var formData = $.formToBean(mail_config_add_dlg_form);
	ajax.remoteCall("bean://emailConfigService:addConfig",
		[formData],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				top.$.messager.alert('警告',reply.exceptionMessage,'warning');
			} 
			else if(reply.result.status=="fail"){
				$("#config-tip-name").html("<b>"+reply.result.message+"</b>");
			}
			else if( reply.result.status=="success" )
			{
				var data = $("#mail_config_add_dlg_container").parent().prop("_data_");
				$("#mail_config_add_dlg").dialog("close");
				data.onClose(reply.result);
			}
			else
			{
				top.$.messager.alert('警告',reply.result.message,'warning');
			}
		}
	);
};

// 关闭
_mail_config_add_dlg_scope_.close = function()
{
	$("#mail_config_add_dlg").dialog("close");
};

//--------------------------

// 保存
$("#mail_config_add_dlg_save_btn").click(function(){
	if(checkName() && checkSender() && checkAddress() && checkPassword()){
		_mail_config_add_dlg_scope_.save();
	}
});

// 关闭
$("#mail_config_add_dlg_close_btn").click(function(){
	_mail_config_add_dlg_scope_.close();
});

</script>
