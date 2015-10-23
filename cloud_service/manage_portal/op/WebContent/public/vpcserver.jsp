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
<title>专属云  -- 致云 ZhiCloud</title>
<meta name="keywords" content="云主机租用，云服务器租用，秒级创建，弹性扩展，云主机监控，主机管理，安全稳定，弹性计费，多个服务器，致云" />
<meta name="description" content="致云专属云适用于多个服务器组建Web站点或其他应用部署、含有仅需要内网访问的服务器的部署、对访问控制严格、服务器安全要求高的服务器组网部署和搭建多个服务器用于集群或负载均衡等的组网部署等" />
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" /> 
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/global.css"/>
<link rel="stylesheet" href="<%=request.getContextPath() %>/css/exclusiveCloud.css"/>
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
	init(3);
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
  <div class="pagewhite">
    <div class="header">
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
   <section class="main">
		<section class="floor_one">
			<div class="wrap">
				<div class="cont">
					<h3>致云专属云</h3>
					<p>由于安全性和隔离性等方面的考虑，或用户需要将多台云服务器放置在隔离的内网中进行集群作业，从而组合而成的一个带有软路由器和软防火墙的虚拟专属内网。</p>
					<a href="<%=request.getContextPath()%>/bean/page.do?userType=4&bean=vpcService&method=createVpnPage">立即购买</a>
				</div>
			</div>
		</section>
		<section class="floor_two">
			<div class="wrap">
				 <div class="cont">
				 	<h4 class="title">多台云服务器组成一个专有的网络</h4>
				 	<img src="<%=request.getContextPath() %>/image/floor_two_cont.png" alt="多台云服务器组成一个专有的网络"/>
				 </div>
			</div>
		</section>
		<section class="floor_three">
			<div class="wrap">
				<div class="cont">
				 	<h4 class="title">并将其放置在隔离的内网环境中进行集群作业</h4>
				 	<img src="<%=request.getContextPath() %>/image/floor_three_cont.png" alt="并将其放置在隔离的内网环境中进行集群作业"/>
				 </div>
			</div>
		</section>
		<section class="floor_four">
			<div class="wrap">
				<div class="cont">
				 	<h4 class="title">并集成一个由 防火墙 路由 交换机组成的统一网络开关</h4>
				 	<img src="<%=request.getContextPath() %>/image/floor_four_cont.png" alt="并集成一个由 防火墙 路由 交换机组成的统一网络开关"/>
				 </div>
			</div>
		</section>
		<section class="floor_five">
			<div class="wrap">
				<div class="cont">
				 	<h4 class="title">统一网络出口连接外网</h4>
				 	<img src="<%=request.getContextPath() %>/image/floor_five_cont.png" alt="统一网络出口连接外网"/>
				 </div>
			</div>
		</section>
		<section class="floor_six">
			<div class="wrap">
				<div class="cont">
					<h4 class="s_title">产品优势</h4>
					<ul>
						<li>
							<span><img src="<%=request.getContextPath() %>/image/pro_advantage_one.png" alt="安全封闭" /></span>
							<p class="line"></p>
							<h5>安全封闭</h5>
							<p class="c">各个专属云之间完全间隔互补干扰</p>
						</li>
						<li>
							<span><img src="<%=request.getContextPath() %>/image/pro_advantage_two.png" alt="自我掌控" /></span>
							<p class="line"></p>
							<h5>自我掌控</h5>
							<p class="c">自我掌控进出规则的专属虚拟内网</p>
						</li>
						<li>
							<span><img src="<%=request.getContextPath() %>/image/pro_advantage_three.png" alt="简便直观" /></span>
							<p class="line"></p>
							<h5>简便直观</h5>
							<p class="c">简便的专属网络配置<br/>直观的图形化可视操作</p>
						</li>
					</ul>
				</div>
			</div>
		</section>
	</section>
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
