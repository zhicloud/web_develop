<%@page import="java.util.List"%>
﻿<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="com.zhicloud.op.vo.CloudDiskVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	String terminalUserId = (String)request.getAttribute("terminalUserId");
// 	List<CloudHostVO> cloudHostList = (List<CloudHostVO>)request.getAttribute("cloudHostList");
// 	List<CloudDiskVO> cloudDiskList = (List<CloudDiskVO>)request.getAttribute("cloudDiskList");
%>

<div id="terminal_user_view_item_dlg_container">
	<div id="terminal_user_view_item_dlg" class="easyui-dialog" title="产品列表" 
			style="
				width:900px; 
				height:600px; 
			"
			data-options="
				buttons: '#terminal_user_view_item_dlg_buttons',
				modal: true,
				onMove:_terminal_user_view_item_dlg_scope_.onMove,
				onClose: function(){
					$(this).dialog('destroy');
				},
				onDestroy: function(){
					delete _terminal_user_view_item_dlg_scope_;
				}
			">
			<table id="terminal_user_view_item_datagrid" class="easyui-datagrid" title=""  
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=terminalUserService&method=viewItem&terminalUserId=<%=terminalUserId%>',
					pagination: false,
					rownumbers: true,
					fitColumns: true,
					striped: true,
					onLoadSuccess: onLoadSuccess
				">
		        <thead>
		            <tr>
		            	<th field="displayName" width="15%">主机名</th>
		            	<th field="region" formatter="formatRegion" width="5%">地域</th>
		            	<th field="status" formatter="formatStatus" width="10%">状态</th>
		            	<th field="cpuCore" formatter="formatCpuCore" width="5%">CPU核数</th>
		            	<th field="memory" formatter="formatCapacity" width="5%">内存</th>
		            	<th field="sysDisk" formatter="formatCapacity" width="10%">系统磁盘</th>
		            	<th field="dataDisk" formatter="formatCapacity" width="10%">数据磁盘</th>
		            	<th field="bandwidth" formatter="formatFlow" width="10%">网络带宽</th>
		            	<th field="innerIp" formatter="formatIp" width="15%">内部监控地址</th>
		            	<th field="outerIp" formatter="formatIp" width="15%">外部监控地址</th>
		            	<th field="operator" formatter="terminalUserColumnFormatter" width="15%">操作</th>
<!-- 						<th data-options="field:'id'" width="5%">ID</th> -->
<!-- 						<th data-options="field:'hostName'" width="15%">主机名</th> -->
<!-- 						<th data-options="field:'status'" width="5%">状态</th> -->
<!-- 		                <th data-options="field:'cpuCore'" width="5%">CPU核数</th> -->
<!-- 		                <th data-options="field:'memory'" width="5%">内存</th> -->
<!-- 		                <th data-options="field:'sysDisk'" width="10%">系统磁盘</th> -->
<!-- 		                <th data-options="field:'dataDisk'" width="10%">数据磁盘</th> -->
<!-- 		                <th data-options="field:'bandwidth'" width="10%">网络带宽</th> -->
<!-- 		                <th data-options="field:'innerIp'" width="15%">内部监控地址</th> -->
<!-- 		                <th data-options="field:'outerIp'" width="15%">外部监控地址</th> -->
<!-- 		                <th data-options="field:'operator'" formatter="terminalUserColumnFormatter" width="20%">操作</th> -->
		            </tr>
		        </thead>
		        <%-- <tbody>
		            <%
						for(CloudHostVO cloudHost : cloudHostList)
						{
					%>
					<tr>
		                <td><%=cloudHost.getId()%></td>
		                <td><%=cloudHost.getHostName()%></td>
		                <td><%=cloudHost.getSummarizedStatusText()%></td>
		                <td><%=cloudHost.getCpuCore()%>核</td>
		                <td><%=cloudHost.getMemoryText(0)%></td>
		                <td><%=cloudHost.getSysDiskText(0)%></td>
		                <td><%=cloudHost.getDataDiskText(0)%></td>
		                <td><%=cloudHost.getBandwidthText(0)%></td>
		                <td><%=cloudHost.getInnerIp()==null?"无":cloudHost.getInnerIp()%></td>
		                <td><%=cloudHost.getOuterIp()==null?"无":cloudHost.getOuterIp()%></td>
		            </tr>
					<%
						}
					%>
		        </tbody> --%>
		    </table>
	</div>
</div>
	

<script type="text/javascript">
// function hostStatusFormatter(val)
// {  
// 	if(val == 1) {  
// 	    return "是";  
// 	} else {
// 	 	return "否";
// 	}
// }
function formatStatus(value, row, index)
{
	var status = $("#terminal_user_view_item_datagrid").datagrid("getData").rows[index].status;
	var realHostId = $("#terminal_user_view_item_datagrid").datagrid("getData").rows[index].realHostId;
	var runningStatus = $("#terminal_user_view_item_datagrid").datagrid("getData").rows[index].runningStatus;
	var processStatus = $("#terminal_user_view_item_datagrid").datagrid("getData").rows[index].processStatus;
	if(status==2){
		return "停机";
	}
	if(status==1 && runningStatus==2){
		return "运行";
	}
	if(processStatus != null && (processStatus == 0 || processStatus == 3)){
		return "创建中";
	}
	if(processStatus!=null && processStatus==2){
		return "创建失败";
	}
	if(status==1 && runningStatus==1){
		return "关机";
	}
	if(realHostId==null){
		return "创建中";
	}
	return "";
	
}
function formatIp(val)
{
	if(val==null){
		return "无";
	}
	return val;
}

function formatRegion(val)
{
	if(val==1){
		return "广州";
	}else if(val==2){
		return "成都";
	}else if(val==3){
		return "北京";
	}
	return "香港";
}
function formatCpuCore(val)
{
	return val+"核";
}

function formatCapacity(val)
{
	return CapacityUtil.toCapacityLabel(val, 0);
}

function formatFlow(val)
{
	return FlowUtil.toFlowLabel(val, 0);
}
function terminalUserColumnFormatter(value, row, index)
{
	var status = $("#terminal_user_view_item_datagrid").datagrid("getData").rows[index].status;
	var realHostId = $("#terminal_user_view_item_datagrid").datagrid("getData").rows[index].realHostId;
	var runningStatus = $("#terminal_user_view_item_datagrid").datagrid("getData").rows[index].runningStatus;
	var processStatus = $("#terminal_user_view_item_datagrid").datagrid("getData").rows[index].processStatus;
	if(status==2){
		return "<div row_index='"+index+"'>\
				<font color='grey'>停用</font>\
				<a href='#' class='datagrid_row_linkbutton reactivate_btn'>恢复</a>\
				<font color='grey'>修改配置</font>\
			</div>";
	}
	if(processStatus != null && (processStatus == 0 || processStatus == 3)){
		return "<div row_index='"+index+"'>\
		<font color='grey'>停用</font>\
		<font color='grey'>恢复</font>\
		<font color='grey'>修改配置</font>\
	</div>";
	}
	if(processStatus!=null && processStatus==2){
		return "<div row_index='"+index+"'>\
			<a href='#' class='datagrid_row_linkbutton delete_btn'>删除</a>\
		</div>";
	}
	if(status==1 && runningStatus==1){
		return "<div row_index='"+index+"'>\
		<a href='#' class='datagrid_row_linkbutton inactivate_btn'>停用</a>\
		<font color='grey'>恢复</font>\
		<a href='#' class='datagrid_row_linkbutton modify_allocation_btn'>修改配置</a>\
	</div>";
	}
	if(realHostId==null){
		return "<div row_index='"+index+"'>\
		<font color='grey'>停用</font>\
		<font color='grey'>恢复</font>\
		<font color='grey'>修改配置</font>\
	</div>";
	}
	return "<div row_index='"+index+"'>\
			<a href='#' class='datagrid_row_linkbutton inactivate_btn'>停用</a>\
			<font color='grey'>恢复</font>\
			<font color='grey'>修改配置</font>\
		</div>";
}
function onLoadSuccess()
{
	//每一行的"删除"按纽
	$("a.delete_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#terminal_user_view_item_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		if( id=="" )
		{
			return;
		}
		top.$.messager.confirm("确认", "确定删除?", function (r) {  
	        if (r) {  
				ajax.remoteCall("bean://cloudHostService:deleteCloudHostByIds",
					[ [id] ], 
					function(reply) {
						if (reply.status=="exception")
						{
							if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
    						{
    							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
    								window.location.reload();
    							});
    						}
    						else 
    						{
    							top.$.messager.alert("警告", reply.exceptionMessage, "warning");
    						}
						}
						else
						{
							$('#terminal_user_view_item_datagrid').datagrid('reload');
						}
					}
				);
	        }  
	    }); 
	});
	//每一行的'停机'按钮
	$("a.inactivate_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#terminal_user_view_item_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		if( id=="" )
		{
			return;
		}
		top.$.messager.confirm("确认", "确定要停用该云主机吗？", function (r) { 
			if(r){
				ajax.remoteCall("bean://cloudHostService:inactivateCloudHost",
					[ id ], 
					function(reply) {
						if (reply.status=="exception")
						{
							if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
							{
								top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
									window.location.reload();
								});
							}
							else 
							{
								top.$.messager.alert("警告", reply.exceptionMessage, "warning");
							}
						}
						else
						{
							top.$.messager.alert("信息", reply.result.message, "info",function(){
								$('#terminal_user_view_item_datagrid').datagrid('reload');
							});
						}
					}
				);
			}
		});
		
	});
	// 每一行的'恢复'按钮
	$("a.reactivate_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#terminal_user_view_item_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		if( id=="" )
		{
			return;
		}
		top.$.messager.confirm("确认", "确定要恢复该云主机吗？", function (r) { 
			if(r){
				ajax.remoteCall("bean://cloudHostService:reactivateCloudHost",
						[ id ], 
						function(reply) {
							if (reply.status=="exception")
							{
								if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
								{
									top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
										window.location.reload();
									});
								}
								else 
								{
									top.$.messager.alert("警告", reply.exceptionMessage, "warning");
								}
							}
							else
							{
								top.$.messager.alert("信息", reply.result.message, "info",function(){
									$('#terminal_user_view_item_datagrid').datagrid('reload');
								});
							}
						}
					);
			}
		});
		
	});
	// 每一行的'修改配置'按钮
	$("a.modify_allocation_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#terminal_user_view_item_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=modifyAllocationPage&hostId="+encodeURIComponent(id),
			onClose: function(data){
				$('#terminal_user_view_item_datagrid').datagrid('reload');
			}
		});
	});
}
var _terminal_user_view_item_dlg_scope_ = new function(){
	
	var self = this;
	
	self.onMove = function(){
		var thisId = "#terminal_user_view_item_dlg";
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
	this.save = function()
	{
		/* var groupId = $("#group_id").val();
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
					var data = $("#terminal_user_view_item_dlg_container").parent().prop("_data_");
					$("#terminal_user_view_item_dlg").dialog("close");
					data.onClose(reply.result);
					top.$.messager.alert("返回结果", reply.result.message);
				}
				else
				{
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		); */
	};
	
	// 关闭
	this.close = function()
	{
		$("#terminal_user_view_item_dlg").dialog("close");
	};
	
	// 初始化
};

</script>
