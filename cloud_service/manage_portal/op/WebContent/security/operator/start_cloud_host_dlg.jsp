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
	String method = StringUtil.trim(request.getParameter("method"));
	String cloudHostId = StringUtil.trim(request.getParameter("cloudHostId"));
	CloudHostVO cloudHostVO = (CloudHostVO)request.getAttribute("cloudHostVO");
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	List<IsoImageData> isoImages = IsoImagePoolManager.getIsoImagePool().getAllIsoImageDataByRegion(cloudHostVO.getRegion());
%>
<!-- start_cloud_host_dlg.jsp -->
<div id="start_cloud_host_dlg_container">

	<div id="start_cloud_host_dlg" class="easyui-dialog" title="启动方式"
		style="width:500px; height:200px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			modal: true,
			onMove: _start_cloud_host_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _start_cloud_host_dlg_scope_;
			}
		">
		<form id="start_cloud_host_dlg_form" method="post">
			
			<table border="0" width="100%" style="margin-top:20px;">
				<tr>
					<td align="right" width="150px">
						<label for="from_disk">
							<input type="radio" id="from_disk" name="start_from" checked="checked" />
							从硬盘启动：
						</label>
					</td>
					<td>
					</td>
				</tr>
				<tr>
					<td height="5px"></td>
				</tr>
				<tr>
					<td align="right" width="150px">
						<label for="from_iso">
							<input type="radio" id="from_iso" name="start_from" />
							从光盘启动：
						</label>
					</td>
					<td>
						<select id="realIsoImageId" name="realIsoImageId" style="width:230px;">
							<option value="">请选择光盘镜像</option>
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
						<a href="javascript:" class="easyui-linkbutton" id="start_cloud_host_dlg_start_btn"
							data-options="iconCls:'icon-tip'">&nbsp;启&nbsp;动&nbsp;</a>
						<a href="javascript:" class="easyui-linkbutton" id="start_cloud_host_dlg_close_btn"
							data-options="iconCls:'icon-undo'">&nbsp;关&nbsp;闭&nbsp;</a>
					</td>
				</tr>
			</table>
			
		</form>
	</div>

</div>


<script type="text/javascript">

//-----------------------------
var _start_cloud_host_dlg_scope_ = new function(){
	
	var self = this;
	
	self.onMove = function(){
		var thisId = "#start_cloud_host_dlg";
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
		// 从硬盘启动
		if( $("#from_disk:checked").length>0 )
		{
			var method = "<%=method%>";
			var callMethod = "";
			if( method=="startCloudHostPage" )
			{
				callMethod = "startCloudHost";
			}
			else if( method=="restartCloudHostPage" )
			{
				callMethod = "restartCloudHost";
			}
			else
			{
				alert("wrong method:["+method+"]");
			}
			
			ajax.remoteCall("bean://cloudHostService:"+callMethod, 
				[ "<%=cloudHostId%>" ],
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
						top.$.messager.alert("信息", reply.result.message, "info",function(){
							var data = $("#start_cloud_host_dlg_container").parent().prop("_data_");
							$("#start_cloud_host_dlg").dialog("close");
							data.onClose(reply.result);
						});
					}
					else
					{
						top.$.messager.alert('警告', reply.result.message, 'warning');
					}
				}
			);
		}
		// 从光盘启动
		else if( $("#from_iso:checked").length>0 )
		{
			var formData = $.formToBean(start_cloud_host_dlg_form);
			if( formData.realIsoImageId.trim()=="" )
			{
				top.$.messager.alert("警告", "请选择光盘镜像", "warning");
				return ;
			}
			
			var method = "<%=method%>";
			var callMethod = "";
			if( method=="startCloudHostPage" )
			{
				callMethod = "startCloudHostFromIsoImage";
			}
			else if( method=="restartCloudHostPage" )
			{
				callMethod = "restartCloudHostFromIsoImage";
			}
			else
			{
				alert("wrong method:["+method+"]");
			}
			
			ajax.remoteCall("bean://cloudHostService:"+callMethod, 
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
						top.$.messager.alert("信息", reply.result.message, "info",function(){
							var data = $("#start_cloud_host_dlg_container").parent().prop("_data_");
							$("#start_cloud_host_dlg").dialog("close");
							data.onClose(reply.result);
						});
					}
					else
					{
						top.$.messager.alert('警告', reply.result.message, 'warning');
					}
				}
			);
		}
	};

	// 关闭
	self.close = function() {
		$("#start_cloud_host_dlg").dialog("close");
	};

	//--------------------------
	
	$(document).ready(function() {
		// 保存
		$("#start_cloud_host_dlg_start_btn").click(function() {
			self.save();
		});
		// 关闭
		$("#start_cloud_host_dlg_close_btn").click(function() {
			self.close();
		});
	});
};
	
	
</script>



