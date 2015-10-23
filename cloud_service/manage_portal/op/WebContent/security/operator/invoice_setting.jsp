<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/js/easyui/themes/metro/easyui.css" />
<div id="invoice_setting_dlg_container">

	<div id="invoice_setting_dlg" class="easyui-dialog" title="发票设置"
		style="width:440px; height:480px; padding:10px;"
		data-options="
			buttons: '#invoice_setting_dlg_buttons',
			modal: true,
			onMove:_invoice_setting_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _invoice_setting_dlg_scope_;
			}
		">
		<form id="invoice_setting_dlg_form" method="post">
  		<table border="0" cellpadding="0" cellspacing="0">
		    <tr>
		    
		    <tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />行业类别：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" id="invoice_type" name="invoice_type" onblur="checkType()"/></td>
		      <td class="inputtip" id="operator-tip-invoice-type"></td>
		    </tr>

		    <tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />税率：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" id="tax_rate" name="tax_rate" onblur="checkTaxRate()"/></td>
		      <td class="inputtip" id="operator-tip-tax_rate"></td>
		    </tr>		    
		    
		    <tr>
		      <td class="inputtitle">财务科目：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" id="Financial_title" name="Financial_title"/></td>
		      <td class="inputtip" id="operator-tip-Financial_title"></td>
		    </tr>			 
		 
		    <tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />开户银行及帐号：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" id="invoice_bank" name="invoice_bank" onblur="checkinvoice_bank()"/></td>
		      <td class="inputtip" id="operator-tip-invoice_bank"></td>
		    </tr>	

		    <tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />公司地址电话：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" id="invoice_address" name="invoice_address" onblur="checkinvoice_addressk()"/></td>
		      <td class="inputtip" id="operator-tip-invoice_address"></td>
		    </tr>
		    		    
		    <tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />复核人：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" id="invoice_drawer" name="invoice_drawer" onblur="checkDrawer()"/></td>
		      <td class="inputtip" id="operator-tip-invoice-drawer"></td>
		    </tr>
		     <tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />收款人：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" id="invoice_payee" name="invoice_payee" onblur="checkPayee()"/></td>
		      <td class="inputtip" id="operator-tip-invoice-payee"></td>
		    </tr>
		    <tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />项目名称：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" id="invoice_subject" name="invoice_subject" onblur="checkSubject()"/></td>
		      <td class="inputtip" id="operator-tip-invoice-subject"></td>
		    </tr>
		  </table>
		</form>
	</div>

	<div id="invoice_setting_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="invoice_setting_dlg_save_btn">&nbsp;保&nbsp;存&nbsp;</a> 
		<a href="javascript:" class="easyui-linkbutton" id="invoice_setting_dlg_close_btn">&nbsp;关&nbsp;闭&nbsp;</a>
	</div>
</div>
<script type="text/javascript">
//==================check begin==================

function checkType(){
	var invoice_type = new String($("#invoice_type").val()).trim();
	if(invoice_type==null || invoice_type	==""){
		$("#operator-tip-invoice-type").html("<b>行业类别不能为空</b>");
		return false;
	}
	$("#operator-tip-invoice-type").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif'/>");
	return true;
}

function checkinvoice_address(){
	var invoice_address = new String($("#invoice_address").val()).trim();
	if(invoice_address==null || invoice_address	==""){
		$("#operator-tip-invoice_address").html("<b>公司地址不能为空 </b>");
		return false;
	}
	$("#operator-tip-invoice_address").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif'/>");
	return true;
}


function checkinvoice_bank(){
	var invoice_bank = new String($("#invoice_bank").val()).trim();
	if(invoice_bank==null || invoice_bank	==""){
		$("#operator-tip-invoice_bank").html("<b>开会银行及帐号不能为空 </b>");
		return false;
	}
	$("#operator-tip-invoice_bank").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif'/>");
	return true;
}

function checkTaxRate(){
	var tax_rate = new String($("#tax_rate").val()).trim();
	if(tax_rate==null || tax_rate	==""){
		$("#operator-tip-invoice-type").html("<b>税率不能为空</b>");
		return false;
	}
	$("#operator-tip-tax_rate").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif'/>");
	return true;
}

function checkDrawer(){
	var invoice_drawer = new String($("#invoice_drawer").val()).trim();
	if(invoice_drawer==null || invoice_drawer==""){
		$("#operator-tip-invoice-drawer").html("<b>开票人不能为空</b>");
		return false;
	}
	$("#operator-tip-invoice-drawer").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}
function checkPayee(){
	var invoice_payee = new String($("#invoice_payee").val()).trim();
	if(invoice_payee==null || invoice_payee==""){
		$("#operator-tip-invoice-payee").html("<b>收款人不能为空</b>");
		return false;
	}
	$("#operator-tip-invoice-payee").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}
function checkSubject(){
	var invoice_subject = new String($("#invoice_subject").val()).trim();
	if(invoice_subject==null || invoice_subject==""){
		$("#operator-tip-invoice-subject").html("<b>项目名称不能为空</b>");
		return false;
	}
	$("#operator-tip-invoice-subject").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确'/>");
	return true;
}

//====================check end================
var _invoice_setting_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#invoice_setting_dlg";
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
	self.save = function(){
		var formData = $.formToBean(invoice_setting_dlg_form);
		ajax.remoteCall("bean://invoiceService:saveSetting", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					var data = $("#invoice_setting_dlg_container").parent().prop("_data_");
					$("#invoice_setting_dlg").dialog("close");
					data.onClose(reply.result);
				} else {
					$("#operator-tip-invoice-type").html("<b>"+reply.result.message+"</b>");
				}
			}
		);
	};

	// 关闭
	this.close = function() {
		$("#invoice_setting_dlg").dialog("close");
	};

	// 初始化
	$(document).ready(function(){
		$("#invoice_type").val("<%=AppProperties.getValue("invoice_type", "")%>");
		$("#invoice_drawer").val("<%=AppProperties.getValue("invoice_drawer", "")%>");
		$("#invoice_payee").val("<%=AppProperties.getValue("invoice_payee", "")%>");
		$("#invoice_subject").val("<%=AppProperties.getValue("invoice_subject", "")%>");
		$("#tax_rate").val("<%=AppProperties.getValue("tax_rate", "")%>");		
		$("#Financial_title").val("<%=AppProperties.getValue("Financial_title", "")%>");		
		$("#invoice_bank").val("<%=AppProperties.getValue("invoice_bank", "")%>");		
		$("#invoice_address").val("<%=AppProperties.getValue("invoice_address", "")%>");				
		
		// 保存
		$("#invoice_setting_dlg_save_btn").click(function() {
			if(checkType() && checkDrawer()  && checkSubject() && checkinvoice_address() && checkinvoice_bank() &&  checkTaxRate()){
				self.save();
			}
		});
		// 关闭
		$("#invoice_setting_dlg_close_btn").click(function() {
			self.close();
		});
	});
};
</script>