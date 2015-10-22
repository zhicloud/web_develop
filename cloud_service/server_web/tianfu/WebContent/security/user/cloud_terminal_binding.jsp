<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.CloudTerminalBindingVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	List<CloudTerminalBindingVO> dataList = (List<CloudTerminalBindingVO>)request.getAttribute("data");
%>

<!-- cloud_terminal_binding.jsp -->

<div id="cloud_host_terminal_binding_dlg_container">
	<div id="cloud_host_terminal_binding_dlg" class="easyui-dialog" title="绑定云终端"
		style="width:700px; height:350px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#cloud_host_terminal_binding_dlg_buttons',
			modal: true,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _cloud_host_terminal_binding_dlg_scope_;
			}
		">
		<form id="cloud_host_terminal_binding_dlg_form" method="post">
			
			<input type="hidden" id="host_id" name="host_id" value="<%=request.getAttribute("host_id")%>" />
			<input type="hidden" id="user_id" name="user_id" value="<%=request.getAttribute("user_id")%>" />
		
			<table border="0" style="height: auto; margin: auto;">
				
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;">云终端ID：</td>
					<td>
						<div>
							<input id="cloud_terminal_id" name="cloud_terminal_id" maxlength="20" style="width:100px"/>
							<a href="#" class="easyui-linkbutton" id="add_cloud_terminal_id">添加绑定</a> 
						</div>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<div id="cloud_termianl_box" style="width:450px;">
						<%
 						for(CloudTerminalBindingVO data : dataList){ 
 						%> 
 						<span style='padding:0 20px 0 0; white-space:nowrap; display:inline-block;'>
						<span class='cloud_terminal_text' cloud_terminal_id='<%=data.getCloudTerminalId() %>'><%=data.getCloudTerminalId() %>
						</span>  
						<a href='javascript:' class='delete_cloud_terminal_id'>解除绑定</a></span>
 						<% 
 						} 						
 						%> 						
						</div>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<div id="cloud_host_terminal_binding_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="cloud_host_terminal_binding_dlg_close_btn"> &nbsp;取&nbsp;消&nbsp; </a>
		<a href="javascript:" class="easyui-linkbutton" id="cloud_host_terminal_binding_dlg_save_btn"> &nbsp;保&nbsp;存&nbsp; </a> 
	</div>
</div>


<script type="text/javascript">

var _cloud_host_terminal_binding_dlg_scope_ = new function(){
	
	var self = this;
	
	// 绑定云终端ID
	self.addTerminalId = function()
	{
		var cloud_terminal_id = $("#cloud_terminal_id").val();
		
		if( cloud_terminal_id.trim()=="" )
		{
			return ;
		}
		// 判断是否已经有那个云终端ID
		if( $("#cloud_termianl_box").find("span.cloud_terminal_text[cloud_terminal_id='"+cloud_terminal_id+"']").size()==0 )
		{
			var item = $("<span style='padding:0 20px 0 0; white-space:nowrap; display:inline-block;'>\
				<span class='cloud_terminal_text' cloud_terminal_id='"+cloud_terminal_id+"'>" + cloud_terminal_id + "</span>  <a href='javascript:' class='delete_cloud_terminal_id'>解除绑定</a>\</span>\
				"); 
			// 插入dom树
			item.appendTo("#cloud_termianl_box");
			// 删除事件响应
			item.find("a.delete_cloud_terminal_id").click(function(){
				var cloud_termianl_id_element = this;
				top.$.messager.confirm('确认', '确定解除绑定?', function(result){
	                if( result ) {
	                	$(cloud_termianl_id_element).parent().remove();
	                }
	            });
			});
			$("#cloud_terminal_id").val(""); 
		}else{
			top.$.messager.alert('提示','该云终端ID已绑定','info',function(){
				$("#cloud_terminal_id").val(""); 
			});
			
		}
		
		
		
	};
	
	// 保存
	self.save = function() {
		var formData = $.formToBean(cloud_host_terminal_binding_dlg_form);
		formData.cloud_terminal_ids = $("#cloud_termianl_box").find("span.cloud_terminal_text").joinAttribute("cloud_terminal_id");
		ajax.remoteCall("bean://cloudHostService:cloudTerminalBinding", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					$("#cloud_host_terminal_binding_dlg").dialog("close");
				} else {
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	};
	
	
	// 关闭
	self.close = function() {
		$("#cloud_host_terminal_binding_dlg").dialog("close");
	};

	//--------------------------
	
	$(document).ready(function() {
			
		// 绑定云终端ID
		$("#add_cloud_terminal_id").click(function(){
			var cloud_terminal_id = $("#cloud_terminal_id").val();
			if(cloud_terminal_id==""){
				top.$.messager.alert("提示","云终端ID不能为空","info",function(){
					$("#cloud_terminal_id").val("");
				});
				return;
			}
			if(!(/^[\w\-]{5,17}$/.test(cloud_terminal_id))){
				top.$.messager.alert("提示","请输入6-18位数字、字母、下划线或者短横线","info",function(){
					$("#cloud_terminal_id").val("");
				});
			}else{
				self.addTerminalId();
			}
		});
		
		$("#cloud_termianl_box a.delete_cloud_terminal_id").click(function(){
			var cloud_termianl_id_element = this;
			top.$.messager.confirm('操作确认', '确定要解除绑定吗?', function(result){
                if( result ) {
                	$(cloud_termianl_id_element).parent().remove();
                }
            });
		});
		
		// 保存
		$("#cloud_host_terminal_binding_dlg_save_btn").click(function() {
				self.save();
		});
		// 关闭
		$("#cloud_host_terminal_binding_dlg_close_btn").click(function() {
			self.close();
		});
	});
	
};
	
	
</script>



