<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.vo.SysUserVO"%>
<%@page import="java.util.List"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	String groupId = (String)request.getAttribute("groupId");
	List<SysUserVO> groupSysUserList = (List<SysUserVO>)request.getAttribute("groupSysUserList");
	Integer userType = Integer.valueOf(request.getParameter("userType"));	
%>
<div id="sys_group_set_member_dlg_container">

	<div id="sys_group_set_member_dlg" class="easyui-dialog" title="权限组组员设置" 
			style="
				width:1000px; 
				height:500px; 
				padding:10px;
			"
			data-options="
				iconCls: 'icon-edit',
				buttons: '#sys_group_set_member_dlg_buttons',
				modal: true,
				onMove:_sys_group_set_member_dlg_scope_.onMove,
				onClose: function(){
					$(this).dialog('destroy');
				},
				onDestroy: function(){
					delete _sys_group_set_member_dlg_scope_;
				}
			">
		<form id="sys_group_set_member_dlg_form" method="post">
		
			<input type="hidden" id="group_id" name="group_id" value="<%=groupId%>" />
		
			<table border="0" style="margin:20px auto 0 auto;">
				<tr>
					<td>
						<%-- 左边的表格 { --%>
						<table id="left_datagrid" class="easyui-datagrid" title="可选"  
							style="
								width:500px; 
								height:350px; 
								border:1px solid #ccc;
							"
							data-options="
								url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=sysUserService&method=querySysUserNotInGroupId',
								queryParams: {
									groupId: '<%=groupId%>'
								},
								toolbar: '#left_datagrid_toolbar',
								idField: 'id',
								rownumbers: true,
								fitColumns: true,
								striped: true,
								pagination: true,
								pageList: [10, 20, 50, 100, 200],
								pageSize: 10
							">
					        <thead>
					            <tr>
									<th data-options="field:'id', checkbox:true"></th>
					                <th data-options="field:'type'" formatter="_sys_group_set_member_dlg_scope_.formatUserType">类型</th>
					                <th data-options="field:'account'">账号</th>
					            </tr>
					        </thead>
					    </table>
					    
					    <div id="left_datagrid_toolbar" style="padding:3px; text-align:right;">
					    	
					    	<select id="left_user_type" name="left_user_type" style="position:relative; top:2px;">
					    		<option value="">全部</option>
					    		<option value="2">运营商</option>
					    		<option value="3">代理商</option>
					    		<option value="4">终端用户</option>
					    	</select>
					    	&nbsp;&nbsp;
					    	
							<span style="position: relative; top: 2px;">账号：</span> 
							<input type="text" id="left_account" style="position:relative; top:2px; height:18px;" /> 
							<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="left_query_btn">查询</a>
						</div>
						<%-- 左边的表格 } --%>
					</td>
					
					<%-- 中间的按钮部份 { --%>
					<td style="padding:0 5px 0 5px;">
						<a href="#" class="easyui-linkbutton" id="sys_group_set_member_dlg_select_btn"> 选择 >> </a>
						<div style="height:5px;"></div>
						<a href="#" class="easyui-linkbutton" id="sys_group_set_member_dlg_cancel_btn"> << 取消 </a>
					</td>
					<%-- 中间的按钮部份 } --%>
					
					<td>
						<%-- 右边的按钮部份 { --%>
						<table id="right_datagrid" class="easyui-datagrid" title="已选"  
							style="
								width:300px; 
								height:350px; 
								border:1px solid #ccc;
							"
							data-options="
								toolbar: '#right_datagrid_toolbar',
								idField: 'id',
								rownumbers: true,
								fitColumns: true,
								striped: true
							">
					        <thead>
					            <tr>
									<th data-options="field:'id', checkbox:true"></th>
					                <th data-options="field:'type'" formatter="_sys_group_set_member_dlg_scope_.formatUserType">类型</th>
					                <th data-options="field:'account'">账号</th>
					            </tr>
					        </thead>
					        <tbody>
					            <%
									for( SysUserVO sysUser : groupSysUserList )
									{
								%>
								<tr>
					                <td><%=sysUser.getId()%></td>
					                <td><%=sysUser.getType()%></td>
					                <td><%=sysUser.getAccount()%></td>
					            </tr>
								<%
									}
								%>
					        </tbody>
					    </table>
					    
					    <div id="right_datagrid_toolbar" style="height:30px; text-align:right;">
						</div>
						<%-- 右边的按钮部份 } --%>
					</td>
				</tr>
			</table>
		</form>
	</div>
	
	<div id="sys_group_set_member_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="sys_group_set_member_dlg_save_btn">
			&nbsp;提&nbsp;交&nbsp;
		</a>
		<a href="javascript:" class="easyui-linkbutton" id="sys_group_set_member_dlg_close_btn">
			&nbsp;关&nbsp;闭&nbsp;
		</a>
	</div>
</div>
	

<script type="text/javascript">

var _sys_group_set_member_dlg_scope_ = new function(){
	
	var self = this;
	self.onMove = function(){
		var thisId = "#sys_group_set_member_dlg";
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
	this.formatUserType = function(val)
	{
		if( val==1 ) {  
		    return "超级管理员";  
		} else if( val==2 ) {
		    return "运营商";  
		} else if( val==3 ) {
		 	return "代理商";
		} else if( val==4 ) {
		 	return "终端用户";
		} else {
			return "其它";
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
		var groupId = $("#sys_group_set_member_dlg_form #group_id").val();
		var selectedUserIds = $("#right_datagrid").datagrid("getRows").joinProperty("id");
		ajax.remoteCall("bean://sysGroupService:setGroupMembers",
			[groupId, selectedUserIds],
			function(reply)
			{
				if( reply.status=="exception" )
				{
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} 
				else if( reply.result.status=="success" )
				{
					var data = $("#sys_group_set_member_dlg_container").parent().prop("_data_");
					$("#sys_group_set_member_dlg").dialog("close");
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
		$("#sys_group_set_member_dlg").dialog("close");
	};
	
	// 初始化
	$(document).ready(function(){
		// 查询
		$("#left_query_btn").click(function(){
			var queryParams = {};
			queryParams.groupId = "<%=StringUtil.trim(groupId)%>";
			queryParams.type    = $("#left_user_type").val().trim();
			queryParams.account = $("#left_account").val().trim();
			$('#left_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		// 选择
		$("#sys_group_set_member_dlg_select_btn").click(function(){
			self.selectLeftToRight();
		});
		// 取消
		$("#sys_group_set_member_dlg_cancel_btn").click(function(){
			self.cancelRightToLeft();
		});
		// 保存
		$("#sys_group_set_member_dlg_save_btn").click(function(){
			self.save();
		});
		// 关闭
		$("#sys_group_set_member_dlg_close_btn").click(function(){
			self.close();
		});
	});
};

</script>
