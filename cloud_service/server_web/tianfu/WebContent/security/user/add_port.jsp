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
			
			<table border="0" style="height: auto; margin: auto;">
				
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;">开放端口：</td>
					<td>
						<div>
							<input id="port" name="port" maxlength="5" style="width:50px"/>
							<select id = "protocol" name="protocol">
								<option value = "0">所有协议</option>
								<option value = "1">TCP</option>
								<option value = "2">UDP</option>
							</select> 
							<a href="#" class="easyui-linkbutton" id="add_port">添加</a> 
						</div>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<div id="port_box" style="width:450px;">
							<%
								for(CloudHostOpenPortVO data : dataList){
							%>
							<span style='padding:0 20px 0 0; white-space:nowrap; display:inline-block;'>
								<span class='port_text'        port='<%=data.getPort()%>'><%=data.getPort()%></span>
								<span class='protocol_text'    protocol='<%=data.getProtocol()%>'><%=data.getProtocol()== 0?"所有协议":(data.getProtocol()== 1?"TCP":"UDP")%></span>
								<a href='javascript:' class='delete_port'>删除</a>
							</span>
							<%
								}
							%>
						</div>
					</td>
				</tr>
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
		
		// 判断是否已经有那个端口
		if( $("#port_box").find("span.port_text[port='"+port+"']").size()==0 )
		{
			var item = $(	"<span style='padding:0 20px 0 0; white-space:nowrap; display:inline-block;'>\
							<span class='port_text' port='"+port+"'>" + port + "</span>\
							<span class='protocol_text' protocol='"+protocol+"'>" + protocol_str + "</span>\
							<a href='javascript:' class='delete_port'>删除</a>\</span>\
							"); 
			// 插入dom树
			item.appendTo("#port_box");
			// 删除事件响应
			item.find("a.delete_port").click(function(){
				var portElement = this;
				top.$.messager.confirm('操作确认', '确定要删除吗?', function(result){
	                if( result ) {
	                	$(portElement).parent().remove();
	                }
	            });
			});
			$("#port").val(""); 
		}
		else
		{
			top.$.messager.alert('提示','该端口已存在','info',function(){
				$("#port").val(""); 
			});
		}
	};
	
	// 保存
	self.save = function() {
		var formData          = $.formToBean(cloud_host_add_port_dlg_form);
		formData.ports        = $("#port_box").find("span.port_text").joinAttribute("port");
		formData.protocols    = $("#port_box").find("span.protocol_text").joinAttribute("protocol");
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
	
	
</script>



