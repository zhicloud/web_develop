<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.CpuPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.MemoryPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.DiskPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.BandwidthPackageOptionVO"%>
<%@page import="com.zhicloud.op.vo.TrialPeriodParamVO"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.common.util.FlowUtil"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="java.util.List"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType    = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	List<CpuPackageOptionVO> cpuOptions       = (List<CpuPackageOptionVO>)request.getAttribute("cpuOptions");
	List<MemoryPackageOptionVO> memoryOptions = (List<MemoryPackageOptionVO>)request.getAttribute("memoryOptions");
	DiskPackageOptionVO diskOption            = (DiskPackageOptionVO)request.getAttribute("diskOption");
	BandwidthPackageOptionVO bandwidthOption  = (BandwidthPackageOptionVO)request.getAttribute("bandwidthOption");
	TrialPeriodParamVO trialPeriodCloudHost   = (TrialPeriodParamVO)request.getAttribute("trialPeriodCloudHost");
	TrialPeriodParamVO trialPeriodCloudDisk   = (TrialPeriodParamVO)request.getAttribute("trialPeriodCloudDisk");
%>
<!DOCTYPE html>
<!-- package_option_manage.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商 - 套餐项管理</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
</head>
	
<body>
<div class="oper-wrap">
	<div class="iframe" style="background:#fff;">
		<div class="panel-header">
			<div class="panel-title">套餐项管理</div>
			<div class="panel-tool"></div>
		</div>
		<div id="package_option_manage_dlg" title="套餐项管理" style="padding:10px;">	
			<div id="cpu_package_add_dlg" title="CPU套餐项管理" style="padding:10px 0 10px 0;border-bottom:1px solid #ccc;">
				<form id="cpu_package_add_dlg_form" method="post">
					<table id="cpu_datagrid" style="width:760px;">
						<tr>
							<td width="81px;">CPU套餐项：</td>
							<td>
								<input id="core" name="core" maxlength="10" data-options="precision: 0,min: 0" class="messager-input"/>
								<i>核</i>
								<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="plain:true,iconCls:'icon-add'" id="add_core">添加</a> 
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<div id="core_box" style="width:600px;padding-top:10px;">				
									<% for( CpuPackageOptionVO cpuOption : cpuOptions ) { %>
									<span style="line-height:24px;white-space:nowrap;padding-right:10px;display:inline-block;">
										<span class="core_text" core="<%=cpuOption.getCore()%>"><%=cpuOption.getCore()%>核</span>
										<a href="javascript:" class="delete_core" coreId="<%=cpuOption.getId()%>">删除</a>
									</span>
									<% } %>
								</div>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div id="memory_package_add_dlg"  title="内存套餐项管理" style="padding:10px 0 10px 0;border-bottom:1px solid #ccc;">
				<form id="memory_package_add_dlg_form" method="post">
					<table id="memory_datagrid" style="width:760px;">
						<tr>
							<td width="81px;">内存套餐项：</td>
							<td>
								<div>
									<input id="memory" name="memory" maxlength="10" data-options="precision: 4,min: 0" class="messager-input"/>
									<i>GB</i>
									<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="plain:true,iconCls:'icon-add'" id="add_memory">添加</a> 
								</div>
							</td>
						</tr>
						<tr>
							<td></td>
							<td>
								<div id="memory_box" style="width:600px;padding-top:10px;">				
									<% for( MemoryPackageOptionVO memoryOption : memoryOptions ) { %>
									<span style="line-height:24px;white-space:nowrap;padding-right:10px;display:inline-block;">
										<span class="memory_text" memory="<%=CapacityUtil.toGB(memoryOption.getMemory(), 4)%>"><%=memoryOption.getLabel()%></span>
										<a href="javascript:" class="delete_memory" memoryId="<%=memoryOption.getId()%>">删除</a>
									</span>
									<% } %>
								</div>
							</td>
						</tr>
					</table>
				</form>
			</div>
			<div id="disk_package_add_dlg"  title="磁盘套餐项管理" style="padding:10px 0 10px 0;border-bottom:1px solid #ccc;">
				<form id="disk_package_add_dlg_form" method="post">
					<table style="width:560px;">
						<tr>
							<td width="81px;">磁盘套餐项：</td>
							<td>
								<label class="f-mr5">最小</label>
								<input id="disk_min" type="text" name="disk_min" data-options="precision: 0,min: 0" class="messager-input"/>GB
							</td>
							<td>
								<label class="f-mr5">最大</label>
								<input id="disk_max" type="text"  name="disk_max" data-options="precision: 0,min: 0" class="messager-input" />GB
							</td>
							<td align="right"></td>
						</tr>
					</table>
				</form>
			</div>
			<div id="bandwidth_package_add_dlg"  title="宽带套餐项管理" style="padding:10px 0 10px 0;border-bottom:1px solid #ccc;">
				<form id="bandwidth_package_add_dlg_form" method="post">
					<table style="width:560px;">
						<tr>
							<td width="81px;">带宽套餐项：</td>
							<td>
								<label class="f-mr5">最小</label>
								<input  id="bandwidth_min" type="text" name="bandwidth_min" class="messager-input" data-options="precision: 0,min: 0"/>Mbps
							</td>
							<td>
								<label class="f-mr5">最大</label>
								<input id="bandwidth_max" type="text" name="bandwidth_max" class="messager-input" data-options="precision: 0,min: 0"/>Mbps
							</td>
							<td align="right"></td>
						</tr>
					</table>
				</form>
			</div>
			<div id = "trial_period_param_manage_dlg" title="试用期管理" style="padding:10px 0 10px 0;border-bottom:1px solid #ccc;">
				<form id="cloud_host_add_dlg_form" method="post">
					<input type="hidden" name ="id1" value = "<%=trialPeriodCloudHost.getId() %>">
					<input type="hidden" name ="id2" value = "<%=trialPeriodCloudDisk.getId() %>">
					<table  style="width:500px;">
						<tr>
							<td width="81px;">试用期：</td>
							<td>
								<input type="checkbox" id = "check1" name = "check1" >
								<label for="check1">是否启用云主机试用期</label>
							</td>
							<td>
								<label>试用期</label>
								<input type = "text" id="day1" name="day1" class="messager-input" value="<%=trialPeriodCloudHost.getDay() %>" data-options="precision: 0,min: 0"/>天
							</td>
						</tr>	
						<tr>
							<td></td>
							<td>
								<input type="checkbox"  id="check2" name="check2" >
								<label for="check2">是否启用云存储试用期</label>
							</td>
							<td>
								<label>试用期</label>
								<input type="text" id="day2" name="day2" class="messager-input" value="<%=trialPeriodCloudDisk.getDay()%>" data-options="precision: 0,min: 0" />天
							</td>
						</tr>
					</table>
				</form>
			</div>
			<table style="width:100%;padding-top:10px;">
				<tr>
					<td align="right"><a class="easyui-linkbutton oper-btn-sty" href="javascript:;" data-options="iconCls:'icon-save'" id="package_option_dlg_save_btn" >保存</a></td>
				</tr>
			</table>
		</div>
	</div>
</div>

<!-- JavaScript_start -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/big.min.js"></script>
<script type="text/javascript">
var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;

var flag1=false;
var flag2=false;
var flag3=false;

/**
 * cpu管理
 */
var _cpu_package_option_manage_dlg_scope_ = new function(){

	var cpu_obj = this;
	var cpu_upper = <%=request.getAttribute("cpu_package_option_upper_limit")%>;
	
	// 添加CPU套餐选项
	cpu_obj.addCore = function()
	{
		var   r = /^[0-9]*[1-9][0-9]*$/;
		var core = $("#core").val().trim();
		if( core=="" || !(r.test(core)))
		{
			top.$.messager.alert("警告","CPU核心数必须为正整数",'warning',function(){
				$("#core").val(""); 
			});
			return ;
		}
		if(core.trim() > cpu_upper)
		{
			top.$.messager.alert("警告","目前支持最大CPU核心数为"+cpu_upper+"个",'warning',function(){
				$("#core").val(""); 
			});
			return ;
		}
		// 判断是否已经有那个套餐项
		if( $("#core_box").find("span.core_text[core='"+core+"']").size()==0 )
		{
			// 存入数据库
			cpu_obj.saveCore();
		}
		else
		{
			alert("该选项已存在！");
		}
	};
	
	// 保存
	cpu_obj.saveCore = function() {
		var formData = $.formToBean(cpu_package_add_dlg_form);
		ajax.remoteCall("bean://packageOptionService:addCpuPackageOption", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					window.location.reload();
				} else {
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	};
	
	// 删除
	cpu_obj.deleteCore = function() {
		var coreId = $(this).attr("coreId");
		top.$.messager.confirm("确认", "确定要删除吗?", function (r) {  
	        if (r) {   
				ajax.remoteCall("bean://packageOptionService:deleteCpuPackageOption", 
					[ coreId ],
					function(reply) {
						if (reply.status == "exception") {
							if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
								top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
									top.location.reload();
								});
							}else{
								top.$.messager.alert("警告",reply.exceptionMessage,"warning",function(){
									top.location.reload();
								});
							}
						} else if (reply.result.status == "success") {
							window.location.reload();
						} else {
							alert(reply.result.message);
						}
					}
				);
	        }  
	    }); 
	};

	$(document).ready(function() {
		// 添加CPU套餐项
		$("#add_core").click(function(){
			cpu_obj.addCore();
		});
		$("#core_box a.delete_core").click(function(){
			cpu_obj.deleteCore.apply(this, arguments);
		});
	});
};

/**
 * 内存管理
 */
var _memory_package_option_manage_dlg_scope_ = new function(){

	var memory_obj = this;
	var memory_upper = <%=request.getAttribute("memory_package_option_upper_limit")%>;
	
	
	// 添加内存套餐选项
	memory_obj.addMemory = function()
	{
		var   r = /^[0-9]*[1-9][0-9]*$/;
		var memory = $("#memory").val().trim();
		if( memory=="" || !(r.test(memory)))
		{
			top.$.messager.alert("警告","内存大小必须为正整数",'warning',function(){
				$("#memory").val(""); 
			});
			return;
		}
		if(memory.trim() > memory_upper)
		{
			top.$.messager.alert("警告","目前支持最大内存为"+memory_upper+"G",'warning',function(){
				$("#memory").val(""); 
			});
			return;
		}
		memory = new Big(memory).toFixed(4);
		// 判断是否已经有那个套餐项
		if( $("#memory_box").find("span.memory_text[memory='"+memory+"']").size()==0 )
		{
			// 存入数据库
			memory_obj.saveMemory();
		}
		else
		{
			top.$.messager.alert('警告','该选项已存在！','warning');
		}
	};
	
	// 保存
	memory_obj.saveMemory = function() {
		var formData = $.formToBean(memory_package_add_dlg_form);
		ajax.remoteCall("bean://packageOptionService:addMemoryPackageOption", 
			[ formData ],
			function(reply) {
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					window.location.reload();				
				} else {
					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
	};
	
	// 删除
	memory_obj.deleteMemory = function() {
		var memoryId = $(this).attr("memoryId");
		top.$.messager.confirm("确认", "确定要删除吗?", function (r) {  
	        if (r) {   
				ajax.remoteCall("bean://packageOptionService:deleteMemoryPackageOption", 
					[ memoryId ],
					function(reply) {
						if (reply.status == "exception") {
							top.$.messager.alert('警告',reply.exceptionMessage,'warning');
						} else if (reply.result.status == "success") {
							window.location.reload();
						} else {
							top.$.messager.alert('警告',reply.result.message,'warning');
						}
					}
				);
	        }  
	    }); 
	};

	$(document).ready(function() {
		// 添加内存套餐项
		$("#add_memory").click(function(){
			memory_obj.addMemory();
		});
		$("#memory_box .delete_memory").click(function(){
			memory_obj.deleteMemory.apply(this, arguments);
		});
	});
};

/**
 * 磁盘管理
 */
var _disk_package_option_manage_dlg_scope_ = new function(){

	var min="<%=CapacityUtil.toGB(diskOption.getMin(),0)%>";
	min=min.replace("GB","");
	document.getElementById("disk_min").setAttribute("value",min);
	
	var max="<%=CapacityUtil.toGB(diskOption.getMax(),0)%>";
	max=max.replace("GB","");
	document.getElementById("disk_max").setAttribute("value",max);
	
	var disk_obj = this;
	
	// 更新 

	
};

/**
 * 宽带管理
 */
var _bandwitdh_package_option_manage_dlg_scope_ = new function(){
	
	var min="<%=FlowUtil.toMbps(bandwidthOption.getMin(), 0)%>";
	min=min.replace("Mbps","");
	document.getElementById("bandwidth_min").setAttribute("value",min);
	
	var max="<%=FlowUtil.toMbps(bandwidthOption.getMax(), 0)%>";
	max=max.replace("Mbps","");
	document.getElementById("bandwidth_max").setAttribute("value",max);
	
	var bandwidth_obj = this;
	 
	
};

/**
 * 试用期管理
 */
var _trial_period_param_manage_dlg_scope_ = new function(){
	
	var trial_period_obj = this;
	var cloud_host_day = <%=trialPeriodCloudHost.getDay() %>;
	var cloud_disk_day = <%=trialPeriodCloudDisk.getDay() %>

	function changeCheck()
	{
		if(cloud_host_day>0)
		{
			document.getElementById("check1").checked = true;
			document.getElementById("check1").value = "yes";
		} 
		else
		{
			document.getElementById("day1").disabled = "disabled";
		}
		if(cloud_disk_day>0)
		{
			document.getElementById("check2").checked = true;
			document.getElementById("check2").value = "yes";
		}
		else
		{
			document.getElementById("day2").disabled = "disabled";
		}
	}
	

	// 更新试用期参数 
	
	$(document).ready(function() {
		
		changeCheck();
	 
				
		
		$(':checkbox[name=check1]').change(function() {
			var checkval = $(this).prop("checked");
			if(checkval == true) {
				document.getElementById("day1").disabled="";
				document.getElementById("check1").value="yes";
			}else{
				document.getElementById("day1").disabled="disabled";
			};
		});
		
		$(':checkbox[name=check2]').change(function() {
			var checkval = $(this).prop("checked");
			if(checkval == true) {
				document.getElementById("day2").disabled="";
				document.getElementById("check2").value="yes";
			}else{
				document.getElementById("day2").disabled="disabled";
			};
		});
		
	});
	
	$("#package_option_dlg_save_btn").click(function(){
		
		var min_str = document.getElementById("disk_min").value;
		var max_str =  document.getElementById("disk_max").value;
		var min = parseInt(min_str);
		var max =  parseInt(max_str);
		
			var   r = /^[0-9]*[1-9][0-9]*$/;
			
			if(min_str == "" || max_str == "") {
					top.$.messager.alert('警告','磁盘大小不能为空','warning');		
					return false;
			}else if(!(r.test(min_str)) || !(r.test(max_str))){
				top.$.messager.alert('警告','磁盘大小必须为正整数','warning');
					return false;
			}else if(min > 999999999|| max > 999999999){
				top.$.messager.alert('警告','本系统最大提供99999999GB磁盘空间','warning');
				return false;
			}else if(min>=max){
				top.$.messager.alert('警告','磁盘最大值必须大于最小值','warning');
					return false;
			}else{
				flag1=true;
				
			}
			
			 min_str = document.getElementById("bandwidth_min").value;
			 max_str =  document.getElementById("bandwidth_max").value;
			 min = parseInt(min_str);
			 max =  parseInt(max_str); 
			if(min_str == "" || max_str == "") {
					top.$.messager.alert('警告','带宽大小不能为空','warning');					
					return false;
			}else if(!(r.test(min_str)) || !(r.test(max_str))){
				top.$.messager.alert('警告','带宽大小必须为正整数','warning');
					return false;
			}else if(min > 999999999|| max > 999999999){
				top.$.messager.alert('警告','本系统最大提供999999999Mbps带宽','warning');
				return false;
			}else if(min>=max){
				top.$.messager.alert('警告','带宽最大值必须大于最小值','warning');
					return false;
			}else{
				flag2=true; 
			}
			
			
			var day1= document.getElementById("day1").value;
			var day2= document.getElementById("day2").value; 
			var checkval1 = document.getElementById("check1").checked;
			var checkval2 = document.getElementById("check2").checked;
			
			 if(checkval1 && day1 == "") {
				 top.$.messager.alert('警告','云主机试用期不能为空','warning');		
					return false; 
			}else if(checkval2 && day2 == "") {
				 top.$.messager.alert('警告','云存储试用期不能为空','warning');		
					return false; 
			}else if(checkval1 && !(r.test(day1))){ 
					top.$.messager.alert('警告','云主机试用期必须为正整数','warning');
					return false; 
			}else if(checkval2 && !(r.test(day2))){ 
					top.$.messager.alert('警告','云存储试用期必须为正整数','warning');
					return false; 
			}else if(checkval1 && day1 > 999999999){ 
				top.$.messager.alert('警告','本系统提供云主机试用期最长为999999999天','warning');
				return false; 
			}else if(checkval2 && day2 > 999999999){ 
				top.$.messager.alert('警告','本系统提供云存储试用期最长为999999999天','warning');
				return false; 
			}else {
				flag3=true; 
			}
			 
			 if(!checkval1){
				 document.getElementById("day1").value=0;
			 }
			 if(!checkval2){
				 document.getElementById("day2").value=0;
			 }
			
		 if(flag1 & flag2 & flag3) {
			 var formData = $.formToBean(disk_package_add_dlg_form);
				ajax.remoteCall("bean://packageOptionService:updateDiskPackageOption", 
					[ formData ],
					function(reply) {
						if (reply.status == "exception") {
							flag1=false;
							top.$.messager.alert('警告',reply.exceptionMessage,'warning');
						} else if (reply.result.status != "success") {	
							flag1=false;
							top.$.messager.alert('警告',reply.result.message,'warning');
						} 
					}
				);
				
				var formData = $.formToBean(bandwidth_package_add_dlg_form);
				ajax.remoteCall("bean://packageOptionService:updateBandwidthPackageOption", 
					[ formData ],
					function(reply) {
						if (reply.status == "exception") {
							flag2=false;
							top.$.messager.alert('警告',reply.exceptionMessage,'warning');
						} else if (reply.result.status != "success") {	
							flag2=false;
							top.$.messager.alert('警告',reply.result.message,'warning');
						} 
					}
				);
				
				
				var formData = $.formToBean(cloud_host_add_dlg_form);
				ajax.remoteCall("bean://packageOptionService:updateTrialPeriodParam", 
					[ formData ],
					function(reply) {
						if (reply.status == "exception") {
							flag3=false;
							top.$.messager.alert('警告',reply.exceptionMessage,'warning');
						} else {
							flag3=false;
							top.$.messager.alert('警告',reply.result.message,'warning');
						}
					}
				);
				 if(flag1 & flag2 & flag3) {
					 top.$.messager.alert('提示','保存成功','info',function(){
						 window.location.reload();
					 });
				 }
		}
		 
		 
	});
};
</script>
</body>
</html>