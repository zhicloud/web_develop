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
		<script type="text/javascript"> 
			var $buoop = {vs:{i:8,f:20,o:15,s:5.1}}; 
			$buoop.ol = window.onload; 
			window.onload = function(){ 
				try {
					if ($buoop.ol) $buoop.ol();
				} catch (e) {
				} 
				var e = document.createElement("script"); 
				e.setAttribute("type", "text/javascript"); 
				e.setAttribute("src", "//browser-update.org/update.js"); 
				document.body.appendChild(e); 
			};
		</script>
		<title>运营商 - 云端在线</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css"> 
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/control2.css" />
		<style type="text/css">
		body
		{
			overflow-y: scroll; 
		}
		</style>
	</head>

	<body>
		
		<table id="main_table" border="0" cellspacing="0" cellpadding="0" style="height:100%;">
			<tr>
				<td id="c1" class="navbar" style="width:200px; vertical-align:top;">
					<div class="navbarlogo">
						<a href="<%=request.getContextPath()%>/"><img
							src="<%=request.getContextPath()%>/control/logo_145_30.png"
							width="145" height="30" alt="云端在线" />
						</a>
					</div>
					<ul>
						<li><a><span>+</span>用户管理</a>
					      <ul>
					      	<li><a href="#" id="mark_manage_link">标签管理</a></li>
				    	    <li><a href="#" id="agent_manage_link">代理商管理</a></li>
							<li><a href="#" id="terminal_user_manage_link">终端用户管理</a></li>
							<li><a href="#" id="view_all_user_oper_log">用户日志</a></li>
							<li><a href="#" id="bill_manage_link">账单查询</a></li>
					      </ul>
					    </li>
					    
					    <li><a><span>+</span>产品管理</a>
					      <ul>
					      	<li><a href="#" id="self_use_cloud_host_link">自用云主机</a></li>
							<li><a href="#" id="cloud_host_manage_link">用户云主机管理</a></li>
							<li><a href="#" id="vpc_manage_link">用户专属云管理</a></li>
							<li><a href="#" id="cloud_disk_manage_link">云硬盘管理</a></li>
					      </ul>
					    </li>				    

						<li><a><span>+</span>套餐管理</a>
					      <ul>
					      	<li><a href="#" id="package_option_manage_link">套餐项管理</a></li>
							<li><a href="#" id="package_price_manage_link">套餐价格自定义</a></li>
					      </ul>
					    </li>					    

					    <li><a><span>+</span>资源管理</a>
					      <ul>
					      	<li><a href="#" id="computer_resource_manage_link">主机资源池管理</a></li>
							<li><a href="#" id="storage_resource_manage_link">存储资源池管理</a></li>
							<li><a href="#" id="address_resource_manage_link">地址资源池管理</a></li>
							<li><a href="#" id="port_resource_manage_link"> 端口资源池管理</a></li>
							<li><a href="#" id="database_monitor_link"> 数据库监控</a></li>
							<li><a href="#" id="platform_resource_monitor_link"> 平台资源监控</a></li>
					      </ul>
					    </li>
					     
						<li><a href="#" id="intelligent_rout_manage">智能路由设置</a></li> 	

						<li><a><span>+</span>仓库和镜像管理</a>
					      <ul>
							<li><a href="#" id="sys_disk_image_manage_link">系统镜像管理</a></li>
							<li><a href="#" id="warehouse_manage_link">仓库类型管理</a></li>
					      </ul>
					    </li>
					    
					    <li><a><span>+</span>统计分析</a>
					      <ul>
							<li><a href="#" id="report_statistic_link">报表统计</a></li>
					      </ul>
					    </li>	
					    
					    <li><a><span>+</span>财务管理</a>
					      <ul>
							<li><a href="#" id="cash_coupon_link">现金券管理</a></li>
							<li><a href="#" id="invoice_manage_link">发票管理</a></li>
					      </ul>
					    </li>	
					    
					    <li><a><span>+</span>系统管理</a>
					      <ul id="my_ul">
				    		<li><a href="#" id="operator_basic_information_link" class="navbarnavactive">基本信息</a></li>
							<li><a href="#" id="operator_update_password_link">修改密码</a></li>
							<li><a href="#" id="operator_suggestion_link">意见反馈</a></li>
					      </ul>
					    </li>
 						<li><a><span>+</span>邮件管理</a> 
 							<ul>
 								<li><a href="#" id="email_config_link" class="navbarnavactive">配置管理</a></li>
 								<li><a href="#" id="email_template_link">模版管理</a></li>
 								<li><a href="#" id="email_record_link">发送记录</a></li>
 							</ul> 
 						</li>
                        <li><a><span>+</span>短信管理</a>
                            <ul>
                                <li><a href="#" id="sms_config_link" class="navbarnavactive">配置管理</a></li>
                                <li><a href="#" id="sms_template_link">模版管理</a></li>
                                <li><a href="#" id="sms_record_link">发送记录</a></li>
                            </ul>
                        </li>
					    <li><a href="#" id="egg_plan_manage_link">蛋壳计划管理</a></li>	
					    <li><a href="#" id="image_host_application_manage_link">镜像主机申请管理</a></li>	
					    
					     <li><a><span>+</span>监控信息管理</a>
					      <ul>
					       	<li><a href="#" id="operator_service_link">服务管理</a></li>		
							<li><a href="#" id="total_monitor_link">总体概况查看</a></li>
							<li><a href="#" id="room_monitor_link">机房信息查看</a></li>
							<li><a href="#" id="rack_monitor_link">机架信息查看</a></li>
							<li><a href="#" id="server_monitor_link">服务器信息查看</a></li>
							<li><a href="#" id="cloudhost_monitor_link">云主机信息查看</a></li>
							<li><a href="#" id="sys_warnrule_link">预警规则管理</a></li>
							<li><a href="#" id="sys_resource_link">资源监控管理</a></li>
							<li><a href="#" id="sys_total_link">系统全局监控</a></li>
					      </ul>
					    </li>		    					    					    
  					</ul>
				</td>
				<td id="c2" class="control" style="width:-1px; vertical-align:top;">
				
					<div id="c2_r1" class="controlheader" style="height:60px; position:relative; background:#fcfcfc; border-bottom:solid 1px #acc0b7;">
						<%-- <div class="controlheaderbutton l">
							<a href="<%=request.getContextPath()%>/" style="width: 28px;">首页</a>
						</div>
						<div class="controlheaderbutton productbutton l">
							<a href="#" style="width: 28px;">产品</a>
						</div>
						<div class="controlheaderbutton l">
							<a href="<%=request.getContextPath()%>/user/buy.do"
								style="width: 70px;">定制云主机</a>
						</div>
						<div class="controlheaderbutton l">
							<a href="<%=request.getContextPath()%>/public/downloadPage.do"
								style="width: 56px;">相关下载</a>
						</div> --%>
						<div class="controlheaderlogout r">
							<a href="#" id="logout_link">注销</a>
						</div>
						<div class="controlheaderuser r"><%=loginInfo.getAccount()%>
						</div>
						<%-- <div id="productmenu" style="position:absolute; left:69px; top:60px; width:96px; height:180px;
							background:#acc0b7; border:solid 1px #acc0b7; display:none;">
							<div>
								<a href="<%=request.getContextPath()%>/user/cloudsever.do">云主机</a>
							</div>
							<div>
								<a href="<%=request.getContextPath()%>/user/cloudstorage.do">云存储</a>
							</div>
							<div>
								<a href="<%=request.getContextPath()%>/user/database.do">云数据库</a>
							</div>
							<div>
								<a href="<%=request.getContextPath()%>/user/banancing.do">负载均衡</a>
							</div>
							<div>
								<a href="<%=request.getContextPath()%>/user/sdn.do">SDN</a>
							</div>
							<div>
								<a href="<%=request.getContextPath()%>/user/recovery.do">云容灾</a>
							</div>
						</div> --%>
					</div>
					
					<div id="c2_r2" style="height:-1px;">
						<iframe id="content_frame" scrolling="no" frameborder="0" hspace="0" vspace="0" style="width:100%; height:100%; border:0px solid #005500;"
							src=""
							></iframe>
					</div>
					
					<div id="c2_r3" style="height:60px; line-height:60px; background:#f0f0f0; text-align:center; border-top:solid 1px #acc0b7;">
						<div style="color:#668e90;">
							Copyright &copy; 2014 
							<a href="http://www.zhicloud.com" target="_blank" style="color:#00ab51;">致云科技有限公司</a>,
							All rights reserved.
							蜀ICP备14004217号-1
						</div>
					</div>
				</td>
			</tr>
		</table>
		
	</body>
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
	
	// 注销
	function logout()
	{
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
	
	function isSwitchable(target)
	{
		var result = true;
		if( $("#content_frame").prop("contentWindow").onBeforeExit!=null )
		{
			result = $("#content_frame").prop("contentWindow").onBeforeExit(target);
		}
		return (result==false) ? false : true;
	}
	
	function onSwitch(target)
	{
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
		else if( target.id=="view_all_user_oper_log" )
		{
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
	}
	
	$(function(){
		// 布局初始化
		$("#c2").css("width", $(document.body).width() - $("#c1").width());
		$("#c2_r2").css("height", $("#c2").height() - $("#c2_r1").height() - $("#c2_r3").height() - 2);
		
		// 左边按钮的事件
		$(".navbar li a").click(function(){
			if( isSwitchable(this)==true )
			{
				$(".navbarnavactive").removeClass("navbarnavactive");
				$(this).addClass("navbarnavactive");
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
// 		iframe的事件
// 		$("#content_frame").load(function(){
// 			$(this).height($(this).contents().find("body").children(":first").outerHeight()); 
// 		}); 
		// 注销
		$("#logout_link").click(function(){
			logout();
		});
		//
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=operatorService&method=basicInformationPage");
	});
	$(document).ready(function(){
		$("#my_ul").slideDown(0);
		  $(".navbar li a").click(function(){
		    if($(this).find("span").html()=="+"){
				$(".navbar > ul > li > a > span:contains('-')").html("+");
				$(".navbar > ul > li > ul").slideUp(200);
				$(this).find("span").html("-");
				$(this).next("ul").slideDown(200);
			}else if($(this).find("span").html()=="-"){
				$(this).find("span").html("+");
				$(this).next("ul").slideUp(200);
			}else{
				$(".navbarnavactive").removeClass("navbarnavactive");
				$(this).addClass("navbarnavactive");
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
</html>
