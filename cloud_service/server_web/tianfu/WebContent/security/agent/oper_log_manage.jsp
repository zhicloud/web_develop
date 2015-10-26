 
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.vo.TerminalUserVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
Integer userType = Integer.valueOf(request.getParameter("userType"));
LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType); 
TerminalUserVO terminalUser = (TerminalUserVO)request.getAttribute("terminalUser");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>致云代理商管理平台</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/agent.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<script type="text/javascript" src="<%=request.getContextPath() %>/javascript/agent.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=3");
$(document).ready(function(){
	init(2);
	inituser("<%=loginInfo.getAccount()%>",0);
	
	var pager = $('#logtable').datagrid('getPager');
	pager.pagination({
		showPageList: false,
		showRefresh: false,
		displayMsg: '',
		beforePageText: '',
		afterPageText: ''
	 }); 

	$("#search").click(function(){
		var start_time = $("#startTime").datebox("getValue");
		var end_time = $("#endTime").datebox("getValue");
		
		var Start = start_time.slice(0,4)+start_time.slice(5,7)+start_time.slice(-2)+'000000000';
		var end = end_time.slice(0,4)+end_time.slice(5,7)+end_time.slice(-2)+'235959999';
		var queryParams = {};
		queryParams.startTime = Start;
		queryParams.endTime = end;
		$('#logtable').datagrid({
			"queryParams": queryParams
		});
		var pager = $('#logtable').datagrid('getPager');
		pager.pagination({
			showPageList: false,
			showRefresh: false,
			displayMsg: '',
			beforePageText: '',
			afterPageText: ''
		 }); 
	});
	$("#back1,#back2").click(function(){
		 window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=terminalUserService&method=viewItemPage&terminalUserId=<%=terminalUser.getId()%>";
	});
});
function formatTime(val, row)
{
	if(val==null||val==''){
		return "";
	}
	return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
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
    <div class="titlebar">
      <div class="blocks l">
      <div class="l"><a href="javascript:void(0);" id="back1"><img src="<%=request.getContextPath()%>/image/button_back.png" width="22" height="30" alt="返回" /></a></div>
      <div class="l"><a href="javascript:void(0);" id="back2"><%=terminalUser.getAccount() %></a>的操作日志</div>
      <div class="clear"></div>
      </div>
      <div class="blocks r"><a id="search" href="#" class="bluebutton r" style="width:60px;">搜索</a><div class="r"><input id="endTime" editable="false" class="easyui-datebox" data-options="width:'100',height:'30'"></input></div><div class="r">至</div><div class="r"><input id="startTime" editable="false" class="easyui-datebox" data-options="width:'100',height:'30'"></input></div></div>
      <div class="clear"></div>
    </div>
    <table id="logtable" class="easyui-datagrid"
    			data-options="
                    url: '<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=operLogService&method=queryOperLog&terminalUserId=<%=terminalUser.getId() %>',					
                    queryParams: {},
					border:false,
					singleSelect:true,
					scrollbarSize:0,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 10
				">
      <thead>
        <tr>
          <th data-options="field:'operTime',width:200,formatter:formatTime">时间</th>
          <th data-options="field:'content',width:720">操作</th>
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
