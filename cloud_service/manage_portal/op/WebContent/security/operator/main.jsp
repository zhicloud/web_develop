<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_OPERATOR;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- main.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商 - 云端在线</title>
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/newoperator/common/img/favicon.ico" type="image/x-icon" /> 
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/newoperator/common/css/global.css" media="all"/>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/newoperator/css/oper_common.css" media="all"/>
   	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/newoperator/css/operation.css" media="all"/>
	<style type="text/css">body{overflow-y: scroll;}</style>
	<script type="text/javascript"> 
	var $buoop = {vs:{i:8,f:20,o:15,s:5.1}}; 
	$buoop.ol = window.onload; 
	window.onload = function(){ 
		try { if ($buoop.ol) $buoop.ol(); } catch (e) { } 
		var e = document.createElement("script"); 
		e.setAttribute("type", "text/javascript"); 
		e.setAttribute("src", "//browser-update.org/update.js"); 
		document.body.appendChild(e); 
	};
	</script>
</head>

<body>
<!-- header_start -->
<div class="g-hd">
	<div class="header-info">
		<div class="hd-left-cont">
			<a class="l-logo" href="javascript:;"><img src="<%=request.getContextPath()%>/newoperator/img/logo.png" alt="logo" /></a>
			<h3 class="f-ml05">致云公有云运营平台v2.0</h3>
		</div>
		<div class="hd-right-cont">
			<img src="<%=request.getContextPath()%>/newoperator/img/portrait.png" alt="portrait" />
			<label class="f-ml10">29447403799@sian.cn | <a href="#" id="oper_logout"><font color="white">注销</font></a></label>
		</div>
	</div>
</div>
<!-- header_end -->

<!-- slide_start -->
<div class="g-sd">
	<div class="nav-list">
		<div class="nav-item">
			<a class="oper-sty current single" href="javascript:void(0);" id="operator_index_link"><i class="icon-tit icon-operational-overview"></i>运营概览</a>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascript:void(0);"><i class="icon-tit icon-user-management"></i>用户管理<i class="icon-arrow icon-arrow-bottom"></i></a>
			<ul>
				<li><a class="sub-sty" href="javascript:void(0);" id="mark_manage_link">标签管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="agent_manage_link">代理商管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="terminal_user_manage_link">终端用户管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="view_all_user_oper_log">用户日志</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="bill_manage_link">账单查询</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascript:void(0);"><i class="icon-tit icon-product-management"></i>产品管理<i class="icon-arrow icon-arrow-bottom"></i></a>
			<ul>
				<li><a class="sub-sty" href="javascript:void(0);" id="self_use_cloud_host_link">自用云主机</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="cloud_host_manage_link">云服务器管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="vpc_manage_link">用户专属云管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="cloud_disk_manage_link">云硬盘管理</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascript:void(0);"><i class="icon-tit icon-package-management"></i>套餐管理<i class="icon-arrow icon-arrow-bottom"></i></a>
			<ul>
				<li><a class="sub-sty" href="javascript:void(0);" id="package_option_manage_link">套餐项管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="package_price_manage_link">套餐价格自定义</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascript:void(0);"><i class="icon-tit icon-resource-management"></i>资源池管理<i class="icon-arrow icon-arrow-bottom"></i></a>
			<ul>
				<li><a class="sub-sty" href="javascript:void(0);" id="computer_resource_manage_link">计算资源池管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="storage_resource_manage_link">存储资源池管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="address_resource_manage_link">地址资源池管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="port_resource_manage_link">端口资源池管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="database_monitor_link">数据库监控</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="platform_resource_monitor_link">平台资源监控</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty single" href="javascript:void(0);" id="intelligent_rout_manage"><i class="icon-tit icon-routing-management"></i>智能路由管理</a>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascript:void(0);"><i class="icon-tit icon-whmirror-management"></i>仓库和镜像管理<i class="icon-arrow icon-arrow-bottom"></i></a>
			<ul>
				<li><a class="sub-sty" href="javascript:void(0);" id="sys_disk_image_manage_link">系统镜像管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="warehouse_manage_link">仓库类型管理</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascript:void(0);"><i class="icon-tit icon-statistical-analysis"></i>统计分析<i class="icon-arrow icon-arrow-bottom"></i></a>
			<ul>
				<li><a class="sub-sty" href="javascript:void(0);" id="report_statistic_link">报表统计</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascript:void(0);"><i class="icon-tit icon-financial-management"></i>财务管理<i class="icon-arrow icon-arrow-bottom"></i></a>
			<ul>
				<li><a class="sub-sty" href="javascript:void(0);" id="cash_coupon_link">现金券管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="invoice_manage_link">发票管理</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascript:void(0);"><i class="icon-tit icon-system-management"></i>系统管理<i class="icon-arrow icon-arrow-bottom"></i></a>
			<ul>
				<li><a class="sub-sty" href="javascript:void(0);" id="operator_basic_information_link">基本信息</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="operator_update_password_link">修改密码</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="operator_suggestion_link">意见反馈</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascript:void(0);"><i class="icon-tit icon-mail-management"></i>邮件管理<i class="icon-arrow icon-arrow-bottom"></i></a>
			<ul>
				<li><a class="sub-sty" href="javascript:void(0);" id="email_config_link">配置管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="email_template_link">模块管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="email_record_link">发送记录</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascript:void(0);"><i class="icon-tit icon-mesg-management"></i>短信管理<i class="icon-arrow icon-arrow-bottom"></i></a>
			<ul>
				<li><a class="sub-sty" href="javascript:void(0);" id="sms_config_link">配置管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="sms_template_link">模块管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="sms_record_link">发送记录</a></li>
			</ul>
		</div>
		<div class="nav-item">
			<a class="oper-sty single" href="javascript:void(0);" id="egg_plan_manage_link"><i class="icon-tit icon-eggplan-management"></i>蛋壳计划管理</a>
		</div>
		<div class="nav-item">
			<a class="oper-sty single" href="javascript:void(0);" id="image_host_application_manage_link"><i class="icon-tit icon-mirrorhost-management"></i>镜像主机申请管理</a>
		</div>
		<div class="nav-item">
			<a class="oper-sty" href="javascript:void(0);"><i class="icon-tit icon-monitinfor-management"></i>监控信息管理<i class="icon-arrow icon-arrow-bottom"></i></a>
			<ul>
			    <li><a class="sub-sty" href="javascript:void(0);" id="operator_service_link">服务管理</a></li>		
				<li><a class="sub-sty" href="javascript:void(0);" id="total_monitor_link">总体概况查看</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="room_monitor_link">机房信息查看</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="rack_monitor_link">机架信息查看</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="server_monitor_link">服务器信息查看</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="cloudhost_monitor_link">云主机信息查看</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="sys_warnrule_link">预警规则管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="sys_resource_link">资源监控管理</a></li>
				<li><a class="sub-sty" href="javascript:void(0);" id="sys_total_link">系统全局监控</a></li>
			</ul>
		</div>
	</div>
</div>
<!-- slide_end -->

<!-- iframe_start -->
<div class="g-mn"><iframe id="content_frame" scrolling="no" frameborder="0" hspace="0" vspace="0" style="width:100%; height:100%; line-height:0;display:block;" src=""></iframe></div>
<!-- iframe_end -->

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

// 显示对话框，提供给iframe使用
function showSingleDialog(param){
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

// 注销
function logout(){
	ajax.remoteCall("bean://sysUserService:logout",
		[],
		function(reply)
		{
			if( reply.status=="exception" )
			{
				top.$.messager.alert('提示','会话超时，请重新登录','info',function(){
					window.location.reload();
				});
			} 
			else if( reply.result.status=="success" )
			{
				// 登录成功
				$("<div class=\"datagrid-mask\"></div>").css({
					display:"block",
					width:"100%",
					height:"100%"
				}).appendTo("body"); 
				$("<div class=\"datagrid-mask-msg\"></div>").html("成功退出，正在跳转页面。。。").appendTo("body").css({
					display:"block",
					left:($(document.body).outerWidth(true) - 190) / 2,
					top:($(window).height() - 45) / 2
				});
				// 跳转页面
				window.setTimeout(function(){
					top.location.href = "<%=request.getContextPath()%>/operator.do";
				}, 500);
			}
			else
			{
				top.$.messager.alert('警告',reply.result.message,'warning');
			}
		}
	);
}

function isSwitchable(target){
	var result = true;
	if( $("#content_frame").prop("contentWindow").onBeforeExit!=null )
	{
		result = $("#content_frame").prop("contentWindow").onBeforeExit(target);
	}
	return (result==false) ? false : true;
}

function onSwitch(target){
	// 首页
	if( target.id=="operator_index_link"){
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=operatorService&method=operatorIndexPage");
	}else{
		$("#content_frame").css({
			'height': '100%'
		})
	}
	// 运营商基本资料
	if( target.id=="operator_basic_information_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=operatorService&method=basicInformationPage");
	}
	// 代理商管理
	else if( target.id=="agent_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=managePage");
	}
	// 终端用户管理
	else if( target.id=="terminal_user_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=terminalUserService&method=managePageForOperator");
	}
	// 自用云主机
	else if( target.id=="self_use_cloud_host_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=selfUseCloudUsePage");
	}
	// 云主机管理
	else if( target.id=="cloud_host_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=managePage");
	}
	// VPC管理
	else if( target.id=="vpc_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=vpcService&method=getAllVpcPage");
	}
	// VPC管理
	else if( target.id=="cloud_disk_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudDiskService&method=managePage");
	}
	// 系统镜像管理
	else if( target.id=="sys_disk_image_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=sysDiskImageService&method=managePage");
	}
	// 标签管理
	else if( target.id=="mark_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=markService&method=managePage");
	}
	// 套餐项管理
	else if( target.id=="package_option_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=packageOptionService&method=managePage");
	}
	// 套餐价格自定义
	else if( target.id=="package_price_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=packageOptionService&method=packagePriceManagePage");
	}
	// 账单查询
	else if( target.id=="bill_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=billService&method=managePage");
	}
	// 发票管理
	else if( target.id=="invoice_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceService&method=pendingPage");
	}
	// 报表统计
	else if( target.id=="report_statistic_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=statementService&method=managePage");
	}
	// 投诉处理
	else if( target.id=="complaint_manage_link" )
	{
	}
	//我的代金券
	else if( target.id=="cash_coupon_link")
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cashCouponService&method=operatorCashCouponPage");
	}
	// 邀请码
	else if( target.id=="invite_code_link")
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=inviteCodeService&method=operatorInviteCodePage");
	}
	// 运营商修改密码
	else if( target.id=="operator_update_password_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=operatorService&method=updatePasswordPage");
	}
	// 订购云主机管理
	else if( target.id=="order_cloud_host_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=paymentService&method=managePage");
	}
	else if( target.id=="operator_suggestion_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=suggestionService&method=managePage");
	}
	else if( target.id=="warehouse_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostWarehouseService&method=managePage");
	}
	//用户日志
	else if(target.id=="view_all_user_oper_log"){
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?type=0&userType=<%=userType%>&bean=operLogService&method=managePage");
	}
	else if( target.id=="computer_resource_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=resourcePoolService&method=computerPoolPage");
	}
	else if( target.id=="storage_resource_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=resourcePoolService&method=storagePoolPage");
	}
	else if( target.id=="address_resource_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=resourcePoolService&method=addressPoolPage");
	}
	else if( target.id=="port_resource_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=resourcePoolService&method=portPoolPage");
	}
	else if( target.id=="database_monitor_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=statementService&method=databaseMonitorPage");
	}
	else if( target.id=="platform_resource_monitor_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=platformResourceMonitorService&method=toMonitorPage");
	} 
	else if( target.id=="intelligent_rout_manage" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=intelligentRouterService&method=queryRulePage");
	} 
	else if( target.id=="operator_service_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=serviceService&method=managePage");
	}
	else if( target.id=="egg_plan_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=eggPlanService&method=eggPlanManagePage");
	}
	else if( target.id=="image_host_application_manage_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=imageHostApplicationService&method=imageHostApplicationManagePage");
	}
	else if( target.id=="email_config_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=emailConfigService&method=managePage");
	}
	else if( target.id=="email_template_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=emailTemplateService&method=managePage");
	}
	else if( target.id=="email_record_link" )
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=messageRecordService&method=managePage&type=0");
	}
       else if( target.id=="sms_config_link" )
       {
           $("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=smsConfigService&method=managePage");
       }
       else if( target.id=="sms_template_link" )
       {
           $("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=smsTemplateService&method=managePage");
       }
       else if( target.id=="sms_record_link" )
       {
           $("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=messageRecordService&method=managePage&type=1");
       }
	//资源监控——总体概况监控
	else if( target.id=="total_monitor_link")
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=monitorService&method=areaPage&menuflag=all");
	}
	//资源监控——机房信息监控
	else if( target.id=="room_monitor_link")
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=monitorService&method=roomPage&menuflag=room");
	}		
	//资源监控——机架信息监控
	else if( target.id=="rack_monitor_link")
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=monitorService&method=rackPage&menuflag=rack");
	}	
	//资源监控——服务器信息监控
	else if( target.id=="server_monitor_link")
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=monitorService&method=serverPage&menuflag=server");
	}	
	//资源监控——云主机信息监控
	else if( target.id=="cloudhost_monitor_link")
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=monitorService&method=cloudHostPage&menuflag=host");
	}	
	//资源监控——预警规则管理
	else if( target.id=="sys_warnrule_link")
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=sysWarnService&method=RuleManagePage");
	}
	//资源监控——资源监控管理
	else if( target.id=="sys_resource_link")
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=monitorService&method=hostResourcePage");
	}
	//资源监控——系统全局监控
	else if( target.id=="sys_total_link")
	{
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=monitorService&method=systemMonitorPage");
	}		
	//系统管理——注销
	else if( target.id=="operator_logout_link")
	{
		logout();
	}
}
	
$(function(){
	$(".icon-arrow").each(function(){
		$(this).removeClass("icon-arrow-bottom");
		$(this).addClass("icon-arrow-right");
		$(this).parent("a").next("ul").slideUp("fast");
	});
	
	// 左边按钮的事件
	$(".nav-item li a,.single").click(function(){
		if( isSwitchable(this)==true ){
			$(".current").removeClass("current");
			$(this).addClass("current");
			onSwitch(this);
		}
	});
	// 上排按钮的事件
	$(".productbutton > a").mouseenter(function(){
		$("#productmenu").css("display","block");
	});
	$(".productbutton > a").mouseleave(function(){
		$("#productmenu").css("display","none");
	});
	$("#productmenu").mouseenter(function(){
		$("#productmenu").css("display","block");
	});
	$("#productmenu").mouseleave(function(){
		$("#productmenu").css("display","none");
	});
// 	iframe的事件
// 	$("#content_frame").load(function(){
// 		$(this).height($(this).contents().find("body").children(":first").outerHeight()); 
// 	}); 
	// 注销
	$("#oper_logout").click(function(){
		logout();
	});
	//
	$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=operatorService&method=operatorIndexPage");
});
	
$(document).ready(function(){
	  $(".icon-arrow").parent("a").click(function(){
	    if($(this).children().last().hasClass("icon-arrow-right")){
	    	$(this).children().last().removeClass("icon-arrow-right");
	    	$(this).children().last().addClass("icon-arrow-bottom");
			$(this).next("ul").slideDown(200);
		}else if($(this).children().last().hasClass("icon-arrow-bottom")){
			$(this).children().last().removeClass("icon-arrow-bottom");
	    	$(this).children().last().addClass("icon-arrow-right");
			$(this).next("ul").slideUp(200);
		}else{
			$(".current").removeClass("current");
			$(this).addClass("current");
		}
	  });
	  $(".productbutton > a").mouseenter(function(){
		$("#productmenu").css("display","block");
	  });
		$(".productbutton > a").mouseleave(function(){
		$("#productmenu").css("display","none");
	  });
		$("#productmenu").mouseenter(function(){
		$("#productmenu").css("display","block");
	  });
		$("#productmenu").mouseleave(function(){
		$("#productmenu").css("display","none");
	  });
	});
</script>
<!-- JavaScript_end -->
</body>
</html>
