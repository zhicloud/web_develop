<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.TerminalUserVO" %>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	TerminalUserVO terminalUser = (TerminalUserVO)request.getAttribute("terminalUser");
%>
<!DOCTYPE html>
<!-- terminal_user_base_info.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		<title>终端用户 - 基本信息</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		/************/
		.panel-header {
			border-top: 0px;
			border-bottom: 1px solid #dddddd;
		}
		.panel-header, 
		.panel-body {
			border-left: 0px;
			border-right: 0px;
		}
		.panel-body {
			border-bottom: 0px;
		}
		</style>
	</head>
	<body>
		<form id="terminal_user_base_info" method="post">
		
			<div class="panel-header">
				<div class="panel-title">基本信息</div>
				<div class="panel-tool"></div>
			</div>
			
			<input type="hidden" id="terminalUser_id" name="terminalUser_id" value="<%=terminalUser.getId()%>" />
			<input type="hidden" id="group_id"    name="group_id"    value="<%=terminalUser.getGroupId()%>" />
			
			<table style="margin: 10px 10px 10px 20px;">
<!-- 				<tr> -->
<!-- 					<td style="vertical-align:middle; text-align:right;">用户名：</td> -->
<!-- 					<td style="padding:5px;"> -->
<!-- 						<input type="text" id="account" name="account" style="width:200px ;height:23px;" -->
<%-- 							value="<%=StringUtil.trim(terminalUser.getAccount())%>" disabled="disabled" onblur="checkAccount()"/> --%>
<!-- 					</td> -->
<!-- 					<td><span id="terminal-tip-account"></span></td> -->
<!--  	 					<td><a href="#"  class="easyui-linkbutton" iconCls="icon-edit" id="account-edit-link">编辑</a></td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
					<td style="vertical-align:middle; text-align:right;">常用邮箱：</td>
					<%if(terminalUser.getEmail()==null||terminalUser.getEmail().isEmpty()){%>
					<td style="padding:5px;">
						<input type="text" id="e_mail" name="e_mail" style="width:200px;height: 23px" 
							value="邮箱关系到账号安全，请注意完善" disabled="disabled"/>
					</td>
					<%}else{%>
					<td style="padding:5px;">
						<input type="text" id="e_mail" name="e_mail" style="width:200px;height: 23px" 
							value="<%=StringUtil.trim(terminalUser.getEmail())%>" disabled="disabled" />
					</td>
					<%}%>
					<td><span id="terminal-tip-email"></span></td>
					<td><a href="#"  class="easyui-linkbutton" iconCls="icon-edit" id="email-edit-link">编辑</a></td>
				</tr>
				<tr>
					<td style="vertical-align:middle; text-align:right;">手机号码：</td>
					<td style="padding:5px;">
						<input type="text" id="phone" name="phone" style="width:200px;height: 23px" 
							value="<%=StringUtil.trim(terminalUser.getPhone())%>" disabled="disabled" />
					</td>
					<td><span id="terminal-tip-phone"></span></td>
					<td><a href="#"  class="easyui-linkbutton" iconCls="icon-edit" id="phone-edit-link">编辑</a></td>
				</tr>
			</table>
			
		</form>
	</body>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
   	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
   	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
   	<script type="text/javascript">
  $(function(){
	  
	  $("#account-edit-link").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=terminalUserService&method=baseInfoPageAccountEdit",
// 				onClose : function(data) {
// 					$('#sys_role_datagrid').datagrid('reload');
// 				}
			});
		});
	  
	  $("#email-edit-link").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=terminalUserService&method=baseInfoPageEmailEdit",
			});
		});
	  
	  $("#phone-edit-link").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=terminalUserService&method=baseInfoPagePhoneEdit",
			});
		});
	  
  });
  
	</script>
</html>