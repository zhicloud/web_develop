<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.CloudDiskVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%> 
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	CloudDiskVO cloudDiskVO = (CloudDiskVO) request.getAttribute("cloudDiskVO");
%>

<!-- my_cloud_disk_mod.jsp -->

<div id="mod_cloud_disk_dlg_container">

	<div id="mod_cloud_disk_dlg" class="easyui-dialog" title="修改配置"
		style="width:480px; height:150px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#mod_cloud_disk_dlg_buttons',
			modal: true,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _mod_cloud_disk_dlg_scope_;
			}
		">
		
		<form id="mod_cloud_disk_dlg_form" method="post">
			<input id = "id" name = "id" type="hidden" value="<%=cloudDiskVO.getId()%>">
			<table border="0" style="margin:0 auto 0 auto;"> 
                   
                      <tr>
						<td style="font-weight:bolder;">
							硬盘：
						</td>
					<td >
                    <input name="disk" id="disk_1" type="radio" value="20" class="checkbox"/>
                    <label class="labelport" for="1"> 20G </label>
                    </td>
                    <td >
                    <input  name="disk" id="disk_2" type="radio" value="50" class="checkbox"/>
                    <label class="labelport" for="2"> 50G </label>
                    </td>
                    <td >
                    <input name="disk" id="disk_3" type="radio" value="100" class="checkbox"/>
                    <label class="labelport" for="3"> 100G </label>
                    </td>
                    <td >
                    <input name="disk" id="disk_4" type="radio" value="200" class="checkbox"/>
                    <label class="labelport" for="4"> 200G </label>
                    </td>
                    <td >
                    <input name="disk" id="disk_5" type="radio" value="500" class="checkbox"/>
                    <label class="labelport" for="5"> 500G </label>
                    </td>
                    </tr>
			</table>
		</form>
		
	</div>
	
	<div id="mod_cloud_disk_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="mod_cloud_disk_dlg_save_btn">&nbsp;确&nbsp;认&nbsp;</a> 
		<a href="javascript:" class="easyui-linkbutton" id="mod_cloud_disk_dlg_close_btn">&nbsp;取&nbsp;消&nbsp;</a>
	</div>
	
</div>
<script type="text/javascript">

function checked()	{   
	
	
	if(CapacityUtil.toCapacityLabel(<%=cloudDiskVO.getDisk()%>) == "20.00GB"){
		document.getElementById("disk_1").checked = true;
	}
	if(CapacityUtil.toCapacityLabel(<%=cloudDiskVO.getDisk()%>) == "50.00GB"){
		document.getElementById("disk_2").checked = true;
	}
	if(CapacityUtil.toCapacityLabel(<%=cloudDiskVO.getDisk()%>) == "100.00GB"){
		document.getElementById("disk_3").checked = true;
	}
	if(CapacityUtil.toCapacityLabel(<%=cloudDiskVO.getDisk()%>) == "200.00GB"){
		document.getElementById("disk_4").checked = true;
	}
	if(CapacityUtil.toCapacityLabel(<%=cloudDiskVO.getDisk()%>) == "500.00GB"){
		document.getElementById("disk_5").checked = true;
	}
}  

var _mod_cloud_disk_dlg_scope_ = new function(){
	
	var self = this;
	
	// 保存
	self.save = function() {
		var formData = $.formToBean(mod_cloud_disk_dlg_form);
		ajax.remoteCall("bean://cloudDiskService:updateCloudDiskById", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception")
				{
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				}
				else if (reply.result.status == "success")
				{
					top.$.messager.alert('提示','修改成功','info',function(){ 
						var data = $("#mod_cloud_disk_dlg_container").parent().prop("_data_");
						$("#mod_cloud_disk_dlg").dialog("close");
						data.onClose(reply.result);
					});
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
		$("#mod_cloud_disk_dlg").dialog("close");
	};

	//--------------------------

	$(document).ready(function(){
		
		checked();
		//更新
		$("#mod_cloud_disk_dlg_save_btn").click(function() { 
				self.save(); 
		});
		// 关闭
		$("#mod_cloud_disk_dlg_close_btn").click(function() {
			self.close();
		});
	});
};
</script>
</html>



