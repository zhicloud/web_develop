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
	init(4);  
	if(userName!= ''){ 
		inituser(userName,0);
	}else{
		inituser();
	}
	$(window).scroll(function(){
		var s = $(window).scrollTop();
		if( s >= 643){
			$(".backtop").fadeIn(100);
		}else{
			$(".backtop").fadeOut(200);
		};
	});
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
<div class="backtop"><img src="image/button_top.gif" width="64" height="64" alt="返回顶部"  onclick="scrollto(0);"/></div>
<div class="page">
  <div class="pagewhite" style="background:#f8f8f8">
    <div class="header pageheader">
        <div class="top">
			<a class="logo l" href="<%=request.getContextPath()%>/"> 
			<img src="<%=request.getContextPath()%>/image/logo_tf.png" width="184" height="34" alt="天府软件园创业场" /></a>
			<div id="beforelogin" class="user r">
				<a id="loginlink" href="javascript:void(0);" class="graylink">登录</a><span>|</span>
				<a id="reglink" href="javascript:void(0);">注册</a>
			</div>
			<div id="afterlogin" class="user r" style="display: none;">
				<img class="reddot" src="<%=request.getContextPath()%>/image/reddot.png" width="6" height="6" alt=" " />
				<a id="logoutlink" href="javascript:void(0);">注销</a><span>|</span>
				<a href="<%=request.getContextPath()%>/user.do" class="bluelink">我的云端</a>
			</div>
			<div class="nav r">
				<a href="<%=request.getContextPath()%>/" style="background: transparent;"><img id="nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/nav_1_i.png" width="20" height="20" alt="首页" style="padding: 8px 0" /> </a>
				<a href="<%=request.getContextPath()%>/cloudsever.do">云主机</a>
				<a href="<%=request.getContextPath()%>/cloudstorage.do">云硬盘</a>
				<a href="<%=request.getContextPath()%>/solution.do">解决方案</a>
				<a href="<%=request.getContextPath()%>/help.do">帮助中心</a>
				<a href="<%=request.getContextPath()%>/aboutus.do">关于我们</a>
				<a href="#" style="display: none"></a>
				<a href="<%=request.getContextPath()%>/user.do?flag=login" style="display: none"></a>
				<a href="#" style="display: none"></a>
				<a href="#" style="display: none">我的云端</a>
			</div>
		</div>
		<div class="subnav">
			<div class="box">1</div>
			<div class="box">2</div>
			<div class="box">3</div>
			<div class="box">4</div>
			<div class="box">5</div>
			<div class="box">6</div>
			<div class="box">7</div>
			<div class="box">8</div>
			<div class="box">9</div>
			<div class="box">
				<a href="#"><img id="nav_10_1" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_1_i.png" width="24" height="24" alt="概览" /><br />概览</a><a
					href="#"><img id="nav_10_2" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_2_i.png" width="24" height="24" alt="我的云主机" /><br />我的云主机</a><a
					href="#"><img id="nav_10_3" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_3_i.png" width="24" height="24" alt="我的云硬盘" /><br />我的云硬盘</a><a
					href="#"><img id="nav_10_4" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_4_i.png" width="24" height="24" alt="我的账户" /><br />我的账户</a><a
					href="#"><img id="nav_10_5" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_5_i.png" width="24" height="24" alt="操作日志" /><br />操作日志</a><a
					href="#"><img id="nav_10_6" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_6_i.png" width="24" height="24" alt="意见反馈" /><br />意见反馈</a><a
					href="#"><img id="nav_10_7" class="swapimage"
					src="<%=request.getContextPath()%>/image/nav_10_7_i.png" width="24" height="24" alt="文件夹" /><br />文件夹</a>
			</div>
		</div>
	</div>
    <div class="pagemain"  >
        <div class="box" style="background: #13bd9c url(image/solution.png) no-repeat center center; height:538px"><a style="width:100%;height:100%;display:block" href="javascript:void(0)" onclick="scrollto(628);"></a></div>      <div class="box" style="width: 1000px; background: url(image/solution_0.gif) no-repeat center center;height:239px;padding:500px 0 0 0;">
        <div class="l" style="width:300px; text-align:center;"><a href="javascript:void(0)" onclick="scrollto(1367);"><img src="image/more_1.png" width="121" height="45" /></a></div>
        <div class="l" style="width:385px; text-align:center;"><a href="javascript:void(0)" onclick="scrollto(1931);"><img src="image/more_1.png" width="121" height="45" /></a></div>
        <div class="l" style="width:315px; text-align:center;"><a href="javascript:void(0)" onclick="scrollto(2495);"><img src="image/more_1.png" width="121" height="45" /></a></div>
        <div class="clear"></div>
      </div>
      <div class="solution solution1" style="height:564px;">
        <div id="m1" class="solutionmenu" style="background:url(image/solution_1.png) no-repeat">
        <a href="javascript:void(0)" class="solutiontitle" onclick="gotosolution(1,1);">&nbsp;</a>
        <a href="javascript:void(0)" class="solutionlink" onclick="gotosolution(1,2);">整 体 架 构</a>
        <a href="javascript:void(0)" class="solutionlink" onclick="gotosolution(1,3);">产 品 功 能</a>
        <a href="javascript:void(0)" class="solutionlink" onclick="gotosolution(1,4);">特 点 优 势</a>
        </div>
        <ul id="s1" class="anime" style="width:400%;">
          <li style="width:25%;background:#fafafa url(image/solution_1_1.gif) no-repeat center center"></li>
          <li style="width:25%;background:#fafafa url(image/solution_1_2.gif) no-repeat center center"></li>
          <li style="width:25%;background:#fafafa url(image/solution_1_3.gif) no-repeat center center"></li>
          <li style="width:25%;background:#fafafa url(image/solution_1_4.gif) no-repeat center center"></li>
        </ul>
      </div>
      <div class="solution solution2" style="height:564px;">
        <div id="m2" class="solutionmenu" style="background:url(image/solution_2.png) no-repeat">
        <a href="javascript:void(0)" class="solutiontitle" onclick="gotosolution(2,1);">&nbsp;</a>
        <a href="javascript:void(0)" class="solutionlink" onclick="gotosolution(2,2);">整 体 架 构</a>
        <a href="javascript:void(0)" class="solutionlink" onclick="gotosolution(2,3);">产 品 功 能</a>
        <a href="javascript:void(0)" class="solutionlink" onclick="gotosolution(2,4);">特 点 优 势</a>
        </div>
        <ul id="s2" class="anime" style="width:400%;">
          <li style="width:25%;background:url(image/solution_2_1.gif) no-repeat center center"></li>
          <li style="width:25%;background:url(image/solution_2_2.gif) no-repeat center center"></li>
          <li style="width:25%;background:url(image/solution_2_3.gif) no-repeat center center"></li>
          <li style="width:25%;background:url(image/solution_2_4.gif) no-repeat center center"></li>
        </ul>
      </div>
      <div class="solution solution3" style="height:564px;">
        <div id="m3" class="solutionmenu" style="background:url(image/solution_3.png) no-repeat">
        <a href="javascript:void(0)" class="solutiontitle" onclick="gotosolution(3,1);">&nbsp;</a>
        <a href="javascript:void(0)" class="solutionlink" onclick="gotosolution(3,2);">整 体 架 构</a>
        <a href="javascript:void(0)" class="solutionlink" onclick="gotosolution(3,3);">产 品 功 能</a>
        <a href="javascript:void(0)" class="solutionlink" onclick="gotosolution(3,4);">特 点 优 势</a>
        </div>
        <ul id="s3" class="anime" style="width:400%;">
          <li style="width:25%;background:#fafafa url(image/solution_3_1.gif) no-repeat center center"></li>
          <li style="width:25%;background:#fafafa url(image/solution_3_2.gif) no-repeat center center"></li>
          <li style="width:25%;background:#fafafa url(image/solution_3_3.gif) no-repeat center center"></li>
          <li style="width:25%;background:#fafafa url(image/solution_3_4.gif) no-repeat center center"></li>
        </ul>
      </div>
    </div>
    <div class="footer">
		<div class="box">
			<div class="sitemap">
				产品<br />
				<a href="<%=request.getContextPath()%>/cloudsever.do">云主机</a><br />
				<a href="<%=request.getContextPath()%>/cloudstorage.do">云硬盘</a>
			</div>
			<div class="sitemap">
				解决方案<br />
				<a href="<%=request.getContextPath()%>/solution.do">云管理平台</a><br />
				<a href="<%=request.getContextPath()%>/solution.do">云存储</a><br />
				<a href="<%=request.getContextPath()%>/solution.do">云桌面</a>
			</div>
			<div class="sitemap">
				帮助中心<br />
				<a href="<%=request.getContextPath()%>/help.do">常见问题</a><br />
				<a href="<%=request.getContextPath()%>/help.do">账户相关指南</a><br />
				<a href="<%=request.getContextPath()%>/help.do">云主机指南</a>
			</div>
			<div class="sitemap">
				关于我们<br />
				<a href="<%=request.getContextPath()%>/aboutus.do">关于我们</a><br /> 
			</div>
			<div class="sitemap" style="width: 100px;">
				关注我们<br />
				<a href="javascript:void(0);">微信公众号</a><br />
				<img src="<%=request.getContextPath()%>/image/weixin.gif" width="70" height="70" />
			</div>
			<div class="sitemap">
				&nbsp;<br />
				<a href="http://weibo.com/zhicloud" target="_blank">新浪微博</a><br />
				<img src="<%=request.getContextPath()%>/image/weibo.gif" width="70" height="70" />
			</div>
			<div class="hotline">
				<img src="<%=request.getContextPath()%>/image/tel.png" width="30" height="30"
					style="vertical-align: middle" /> 客服热线<br />
				<span style="font-size: 22px; color: #595959;">4000-212-999</span><br />
				<span>客服服务时间：7X24小时</span>
			</div>
			<div class="clear"></div>
			<div class="copyright">
				Copyright &copy; 2014 <a href="http://www.tianfusoftwarepark.com" target="_blank">成都天府软件园有限公司</a>, All rights reserved.
				蜀ICP备11001370号-3
			</div>
		</div> 
	</div>
  </div>  
  <div class="pageright">
    <iframe id="loginiframe" src="<%=request.getContextPath() %>/public/user/login.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
    <iframe id="regiframe" src="<%=request.getContextPath() %>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
  </div> 
</div>
</body>
</html>