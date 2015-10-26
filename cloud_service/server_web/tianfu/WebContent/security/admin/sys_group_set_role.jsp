<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.vo.SysRoleVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	
	String groupId = (String)request.getAttribute("groupId");
	List<SysRoleVO> notSelectedRoleList = (List<SysRoleVO>)request.getAttribute("notSelectedRoleList");
	List<SysRoleVO> selectedRoleList    = (List<SysRoleVO>)request.getAttribute("selectedRoleList");
%>

<div id="sys_group_add_dlg_container">
	<div id="sys_group_add_dlg" class="easyui-dialog" title="权限组角色设置" 
			style="
				width:600px; 
				height:500px; 
				padding:10px;
			"
			data-options="
				iconCls: 'icon-edit',
				buttons: '#sys_group_add_dlg_buttons',
				modal: true,
				onMove:_sys_group_add_dlg_scope_.onMove,
				onClose: function(){
					$(this).dialog('destroy');
				},
				onDestroy: function(){
					delete _sys_group_add_dlg_scope_;
				}
			">
		<form id="sys_group_add_dlg_form" method="post">
			
			<input type="hidden" id="group_id" name="group_id" value="<%=groupId%>" />
		
			<table border="0" style="margin:20px auto 0 auto;">
				<tr>
					<td>
						<table id="left_datagrid" class="easyui-datagrid" title="可选"  
							style="
								width:200px; 
								height:350px; 
								border:1px solid #ccc;
							"
							data-options="
								idField: 'id',
								rownumbers: true,
								fitColumns: true,
								striped: true
							">
					        <thead>
					            <tr>
									<th data-options="field:'id', checkbox:true"></th>
					                <th data-options="field:'roleName'">角色名</th>
					            </tr>
					        </thead>
					        <tbody>
					            <%
									for( SysRoleVO sysRole : notSelectedRoleList )
									{
								%>
								<tr>
					                <td><%=sysRole.getId()%></td>
					                <td><%=sysRole.getRoleName()%></td>
					            </tr>
								<%
									}
								%>
					        </tbody>
					    </table>
					</td>
					<td style="padding:0 5px 0 5px;">
						<a href="#" class="easyui-linkbutton" id="sys_group_add_dlg_select_link"> 选择 >> </a>
						<div style="height:5px;"></div>
						<a href="#" class="easyui-linkbutton" id="sys_group_add_dlg_cancel_link"> << 取消 </a>
					</td>
					<td>
						<table id="right_datagrid" class="easyui-datagrid" title="已选"  
							style="
								width:200px; 
								height:350px; 
								border:1px solid #ccc;
							"
							data-options="
								idField: 'id',
								rownumbers: true,
								fitColumns: true,
								striped: true
							">
					        <thead>
					            <tr>
									<th data-options="field:'id', checkbox:true"></th>
					                <th data-options="field:'roleName'">角色名</th>
					            </tr>
					        </thead>
					        <tbody>
					            <%
									for( SysRoleVO sysRole : selectedRoleList )
									{
								%>
								<tr>
					                <td><%=sysRole.getId()%></td>
					                <td><%=sysRole.getRoleName()%></td>
					            </tr>
								<%
									}
								%>
					        </tbody>
					    </table>
					</td>
				</tr>
			</table>
		</form>
	</div>
	
	<div id="sys_group_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="sys_group_add_dlg_save_btn">
			&nbsp;提&nbsp;交&nbsp;
		</a>
		<a href="javascript:" class="easyui-linkbutton" id="sys_group_add_dlg_close_btn">
			&nbsp;关&nbsp;闭&nbsp;
		</a>
	</div>
</div>
	

<script type="text/javascript">

var _sys_group_add_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#sys_group_add_dlg";
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
	// 选择
	this.selectLeftToRight = function()
	{
		var $leftDatagrid = $("#left_datagrid");
		var leftRows = $leftDatagrid.datagrid("getSelections");
		if( leftRows.length==0 )
		{
			return ;
		}
		// 添加到右边
		var rightRows = $("#right_datagrid").datagrid("getRows");
		var newRows = rightRows.concat(leftRows);
		$("#right_datagrid").datagrid("loadData", newRows);
		
		// 删除左边的
		for( var i=leftRows.length-1; i>=0; i-- )
		{
			var row = leftRows[i];
			var rowIndex = $leftDatagrid.datagrid("getRowIndex", row);
			$leftDatagrid.datagrid("deleteRow", rowIndex);
		}
	};
	
	// 取消
	self.cancelRightToLeft = function()
	{
		var $rightDatagrid = $("#right_datagrid");
		var rightRows = $rightDatagrid.datagrid("getSelections");
		if( rightRows.length==0 )
		{
			return ;
		}
		// 添加到右边
		var leftRows = $("#left_datagrid").datagrid("getRows");
		var newRows = leftRows.concat(rightRows);
		$("#left_datagrid").datagrid("loadData", newRows);
		
		// 删除左边的
		for( var i=rightRows.length-1; i>=0; i-- )
		{
			var row = rightRows[i];
			var rowIndex = $rightDatagrid.datagrid("getRowIndex", row);
			$rightDatagrid.datagrid("deleteRow", rowIndex);
		}
	};
	
	// 保存
	this.save = function()
	{
		var groupId = $("#group_id").val();
		var selectedRoleIds = $("#right_datagrid").datagrid("getRows").joinProperty("id");
		ajax.remoteCall("bean://sysGroupService:updateRoleFromGroupItem",
			[groupId, selectedRoleIds],
			function(reply)
			{
				if( reply.status=="exception" )
				{
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} 
				else if( reply.result.status=="success" )
				{
					var data = $("#sys_group_add_dlg_container").parent().prop("_data_");
					$("#sys_group_add_dlg").dialog("close");
					data.onClose(reply.result);
					top.$.messager.alert("返回结果", reply.result.message);
				}
				else
				{
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	};
	
	// 关闭
	this.close = function()
	{
		$("#sys_group_add_dlg").dialog("close");
	};
	
	// 初始化
	$(document).ready(function(){
		// 选择
		$("#sys_group_add_dlg_select_link").click(function(){
			self.selectLeftToRight();
		});
		// 取消
		$("#sys_group_add_dlg_cancel_link").click(function(){
			self.cancelRightToLeft();
		});
		// 保存
		$("#sys_group_add_dlg_save_btn").click(function(){
			self.save();
		});
		// 关闭
		$("#sys_group_add_dlg_close_btn").click(function(){
			self.close();
		});
	});
};

</script>
