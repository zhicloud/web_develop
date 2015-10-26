<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%> 
<%@page import="java.math.BigDecimal"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String userName = "";
	if(loginInfo!=null){
		userName = loginInfo.getAccount();
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<%--  <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.js"></script> --%>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>


<script src="javascript/page.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4"); 
var userName = '<%=userName%>';
$(document).ready(function(){
	init(5);
	if(userName!= ''){ 
		inituser(userName,0);
	}else{
		inituser();
	}
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
  <div class="pagewhite" style="background:#1bbc9c">
    <div class="header pageheader"></div>
    <div class="pagemain">
      <div class="box" style="height:114px;">
        <div class="titlebar">
          <div class="l">服务与支持</div>
           <div class="r"><a href="qanda.do">Q&amp;A</a>　|　<a href="serverhelp.do" class="active">使用指南</a>　|　<a href="downloadPage.do">相关下载</a>　|　<a href="hotline.do">服务热线</a></div>
          <div class="clear"></div>
        </div>
      </div>
       <div class="box" style="padding:40px 0;"><img src="image/help_title.png" width="710" height="109" /></div>
      <div class="helptitlebar"><a href="javascript:void(0);" class="active">云主机指南</a><a href="storagehelp.do">云硬盘指南</a></div>
      <div class="box"><img src="image/serverhelp1.jpg" width="1000" height="883" /><br />
      <img src="image/serverhelp2.jpg" width="1000" height="1089" /><br />
      <img src="image/serverhelp3.jpg" width="1000" height="613" /><br />
      <img src="image/serverhelp4.jpg" width="1000" height="1493" /><br />
      <img src="image/serverhelp5.jpg" width="1000" height="1558" /></div>
    </div>
    <div class="whitefooter"> </div>
  </div>
</div>
<div class="pageright">
  <iframe id="loginiframe" src="<%=request.getContextPath() %>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  <iframe id="regiframe" src="<%=request.getContextPath() %>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
</div>
</div>
</body>
</html>