<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
		Integer userType = Integer.valueOf(request.getParameter("userType"));
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String userId  = loginInfo.getUserId();
%>

<!--invoice_address_add.jsp -->

<div id="invoice_address_add_dlg_container">

	<div id="invoice_address_add_dlg" class="easyui-dialog" title="添加邮寄地址"
		style="width:440px; height:240px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#invoice_address_add_dlg_buttons',
			modal: true,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _invoice_address_add_dlg_scope_;
			}
		">
		<form id="invoice_address_add_dlg_form" method="post">
				<input id="userId" name="userId"  value ="<%=userId %>"  type ="hidden" maxlength="100" style="width:250px" />
  		<table border="0" cellpadding="0" cellspacing="0">
		   <tr>
					<td>发票抬头：<input id="invoiceTitle" name="invoiceTitle"  type = "text" maxlength="100" style="width:250px" /></td>
				</tr>
				
				<tr>
					<td>邮寄地址：<input id="address" name="address" type = "text"   maxlength="100" style="width:250px"/></td>
				</tr>	
				
				<tr>	
					<td>收件人：&nbsp;&nbsp;&nbsp;<input id="recipients" name="recipients"   type = "text"  maxlength="100" style="width:250px"/></td>
				</tr>
				
				<tr>	
					<td>联系方式：<input id="phone" name="phone"  type = "text"  maxlength="100" style="width:250px"/></td>
				</tr>
		  </table>
		</form>
	</div>

	<div id="invoice_address_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="invoice_address_add_dlg_save_btn">&nbsp;保&nbsp;存&nbsp;</a> 
		<a href="javascript:" class="easyui-linkbutton" id="invoice_address_add_dlg_close_btn">&nbsp;关&nbsp;闭&nbsp;</a>
	</div>
</div> 
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script> 
	 
<script type="text/javascript">
var _invoice_address_add_dlg_scope_ = new function(){
	
	var self = this;
	
	function checkInvocieTitle(){
		var invocieTitle = new String($("#invocieTitle").val());
		if(invocieTitle==null || invocieTitle==""){
			top.$.messager.alert('警告','发票抬头不能为空','warning');
			return false;
		}
		return true;
	}
	
	function checkAddress(){
		var address = new String($("#address").val());
		if(address==null || address==""){
			top.$.messager.alert('警告','邮寄地址不能为空','warning');
			return false;
		}
		return true;
	}
	
	function checkRecipients(){
		var recipients = new String($("#recipients").val());
		if(recipients==null || recipients==""){
			top.$.messager.alert('警告','收件人不能为空','warning');
			return false;
		}
		return true;
	}
	
	function checkRecipients(){
		var recipients = new String($("#recipients").val());
		if(recipients==null || recipients==""){
			top.$.messager.alert('警告','收件人不能为空','warning');
			return false;
		}
		return true;
	}
	
	function checkPhone(){
		var phone = new String($("#phone").val());
		if(phone==null || phone==""){
			top.$.messager.alert('警告','手机号码不能为空','warning');
			return false;
		}
		if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))){ 
			top.$.messager.alert('警告','请输入正确的手机号码','warning');
			return false;
		}
		return true;
	}
	
	// 保存
	self.save = function(){
		var formData = $.formToBean(invoice_address_add_dlg_form);
		ajax.remoteCall("bean://invoiceAddressService:addAddress", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					top.$.messager.alert('提示','保存成功','info',function(){
						var data = $("#invoice_address_add_dlg_container").parent().prop("_data_");
						$("#invoice_address_add_dlg").dialog("close");
						data.onClose(reply.result);
					});
				}
			}
		);
	};

	// 关闭
	this.close = function() {
		$("#invoice_address_add_dlg").dialog("close");
	};

	// 初始化
	$(document).ready(function(){
		// 保存
		$("#invoice_address_add_dlg_save_btn").click(function() {
			if(checkInvocieTitle() && checkAddress()  && checkRecipients() && checkPhone()){
				self.save();
			}
		});
		// 关闭
		$("#invoice_address_add_dlg_close_btn").click(function() {
			self.close();
		});
	});
};
</script>