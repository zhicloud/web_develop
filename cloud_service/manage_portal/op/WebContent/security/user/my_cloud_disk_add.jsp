<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%> 
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	String userId  = loginInfo.getUserId();
%>

<!-- my_cloud_disk_add.jsp -->
<div id="add_cloud_disk_dlg_container">

	<div id="add_cloud_disk_dlg" class="easyui-dialog" title="创建云硬盘"
		style="width:580px; height:250px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#add_cloud_disk_dlg_buttons',
			modal: true,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _add_cloud_disk_dlg_scope_;
			}
		">
		
		<form id="add_cloud_disk_dlg_form" method="post">
			<div >
				<div class="panel-title">基础配置</div>
			</div>
			<input id="userId" name="userId" type="hidden" value="<%=userId%>"/>
			<input id="account" name="account" type="hidden" value="<%=loginInfo.getAccount()%>"/>
			<table border="0" style="margin:0 auto 0 auto;"> 
					<tr>
						<td style="font-weight:bolder;">
							地域：
						</td>
					<td >
                    <input name="region" id="region" type="radio" value="1" class="checkbox"  checked="checked"/>
                    <label class="labelport" for="1"> 广州 </label>
                    </td>
                    <td >
                    <input name="region" id="region" type="radio" value="2" class="checkbox" />
                    <label class="labelport" for="2"> 北京 </label>
                    </td>
                    <td >
                    <input name="region" id="region" type="radio" value="3" class="checkbox"/>
                    <label class="labelport" for="3"> 成都 </label>
                    </td>
                    <td>
                    <input name="region" id="region" type="radio" value="4" class="checkbox" />
                    <label class="labelport" for="4">香港 </label>
  					</td>
  					</tr>
                   
                      <tr>
						<td style="font-weight:bolder;">
							硬盘：
						</td>
					<td >
                    <input name="disk" id="disk" type="radio" value="20" class="checkbox"  checked="checked"/>
                    <label class="labelport" for="1"> 20G </label>
                    </td>
                    <td >
                    <input  name="disk" id="disk" type="radio" value="50" class="checkbox"/>
                    <label class="labelport" for="2"> 50G </label>
                    </td>
                    <td >
                    <input name="disk" id="disk" type="radio" value="100" class="checkbox"/>
                    <label class="labelport" for="3"> 100G </label>
                    </td>
                    <td >
                    <input name="disk" id="disk" type="radio" value="200" class="checkbox"/>
                    <label class="labelport" for="4"> 200G </label>
                    </td>
                    <td >
                    <input name="disk" id="disk" type="radio" value="500" class="checkbox"/>
                    <label class="labelport" for="5"> 500G </label>
                    </td>
                    </tr>
			</table>
		</form>
		
	</div>
	
	<div id="add_cloud_disk_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="add_cloud_disk_dlg_save_btn">&nbsp;创&nbsp;建&nbsp;</a> 
		<a href="javascript:" class="easyui-linkbutton" id="add_cloud_disk_dlg_close_btn">&nbsp;取&nbsp;消&nbsp;</a>
	</div>
	
</div>
<script type="text/javascript">
 

var _add_cloud_disk_dlg_scope_ = new function(){
	
	var self = this;
	
	// 保存
	self.save = function() {
		var formData = $.formToBean(add_cloud_disk_dlg_form);
		ajax.remoteCall("bean://cloudDiskService:addCloudDisk", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception")
				{
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				}
				else if (reply.result.status == "success")
				{
					top.$.messager.alert('提示','创建成功','info',function(){ 
						var data = $("#add_cloud_disk_dlg_container").parent().prop("_data_");
						$("#add_cloud_disk_dlg").dialog("close");
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
		$("#add_cloud_disk_dlg").dialog("close");
	};

	//--------------------------

	$(document).ready(function(){
		// 保存
		$("#add_cloud_disk_dlg_save_btn").click(function() { 
				self.save(); 
		});
		// 关闭
		$("#add_cloud_disk_dlg_close_btn").click(function() {
			self.close();
		});
	});
};
</script>
</html>



