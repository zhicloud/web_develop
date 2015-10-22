<%@page import="com.zhicloud.op.vo.SysGroupVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	String region         = (String)request.getAttribute("region");
	String bandwidthMin_1 = AppProperties.getValue("bandwidthMin_1","");
	String bandwidthMin_2 = AppProperties.getValue("bandwidthMin_2","");
	String bandwidthMin_4 = AppProperties.getValue("bandwidthMin_4","");
	String bandwidthMax_1 = AppProperties.getValue("bandwidthMax_1","");
	String bandwidthMax_2 = AppProperties.getValue("bandwidthMax_2","");
	String bandwidthMax_4 = AppProperties.getValue("bandwidthMax_4","");
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/metro/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css" /> 

<div id="package_price_bandwidth_add_dlg_container">

	<div id="package_price_bandwidth_add_dlg" class="easyui-dialog" title="添加带宽配置"
		style="width:500px; height:250px; padding:15px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#package_price_bandwidth_add_dlg_buttons',
			modal: true,
			onMove:_package_price_bandwidth_add_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _package_price_bandwidth_add_dlg_scope_;
			}
		">
		<form id="package_price_bandwidth_add_dlg_form" method="post">
			<input type="hidden" name="type" value="4"/>
			<input type="hidden" name="region" value="<%=region%>"/>
			<table border="0" cellpadding="0" cellspacing="0">
		    <tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />带宽大小(M)：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" style="width:180px;height:24px" id="bandwidth" name="bandwidth" onblur="checkBandwidth()"/></td>
		      <td class="inputtip" id="tip-bandwidth-add"><i>请输入带宽大小(单位：M)</i></td>
		    </tr>
		     <tr>
      			<td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />类型：</td>
      			<td class="inputcont"><select class="inputselect"  id="purpose_status" name="status" onblur="checkPurpose()" >
		          <option value="1" selected="selected">通用</option>
		          <option value="2">仅专属云</option>
		          <option value="3">非专属云</option>
        		</select></td>
		        <td class="inputtip" id="purpose-tip"><i>请选择类型</i></td>
		    </tr>
<!-- 		    <tr style="height:56px;"> -->
<%-- 		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />价格：</td> --%>
<!-- 		      <td class="inputcont"><input class="textbox inputtext" type="text" style="width:180px;height:24px" id="price" name="price" onblur="checkPrice()"/></td> -->
<!-- 		      <td class="inputtip" id="tip-bandwidth-price-add"><i>请输入价格</i></td> -->
<!-- 		    </tr> -->
		  </table>
			 
		</form>
	</div>

	<div id="package_price_bandwidth_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="package_price_bandwidth_add_dlg_save_btn"> &nbsp;保&nbsp;存&nbsp; </a>
		<a href="javascript:" class="easyui-linkbutton" id="package_price_bandwidth_add_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
</div>

<script type="text/javascript">
var bandwidthMin_1 = "<%=bandwidthMin_1%>";
var bandwidthMin_2 = "<%=bandwidthMin_2%>";
var bandwidthMin_4 = "<%=bandwidthMin_4%>";
var bandwidthMax_1 = "<%=bandwidthMax_1%>";
var bandwidthMax_2 = "<%=bandwidthMax_2%>";
var bandwidthMax_4 = "<%=bandwidthMax_4%>";
var region = "<%=region%>";
var min_1 = parseInt(bandwidthMin_1);
var min_2 = parseInt(bandwidthMin_2);
var min_4 = parseInt(bandwidthMin_4);
var max_1 = parseInt(bandwidthMax_1);
var max_2 = parseInt(bandwidthMax_2);
var max_4 = parseInt(bandwidthMax_4);
var regionValue = parseInt(region);
//==================check begin==================
function checkBandwidth(){
	var bandwidth = new String($("#bandwidth").val()).trim(); 
	var bandwidthValue = parseInt(bandwidth);
	if(bandwidth==null || bandwidth==""){
		$("#tip-bandwidth-add").html("<b>带宽大小不能为空</b>");
		return false;		
	}
	if(!(/^[1-9][0-9]{0,3}?$/.test(bandwidth))){
		$("#tip-bandwidth-add").html("<b>带宽大小应为最大4位的正整数</b>");
		return false;
	}
	if(region==1){
		if(min_1>bandwidthValue || bandwidthValue > max_1){
			$("#tip-bandwidth-add").html("<b>带宽大小超出设定范围["+min_1+"-"+max_1+"]M</b>");
			return false;
		}
	}
	if(region==2){
		if(min_2>bandwidthValue || bandwidthValue > max_2){
			$("#tip-bandwidth-add").html("<b>带宽大小超出设定范围["+min_2+"-"+max_2+"]M</b>");
			return false;
		}
	}
	if(region==4){
		if(min_4>bandwidthValue || bandwidthValue > max_4){
			$("#tip-bandwidth-add").html("<b>带宽大小超出设定范围["+min_4+"-"+max_4+"]M</b>");
			return false;
		}
	}
	$("#tip-bandwidth-add").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}

function checkPrice(){
	var price = new String($("#price").val()).trim();
	if(price==null || price==""){
		$("#tip-bandwidth-price-add").html("<b>价格不能为空</b>");
		return false;
	}
	if(!(/^[0-9]+.?[0-9]*$/.test(price))){
		$("#tip-bandwidth-price-add").html("<b>价格应为正数</b>");
		return false;
	}
	$("#tip-bandwidth-price-add").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
	
}
function checkPurpose(){
	var p_status = new String($("#purpose_status").val()).trim();
	if(p_status==null || p_status==""){
		$("#purpose-tip").html("<b>请选择一个类型</b>");
		return false;
	}
	$("#purpose-tip").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
	
}
//====================check end================
var _package_price_bandwidth_add_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#package_price_bandwidth_add_dlg";
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
		var formData = $.formToBean(package_price_bandwidth_add_dlg_form);
		ajax.remoteCall("bean://packageOptionService:addPackagePrice", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					var data = $("#package_price_bandwidth_add_dlg_container").parent().prop("_data_");
					$("#package_price_bandwidth_add_dlg").dialog("close");
					data.onClose(reply.result);
				} else {
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	};

	// 关闭
	self.close = function() {
		$("#package_price_bandwidth_add_dlg").dialog("close");
	};

	//--------------------------
	
	// 初始化
	$(document).ready(function(){
		// 保存
		$("#package_price_bandwidth_add_dlg_save_btn").click(function() {
			if(checkBandwidth() & checkPurpose()){
				self.save();
			}
		});
		// 关闭
		$("#package_price_bandwidth_add_dlg_close_btn").click(function() {
			self.close();
		});
	});
};
</script>



