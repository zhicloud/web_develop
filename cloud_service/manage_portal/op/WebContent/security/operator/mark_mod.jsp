<%@page import="com.zhicloud.op.vo.MarkVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	MarkVO markVO = (MarkVO)request.getAttribute("markVO");
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/metro/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css" /> 

<div id="mark_add_dlg_container">

	<div id="mark_add_dlg" class="easyui-dialog" title="修改标签"
		style="width:530px; height:300px; padding:15px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#mark_add_dlg_buttons',
			modal: true,
			onMove:_mark_add_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _mark_add_dlg_scope_;
			}
		">
		<form id="mark_add_dlg_form" method="post">
			<input type="hidden" name="id" value="<%=markVO.getId() %>" />
			<table border="0" cellpadding="0" cellspacing="0">
		    <tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />标签名：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" style="width:180px;height:24px" id="name" name="name" onblur="checkName()" value="<%=markVO.getName() %>" /></td>
		      <td class="inputtip" id="operator-tip-name"><i>请输入标签名</i></td>
		    </tr>
		    
		    
		    <tr style="height:56px;">
		      <td class="inputtitle">描述：</td>
		      <td class="inputcont"><textarea class="textbox" style="width:180px;height:48px" id="description" name="description" onblur="checkDescription()" /></td>
		      <td class="inputtip" id="operator-tip-description"><i>请输入描述信息</i></td>
		    </tr>
		    
		  </table>
			 
		</form>
	</div>

	<div id="mark_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="mark_add_dlg_save_btn"> &nbsp;保&nbsp;存&nbsp; </a>
		<a href="javascript:" class="easyui-linkbutton" id="mark_add_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
</div>

<script type="text/javascript">
//==================check begin==================
function checkName(){
	var account = new String($("#name").val()).trim(); 
	if(account==null || account==""){
		$("#operator-tip-name").html("<b>标签名不能为空</b>");
		return false;		
	}
	if(account.length>30){
		$("#operator-tip-name").html("<b>输入不能超过30个字符</b>");
		return false;
	}
	$("#operator-tip-name").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}

function checkDescription(){
	var description = new String($("#description").val()).trim();
	if(description.length>100){
		$("#operator-tip-description").html("<b>描述不能超过100个字符</b>");
		return false;
	}
	 
	return true;
}

//====================check end================
var _mark_add_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#mark_add_dlg";
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
	self.save = function() {
		var result = null;
		var formData = $.formToBean(mark_add_dlg_form);
		ajax.remoteCall("bean://markService:modMark", 
			[ formData ],
			function(reply) {
				result = new String(reply.result.message);
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					var data = $("#mark_add_dlg_container").parent().prop("_data_");
					$("#mark_add_dlg").dialog("close");
					data.onClose(reply.result);
				} else {
					$("#operator-tip-name").html("<b>"+reply.result.message+"</b>");
				}
			}
		);
	};

	// 关闭
	self.close = function() {
		$("#mark_add_dlg").dialog("close");
	};

	//--------------------------
	
	// 初始化
	$(document).ready(function(){
		var desc = "<%=markVO.getDescription()%>";
		$("#description").html(desc);
		// 保存
		$("#mark_add_dlg_save_btn").click(function() {
			if(checkName()&checkDescription()){
				self.save();
			}
		});
		// 关闭
		$("#mark_add_dlg_close_btn").click(function() {
			self.close();
		});
	});
};
</script>



