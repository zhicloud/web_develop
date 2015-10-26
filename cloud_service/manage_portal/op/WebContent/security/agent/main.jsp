<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_AGENT;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- main.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		<script  language="JavaScript" type="text/javascript"> 
		var $buoop = {vs:{i:8,f:20,o:15,s:5.1}}; 
		$buoop.ol = window.onload; 
		window.onload=function(){ 
			try {if ($buoop.ol) $buoop.ol();}catch (e) {} 
			var e = document.createElement("script"); 
			e.setAttribute("type", "text/javascript"); 
			e.setAttribute("src", "//browser-update.org/update.js"); 
			document.body.appendChild(e); 
		};
		</script>
		<title>代理商 - 云端在线</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css" /> 
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
					    <li><a><span>+</span>账户管理</a>
					      <ul>
				    	    <li><a href="#" id="base_info_link" class="navbarnavactive">基本信息</a></li> 
					    	<li><a href="#" id="change_password_link">修改密码</a></li> 
					    	<li><a href="#" id="recharge_link">账户充值</a></li> 
					    	<li><a href="#" id="recharge_detail_link">充值记录</a></li>  
					    	<li><a href="#" id="consumption_detail_link">消费记录</a></li>  
					      </ul>
					    </li>
				    	<li><a href="#"  id="terminal_user_manage_link">终端用户管理</a></li> 
				    	<li><a><span>+</span>业务管理</a>
					      <ul>
							<li><a href="#" id="business_graphics_link">业务图表</a></li>
					    	<li><a href="#" id="business_statistics_link">业务统计</a></li>
					    	<li><a href="#" id="business_detail_link">业务明细</a></li> 
					      </ul>
					    </li>
				    	<li><a href="#" id="invoice_manage_link">发票管理</a></li>
				    	<li><a href="#" id="operation_log_link">操作日志</a></li>  
<!-- 				    	<li><a href="#" id="cloud_host_manage_link">用户云主机管理</a></li>  -->
<!-- 				    	<li><a href="#" id="bill_inquiry_link">账单查询</a></li> -->
<!-- 				    	<li><a href="#" id="statistics_report_link">报表统计</a></li> -->
<!-- 						<li><a href="#" id="cash_coupon_link">我的代金券</a></li> -->
<!-- 						<li><a href="#" id="invite_code_link">我的邀请码</a></li> -->
					</ul>
					
				</td>
				<td id="c2" class="control" style="width:-1px; vertical-align:top;">
				
					<div id="c2_r1" class="controlheader" style="height:60px; position:relative; background:#fcfcfc; border-bottom:solid 1px #acc0b7;">
						 
						<div class="controlheaderlogout r">
							<a href="#" id="logout_link">注销</a>
						</div>
						<div class="controlheaderuser r"><%=loginInfo.getAccount()%>
						</div>
						<div id="productmenu" style="position:absolute; left:69px; top:60px; width:96px; height:180px;
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
						</div>
					</div>
					
					<div id="c2_r2" style="height:-1px;">
						<iframe id="content_frame" scrolling="yes" frameborder="0" hspace="0" vspace="0" style="width:100%; height:100%; border:0px solid #005500;"
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
						top.location.href = "<%=request.getContextPath()%>/agent.do";
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
		// 基本信息
		if( target.id=="base_info_link" )
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=basicInformationPage");
		}
		// 终端用户管理
		else if( target.id=="terminal_user_manage_link" )
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=terminalUserService&method=managePageForAgent");
		}
		// 云主机管理
		else if( target.id=="cloud_host_manage_link" )
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=managePage");
		}
		// 账单查询
		else if( target.id=="bill_inquiry_link" )
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=billService&method=managePage");
		}
		// 发票管理
		else if( target.id=="invoice_manage_link" )
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceService&method=managePageForAgent");
		}
		// 统计报表
		else if( target.id=="statistics_report_link" )
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=statementService&method=managePage");
		}
		// 业务明细
		else if( target.id=="business_detail_link" )
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=businessStatisticsService&method=toBusinessDetailManagePage");
		}
		// 业务统计
		else if( target.id=="business_statistics_link" )
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=businessStatisticsService&method=agentBusinessStatistics");
		}
		// 投诉处理
		else if( target.id=="complaint_manage_link" )
		{
		}
		// 邀请码
		else if( target.id=="invite_code_link")
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=inviteCodeService&method=agentInviteCodePage");
		}
		//代金券
		else if( target.id=="cash_coupon_link")
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cashCouponService&method=agentCashCouponPage");
		}
		// 修改密码
		else if( target.id=="change_password_link" )
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=updatePasswordPage");
		}
		// 修改密码
		else if( target.id=="recharge_link" )
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=rechargePage");
		}
		// 充值明细
		else if( target.id=="recharge_detail_link" )
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=toRechargeRecordPage");
		}
		// 消费明细
		else if( target.id=="consumption_detail_link" )
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=toConsumptionRecordPage");
		}
		// 业务图表
		else if( target.id=="business_graphics_link" )
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=statementService&method=businessGraphicsPage");
		}
		// 操作日志
		else if( target.id=="operation_log_link" )
		{
			$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=operLogService&method=managePageForAgent");
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
		// iframe的事件
// 		$("#content_frame").load(function(){
// 			$(this).height($(this).contents().find("body").children(":first").outerHeight()); 
// 		}); 
		// 注销
		$("#logout_link").click(function(){
			logout();
		});
		//
		$("#content_frame").attr("src", "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=basicInformationPage");
	});
	
	$(document).ready(function(){
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