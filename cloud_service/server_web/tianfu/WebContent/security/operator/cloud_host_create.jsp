<%@page import="com.zhicloud.op.app.helper.RegionHelper.RegionData"%>
<%@page import="com.zhicloud.op.app.helper.RegionHelper"%>
<%@page import="com.zhicloud.op.vo.SysDiskImageVO"%>
<%@page import="com.zhicloud.op.common.util.FlowUtil"%>
<%@page import="com.zhicloud.op.vo.BandwidthPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.DiskPackageOptionVO"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.vo.MemoryPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.CpuPackageOptionVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
List<CpuPackageOptionVO> cpuOptions = (List<CpuPackageOptionVO>)request.getAttribute("cpuOptions");
List<MemoryPackageOptionVO> memoryOptions = (List<MemoryPackageOptionVO>)request.getAttribute("memoryOptions");
List<SysDiskImageVO> sysDiskImageOptions = (List<SysDiskImageVO>)request.getAttribute("sysDiskImageOptions");
DiskPackageOptionVO diskOption = (DiskPackageOptionVO)request.getAttribute("diskOption");
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/metro/easyui.css" />

<div id="cloud_host_add_dlg_container">

	<div id="cloud_host_add_dlg" class="easyui-dialog" title="创建云主机"
		style="width:700px; height:560px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#cloud_host_add_dlg_buttons',
			modal: true,
			onMove: _cloud_host_add_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _cloud_host_add_dlg_scope_;
			}
		">
		<form id="cloud_host_add_dlg_form" method="post">
			
			<input type="hidden" id="user_id" name="user_id" value="<%=loginInfo.getUserId()%>" />
		
			<table border="0" style="height: auto; margin: auto;">
			
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />主机名：</td>
					<td class="inputcont">T2_<input type="text" id="host_name" name="host_name" style="width:300px;" onblur="_cloud_host_add_dlg_scope_.checkLength()"/></td>
					<td class="inputtip" id="tip-host-name"><i>请输入云主机名称</i></td>
				</tr>
				
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />地域：</td>
					<td style="padding:5px 0 5px 0;">
						<select id="region" name="region">
							<%
								for( RegionData regionData : RegionHelper.singleton.getAllResions() )
								{
							%>
							<option value="<%=regionData.getId()%>"><%=regionData.getName()%></option>
							<%
								}
							%>
						</select>
					</td>
					<td class="inputtip" id="tip-region"><i>请选择地域</i></td>
				</tr>
				
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />CPU核心数：</td>
					<td style="padding:5px 0 5px 0;">
						<%
						    int i=0;
							for( CpuPackageOptionVO cpuOption : cpuOptions )
							{
								if(i%7==0){
									out.println("<br/>");
								}
								i = i+1;
						%>
						<label for="cpu_core_<%=cpuOption.getCore()%>" style="padding:0 10px 0 0;">
							<input type="radio" id="cpu_core_<%=cpuOption.getCore()%>"  name="cpu_core" onblur="_cloud_host_add_dlg_scope_.checkCPUCore()" value="<%=cpuOption.getCore()%>" />
							<%=cpuOption.getCore()%>核
						</label>
						<%
							}
						%>
					</td>
					<td class="inputtip" id="tip-cpu_core"><i>请选择CPU核心数</i></td>
				</tr>
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />内存：</td>
					<td style="padding:5px 0 5px 0;">
						<%
						    i=0;
							for( MemoryPackageOptionVO memoryOption : memoryOptions )
							{
								if(i%6==0){
									out.println("<br/>");
								}
								i = i+1;
						%>
						<label for="memory_<%=memoryOption.getMemory()%>" style="padding:0 10px 0 0;">
							<input type="radio" id="memory_<%=memoryOption.getMemory()%>" name="memory" onblur="_cloud_host_add_dlg_scope_.checkMemory()"  value="<%=memoryOption.getMemory()%>" />
							<%=CapacityUtil.toCapacityLabel(memoryOption.getMemory(), 0)%>
						</label>
						<%
							}
						%>
					</td>
					<td class="inputtip" id="tip-host-memory"><i>请选择内存</i></td>
				</tr>
				
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />系统磁盘：</td>
					<td style="padding:5px 0 5px 0;">
						<label for="create_from_img" style="padding:0 20px 0 0;">
							<input type="radio" id="create_from_img" name="sys_disk_type" value="from_sys_image" checked="checked" />从镜像创建
						</label>
						<select id="sys_image_id" name="sys_image_id" onblur="_cloud_host_add_dlg_scope_.checkSys()" style="width:270px" class="easyui-radio">
							<option value="">请选择</option>
						</select>
					</td>
					<td class="inputtip" id="tip-host-sys"><i>请选择系统磁盘</i></td>
				</tr>
				
				<tr>
					<td></td>
					<td style="padding:5px 0 5px 0;">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<td style="width:70px;">
									<label for="empty_system">
										<input type="radio" id="empty_system" name="sys_disk_type" value="from_empty" />空白系统
									</label> 
								</td>
								<td style="padding:10px 20px 15px 30px ">
									<input id="sys_disk_slider" class="easyui-slider" style="width:300px" name="sys_disk"
										data-options="
											showTip:true, 
											min: <%=diskOption.getMin()%>,
											max: <%=diskOption.getMax()%>,
											rule: ['<%=CapacityUtil.toGB(diskOption.getMin(), 0)%>', '<%=CapacityUtil.toGB(diskOption.getMax(), 0)%>'],
											tipFormatter:function(value){
								                return CapacityUtil.toGB(value, 0);
								            },
										" />
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;">数据磁盘：</td>
					<td style="padding:5px 0 5px 0;">
						<table cellpadding="0" cellspacing="0">
							<tr>
								<td style="width:70px;">
									<label for="check2">
										<input type="checkbox" id="check2" name="check2" value="0" checked="checked" 
											style="position:relative; top:2px;" />
										使用
									</label>
								</td>
								<td style="padding:10px 20px 15px 30px ">
									<input id="data_disk_slider" class="easyui-slider" style="width:300px" name="data_disk"
										data-options="
											showTip:true, 
											min: <%=diskOption.getMin()%>,
											max: <%=diskOption.getMax()%>,
											rule: ['<%=CapacityUtil.toGB(diskOption.getMin(), 0)%>', '<%=CapacityUtil.toGB(diskOption.getMax(), 0)%>'],
											tipFormatter:function(value){
								                return CapacityUtil.toGB(value, 0);
								            },
										" />
								</td>
							</tr>
						</table>
					</td>
				</tr>
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;">其他：</td>
					<td style="padding:5px 0 5px 0;">
						<label for="is_auto_startup">
							<input type="checkbox" id="is_auto_startup" name="is_auto_startup" value="yes" style="position:relative; top:2px;" />
							自动启动
						</label>
					</td>
				</tr>
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;">开放端口：</td>
					<td>
						<div>
							<select id="protocol">
								<option value="0">所有协议</option>
								<option value="1">TCP</option>
								<option value="2">UDP</option>
							</select>
							
							<input id="port" name="port" maxlength="5" style="width:50px"/>
								 
							<a href="#" class="easyui-linkbutton" id="add_port">添加</a> 
						</div>
					</td>
				</tr>
				<tr>
					<td></td>
					<td>
						<div id="port_box" style="width:450px;">
						</div>
					</td>
				</tr>
			</table>
		</form>
	</div>

	<div id="cloud_host_add_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="cloud_host_add_dlg_save_btn"> &nbsp;创&nbsp;建&nbsp; </a> 
		<a href="javascript:" class="easyui-linkbutton" id="cloud_host_add_dlg_close_btn"> &nbsp;取&nbsp;消&nbsp; </a>
	</div>
	
	<div style="display:none;">
		<select id="sys_image_id_template" name="sys_image_id_template">
			<%
				for( SysDiskImageVO sysDiskImageOption : sysDiskImageOptions )
				{
			%>
			<option value="<%=sysDiskImageOption.getId()%>" region="<%=sysDiskImageOption.getRegion()%>"><%=sysDiskImageOption.getName()%></option>
			<%
				}
			%>
		</select>
	</div>
</div>


<script type="text/javascript">

var _cloud_host_add_dlg_scope_ = new function(){
	
	var self = this;
	
	self.checkLength = function(){
		var hostName = $("#host_name").val().trim();
		if(hostName==null || hostName==""){
			$("#tip-host-name").html("<b>主机名不能为空</b>");
			return false;
		}
		if( /[\u4e00-\u9fa5]/.test(hostName)){
			$("#tip-host-name").html("<b>主机名不能包含中文</b>");
			return false;
		}
		if(hostName.length>32){
			$("#tip-host-name").html("<b>主机名长度不能超过32个字符</b>");
			return false;
		}
		$("#tip-host-name").html("");
		return true;
	};
	self.checkCPUCore = function(){
		var core = $("input[name='cpu_core']:checked").val(); 
		if(core==null || core==""){
			$("#tip-cpu_core").html("<b>请选择CPU核心数</b>");
			return false;
		} 
		$("#tip-cpu_core").html("");
		return true;
	};
	self.checkMemory = function(){
		var memory = $("input[name='memory']:checked").val(); 
		if(memory==null || memory==""){
			$("#tip-host-memory").html("<b>请选择内存</b>");
			return false;
		} 
		$("#tip-host-memory").html("");
		return true;
	};
	self.checkSys = function(){
		var type = $("input[name='sys_disk_type']:checked").val(); 
		if(type=="from_empty"){
			$("#tip-host-sys").html("");
			return true;
		}else{
			var id = $("#sys_image_id").val();
			if(id==null || id==""){
			    $("#tip-host-sys").html("<b>请选择镜像</b>");
				return false;
			}
			$("#tip-host-sys").html("");
			return true;
			
		}
	};
	
	self.onMove = function(){
		var thisId = "#cloud_host_add_dlg";
		var topValue = $(thisId).offset().top;
		var leftValue = $(thisId).offset().left;
		if(topValue==0){
			topValue = 30;
		}
		if(topValue<30){
			$(thisId).dialog('move',{
				left:leftValue,
				top:1
			});
			return;
		}
	};
	
	// 添加开放端口
	self.addPort = function()
	{
		var protocolText = $("#protocol").find("option:selected").text();
		var protocol = $("#protocol").val();
		var port = $("#port").val();
		if( protocol.trim()=="" )
		{
			return ;
		}
		if( port.trim()=="" )
		{
			return ;
		}
		// 判断是否已经有那个端口
		if( $("#port_box").find("span.port_text[port='"+protocol + ":" + port+"']").size()==0 )
		{
			var item = $("<span style='padding:0 20px 0 0; white-space:nowrap; display:inline-block;'>\
				<span class='port_text' port='"+protocol + ":" + port+"'>" + protocolText + ":" + port + "</span><a href='javascript:' class='delete_port'>删除</a>\
				</span>\
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
	};
	
	// 保存
	self.save = function() {
		var formData = $.formToBean(cloud_host_add_dlg_form);
		formData.sys_disk  = CapacityUtil.toCapacityLabel(formData.sys_disk,  0);
		formData.data_disk = CapacityUtil.toCapacityLabel(formData.data_disk, 0);
		formData.ports = $("#port_box").find("span.port_text").joinAttribute("port");
		ajax.remoteCall("bean://cloudHostService:addCloudHost", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception")
				{
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				}
				else if (reply.result.status == "success")
				{
					var data = $("#cloud_host_add_dlg_container").parent().prop("_data_");
					$("#cloud_host_add_dlg").dialog("close");
					data.onClose(reply.result);
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
		$("#cloud_host_add_dlg").dialog("close");
	};

	//--------------------------
	
	$(document).ready(function() {
		// “地域”的onchange事件
		$("#region").change(function(){
			$("#sys_image_id").find("option:eq(0) ~ *").remove();													// 删除除第一个之外的option
			$("#sys_image_id_template").find("> option[region="+this.value+"]").clone().appendTo("#sys_image_id");	// 加入option
		});
		// 选择“从镜像创建”或“空白系统”
		$(":radio[name=sys_disk_type]").change(function(){
			if( this.id=="create_from_img" ) {
				$("#sys_image_id").attr("disabled", false);
			} else {
				$("#sys_image_id").attr("disabled", true).val("");
			}
		});
		// 添加端口
		$("#add_port").click(function(){
			var port = $("#port").val();
			if(!(/^[1-9][0-9]*$/.test(port))){
				top.$.messager.alert("提示","端口号只能是数字","info",function(){
					$("#port").val("");
				});
			}else{
				self.addPort();
			}
		});
		// 保存
		$("#cloud_host_add_dlg_save_btn").click(function() {
			if( self.checkLength() && self.checkCPUCore() && self.checkMemory() && self.checkSys() ) {
				self.save();
			}
		});
		// 关闭
		$("#cloud_host_add_dlg_close_btn").click(function() {
			self.close();
		});
		// 触发一次事件，进行sys_image_id > option的初始化
		$("#region").change();
	});
	
};
</script>



