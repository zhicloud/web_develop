<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
	String userId = loginInfo.getUserId();
%>
<!DOCTYPE html>
<!-- my_cloud_host.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
	    <meta http-equiv="Content-Type" content="textml; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>用户 - 我的云主机</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		html {
			width: 100%;
		}
		body {
			width: 100%;
		}
		/************/
		.panel-header {
			border-top: 0px;
			border-bottom: 1px solid #dddddd;
		}
		.panel-header, 
		.panel-body {
			border-left: 0px;
			border-right: 0px;
		}
		.panel-body {
			border-bottom: 0px;
		}
		/**********/
		.cloud_host_item_label {
			display: inline-block;
			padding: 5px 10px 5px 10px;
		}
		</style>
	</head>
	<body>
		<form id="big_form" style="padding:0; border:0; margin:0;" method="post">

			<div id="titlebar" class="panel-header"  >
				<div class="panel-title">我的云主机</div>
				<div class="panel-tool"></div>
			</div>
			
			<div id="toolbar" class="datagrid-toolbar" style="padding: 3px;">
				<div style="display: table;width: 100%;" > 
					<div style="display: table-cell; text-align: right;" >
						<span style="position:relative; top:2px;">云主机：</span> 
						<input type="text" id="cloud_host_name" style="position:relative; top:2px; height:18px;" /> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_agent_btn">查询</a>
					</div>
				</div>
			</div>
			
			<div id="my_cloud_host_item_all_box" class="datagrid-toolbar" style="height:400px; border:0px; overflow: auto;">
			</div>
			
		</form>
	</body>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/pay.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/md5.js"></script>
	<script type="text/javascript">
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	
	// 初始化
	$(document).ready(function(){
		$("#buy_cloud_host_btn").click(function(){
			window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=paymentService&method=execute";
		});
		
		var self = {};
		self.hoverCloudHostId = "";
		var userId = "<%=userId%>";
		
		
		// 查询的函数
		self.query = function(){
			// 先消除菜单的之前的事件响应
			$("#cloud_host_item_other_oper_menu").remove();
			// 清除之前的内容，然后加载新的内容
			$("#my_cloud_host_item_all_box").empty().load(
				"<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudUserService&method=myCloudHostQueryResultPartPage",
				{
					"cloudHostName" : $("#cloud_host_name").val().trim()
				},
				function(){
					// 解析jeasyui元素
					$.parser.parse(this);
					// 分配事件
					// 在鼠标移上去的时候将cloud_host_id存起来
					$("#my_cloud_host_item_all_box .cloud_host_item_outer_box").hover(function(evt){
						self.hoverCloudHostId = $(this).attr("cloudHostId");
					});
					// 查看详情
					$("#my_cloud_host_item_all_box .view-detail").click(function(evt){
						self.cloudHostViewDetail(self.hoverCloudHostId);
					});
					// 启动
					$("#my_cloud_host_item_all_box .launch").click(function(evt){
						self.startCloudHost();
					});
					// 关机
					$("#my_cloud_host_item_all_box .shutdown").click(function(evt){
						self.haltCloudHost();
					});
					// 重启
					$("#my_cloud_host_item_all_box .restart").click(function(evt){
						self.restartCloudHost();
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
						top.showSingleDialog({
							url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=addPortPage&hostId="+encodeURIComponent(self.hoverCloudHostId)
						});
					});
					// 修改配置
					$("#cloud_host_item_other_oper_menu .modify-allocation").click(function(){
						top.showSingleDialog({
							url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=modifyAllocationPage&hostId="+encodeURIComponent(self.hoverCloudHostId),
							onClose:function(data){
								$('#terminal_user_datagrid').datagrid('reload');
							}
						});
					});
					// 绑定云终端
					$("#cloud_host_item_other_oper_menu .binding-terminal").click(function(){
						top.showSingleDialog({
							url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=cloudTerminalBindingPage&hostId="+encodeURIComponent(self.hoverCloudHostId)+"&userId="+encodeURIComponent(userId)
						});
						 
					});
					// 修改监控密码
					$("#cloud_host_item_other_oper_menu .modify_password").click(function(){
						top.showSingleDialog({
							url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=updatePasswordPage&hostId="+encodeURIComponent(self.hoverCloudHostId)+"&userId="+encodeURIComponent(userId)
						});
					});
					// 申请停机
					$("#cloud_host_item_other_oper_menu .inactivate").click(function(){
						self.inactivateCloudHost();
					});
					// 申请停机恢复
					$("#cloud_host_item_other_oper_menu .reactivate").click(function(){
						self.reactivateCloudHost();
					});
					// 删除云主机
					$("#cloud_host_item_other_oper_menu .delete-cloud-host").click(function(){
						if( self.hoverCloudHostId.trim()=="" )
						{
							return;
						}
						top.$.messager.confirm("确认", "确定删除?", function (r) {  
					        if (r) {  
								ajax.remoteCall("bean://cloudHostService:deleteCloudHostByIds",
									[ [self.hoverCloudHostId] ], 
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
											self.query();
										}
									}
								);
					        }  
					    }); 
					});
				}
			);
		};
		// 启动云主机
		self.startCloudHost = function()
		{
			if( self.hoverCloudHostId=="" )
			{
				return;
			}
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=startCloudHostPage",
				params: {
					"cloudHostId" : self.hoverCloudHostId
				},
				onClose : function(data) {
					self.query();
				}
			});
		};
		
		// 关机
		self.haltCloudHost = function()
		{
			if( self.hoverCloudHostId=="" )
			{
				return;
			}

			top.$.messager.confirm("确认", "确定要强制关闭正在运行的云主机吗?", function (r) {  
		        if (r) { 
		        	ajax.remoteCall("bean://cloudHostService:haltCloudHost",
	    				[ self.hoverCloudHostId ], 
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
		
		// 重启
		self.restartCloudHost = function()
		{
			if( self.hoverCloudHostId=="" )
			{
				return;
			}
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=restartCloudHostPage",
				params: {
					"cloudHostId" : self.hoverCloudHostId
				},
				onClose : function(data) {
					self.query();
				}
			});
		};
		
		// 加载光盘镜像
		self.insertIsoImage = function()
		{
			if( self.hoverCloudHostId=="" )
			{
				return;
			}
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=insertIsoImagePage",
				params: {
					"cloudHostId" : self.hoverCloudHostId
				},
				onClose : function(data) {
					self.query();
				}
			});
		};
		
		// 弹出光盘镜像
		self.popupIsoImage = function()
		{
			if( self.hoverCloudHostId=="" )
			{
				return;
			}
			ajax.remoteCall("bean://cloudHostService:popupIsoImage",
   				[ self.hoverCloudHostId ], 
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
			if( self.hoverCloudHostId=="" )
			{
				return;
			}
			ajax.remoteCall("bean://cloudHostService:inactivateCloudHost",
   				[ self.hoverCloudHostId ], 
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

		// 申请停机恢复
		self.reactivateCloudHost = function()
		{
			if( self.hoverCloudHostId=="" )
			{
				return;
			}
			ajax.remoteCall("bean://cloudHostService:reactivateCloudHost",
   				[ self.hoverCloudHostId ], 
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
		
		// 查看云主机详情
		self.cloudHostViewDetail = function(cloudHostId){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=cloudHostViewDetailPage",
				params: {
					"cloudHostId" : cloudHostId
				},
				onClose : function(data) {
					self.query();
				}
			});
		};
		
		// 查询
		$("#query_agent_btn").click(function(){
			self.query();
		});
		
		// 创建云主机
		$("#create_cloud_host_btn").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=createCloudHostPage",
				onClose : function(data) {
					self.query();
				}
			});
		});
		
		$('#cloud_host_name').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	$("#query_agent_btn").click();
	        	return false;
	        }
	    });
		// 布局初始化
		$("#my_cloud_host_item_all_box").height($(document.body).height() - $("#titlebar").height() - $("#toolbar").height() - 20);
		// 初始仳
		self.query();
	});
	</script>
</html>



