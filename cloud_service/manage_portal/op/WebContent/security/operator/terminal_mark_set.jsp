<%@page import="com.zhicloud.op.vo.SysGroupVO"%>
<%@page import="com.zhicloud.op.vo.MarkVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%

String userids = (String)request.getAttribute("userids");
List<MarkVO> markList = (List<MarkVO>)request.getAttribute("markList");
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/metro/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css" /> 

<div id="terminal_user_add_dlg_container">

	<div id="terminal_user_add_dlg" class="easyui-dialog" title="设置标签"
		style="width:500px; height:200px; padding:15px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#terminal_user_add_dlg_buttons',
			modal: true,
			onMove:_terminal_user_add_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _terminal_user_add_dlg_scope_;
			}
		">
		<form id="terminal_user_add_dlg_form" method="post">
		<input type="hidden" name="userids" id="userids" value="<%=userids%>"/>
			<table border="0" cellpadding="0" cellspacing="0">
		    <tr style="height:50px;">
		      <td class="inputtitle">标签：</td>
		      <td class="inputcont">
		      <select  id="mark_id" name="markId" class="inputselect" onblur="checkMarkId()">
		          <option value="">请选择</option>
		          <%
						for( MarkVO mark : markList ) 
						{ 
				  %> 
		          <option value="<%=mark.getId()%>"><%=mark.getName()%></option>
		          <%
						} 
				  %> 
		        </select>
		        </td>
		        <td class="inputtip" id="operator-tip-group"><i>请选择一个标签</i></td>
		    </tr>
		  </table>
			 
		</form>
	</div>

	<div id="terminal_user_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="terminal_user_add_dlg_save_btn"> &nbsp;保&nbsp;存&nbsp; </a>
		<a href="javascript:" class="easyui-linkbutton" id="terminal_user_add_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
</div>

<script type="text/javascript">
function checkMarkId(){ 
	$("#operator-tip-group").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}
var _terminal_user_add_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#terminal_user_add_dlg";
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
		if(checkMarkId()){
		var result = null;
		var formData = $.formToBean(terminal_user_add_dlg_form);
		ajax.remoteCall("bean://terminalUserService:updateTerminalUserWithMarkID", 
			[ formData ],
			function(reply) {
				result = new String(reply.result.message);
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					var data = $("#terminal_user_add_dlg_container").parent().prop("_data_");
					$("#terminal_user_add_dlg").dialog("close");
					data.onClose(reply.result);
				} else {
						$("#operator-tip-group").html("<b>"+reply.result.message+"</b>");
				}
			}
		);
		}
	};

	// 关闭
	self.close = function() {
		$("#terminal_user_add_dlg").dialog("close");
	};

	//--------------------------
	
	// 初始化
	$(document).ready(function(){
		// 保存
		$("#terminal_user_add_dlg_save_btn").click(function() {
				self.save();
		});
		// 关闭
		$("#terminal_user_add_dlg_close_btn").click(function() {
			self.close();
		});
	});
};
</script>



