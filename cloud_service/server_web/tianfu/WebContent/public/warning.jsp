<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
String message = StringUtil.trim(request.getAttribute("message"));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>账户激活 - 云端在线</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/css/style.css" />
</head>

<body>
<div class="page">
  <div class="pagebox">
    <div class="header">
      <div class="headerbox">
        <div class="headerlogo l"><a href="#"><img src="<%=request.getContextPath() %>/images/logo_green_light_145_29.gif" width="145" height="29" alt="云端在线" /></a></div>
         
      </div>
    </div>
    <div class="nav">
      <div class="navbox">
        <div class="navbutton l"><a href="<%=request.getContextPath() %>/">首页</a></div>
        <div class="navsplitter l">&nbsp;</div> 
      </div>
    </div>
    <div class="main">
      <div class="fullcontainer">
        <div class="fulltitlebar">
          <div class="tab tabactive">提示</div>
        </div>
        <div class="fullcontent">
<div class="fullcenter"> <br /><br /><br /><%=message %><br /><br /> <br /><br /><br /></div>
        </div>
      </div>
      <div class="clear">&nbsp;</div>
    </div>
  </div>
  <div class="pagefooter">
    <div class="sitemap">
      <div class="sitemapbox">
        <div class="sitemapsocial">
          <div class="sitemaplogo l"><a href="#"><img src="<%=request.getContextPath() %>/images/logo_green_dark_40_29.gif" width="40" height="29" alt="云端在线" /></a></div>
          <div class="sitemapweixin r"><a href="#">微信扫二维码<br />
            关注云端在线</a></div>
          <div class="sitemapweibo r"><a href="#">新浪<br />
            微博</a></div>
        </div>
        <ul>
          <li><a href="<%=request.getContextPath()%>/">首页</a></li>
        </ul>
        <ul>
          <li><a href="#">定制云主机</a></li>
          <li><a href="#">试用</a></li>
          <li><a href="#">包月试用</a></li>
          <li><a href="#">按量付费</a></li>
        </ul>
        <ul>
          <li><a href="#">相关下载</a></li>
        </ul>
        <ul>
          <li><a href="#">我的云端</a></li>
          <li><a href="#">我的云主机</a></li>
          <li><a href="#">财务中心</a></li>
          <li><a href="#">用户中心</a></li>
        </ul>
        <ul>
          <li><a href="#">账户注册</a></li>
          <li><a href="#">账户登录</a></li>
        </ul>
        <ul>
          <li><a href="#">关于我们</a></li>
          <li><a href="#">联系我们</a></li>
        </ul>
      </div>
    </div>
    <div class="footer">
      <div class="footerbox">
        <div class="footercopyright l">Copyright &copy; 2014 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.</div>
        <div class="footercopyright r">蜀ICP备14004217号-1</div>
      </div>
    </div>
  </div>
</div>
</body>
</html>
