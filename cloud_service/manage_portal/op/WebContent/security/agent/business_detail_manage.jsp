<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String userId = loginInfo.getUserId();
	BigDecimal incomeCount = (BigDecimal)request.getAttribute("incomeCount");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- invoice_manage.jsp -->
<head>
<!-- 		<meta charset="UTF-8" /> -->
<!-- 		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> -->
<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
<title>致云代理商管理平台</title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/agent.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" /> 
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/agent.js"></script>

<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
$(document).ready(function(){
	init(1);
	inituser("<%=loginInfo.getAccount()%>",0);
	$("#business_statistics").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=businessStatisticsService&method=agentBusinessStatistics";
	});
	$("#business_detail").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=businessStatisticsService&method=toBusinessDetailManagePage";
	});
	$("#business_graphics").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=statementService&method=businessGraphicsPage";
	});
	
	var date = new Date();
	var year = date.getFullYear();
	var month = date.getMonth()+1;
	var day = date.getDate();
	var hour = date.getHours();
	if(hour.length<10){
		hour = "0"+hour;
	}
	var minute = date.getMinutes();
	if(minute.length<10){
		minute = "0"+minute;
	}
	var second = date.getSeconds();
	if(second.length<10){
		second = "0"+second;
	}
	$("#time").html("统计数据截止至"+year+'年'+month+'月'+day+'日 '+hour+':'+minute+':'+second); 
});
window.name = "selfWin";
var time = "";
var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;


// 布局初始化
$("#agent_invoice_datagrid_agent").height( $(document.body).height());

function formatSubmitTime(val, row)
{
	return $.formatDateString(val, "yyyyMM", "yyyy年MM月");
}

function formatStatus(val,row)
{  
	if(val == 2 || val == 4){
		return "审核中";
	}else if(val == 3){
		return "已寄送";
	}
	else if(val == 5){
		return "寄送完成";
	}
}  

function toA(createTime){ 
	time = createTime; 
	
}



//查询结果为空
function createView(){
	return $.extend({},$.fn.datagrid.defaults.view,{
	    onAfterRender:function(target){
	        $.fn.datagrid.defaults.view.onAfterRender.call(this,target);
	        var opts = $(target).datagrid('options');
	        var vc = $(target).datagrid('getPanel').children('div.datagrid-view');
	        vc.children('div.datagrid-empty').remove();
	        if (!$(target).datagrid('getRows').length){
	            var d = $('<div class="datagrid-empty"></div>').html( '没有相关记录').appendTo(vc);
	            d.css({
	                position:'absolute',
	                left:0,
	                top:50,
	                width:'100%',
	                textAlign:'center'
	            });
	        }
	    }
    });
}

function onLoadSuccess()
{
	$("body").css({
		"visibility":"visible"
	});
	//去掉没用的checkbox
	$("input[type='checkbox'][name!='ck']").remove();
}

$(document).ready(function(){
	// 按 id 查询对应的代理商的发票
// 	var queryParams = {};
<%-- 	queryParams.user_id = '<%=userId%>'; --%>
// 	$('#agent_invoice_datagrid_agent').datagrid({
// 		"queryParams": queryParams
// 	}); 
	var pager = $('#agent_invoice_datagrid_agent').datagrid('getPager');
	pager.pagination({
		showPageList: false,
		showRefresh: false,
		displayMsg: '',
		beforePageText: '',
		afterPageText: ''
	 });     
	$("#to_detail").click(function(){	 
		var d = $("#agent_invoice_datagrid_agent").datagrid("getSelected");
		if(d==""){
			top.$.messager.alert('警告','您还未选中月份','warning'); 
			return;
		}
		window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=businessStatisticsService&method=agentMonthlyDetailPage&createTime="+d.createTime;
		 
	});
}); 
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
    <div class="titlebar"><div class="links l"><a href="#" id="business_graphics">业务图表</a>　｜　<a href="#" id="business_statistics" >业务统计</a>　｜　<a href="#" id="business_detail" class="active">业务明细</a></div><div class="r" id="time">统计数据截止至2014-12-30 03:00:00</div><div class="clear"></div></div>
    <div class="titlebar" style="padding:15px 0 5px 0;">
      <div class="l"><a href="#" class="bluebutton" id="to_detail">查看当月详情</a></div>
      <div class="clear"></div>
    </div>
    <table id="agent_invoice_datagrid_agent" class="easyui-datagrid"  
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=businessStatisticsService&method=queryBusinessByMonthly&user_id=<%=userId %>',
					queryParams: {},
					border:false,
					singleSelect:true,
					scrollbarSize:0,
					pagination:true,
					toolbar: '#toolbar',
					remoteSort:false,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 10 ,
					onLoadSuccess: onLoadSuccess,
					onClickRow: function (rowIndex, rowData) {  toA(rowData.createTime); }
				">
				<thead>
					<tr>  
				          <th data-options="field:'ck',checkbox:true,width:28">&nbsp;</th>
				          <th data-options="field:'createTime',width:446"  formatter="formatSubmitTime">月份</th>
				          <th data-options="field:'fee',width:446">业务额</th> 
					</tr>
				</thead>
			</table> 
   </div>
  <div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
  </div>
</div>
</body>  
</html>