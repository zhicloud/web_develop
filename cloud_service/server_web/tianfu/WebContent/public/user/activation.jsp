<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>账户激活 - 云端在线</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css" />
</head>

<body>
<div class="page">
  <div class="pagebox">
    <div class="header">
      <div class="headerbox">
        <div class="headerlogo l"><a href="<%=request.getContextPath() %>/"><img src="<%=request.getContextPath()%>/images/logo_green_light_145_29.gif" width="145" height="29" alt="云端在线" /></a></div>
        <div class="headerregister r"><a href="<%=request.getContextPath()%>/public/user/register.jsp">注册</a></div>
        <div class="headerlogin r"><a href="<%=request.getContextPath()%>/user.do">登录</a></div>
      </div>
    </div>
    <div class="nav">
      <div class="navbox">
        <div class="navbutton l"><a href="<%=request.getContextPath()%>/">首页</a></div>
        <div class="navsplitter l">&nbsp;</div>
		    <div class="navbutton l"><a href="<%=request.getContextPath()%>/user/buy.do">定制主机</a></div>
		    <div class="navsplitter l">&nbsp;</div>
		    <div class="navbutton l"><a href="<%=request.getContextPath()%>/public/downloadPage.do">相关下载</a></div>
        <div class="navbutton r"><a href="#" id="navcart"></a></div>
        <div class="navsplitter r">&nbsp;</div>
        <div class="navcontrol r"><a href="#"><span>我的云端</span></a></div>
      </div>
    </div>
    <div class="main">
      <div class="fullcontainer">
        <div class="fulltitlebar">
          <div class="tab tabactive">账户激活</div>
        </div>
        <div class="fullcontent">
	<div class="fullcenter"><img src="<%=request.getContextPath()%>/images/image_activation.gif" width="147" height="111" alt="请激活账户" /><br /><br /><br /><h3>您已注册成功，请到您注册的邮箱激活账户！</h3><br /><br /><br /><br /><br /><br /></div>
        </div>
      </div>
      <div class="clear">&nbsp;</div>
    </div>
  </div>
   <div class="pagefooter">
		<div class="footer">
		    <div class="footerbox">
		       <div class="footercopyright">Copyright &copy; 2014 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1</div>
		   </div>
	  </div>
	</div>
  </div>
</body>
</html>
