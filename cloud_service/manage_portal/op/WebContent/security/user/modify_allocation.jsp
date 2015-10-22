<%@page import="java.math.BigInteger"%>
<%@page import="com.zhicloud.op.vo.SysDiskImageVO"%>
<%@page import="com.zhicloud.op.common.util.FlowUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.vo.MemoryPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.CpuPackageOptionVO"%>
<%@page import="com.zhicloud.op.app.helper.CloudHostPrice"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
CloudHostVO cloudHost = (CloudHostVO)request.getAttribute("cloudHost");
List<CpuPackageOptionVO> cpuOptions = (List<CpuPackageOptionVO>)request.getAttribute("cpuOptions");
List<MemoryPackageOptionVO> memoryOptions = (List<MemoryPackageOptionVO>)request.getAttribute("memoryOptions");
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/metro/easyui.css" />

<div id="cloud_host_modify_allocation_container">

	<div id="cloud_host_modify_allocation" class="easyui-dialog" title="修改云主机配置"
		style="width:700px; height:340px; padding:10px;"
		data-options="
			iconCls: 'icon-add',
			buttons: '#cloud_host_modify_allocation_buttons',
			modal: true,
			onMove: _cloud_host_modify_allocation_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _cloud_host_modify_allocation_scope_;
			}
		">
		<form id="cloud_host_modify_allocation_form" method="post">
			
			<input type="hidden" id="region" name="region" value="<%=cloudHost.getRegion()%>" />
			<input type="hidden" id="cloudHostName" name="cloudHostName" value="<%=cloudHost.getHostName()%>" />
			<input type="hidden" id="cloudHostId" name="cloudHostId" value="<%=cloudHost.getId()%>" />
			<input type="hidden" id="realCloudHostId" name="realCloudHostId" value="<%=cloudHost.getRealHostId()%>" />
			<input type="hidden" id="bandWidth" name="bandWidth" value="<%=cloudHost.getBandwidth()%>" />
			<input type="hidden" id="sysDisk" name="sysDisk" value="<%=cloudHost.getSysDisk()%>" />
			<input type="hidden" id="dataDisk" name="dataDisk" value="<%=cloudHost.getDataDisk()%>" />
			<table border="0" style="height: auto; margin: auto;">
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />CPU核心数：</td>
					<td style="padding:5px 0 5px 0;">
						<label for="cpu_core_1" style="padding:0 10px 0 0;">
							<input type="radio" id="cpu_core_1"  name="cpu_core" onblur="_cloud_host_modify_allocation_scope_.checkCPUCore()" onclick="_cloud_host_modify_allocation_scope_.updatePrice()" value="1" />
							1核
						</label>
						<label for="cpu_core_2" style="padding:0 10px 0 0;">
							<input type="radio" id="cpu_core_2"  name="cpu_core" onblur="_cloud_host_modify_allocation_scope_.checkCPUCore()" onclick="_cloud_host_modify_allocation_scope_.updatePrice()" value="2" />
							2核
						</label>
						<label for="cpu_core_4" style="padding:0 10px 0 0;">
							<input type="radio" id="cpu_core_4"  name="cpu_core" onblur="_cloud_host_modify_allocation_scope_.checkCPUCore()" onclick="_cloud_host_modify_allocation_scope_.updatePrice()" value="4" />
							4核
						</label>
						<label for="cpu_core_8" style="padding:0 10px 0 0;">
							<input type="radio" id="cpu_core_8"  name="cpu_core" onblur="_cloud_host_modify_allocation_scope_.checkCPUCore()" onclick="_cloud_host_modify_allocation_scope_.updatePrice()" value="8" />
							8核
						</label>
						<label for="cpu_core_12" style="padding:0 10px 0 0;">
							<input type="radio" id="cpu_core_12"  name="cpu_core" onblur="_cloud_host_modify_allocation_scope_.checkCPUCore()" onclick="_cloud_host_modify_allocation_scope_.updatePrice()" value="12" />
							12核
						</label>
					</td>
					<td class="inputtip" id="tip-cpu_core"><i>请选择CPU核心数</i></td>
				</tr>
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;"><img src="<%=request.getContextPath() %>/images/button_required_red_16_16.gif" width="12" height="12" alt="必填" />内存：</td>
					<td style="padding:5px 0 5px 0;">
						<label for="memory_1" style="padding:0 10px 0 0;">
							<input type="radio" id="memory_1" name="memory" onblur="_cloud_host_modify_allocation_scope_.checkMemory()" onclick="_cloud_host_modify_allocation_scope_.updatePrice()" value="1" />
							1G
						</label>
						<label for="memory_2" style="padding:0 10px 0 0;">
							<input type="radio" id="memory_2" name="memory" onblur="_cloud_host_modify_allocation_scope_.checkMemory()" onclick="_cloud_host_modify_allocation_scope_.updatePrice()" value="2" />
							2G
						</label>
						<label for="memory_4" style="padding:0 10px 0 0;">
							<input type="radio" id="memory_4" name="memory" onblur="_cloud_host_modify_allocation_scope_.checkMemory()" onclick="_cloud_host_modify_allocation_scope_.updatePrice()" value="4" />
							4G
						</label>
						<label for="memory_6" style="padding:0 10px 0 0;">
							<input type="radio" id="memory_6" name="memory" onblur="_cloud_host_modify_allocation_scope_.checkMemory()" onclick="_cloud_host_modify_allocation_scope_.updatePrice()" value="6" />
							6G
						</label>
						<label for="memory_8" style="padding:0 10px 0 0;">
							<input type="radio" id="memory_8" name="memory" onblur="_cloud_host_modify_allocation_scope_.checkMemory()" onclick="_cloud_host_modify_allocation_scope_.updatePrice()" value="8" />
							8G
						</label>
						<label for="memory_12" style="padding:0 10px 0 0;">
							<input type="radio" id="memory_12" name="memory" onblur="_cloud_host_modify_allocation_scope_.checkMemory()" onclick="_cloud_host_modify_allocation_scope_.updatePrice()" value="12" />
							12G
						</label>
						<br/>
						<label for="memory_16" style="padding:0 10px 0 0;">
							<input type="radio" id="memory_16" name="memory" onblur="_cloud_host_modify_allocation_scope_.checkMemory()" onclick="_cloud_host_modify_allocation_scope_.updatePrice()" value="16" />
							16G
						</label>
						<label for="memory_32" style="padding:0 10px 0 0;">
							<input type="radio" id="memory_32" name="memory" onblur="_cloud_host_modify_allocation_scope_.checkMemory()" onclick="_cloud_host_modify_allocation_scope_.updatePrice()" value="32" />
							32G
						</label>
						<label for="memory_64" style="padding:0 10px 0 0;">
							<input type="radio" id="memory_64" name="memory" onblur="_cloud_host_modify_allocation_scope_.checkMemory()" onclick="_cloud_host_modify_allocation_scope_.updatePrice()" value="64" />
							64G
						</label>
					</td>
					<td class="inputtip" id="tip-host-memory"><i>请选择内存</i></td>
				</tr>
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;">每月费用：</td>
					<td style="padding:5px 0 5px 0;">
						<input type="text" name="price" id="price" value="<%=cloudHost.getMonthlyPrice()%>"/>元
					</td>
					<td class="inputtip" id=""><i></i></td>
				</tr>
				<tr>
					<td style="vertical-align:middle; white-space: nowrap; text-align:right;"></td>
					<td style="padding:5px 0 5px 0;">
						<span>当您提交了配置的修改之后，您的云主机将会按照新的费用进行计<br/>
						费，为使配置的修改能立即生效，系统会强制关闭您的云主机，为<br/>
						确保您的数据不丢失，请确认关闭云主机后再进行操作。</span>
					</td>
					<td class="inputtip" id=""><i></i></td>
				</tr>
			</table>
		</form>
	</div>

	<div id="cloud_host_modify_allocation_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="cloud_host_modify_allocation_save_btn"> &nbsp;提&nbsp;交&nbsp; </a> 
		<a href="javascript:" class="easyui-linkbutton" id="cloud_host_modify_allocation_close_btn"> &nbsp;取&nbsp;消&nbsp; </a>
	</div>
</div>

<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/user/refreshprice.js"></script>
<script type="text/javascript">

var _cloud_host_modify_allocation_scope_ = new function(){
	
	var self = this;
	
	self.updatePrice = function(){
		
		var core = $("input[name='cpu_core']:checked").val();
		if(core == null){
			core = "<%=cloudHost.getCpuCore()%>";
		}
		var memory = $("input[name='memory']:checked").val();
		if(memory == null){
			memory = "<%=CapacityUtil.toGBValue(cloudHost.getMemory(),0)%>";
		}
		/* if(core == 1){
			if(memory > 4){
				document.getElementById("memory_1").checked = true;
				memory = 1;
			}
			document.getElementById("memory_1").disabled = false;
			document.getElementById("memory_2").disabled = false;
			document.getElementById("memory_4").disabled = false;
			document.getElementById("memory_6").disabled = true;
			document.getElementById("memory_8").disabled = true;
			document.getElementById("memory_12").disabled = true;
			document.getElementById("memory_16").disabled = true;
			document.getElementById("memory_24").disabled = true;
			document.getElementById("memory_32").disabled = true;
			document.getElementById("memory_40").disabled = true;
			document.getElementById("memory_48").disabled = true;
			document.getElementById("memory_6").checked = false;
			document.getElementById("memory_8").checked = false;
			document.getElementById("memory_12").checked = false;
			document.getElementById("memory_16").checked = false;
			document.getElementById("memory_24").checked = false;
			document.getElementById("memory_32").checked = false;
			document.getElementById("memory_40").checked = false;
			document.getElementById("memory_48").checked = false;
		}
		if(core == 2){
			if(memory > 8){
				document.getElementById("memory_1").checked = true;
				memory = 1;
			}
			document.getElementById("memory_1").disabled = false;
			document.getElementById("memory_2").disabled = false;
			document.getElementById("memory_4").disabled = false;
			document.getElementById("memory_6").disabled = false;
			document.getElementById("memory_8").disabled = false;
			document.getElementById("memory_12").disabled = true;
			document.getElementById("memory_16").disabled = true;
			document.getElementById("memory_24").disabled = true;
			document.getElementById("memory_32").disabled = true;
			document.getElementById("memory_40").disabled = true;
			document.getElementById("memory_48").disabled = true;
			document.getElementById("memory_12").checked = false;
			document.getElementById("memory_16").checked = false;
			document.getElementById("memory_24").checked = false;
			document.getElementById("memory_32").checked = false;
			document.getElementById("memory_40").checked = false;
			document.getElementById("memory_48").checked = false;
		}
		if(core == 4){
			if(memory<2 || memory>16){
				document.getElementById("memory_2").checked = true;
				memory = 2;
			}
			document.getElementById("memory_1").disabled = true;
			document.getElementById("memory_2").disabled = false;
			document.getElementById("memory_4").disabled = false;
			document.getElementById("memory_6").disabled = false;
			document.getElementById("memory_8").disabled = false;
			document.getElementById("memory_12").disabled = false;
			document.getElementById("memory_16").disabled = false;
			document.getElementById("memory_24").disabled = true;
			document.getElementById("memory_32").disabled = true;
			document.getElementById("memory_40").disabled = true;
			document.getElementById("memory_48").disabled = true;
			document.getElementById("memory_1").checked = false;
			document.getElementById("memory_24").checked = false;
			document.getElementById("memory_32").checked = false;
			document.getElementById("memory_40").checked = false;
			document.getElementById("memory_48").checked = false;
		}
		if(core == 8){
			if(memory<4 || memory>32){
				document.getElementById("memory_4").checked = true;
				memory = 4;
			}
			document.getElementById("memory_1").disabled = true;
			document.getElementById("memory_2").disabled = true;
			document.getElementById("memory_4").disabled = false;
			document.getElementById("memory_6").disabled = false;
			document.getElementById("memory_8").disabled = false;
			document.getElementById("memory_12").disabled = false;
			document.getElementById("memory_16").disabled = false;
			document.getElementById("memory_24").disabled = false;
			document.getElementById("memory_32").disabled = false;
			document.getElementById("memory_40").disabled = true;
			document.getElementById("memory_48").disabled = true;
			document.getElementById("memory_1").checked = false;
			document.getElementById("memory_2").checked = false;
			document.getElementById("memory_40").checked = false;
			document.getElementById("memory_48").checked = false;
		}
		if(core == 12){
			if(memory<8){
				document.getElementById("memory_8").checked = true;
				memory = 8;
			}
			document.getElementById("memory_1").disabled = true;
			document.getElementById("memory_2").disabled = true;
			document.getElementById("memory_4").disabled = true;
			document.getElementById("memory_6").disabled = true;
			document.getElementById("memory_8").disabled = false;
			document.getElementById("memory_12").disabled = false;
			document.getElementById("memory_16").disabled = false;
			document.getElementById("memory_24").disabled = false;
			document.getElementById("memory_32").disabled = false;
			document.getElementById("memory_40").disabled = false;
			document.getElementById("memory_48").disabled = false;
			document.getElementById("memory_1").checked = false;
			document.getElementById("memory_2").checked = false;
			document.getElementById("memory_4").checked = false;
			document.getElementById("memory_6").checked = false;
		} */
		var region = "<%=cloudHost.getRegion()%>";
		var dataDisk = "<%=CapacityUtil.toGBValue(cloudHost.getDataDisk(), 0)%>";
		var bandWidth = "<%=FlowUtil.toMbpsValue(cloudHost.getBandwidth(),  0)%>";
		var newPrice = refreshPrice(region,core,memory,dataDisk,bandWidth);
		document.getElementById("price").value = newPrice;
		return;
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
	self.onMove = function(){
		var thisId = "#cloud_host_modify_allocation";
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
	// 提交
	self.save = function() {
		var formData = $.formToBean(cloud_host_modify_allocation_form);
		ajax.remoteCall("bean://cloudHostService:modifyAllocation", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception")
				{
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				}
				else if (reply.result.status == "success")
				{
					var data = $("#cloud_host_modify_allocation_container").parent().prop("_data_");
					data.onClose(reply.result);
					top.$.messager.alert('提示',reply.result.message,'info',function(){
                    				top.$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=4&bean=cloudUserService&method=myCloudHostPage");
                    				$("#cloud_host_modify_allocation").dialog("close");
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
		$("#cloud_host_modify_allocation").dialog("close");
	};

	//--------------------------
	
	$(document).ready(function() {
		// 提交
		$("#cloud_host_modify_allocation_save_btn").click(function() {
			if(self.checkCPUCore()&&self.checkMemory()) {
				self.save();
			}
		});
		// 关闭
		$("#cloud_host_modify_allocation_close_btn").click(function() {
			self.close();
		});
	});
	
};
</script>

