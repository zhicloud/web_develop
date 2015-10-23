<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.vo.AgentVO"%>
<%@page import="com.zhicloud.op.vo.SysRoleVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_AGENT;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request,AppConstant.SYS_USER_TYPE_AGENT);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>致云代理商管理平台</title>
	<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/agent.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/usemanual.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
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
		<div class="wrap">
			<div class="left_nav" id="subNav">
				<ul>
					<li><a data-href="1" href="javascript:;">云主机</a></li>
					<!-- <li><a data-href="2" href="javascript:;">专属云</a></li> -->
				</ul>
			</div>
			<div class="middle_line"></div>
			<div class="right_content">
				<div class="contents" id="getContents"></div>
			</div>
		</div>
	</div>
	<div class="footer">Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1</div>
</div>

<!-- JavaScript -->
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
	var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=3"); 
	$(document).ready(function(){
		init(5);
		inituser("<%=loginInfo.getAccount()%>",0);
	});
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/agent/usemanual.js"></script>
</body>
</html>