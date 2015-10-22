<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import = "java.math.BigInteger "%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String userId = loginInfo.getUserId();
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
$(document).ready(function(){
	init(3);
	inituser("<%=loginInfo.getAccount()%>",0);
});
$(document).ready(function(){   
	initstep(1);
	
	// 按 id 查询对应的用户的发票
	var queryParams = {};
	queryParams.user_id = '<%=userId%>';
	$('#invoice_datagrid').datagrid({
		"queryParams": queryParams
	});
	
	$("#base_info").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=toConsumptionRecordPage";
	});
	$("#recharge").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=rechargePage";
	});
	$("#invoice_manage").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceService&method=managePage";
	}); 
});

function getLoginInfo(name,message,userId){
	slideright();
	inituser(name,message);
	window.location.reload();
}

window.name = "selfWin";

var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=<%=userType%>");
ajax.async = false;


// 布局初始化
$("#invoice_datagrid").height( $(document.body).height());
 

function statusFormatter(val)	
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

function onLoadSuccess()
{
	$("body").css({
		"visibility":"visible"
	});
	
	
}

function timeFormat(val)	
{   
	return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
}  

$(function(){
	//索取发票
	$("#add_invoice_btn").click(function(){
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceService&method=addInvoicePage",
			onClose: function(data){
				$('#invoice_datagrid').datagrid('reload');
			}
		});
	});
	//邮寄地址管理
	$("#invoice_address_manage_btn").click(function(){
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceAddressService&method=managePage",
			onClose: function(data){
				$('#invoice_datagrid').datagrid('reload');
			}
		});
	});
	
})



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
  		<div class="pageleft">
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
      <div class="titlebar"><div class="links"><a href="#" id="base_info">基本信息</a>　｜　<a href="#" id="recharge">账户充值</a>　｜　<a href="#" class="active">发票管理</a></div></div>
      <div class="box" style="padding: 10px 0 0 0;">
      <div class="titlebar">
      <div class="l">已开发票</div>
        <a href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceService&method=addInvoicePage" class="greenbutton r">索取发票</a>
		</div>
			<table id="invoice_datagrid" class="easyui-datagrid" 
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=invoiceService&method=queryInvoice',
					queryParams: {},
					toolbar: '#toolbar',
					remoteSort:false,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 10,
					onLoadSuccess: onLoadSuccess
				">
				<thead>
					<tr>
						<th data-options="field:'totalAmount',width:140" >金额（元）</th>
						<th data-options="field:'invoiceTitle',width:380">发票抬头</th>
						<th data-options="field:'submitTime',width:150" formatter= "timeFormat"sortable=true>提交时间</th>
						<th data-options="field:'status',width:100" formatter="statusFormatter">状态</th>
					</tr>
				</thead>
			</table>
		 </div>
      <div class="clear">&nbsp;</div>
    </div>
    <div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
  </div>
  </div> 
 	</div>
 	<script type="text/javascript">
		$(function(){
		var pager = $('#invoice_datagrid').datagrid('getPager');
		pager.pagination({
			showPageList: false,
			showRefresh: false,
			displayMsg: '',
			beforePageText: '',
			afterPageText: ''
		 });            
	})
</script>
</body>
</html>