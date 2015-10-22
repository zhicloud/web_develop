<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.UserOrderVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	UserOrderVO detail = (UserOrderVO)request.getAttribute("detail");
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
<div id="sys_role_add_dlg_container">
	<div id="sys_role_add_dlg" class="easyui-dialog" title="云主机管理" 
			style="
				width:470px; 
				height:200px; 
				padding:10px;
			"
			data-options="
				iconCls: 'icon-add',
				buttons: '#sys_role_add_dlg_buttons',
				modal: true,
				onMove:_sys_role_add_dlg_scope_.onMove,
				onClose: function(){
					$(this).dialog('destroy');
				},
				onDestroy: function(){
					delete _sys_role_add_dlg_scope_;
				}
			">
		<form id="sys_role_add_dlg_form" method="post">
			<table border="0" style="margin:20px auto 0 auto;">
			<tr>
			      <td class="inputtitle">云主机ID:</td>
			      <td class="inputcont">
			      <%
					   if(detail.getProcessStatus()==1){
						   
					%>
					
					 <%=detail.getHostId()%> 
					<%-- <input type="text" id="realId" name="realId" value="<%=detail.getHostId() %>" diasbled="disabled"/> --%>
					<%
					   }else{
						   
					%>
					<input type="text" id="realId" name="realId" onblur="checkID();" />
					<input type="hidden" name="id" value="<%=request.getParameter("id") %>" /></td>
			        <td class="inputtip" id="operator-tip-realId"><img src="<%=request.getContextPath()%>/images/button_required_red_16_16.gif" width="16" height="16" alt="必填" /></td>
					<%
					   }
					%>
			      </td>
			    </tr>
				 
				<!-- <tr>
					<td><input id="pay_unionpay" name="isHostCreated" type="radio" value="2" />已创建</td>
					<td><input id="pay_unionpay" name="isHostCreated" type="radio" value="1" />未创建</td> 
				</tr> -->
			</table>
		</form>
	</div>
	<%
	   if(detail.getProcessStatus()!=1){
		   
	%>
	<div id="sys_role_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="sys_role_add_dlg_save_btn">
			&nbsp;保&nbsp;存&nbsp;
		</a>
		<a href="javascript:" class="easyui-linkbutton" id="sys_role_add_dlg_close_btn">
			&nbsp;关&nbsp;闭&nbsp;
		</a>
	</div>
	<%
	   }
	
	%>
</div>
	
</body>
<script type="text/javascript">
function checkID(){
	var realId = new String($("#realId").val()).trim();
	if(realId.length<1 || realId.length>32){
		$("#operator-tip-realId").html("<img src='<%=request.getContextPath()%>/images/button_caution_red_16_16.gif' width='16' height='16' alt='注意' /><b>ID长度必须为1-32个字符</b>");
		return false;  
	}
	$("#operator-tip-realId").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}

var _sys_role_add_dlg_scope_ = {};		// 作用域
_sys_role_add_dlg_scope_.onMove = function(){
	var thisId = "#sys_role_add_dlg";
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
_sys_role_add_dlg_scope_.save = function()
{
	var formData = $.formToBean(sys_role_add_dlg_form);
	ajax.remoteCall("bean://paymentService:addRealHost",
		[formData],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				top.$.messager.alert('警告',reply.exceptionMessage,'warning');
			} 
			else if( reply.result.status=="success" )
			{
				var data = $("#sys_role_add_dlg_container").parent().prop("_data_");
				$("#sys_role_add_dlg").dialog("close");
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
_sys_role_add_dlg_scope_.close = function()
{
	$("#sys_role_add_dlg").dialog("close");
};

//--------------------------

// 保存
$("#sys_role_add_dlg_save_btn").click(function(){
	if(checkID()){
		
	_sys_role_add_dlg_scope_.save();
	}
});

// 关闭
$("#sys_role_add_dlg_close_btn").click(function(){
	_sys_role_add_dlg_scope_.close();
});

</script>
</html>



