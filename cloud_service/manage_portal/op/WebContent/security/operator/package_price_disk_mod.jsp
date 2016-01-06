<%@page import="com.zhicloud.op.vo.PackagePriceVO"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	PackagePriceVO packagePrice = (PackagePriceVO)request.getAttribute("packagePrice");
	String dataDiskMin = AppProperties.getValue("dataDiskMin","");
	String dataDiskMax = AppProperties.getValue("dataDiskMax","");
%>
<!-- package_price_disk_mod.jsp -->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/metro/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css" /> 

<div id="package_price_disk_mod_dlg_container">

	<div id="package_price_disk_mod_dlg" class="easyui-dialog" title="修改内存配置"
		style="width:500px; height:250px; padding:15px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#package_price_disk_mod_dlg_buttons',
			modal: true,
			onMove:_package_price_disk_mod_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _package_price_disk_mod_dlg_scope_;
			}
		">
		<form id="package_price_disk_mod_dlg_form" method="post">
			<input type="hidden" name="id" value="<%=packagePrice.getId()%>"/>
			<input type="hidden" name="type" value="3"/>
			<input type="hidden" name="region" value="1"/>
			<table border="0" cellpadding="0" cellspacing="0">
		    <tr>
		      <td class="inputtitle"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />硬盘大小：</td>
		      <td class="inputcont"><input class="textbox inputtext" type="text" style="width:180px;height:24px" id="disk" name="dataDisk" value="<%=CapacityUtil.toGBValue(packagePrice.getDataDisk(), 0) %>" onblur="checkDisk()"/></td>
		      <td class="inputtip" id="tip-disk-mod"><i></i></td>
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
<%-- 		      <td class="inputcont"><input class="textbox inputtext" type="text" style="width:180px;height:24px" id="price" name="price" value="<%=packagePrice.getPrice() %>" onblur="checkPrice()"/></td> --%>
<!-- 		      <td class="inputtip" id="tip-disk-price-mod"><i></i></td> -->
<!-- 		    </tr> -->
		  </table>
			 
		</form>
	</div>

	<div id="package_price_disk_mod_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="package_price_disk_mod_dlg_save_btn"> &nbsp;保&nbsp;存&nbsp; </a>
		<a href="javascript:" class="easyui-linkbutton" id="package_price_disk_mod_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
</div>

<script type="text/javascript">
var diskMin = "<%=dataDiskMin%>";
var diskMax = "<%=dataDiskMax%>";
var min = parseInt(diskMin);
var max = parseInt(diskMax);
//==================check begin==================
function checkDisk(){
	var disk = new String($("#disk").val()).trim(); 
	var diskValue = parseInt($("#disk").val());
	if(disk==null || disk==""){
		$("#tip-disk-mod").html("<b>硬盘大小不能为空</b>");
		return false;		
	}
	if(!(/^[1-9][0-9]{0,4}?$/.test(disk))){
		$("#tip-disk-mod").html("<b>硬盘大小应为最大五位的正整数</b>");
		return false;
	}
	if(diskMin>diskValue || diskValue>diskMax){
		$("#tip-disk-mod").html("<b>硬盘大小超出设定范围["+diskMin+"-"+diskMax+"]GB</b>");
		return false;
	}
	$("#tip-disk-mod").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
	return true;
}

function checkPrice(){
	var price = new String($("#price").val()).trim();
	if(price==null || price==""){
		$("#tip-disk-price-mod").html("<b>价格不能为空</b>");
		return false;
	}
	if(!(/^[0-9]+.?[0-9]*$/.test(price))){
		$("#tip-disk-price-mod").html("<b>价格应为正数</b>");
		return false;
	}
	$("#tip-disk-price-mod").html("<img src='<%=request.getContextPath()%>/images/button_validated_green_16_16.gif' width='16' height='16' alt='正确' />");
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
var _package_price_disk_mod_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#package_price_disk_mod_dlg";
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
		var formData = $.formToBean(package_price_disk_mod_dlg_form);
		ajax.remoteCall("bean://packageOptionService:modPackagePrice", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					var data = $("#package_price_disk_mod_dlg_container").parent().prop("_data_");
					$("#package_price_disk_mod_dlg").dialog("close");
					data.onClose(reply.result);
				} else {
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	};

	// 关闭
	self.close = function() {
		$("#package_price_disk_mod_dlg").dialog("close");
	};

	//--------------------------
	var curStatus = "<%=packagePrice.getStatus()%>";
	// 初始化
	$(document).ready(function(){
		$("#purpose_status option[value="+curStatus+"]").attr("selected",true);
		// 保存
		$("#package_price_disk_mod_dlg_save_btn").click(function() {
			if(checkDisk() & checkPurpose()){
				self.save();
			}
		});
		// 关闭
		$("#package_price_disk_mod_dlg_close_btn").click(function() {
			self.close();
		});
	});
};
</script>



