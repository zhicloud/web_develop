<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
Integer userType = Integer.valueOf(request.getParameter("userType"));
LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
String terminalUserId = (String)request.getAttribute("terminalUserId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>致云代理商管理平台</title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
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
	init(4);
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
});
function formatTime(val, row)
{
	if(val==null||val==''){
		return "";
	}
	return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
}
function formatStatus(val, row)
{  
	if(val == 1){  
	    return "成功";  
	}else if(val == 2) {
	    return "失败";  
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
    <div class="titlebar">
      <div class="l">操作日志</div>
      <div class="blocks r"><a id="search" href="#" class="bluebutton r" style="width:60px;">搜索</a><div class="r"><input id="endTime" editable="false" class="easyui-datebox" data-options="width:'100',height:'30'"></input></div><div class="r">至</div><div class="r"><input id="startTime" editable="false" class="easyui-datebox" data-options="width:'100',height:'30'"></input></div></div>
      <div class="clear"></div>
    </div>
    <table id="logtable" class="easyui-datagrid"
    			data-options="
					url: '<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=operLogService&method=queryOperLogForAgent&terminalUserId=<%=terminalUserId %>',
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
          <th data-options="field:'operTime',width:140,formatter:formatTime">时间</th>
          <th data-options="field:'content',width:480">操作</th>
          <th data-options="field:'status',width:140" formatter="formatStatus">状态</th>
        </tr>
      </thead>
      <!-- <tbody>
        <tr>
          <td>2014/08/12 15:03</td>
          <td>更改云主机<a href="#">Host_1</a>配置</td>
        </tr>
        <tr>
          <td>2014/08/12 15:03</td>
          <td>更改云主机<a href="#">Host_1</a>配置</td>
        </tr>
        <tr>
          <td>2014/08/12 15:03</td>
          <td>更改云主机<a href="#">Host_1</a>配置</td>
        </tr>
        <tr>
          <td>2014/08/12 15:03</td>
          <td>更改云主机<a href="#">Host_1</a>配置</td>
        </tr>
        <tr>
          <td>2014/08/12 15:03</td>
          <td>更改云主机<a href="#">Host_1</a>配置</td>
        </tr>
        <tr>
          <td>2014/08/12 15:03</td>
          <td>更改云主机<a href="#">Host_1</a>配置</td>
        </tr>
        <tr>
          <td>2014/08/12 15:03</td>
          <td>更改云主机<a href="#">Host_1</a>配置</td>
        </tr>
        <tr>
          <td>2014/08/12 15:03</td>
          <td>更改云主机<a href="#">Host_1</a>配置</td>
        </tr>
        <tr>
          <td>2014/08/12 15:03</td>
          <td>更改云主机<a href="#">Host_1</a>配置</td>
        </tr>
        <tr>
          <td>2014/08/12 15:03</td>
          <td>更改云主机<a href="#">Host_1</a>配置</td>
        </tr>
      </tbody> -->
    </table>
<!--     <div class="easyui-pagination" data-options="total: 114, showPageList: false, showRefresh: false, displayMsg: '', beforePageText: '', afterPageText: ''"></div> -->
  </div>
  <div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
  </div>
</div>
</body>
</html> 
