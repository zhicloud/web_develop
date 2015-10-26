<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.vo.AgentVO"%>
<%@page import="com.zhicloud.op.vo.SysRoleVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request,AppConstant.SYS_USER_TYPE_AGENT);
	String userId  = loginInfo.getUserId();
	Integer userType = AppConstant.SYS_USER_TYPE_AGENT; 
	%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>致云代理商管理平台</title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/agent.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/big.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/esl.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/agent.js"></script>
<script type="text/javascript"> 
var a = '<%= request.getContextPath()%>';
$(document).ready(function(){
	init(1);
	inituser('<%=loginInfo.getAccount() %>',0);
});

window.name = "selfWin";
var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;
$(document).ready(function(){
	changeTime(); 
	changeTimeAndOthers();
	$("#select_time").combobox({
		onChange: function (n,o) { 
	        changeTime(); 		
		}

	}); 
	$("#select_time2 ,#select_goods ,#select_region").combobox({
		onChange: function (n,o) {  	
		changeTimeAndOthers();
		}

	}); 
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var day = date.getDate();
	var hour = date.getHours();
	var minute = date.getMinutes();
	var second = date.getSeconds();
	$("#time").html("统计数据截止至"+year+'年'+month+'月'+day+'日 '+hour+':'+minute+':'+second); 
	 
	$("#business_statistics").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=businessStatisticsService&method=agentBusinessStatistics";
	});
	$("#business_detail").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=businessStatisticsService&method=toBusinessDetailManagePage";
	});
	$("#business_graphics").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=statementService&method=businessGraphicsPage";
	});
	$('input').attr("readonly","readonly");
	
});

function changeTime(){
var time= new Array();
var userCount = new Array();
var hostCount = new Array();
var vpcCount = new Array();
var select_time=$("#select_time").val(); 
var formData = $.formToBean(host_user_count);
ajax.remoteCall("bean://statementService:businessGraphicsByTime", 
		[formData],
		function(reply){
		if( reply.status=="exception" )
		{
			if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
				top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
				top.location.reload();
				});
			}
			else{
				top.$.messager.alert("警告","信息有误","warning",function(){
					top.location.reload();
				});
			} 
		}if(reply.status=="success"){
			userCount   =reply.result.properties.userCount;
			userCounts  =reply.result.properties.userCounts;
			hostCount   =reply.result.properties.hostCount;
			hostCounts  =reply.result.properties.hostCounts;
			vpcCount   =reply.result.properties.vpcCount;
			vpcCounts  =reply.result.properties.vpcCounts;
			time   =reply.result.properties.time;
			$("#new_users_count").html(userCounts); 
		    $("#new_hosts_count").html(hostCounts);
		}
	
require.config({
    paths:{
    	'echarts':'<%=request.getContextPath()%>/js/echarts',
        'echarts/chart/line': '<%=request.getContextPath()%>/js/echarts'
    }
});
require(
        [
			'echarts',
			'echarts/chart/line'
        ],
        function(ec) { 
            var mainChart1 = ec.init(document.getElementById('main_pie_1'));  
            option1 = {
            		color:['#4FA6F6','#58CB81','#660099'],
//	            		animation:false,
            		title : {
            	        text: ''
            	    },
            	    tooltip : {
            	        trigger: 'axis'
            	    },
            	    legend: {
            	        data:['用户数','云主机','专属云']
            	    },
            	    grid: {
        		    	x:'100',
        		    	y:'40',
        	            width: '720',
        	            height: '330',
//         	            backgroundColor: 'rgba(0,0,0,0)',
//         	            borderWidth: 1,
//         	            borderColor: '#ccc'
        	        },
            	    calculable : true,
            	    xAxis : [
            	        {
            	            type : 'category',
            	            boundaryGap : false,
            	        	data:time,
            	        	splitLine:{
            	            	show:false
            	            }
            	        }
            	    ],
            	    yAxis : [
            	        {
            	            type : 'value',
            	            axisLabel : {
            	                formatter: '{value}'
            	            }
            	        }
            	    ],
            	    series : [
            	        {
            	            name:'用户数',
            	            type:'line',
							data:userCount,
            	        },
            	        {
            	            name:'云主机',
            	            type:'line',
            	            data:hostCount,
            	        },
            	        {
            	            name:'专属云',
            	            type:'line',
            	            data:vpcCount,
            	        }
            	    ]
            	};
          	mainChart1.setOption(option1,true);  
       }
	);
});
}

function changeTimeAndOthers(){
var time= new Array();
var incomeCount = new Array();
var select_time=$("#select_time2").val();
var select_region=$("#select_region").val();
var formData = $.formToBean(volume_business);
ajax.remoteCall("bean://statementService:businessGraphicsByTimeAndOthers", 
	[formData],
		function(reply){
		if( reply.status=="exception" )
		{
			if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
				top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
				top.location.reload();
				});
			}
			else{
				top.$.messager.alert("警告","信息有误","warning",function(){
					top.location.reload();
				});
			} 
		}if(reply.status=="success"){
			incomeCount =reply.result.properties.incomeCount;
			incomeCounts=reply.result.properties.incomeCounts;
			time   =reply.result.properties.time;
			$("#business_money").html(incomeCounts); 
		}
require.config({
    paths:{
    	'echarts':'<%=request.getContextPath()%>/js/echarts',
        'echarts/chart/bar' : '<%=request.getContextPath()%>/js/echarts'
    }
});
require(
        [
			'echarts',
			'echarts/chart/bar'
        ],
        function(ec) {
            var mainChart2 = ec.init(document.getElementById('main_pie_2'));
            option2 = {
            		title : {
            	        text: '',
            	    },
            	    tooltip : {
            	        trigger: 'axis',
            	        formatter:'{b}<br/>{a}:{c}元'
            	    },
            	    legend: {
            	        data:['业务额']
            	    },
//	            	    animation:false,
            	    calculable : true,
            	    grid: {
            	    	x:'100',
        		    	y:'40',
        	            width: '720',
        	            height: '330',
//         	            backgroundColor: 'rgba(0,0,0,0)',
//         	            borderWidth: 1,
//         	            borderColor: '#ccc'
        	        },
            	    xAxis : [
            	        {
            	            type : 'category',
            	            data : time,
            	            splitLine:{
            	            	show:false
            	            }
            	        }
            	    ],
            	    yAxis : [
            	        {
            	            type : 'value'
            	        }
            	    ],
            	    series : [
            	        {
            	            name:'业务额',
            	            type:'bar',
            	            data:incomeCount
            	        }
            	    ]
            	};
            
          	mainChart2.setOption(option2,true); 
       }
	);
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
			<div class="titlebar"><div class="links l"><a href="#" id="business_graphics" class="active">业务图表</a>　｜　<a href="#" id="business_statistics">业务统计</a>　｜　<a href="#" id="business_detail">业务明细</a></div><div class="r" id="time">统计数据截止至2014-12-30 03:00:00</div><div class="clear"></div></div>
            <div class="titlebar" style="padding:15px 0 15px 0;border:none;width:720px;">
            <div class="l">
					<form id="host_user_count" method="post"> 
             <select id="select_time" name="select_time" class="easyui-combobox"  style="width:120px;" data-options="width:120,height:30,panelHeight:'auto'" onchange="changeTime()">
							<option value="seven">最近七天</option>
							<option value="month">最近一月</option>
							<option value="year">最近一年</option>
							</select>
					</form> 
	      </div>
	      
	      <div class="clear"></div>
	    </div>  
				 
					<div id="main_pie_1" style="height:440px; with: border-right: 1px solid #ccc;text-align:center;"></div>
		 
				
				<hr style="width:99%;"/>
				
				
				<div class="titlebar" style="padding:15px 0 15px 0;margin:30px auto 0 auto;border:none;width:720px;">
			    <form id="volume_business" method="post">
			    <div class="l">
			         <select id="select_time2" name="select_time2" class="easyui-combobox"  style="width:120px;" data-options="width:120,height:30,panelHeight:'auto'" onchange="changeTimeAndOthers()">
							<option value="seven">最近七天</option>
							<option value="month">最近一月</option>
							<option value="year">最近一年</option>
							</select> </div>
			      <div class="blocks r">
			      <div class="r">
			      <select id="select_region" name="select_region" class="easyui-combobox"  style="width:120px;" data-options="width:120,height:30,panelHeight:'auto'"x" onchange="changeTimeAndOthers()">
							<option value="0">所有地域</option>
							<option value="1">广州</option>
							<option value="2">成都</option>
<!-- 							<option value="3">北京</option> -->
							<option value="4">香港</option>
							</select>
			</div> 
			
			<div class="clear"></div>
           </div>
			</form>
			<div class="clear"></div>
		   </div>  
				 
				
<!-- 				<hr style="width:99%;"/> -->
				 
					<div id="main_pie_2" style="height:440px; with: border-right: 1px solid #ccc;text-align:center;"></div> 
 			</div>
			<div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
   </div>
<!--  			<div class="footer"> -->
<!--               Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1 -->
<!--             </div> -->
		</div> 
	</body>
</html>