<%@page import="com.zhicloud.op.vo.MessageRecordVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	MessageRecordVO messageRecordVO = (MessageRecordVO)request.getAttribute("messageRecordVO");
	
%>
<!-- email_manage_detail.jsp -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/metro/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css" /> 

<div id="message_detail_dlg_container">

	<div id="message_detail_dlg" class="easyui-dialog" title="邮件内容"
		style="width:530px; height:300px; padding:15px;"
		data-options="
			iconCls: 'icon-tip',
			buttons: '#message_detail_dlg_buttons',
			modal: true,
			onMove:_message_detail_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _message_detail_dlg_scope_;
			}
		">
		<form id="message_detail_dlg_form" method="post">
			<input type="hidden" name="id" value="<%=messageRecordVO.getId() %>" />
			<table border="0" cellpadding="0" cellspacing="0">
		    <tr>
		      <td class="inputtitle">发送地址</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" readonly='true' style="width:180px;height:24px" id="name" name="name"" value="<%=messageRecordVO.getSenderAddress() %>" /></td>		      
		    </tr>
		    <tr>
		      <td class="inputtitle">接收地址</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" readonly='true' style="width:180px;height:24px" id="name" name="name"" value="<%=messageRecordVO.getRecipientAddress() %>" /></td>		      
		    </tr>
		    <tr>
		      <td class="inputtitle">发送时间</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" readonly='true' style="width:180px;height:24px" id="name" name="name"" value="<%=messageRecordVO.getCreateTime() %>" /></td>		      
		    </tr>
		    <tr>
		      <td class="inputtitle">邮件内容</td>
		      <td class="inputcont"><!-- <input class="textbox inputtext" type="text" readonly='true' style="width:180px;height:24px" id="name" name="name"" value="<%=messageRecordVO.getContent() %>" /> -->		      	
		      	<%=messageRecordVO.getContent() %>
		      </td>		      
		    </tr>
		    
		    <!-- 
		    <tr style="height:56px;">
		      <td class="inputtitle">描述：</td>
		      <td class="inputcont"><textarea class="textbox" style="width:180px;height:48px" id="description" name="description" onblur="checkDescription()" /></td>
		      <td class="inputtip" id="operator-tip-description"><i>请输入描述信息</i></td>
		    </tr>
		     -->
		  </table>
			 
		</form>
	</div>

	<div id="message_detail_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="message_detail_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
</div>

<script type="text/javascript">
var _message_detail_dlg_scope_ = new function(){	
	var self = this;
	self.onMove = function(){
		var thisId = "#message_detail_dlg";
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


	// 关闭
	self.close = function() {
		$("#message_detail_dlg").dialog("close");
	};

	//--------------------------
	
	// 初始化
	$(document).ready(function(){
		// var desc = "<%=messageRecordVO.getContent()%>";
		// $("#description").html(desc);

		// 关闭
		$("#message_detail_dlg_close_btn").click(function() {
			self.close();
		});
	});
};
</script>



