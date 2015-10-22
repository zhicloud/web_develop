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
<title>帮助中心 -- 致云 ZhiCloud</title>
<meta name="keywords" content="云产品帮助中心，用户使用帮助，常见问题，致云" />
<meta name="description" content="致云帮助中心是致云用户的网上帮助平台，提供常见问题查询，帮助教程等服务。" />
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
	init(6);
	if(userName!= ''){ 
		inituser(userName,0);
	}else{
		inituser();
	}
	$("#helpnav .h2").click(function(){
		$("#helpnav a").removeClass("active");
		$(this).addClass("active");
	});
	$("#helpnav .h1:eq(0)").click(function(){
		$("#helpnav a").removeClass("active");
		$(this).addClass("active");
	});
	$("#helpnav .h1:eq(1)").click(function(){
		$("#helpnav a").removeClass("active");
		$(this).next(".h2").addClass("active");
	});
	$("#helpnav .h1:eq(2)").click(function(){
		$("#helpnav a").removeClass("active");
		$(this).next(".h2").addClass("active");
	});
});
$(function(){ 
$("#iframehelp").load(function(){
$(this).height($(this).contents().find(".help").height()); 
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
<div class="page">
  <div class="pagewhite">
    <div class="header pageheader">
        <div class="top">
			<a class="logo l" href="<%=request.getContextPath()%>/"> 
			<img src="<%=request.getContextPath()%>/image/logo_big.png" width="153" height="33" alt="致云 ZhiCloud" /></a>
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
				<a href="<%=request.getContextPath()%>/vpcserver.do">专属云</a>
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
    <div class="pagemain">
      <div class="box" style="width:980px; padding:60px 0 0 0">
        <div id="helpnav" class="l" style="width:210px; border-right:solid 1px #e1e1e1;"> 
	        <a href="public/help_1_1.jsp" target="iframehelp" class="h1 active" style="background:url(image/help_faq.png) no-repeat left center;margin-top:0">常见问题</a> 
	        <a href="public/help_2_1.jsp" target="iframehelp" class="h1" style="background:url(image/help_account.png) no-repeat left center">账户相关</a> 
	        <a href="public/help_2_1.jsp" target="iframehelp" class="h2">新用户注册</a> 
	        <a href="public/help_2_2.jsp" target="iframehelp" class="h2">账户充值</a> 
	        <a href="public/help_2_3.jsp" target="iframehelp" class="h2">获取发票</a> 
	        <a href="public/help_2_4.jsp" target="iframehelp" class="h2">现金券兑换</a> 
	        <a href="public/help_2_5.jsp" target="iframehelp" class="h2">修改邮箱</a> 
	        <a href="public/help_2_6.jsp" target="iframehelp" class="h2">修改手机</a> 
	        <a href="public/help_2_7.jsp" target="iframehelp" class="h2">修改密码</a> 
	        <a href="public/help_3_1.jsp" target="iframehelp" class="h1" style="background:url(image/help_server.png) no-repeat left center">云主机</a> 
	        <a href="public/help_3_1.jsp" target="iframehelp" class="h2">创建云主机</a> 
	        <a href="public/help_3_2.jsp" target="iframehelp" class="h2">连接云主机</a> 
	        <a href="public/help_3_3.jsp" target="iframehelp" class="h2">使用数据盘</a> 
	        <a href="public/help_3_4.jsp" target="iframehelp" class="h2">相关初始密码</a> 
	        <a href="public/help_3_5.jsp" target="iframehelp" class="h2">修改密码</a> 
	        <a href="public/help_3_6.jsp" target="iframehelp" class="h2">上传文件</a> 
	        <a href="public/help_3_7.jsp" target="iframehelp" class="h2">更改配置</a> 
	        <a href="public/help_3_8.jsp" target="iframehelp" class="h2">停用/启用</a> 
	        <a href="public/help_3_9.jsp" target="iframehelp" class="h2">资源监控</a> 
	        <a href="icp.do" target="iframehelp" class="h2">ICP备案</a> 
        </div>
        <div class="r" style="width:649px; padding:0 60px 40px 0px;">
        <iframe name="iframehelp" src="public/help_1_1.jsp" id="iframehelp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
        </div>
        <div class="clear"></div>
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
				<a href="<%=request.getContextPath()%>/aboutus.do">关于致云</a><br />
				<a href="<%=request.getContextPath()%>/job.do">加入我们</a><br />
				<a href="<%=request.getContextPath()%>/aboutus.do">联系我们</a>
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
				Copyright &copy; 2014 <a href="http://www.zhicloud.com"
					target="_blank">致云科技有限公司</a>, All rights reserved.
				蜀ICP备14004217号-1
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