
<%@page import="com.zhicloud.op.vo.SuggestionVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%> 
<%
 	SuggestionVO suggestion  = (SuggestionVO)request.getAttribute("suggestion");
 %>
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
		style="width:580px; height:390px; padding:10px;"
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
						<select id="type" name="type" value="<%=suggestion.getType() %>" class="inputselect"  >
							<option value="">请选择</option> 
							<option value="1">页面风格</option> 
							<option value="2">产品功能</option> 
							<option value="3">其他</option> 
						</select> 
					<td class="inputtip" id="operator-tip-image"> </td>
					</td>
				</tr> 
				<tr>
					<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />内容：</td>
					<td class="inputcont">
						<textarea name="content" id="content"  rows="8" cols="30" style="resize:none;"><%=suggestion.getContent() %></textarea>
                    </td>
					<td class="inputtip" id="operator-tip-description"> </td>
				</tr>
				<%
				  if(suggestion.getStatus()!=null&&suggestion.getStatus()==2){ 
				%>
				<tr>
					<td class="inputtitle"> 答复：</td>
					<td class="inputcont">
						<textarea name="result" id="result"   rows="8" cols="30" style="resize:none;"><%=suggestion.getResult() %></textarea>
                    </td>
					<td class="inputtip" id="operator-tip-description"> </td>
				</tr>
				
				<%
				  }
				%>
			</table>
		</form>
		
	</div> 
	
</div>

</body>
<script type="text/javascript">
//----------------------------
 
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
		$("#type").val("<%=suggestion.getType()%>");
	});
};
</script>
</html>



