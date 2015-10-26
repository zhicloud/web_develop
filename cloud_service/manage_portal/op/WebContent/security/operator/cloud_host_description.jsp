<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
    CloudHostVO host = (CloudHostVO) request.getAttribute("host");
 %>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/metro/easyui.css" />
<div id="invite_code_add_dlg_container">
	<div id="invite_code_add_dlg" class="easyui-dialog" title="备注-[<%=host.getHostName() %>]" 
			style="
				width:450px; 
				height:300px; 
				padding:15px;
			"
			data-options="
				iconCls: 'icon-add',
				buttons: '#invite_code_add_dlg_buttons',
				modal: true,
				onMove:_invite_code_add_dlg_scope_.onMove,
				onClose: function(){
					$(this).dialog('destroy');
				},
				onDestroy: function(){
					delete _invite_code_add_dlg_scope_;
				}
			">
		<form id="agent_recharge" method="post">
		    <input type="hidden" name="id" value="<%=host.getId()%>"/>
			<table border="0" cellpadding="0" cellspacing="0">  
			    <tr>
					<td class="inputtitle" width="160px;">备注：</td>
					<td class="inputcont"> 
  					    <textarea class="textbox" style="width:180px;height:100px" id="description" name="description" onblur="checkDescription()" ><%=host.getDescription()==null?"":host.getDescription() %></textarea>
				     </td>
				      <td class="inputtip" id="operator-tip-diy-reason"></td>
 				</tr> 
			</table>
		</form>
	</div>
	
	<div id="invite_code_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="invite_code_add_dlg_save_btn">
			&nbsp;保&nbsp;存&nbsp;
		</a>
		<a href="javascript:" class="easyui-linkbutton" id="invite_code_add_dlg_close_btn">
			&nbsp;关&nbsp;闭&nbsp;
		</a>
	</div>
</div>
	

<script type="text/javascript">  
function checkDescription(){
	var description = new String($("#description").val()).trim(); 
	if(description.length>100){
		$("#operator-tip-diy-reason").html("<b>备注长度应小于100个字符</b>");
		return false;
	} 
	return true;
} 
//=============================
var _invite_code_add_dlg_scope_ = {};		// 作用域
_invite_code_add_dlg_scope_.onMove = function(){
	var thisId = "#invite_code_add_dlg";
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
_invite_code_add_dlg_scope_.save = function()
{
	var formData = $.formToBean(agent_recharge);
	ajax.remoteCall("bean://cloudHostService:modifyDescription",
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
				var data = $("#invite_code_add_dlg_container").parent().prop("_data_");
				$("#invite_code_add_dlg").dialog("close");
				data.onClose(reply.result);
				top.$.messager.alert('提示','备注成功','info') 
			}
			else
			{
				$("#tip-cash").html("<b>"+reply.result.message+"</b>");
			}
		}
	);
};

// 关闭
_invite_code_add_dlg_scope_.close = function()
{
	$("#invite_code_add_dlg").dialog("close");
};

//--------------------------

// 保存
$("#invite_code_add_dlg_save_btn").click(function(){
	if(checkDescription()){ 
		_invite_code_add_dlg_scope_.save();
	}
});

// 关闭
$("#invite_code_add_dlg_close_btn").click(function(){
	_invite_code_add_dlg_scope_.close();
});
$(function(){
    $("#code_number,cash_value").bind('keypress',function(event){
        if(event.keyCode == "13")    
        {
        	_invite_code_add_dlg_scope_.save();
        }
    });
}); 

</script>



