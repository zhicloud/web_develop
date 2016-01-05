<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.vo.CloudHostOpenPortVO"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	CloudHostVO cloudHost  = (CloudHostVO)request.getAttribute("cloudHost");
	Integer progress       = (Integer)request.getAttribute("progress");
	Boolean creationStatus = (Boolean)request.getAttribute("creation_status");
%>
<!-- cloud_host_creation_progress.jsp -->
<div id="cloud_host_view_detail_dlg_container">
	<div id="cloud_host_view_detail_dlg" class="easyui-dialog" title="云主机创建进度"
		style="width:300px; height:200px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			modal: true,
			onMove: self.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				JsUtil.doDelete(_cloud_host_view_detail_dlg_scope_);
			}
		">
		<form id="cloud_host_view_detail_dlg_form" method="post">
			<div id="progressbar" class="easyui-progressbar" value="<%=progress==null ? 0 : progress%>" 
				style="width:200px; margin-left:30px; margin-top:40px;"></div>
		</form>
	</div>

</div>


<script type="text/javascript">
//-----------------------------
var _cloud_host_view_detail_dlg_scope_ = new function(){
	
	var self = this;
	
	
	self.move = function()
	{
		var thisId = "#cloud_host_view_detail_dlg";
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
	

	// 关闭
	self.close = function() 
	{
		$("#cloud_host_view_detail_dlg").dialog("close");
	};
	
	// 刷新progress
	self.refreshProgress = function()
	{
		ajax.remoteCall("bean://cloudHostService:getCloudHostCreationResult", 
			[ "<%=cloudHost.getId()%>" ],
			function(reply) {
				if( reply.status == "exception" ) 
				{
					top.$.messager.alert('警告', reply.exceptionMessage, 'warning');
				} 
				else if(reply.result.status == "success") 
				{
					$('#progressbar').progressbar('setValue', parseInt(reply.result.properties.progress));
					if( self.refreshProgress==null )
					{
						return ;
					}
					if( reply.result.properties.creation_status==null )
					{
						window.setTimeout(self.refreshProgress, 1000);
					}
					else if( reply.result.properties.creation_status==false )
					{
						$("#cloud_host_view_detail_dlg_form").html("<div style='text-align:center; padding-top:40px;'>创建失败</div>");
					}
					else if( reply.result.properties.creation_status==true )
					{
						$("#cloud_host_view_detail_dlg_form").html("<div style='text-align:center; padding-top:40px;'>创建成功</div>");
					}
				} 
				else 
				{
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	};

	//--------------------------
	
	$(document).ready(function() {
		// 关闭
		$("#cloud_host_view_detail_dlg_close_btn").click(function() {
			self.close();
		});
		// 刷新progress
		var creationStatus = <%=creationStatus%>;
		if( creationStatus==null )
		{
			window.setTimeout(self.refreshProgress, 1000);
		}
		else
		{
			var creationStatusLabel = (creationStatus==true) ? "创建成功" : "创建失败";
			$("#cloud_host_view_detail_dlg_form").html("<div style='text-align:center; padding-top:40px;'>"+creationStatusLabel+"</div>");
		}
	});
};
</script>



