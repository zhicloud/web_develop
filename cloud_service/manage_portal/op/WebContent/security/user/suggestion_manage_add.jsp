<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="com.zhicloud.op.vo.SysDiskImageVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%> 
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>添加意见反馈 - 云端在线</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/metro/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css" /> 

</head>
<body>
<div id="suggestion_dlg_container">

	<div id="suggestion_dlg" class="easyui-dialog" title="添加意见反馈 "
		style="width:580px; height:350px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#suggestion_dlg_buttons',
			modal: true,
			onMove:_suggestion_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _suggestion_dlg_scope_;
			}
		">
		
		<form id="suggestion_dlg_form" method="post">
		
			<table border="0" style="margin:0 auto 0 auto;"> 
				<tr>
					<td class="inputtitle" style="width:100px"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />类别：</td>
					<td class="inputcont">
						<select id="type" name="type" class="inputselect" onblur="checkType()">
							<option value="">请选择</option> 
							<option value="1">页面风格</option> 
							<option value="2">产品功能</option> 
							<option value="3">其他</option> 
						</select> 
					<td class="inputtip" id="terminaluser-tip-type"><i>请选择一个类别</i></td>
					</td>
				</tr> 
				<tr>
					<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />内容：</td>
					<td class="inputcont">
						<textarea name="content" id="content" onblur="checkContent();" rows="8" cols="30" style="resize:none;"></textarea>
                    </td>
					<td class="inputtip" id="teminaluser-tip-content"><i>最多输入100个字符</i></td>
				</tr>
			</table>
		</form>
		
	</div>
	
	<div id="suggestion_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="suggestion_dlg_save_btn">&nbsp;保&nbsp;存&nbsp;</a> 
		<a href="javascript:" class="easyui-linkbutton" id="suggestion_dlg_close_btn">&nbsp;关&nbsp;闭&nbsp;</a>
	</div>
	
</div>

</body>
<script type="text/javascript">
 
function checkType(){
	var type = new String($("#type").val());
	if(type==null || type==""){
		$("#terminaluser-tip-type").html("<img src='<%=request.getContextPath()%>/images/button_caution_red_16_16.gif' width='16' height='16' alt='必填' /><b>请选择一个类别</b>");
		 
		return false;
	}
	$("#terminaluser-tip-type").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	 
	return true;
}
function checkContent(){
	var content = new String($("#content").val()); 
	if(content.length>100){
		$("#terminaluser-tip-content").html("<img src='<%=request.getContextPath()%>/images/button_caution_red_16_16.gif' width='16' height='16' alt='必填' /><b>最多输入100个字符</b>");
		 
		return false;
	}
	if(description.length==0){
		$("#terminaluser-tip-content").html("<img src='<%=request.getContextPath()%>/images/button_caution_red_16_16.gif' width='16' height='16' alt='必填' /><b>请输入内容</b>");
		 
		return false;
	}
	$("#terminaluser-tip-content").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");

	return true;
}
 


//----------------------------
var _suggestion_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#suggestion_dlg";
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
		var formData = $.formToBean(suggestion_dlg_form);
		ajax.remoteCall("bean://suggestionService:addSuggestion", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception")
				{
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				}
				else if (reply.result.status == "success")
				{
					top.$.messager.alert('提示','提交成功！<br/>感谢提出您的宝贵意见，我们会尽快给您答复，谢谢！','info',function(){ 
						var data = $("#suggestion_dlg_container").parent().prop("_data_");
						$("#suggestion_dlg").dialog("close");
						data.onClose(reply.result);
					});
				}
				else
				{
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	};

	// 关闭
	self.close = function() {
		$("#suggestion_dlg").dialog("close");
	};

	//--------------------------

	$(document).ready(function(){
		// 保存
		$("#suggestion_dlg_save_btn").click(function() { 
				self.save(); 
		});
		// 关闭
		$("#suggestion_dlg_close_btn").click(function() {
			self.close();
		});
	});
};
</script>
</html>



