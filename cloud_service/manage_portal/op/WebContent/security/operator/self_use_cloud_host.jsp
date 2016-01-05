<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
    LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_OPERATOR);
    String userId = loginInfo.getUserId();
%>
<!DOCTYPE html>
<!-- self_use_cloud_host.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商 - 自用云主机管理</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
</head>
	
<body>
<div class="oper-wrap">
	<div class="tab-container">
		<div id="titlebar" class="panel-header">
			<div class="panel-title">自用云主机管理</div>
			<div class="panel-tool"></div>
		</div>
		<div id="toolbar" class="datagrid-toolbar" style="padding: 3px;">
			<div style="display: table; width: 100%;">
				<div style="display:table-cell; text-align:left">
					<a class="easyui-linkbutton oper-btn-sty" href="javascript:;" data-options="plain:true,iconCls:'icon-print'" id="create_cloud_host_btn">创建云主机</a> 
				</div>
				<div style="display:table-cell; text-align:right;">
					<span class="sear-row">
						<label class="f-mr5">云主机</label>
						<input type="text" id="cloud_host_name" class="messager-input" /> 
					</span>
					<span class="sear-row">
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-search'" id="query_agent_btn">查询</a>
					</span>
				</div>
			</div>
		</div>
		<div id="cloud_host_item_all_box" class="oper-item-sty" style="overflow-y:auto;background:#fff;"></div>
	</div>
</div>
<!-- JavaScript_start -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;

var asyncAjax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
asyncAjax.async = true;

//布局初始化
$("#cloud_host_item_all_box").height($(document.body).height() - $("#titlebar").height() - $("#toolbar").height() - 43);

// 初始化
$(document).ready(function(){
	var self = {};
	self.hoverCloudHostId = "";
	self.hoverCloudHostName = "";
	var userId = "<%=userId%>";
	
	// 查询的函数
	self.query = function(){
		// 先消除菜单的之前的事件响应
		$("#cloud_host_item_other_oper_menu").remove();
		// 清除之前的内容，然后加载新的内容
		$("#cloud_host_item_all_box").empty().load(
			"<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=cloudHostQueryResultPartPage",
			{
				"cloudHostName" : $("#cloud_host_name").val().trim()
			},
			function(){
				// 解析jeasyui元素
				$.parser.parse(this);
				// 分配事件
				// 在鼠标移上去的时候将cloud_host_id存起来
				$("#cloud_host_item_all_box .cloud_host_item_outer_box").hover(function(evt){
					self.hoverCloudHostId = $(this).attr("cloudHostId").trim();
					self.hoverCloudHostName = $(this).attr("cloudHostName").trim();
				});
				// 查看详情
				$("#cloud_host_item_all_box .view-detail").click(function(evt){
					self.cloudHostViewDetail(self.hoverCloudHostId);
				});
				// 启动
				$("#cloud_host_item_all_box .launch").click(function(evt){
					self.startCloudHost();
				});
				// 关机
				$("#cloud_host_item_all_box .shutdown").click(function(evt){
					self.haltCloudHost();
				});
				// 重启
				$("#cloud_host_item_all_box .restart").click(function(evt){
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
						// add_port.jsp
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
				// 删除云主机
				$("#cloud_host_item_other_oper_menu .delete-cloud-host").click(function(){
					if( self.hoverCloudHostId.trim()=="" )
					{
						return;
					}
					top.$.messager.confirm("确认", "确定要删除吗?", function (r) {  
				        if (r) { 
							ajax.remoteCall("bean://cloudHostService:deleteCloudHostById",
								[ self.hoverCloudHostId ], 
								function(reply) {
									if (reply.status=="exception")
									{
										if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
										{
											top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
												top.location.reload();
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
    								top.location.reload();
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
  								top.location.reload();
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
	// 初始仳
	self.query();
});
</script>
</body>
</html>



