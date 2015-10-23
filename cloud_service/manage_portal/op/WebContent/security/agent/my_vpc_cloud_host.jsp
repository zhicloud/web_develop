<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import="com.zhicloud.op.common.util.FlowUtil"%>
<%@page import="com.zhicloud.op.vo.PackagePriceVO"%>
<%@page import="com.zhicloud.op.vo.SysDiskImageVO"%>
<%@page import="com.zhicloud.op.vo.VpcBaseInfoVO"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_AGENT);
	String userId = loginInfo.getUserId(); 
	String terminalUserId = (String)request.getSession().getAttribute("terminalUserId");
	List<CloudHostVO> myCloudHostList = (List<CloudHostVO>)request.getAttribute("myCloudHostList");
	List<CloudHostVO> hostList = (List<CloudHostVO>)request.getAttribute("hostList");
	String tips = (String)request.getAttribute("tips");
	VpcBaseInfoVO vpcVo = (VpcBaseInfoVO)request.getAttribute("vpcVo");
	String vpcId = (String)request.getAttribute("vpcId");
	String message = "";
 	if(myCloudHostList.isEmpty()){
		message = "没有相关记录";
	}else{
		message = "共找到"+myCloudHostList.size()+"条相关记录";
	}	
 	List<PackagePriceVO> cpuRegion  = (List<PackagePriceVO>)request.getAttribute("cpuRegion"); 
	List<PackagePriceVO> memoryRegion  = (List<PackagePriceVO>)request.getAttribute("memoryRegion"); 
	List<PackagePriceVO> diskRegion  = (List<PackagePriceVO>)request.getAttribute("diskRegion"); 
	List<PackagePriceVO> bandwidthRegion  = (List<PackagePriceVO>)request.getAttribute("bandwidthRegion"); 
	List<PackagePriceVO> packageInfo  = (List<PackagePriceVO>)request.getAttribute("packageInfo"); 
	List<SysDiskImageVO> imageList  = (List<SysDiskImageVO>)request.getAttribute("imageList"); 
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/agent.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<%-- <script src="<%=request.getContextPath() %>/javascript/jquery.min.js"></script>  --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/user/refreshprice.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/plugins/jquery.blockUI.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/agent.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=3");
ajax.async = true;
var userName = "<%=loginInfo.getAccount()%>";
var current_input_id = "";
var old_host_name = "";
var current_host_id = "";
$(document).ready(function(){
 	init(2);
 	inituser(userName,0);
	//----
	if('<%=tips%>'=='yes'){
		showbrief();
	}
	//===================全局操作：启动、关机、重启==========================
	$('#my_warehouse_datagrid').datagrid({
		onUnselect: function(rows){
			$('#sa').removeAttr("checked");
		},
		onSelect: function(rows){
			if($('#my_warehouse_datagrid').datagrid('getSelections').length>=$('#my_warehouse_datagrid').datagrid('getRows').length){
			$('#sa').prop("checked",true);
			}
		}
	});
	$('#sa').hover(function(event){
		$(this).next('label').addClass('active');
	},
	function(event){
		$(this).next('label').removeClass('active');
	});
	$('#sa').next('label').hover(function(event){
		$(this).addClass('active');
	},
	function(event){
		$(this).removeClass('active');
	});
	//全部启动
	$("#start-all").click(function(){

		var allHost = [];
		allHost = $("#my_warehouse_datagrid").datagrid("getSelections");
		if(allHost.length<=0){
			top.$.messager.alert("警告", "您尚未选择云主机", "warning", function(){
			});
			return;
		}
		var allStart = $('#start-all').attr("start-all");
		if(allStart=="2"){
			return;
		}
		var flagStatus = 0;
		
		top.$.messager.confirm("确认", "确定启动所有选中的云主机？", function (r) {
			if(r){
				loadingbegin(); 
				setTimeout("startAllHost()",1000);
				
			}
		}); 
		
			
	});
 
	//全部关机
	$("#shutdown-all").click(function(){
		var allHost = [];
		allHost = $("#my_warehouse_datagrid").datagrid("getSelections");
		if(allHost.length<=0){
			top.$.messager.alert("警告", "您尚未选择云主机", "warning", function(){
			});
			return;
		}
		var allShutdown = $('#shutdown-all').attr("shutdown-all");
		if(allShutdown=="2"){
			return;
		} 
		
		top.$.messager.confirm("确认", "确定关机所有选中的云主机？", function (r) {
			if(r){ 
				loadingbegin(); 
				setTimeout("stopAllHost()",1000);
			} 
		});  
			
			
	});
	//全部重启
	$("#restart-all").click(function(){
		var allHost = [];
		allHost = $("#my_warehouse_datagrid").datagrid("getSelections");
		if(allHost.length<=0){
			top.$.messager.alert("警告", "您尚未选择云主机", "warning", function(){
			});
			return;
		}
		var allRestart = $('#restart-all').attr("restart-all");
		if(allRestart=="2"){
			return;
		}
		top.$.messager.confirm("确认", "确定重启所有选中的云主机？", function (r) {
			if(r){
				loadingbegin(); 
				setTimeout("restartAllHost()",1000);
				
			}else{
				return;
			}
		});
	});
 	//==========================================================
	var self = {};
 	var ids = new Array();
 	<% for(int i=0;i<myCloudHostList.size();i++){%>
 		<%if("创建中".equals(myCloudHostList.get(i).getSummarizedStatusText())){%>
		ids.push("<%=myCloudHostList.get(i).getId()%>"); 		
 	<%}}%> 
	
	self.cloudHostId = "";
	self.ip = "";
	self.password="";
	self.cloudHostStartId="";
	self.cloudHostShutdownId = "";
	self.cloudHostRestartId = "";
	self.cloudHostInactivateId = "";
	self.cloudHostHaltId = "";
	self.cloudHostDeleteId = "";
	// 查询
	$("a[name='cloud_host']").click(function(){
		var region = $(this).attr("region");
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudUserService&method=myCloudHostPage&region="+encodeURIComponent(region);
		return;
	});	
	$("#one_cloud_host .box").hover(function(evt){
		self.cloudHostId = $(this).attr("cloudHostId");
		self.ip = $(this).attr("ip");
		self.password = $(this).attr("password");
	});
	// 查看详情
	$("#one_cloud_host .my_cloud_host_detail").click(function(evt){
		self.cloudHostViewDetail(self.cloudHostId);
	});
	// 进入云主机
	$("#one_cloud_host .enter_my_cloud_host").click(function(evt){
		var ip = self.ip;
		var password = self.password;
		window.open("<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=cloudHostPage&ip="+encodeURIComponent(ip)+"&password="+encodeURIComponent(password));
	});
	// 启动
	$("#one_cloud_host .launch_host").click(function(evt){
		
		self.cloudHostStartId = self.cloudHostId;
		var startHost = $(this).attr("startHost");
		$.cloudHostStart = $(this);
		if(startHost == "1"){
			top.$.messager.confirm("确认", "确定启动云主机？", function (r) {
				if(r){
					$('#cloud-status-id'+self.cloudHostStartId).html("正在启动...");
					$.cloudHostStart.removeClass("launch_host");
					$.cloudHostStart.addClass("alpha");
					$.cloudHostStart.attr("startHost","2");
					$('#allocation'+self.cloudHostStartId).addClass("alpha");
					$('#allocation'+self.cloudHostStartId).attr("modifyAllocation","2");
					self.startCloudHost();
				}else{
					return;
				}
			});  
		}
		sp();
	});
	// 关机
	$("#one_cloud_host .shutdown_host").click(function(evt){
		
		self.cloudHostShutdownId = self.cloudHostId;
		var stopHost = $(this).attr("stopHost");
		$.cloudHostStop = $(this);
		if(stopHost == "1"){
			top.$.messager.confirm("确认", "确定要关闭正在运行的云主机吗?", function (r) {
				if(r){
					$('#status'+self.cloudHostShutdownId).html("关机中...");
					$.cloudHostStop.addClass("alpha");
					$.cloudHostStop.attr("stopHost","2");
					$('#restart'+self.cloudHostShutdownId).addClass("alpha");
					$('#restart'+self.cloudHostShutdownId).attr("restartHost","2");
					$('#halt'+self.cloudHostShutdownId).addClass("alpha");
					$('#halt'+self.cloudHostShutdownId).attr("haltHost","2");
					$('#outOfService'+self.cloudHostShutdownId).addClass("alpha");
					$('#outOfService'+self.cloudHostShutdownId).attr("outofservice","2");
					self.stopCloudHost();
				}else{
					return;
				}
			});
		}else{
			return;
		}
		sp();
	});
	// 强制关机
	$("#one_cloud_host .halt_host").click(function(evt){
		
		self.cloudHostHaltId = self.cloudHostId;
		var haltHost = $(this).attr("haltHost");
		if(haltHost=="1"){
			self.haltCloudHost();
		}else{
			return;
		}
		sp();
	});
	// 重启
	$("#one_cloud_host .restart_host").click(function(evt){
		
		self.cloudHostRestartId = self.cloudHostId;
		var restartHost = $(this).attr("restartHost");
		if(restartHost=="1"){
			top.$.messager.confirm("确认", "确定要重新启动吗?", function (r) {
				if(r){
					$('#status'+self.cloudHostRestartId).html("正在重启...");
					$('#shutdown'+self.cloudHostRestartId).addClass("alpha");
					$('#shutdown'+self.cloudHostRestartId).attr("stopHost","2");
					$('#restart'+self.cloudHostRestartId).addClass("alpha");
					$('#restart'+self.cloudHostRestartId).attr("restartHost","2");
					$('#halt'+self.cloudHostRestartId).addClass("alpha");
					$('#halt'+self.cloudHostRestartId).attr("haltHost","2");
					self.restartCloudHost();
				}else{
					return;
				}
			});
		}
		sp();
	});
	
	// 弹出光盘
	$("#cloud_host_item_other_oper_menu .popup-iso").click(function(){
		self.popupIsoImage();
	});
	// 插入光盘
	$("#cloud_host_item_other_oper_menu .insert-iso").click(function(){
		self.insertIsoImage();
	});
	// 开放端口
	$("#cloud_host_item_other_oper_menu .open-port").click(function(){
		showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=addPortPage&hostId="+encodeURIComponent(self.cloudHostId)
		});
	});
	// 修改配置
	$("#one_cloud_host .modify-allocation").click(function(){
		
		var modifyAllocation = $(this).attr("modifyAllocation");
		if(modifyAllocation == "1"){
			window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=modifyAllocationPage&hostId="+encodeURIComponent(self.cloudHostId);
		}
		return;
		ajax.remoteCall("bean://cloudHostService:modifyAllocationPage",
				[ self.cloudHostId ], 
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
						top.$.messager.alert("信息", reply.result.message, "info");
					}
				}
			 );
	});
	//修改端口 
	$("#one_cloud_host .modify-port").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=modifyPortPage&hostId="+encodeURIComponent(self.cloudHostId);
	});
	// 绑定云终端
	$("#cloud_host_item_other_oper_menu .binding-terminal").click(function(){
		showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=cloudTerminalBindingPage&hostId="+encodeURIComponent(self.cloudHostId)+"&userId="+encodeURIComponent(userId)
		});
		 
	});
	// 修改监控密码
	$("#cloud_host_item_other_oper_menu .modify_password").click(function(){
		showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=updatePasswordPage&hostId="+encodeURIComponent(self.cloudHostId)+"&userId="+encodeURIComponent(userId)
		});
	});
	// 申请停机
	$("#one_cloud_host .out-of-service").click(function(){
		
		var outOfService = $(this).attr("outofservice");
		if(outOfService=="2"){
			return;
		}
		self.cloudHostInactivateId = self.cloudHostId;
		top.$.messager.confirm("确认", "确定要断开该云主机吗？", function (r) { 
			if(r){
				self.inactivateCloudHost();
			}
		});
		sp();
	});
	// 申请停机恢复
	$("#one_cloud_host .reactivate").click(function(){
		
		self.cloudHostInactivateId = self.cloudHostId;
		top.$.messager.confirm("确认", "确定要恢复该云主机吗？", function (r) { 
			if(r){
				self.reactivateCloudHost();
			}
		});
		sp();
	});
	$("#one_cloud_host .delete-cloud-host").click(function(){
		
		self.cloudHostHaltId = self.cloudHostId;
		if( self.cloudHostHaltId.trim()=="" )
		{
			return;
		}
		top.$.messager.confirm("确认", "确定删除?", function (r) {  
	        if (r) {  
				ajax.remoteCall("bean://cloudHostService:deleteCloudHostByIds",
					[ [self.cloudHostHaltId] ], 
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
							window.location.reload();
						}
					}
				);
	        }  
	    });
		sp();
	});
	// 启动云主机
	self.startCloudHost = function()
	{
		if( self.cloudHostStartId=="" )
		{
			return;
		}
		 ajax.remoteCall("bean://cloudHostService:startCloudHost", 
				[ self.cloudHostStartId ],
				function(reply) {
					if( reply.status == "exception" )
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
						top.$.messager.alert("信息", reply.result.message, "info",function(){
							window.location.reload();
						});
					}
					else
					{
						top.$.messager.alert('警告', reply.result.message, 'warning',function(){
							window.location.reload();
						});
					}
				}
			) ;
	};
	
	// 强制关机
	self.haltCloudHost = function()
	{
		if( self.cloudHostHaltId=="" )
		{
			return;
		}

		top.$.messager.confirm("确认", "确定要强制关闭正在运行的云主机吗?", function (r) {  
	        if (r) { 
	        	ajax.remoteCall("bean://cloudHostService:haltCloudHost",
    				[ self.cloudHostHaltId ], 
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
    							window.location.reload();
    						});
    					}
    				}
    			);
	        }  
	    }); 
	};
	// 关机
	self.stopCloudHost = function()
	{
		if( self.cloudHostShutdownId=="" )
		{
			return;
		}

      	ajax.remoteCall("bean://cloudHostService:stopCloudHost",
 				[ self.cloudHostShutdownId ], 
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
 							top.$.messager.alert("警告", reply.exceptionMessage, "warning",function(){
 								window.location.reload();
 							});
 						}
 					}
 					else if(reply.result.status=="success")
 					{
 						
 						top.$.messager.alert("信息", reply.result.message, "info",function(){
 							refreshStatus(self.cloudHostShutdownId);
					});
 						
 					}else{
						top.$.messager.alert("信息", reply.result.message, "info",function(){
							window.location.reload();
 						});
 					}
	    }); 
	};
	
	// 重启
	self.restartCloudHost = function()
	{
		if( self.cloudHostRestartId=="" )
		{
			return;
		}
		ajax.remoteCall("bean://cloudHostService:restartCloudHost", 
				[ self.cloudHostRestartId ],
				function(reply) {
					if( reply.status == "exception" )
					{
 						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
						{
							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
								window.location.reload();
							});
						}
						else 
						{
							top.$.messager.alert("警告", reply.exceptionMessage, "warning",function(){
								window.location.reload();
							});
						}
					}
					else if (reply.result.status == "success")
					{
						top.$.messager.alert("信息", reply.result.message, "info",function(){
							window.location.reload();
						});
					}
					else
					{
						top.$.messager.alert('警告', reply.result.message, 'warning',function(){
							window.location.reload();
						});
					}
				}
			) ;
	};
	
	// 加载光盘镜像
	self.insertIsoImage = function()
	{
		if( self.cloudHostId=="" )
		{
			return;
		}
		showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=insertIsoImagePage",
			params: {
				"cloudHostId" : self.cloudHostId
			},
			onClose : function(data) {
				window.location.reload();
			}
		});
	};
	
	// 弹出光盘镜像
	self.popupIsoImage = function()
	{
		if( self.cloudHostId=="" )
		{
			return;
		}
		ajax.remoteCall("bean://cloudHostService:popupIsoImage",
				[ self.cloudHostId ], 
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
						top.$.messager.alert("信息", reply.result.message, "info");
					}
				}
			);
	};
	
	// 申请停机
	self.inactivateCloudHost = function()
	{
		if( self.cloudHostInactivateId=="" )
		{
			return;
		}
		ajax.remoteCall("bean://vpcService:UnboundByHostId",
				[ self.cloudHostInactivateId ], 
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
							window.location.reload();
						});
					}
				}
			);
	};

	// 申请停机恢复
	self.reactivateCloudHost = function()
	{
		if( self.cloudHostInactivateId=="" )
		{
			return;
		}
		ajax.remoteCall("bean://cloudHostService:reactivateCloudHost",
				[ self.cloudHostInactivateId ], 
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
							window.location.reload();
						});
					}
				}
			);
	};
	
	// 查看云主机详情
	self.cloudHostViewDetail = function(cloudHostId){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=cloudHostViewDetailPage&cloudHostId="+encodeURIComponent(cloudHostId);
		return;
		showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=cloudHostViewDetailPage",
			params: {
				"cloudHostId" : cloudHostId
			},
			onClose : function(data) {
// 				self.query();
			}
		});
	};
	//获取当前主机ID
	$(".box .host_name").focus(function(){
		current_input_id = $(this).attr("id");
		old_host_name = $(this).val();
		current_host_id = current_input_id.substring(2,current_input_id.length);
	});
	// 查询
	$("#query_agent_btn").click(function(){
// 		self.query();
	});
	
	// 创建云主机
	$("#create_cloud_host_btn").click(function(){
		showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=createCloudHostPage",
			onClose : function(data) {
				self.query();
			}
		});
	});
	for(var i=0;i<ids.length;i++){
		$('#'+ids[i]).progressbar({value:0});
	}
	for(var i=0;i<ids.length;i++){
		refreshProgress(ids[i]);
	}
	
});
//--------------------------
function selectall(){
	if($("#sa").prop("checked")==true){
		$('#my_warehouse_datagrid').datagrid('selectAll');
	}else{
		$('#my_warehouse_datagrid').datagrid('unselectAll');
	}
}
function sp(){
	event.stopPropagation();
}

var my_flag = 0;
function refreshStatus(id){ 
	if(id!=''){
		ajax.remoteCall("bean://cloudHostService:getCloudHostStatus", 
			[ id ],
			function(reply) {
				if (reply.status == "exception") 
				{
// 					top.$.messager.alert('警告', reply.exceptionMessage, 'warning');
				} 
				else if (reply.result.status == "success") 
				{
					$('#status'+id).html("<strong class='gray'>已关机</strong>");
					$('#start'+id).removeClass("alpha");
					$('#start'+id).addClass("launch_host");
					$('#start'+id).attr("startHost","1");
					$('#shutdown'+id).addClass("alpha");
					$('#shutdown'+id).removeClass("shutdown_host");
					$('#shutdown'+id).attr("stopHost","2");
					$('#restart'+id).addClass("alpha");
					$('#restart'+id).removeClass("restart_host");
					$('#restart'+id).attr("restartHost","2");
					$('#halt'+id).addClass("alpha");
					$('#halt'+id).removeClass("halt_host");
					$('#halt'+id).attr("haltHost","2");
					$('#modify'+id).removeClass("alpha");
					$('#modify'+id).attr("modifyAllocation","1");
					$('#outOfService'+id).removeClass("alpha");
					$('#outOfService'+id).attr("outofservice","1");
					return;
				} 
				else 
				{
// 					if(my_flag++ < 30){
						window.setTimeout(function(){
							refreshStatus(id);
						},4000);
// 					}else{
// 						return;
// 					}
				}
			}
		);
		
	}else{
		 return;
	}
}
var fail_flag = "0";
//--------------------------
function refreshProgress(id){ 
	if(id!=''){
		ajax.remoteCall("bean://cloudHostService:getCloudHostCreationResult", 
			[ id ],
			function(reply) {
			console.info(reply);
				if (reply.status == "exception") 
				{
//					top.$.messager.alert('警告', reply.exceptionMessage, 'warning');
				} 
				else if (reply.result.status == "success") 
				{
					$('#'+id).progressbar('setValue', parseInt(reply.result.properties.progress));
					if( self.refreshProgress==null )
					{
						return ;
					}
					if( reply.result.properties.creation_status==null )
					{
						window.setTimeout(function(){
							refreshProgress(id);
						}, 3000);
					}
					else if( reply.result.properties.creation_status==false )
					{
						if(fail_flag == "0"){
							fail_flag = "1";
							top.$.messager.alert("提示","云主机创建失败","warning",function(){
								window.location.reload();
							});
						}
						
// 						$("#cloud_host_view_detail_dlg_form").html("<div style='text-align:center; padding-top:40px;'>创建失败</div>");
					}
					else if( reply.result.properties.creation_status==true )
					{
						window.setTimeout(function(){
							top.$.messager.alert("提示","云主机创建成功","info",function(){
								window.location.reload();
							});
						}, 1000);
						
// 						$("#cloud_host_view_detail_dlg_form").html("<div style='text-align:center; padding-top:40px;'>创建成功</div>");
					}
				} 
				else if(reply.result.message == "尚未开始创建云主机")
				{
					window.setTimeout(function(){
						refreshProgress(id);
					}, 3000);
				}
				else
				{
// 					top.$.messager.alert('警告',reply.result.message,'warning');
				}
			}
		);
		
	}else{
		 window.setTimeout(refreshProgress(id), 5000);
	}
}
//--------
function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	window.location.reload();
}
function showSingleDialog(param)
{
	var container = $("#single_dialog_dynamic_container");
	if( container.length==0 )
	{
		container = $("<div id='single_dialog_dynamic_container' style='display:none;'></div>").appendTo(document.body);
	}
	if( param.params==null )
	{
		param.params = {};
	}
	container.load(
		param.url, 
		param.params,
		function(){
			$.parser.parse(container.get(0));
			container.prop("_data_", param);
		}
	);
}
function changeHostName(){
	var new_host_name = $("#"+current_input_id).val();
	if(old_host_name == new_host_name){
		return;
	}
	if(new_host_name == ''||new_host_name.length>30){ 
		top.$.messager.alert("提示","主机名必须为1-30个字符","info",function(){
			window.location.reload();
		});
		return ;
	}
	if(current_host_id!='' && new_host_name!=''){
		ajax.remoteCall("bean://cloudHostService:updateHostNameById", 
			[ current_host_id,new_host_name],
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
						top.$.messager.alert("警告", "修改失败", "warning",function(){
							window.location.reload();
						});
					}
				} 
				else if (reply.result.status == "success") 
				{
					top.$.messager.alert('提示',reply.result.message,'info',function(){}); 
				} 
				else 
				{
					top.$.messager.alert('警告',reply.result.message,'warning',function(){
						
					});
				}
			}
		);
		
	}else{
		 window.location.reload();
	}
}
function startAllHost(){  
		var allHost = [];
		allHost = $("#my_warehouse_datagrid").datagrid("getSelections");
		$('#start-all').addClass("disable");
		$('#start-all').attr("start-all","2");
		$('#shutdown-all').addClass("disable");
		$('#shutdown-all').attr("shutdown-all","2");
		$('#restart-all').addClass("disable");
		$('#restart-all').attr("restart-all","2");
		var flag = 0;
		var hostIds="";
		for(var i=0;i<allHost.length;i++){ 
			var str = new String(allHost[i].data);
			var hostId = str.substring(str.indexOf("cloudhostid",0)+13,str.indexOf("cloudhostid",0)+45);
			var startHost = $('#allStart'+hostId).attr("startHost");
			if(str.indexOf("已关机",0)<0 || startHost=="2"){
				flag++;
				if(flag>=allHost.length){
					top.$.messager.alert("提示","未找到需要启动的云主机","info");
					$('#start-all').removeClass("disable");
					$('#start-all').attr("start-all","1");
					$('#shutdown-all').removeClass("disable");
					$('#shutdown-all').attr("shutdown-all","1");
					$('#restart-all').removeClass("disable");
					$('#restart-all').attr("restart-all","1");
					loadingend();
					return;
				}
				continue;
			}else{
				hostIds  = hostIds+hostId+"/";
				$('#cloud-status-id'+hostId).html("正在启动...");
				$('#allStart'+hostId).removeClass("launch_host");
				$('#allStart'+hostId).addClass("alpha");
				$('#allStart'+hostId).attr("startHost","2");
				$('#allocation'+hostId).addClass("alpha");
				$('#allocation'+hostId).attr("modifyAllocation","2"); 
				ajax.remoteCall("bean://cloudHostService:startCloudHost", 
					[ hostId ],
					function(reply) {
					        hostIds = hostIds.replace(reply.result.properties.hostId+"/","");
							if(hostIds.length<=1){
								loadingend();
								top.$.messager.alert("信息", "启动完成", "info",function(){
									window.location.reload();
								});
							}
						if( reply.status == "exception" )
						{
							
	 						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
								{
									top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
										window.location.reload();
									});
								}
								else 
								{
								}
								}
								else if (reply.result.status == "success")
								{
									
// 									top.$.messager.alert("信息", reply.result.message, "info",function(){
// 									});
								}
								else
								{
// 									top.$.messager.alert('警告', reply.result.message, 'warning',function(){
									
// 									});
								}
							}
						) ;
					} 
				}
		        
}
function restartAllHost(){  
		var allHost = [];
		allHost = $("#my_warehouse_datagrid").datagrid("getSelections");
		$('#start-all').addClass("disable");
		$('#start-all').attr("start-all","2");
		$('#shutdown-all').addClass("disable");
		$('#shutdown-all').attr("shutdown-all","2");
		$('#restart-all').addClass("disable");
		$('#restart-all').attr("restart-all","2");
		var flag = 0;
		var hostIds="";
		for(var i=0;i<allHost.length;i++){
			var str = new String(allHost[i].data);
			var hostId = str.substring(str.indexOf("cloudhostid",0)+13,str.indexOf("cloudhostid",0)+45);
			var restartHost = $('#restart'+hostId).attr("restartHost");
			if(str.indexOf("运行中",0)<0 || restartHost=="2"){
				flag++;
				if(flag>=allHost.length){
					top.$.messager.alert("提示","未找到需要重启的云主机","info");
					$('#start-all').removeClass("disable");
					$('#start-all').attr("start-all","1");
					$('#shutdown-all').removeClass("disable");
					$('#shutdown-all').attr("shutdown-all","1");
					$('#restart-all').removeClass("disable");
					$('#restart-all').attr("restart-all","1");
					loadingend();
					return;
				}
				continue;
			}else{
				hostIds  = hostIds+hostId+"/";
				$('#status'+hostId).html("正在重启...");
				$('#shutdown'+hostId).addClass("alpha");
				$('#shutdown'+hostId).attr("stopHost","2");
				$('#restart'+hostId).addClass("alpha");
				$('#restart'+hostId).attr("restartHost","2");
				$('#halt'+hostId).addClass("alpha");
				$('#halt'+hostId).attr("haltHost","2");
				ajax.remoteCall("bean://cloudHostService:restartCloudHost", 
					[ hostId ],
					function(reply) {
						hostIds = hostIds.replace(reply.result.properties.hostId+"/","");
						if(hostIds.length<=1){
							loadingend(); 
						}
						if( reply.status == "exception" )
						{
	 						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
							{
								top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
									window.location.reload();
								});
							}
							else 
							{
								top.$.messager.alert("警告", reply.exceptionMessage, "warning",function(){
								});
							}
						}
						else if (reply.result.status == "success")
						{
//								top.$.messager.alert("信息", reply.result.message, "info",function(){
//								});
						}
						else
						{
//								top.$.messager.alert('警告', reply.result.message, 'warning',function(){
//								});
						}
					}
				);
			}
		}
		loadingend();
		top.$.messager.alert('提示','已重启完毕', 'info',function(){
			window.location.reload();
		});
		        
}
function stopAllHost(){  
	var allHost = [];
	allHost = $("#my_warehouse_datagrid").datagrid("getSelections");
	$('#start-all').addClass("disable");
	$('#start-all').attr("start-all","2");
	$('#shutdown-all').addClass("disable");
	$('#shutdown-all').attr("shutdown-all","2");
	$('#restart-all').addClass("disable");
	$('#restart-all').attr("restart-all","2");
	var idList = new Array();
	var index = 0;
	var flag = 0; 
	for(var i=0;i<allHost.length;i++){
		var str = new String(allHost[i].data);
		var id = str.substring(str.indexOf("cloudhostid",0)+13,str.indexOf("cloudhostid",0)+45);
		var stopHost = $('#shutdown'+id).attr("stopHost");
		if(str.indexOf("运行中",0)<0 || stopHost=="2"){
			flag++;
			if(flag>=allHost.length){
				top.$.messager.alert("提示","未找到需要关闭的云主机","info");
				$('#start-all').removeClass("disable");
				$('#start-all').attr("start-all","1");
				$('#shutdown-all').removeClass("disable");
				$('#shutdown-all').attr("shutdown-all","1");
				$('#restart-all').removeClass("disable");
				$('#restart-all').attr("restart-all","1");
				loadingend();
				return;
			}
			continue;
		}else{
			idList.push(id);
			$('#status'+id).html("关机中...");
			$('#shutdown'+id).addClass("alpha");
			$('#shutdown'+id).attr("stopHost","2");
			$('#restart'+id).addClass("alpha");
			$('#restart'+id).attr("restartHost","2");
			$('#halt'+id).addClass("alpha");
			$('#halt'+id).attr("haltHost","2");
			$('#outOfService'+id).addClass("alpha");
			$('#outOfService'+id).attr("outofservice","2");
			ajax.remoteCall("bean://cloudHostService:stopCloudHost",
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
 							top.$.messager.alert("警告", reply.exceptionMessage, "warning",function(){
 							});
 						}
 					}
 					else if(reply.result.status=="success")
 					{
 						
							if(index<idList.length){
								refreshStatus(idList[index++]);
							}
 						
 					}else{
				//		top.$.messager.alert("信息", reply.result.message, "info",function(){
 							
 				//		});
 					}
		    }); 
		}
	}
	loadingend();
		        
}
$(document).ready(function(){
	$("#item6").click(function(){
		$("#diy").css("display","block");
	}); 
	$("#item0").click(function(){
		$("#diy").css("display","none");
	}); 
	$("#item1").click(function(){
		$("#diy").css("display","none");
	}); 
	$("#item2").click(function(){
		$("#diy").css("display","none");
	}); 
	$("#item3").click(function(){
		$("#diy").css("display","none");
	}); 
	$("#item4").click(function(){
		$("#diy").css("display","none");
	}); 
	$("input[name=item][id!=item6]").click(function(){
		$("#diy").css("display","none");
	}); 
	$("#item5").click(function(){
		$("#diy").css("display","none");
	});
	$("#cpuRegion1_1").click();
	$("#memoryRegion1_1").click();
	$("#bandwidthRegion1_1").click();
	$("#diskRegion1_1").click();
	$("#item1_1").click();
	$("#hostAmount").spinner({  
		onSpinUp:function(){
			getDescriptionAndPrice();
		} ,
		onSpinDown:function(){
			getDescriptionAndPrice();
		}
	 }); 
});
function showCreateHosts(){	
	jQuery.blockUI({ message: jQuery("#ddfff"),  
	        css: {border:'3px solid #aaa',
	              backgroundColor:'#FFFFFF',
	              overflow: 'hide',
	              width: '70%', 
	              height: 'auto', 
	              left:'15%',
	              top:'10%'} 
	        }); 
	jQuery('.blockOverlay').attr('title','单击关闭').click(jQuery.unblockUI);  
	$("#item1_1").click();
}
function showHosts(){	
	if($("#hostSelected").children().length>0){		
		jQuery.blockUI({ message: jQuery("#host_already_exsit"),  
		        css: {border:'3px solid #aaa',
		              backgroundColor:'#FFFFFF',
		              overflow: 'hide',
		              width: '70%', 
		              height: 'auto', 
		              left:'15%',
		              top:'10%'} 
		        }); 
		jQuery('.blockOverlay').attr('title','单击关闭').click(jQuery.unblockUI);  
	}else{
		top.$.messager.alert('警告','无主机可以选择','warning'); 
	}
}
function getDescriptionAndPrice(){
	var iteminfo="";
	var sysImageName="";
	var ports = "";
	var chkObjs = document.getElementsByName("item");
    var item = "";
    var bandwidth = "";
    var memory = "";
    var cpu = "";
    var dataDisk = "";
    var monthlyprice = 0;
    var hourprice = "";
    var flag = true;  
    var free = "";
    var region = document.getElementsByName("region"); 
    var region_val = " "; 
    for(var i=0;i<region.length;i++){
    	if(region[i].checked){
    		region_val = region[i].value;
    		break;
    	}
    } 
    var region_info; 
    if(region_val=="1"){
    	region_info = "广州";
    }else if(region_val=="2"){
    	region_info = "成都";
    	
    }else if(region_val=="4"){
    	region_info = "香港";
    	
    } 
    for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked){
        	if(chkObjs[i].value==""){
        		break;
        	}
        	item = chkObjs[i];       	 
        	cpu = $(item).attr("cpu");
        	memory = $(item).attr("memory");
        	dataDisk = $(item).attr("disk");
        	bandwidth = $(item).attr("bandwidth");    	 
        	monthlyprice = $(item).attr("price");  
        	flag = false;
            break;
        }
    }
    if(flag){  
    	chkObjs = document.getElementsByName("cpu");
    	var cpu = "";
    	for(var i=0;i<chkObjs.length;i++){
    		if(chkObjs[i].checked){
    			cpu = chkObjs[i].value;
    			break;
    		}
    	}
    	chkObjs = document.getElementsByName("memory");
    	var memory = "";
    	for(var i=0;i<chkObjs.length;i++){
    		if(chkObjs[i].checked){
    			memory = chkObjs[i].value;
    			break;
    		}
    	}
    	var diy = "true";
    	chkObjs = document.getElementsByName("dataDisk");
    	for(var i=0;i<chkObjs.length;i++){
    		if(chkObjs[i].checked){
    			dataDisk = chkObjs[i].value;
    			diy = "false";
    			break;
    		}
    	} 
    	if(diy=="true"){
    		dataDisk = $("#dataDisk").val().replace("G","");
    	}
    	diy = "true";
    	chkObjs = document.getElementsByName("bandwidth");
    	for(var i=0;i<chkObjs.length;i++){
    		if(chkObjs[i].checked){
    			bandwidth = chkObjs[i].value;
    			diy = "false";
    			break;
    		}
    	}
    	if(diy=="true"){
    		bandwidth = $("#bandwidth").val().replace("M","");
    	}
    }
    iteminfo = "CPU:"+cpu+"核     内存:"+memory+"G   硬盘:"+dataDisk+"G   带宽:"+bandwidth+"M"; 
    var chkObjs = document.getElementsByName("sysImageId"); 
    for(var i=0;i<chkObjs.length;i++){
        if(chkObjs[i].checked==true){  
        	sysImageName = chkObjs[i].id;
        	break;
        }
    } 
    
     
    if(monthlyprice==0){  
    	ajax.async = false; 	
    	monthlyprice = refreshPrice(region_val,"2",cpu,memory,dataDisk,bandwidth); 
     	ajax.async = true; 
    	if(flag==false){
    		free = "(推广期免费，2015-03-31止)";
    		if(region_val == '2'){
    			free = "(2015-12-31前免费)";   			
    		}
    	}
    } 
	hourprice = (monthlyprice*$("#hostAmount").val()/30/24).toFixed(3);  
	var info = "<i>单价:<strong>"+hourprice+"元/小时</strong> 约合:<strong>"+monthlyprice*$("#hostAmount").val()+"元/月</strong>"+free+"</i><br/>"+iteminfo
	 
 	$("#description_2").html(info); 
    
}

function getNewCreate(){ 
  	var formData = $.formToBean(cloud_host_config_form);
 	ajax.remoteCall("bean://vpcService:addNewHostToVpcBaseInfo", 
		[ formData ],
		function(reply) {  
			if (reply.status == "exception") {
 				if(reply.errorCode=="RMC_1"){
						top.$.messager.alert("警告","登陆超时，请重新登录","warning",function(){
							slideleft(1);
					});
				}else{ 
					top.$.messager.alert('警告','创建失败','warning'); 
				}
			} else if (reply.result.status == "fail") {
 				top.$.messager.alert('警告',reply.result.message,'warning'); 
// 				$("#cloudhostcreateprogress").html("创建失败");
// 				$("#progressbar_id").hide();
				
			} else {   
				top.$.messager.alert("提示",reply.result.message,"info",function(){
 				     window.location.reload();
			     });
			}
		}
	);  
}

function Unbound(id){
	top.$.messager.confirm("确认", "主机解除之后主机将被删除，无法恢复<br/>确定解除绑定吗？", function (r) {
		if(r){
			ajax.remoteCall("bean://vpcService:unboundByHostId", 
					[ id ],
					function(reply) {
						if( reply.status == "exception" )
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
							top.$.messager.alert("信息", reply.result.message, "info",function(){
								window.location.reload();
							});
						}
						else
						{
							top.$.messager.alert('警告', reply.result.message, 'warning',function(){
								window.location.reload();
							});
						}
					}
				) ;
		}else{
			return;
		}
	});
}
function toBinding(id){
	
	var _ids = [];
	$("input[name=hostId]:checked").each(function(){
		_ids.push($(this).val());
	});
	if(_ids.length==0){
		top.$.messager.alert("提示", "请选择需要删除的项", "info", function(){
		});
		return;
	}
	top.$.messager.confirm("确认", "确定绑定吗？", function (r) {
		if(r){
			ajax.remoteCall("bean://vpcService:boundByHostIds", 
					[ _ids,id ],
					function(reply) {
						if( reply.status == "exception" )
						{
	 						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
							{
								top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
									window.location.reload();
								});
							}
							else 
							{
								top.$.messager.alert("警告", '绑定失败', "warning");
							}
						}
						else if (reply.result.status == "success")
						{
							top.$.messager.alert("信息", reply.result.message, "info",function(){
								window.location.reload();
							});
						}
						else
						{
							top.$.messager.alert('警告', '绑定失败', 'warning',function(){
								window.location.reload();
							});
						}
					}
				) ;
		}else{
			return;
		}
	});
}
</script>
<!--[if IE 6]>
<script src="javascript/DD_belatedPNG.js"></script>
<script type="text/javascript">
	DD_belatedPNG.fix("*");
</script>
<![endif]-->
</head>

<body>
<div class="page">
  <div class="header">
     <div class="top">
	   <a class="logo l" href="#"><img src="<%=request.getContextPath()%>/image/agent_logo.png" width="188" height="25" alt="致云代理商管理平台" /></a>
	   <div class="nav l">
	    <a href="#" id="business">
				<img id="agent_nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_1_i.png" width="21" height="21" />
				<label>业务信息</label>
			</a>
			<a href="#" id="user_manage">
				<img id="agent_nav_2" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_2_i.png" width="21" height="21" />
				<label>用户管理</label>
			</a>
			<a href="#" id="my_account">
				<img id="agent_nav_3" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_3_i.png" width="21" height="21" />
				<label>我的账户</label>
			</a>
			<a href="#" id="oper_log">
				<img id="agent_nav_4" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_4_i.png" width="21" height="21" />
				<label>操作日志</label>
			</a>
			<a href="#" id="user_manual">
				<img id="agent_nav_5" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_5_i.png" width="21" height="21"/>
				<label>使用手册</label>
			</a>
	   </div>
	   <div class="user l">
	    <img class="reddot" src="<%=request.getContextPath()%>/image/reddot.png" width="6" height="6" alt=" " />
	    <a id="logoutlink" href="javascript:void(0);">注销</a>
	    <span>|</span>
	    <a id="userlink" href="javascript:void(0);"></a>
	   </div>
	   <div class="clear"></div>
	  </div>
  </div>
    <div class="main">
      <div class="titlebar">
        <div class="tabbar l"><a href="<%=request.getContextPath()%>/bean/page.do?userType=3&bean=terminalUserService&method=agentViewVpcPage&terminalUserId=<%=terminalUserId%>" onclick="self.location=document.referrer;"><img src="<%=request.getContextPath()%>/image/button_back.png" width="22" height="30" alt="返回" /><div style="margin-top:-30px;margin-left:25px;">VPC:<%=vpcVo.getDisplayName() %></div></a></div>
         <a href="#" onclick="showCreateHosts();" style="cursor:pointer;margin-left:20px;" class="greenbutton r" title="重新给VPC创建主机">创建主机</a>  
         <a href="#" onclick="showHosts();" style="cursor:pointer;margin-left:100px;" class="greenbutton r" title="从我的云主机添加到VPC">添加主机</a>      
        </div>
         <div style="width:980px;padding: 16px 0 0 0; margin:0 auto;">
        <div style="display:none">
        <div class="sa l" style="font-size:14px; padding-left:7px;"><input id="sa" name="" type="checkbox" value="" onchange="selectall();" /><label for="sa"> 全选</label></div>
        <div class="sa l" style="font-size:14px; padding-left:113px;"><a id="restart-all" href="#" restart-all="1">重启</a><a id="shutdown-all" href="#" shutdown-all="1">关机</a><a id="start-all" href="#" start-all="1">开机</a>
        </div>
        <div class="clear"></div>
        </div>
        <div class="clear"></div>
      </div>
      <div id="one_cloud_host" style="width:980px;padding: 6px 0 0 0; margin:0 auto;">
        <table id="my_warehouse_datagrid" class="easyui-datagrid" data-options="border:false,showHeader:false,scrollbarSize:0,width:980">
          <thead>
            <tr>
              <th data-options="field:'data',width:980"><%=message %></th>
            </tr>
          </thead>
          <tbody>
          <%
		 	if(myCloudHostList.isEmpty()){
		  %>
		 	<center>没有相关记录</center>
          <%
		 	}
          	for(CloudHostVO cloudHost : myCloudHostList){
          %>
            <tr>
              <td><div class="box" cloudHostId="<%=cloudHost.getId()%>" ip="<%=cloudHost.getOuterIp() %>" password="<%=cloudHost.getPassword()%>">
                  <div class="listleft l">
                    <div class="div1 l">
                     <%if(cloudHost.getType()==6){%>
                    	 <img src="<%=request.getContextPath() %>/image/icon_agent.png" width="20" height="20" class="agenticon" />
                     <%} %>
                      <div class="listicon">
                      <%if(cloudHost.getSysImageName()!=null){ %>
	                      <%if(cloudHost.getSysImageName().contains("indows7")){%>
	                      		<a href="javascript:void(0);" class="newbtn windows7" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageName().contains("indows2003")){%>
	                    	  	<a href="javascript:void(0);" class="newbtn windows2003" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageName().contains("indows2008")){%>
	              	      		<a href="javascript:void(0);" class="newbtn windows2008" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageName().contains("indows2012")){%>
	        	      			<a href="javascript:void(0);" class="newbtn windows2012" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
	          	      	  <%}else if(cloudHost.getSysImageName().contains("entos6.4") || cloudHost.getSysImageName().contains("entos_6.4")){%>
	                  	  		<a href="javascript:void(0);" class="newbtn centos6-4" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageName().contains("entos6.5") || cloudHost.getSysImageName().contains("entos_6.5")){%>
	            	      		<a href="javascript:void(0);" class="newbtn centos6-5" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageName().contains("buntu12.04") || cloudHost.getSysImageName().contains("buntu_12.04")){%>
	      	      				<a href="javascript:void(0);" class="newbtn ubuntu12-04" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
	        	      	  <%}else if(cloudHost.getSysImageName().contains("buntu14.04") || cloudHost.getSysImageName().contains("buntu_14.04")){%>
    	      					<a href="javascript:void(0);" class="newbtn ubuntu14-04" title="<%=cloudHost.getSysImageName()%>">&nbsp;</a>
  	        	      	  <%}else{%>
		                        <a href="javascript:void(0);" class="newbtn default-system" title="未知系统">&nbsp;</a>
		                  <%} %>
	                   <%}else{ %>
	                   	  <%if(cloudHost.getSysImageNameOld()!=null && cloudHost.getSysImageNameOld().contains("indows7")){%>
	                      		<a href="javascript:void(0);" class="newbtn windows7" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageNameOld()!=null && cloudHost.getSysImageNameOld().contains("indows2003")){%>
	                    	  	<a href="javascript:void(0);" class="newbtn windows2003" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageNameOld()!=null && cloudHost.getSysImageNameOld().contains("indows2008")){%>
	              	      		<a href="javascript:void(0);" class="newbtn windows2008" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageNameOld()!=null && cloudHost.getSysImageNameOld().contains("indows2012")){%>
	        	      			<a href="javascript:void(0);" class="newbtn windows2012" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
	          	      	  <%}else if(cloudHost.getSysImageNameOld()!=null && (cloudHost.getSysImageNameOld().contains("entos6.4") || cloudHost.getSysImageNameOld().contains("entos_6.4"))){%>
                	  			<a href="javascript:void(0);" class="newbtn centos6-4" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageNameOld()!=null && (cloudHost.getSysImageNameOld().contains("entos6.5") || cloudHost.getSysImageNameOld().contains("entos_6.5"))){%>
	          	      			<a href="javascript:void(0);" class="newbtn centos6-5" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
	                      <%}else if(cloudHost.getSysImageNameOld()!=null && (cloudHost.getSysImageNameOld().contains("buntu12.04") || cloudHost.getSysImageNameOld().contains("buntu_12.04"))){%>
	    	      				<a href="javascript:void(0);" class="newbtn ubuntu12-04" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
	      	      	  	  <%}else if(cloudHost.getSysImageNameOld()!=null && (cloudHost.getSysImageNameOld().contains("buntu14.04") || cloudHost.getSysImageNameOld().contains("buntu_14.04"))){%>
		      					<a href="javascript:void(0);" class="newbtn ubuntu14-04" title="<%=cloudHost.getSysImageNameOld()%>">&nbsp;</a>
        	      	  <%}else{%>
	                        	<a href="javascript:void(0);" class="newbtn default-system" title="未知系统">&nbsp;</a>
	                       <%} %>
	                   <%} %>
                        <%if("创建中".equals(cloudHost.getSummarizedStatusText()) || "创建失败".equals(cloudHost.getSummarizedStatusText())){ %>
                       		<div class="text">
	                          <input id="t1<%=cloudHost.getId() %>" name="" disabled='disabled' title="<%=cloudHost.getDisplayName() %>" type="text" value="<%=cloudHost.getDisplayName()%>"/>
	                        </div>
                        <%}else{ %>
                         <div class="text">
<%--                           <input id="t1<%=cloudHost.getId() %>" class="host_name" onblur="changeHostName()" maxlength="100" name="" type="text" value="<%=cloudHost.getDisplayName()%>"/> --%>
                          <input id="t1<%=cloudHost.getId() %>" class="host_name"  onblur="$(this).next('label').css('left','0');changeHostName();"  maxlength="100" name="" type="text" value="<%=cloudHost.getDisplayName()%>"/>
                          <label for="t1<%=cloudHost.getId() %>" onclick="$(this).css('left','-99999px');" title="<%=cloudHost.getDisplayName() %>" class="smalllabel l" style="cursor:pinter; background:url(<%=request.getContextPath()%>/image/icon_edit.png) no-repeat center right"></label>
                         </div>
                        <%} %>
                      </div>
                    </div>
                    <div class="div2 l">
                    <div class="listicon l" style="width:80px">
                        <div class="btn btn1">&nbsp;</div>
                        <div class="text"><%=cloudHost.getCpuCore()%>核</div>
                      </div>
                      <div class="listicon l" style="width:80px">
                        <div class="btn btn2">&nbsp;</div>
                        <div class="text"><%=cloudHost.getMemoryText(0)%></div>
                      </div>
                      <div class="listicon l" style="width:80px">
                        <div class="btn btn3">&nbsp;</div>
                        <div class="text"><%=cloudHost.getDataDiskText(0)%></div>
                      </div>
                    </div>
                    <%if("关机".equals(cloudHost.getSummarizedStatusText())){ %>
                    <div id="cloud-status-id<%=cloudHost.getId()%>" class="div3 l"><strong class="gray">已关机</div>
                    <%}else if("运行".equals(cloudHost.getSummarizedStatusText())){ %>
                    <div id="status<%=cloudHost.getId()%>" class="div3 l">运行中</div>
                    <%}else if("创建中".equals(cloudHost.getSummarizedStatusText())){ %>
                    <div class="div3 l"><strong class="gray">创建中</div>
                    <%}else if("创建失败".equals(cloudHost.getSummarizedStatusText())){ %>
                    <div class="div3 l"><strong class="gray">创建失败</div>
                    <%}else if("告警".equals(cloudHost.getSummarizedStatusText())){ %>
                    <div class="div3 l"><strong class="orange">告警</div>
                    <%}else if("故障".equals(cloudHost.getSummarizedStatusText())){ %>
                    <div class="div3 l"><strong class="red">故障</div>
                    <%}else if("停机".equals(cloudHost.getSummarizedStatusText())){ %>
                    <div class="div3 l"><strong class="gray">已停机</div>
                    <%} %>
                    <%if(cloudHost.getRegion()!=null && cloudHost.getRegion()==2){ %>
                    	<div class="div4 l"><div class="l" style="margin-left:24px;"  ><img src="<%=request.getContextPath() %>/image/icon_ip.png" width="20" height="20" alt=" " style="vertical-align:middle"/><%=cloudHost.getVpcIp()==null?"无":cloudHost.getVpcIp() %></div>
                        <div class="r" style="margin-right:24px;"><img src="<%=request.getContextPath() %>/image/icon_bandwidth.png" width="20" height="20" alt=" " style="vertical-align:middle" /><%=cloudHost.getBandwidthText(0)%></div>
                    </div>
                    <%} else{%>
                    <div class="div4 l"><div class="l" style="margin-left:24px;"  ><img src="<%=request.getContextPath() %>/image/icon_ip.png" width="20" height="20" alt=" " style="vertical-align:middle"/><%=cloudHost.getVpcIp()==null?"无":cloudHost.getVpcIp() %></div>
                      <div class="r" style="margin-right:24px;"><img src="<%=request.getContextPath() %>/image/icon_bandwidth.png" width="20" height="20" alt=" " style="vertical-align:middle" /><%=cloudHost.getBandwidthText(0)%></div>
                    </div>
                    <%} %>
                  </div>
                  <%if("关机".equals(cloudHost.getSummarizedStatusText())) {%>
                  	<div class="listright l" name="a<%=cloudHost.getId()%>">
	                  <a id="allStart<%=cloudHost.getId()%>" href="javascript:void(0);" class="btn btn1 l launch_host" title="开机" startHost="1">&nbsp;</a>
	                  <a href="javascript:void(0);" class="btn btn2 alpha l shutdown_host" title="关机" stopHost="2">&nbsp;</a>
	                  <a href="javascript:void(0);" class="btn btn3 alpha l restart_host" title="重启" restartHost="2">&nbsp;</a>
	                  <a href="javascript:void(0);" class="btn btn4 l alpha halt_host" title="强制关机" haltHost="2">&nbsp;</a>
	                  <%if(cloudHost.getType()==6){ %>
	                  	<a href="javascript:void(0);" class="btn btn18 alpha l" title="断开" onclick="Unbound('<%=cloudHost.getId() %>');">&nbsp;</a>
	                  	<a href="javascript:void(0);" class="btn btn7 l alpha" title="修改配置">&nbsp;</a>	                  	
	                  	<%if(cloudHost.getRegion()!=null && cloudHost.getRegion()==2){ %>
		                 	<a id="modifiy_port<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn13 l modify-port" title="端口详情" modifyPort="1">&nbsp;</a>	                  	
	                  <%}}else{ %>
	                  	<a id="outOfService<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn18 l out-of-service" title="停用" outofservice="1">&nbsp;</a>
	                  	<a id="allocation<%=cloudHost.getId()%>" href="javascript:void(0);" class="btn btn7 l modify-allocation" title="修改配置" modifyAllocation="1">&nbsp;</a>
	                  	<%if(cloudHost.getRegion()!=null && cloudHost.getRegion()==2){ %>
		                 	<a id="modifiy_port<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn13 l modify-port" title="端口详情" modifyPort="1">&nbsp;</a>	
	                  <%}} %>
	                  <a href="javascript:void(0);" class="graybutton r my_cloud_host_detail"><div class="r">主机详情</div><div class="btn10 l">&nbsp;</div></a>
                    </div>	
					<%}else if("运行".equals(cloudHost.getSummarizedStatusText())) {%>
						<div class="listright l" name="a<%=cloudHost.getId()%>">
		                  <a id="start<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn1 alpha l launch_host" title="开机" startHost="2">&nbsp;</a>
		                  <a id="shutdown<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn2 l shutdown_host" title="关机" stopHost="1">&nbsp;</a>
		                  <a id="restart<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn3 l restart_host" title="重启" restartHost="1">&nbsp;</a>
		                  <a id="halt<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn4 l halt_host" title="强制关机" haltHost="1">&nbsp;</a>	
<!-- 		                  <a href="javascript:void(0);" class="graybutton l enter_my_cloud_host"><div class="r">进入主机</div><div class="btn10 l">&nbsp;</div></a> -->
 		                  <%if(cloudHost.getType()==6){ %>
		                  	<a href="javascript:void(0);" class="btn btn18 alpha l" title="断开" onclick="Unbound('<%=cloudHost.getId() %>');">&nbsp;</a>
		                    <a href="javascript:void(0);" class="btn btn7 l alpha" title="修改配置" modifyAllocation="2">&nbsp;</a>
		                    <%if(cloudHost.getRegion()!=null && cloudHost.getRegion()==2){ %>
		                 	<a id="modifiy_port<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn13 l modify-port" title="端口详情" modifyPort="1">&nbsp;</a>			  
		                  <%}}else{ %>
		                  	<a id="outOfService<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn18 l" onclick="Unbound('<%=cloudHost.getId() %>');"  title="断开" outofservice="1">&nbsp;</a>
		                    <a id="modify<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn7 l alpha modify-allocation" title="修改配置" modifyAllocation="2">&nbsp;</a>
		                 	<%if(cloudHost.getRegion()!=null && cloudHost.getRegion()==2){ %>
		                 	<a id="modifiy_port<%=cloudHost.getId() %>" href="javascript:void(0);" class="btn btn13 l modify-port" title="端口详情" modifyPort="1">&nbsp;</a>			  
		                  <%}} %>
		                  <a href="javascript:void(0);" class="graybutton r my_cloud_host_detail"><div class="r">主机详情</div><div class="btn10 l">&nbsp;</div></a>
                 	 	</div>
					<%}else if("创建中".equals(cloudHost.getSummarizedStatusText())){%>
						<div class="listright l">
		                    <div class="listinfo">您的主机正在创建中，预计3分钟后完成创建。</div>
		                    <div id="<%=cloudHost.getId() %>" style="width:240px;margin-left:20px;margin-top:0px;" text="云主机创建中({value}%)"></div>
	               		</div>
					<%}else if("停机".equals(cloudHost.getSummarizedStatusText())){%>
						<%if(cloudHost.getType()==6){ %>
							<div class="listright l">
								<div class="listinfo">
									您的主机已停用，请联系代理商。
								</div>
							</div>
						<%}else{ %>
							<div class="listright l">
			                    <div class="listinfo">您的主机已停用，我们将为您保存数据至 
			                    <%
			                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			                    Date date = StringUtil.stringToDate(cloudHost.getInactivateTime(),"yyyyMMddHHmmssSSS");
			                    Calendar calendar = Calendar.getInstance();
			                    calendar.setTime(date);
			                    calendar.add(Calendar.DAY_OF_MONTH,7);
			                    %>
			                    <%=sdf.format(calendar.getTime())%>
			                  	  ，<br />到期主机及数据将会删除。</div>
			                    <a href="javascript:void(0);" class="graybutton r reactivate"><div class="r">恢复服务</div><div class="btn11 l">&nbsp;</div></a>
		           			</div>
	           			<%} %>
					<%}else if("创建失败".equals(cloudHost.getSummarizedStatusText())){%>
						<div class="listright l" name="a<%=cloudHost.getId()%>">
		                  <a href="javascript:void(0);" class="btn btn1 alpha l" title="开机">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn2 alpha l" title="关机">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn3 alpha l" title="重启">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn4 l alpha" title="强制关机">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn5 l delete-cloud-host" title="删除">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn7 l alpha" title="修改配置">&nbsp;</a>
		                  <a href="javascript:void(0);" class="graybutton r alpha"><div class="r">主机详情</div><div class="btn10 l">&nbsp;</div></a>
                 	 	</div>
					<%}else{%>
						<div class="listright l" name="a<%=cloudHost.getId()%>">
		                  <a href="javascript:void(0);" class="btn btn1 alpha l" title="开机">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn2 alpha l" title="关机">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn3 alpha l" title="重启">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn4 l halt_host" title="强制关机" haltHost="1">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn newbtn5 l out-of-service" title="停用" outofservice="1">&nbsp;</a>
		                  <a href="javascript:void(0);" class="btn btn7 l alpha" title="修改配置">&nbsp;</a>
		                  <a href="javascript:void(0);" class="graybutton r alpha"><div class="r">主机详情</div><div class="btn10 l">&nbsp;</div></a>
                 	 	</div>
					<%}%>
                </div>
                </td>
            </tr>
            <%
          	}
            %>
              
          </tbody>
        </table>
        </div>
       
       <div id="ddfff" style="margin:10px;display:none;margin-left:70px;">
           <form class="wizard" id="cloud_host_config_form"  action="<%=request.getContextPath()%>/bean/page.do" target="_blank" method="post">
           
            <input type="hidden" name="vpcId" value="<%=vpcId%>"/>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">创建主机个数</span></div>
            <div class="l" style="width:100px; align:left;"><input class="easyui-numberspinner"   id="hostAmount" name="hostAmount" onchange="getDescriptionAndPrice();" precision="0" min="1" value="1" increment="1" style="height:28px; width:100px; margin-left:5px;text-align:center;"/>
            <lable id="for_vpchosts" style="color:red;font-size:13px;"></lable>
            </div>
            <div class="clear"></div>
             <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">推荐配置</span></div>
            <div class="l" style="width:600px;"> 
	            <%
	              int i = 1;
	              for(PackagePriceVO vo : packageInfo){ 
	          	  %>
	                <input name="item" id="item1_<%=i %>" onclick="getDescriptionAndPrice();" class="radio" type="radio" value="<%=vo.getId() %>"  price="<%=vo.getPrice()%>" cpu="<%=vo.getCpuCore()%>" memory="<%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>" disk="<%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>" bandwidth="<%=FlowUtil.toMbpsValue(vo.getBandwidth(), 0)%>"/>
                    <label for="item1_<%=i %>" class="biglabel l"><%=vo.getDescription() %></label>
	          	  <%
	          	    i++;
	              }
	            %>                         
              <input name="item" id="item6" class="radio" type="radio" value="" />
              <label for="item6" class="biglabel r" >自定义</label>
            </div>
            <div class="clear"></div>
            <div id="diy" style="display:none">
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">CPU</span></div>
            <div class="l" style="width:600px;">
 	            <%
	              i = 1;
	              for(PackagePriceVO vo : cpuRegion){ 
	          	  %>
	              <input name="cpu" id="cpuRegion1_<%=i %>"  onclick="getDescriptionAndPrice();"  class="radio" type="radio" value="<%=vo.getCpuCore() %>"  />
	              <label for="cpuRegion1_<%=i %>" class="smalllabel l"><%=vo.getCpuCore() %>核</label>
	          	  
	          	  <%
	          	    i++;
	              }
	            %>     
            
            </div>
            <div class="clear"></div>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">内存</span></div>
            <div class="l" style="width:600px;">
 	            <%
	              i = 1;
	              for(PackagePriceVO vo : memoryRegion){ 
	          	  %>
	              <input name="memory" id="memoryRegion1_<%=i %>"  onclick="getDescriptionAndPrice();"  class="radio" type="radio" value="<%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>"  />
                  <label for="memoryRegion1_<%=i %>" class="smalllabel l"><%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>G</label>
	          	  <%
	          	    i++;
	              }
	            %>            
             
            </div>
            <div class="clear"></div>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">硬盘</span></div>
            <div class="l" style="width:600px;">
 	            <%
	              i = 1;
	              for(PackagePriceVO vo : diskRegion){ 
	          	  %>
	          	  <input name="dataDisk" id="diskRegion1_<%=i %>"  onclick="getDescriptionAndPrice();"  class="radio" type="radio" value="<%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>"   />
                  <label for="diskRegion1_<%=i %>" class="smalllabel l"><%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>G</label> 
	          	  <%
	          	    i++;
	              }
	            %>            
             
            
              
              <div class="custom smalllabel l" style="border:none; margin:0;">
                <input  onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" maxlength="5" id="dataDisk" name="dataDiskDIY" type="text" class="smalllabel" title="G"/>
                <label for="dataDisk" class="smalllabel l">自定义(G)</label>
              </div>
            </div>
            <div class="clear"></div>
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">带宽</span></div>
            <div class="l" style="width:600px;">
             
	            <%
	              i = 1;
	              for(PackagePriceVO vo : bandwidthRegion){ 
	          	  %>
	          	  <input name="bandwidth" id="bandwidthRegion1_<%=i %>"  onclick="getDescriptionAndPrice();"  class="radio" type="radio" value="<%=FlowUtil.toMbpsValue(vo.getBandwidth(), 0)%>"   />
                  <label for="bandwidthRegion1_<%=i %>" class="smalllabel l"><%=FlowUtil.toMbpsValue(vo.getBandwidth(), 0)%>M</label>
	          	  <%
	          	    i++;
	              }
	            %>            
             
            
              
              <div class="custom smalllabel l" style="border:none; margin:0;">
                <input onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" maxlength="5" id="bandwidth" name="bandwidthDIY" type="text" class="smalllabel" title="M"/>
                <label for="bandwidth" class="smalllabel l">自定义(M)</label>
              </div>
            </div>
           
            </div>
            <div class="clear"></div>
            
            <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px;margin:0 15px 10px 0">镜像</span></div>
            <div class="l" style="width:210px;"> 
               <select class="easyui-combobox" id="sysImageId" name="sysImageId" data-options="width:'200',height:'30',panelHeight:'auto',editable:false"> 
               <%
	              i = 1;
	              for(SysDiskImageVO vo : imageList){ 
 	          	  %>
	          	  
	          	  <option value="<%=vo.getId()%>"><%=vo.getName() %></option>
	          	  <% 
	              }
	            %> 
                </select>
                
               
            </div>
                
              <div class="clear"></div>
             
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2">
            <br/>
            <a class="bluebutton r" href="javascript:void(0);" onclick="getNewCreate();" style="margin-left:10px;">确认添加</a> &nbsp;&nbsp;
            <a class="graybutton r" href="javascript:void(0);" onclick="$.unblockUI();">取消</a> 
            <div id="description_2"  style="width:300px;margin-top:-30px;text-align:left;">
            <i>单价：<strong>0.1元/小时</strong>　约合：<strong>72元/月</strong></i>&nbsp;
             </div>
              </div> 
              
              </form>
          </div>
       <div id="host_already_exsit" style="margin:10px;display:none;margin-left:70px;">
           <div class="l" style="width:120px;"><span class="bluebutton" style="width:105px; margin:0 15px 10px 0">选择已有主机</span></div>
            <div class="l" style="width:600px;display:inline; " id="hostSelected">
                   <%
 	              for(CloudHostVO vo : hostList){
 	            	  if(vo.getRegion() != vpcVo.getRegion() || StringUtil.isBlank(vo.getRealHostId())){
 	            		  continue;
 	            	  }
 	          	  %>
	              <input name="hostId" id="host_<%=vo.getId() %>"  class="checkbox" type="checkbox"       value="<%=vo.getId() %>" />
	              <label style="width: 180px;overflow: hidden;white-space: nowrap;text-overflow: ellipsis;" for="host_<%=vo.getId() %>" class="biglabel l" title=" <%=vo.getDisplayName() %> - <%=vo.getCpuCore()%>核/<%=CapacityUtil.toGBValue(vo.getMemory(), 0) %>G/<%=CapacityUtil.toGBValue(vo.getDataDisk(), 0) %>G/<%=CapacityUtil.toMBValue(vo.getBandwidth(), 0) %>M">
	              <%=vo.getDisplayName() %>      
	              </label>
	              <%
	              }
	              %>
	               
               
              
                </div>
             <div class="clear"></div>
             
            <div class="buttonbar" style="border-top:solid 1px #b2b2b2">
            <br/>
            <a class="bluebutton r" href="javascript:void(0);" onclick="toBinding('<%=vpcVo.getId() %>');" style="margin-left:10px;">确认添加</a> &nbsp;&nbsp;
            <a class="graybutton r" href="javascript:void(0);" onclick="$.unblockUI();">取消</a> 
            <div id="description_1" style="width:300px;padding-top:-10px;">
             
             </div>
              </div>   
          </div>
    </div>
    <div class="clear"></div>
  	<div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
  </div>
</div> 
</body>
</html> 
