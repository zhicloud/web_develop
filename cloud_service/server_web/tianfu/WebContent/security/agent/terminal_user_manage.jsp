<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
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
	
	var pager = $('#terminaltable').datagrid('getPager');
	pager.pagination({
		showPageList: false,
		showRefresh: false,
		displayMsg: '',
		beforePageText: '',
		afterPageText: ''
	 });
	// 查询
	$("#query_terminal_user_btn").click(function(){
		var queryParams = {};
		queryParams.terminal_user_account = $("#terminal_user_account").val().trim();
		$('#terminaltable').datagrid({
			"queryParams": queryParams
		});
		myPager();
	});
	//查看用户详情
	$("#view_item_btn").click(function(){
		var d = $("#terminaltable").datagrid("getSelected");
		if(d==null){
			top.$.messager.alert("提示","请选择您要查看的用户","info",function(){
			});
			return;
		}
		var id = d.id;
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=terminalUserService&method=viewItemPage&terminalUserId="+encodeURIComponent(id);
	});
	//添加用户
	$("#add_user").click(function(){
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=terminalUserService&method=addPage";
	});
});
function myPager(){
	var pager = $('#terminaltable').datagrid('getPager');
	pager.pagination({
		showPageList: false,
		showRefresh: false,
		displayMsg: '',
		beforePageText: '',
		afterPageText: ''
	 });
}
function formatTime(val, row)
{
	if(val==null||val==''){
		return "";
	}
	return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
}
function onLoadSuccess()
{
	$("body").css({
		"visibility":"visible"
	});
	var d = $("#terminaltable").datagrid("getData");
	var t = d.total;
	$("#totalUser").html("共<b>"+t+"</b>位用户");
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
      <div class="l" id="totalUser"></div>
      <a id="add_user" href="#" class="greenbutton r">添加用户</a>
      <div class="clear"></div>
    </div>
    <div class="titlebar" style="padding:15px 0 5px 0;">
      <div class="blocks l">
        <div class="l"><a id="view_item_btn" href="#" class="bluebutton">查看用户详情</a></div>
      </div>
      <div class="blocks r"><a id="query_terminal_user_btn" href="#" class="bluebutton r" style="width:60px;">搜索</a>
        <div class="r">
          <input id="terminal_user_account" class="easyui-textbox" type="text" name="name" data-options="width:'160',height:'30'">
        </div>
        <div class="clear"></div>
      </div>
      <div class="clear"></div>
    </div>
    <table id="terminaltable" class="easyui-datagrid" 
    	data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=terminalUserService&method=queryTerminalUserForAgent',
					queryParams: {},
					border:false,
					singleSelect:true,
					scrollbarSize:0,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 10,
					onLoadSuccess: onLoadSuccess
				">
      <thead>
        <tr>
          <th data-options="field:'ck',checkbox:true,width:28"></th>
          <th data-options="field:'account',width:360">用户</th>
          <th data-options="field:'createTime',width:360,formatter:formatTime">加入时间</th>
          <th data-options="field:'turnover',width:172">业务额（元）</th>
        </tr>
      </thead>
    </table>
<!--     <div class="easyui-pagination" data-options="total: 114, showPageList: false, showRefresh: false, displayMsg: '', beforePageText: '', afterPageText: ''"></div> -->
  </div>
  <div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
  </div>
</div>
</body> 
</html>
