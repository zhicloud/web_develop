<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	String createTime = (String)request.getParameter("createTime");
	BigDecimal incomeCount = (BigDecimal)request.getAttribute("incomeCount");
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
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
});
window.name = "selfWin";

var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;


// 布局初始化
$("#agent_business_statistics").height( $(document.body).height());

 


 

 

$(document).ready(function(){ 
	
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
	
	$("#business_statistics").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=businessStatisticsService&method=agentBusinessStatistics";
	});
	$("#business_detail").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=businessStatisticsService&method=toBusinessDetailManagePage";
	});
	$("#business_graphics").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=statementService&method=businessGraphicsPage";
	});
	
	var pager = $('#agent_invoice_datagrid_agent').datagrid('getPager');
	pager.pagination({
		showPageList: false,
		showRefresh: false,
		displayMsg: '',
		beforePageText: '',
		afterPageText: ''
	 });
	
	$("#export_btn").click(function() { 
		
		var grid = $('#agent_invoice_datagrid_agent');
		var options = grid.datagrid('getPager').data("pagination").options;
		var page = options.pageNumber;
		var rows = options.pageSize; 
		 
		top.$.messager.confirm("确认", "确定导出？", function (r) {  
	        if (r) {   
				window.location.href= "<%=request.getContextPath()%>/agentBillExport.do?userType=<%=userType%>&page="+page+"&rows="+rows+"&createTime=<%=createTime%>";
		        }  
	    });   
	});
	
});

window.name = "selfWin";

var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false; 	

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


var _agent_bill_detail_view_item_dlg_scope_ = new function(){
	
	var self = this; 
	
	// 关闭
	this.close = function()
	{
		$("#agent_bill_detail_view_item_dlg").dialog("close");
	};
	
	// 初始化
}; 
  
 

function formatRegion(val,row)
{  
	if(val == 1){
		return "广州";
	}else if(val == 2){
		return "成都";
	}else if(val == 4){
		return "香港";
	}
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
	    <a href="#" id="business"><img id="agent_nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_1_i.png" width="21" height="21" />业务信息</a>
	    <a href="#" id="user_manage"><img id="agent_nav_2" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_2_i.png" width="21" height="21" />用户管理</a>
	    <a href="#" id="my_account"><img id="agent_nav_3" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_3_i.png" width="21" height="21" />我的账户</a>
	    <a href="#" id="oper_log"><img id="agent_nav_4" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_4_i.png" width="21" height="21" />操作日志</a>
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
    <div class="l"><a href="#" class="bluebutton" id="export_btn">导出Excel</a></div>
      <div class="blocks r"> 
                           业务总额：<%=incomeCount %>
      </div>
      
      <div class="clear"></div>
    </div>
	        <table id="agent_invoice_datagrid_agent" class="easyui-datagrid"  
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=businessStatisticsService&method=queryAgentMonthlyDetail&createTime=<%=createTime %>',
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
					singleSelect:true,
					view:createView() 
				">
				<thead>
					<tr>
						<th data-options="field:'userName',width:230" width="70px">用户</th> 
						<th data-options="field:'hostName',width:230" width="70px">主机名</th> 
						<th data-options="field:'region',width:230" formatter="formatRegion" width="70px">地域</th> 
						<th data-options="field:'fee',width:230" width="70px" sortable=true>金额（元）</th>
					</tr>
				</thead>
			</table>
			 
		
    <style type="text/css">
.datagrid-header,
.datagrid-toolbar,
.datagrid-pager,
.datagrid-footer-inner {
  border-color: #b2b2b2;
}
.datagrid-header td,
.datagrid-body td,
.datagrid-footer td {
  border-color: #b2b2b2;
}
</style>
  </div>
  <div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
  </div>
</div>
</body>
</html> 