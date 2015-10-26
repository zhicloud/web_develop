<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.app.helper.RegionHelper.RegionData"%>
<%@page import="com.zhicloud.op.app.helper.RegionHelper"%>
<%@page import="com.zhicloud.op.vo.SysDiskImageVO"%>
<%@page import="com.zhicloud.op.common.util.FlowUtil"%>
<%@page import="com.zhicloud.op.vo.DiskPackageOptionVO"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
DiskPackageOptionVO diskOption = (DiskPackageOptionVO)request.getAttribute("diskOption");
List<SysDiskImageVO> sysDiskImageOptionsGZ = (List<SysDiskImageVO>)request.getAttribute("sysDiskImageOptionsGZ");
List<SysDiskImageVO> sysDiskImageOptionsHK = (List<SysDiskImageVO>)request.getAttribute("sysDiskImageOptionsHK");
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/metro/easyui.css" />

<div id="warehouse_add_dlg_container">

	<div id="warehouse_add_dlg" class="easyui-dialog" title="添加库存类型"
		style="width:650px; height:450px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#warehouse_add_dlg_buttons',
			modal: true,
			onMove: _warehouse_add_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _warehouse_add_dlg_scope_;
			}
		">
		<form id="warehouse_add_dlg_form" method="post">
			<table border="0" style="height: auto; margin: auto;">
				
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;">地域：</td>
					<td style="padding:5px 0 5px 0;">
						<select id="region" name="region" onchange="_warehouse_add_dlg_scope_.changeSysImage()">
							<%
								for( RegionData regionData : RegionHelper.singleton.getAllResions() )
								{
							%>
							<option value="<%=regionData.getId()%>"><%=regionData.getName()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;">系统磁盘：</td>
					<td style="padding:5px 0 5px 0;">
						<label for="create_from_img" style="padding:0 20px 0 0;">
							<input type="radio" id="create_from_img" name="sys_disk_type" value="from_sys_image" checked="checked" />从镜像创建
						</label>
						<div id="region_1">
							<select id="sys_image_id_GZ" name="sys_image_id_GZ" style="width:225px" class="easyui-radio">
								<option value="">请选择</option>
								<%
									for( SysDiskImageVO sysDiskImageOption : sysDiskImageOptionsGZ )
									{
								%>
										<option value="<%=sysDiskImageOption.getId()%>"><%=sysDiskImageOption.getName()%></option>
								<%
									}
								%>
								
							</select>
						</div>
					 	<div id="region_4">
							<select id="sys_image_id_HK" name="sys_image_id_HK" style="width:225px" class="easyui-radio">
								<option value="">请选择</option>
 								<% 
 									for( SysDiskImageVO sysDiskImageOption : sysDiskImageOptionsHK )
 									{
 								%> 
 										<option value="<%=sysDiskImageOption.getId()%>"><%=sysDiskImageOption.getName()%></option> 
 								<% 
 									}
 								%> 
							</select>
						</div> 
					</td>
				</tr>
				
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;">描述：</td>
					<td style="padding:5px 0 5px 0;">
						<textarea name="description" id="description" rows="6" cols="44" style="resize:none;"></textarea>
                    </td>
					<td id="operator-tip-description"><i></i></td>
				</tr>
				
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;">库存总量：</td>
					<td style="padding:5px 0 5px 0;">
						<input type="text" id="total_amount" name="total_amount" style="position:relative; top:2px;" />
						个
					</td>
				</tr>
				
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;">马上创建：</td>
					<td>
						<label for="is_create">
							<input type="checkbox" id="is_create" name="is_create" value="true" checked="checked" style="position:relative; top:2px;" />
							是
						</label>
					</td>
				</tr>
				
				<tr>
					<td></td>
					<td>
						<div id="port_box" style="width:450px;">
						</div>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<div id="warehouse_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="warehouse_add_dlg_save_btn"> &nbsp;添&nbsp;加&nbsp; </a> 
		<a href="javascript:" class="easyui-linkbutton" id="warehouse_add_dlg_close_btn"> &nbsp;取&nbsp;消&nbsp; </a>
	</div>
</div>


<script type="text/javascript">
$(function(){
	document.getElementById("region_4").style.display = "none";
});

var _warehouse_add_dlg_scope_ = new function(){
	
	var self = this;
	
	self.checkLength = function(){
		var hostName = $("#host_name").val().trim();
		if(hostName==null || hostName==""){
			$("#tip-host-name").html("<b>主机名不能为空</b>");
			return false;
		}
		if( /^\w{1,32}$/.test(hostName)==false ){
			$("#tip-host-name").html("<b>主机名长度不能超过32个字符,且不能包含中文和特殊字符</b>");
			return false;
		}
		$("#tip-host-name").html("");
		return true;
	};
	
	self.onMove = function(){
		var thisId = "#warehouse_add_dlg";
		var topValue = $(thisId).offset().top;
		var leftValue = $(thisId).offset().left;
		if(topValue==0){
			topValue = 30;
		}
		if(topValue<30){
			$(thisId).dialog('move',{
				left:leftValue,
				top:1
			});
			return;
		}
	};
	
	// 保存
	self.save = function() {
		var formData = $.formToBean(warehouse_add_dlg_form);
		ajax.remoteCall("bean://cloudHostWarehouseService:addWarehouse", 
			[ formData ],
			function(reply) {
				if (reply.status=="exception")
				{
					if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
					{
						top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
							top.location.reload();
						});
					}
					else 
					{
						top.$.messager.alert("警告", reply.exceptionMessage, "warning");
					}
				}
				else if (reply.result.status == "success")
				{
					var data = $("#warehouse_add_dlg_container").parent().prop("_data_");
					$("#warehouse_add_dlg").dialog("close");
					data.onClose(reply.result);
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
		$("#warehouse_add_dlg").dialog("close");
	};

	//--------------------------
	
	(function() {
		// 选择“从镜像创建”或“空白系统”
		$(":radio[name=sys_disk_type]").change(function(){
			if( this.id=="create_from_img" ) {
				$("#sys_image_id").attr("disabled", false);
			} else {
				$("#sys_image_id").attr("disabled", true).val("");
			}
		});
		// 保存
		$("#warehouse_add_dlg_save_btn").click(function() {
			self.save();
		});
		// 关闭
		$("#warehouse_add_dlg_close_btn").click(function() {
			self.close();
		});
	})();
	
	self.changeSysImage = function(){
		var region = $("#region").val();
		if(region == 1){
			$("#sys_image_id_HK").val("");
			document.getElementById("region_4").style.display = "none";
			document.getElementById("region_1").style.display = "";
		}else if(region == 4){
			$("#sys_image_id_GZ").val("");
			document.getElementById("region_1").style.display = "none";
			document.getElementById("region_4").style.display = "";
		}
	};
};
</script>
