<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%> 
<%@page import="java.math.BigDecimal"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>致云代理商管理平台</title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<script src="<%=request.getContextPath() %>/javascript/jquery.min.js"></script>
<script src="<%=request.getContextPath() %>/javascript/jquery.easyui.min.js"></script>
<script src="<%=request.getContextPath() %>/javascript/page.js"></script>
<script type="text/javascript">
$(document).ready(function(){
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
  <div class="pagewhite">
    <div class="pagemain" style="padding-bottom:124px">
      <div class="box" style="padding:60px 0"><img src="<%=request.getContextPath() %>/image/404.gif" width="454" height="227" />
      <a href="javascript:void(0);"  onclick="window.history.back();" class="bluelinebutton" style="margin:40px auto 0 auto">返　　回</a>
      </div>
    </div>
    <div class="footer" style="height:124px;line-height:60px; text-align:center;"><a href="#"><img src="<%=request.getContextPath() %>/image/logo_big.gif" width="95" height="64" /></a><br />Copyright &copy; 2014 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1</div>
  </div>
</div>
</body>
</html>