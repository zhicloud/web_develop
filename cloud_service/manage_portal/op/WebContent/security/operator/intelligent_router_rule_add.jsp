﻿<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoExt"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%

 List<ServiceInfoExt> targetarray = (List<ServiceInfoExt>)request.getAttribute("targetarray");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<!-- intelligent_router_rule_add.jsp -->
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>运营商 - 新增规则</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/metro/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css" /> 

</head>
<body>
<div id="rule_add_dlg_container">
	<div id="rule_add_dlg" class="easyui-dialog" title="添加规则"
		style="width:500px; height:400px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#rule_add_dlg_buttons',
			modal: true,
			onMove:_rule_add_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _rule_add_dlg_scope_;
			}
		">
		<form id="rule_add_dlg_form" method="post">
  <table border="0" cellpadding="0" cellspacing="0">
  	<tr   style="height:50px;">
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />地域：</td>
		      <td class="inputcont"><select class="inputselect"  id="region" name="region" onblur="checkMarkId()">
		          <option value="1">广州</option> 
		          <option value="2">成都</option> 
		          <option value="4">香港</option> 
		        </select></td>
		        <td class="inputtip" id="operator-tip-mark"><i>请选择一个地域</i></td>
	</tr>
	<tr  style="height:50px;" >
		      <td class="inputtitle">智能路由：</td>
		      <td class="inputcont"><select class="inputselect"  id="mark_id" name="markId" onblur="checkMarkId()">
		          <option value="">请选择</option> 
		          <%
					for( ServiceInfoExt mark : targetarray ) 
					{ 
						if(!mark.getName().contains("intelligen")||mark.getRegion()!=1){
							continue;
						}
				  %> 
		          <option value="<%=mark.getName()%>" region="<%=mark.getRegion()%>"><%=mark.getName()%></option>
		          <%
						} 
				  %>  
		        </select></td>
		        <td class="inputtip" id="operator-tip-mark"><i>请选择一个标签</i></td>
		    </tr>
   	<tr   style="height:50px;">
		      <td class="inputtitle">规则：</td>
		      <td class="inputcont"><select class="inputselect"  id="mode" name="mode"  >
<!-- 		      	  <option value="">请选择</option> -->
		          <option value="0" checked="checked">INPUT规则</option> 
<!-- 		          <option value="1">FORWARD规则</option>  -->
<!-- 		          <option value="2">NAT规则</option>  -->
		        </select></td>
		        <td class="inputtip" id="operator-tip-mark"><i>请选择一个规则</i></td>
	</tr>  
	 <tr id="sourceIP1"  style="height:50px;">
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />目的IP：</td>
      <td class="inputcont"><input class="textbox inputtext" type="text" id="ip1" name="ip1" onblur="checkIP('operator-tip-ip1','ip1')"/></td>
      <td class="inputtip" id="operator-tip-ip1"> <i>请输入IP</i></td>
    </tr> 
    <tr id="sourceIP2"  style="height:50px;">
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />源IP：</td>
      <td class="inputcont"><input class="textbox inputtext" type="text" id="ip2" name="ip2" onblur="checkIP('operator-tip-ip2','ip2')"/></td>
      <td class="inputtip" id="operator-tip-ip2"> <i>请输入IP</i></td>
    </tr>  
    <tr id="sourcePort1"  style="height:50px;">
      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />目的端口：</td>
      <td class="inputcont"><input class="textbox inputtext" type="text" id="port1" name="port1" onblur="checkPort('operator-tip-port1','port1')"/></td>
      <td class="inputtip" id="operator-tip-port1"> <i>请输入端口</i></td>
    </tr> 
<!--     <tr id="sourcePort2"> -->
<%--       <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />转换后端口：</td> --%>
<!--       <td class="inputcont"><input class="textbox inputtext" type="text" id="port2" name="port2" onblur="checkPort(operator-tip-port2)"/></td> -->
<!--       <td class="inputtip" id="operator-tip-port2"> <i>请输入端口</i></td> -->
<!--     </tr> -->
	
	   
    
    
      
  </table>
</form>
	</div>

	<div id="rule_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton"
			id="rule_add_dlg_save_btn"> &nbsp;保&nbsp;存&nbsp; </a> <a
			href="javascript:" class="easyui-linkbutton"
			id="rule_add_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
	
	<div style="display:none;">
		<select id="mark_id_template" name="mark_id_template">
		    <%
					for( ServiceInfoExt mark : targetarray ) 
					{ 
						if(!mark.getName().contains("intelligen")){
							continue;
						}
			  %> 
	          <option value="<%=mark.getName()%>" region="<%=mark.getRegion()%>"><%=mark.getName()%></option>
	          <%
					} 
			  %>  
		</select>
	</div>
</div>

</body>
<script type="text/javascript">
//==================check begin==================
 
function checkIP(id1,id2){
		var reg = /^((1?\d?\d|(2([0-4]\d|5[0-5])))\.){3}(1?\d?\d|(2([0-4]\d|5[0-5])))$/ ;  
		if($("#"+id2).val() == ""){
			$("#"+id1).html("<b>请输入IP地址</b>");
			return false;
		}
        if(reg.test($("#"+id2).val())){
    		$("#"+id1).html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />"); 

        	return true;
        }else{
        	$("#"+id1).html("<b>请输入合法的IP地址</b>");
        	return false;
        }
	}
	
function checkPort(id1,id2){
	if($("#"+id2).val() == ""){
		$("#"+id1).html("<b>请输入端口</b>");
		return false;
	}  
	if(!(/^[1-9][0-9]*$/.test($("#"+id2).val()))){
		$("#"+id1).html("<b>请输入合法的端口</b>"); 
		return false;
	}else{
		$("#"+id1).html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />"); 
		return true;
	}
}
	
//====================check end================
var _rule_add_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#rule_add_dlg";
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
		if(true){
		var formData = $.formToBean(rule_add_dlg_form);
		ajax.remoteCall("bean://intelligentRouterService:addRule", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
					{
						top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
							window.location.reload();
						});
					}else{
						
						top.$.messager.alert('警告',reply.exceptionMessage,'warning');
					}
				} else if (reply.result.status == "success") {
					var data = $("#rule_add_dlg_container").parent().prop("_data_");
					$("#rule_add_dlg").dialog("close");
					data.onClose(reply.result);
				} else {
					top.$.messager.alert("警告", "新增失败", "warning");	
				}
			}
		);
		}
	};
	
	
	// 关闭
	self.close = function() {
		$("#rule_add_dlg").dialog("close");
	};
	
	//--------------------------
	
	// 初始化
	$(document).ready(function(){
		
		$("#region").change(function(){
			$("#mark_id").find("option:eq(0) ~ *").remove();													// 删除除第一个之外的option
			$("#mark_id_template").find("> option[region="+this.value+"]").clone().appendTo("#mark_id");	// 加入option
		});
		// 保存
		$("#rule_add_dlg_save_btn").click(function() {
			if(checkIP('operator-tip-ip1','ip1')&&checkIP('operator-tip-ip2','ip2')&&checkPort('operator-tip-port1','port1')){
				self.save();
			}
		});
		// 关闭
		$("#rule_add_dlg_close_btn").click(function() {
			self.close();
		});
		
		
	});
};

</script>
</html>



