<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.app.pool.IsoImagePool.IsoImageData"%>
<%@page import="com.zhicloud.op.app.pool.IsoImagePoolManager"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.vo.CloudHostOpenPortVO"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	String cloudHostId = StringUtil.trim(request.getParameter("cloudHostId"));
	CloudHostVO cloudHostVO = (CloudHostVO)request.getAttribute("cloudHostVO");
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	List<IsoImageData> isoImages = IsoImagePoolManager.getIsoImagePool().getAllIsoImageDataByRegion(cloudHostVO.getRegion());
%>
<!-- insert_iso_image.jsp -->
<div id="operator_self_use_cloud_host_insert_iso_image_dlg_container">

	<div id="operator_self_use_cloud_host_insert_iso_image_dlg" class="easyui-dialog" title="加载光盘"
		style="width:470px; height:200px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			modal: true,
			onMove: _operator_self_use_cloud_host_insert_iso_image_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _operator_self_use_cloud_host_insert_iso_image_dlg_scope_;
			}
		">
		<form id="operator_self_use_cloud_host_insert_iso_image_dlg_form" method="post">
			
			<table border="0" width="100%" style="margin-top:40px;">
				<tr>
					<td align="right" width="150px">请选择光盘镜像：</td>
					<td>
						<select id="realIsoImageId" name="realIsoImageId" style="width:200px;">
							<option></option>
							<%
								for( IsoImageData isoImage : isoImages )
								{
							%>
							<option value="<%=isoImage.getRealImageId()%>"><%=isoImage.getName()%></option>
							<%
								}
							%>
						</select>
					</td>
				</tr>
				<tr>
					<td height="30px"></td>
				</tr>
				<tr>
					<td colspan="99" align="right">
						<a href="javascript:" class="easyui-linkbutton" id="operator_self_use_cloud_host_insert_iso_image_dlg_save_btn"
							data-options="iconCls:'icon-redo'">&nbsp;加&nbsp;载&nbsp;</a>
						<a href="javascript:" class="easyui-linkbutton" id="operator_self_use_cloud_host_insert_iso_image_dlg_close_btn"
							data-options="iconCls:'icon-undo'">&nbsp;关&nbsp;闭&nbsp;</a>
					</td>
				</tr>
			</table>
			
		</form>
	</div>

</div>


<script type="text/javascript" src="<%=request.getContextPath()%>/js/esl.js"></script>
<script type="text/javascript">

//-----------------------------
var _operator_self_use_cloud_host_insert_iso_image_dlg_scope_ = new function(){
	
	var self = this;
	
	self.onMove = function(){
		var thisId = "#operator_self_use_cloud_host_insert_iso_image_dlg";
		var topValue = $(thisId).offset().top;
		var leftValue = $(thisId).offset().left;
		if( topValue==0 ) {
			topValue = 30;
		}
		if( topValue<30 || topValue>600 || leftValue>1315 ) {
			$(thisId).dialog('move',{
				left:460,
				top:145
			});
			return;
		}
	};
	
	// 保存
	self.save = function() {
		var formData = $.formToBean(operator_self_use_cloud_host_insert_iso_image_dlg_form);
		ajax.remoteCall("bean://cloudHostService:insertIsoImage", 
			[ "<%=cloudHostId%>", formData.realIsoImageId ],
			function(reply) {
				if( reply.status == "exception" )
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
					var data = $("#operator_self_use_cloud_host_insert_iso_image_dlg_container").parent().prop("_data_");
					$("#operator_self_use_cloud_host_insert_iso_image_dlg").dialog("close");
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
		$("#operator_self_use_cloud_host_insert_iso_image_dlg").dialog("close");
	};

	//--------------------------
	
	$(document).ready(function() {
		// 保存
		$("#operator_self_use_cloud_host_insert_iso_image_dlg_save_btn").click(function() {
			self.save();
		});
		// 关闭
		$("#operator_self_use_cloud_host_insert_iso_image_dlg_close_btn").click(function() {
			self.close();
		});
	});
};
	
	
</script>



