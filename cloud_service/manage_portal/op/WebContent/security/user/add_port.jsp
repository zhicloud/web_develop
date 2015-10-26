<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.CloudHostOpenPortVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	List<CloudHostOpenPortVO> dataList = (List<CloudHostOpenPortVO>)request.getAttribute("data");
%>

<!-- add_port.jsp -->

<div id="cloud_host_add_port_dlg_container">

	<div id="cloud_host_add_port_dlg" class="easyui-dialog" title="云主机开放端口维护"
		style="width:700px; height:450px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#cloud_host_add_port_dlg_buttons',
			modal: true,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _cloud_host_add_port_dlg_scope_;
			}
		">
		
		<form id="cloud_host_add_port_dlg_form" method="post">
			
			<input type="hidden" id="host_id" name="host_id" value="<%=request.getAttribute("host_id")%>" />
			<input type="hidden" id="delete_port" name="delete_port" />
			
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;"> 
					<div>
							开放端口：<input id="port" name="port" maxlength="5" style="width:50px"/>
							<select id = "protocol" name="protocol">
								<option value = "0">所有协议</option>
								<option value = "1">TCP</option>
								<option value = "2">UDP</option>
							</select> 
							<a href="javascript:void(0);" class="easyui-linkbutton" id="add_port">添加</a> 
						</div>
				</div>
			</div>
			
			<table id="cloud_host_port_datagrid" class="easyui-datagrid"
				style="height:514px;border:1px solid #ccc;"
				 data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=2&bean=cloudHostService&method=queryCloudHostPorts&hostId=<%=request.getAttribute("host_id")%>',
					fitColumns: true,
					rownumbers: true,
					striped: true ,
					selectOnCheck: true,
					onLoadSuccess:function(data){                    
				        if(data){
				            $.each(data.rows, function(index, item){ 
				                $('#cloud_host_port_datagrid').datagrid('checkRow', index); 
				            });
				        }
				    }         
					"> 
				<thead>
				    
					<tr>
						<th data-options="checkbox:true"></th>
						<th  data-options="field:'protocol',width:100,formatter:formatProtocol" width="200px">协议</th>
						<th  data-options="field:'port',width:100" width="100px" >端口</th> 
						<th  data-options="field:'outerPort',width:100" width="100px" >外网端口</th> 
						<th   data-options="field:'keyValue',width:100" width="100" hidden="true"  ></th>
					</tr>
				</thead>
			</table>
			
			
			
 
		</form>
	</div>

	<div id="cloud_host_add_port_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="cloud_host_add_port_dlg_save_btn"> &nbsp;保&nbsp;存&nbsp; </a> 
		<a href="javascript:" class="easyui-linkbutton" id="cloud_host_add_port_dlg_close_btn"> &nbsp;取&nbsp;消&nbsp; </a>
	</div>
</div>


<script type="text/javascript">

var _cloud_host_add_port_dlg_scope_ = new function(){
	
	var self = this;
	var deletePort = new Array();
	
	// 添加开放端口
	self.addPort = function()
	{
		var port = $("#port").val();
		var protocol = $("#protocol").val();
		var protocol_str;
		var val = protocol+"&"+port;
		
		if(protocol == 0)
		{
			protocol_str="所有协议";
		}
		else if(protocol == 1)
		{
			protocol_str="TCP";
		}
		else
		{
			protocol_str="UDP";
		}
		
		if( port.trim()=="" )
		{
			return ;
		}
		var data = $('#cloud_host_port_datagrid').datagrid('getData'); 
		var total = data.total;
		for(var i = 0;i<total;i++){
			 
	  		  if(val == $("#cloud_host_port_datagrid").datagrid("getData").rows[i].protocol+"&"+$("#cloud_host_port_datagrid").datagrid("getData").rows[i].port){
	  			top.$.messager.alert('提示','该端口已存在','info',function(){
					$("#port").val(""); 
					return false;
				});
	  		  }
		}
		
		$('#cloud_host_port_datagrid').datagrid('appendRow',{
 			protocol: protocol,
			port: port,
			outerPort: '未分配',
			keyValue: protocol+"&"+port
		});
		 $('#cloud_host_port_datagrid').datagrid('checkRow', total); 
		 $("#port").val("");
		 $("#protocol").val("0");
 		
	
	};
	
	// 保存
	self.save = function() {
		var formData          = $.formToBean(cloud_host_add_port_dlg_form);
		var rows = $('#cloud_host_port_datagrid').datagrid('getSelections'); 
		formData.ports = rows.joinProperty("keyValue");
 		ajax.remoteCall("bean://cloudHostService:addPort", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception")
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
				else if (reply.result.status == "success")
				{
					$("#cloud_host_add_port_dlg").dialog("close");
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
		$("#cloud_host_add_port_dlg").dialog("close");
	};

	//--------------------------
	
	$(document).ready(function() {
			
		// 添加端口
		$("#add_port").click(function(){
			var port = $("#port").val();
			if(port==""){
				top.$.messager.alert("提示","端口号不能为空","info",function(){
					$("#port").val("");
				});
				return;
			}
			if(!(/^[1-9][0-9]*$/.test(port))){
				top.$.messager.alert("提示","端口号只能是数字","info",function(){
					$("#port").val("");
				});
			}else{
				self.addPort();
			}
		});
		
		$("#port_box a.delete_port").click(function(){
			var portElement = this;
			top.$.messager.confirm('操作确认', '确定要删除吗?', function(result){
                if( result ) {
                	deletePort.push($(portElement).parent().children("span.port_text").text());
                	document.getElementById("delete_port").value=deletePort;
                	$(portElement).parent().remove();
                }
            });
		});
		
		// 保存
		$("#cloud_host_add_port_dlg_save_btn").click(function() {
			self.save();
		});
		// 关闭
		$("#cloud_host_add_port_dlg_close_btn").click(function() {
			self.close();
		});
	});
	
};

function  formatProtocol(value, row, index)
{
	if(value == '0'){
		return "所有协议";
	}else if(value == '1'){
		return "TCP";
	}else if(value == '2'){
		return "UDP";
	}else{
		return "";
	}
}
 
	
	
</script>



