﻿<%@page import="com.zhicloud.op.app.pool.CloudHostData"%>
<%@page import="com.zhicloud.op.app.pool.CloudHostPoolManager"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.vo.TerminalUserVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	TerminalUserVO terminalUserVO = (TerminalUserVO)request.getAttribute("user");
%>
<!-- user_detail.jsp -->
<div id="vpc_detail_dlg_container">
	<div id="vpc_detail_dlg" class="easyui-dialog" title="用户详情"
		style="width:800px; height:300px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			modal: true,
			onMove: _vpc_detail_dlg_scope_.onMove ,
			onClose: _vpc_detail_dlg_scope_.myOnClose ,
			onDestroy: function(){
 				delete _vpc_detail_dlg_scope_; 
			}
		">
		<form id="vpc_detail_dlg_form" method="post">
			
			<table border="0" style="width:90%; margin:0 auto 0 auto;">
				<tr>
					<td style="padding:5px;">用户名(邮箱)：<%=terminalUserVO.getEmail() %></td>
					<td style="padding:5px;">邮箱验证：<%=terminalUserVO.getEmailVerified()==1?"否":"是" %></td>
				</tr>
				<tr>
					<td style="padding:5px;">手机号码：<%=terminalUserVO.getPhone() %></td>
					<td style="padding:5px;">手机验证：<%=terminalUserVO.getPhoneVerified()==1?"否":"是" %></td>
				</tr>
				<tr>
					<td style="padding:5px;">状态：<%=terminalUserVO.getStatusFormat() %></td>
					<td style="padding:5px;">余额：<%=terminalUserVO.getAccountBalance() %>元</td>
				</tr> 
				<tr>
					<td style="padding:5px;">云主机数：<%=terminalUserVO.getHostAmount() %></td>
					<td style="padding:5px;">专属云数：<%=terminalUserVO.getVpcAmount() %></td>
				</tr> 
				<tr>
					<td style="padding:5px;">云硬盘数：<%=terminalUserVO.getDiskAmount() %></td>
					<td style="padding:5px;">创建时间：<%=terminalUserVO.getCraeteTimeFormat() %></td>
				</tr> 
				<!-- </tr>
				<tr>
					<td style="padding:5px;">磁盘读总流量：0</td>
					<td style="padding:5px;">磁盘写总流量：0</td>
				</tr>
				<tr>
					<td style="padding:5px;">网络读总流量：100G</td>
					<td style="padding:5px;">网络写总流量：100G</td>
				</tr>
				<tr>
					<td colspan="99"><hr /></td>
				</tr> -->
				
			</table>
			
		</form>
	</div>

</div>


<script type="text/javascript">


//-----------------------------

//var si = null;

var _vpc_detail_dlg_scope_ = new function(){
	
	var self = this;
	
	self.si = null;
	
	self.myOnClose = function(){
		window.clearInterval(self.si);
	    $("#vpc_detail_dlg").dialog('destroy');
		return true; 
	};
	
	self.onMove = function(){
		var thisId = "#vpc_detail_dlg";
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
	}
	
	// 保存
	self.save = function() {
		var formData = $.formToBean(vpc_detail_dlg_form);
		ajax.remoteCall("bean://cloudHostService:addCloudHost", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					var data = $("#vpc_detail_dlg_container").parent().prop("_data_");
					$("#vpc_detail_dlg").dialog("close");
					data.onClose(reply.result);
				} else {
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	};

	// 关闭
	self.close = function() {
		$("#vpc_detail_dlg").dialog("close");
	};
	
	//--------------------------
	$(document).ready(function() {
		
		// 保存
		$("#vpc_detail_dlg_save_btn").click(function() {
			self.save();
		});
		// 关闭
		$("#vpc_detail_dlg_close_btn").click(function() {
			self.close();
		});
	});
	
};
	
	
</script>



